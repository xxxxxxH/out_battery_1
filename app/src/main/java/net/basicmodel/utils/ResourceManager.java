package net.basicmodel.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import net.basicmodel.R;

import java.util.ArrayList;

/**
 * Copyright (C) 2021,2021/8/30, a Tencent company. All rights reserved.
 * <p>
 * User : v_xhangxie
 * <p>
 * Desc :
 */
public class ResourceManager {

    private final String bg_part1 = "https:magichua.club/preview/img/bg_";
    private final String bg_part2 = ".jpg";

    private static ResourceManager instance = null;

    private ResourceManager() {
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public ArrayList<String> getBackgroundRes() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            String url = bg_part1 + i + bg_part2;
            result.add(url);
        }
        return result;
    }

    public ArrayList<String> getAnimRes(Context context) {
        ArrayList<String> result = new ArrayList<>();
        result.add(getResId2Str(context, R.drawable.anim1));
        result.add(getResId2Str(context, R.drawable.anim3));
        result.add(getResId2Str(context, R.drawable.anim4));
        result.add(getResId2Str(context, R.drawable.anim7));
        result.add(getResId2Str(context, R.drawable.anim8));
        result.add(getResId2Str(context, R.drawable.anim10));
        result.add(getResId2Str(context, R.drawable.anim11));
        result.add(getResId2Str(context, R.drawable.anim13));
        result.add(getResId2Str(context, R.drawable.anim14));
        result.add(getResId2Str(context, R.drawable.anim18));
        result.add(getResId2Str(context, R.drawable.anim20));
        result.add(getResId2Str(context, R.drawable.anim22));
        result.add(getResId2Str(context, R.drawable.anim23));
        result.add(getResId2Str(context, R.drawable.anim24));
        result.add(getResId2Str(context, R.drawable.anim25));
        result.add(getResId2Str(context, R.drawable.anim26));
        result.add(getResId2Str(context, R.drawable.anim27));
        result.add(getResId2Str(context, R.drawable.anim28));
        result.add(getResId2Str(context, R.drawable.gif_1));
        result.add(getResId2Str(context, R.drawable.gif_2));
        result.add(getResId2Str(context, R.drawable.gif_3));
        result.add(getResId2Str(context, R.drawable.gif_5));
        result.add(getResId2Str(context, R.drawable.gif_6));
        result.add(getResId2Str(context, R.drawable.gif_7));
        result.add(getResId2Str(context, R.drawable.gif_17));
        result.add(getResId2Str(context, R.drawable.gif_18));
        result.add(getResId2Str(context, R.drawable.gif_19));
        result.add(getResId2Str(context, R.drawable.gif_21));
        result.add(getResId2Str(context, R.drawable.gif_23));
        result.add(getResId2Str(context, R.drawable.gif_24));
        result.add(getResId2Str(context, R.drawable.gif_26));
        result.add(getResId2Str(context, R.drawable.gif_31));
        result.add(getResId2Str(context, R.drawable.gif_32));
        result.add(getResId2Str(context, R.drawable.gif_33));
        result.add(getResId2Str(context, R.drawable.gif_34));
        return result;
    }

    public String getResId2Str(Context context, int id) {
        Resources resources = context.getResources();
        Uri url = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id));
        return url.toString();
    }

}
