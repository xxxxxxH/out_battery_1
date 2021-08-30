package net.basicmodel.activity

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.layout_activity_.*
import net.basicmodel.R
import net.basicmodel.adapter.MyAdapter
import net.basicmodel.event.MessageEvent
import net.basicmodel.utils.Constant
import net.basicmodel.utils.OnItemClickListener
import net.basicmodel.utils.ResourceManager
import org.greenrobot.eventbus.EventBus

/**
 * Copyright (C) 2021,2021/8/30, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
class SelectActivity : AppCompatActivity(), OnItemClickListener {
    var type = ""
    var data: ArrayList<String>? = null
    var adapter: MyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_)
        initData()
        initView()
    }

    private fun initData() {
        val intent = intent
        type = intent.getStringExtra(Constant.TYPE) as String
        if (TextUtils.equals(type, Constant.TYPE_BG))
            data = ResourceManager.getInstance().backgroundRes
        if (TextUtils.equals(type, Constant.TYPE_ANIM))
            data = ResourceManager.getInstance().getAnimRes(this)
    }

    private fun initView() {
        adapter = MyAdapter(data, data, this, this, this, type)
        var spanCount = 0
        if (TextUtils.equals(type, Constant.TYPE_BG))
            spanCount = 2
        if (TextUtils.equals(type, Constant.TYPE_ANIM))
            spanCount = 3
        recycler.layoutManager = GridLayoutManager(this, spanCount)
        recycler.adapter = adapter
    }

    override fun onItemClick(position: Int, type: String) {
        EventBus.getDefault().post(MessageEvent(type, data!![position]))
        finish()
    }
}