package com.uttoron

import android.R.attr
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uttoron.model.SubCategory
import com.uttoron.utils.AppConstant
import kotlinx.android.synthetic.main.home_layout.*
import kotlinx.android.synthetic.main.item_other_cat.view.*
import kotlinx.android.synthetic.main.item_soft_skill.view.*
import android.graphics.Bitmap

import com.bumptech.glide.request.target.SimpleTarget

import android.R.attr.data
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.uttoron.asynctask.DownloadFileFromURLTask
import com.uttoron.callback.DownloadListener
import com.uttoron.model.Category

private const val outputDir = "uttoron"
class HomeFragmentOffline : Fragment(){
    private var filename: String = ""
    private var srcUrl: String  = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_layout,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var topCatId = ""
        var topCatName = ""

        for ((index, value) in AppConstant.getCatagories(requireContext()).withIndex()) {
            if(value.sort_order ==1){
                topCatId = value.id.toString()
                topCatName = value.name
            }
        }
        tvTopCatName.text = topCatName
        tvSloganTop.text = AppConstant.getGeneralsettings(requireContext())[0].slogan

        var topcatCatSubCategoryList  = arrayListOf<SubCategory>()


//        softSkillCatList.add(SubCategory(0,"","","",0,"",0,""))
//        softSkillCatList.add(SubCategory(0,"","","",0,"",0,""))
//        softSkillCatList.add(SubCategory(0,"","","",0,"",0,""))
//        softSkillCatList.add(SubCategory(0,"","","",0,"",0,""))

        topcatCatSubCategoryList.clear()

        for ((index, value) in AppConstant.getSubCatagories(requireContext()).withIndex()) {
            if(value.category_name.equals(topCatName)){
                topcatCatSubCategoryList.add(value)
            }
        }

        val layoutManager = GridLayoutManager(context, 4)
        recycleSoftSkill!!.setLayoutManager(layoutManager)
        var usersAdapter = TopCatListListAdapter(topcatCatSubCategoryList, requireContext())
        //recyclerDrotoJogajog!!.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        recycleSoftSkill!!.setAdapter(usersAdapter)



        val layoutManagerOtherCat = GridLayoutManager(context, 2)
        recycleCats!!.setLayoutManager(layoutManagerOtherCat)
        var usersAdapterOtherCat = OtherCatListAdapter(AppConstant.getHome4Catagories(requireContext()) as ArrayList<Category>, requireContext())
        //recyclerDrotoJogajog!!.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        recycleCats!!.setAdapter(usersAdapterOtherCat)

        relGoVidPage.setOnClickListener {
            AppConstant.catName = topCatName
            AppConstant.catID = topCatId
            AppConstant.subCatList = topcatCatSubCategoryList
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, VideoFragmentOffline())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        imgGoFor.setOnClickListener {

            AppConstant.catName = topCatName
            AppConstant.catID = topCatId
            AppConstant.subCatList = topcatCatSubCategoryList
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, VideoFragmentOffline())
            transaction.addToBackStack(null)
            transaction.commit()
        }


//        Glide.with(requireContext())
//            .asBitmap()
//            .load(AppConstant.getGeneralsettings(requireContext())[0].app_logo_icon)
//            .into(object : SimpleTarget<Bitmap?>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                    imgUttorn.setImageBitmap(resource)
//                }
//            })
        imgUttorn.setImageURI(Uri.parse("/sdcard/download/uttoron/app_logo_icon"+".png"))


        imgQuize.setImageURI(Uri.parse("/sdcard/download/uttoron/উত্তরণ কুইজ গেইম"+".png"))

//        Glide.with(requireContext())
//            .asBitmap()
//            .load("http://uttoron.nanoit.biz/public/backend/upload/category/202201020555উত্তরণ কুইজ গেইম.png")
//            .into(object : SimpleTarget<Bitmap?>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                    imgQuize.setImageBitmap(resource)
//                }
//            })



