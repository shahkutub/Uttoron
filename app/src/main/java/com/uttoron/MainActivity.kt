package com.uttoron

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaopiz.kprogresshud.KProgressHUD
import com.uttoron.model.AllDataResponse
import com.uttoron.model.Category
import com.uttoron.utils.AppConstant
import com.uttoron.utils.NetInfo
import gov.bd.mpportal.utils.ApiKt
import gov.bd.mpportal.utils.baseUrl
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Environment
import androidx.core.app.ActivityCompat

import android.content.DialogInterface

import android.os.Build

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.AsyncTask

import androidx.core.content.ContextCompat
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.downloader.*
import com.uttoron.asynctask.DownloadFileFromURLTask
import com.uttoron.asynctask.DownloadImageFileFromURLTask
import com.uttoron.asynctask.SaveBitmapTask
import com.uttoron.callback.DownloadListener
import com.uttoron.model.TrackResponse
import com.uttoron.utils.AlertMessage
import com.uttoron.utils.PersistData
import java.io.*
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList




private const val REQ_ADD_IMAGE_TO_ALBUM = 1
private const val REQ_SAVE_BITMAP = 2
private const val REQ_DOWNLOAD_FILE = 3
private const val REQ_PICK_FILE = 4
private const val REQ_CREATE_WRITE_REQUEST = 5
private const val REQ_ALL_FILES_ACCESS_PERMISSION = 6

val storagePermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
private const val outputDir = "uttron"

class MainActivity : AppCompatActivity()  {

    private var trackResponse: TrackResponse? = null
    private var allDataresponse: AllDataResponse? = null
    var fileN: String? = null
    val MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123
    var result = false
    var urlString: String? = null
    var downloadDialog: Dialog? = null
    var context:Context? = null
    private var filename: String = ""
    private var srcUrl: String  = ""
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        result = checkPermission()
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
//                R.id.navigation_fav-> {
//                    title=resources.getString(R.string.favorites)
//                    loadFragment(FavoriteFragment())
//                    return@setOnNavigationItemSelectedListener true
//                }

                R.id.navigation_home-> {
                    //title=resources.getString(R.string.home)
                    if (!NetInfo.isOnline(applicationContext)) {
                        loadFragment(HomeFragmentOffline())
                        //Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                    }else{
                        loadFragment(HomeFragmentOffline())
                        //loadFragment(HomeFragment())
                        //getAllData()
                    }
                    return@setOnNavigationItemSelectedListener false
                }

                R.id.navigation_settings-> {
                    title=resources.getString(R.string.settings)
                    loadFragment(InfoFragment())
                    return@setOnNavigationItemSelectedListener false
                }

            }
            false

        }


        if(result){
            checkFolder()
        }
        if (!NetInfo.isOnline(applicationContext)) {
            loadFragment(HomeFragmentOffline())
            //getAllData()

            //Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }else{
            loadFragment(HomeFragmentOffline())
            //loadFragment(HomeFragment())
            //getAllData()
            getTrackData()
        }

