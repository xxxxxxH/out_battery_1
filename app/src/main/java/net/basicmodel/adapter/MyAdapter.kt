package net.basicmodel.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_item.view.*
import net.basicmodel.R
import net.basicmodel.utils.Constant
import net.basicmodel.utils.OnItemClickListener
import net.basicmodel.utils.ScreenUtils

/**
 * Copyright (C) 2021,2021/8/30, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
class MyAdapter(
    val bg: ArrayList<String>?,
    val anim: ArrayList<String>?,
    val context:Context,
    val activity:Activity,
    val listener: OnItemClickListener,
    val type: String
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.layout_item, null) as View)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(type){
            Constant.TYPE_BG -> {
                with(holder.itemView){
                    val params = root.layoutParams
                    params.width = ScreenUtils.getScreenSize(activity)[1] / 2
                    root.layoutParams = params

                    val params1 = item_img.layoutParams
                    params1.width = ScreenUtils.getScreenSize(activity)[1] / 2 - 20
                    item_img.layoutParams = params1

                    Glide.with(context).load(bg!![position]).into(item_img)

                    item_img.setOnClickListener {
                        listener.onItemClick(holder.layoutPosition,type)
                    }
                }

            }
            Constant.TYPE_ANIM -> {
                with(holder.itemView){
                    val params = root.layoutParams
                    params.width = ScreenUtils.getScreenSize(activity)[1] / 3
                    root.layoutParams = params

                    val params1 = item_img.layoutParams
                    params1.width = ScreenUtils.getScreenSize(activity)[1] / 3 - 20
                    params1.height = ScreenUtils.getScreenSize(activity)[1] / 3 - 20
                    item_img.layoutParams = params1

                    item_img.setOnClickListener {
                        listener.onItemClick(holder.layoutPosition,type)
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        var size = 0
        if (bg != null)
            size = bg.size
        if (anim != null)
            size = anim.size
        return size
    }
}