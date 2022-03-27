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
import android.text.TextUtils
import android.view.MenuItem
import com.uttoron.utils.NetInfo
import com.uttoron.utils.PersistData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_layout.navigationView


class InfoFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_layout,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        if (!TextUtils.isEmpty(AppConstant.infoPagename)){
            tvTitle.text = AppConstant.infoPagename.toString()
        }

       // imgBack.visibility = View.GONE
        vidView.visibility = View.GONE

        //if (AppConstant.)

        AppConstant.getContent(context).forEachIndexed { index, content ->

                if (content.category_name.equals("সফটস্কিল")){
                    if (content.sub_category_id ==null){
                        if (content.content !=null){
                            tvContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Html.fromHtml(content.content, Html.FROM_HTML_MODE_COMPACT)
                            } else {
                                Html.fromHtml(content.content)
                            }
                        }
                    }

                }

        }



        imgBack.setOnClickListener {
            //return@setOnNavigationItemSelectedListener true
           //requireActivity().navigationView.getMenu().getItem(0).setChecked(true)

//           var  item = requireActivity().navigationView.getMenu().findItem(R.id.navigation_home)
//            item.setChecked(true)

            requireActivity().onBackPressed()
            //requireActivity().navigationView.getMenu().getItem(0).setChecked(true)

        }



    }

}