//        btn_update_fab.setOnClickListener {
//            getAllData()
//        }




    }


    //hare you can start downloding video
    fun newDownload(url: String?) {
       // val downloadTask = DownloadTask(this@MainActivity)
       // downloadTask.execute(url)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun checkFolder () : File {
//        val path = Environment.getStorageDirectory().absolutePath + "/Uttron/"
//        val dir = File(path)
//        Log.e("dir",""+dir)
//        var isDirectoryCreated: Boolean = dir.exists()
//        if (!isDirectoryCreated) {
//            isDirectoryCreated = dir.mkdir()
//        }
//        if (isDirectoryCreated) {
//            // do something\
//            Log.d("Folder", "Already Created")
//        }

        val mydir: File = applicationContext.getDir("uttoron", MODE_PRIVATE) //Creating an internal dir;
        Log.e("dir",""+mydir)
        if (!mydir.exists()) {
            mydir.mkdirs()
        }
        return mydir
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun checkPermission(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!")
                    alertBuilder.setPositiveButton(android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, which ->
                            ActivityCompat.requestPermissions(
                                this@MainActivity,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                MY_PERMISSIONS_REQUEST_WRITE_STORAGE
                            )
                        })
                    val alert: AlertDialog = alertBuilder.create()
                    alert.show()
                } else {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE
                    )
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }


    fun checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            val alertBuilder = AlertDialog.Builder(this@MainActivity)
            alertBuilder.setCancelable(true)
            alertBuilder.setTitle("Permission necessary")
            alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!")
            alertBuilder.setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE
                )
            }
            val alert = alertBuilder.create()
            alert.show()
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_WRITE_STORAGE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_STORAGE -> if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
               // checkFolder()
            } else {
                //code for deny
                checkAgain()
            }
        }
    }




    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun getAllData() {
        if (!this!!.context?.let { NetInfo.isOnline(it) }!!)
        {
            context?.let { AlertMessage.showMessage(it, "Alert!", "No internet connection!") }
        }
        var hud = KProgressHUD.create(this@MainActivity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setMaxProgress(100)
        hud!!.show()



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
                AppConstant.alldata =response.body()
                // AppConstant.alldata = null
                Log.e("AppConstant.alldata","size: "+AppConstant.alldata[0].categories.size)
                //downloadVid1()
                    allDataresponse = response.body()
               // Log.e("response",""+allDataresponse.toString())
//                if (allDataresponse != null) {
//                    PersistData.setIntData(context,AppConstant.oldTrackNo, allDataresponse!![0].track_no)
//                    AppConstant.home4Cat.clear()
//                    AppConstant.home4Cat = allDataresponse!![0].categories as ArrayList<Category>
//                    AppConstant.home4Cat.removeAt(0)
//                    AppConstant.home4Cat.removeAt(4)
//                    AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)
//
//                }

                PersistData.setIntData(context,AppConstant.currentTrackNumber, trackResponse!![0].track_no)
                //toast("File is downloaded successfully at $path")
                if (allDataresponse != null) {
                    //PersistData.setIntData(context,AppConstant.oldTrackNo, allDataresponse!![0].track_no)
                    AppConstant.home4Cat.clear()
                    AppConstant.home4Cat = allDataresponse!![0].categories as ArrayList<Category>
                    AppConstant.home4Cat.removeAt(0)
                    AppConstant.home4Cat.removeAt(4)
                    AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)

                    AppConstant.getContent(context).clear()
                    AppConstant.getCatagories(context).clear()
                    AppConstant.getGeneralsettings(context).clear()
                    AppConstant.getSubCatagories(context).clear()

                    //AppConstant.saveCatagories(applicationContext,allDataresponse!![0].categories)
                    //AppConstant.saveSubCatagories(applicationContext,allDataresponse!![0].sub_categories)
                    AppConstant.saveContent(applicationContext,allDataresponse!![0].contents)
                    AppConstant.saveGeneralsettings(applicationContext,allDataresponse!![0].general_settings)

                    //loadFragment(HomeFragmentOffline())

//            AppConstant.home4Cat.clear()
//            AppConstant.home4Cat = allDataresponse!![0].categories as java.util.ArrayList<Category>
//            AppConstant.home4Cat.removeAt(0)
//            AppConstant.home4Cat.removeAt(4)
//            AppConstant.getHome4Catagories(context).clear()
//            AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)


                }

            }

            override fun onFailure(call: Call<AllDataResponse>, t:Throwable) {
                hud.dismiss()
            }
        })

    }




    var pressCount = 0
    override fun onBackPressed() {
//        pressCount = pressCount+1
//        if (pressCount == 1){
//            Toast.makeText(applicationContext,"To exit from app, press again",Toast.LENGTH_SHORT).show()
//        }else if (pressCount == 2){
//            pressCount = 0
//            finish()
//        }

//        val transaction = supportFragmentManager.beginTransaction()
//        if (AppConstant.isHome){
//            transaction.replace(R.id.container, HomeFragmentOffline())
//            transaction.addToBackStack(null)
//            transaction.commit()
//            pressCount = 1
//        }else{
//            transaction.replace(R.id.container, VideoFragmentOffline())
//            transaction.addToBackStack(null)
//            transaction.commit()
//            pressCount = 0
//        }
//
//        if(pressCount ==1){
//            finish()
//        }
        //transaction.replace(R.id.container, VideoFragment())

//        val count = supportFragmentManager.backStackEntryCount
//        if (count == 1) {
//            super.onBackPressed()
//            finish()
//        } else {
//            supportFragmentManager.popBackStack()
//        }


        val fragments = supportFragmentManager.backStackEntryCount
        if (fragments == 1) {
            finish()
        } else {
            if (fragmentManager.backStackEntryCount > 1) {
                fragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }
        }

    }

//    // [START saveBitmap]
//    private fun saveBitmap() {
//        //val bitmap = binding.linearParent.drawToBitmap()
//        val saveBitmap = SaveBitmapTask(mContext, bitmap, outputDir, object : DownloadListener {
//            override fun onSuccess(path: String) {
//                toast("Bitmap is saved successfully at $path")
//            }
//
//            override fun onFailure(error: String) {
//                toast(error)
//            }
//        })
//        saveBitmap.execute()
//    }
    // [END saveBitmap]

    // [START downloadFile]
