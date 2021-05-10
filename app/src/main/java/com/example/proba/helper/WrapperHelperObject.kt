package com.example.proba.helper

import android.content.Context
import android.graphics.drawable.Drawable

object WrapperHelperObject {

    fun getDrawableResursFromString(abbr : String, context : Context):Drawable{
        var id = "@drawable/ic_"+abbr
        var img_res = context.resources.getIdentifier(id,null,context.packageName)
        return context.resources.getDrawable(img_res)
    }
}