package com.uttoron

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kaopiz.kprogresshud.KProgressHUD
import com.uttoron.asynctask.DownloadFileFromURLTask
import com.uttoron.callback.DownloadListener
import com.uttoron.model.AllDataResponse
import com.uttoron.model.Category
import com.uttoron.utils.AppConstant
import com.uttoron.utils.NetInfo
import gov.bd.mpportal.utils.ApiKt
import gov.bd.mpportal.utils.baseUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.ArrayList
import android.provider.MediaStore

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import java.lang.Exception
import android.content.IntentSender.SendIntentException

import androidx.core.app.ActivityCompat.startIntentSenderForResult

import android.app.PendingIntent
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uttoron.asynctask.DownloadImageFileFromURLTask
import com.uttoron.model.AllDataResponseItem
import com.uttoron.model.ImageBmpModel
import com.uttoron.utils.PersistData
import java.io.IOException


private const val outputDir = "uttoron"
class SplashActivity : AppCompatActivity() {
    private var filename: String = ""
    private var srcUrl: String  = ""

    /**
     * Duration of wait
     */
    private val SPLASH_DISPLAY_LENGTH = 3000

    /**
     * Called when the activity is first created.
     */

    var context : Context ? = null
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        setContentView(R.layout.activity_splash)
        context = this

        val jsonFileString = getJsonDataFromAsset(applicationContext, "all_data.json")
        Log.e("data", jsonFileString.toString())

        val gson = Gson()
        val listPersonType = object : TypeToken<List<AllDataResponseItem>>() {}.type
        var alldata: List<AllDataResponseItem> = gson.fromJson(jsonFileString, listPersonType)
        alldata.forEachIndexed { idx, person -> Log.e("data", "> Item $idx:\n$alldata") }

        AppConstant.oldTrackNo = alldata[0].track_no

            if(PersistData.getIntData(context,AppConstant.currentTrackNumber) == null){
            PersistData.setIntData(context,AppConstant.currentTrackNumber,alldata[0].track_no)
        }



        AppConstant.getContent(context).clear()
        AppConstant.getCatagories(context).clear()
        AppConstant.getGeneralsettings(context).clear()
        AppConstant.getSubCatagories(context).clear()

        AppConstant.saveCatagories(applicationContext,alldata[0].categories)
        AppConstant.saveSubCatagories(applicationContext,alldata[0].sub_categories)
        AppConstant.saveContent(applicationContext,alldata[0].contents)
        AppConstant.saveGeneralsettings(applicationContext,alldata[0].general_settings)



        AppConstant.home4Cat.clear()
        AppConstant.home4Cat = alldata[0].categories as ArrayList<Category>
        AppConstant.home4Cat.removeAt(0)
        AppConstant.home4Cat.removeAt(4)
        AppConstant.getHome4Catagories(context).clear()
        AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)



        initUi()

        //getAllData()

//        if (NetInfo.isOnline(applicationContext)){
//            deleteDirectory(File("/sdcard/download/uttoron"))
//            getAllData()
//        }else{
//            if (AppConstant.getCatagories(applicationContext).size>0){
//                initUi()
//            }else{
//                Toast.makeText(applicationContext,"No data found.Please check your internet/data connection",Toast.LENGTH_SHORT).show()
//                finish()
//            }
//
//        }

//        imgForword.setOnClickListener {
//            if (NetInfo.isOnline(applicationContext)){
//                if(AppConstant.alldata != null){
//                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//                    finish()
//                }else{
//                    Toast.makeText(applicationContext,"No data found.Please check your internet/data connection",Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//
//            }else{
//                if (AppConstant.getCatagories(applicationContext).size>0){
//                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//                    finish()
//                }else{
//                    Toast.makeText(applicationContext,"No data found.Please check your internet/data connection",Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//
//            }
//        }