//    private fun downloadFile() {
//        val download = DownloadFileFromURLTask(applicationContext, outputDir, object :
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
    // [END downloadFile]

    private fun getTrackData() {

        var retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
//.client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiKt::class.java)
        val userCall = api.getTrackDataData()

        userCall?.enqueue(object: Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                //hud.dismiss()
                trackResponse = response.body()
                if (trackResponse != null) {
                    Log.e("response",""+trackResponse.toString())
                    if (trackResponse!![0].track_no > PersistData.getIntData(context,AppConstant.currentTrackNumber)){

                  //      Toast.makeText(context,"Update available",Toast.LENGTH_SHORT).show()
                        updateDialog()

                    }

                }
            }

            override fun onFailure(call: Call<TrackResponse>, t:Throwable) {
                //hud.dismiss()
            }
        })

    }

    private fun updateDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update available!")
        builder.setMessage("Do you want to updaste")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            getAllData()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()


        }

//        builder.setNeutralButton("Maybe") { dialog, which ->
//            Toast.makeText(applicationContext,
//                "Maybe", Toast.LENGTH_SHORT).show()
//        }
        builder.show()
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

        PersistData.setIntData(context,AppConstant.currentTrackNumber, trackResponse!![0].track_no)
        //toast("File is downloaded successfully at $path")
        if (allDataresponse != null) {
            //PersistData.setIntData(context,AppConstant.oldTrackNo, allDataresponse!![0].track_no)
            AppConstant.home4Cat.clear()
            AppConstant.home4Cat = allDataresponse!![0].categories as ArrayList<Category>
            AppConstant.home4Cat.removeAt(0)
            AppConstant.home4Cat.removeAt(4)
            AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)

            AppConstant.getContent(context).clear()
            AppConstant.getCatagories(context).clear()
            AppConstant.getGeneralsettings(context).clear()
            AppConstant.getSubCatagories(context).clear()

            //AppConstant.saveCatagories(applicationContext,allDataresponse!![0].categories)
            //AppConstant.saveSubCatagories(applicationContext,allDataresponse!![0].sub_categories)
            AppConstant.saveContent(applicationContext,allDataresponse!![0].contents)
            AppConstant.saveGeneralsettings(applicationContext,allDataresponse!![0].general_settings)

            //loadFragment(HomeFragmentOffline())

//            AppConstant.home4Cat.clear()
//            AppConstant.home4Cat = allDataresponse!![0].categories as java.util.ArrayList<Category>
//            AppConstant.home4Cat.removeAt(0)
//            AppConstant.home4Cat.removeAt(4)
//            AppConstant.getHome4Catagories(context).clear()
//            AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)


        }

        //downloadAllImage()
    }

    private fun downloadAllImage() {

        val download = DownloadImageFileFromURLTask(context!!, outputDir,AppConstant.alldata[0].general_settings[0].app_logo_icon,"app_logo_icon.png", object :
            DownloadListener {
            override fun onSuccess(path: String) {
                PersistData.setIntData(context,AppConstant.currentTrackNumber, trackResponse!![0].track_no)
                //toast("File is downloaded successfully at $path")
                if (allDataresponse != null) {
                    //PersistData.setIntData(context,AppConstant.oldTrackNo, allDataresponse!![0].track_no)
                    AppConstant.home4Cat.clear()
                    AppConstant.home4Cat = allDataresponse!![0].categories as ArrayList<Category>
                    AppConstant.home4Cat.removeAt(0)
                    AppConstant.home4Cat.removeAt(4)
                    AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)

                    AppConstant.getContent(context).clear()
                    AppConstant.getCatagories(context).clear()
                    AppConstant.getGeneralsettings(context).clear()
                    AppConstant.getSubCatagories(context).clear()

                    AppConstant.saveCatagories(applicationContext,allDataresponse!![0].categories)
                    AppConstant.saveSubCatagories(applicationContext,allDataresponse!![0].sub_categories)
                    AppConstant.saveContent(applicationContext,allDataresponse!![0].contents)
                    AppConstant.saveGeneralsettings(applicationContext,allDataresponse!![0].general_settings)



                    AppConstant.home4Cat.clear()
                    AppConstant.home4Cat = allDataresponse!![0].categories as java.util.ArrayList<Category>
                    AppConstant.home4Cat.removeAt(0)
                    AppConstant.home4Cat.removeAt(4)
                    AppConstant.getHome4Catagories(context).clear()
                    AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)


                }
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

        //downloadAllImage()
    }

    override fun onResume() {
        super.onResume()


    }

}

