package net.basicmodel.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_activity_charge.*
import net.basicmodel.R
import net.basicmodel.utils.Constant
import net.basicmodel.utils.ResourceManager

class ChargeActivity : AppCompatActivity() {
    val defaultUrl = "https://magichua.club/preview/img/bg_1.jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_charge)
        clock.isNight = true
        clock.start()
        val intent = IntentFilter()
        intent.addAction(Intent.ACTION_POWER_DISCONNECTED)
        intent.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(receiver, intent)
        val bgUrl: String = MMKV.defaultMMKV()!!.decodeString(Constant.KEY_BG, defaultUrl)!!
        val animUrl: String = MMKV.defaultMMKV()!!.decodeString(
            Constant.KEY_ANIM,
            ResourceManager.getInstance().getResId2Str(this, R.drawable.anim1)
        )!!
        Glide.with(this)
            .load(animUrl)
            .into(anim)
        Glide.with(this)
            .load(bgUrl)
            .into(bg)
    }

    private var receiver: BroadcastReceiver = object : BroadcastReceiver() {
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
                power.text = "CURRENT POWER: $level%"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}