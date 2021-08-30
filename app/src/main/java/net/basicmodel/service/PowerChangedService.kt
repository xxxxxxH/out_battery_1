package net.basicmodel.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.IBinder
import net.basicmodel.event.MessageEvent
import org.greenrobot.eventbus.EventBus

/**
 * Copyright (C) 2021,2021/8/30, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
class PowerChangedService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val intent = IntentFilter()
        intent.addAction(Intent.ACTION_BATTERY_CHANGED)
        intent.addAction(Intent.ACTION_POWER_CONNECTED)
        registerReceiver(receiver, intent)
    }

    private var receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    val rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val percentage = rawLevel.toFloat() / scale.toFloat()
                    val level = (percentage * 100).toInt()
                    EventBus.getDefault().post(MessageEvent("BATTERY_CHANGED",level.toString()))
                }
                Intent.ACTION_POWER_CONNECTED -> {
                    EventBus.getDefault().post(MessageEvent("POWER_CONNECTED"))
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}