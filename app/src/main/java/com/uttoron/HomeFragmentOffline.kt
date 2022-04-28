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
import kotlinx.android.synthetic.main.home_layout_new_design.*
import kotlinx.android.synthetic.main.item_other_cat.view.*
import kotlinx.android.synthetic.main.item_soft_skill.view.*
import android.graphics.Bitmap

import com.bumptech.glide.request.target.SimpleTarget

import android.R.attr.data
import android.icu.text.IDNA
import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.uttoron.asynctask.DownloadFileFromURLTask
import com.uttoron.callback.DownloadListener
import com.uttoron.model.Category
import com.uttoron.utils.PersistData
import kotlinx.android.synthetic.main.content_layout.*
import kotlinx.android.synthetic.main.home_layout_new_design.imgUttorn
import kotlinx.android.synthetic.main.home_layout_new_design.topbar

private const val outputDir = "uttoron"
class HomeFragmentOffline : Fragment(){
    private var filename: String = ""
    private var srcUrl: String  = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_layout_new_design,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //AppConstant.isHome = true
        var topCatId = ""
        var topCatName = ""

        for ((index, value) in AppConstant.getCatagories(requireContext()).withIndex()) {
            if(value.sort_order ==1){
                topCatId = value.id.toString()
                topCatName = value.name
            }
        }

        tvSoftSkill.text = topCatName
        //tvTopCatName.text = topCatName
        //tvSloganTop.text = AppConstant.getGeneralsettings(requireContext())[0].slogan

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

        topcatCatSubCategoryList.sortBy { it.sort_order }


        topcatCatSubCategoryList.forEachIndexed { index, subCategory ->
            Log.e("sort_order","sort_order: "+subCategory.sort_order)
            Log.e("sort_order","sort_order: "+subCategory.name)
        }


        val layoutManager = GridLayoutManager(context, 2)
        recycleSoftSkill!!.setLayoutManager(layoutManager)
        var usersAdapter = TopCatListListAdapter(topcatCatSubCategoryList, requireContext())
        //recyclerDrotoJogajog!!.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        recycleSoftSkill!!.setAdapter(usersAdapter)



        AppConstant.getHome4Catagories(requireContext()).sortBy { it.sort_order }

        AppConstant.getHome4Catagories(requireContext()).forEachIndexed { index, subCategory ->
            Log.e("sort_order","sort_order: "+subCategory.sort_order)
            Log.e("sort_order","sort_order: "+subCategory.name)
        }

        // OtherCatListAdapter
        val layoutManagerOtherCat = GridLayoutManager(context, 2)
        layoutManagerOtherCat.orientation = LinearLayoutManager.VERTICAL
        recycleCats!!.setLayoutManager(layoutManagerOtherCat)
        var usersAdapterOtherCat = OtherCatListAdapter(AppConstant.getHome4Catagories(requireContext()) as ArrayList<Category>, requireContext())
        //recyclerDrotoJogajog!!.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        recycleCats!!.setAdapter(usersAdapterOtherCat)


        //for last odd item full width

        val num = AppConstant.getHome4Catagories(requireContext()).size
        if (num % 2 == 0){
            println("$num is even")
        }else{
            layoutManagerOtherCat.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (usersAdapterOtherCat != null) {
                        when (usersAdapterOtherCat.getItemViewType(position)) {
                            1 -> 1
                            0 -> 2 //number of columns of the grid
                            else -> -1
                        }
                    } else {
                        -1
                    }
                }
            }
        }
        //if (AppConstant.getHome4Catagories.size = )




        topbar.setOnClickListener {
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.container, InfoFragment())
//            transaction.addToBackStack(null)
//            transaction.commit()
        }

        relGoVidPage.setOnClickListener {
            AppConstant.catName = topCatName
            AppConstant.catID = topCatId
            AppConstant.subCatList = topcatCatSubCategoryList
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, VideoFragmentOfflineSoftSkill())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        goQuize.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, QuizeFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

//        imgGoFor.setOnClickListener {
//
//            AppConstant.catName = topCatName
//            AppConstant.catID = topCatId
//            AppConstant.subCatList = topcatCatSubCategoryList
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.container, VideoFragmentOffline())
//            transaction.addToBackStack(null)
//            transaction.commit()
//        }


//        Glide.with(requireContext())
//            .asBitmap()
//            .load(AppConstant.getGeneralsettings(requireContext())[0].app_logo_icon)
//            .into(object : SimpleTarget<Bitmap?>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                    imgUttorn.setImageBitmap(resource)
//                }
//            })
       // imgUttorn.setImageURI(Uri.parse("/sdcard/download/uttoron/app_logo_icon"+".png"))
        imgUttorn.setImageResource(R.drawable.app_logo_icon)
        imgQuize.setImageResource(R.drawable.quize)


        //imgQuize.setImageURI(Uri.parse("/sdcard/download/uttoron/উত্তরণ কুইজ গেইম"+".png"))

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


