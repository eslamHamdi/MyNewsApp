package com.example.mynews.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mynews.R

@BindingAdapter("showImage")
fun showImage(image: ImageView, imgUrl:String?)
{
    imgUrl?.let {

        Glide.with(image).load(imgUrl)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(image)
    }

    @BindingAdapter("viewVisibility")
    fun showOrHide(view: View,boolean: Boolean)
    {
        if (boolean)
        {
            view.visibility = View.VISIBLE
        }else
        {
            view.visibility = View.GONE
        }
    }

}