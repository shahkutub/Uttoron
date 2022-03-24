package com.uttoron

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.uttoron.model.SubCategory
import com.uttoron.utils.AppConstant
import kotlinx.android.synthetic.main.content_layout.*
import android.os.Build
import android.text.Html
import com.uttoron.utils.NetInfo


class ContentFragmentOffline : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_layout,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        tvTitle.text = AppConstant.subCatName
        if (NetInfo.isOnline(context)){
           // tvSloganTop.text = AppConstant.getGeneralsettings(requireContext())[0].slogan

            for ((index, value) in AppConstant.getContent(requireContext()).withIndex()) {
                if (AppConstant.subCatName.equals(value.sub_category_name)){
                    tvContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(value.content, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(value.content)
                    }
                }
            }

        }else{
            //tvSloganTop.text = AppConstant.getGeneralsettings(requireContext())[0].slogan

            for ((index, value) in AppConstant.getContent(context).withIndex()) {

                if (AppConstant.subCatName.equals(value.sub_category_name)){
                    tvContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(value.content, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(value.content)
                    }
                }
            }

        }

        imgBack.setOnClickListener {
            requireActivity().onBackPressed()
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            if (AppConstant.isHome){
//                    transaction.replace(R.id.container, HomeFragmentOffline())
//            }else{
//                transaction.replace(R.id.container, VideoFragmentOffline())
//            }
//            //transaction.replace(R.id.container, VideoFragment())
//            transaction.addToBackStack(null)
//            transaction.commit()
        }

        //imgUttornContent.setImageURI(Uri.parse("/sdcard/download/uttoron/app_logo_icon"+".png"))

    }

}