//        for ((index, value) in AppConstant.alldata[0].sub_categories.withIndex()) {
//
//        }

        try {
           // downloadPdfFile()
        }catch (e:Exception){

        }

    }


    inner class TopCatListListAdapter(var list: ArrayList<SubCategory>, var context: Context) :
        RecyclerView.Adapter<TopCatListListAdapter.UserViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_soft_skill, parent, false)
        )

        override fun getItemCount() = list.size
        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {




           // if(AppConstant.oldTrackNo == PersistData.getIntData(context,AppConstant.currentTrackNumber)){
                if (list[position].name.equals("কর্মক্ষেত্রে যোগাযোগ")){
                    holder.tvCatName.text = "কর্মক্ষেত্রে \n যোগাযোগ"
                    holder.imgTopCat.setImageResource(R.drawable.jogajog)
                }

                if (list[position].name.equals("সমস্যা সমাধানের উপায়")){
                    holder.tvCatName.text = "সমস্যা সমাধানের \n উপায়"
                    holder.imgTopCat.setImageResource(R.drawable.dokkta)
                }

            if (list[position].name.equals("সঠিক সিদ্ধান্ত গ্রহণ")){
                holder.tvCatName.text = "সঠিক সিদ্ধান্ত \n গ্রহণ"
                holder.imgTopCat.setImageResource(R.drawable.sothiksiddanto)
            }

            if (list[position].name.equals("সময় ব্যবস্থাপনা")){
                holder.tvCatName.text = "সময় \n ব্যবস্থাপনা"
                holder.imgTopCat.setImageResource(R.drawable.somoibebostha)
            }


           // }

//            if(AppConstant.oldTrackNo < PersistData.getIntData(context,AppConstant.currentTrackNumber)){
//                holder.tvCatName.text = list[position].name
//
//                holder.imgTopCat.setImageURI(Uri.parse("/sdcard/download/uttoron/"+list[position].name+".png"))
//
//            }


//            if (list[position].name.equals("সঠিক সিদ্ধান্ত গ্রহণ")){
//                holder.imgTopCat.setImageResource(R.drawable.sothiksiddanto)
//            }

//            Glide.with(requireContext())
//                .asBitmap()
//                .load(list[position].icon)
//                .into(object : SimpleTarget<Bitmap?>() {
//                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                        holder.imgTopCat.setImageBitmap(resource)
//                    }
//                })

            holder.fullTop.setOnClickListener {

                AppConstant.isHome = false

                AppConstant.subCatName = list[position].name
                AppConstant.subCatId = list[position].id.toString()

                AppConstant.getContent(requireContext()).forEachIndexed { index, content ->
                    if (content.content != null){

                        if (content.sub_category_id != null ){
                            if (content.sub_category_id == list[position].id){
                                AppConstant.content = content.content
                                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.container, ContentFragmentOffline())
                                transaction.addToBackStack(null)
                                transaction.commit()
                            }
                        }

                    }
                }
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

        override fun getItemViewType(position: Int): Int {
            return if (position == getItemCount() - 1) 0 else 1 // If the item is last, `itemViewType` will be 0
        }
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

            if (dataList[position].name.equals("চাকুরীর সাধারণ নিয়ম কানুন")){
                holder.imgOtehrCat.setImageResource(R.drawable.chari_niom)
            }
            if (dataList[position].name.equals("চাকুরীতে উন্নয়নের উপায়")){
                holder.imgOtehrCat.setImageResource(R.drawable.chakri_unnoun)
            }
            if (dataList[position].name.equals("আর্থিক ব্যবস্থাপনা ও পরিকল্পনা")){
                holder.imgOtehrCat.setImageResource(R.drawable.arthik_bebosthapona)
            }
            if (dataList[position].name.equals("স্বাস্থ্য ও সুরক্ষা")){
                holder.imgOtehrCat.setImageResource(R.drawable.sasthonew)
            }


//            for ((index, value) in AppConstant.getAllImageBitmap(requireContext()).withIndex()) {
//                Log.e("bitmap","bitmap: "+value.bitmap)
//                if (value.name.equals(dataList[position].name)){
//                    holder.imgOtehrCat.setImageBitmap(value.bitmap)
//                }
//
//            }

            //holder.imgOtehrCat.setImageURI(Uri.parse("/sdcard/download/uttoron/"+dataList[position].name+".png"))

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

    private fun downloadPdfFile() {


        for ((index, value) in AppConstant.getContent(requireContext()).withIndex()) {

            if (value.video !=  null){
                srcUrl = value.video
            }
            if (value.sub_category !=  null){
                filename = value.sub_category.name+".pdf"
            }
            if (value.sub_category ==  null){
                filename = value.category_name+".pdf"
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