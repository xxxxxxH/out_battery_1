package net.basicmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import net.basicmodel.event.MessageEvent
import net.basicmodel.service.PowerChangedService
import net.basicmodel.utils.Constant
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    val defaultUrl = "https://magichua.club/preview/img/bg_1.jpg"
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        startService(Intent(this, PowerChangedService::class.java))
        requestPermissions()
        initView()
    }

    private fun initView() {
        Glide.with(this).load(defaultUrl).into(bg)
        Glide.with(this).load(R.drawable.anim1).into(anim)
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()
        when (msg[0]) {
            Constant.BATTERY_CHANGED -> {
                percent.text = "CURRENT POWER" + "\n" + msg[1]
            }
            Constant.POWER_CONNECTED -> {

            }
            Constant.TYPE_BG -> {
                Glide.with(this).load(msg[1]).into(bg)
            }
            Constant.TYPE_ANIM -> {
                Glide.with(this).load(msg[1]).into(anim)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun requestPermissions() {
        if (checkPermission(permissions[0]) && checkPermission(permissions[1])) {

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
                } else {
                    Log.i("xxxxxxH", "获取权限成功")
                }
            }
        }
    }
}