//        val async = DownloadVideoAsyncTask(requireContext())
//        async.execute("http://uttoron.nanoit.biz/public/backend/upload/videos/IE6XuFkM6LZ08awrk8d8G39HjqrxitwqEmTX0sx0.mp4")

       // downloadFile()

    }


    inner class TopCatListListAdapter(var list: ArrayList<SubCategory>, var context: Context) :
        RecyclerView.Adapter<TopCatListListAdapter.UserViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_soft_skill, parent, false)
        )

        override fun getItemCount() = list.size
        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {



            holder.tvCatName.text = list[position].name

            holder.imgTopCat.setImageURI(Uri.parse("/sdcard/download/uttoron/"+list[position].name+".png"))


//            Glide.with(requireContext())
//                .asBitmap()
//                .load(list[position].icon)
//                .into(object : SimpleTarget<Bitmap?>() {
//                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                        holder.imgTopCat.setImageBitmap(resource)
//                    }
//                })

            holder.fullTop.setOnClickListener {

                AppConstant.isHome = true

                AppConstant.subCatName = list[position].name
                AppConstant.catName = list[position].category_name

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, ContentFragmentOffline())
                transaction.addToBackStack(null)
                transaction.commit()

//                AppConstant.catName = list[position].category_name
//                AppConstant.catID = list[position].category_id.toString()
//                AppConstant.subCatList = list
//                val transaction = requireActivity().supportFragmentManager.beginTransaction()
//                transaction.replace(R.id.container, VideoFragment())
//                transaction.addToBackStack(null)
//                transaction.commit()
            }

        }

        inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvCatName = view.tvCatName
            val imgTopCat = view.imgTopCat
            val fullTop = view.fullTop
        }
    }

    inner class OtherCatListAdapter(var dataList: ArrayList<Category>, var context: Context) :
        RecyclerView.Adapter<OtherCatListAdapter.UserViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_other_cat, parent, false)
        )

        override fun getItemCount() = dataList.size
        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

//            if (position == 0){
//                dataList.removeAt(0)
//            }
//
//            if (position == 4){
//                dataList.removeAt(4)
//                //holder.fullViewCat.visibility = View.GONE
//            }


            holder.tvCatNameOther.text = dataList[position].name

            holder.fullViewCat.setOnClickListener {
                AppConstant.catName = dataList[position].name
                AppConstant.catID = dataList[position].id.toString()

                AppConstant.subCatList.clear()
                for ((index, value) in AppConstant.getSubCatagories(requireContext()).withIndex()) {
                    if(value.category_name.equals(dataList[position].name)){
                        AppConstant.subCatList.add(value)
                    }
                }

                //AppConstant.subCatList = dataList[position].
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, VideoFragmentOffline())
                transaction.addToBackStack(null)
                transaction.commit()
            }

//            for ((index, value) in AppConstant.getAllImageBitmap(requireContext()).withIndex()) {
//                Log.e("bitmap","bitmap: "+value.bitmap)
//                if (value.name.equals(dataList[position].name)){
//                    holder.imgOtehrCat.setImageBitmap(value.bitmap)
//                }
//
//            }

            holder.imgOtehrCat.setImageURI(Uri.parse("/sdcard/download/uttoron/"+dataList[position].name+".png"))

//            Glide.with(requireContext())
//                .asBitmap()
//                .load("/sdcard/download/uttoron/"+dataList[position].name)
//                .into(object : SimpleTarget<Bitmap?>() {
//                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                        holder.imgOtehrCat.setImageBitmap(resource)
//                    }
//                })


        }


        inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvCatNameOther = view.tvCatNameOther
            val imgOtehrCat = view.imgOtehrCat
            val fullViewCat = view.fullViewCat

        }
    }


    private fun downloadFile() {

        for ((index, value) in AppConstant.alldata[0].categories.withIndex()) {

            if (value.video !=  null){
                srcUrl = value.video
            }
            if (value.name !=  null){
                filename = value.name+".mp4"
            }

            val download = DownloadFileFromURLTask(requireContext(), outputDir,srcUrl,filename, object :
                DownloadListener {
                override fun onSuccess(path: String) {
                    //toast("File is downloaded successfully at $path")
                }

                override fun onFailure(error: String) {
                    //toast(error)
                }
            })
            download.execute()
        }


    }

//    private fun downloadFile() {
//        val download = DownloadFileFromURLTask(requireContext(), outputDir, object :
//            DownloadListener {
//            override fun onSuccess(path: String) {
//                //toast("File is downloaded successfully at $path")
//            }
//
//            override fun onFailure(error: String) {
//                //toast(error)
//            }
//        })
//        download.execute()
//    }
}