//        val file = File("")
//        val deleted: Boolean = file.delete()

       // initUi()


   }


    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun initUi() {



        //tvSlogan.visibility = View.VISIBLE
        //imgForword.visibility = View.VISIBLE
        logo_image.visibility = View.VISIBLE

        logo_image.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val anim = ValueAnimator.ofFloat(1f, 1.5f)
        anim.duration = 1200
        anim.addUpdateListener { animation ->
            logo_image.setScaleX(animation.animatedValue as Float)
            logo_image.setScaleY(animation.animatedValue as Float)
        }
        anim.repeatCount = 1
        anim.repeatMode = ValueAnimator.REVERSE
        anim.start()

        val animMove = AnimationUtils.loadAnimation(applicationContext, R.anim.move)
        //tvSlogan.startAnimation(animMove)
        imgForword.startAnimation(animMove)


//        Handler().postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, 3000) // 3000 is the delayed time in milliseconds.


//        if(NetInfo.isOnline(applicationContext)){
//            tvSlogan.text = AppConstant.alldata[0].general_settings[0].slogan
//            if (checkAndRequestPermissions()){
//                //downloadFile()
//                downloadVid1()
//            }else{
//                checkAndRequestPermissions()
//            }
//
//        }else{
//            if (AppConstant.getGeneralsettings(applicationContext).size > 0){
//                tvSlogan.text = AppConstant.getGeneralsettings(applicationContext)[0].slogan
//            }
//        }


        Handler(Looper.getMainLooper()).postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 4000)


    }

    private fun getAllData4() {

        var hud = KProgressHUD.create(this@SplashActivity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setMaxProgress(100)
        hud!!.show()

//        if (!this!!.context?.let { NetInfo.isOnline(it) }!!)
//        {
//            context?.let { AlertMessage.showMessage(it, "Alert!", "No internet connection!") }
//        }

        var retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
//.client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiKt::class.java)
        val userCall = api.getAllData()

        userCall?.enqueue(object: Callback<AllDataResponse> {
            override fun onResponse(call: Call<AllDataResponse>, response: Response<AllDataResponse>) {
                hud.dismiss()
                val  allDataresponse = response.body()
                Log.e("response",""+allDataresponse.toString())
                if (allDataresponse != null) {

                    AppConstant.home4Cat.clear()
                    AppConstant.home4Cat = allDataresponse[0].categories as ArrayList<Category>
                    AppConstant.home4Cat.removeAt(0)
                    AppConstant.home4Cat.removeAt(4)


                    AppConstant.getHome4Catagories(context).clear()
                    AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)


                    initUi()
//                    downloadAllImage()
//                    if (checkAndRequestPermissions()){
//                        //downloadFile()
//                        downloadVid1()
//                    }else{
//                        checkAndRequestPermissions()
//                    }


                }
            }

            override fun onFailure(call: Call<AllDataResponse>, t:Throwable) {
                hud.dismiss()
            }
        })

    }



    private fun getAllData() {

        var hud = KProgressHUD.create(this@SplashActivity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setMaxProgress(100)
        hud!!.show()

//        if (!this!!.context?.let { NetInfo.isOnline(it) }!!)
//        {
//            context?.let { AlertMessage.showMessage(it, "Alert!", "No internet connection!") }
//        }

        var retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
//.client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiKt::class.java)
        val userCall = api.getAllData()

        userCall?.enqueue(object: Callback<AllDataResponse> {
            override fun onResponse(call: Call<AllDataResponse>, response: Response<AllDataResponse>) {
                hud.dismiss()
                val  allDataresponse = response.body()
                Log.e("response",""+allDataresponse.toString())
                if (allDataresponse != null) {
                    AppConstant.alldata = allDataresponse

//
                    AppConstant.getContent(context).clear()
                    AppConstant.getCatagories(context).clear()
                    AppConstant.getGeneralsettings(context).clear()
                    AppConstant.getSubCatagories(context).clear()

                    AppConstant.saveCatagories(applicationContext,allDataresponse[0].categories)
                    AppConstant.saveSubCatagories(applicationContext,allDataresponse[0].sub_categories)
                    AppConstant.saveContent(applicationContext,allDataresponse[0].contents)
                    AppConstant.saveGeneralsettings(applicationContext,allDataresponse[0].general_settings)


                    Log.e("catdata","data "+AppConstant.getCatagories(applicationContext)[0].name)
                    Log.e("subcatdata","data "+AppConstant.getSubCatagories(applicationContext)[8].name)
                    Log.e("Contentdata","data "+AppConstant.getContent(applicationContext)[0].content)

                    getAllData4()


                    //downloadFile()
                }else{
                    Toast.makeText(context,"NO data found",Toast.LENGTH_SHORT).show()
                    finish()
                }

            }

            override fun onFailure(call: Call<AllDataResponse>, t:Throwable) {
                hud.dismiss()
                Toast.makeText(context,"NO data found",Toast.LENGTH_SHORT).show()
                finish()

            }
        })

    }

    private fun downloadAllImage() {

        val download = DownloadImageFileFromURLTask(context!!, outputDir,AppConstant.alldata[0].general_settings[0].app_logo_icon,"app_logo_icon.png", object :
            DownloadListener {
            override fun onSuccess(path: String) {
                //toast("File is downloaded successfully at $path")
            }

            override fun onFailure(error: String) {
                //toast(error)
                //downloadVid6()
            }
        })
        download.execute()

        for ((index, value) in AppConstant.alldata[0].categories.withIndex()) {
            val download = DownloadImageFileFromURLTask(context!!, outputDir,value.icon,value.name+".png", object :
                DownloadListener {
                override fun onSuccess(path: String) {
                    //toast("File is downloaded successfully at $path")
                }

                override fun onFailure(error: String) {
                    //toast(error)
                    //downloadVid6()
                }
            })
            download.execute()
        }

        for ((index, value) in AppConstant.alldata[0].sub_categories.withIndex()) {
            val download = DownloadImageFileFromURLTask(context!!, outputDir,value.icon,value.name+".png", object :
                DownloadListener {
                override fun onSuccess(path: String) {
                    //toast("File is downloaded successfully at $path")
                }

                override fun onFailure(error: String) {
                    //toast(error)
                    //downloadVid6()
                }
            })
            download.execute()
        }

    }

    override fun onResume() {
        super.onResume()
        //downloadFile()
    }

    private fun downloadVid1() {


//        val file: File = File("Download/uttoronvid/সফটস্কিল.mp4")
//        val deleted = file.delete()

        if (AppConstant.alldata[0].categories[0].video !=  null){
            srcUrl = AppConstant.alldata[0].categories[0].video
        }
        if (AppConstant.alldata[0].categories[0].name !=  null){
            filename = AppConstant.alldata[0].categories[0].name+".mp4"
        }

        //deleteFileUsingDisplayName(context!!,AppConstant.alldata[0].categories[0].name)

        val download = DownloadFileFromURLTask(context!!, outputDir,srcUrl,filename, object :
            DownloadListener {
            override fun onSuccess(path: String) {
                //toast("File is downloaded successfully at $path")

                Log.e("path1",path)

                downloadVid2()
            }

            override fun onFailure(error: String) {
                downloadVid1()
            }
        })
        download.execute()


    }

    private fun downloadVid2() {

        if (AppConstant.alldata[0].categories[1].video !=  null){
            srcUrl = AppConstant.alldata[0].categories[1].video
        }
        if (AppConstant.alldata[0].categories[1].name !=  null){
            filename = AppConstant.alldata[0].categories[1].name+".mp4"
        }

        val download = DownloadFileFromURLTask(context!!, outputDir,srcUrl,filename, object :
            DownloadListener {
            override fun onSuccess(path: String) {
                //toast("File is downloaded successfully at $path")
                downloadVid3()
            }

            override fun onFailure(error: String) {
                //toast(error)
                downloadVid2()
            }
        })
        download.execute()

    }

    private fun downloadVid3() {

        if (AppConstant.alldata[0].categories[2].video !=  null){
            srcUrl = AppConstant.alldata[0].categories[2].video
        }
        if (AppConstant.alldata[0].categories[2].name !=  null){
            filename = AppConstant.alldata[0].categories[2].name+".mp4"
        }

        val download = DownloadFileFromURLTask(context!!, outputDir,srcUrl,filename, object :
            DownloadListener {
            override fun onSuccess(path: String) {
                //toast("File is downloaded successfully at $path")
                downloadVid4()
            }

            override fun onFailure(error: String) {
                //toast(error)
                downloadVid3()
            }
        })
        download.execute()

    }

    private fun downloadVid4() {

        if (AppConstant.alldata[0].categories[3].video !=  null){
            srcUrl = AppConstant.alldata[0].categories[3].video
        }
        if (AppConstant.alldata[0].categories[3].name !=  null){
            filename = AppConstant.alldata[0].categories[3].name+".mp4"
        }

        val download = DownloadFileFromURLTask(context!!, outputDir,srcUrl,filename, object :
            DownloadListener {
            override fun onSuccess(path: String) {
                //toast("File is downloaded successfully at $path")
                downloadVid5()
            }

            override fun onFailure(error: String) {
                //toast(error)
                downloadVid4()
            }
        })
        download.execute()

    }


    private fun downloadVid5() {

        if (AppConstant.alldata[0].categories[4].video !=  null){
            srcUrl = AppConstant.alldata[0].categories[4].video
        }
        if (AppConstant.alldata[0].categories[4].name !=  null){
            filename = AppConstant.alldata[0].categories[4].name+".mp4"
        }

        val download = DownloadFileFromURLTask(context!!, outputDir,srcUrl,filename, object :
            DownloadListener {
            override fun onSuccess(path: String) {
                //toast("File is downloaded successfully at $path")
                downloadVid6()
            }

            override fun onFailure(error: String) {
                //toast(error)
                downloadVid5()
            }
        })
        download.execute()

    }

    private fun downloadVid6() {

        if (AppConstant.alldata[0].categories[5].video !=  null){
            srcUrl = AppConstant.alldata[0].categories[5].video
        }
        if (AppConstant.alldata[0].categories[5].name !=  null){
            filename = AppConstant.alldata[0].categories[5].name+".mp4"
        }

        val download = DownloadFileFromURLTask(context!!, outputDir,srcUrl,filename, object :
            DownloadListener {
            override fun onSuccess(path: String) {
                //toast("File is downloaded successfully at $path")
            }

            override fun onFailure(error: String) {
                //toast(error)
                //downloadVid6()

            }
        })
        download.execute()

        downloadAllImage()
    }


    fun deleteDirectory(path: File): Boolean {
        if (path.exists()) {
            val files = path.listFiles() ?: return true
            for (i in files.indices) {
                if (files[i].isDirectory) {
                    deleteDirectory(files[i])
                } else {
                    files[i].delete()
                }
            }
        }
        return path.delete()
    }

    private fun downloadFile() {

        for ((index, value) in AppConstant.alldata[0].categories.withIndex()) {

            if (value.video !=  null){
                srcUrl = value.video
            }
            if (value.name !=  null){
                filename = value.name+".mp4"
            }

            val download = DownloadFileFromURLTask(context!!, outputDir,srcUrl,filename, object :
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

    private fun checkAndRequestPermissions(): Boolean {

//        val camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        val listPermissionsNeeded = ArrayList<String>()

//        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.CAMERA)
//        }

        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }



        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //handle permission request results || calls when user from Permission request dialog presses Allow or Deny
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
               //downloadFile()
                downloadVid1()
            }
            else{
                //permission denied, cann't pick contact, just show message
                //Toast.makeText(context!!, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {

        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
        private val REQUEST_PERM_DELETE = 2000
    }


}

