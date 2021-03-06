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
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.IDNA
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.uttoron.asynctask.DownloadFileFromURLTask
import com.uttoron.callback.DownloadListener
import com.uttoron.model.Category
import com.uttoron.utils.NetInfo
import com.uttoron.utils.PersistData
import kotlinx.android.synthetic.main.content_layout.*
import kotlinx.android.synthetic.main.home_layout_new_design.imgUttorn
import kotlinx.android.synthetic.main.home_layout_new_design.topbar
import org.jsoup.Jsoup
import java.io.IOException

private const val outputDir = "uttoron"
class HomeFragmentOffline : Fragment(){
    private var filename: String = ""
    private var srcUrl: String  = ""
    var currentVersion: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_layout_new_design,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //AppConstant.isHome = true

        var mediaPlayer = MediaPlayer()
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop()
        }

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
            transaction.add(R.id.container, VideoFragmentOfflineSoftSkill())
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


        //imgQuize.setImageURI(Uri.parse("/sdcard/download/uttoron/?????????????????? ???????????? ????????????"+".png"))

//        Glide.with(requireContext())
//            .asBitmap()
//            .load("http://uttoron.nanoit.biz/public/backend/upload/category/202201020555?????????????????? ???????????? ????????????.png")
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

//        try {
//            if(NetInfo.isOnline(requireContext())){
//                downloadPdfFile()
//            }
//
//        }catch (e:Exception){
//
//        }

        //appUpdate()
    }


    inner class TopCatListListAdapter(var list: ArrayList<SubCategory>, var context: Context) :
        RecyclerView.Adapter<TopCatListListAdapter.UserViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_soft_skill, parent, false)
        )

        override fun getItemCount() = list.size
        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {




           // if(AppConstant.oldTrackNo == PersistData.getIntData(context,AppConstant.currentTrackNumber)){
                if (list[position].name.equals("???????????????????????????????????? ?????????????????????")){
                    holder.tvCatName.text = "???????????????????????????????????? \n ?????????????????????"
                    holder.imgTopCat.setImageResource(R.drawable.jogajog)
                }

                if (list[position].name.equals("?????????????????? ???????????????????????? ????????????")){
                    holder.tvCatName.text = "?????????????????? ???????????????????????? \n ????????????"
                    holder.imgTopCat.setImageResource(R.drawable.sothiksiddanto)
                }

            if (list[position].name.equals("???????????? ?????????")){
                holder.tvCatName.text = "???????????? ?????????"
                holder.imgTopCat.setImageResource(R.drawable.dokkta)
            }

            if (list[position].name.equals("????????? ?????????????????????????????????")){
                holder.tvCatName.text = "????????? \n ?????????????????????????????????"
                holder.imgTopCat.setImageResource(R.drawable.somoibebostha)
            }


           // }

//            if(AppConstant.oldTrackNo < PersistData.getIntData(context,AppConstant.currentTrackNumber)){
//                holder.tvCatName.text = list[position].name
//
//                holder.imgTopCat.setImageURI(Uri.parse("/sdcard/download/uttoron/"+list[position].name+".png"))
//
//            }


//            if (list[position].name.equals("???????????? ??????????????????????????? ???????????????")){
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

                AppConstant.isHome = true

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
                transaction.add(R.id.container, VideoFragmentOffline(),"VidFragment")
                //transaction.add(detailFragment, "detail")
                transaction.addToBackStack(null)
                transaction.commit()
            }

            if (dataList[position].name.equals("????????????????????? ?????????????????? ???????????? ???????????????")){
                holder.imgOtehrCat.setImageResource(R.drawable.chari_niom)
            }
            if (dataList[position].name.equals("???????????????????????? ???????????????????????? ????????????")){
                holder.imgOtehrCat.setImageResource(R.drawable.chakri_unnoun)
            }
            if (dataList[position].name.equals("?????????????????? ????????????????????????????????? ??? ???????????????????????????")){
                holder.imgOtehrCat.setImageResource(R.drawable.arthik_bebosthapona)
            }
            if (dataList[position].name.equals("??????????????????????????? ??? ?????????????????????")){
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


        for ((index, value) in AppConstant.getContent(context).withIndex()) {

            if (value.content !=  null){
                srcUrl = value.content
            }
            if(srcUrl.contains(".pdf")){
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

    private fun appUpdate() {
        //Toast.makeText(getBaseContext(),"App update", Toast.LENGTH_SHORT).show();
        try {
            currentVersion = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        Toast.makeText(requireActivity().getBaseContext(),"currentVersion"+currentVersion, Toast.LENGTH_SHORT).show();

        CheckIsUpdateReady(
            //"https://play.google.com/store/apps/details?id=$packageName&hl=en",
            "https://play.google.com/store/apps/details?id=com.uttoron",
            object : UrlResponce() {
                override fun onReceived(resposeStr: String?) {
                    Toast.makeText(requireActivity().getBaseContext(),"resposeStr: "+resposeStr, Toast.LENGTH_SHORT).show();

                    //resposeStr = "6.0.0";
                    if (!currentVersion.equals(
                            resposeStr,
                            ignoreCase = true
                        ) && null != resposeStr
                    ) {
                        //show dialog
                        //Toast.makeText(this, "Update App", Toast.LENGTH_SHORT).show();
                        val builder = AlertDialog.Builder(
                            context!!
                        )
                        builder.setTitle(R.string.app_update_available)
                            .setMessage(R.string.app_update_text)
                            .setPositiveButton(android.R.string.yes,
                                DialogInterface.OnClickListener { dialog, which -> // continue to play store
                                    val appPackageName =
                                        context!!.packageName // getPackageName() from Context or Activity object
                                    try {
                                        context!!.startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                //Uri.parse("market://details?id=$appPackageName")
                                                Uri.parse("market://details?id=com.uttoron")
                                            )
                                        )
                                    } catch (anfe: ActivityNotFoundException) {
                                        context!!.startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                //Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                                Uri.parse("https://play.google.com/store/apps/details?id=com.uttoron")
                                            )
                                        )
                                    }
                                })
                            .setNegativeButton(android.R.string.no,
                                DialogInterface.OnClickListener { dialog, which ->
                                    // do nothing
                                })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show()
                    }
                }
            }).execute()

    }


    abstract class UrlResponce {
        abstract fun onReceived(resposeStr: String?)
    }

    class CheckIsUpdateReady(appURL: String, callback: UrlResponce) :
        AsyncTask<Void?, String?, String?>() {
        var appURL = ""
        private val mUrlResponce: UrlResponce
        override fun doInBackground(vararg params: Void?): String? {
            var newVersion: String? = null
            try {
                val document = Jsoup.connect(appURL)
                    .timeout(20000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                if (document != null) {
                    val element = document.getElementsContainingOwnText("Current Version")
                    for (ele in element) {
                        if (ele.siblingElements() != null) {
                            val sibElemets = ele.siblingElements()
                            for (sibElemet in sibElemets) {
                                newVersion = sibElemet.text()
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return newVersion
        }

        override fun onPostExecute(onlineVersion: String?) {
            super.onPostExecute(onlineVersion)
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                mUrlResponce.onReceived(onlineVersion)
            }
            Log.d("update", " playstore App version $onlineVersion")
        }

        init {
            this.appURL = appURL
            mUrlResponce = callback
        }


    }

}