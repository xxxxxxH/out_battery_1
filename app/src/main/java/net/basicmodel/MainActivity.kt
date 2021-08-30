package net.basicmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_main.*
import net.basicmodel.activity.ChargeActivity
import net.basicmodel.activity.SelectActivity
import net.basicmodel.event.MessageEvent
import net.basicmodel.service.PowerChangedService
import net.basicmodel.utils.Constant
import net.basicmodel.utils.GlideEngine
import net.basicmodel.utils.ResourceManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val defaultUrl = "https://magichua.club/preview/img/bg_1.jpg"
    var dialog: Dialog? = null
    var btnBg: TextView? = null
    var btnAnim: TextView? = null
    var btnPhoto: TextView? = null
    var btnCancel: TextView? = null
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        MMKV.initialize(this)
        startService(Intent(this, PowerChangedService::class.java))
        requestPermissions()
        val intent = IntentFilter()
        intent.addAction(Intent.ACTION_POWER_DISCONNECTED)
        intent.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(mBroadcastReceiver, intent)
    }

    private fun initView() {
        val bgUrl: String = MMKV.defaultMMKV()!!.decodeString(Constant.KEY_BG, defaultUrl)!!
        val animUrl: String = MMKV.defaultMMKV()!!.decodeString(
            Constant.KEY_ANIM,
            ResourceManager.getInstance().getResId2Str(this, R.drawable.anim1)
        )!!
        Glide.with(this).load(bgUrl).into(bg)
        Glide.with(this).load(animUrl).into(anim)
        btn.setOnClickListener {
            if (dialog == null)
                dialog = createDlg()
            dialog!!.show()
        }
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()
        when (msg[0]) {
            Constant.BATTERY_CHANGED -> {
                percent.text = "CURRENT POWER" + "\n" + msg[1] + "%"
            }
            Constant.POWER_CONNECTED -> {
                val intent = Intent(this, ChargeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            Constant.TYPE_BG -> {
                val bgUrl = msg[1].toString()
                Glide.with(this).load(bgUrl).into(bg)
                MMKV.defaultMMKV()!!.encode(Constant.KEY_BG, bgUrl)
            }
            Constant.TYPE_ANIM -> {
                val animUrl = msg[1].toString()
                Glide.with(this).load(animUrl).into(anim)
                MMKV.defaultMMKV()!!.encode(Constant.KEY_ANIM, animUrl)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        unregisterReceiver(mBroadcastReceiver)
    }

    private fun openGallery() {
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine()).forResult(
                PictureConfig.CHOOSE_REQUEST
            )
    }

    private fun createDlg(): AlertDialog {
        val dlg = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_select, null)
        dlg.setView(view)
        btnBg = view.findViewById(R.id.btn1)
        btnPhoto = view.findViewById(R.id.btn2)
        btnAnim = view.findViewById(R.id.btn3)
        btnCancel = view.findViewById(R.id.btn4)
        btnBg!!.setOnClickListener(this)
        btnAnim!!.setOnClickListener(this)
        btnPhoto!!.setOnClickListener(this)
        btnCancel!!.setOnClickListener(this)
        return dlg.create()
    }

    private var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (Intent.ACTION_POWER_DISCONNECTED == action) {
            } else if (Intent.ACTION_BATTERY_CHANGED == action) {
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                // get the battery level
                val rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val percentage = rawlevel.toFloat() / scale.toFloat()
                val level = (percentage * 100).toInt()
                percent.text = "CURRENT POWER\n$level%"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val data: List<LocalMedia> = PictureSelector.obtainMultipleResult(data)
            val result = data[0].path
            MMKV.defaultMMKV()!!.encode(Constant.KEY_BG, result)
            GlideEngine.createGlideEngine().loadImage(this, result, bg)
        }
    }


    private fun requestPermissions() {
        if (checkPermission(permissions[0]) && checkPermission(permissions[1])) {
            initView()
        } else {
            ActivityCompat.requestPermissions(this, permissions, 321)
        }
    }

    private fun checkPermission(per: String): Boolean {
        return ContextCompat.checkSelfPermission(this, per) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish()
                } else {
                    Log.i("xxxxxxH", "获取权限成功")
                    initView()
                }
            }
        }
    }

    private fun openSelectActivity(type: String) {
        val intent = Intent(this, SelectActivity::class.java)
        intent.putExtra(Constant.TYPE, type)
        startActivity(intent)
    }

    private fun closeDlg() {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnBg -> {
                openSelectActivity(Constant.TYPE_BG)
            }
            btnAnim -> {
                openSelectActivity(Constant.TYPE_ANIM)
            }
            btnPhoto -> {
                openGallery()
            }
            btnCancel -> {
                closeDlg()
            }
        }
        closeDlg()
    }
}