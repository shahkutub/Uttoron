package com.uttoron

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.downloader.*
import com.kaopiz.kprogresshud.KProgressHUD
import com.uttoron.asynctask.DownloadFileFromURLTask
import com.uttoron.asynctask.DownloadImageFileFromURLTask
import com.uttoron.callback.DownloadListener
import com.uttoron.model.AllDataResponse
import com.uttoron.model.Category
import com.uttoron.model.TrackResponse
import com.uttoron.utils.AlertMessage
import com.uttoron.utils.AppConstant
import com.uttoron.utils.NetInfo
import com.uttoron.utils.PersistData
import gov.bd.mpportal.utils.ApiKt
import gov.bd.mpportal.utils.baseUrl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.video_play_layout.*
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.util.*
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.view.View
import com.google.android.material.snackbar.Snackbar

import com.google.android.play.core.install.model.AppUpdateType

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.tasks.OnSuccessListener


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


    var currentVersion: String? = null

     var appUpdateManager: AppUpdateManager? = null
    private val IMMEDIATE_APP_UPDATE_REQ_CODE = 124
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null

    private val FLEXIBLE_APP_UPDATE_REQ_CODE = 123

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkUpdate()

        //appUpdate()
//        val currentapiVersion = Build.VERSION.SDK_INT
//        if (currentapiVersion > 29){
//            if (Environment.isExternalStorageManager()) {
//
//// If you don't have access, launch a new activity to show the user the system's dialog
//// to allow access to the external storage
//            } else {
//                val intent = Intent()
//                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
//                val uri: Uri = Uri.fromParts("package", this.packageName, null)
//                intent.data = uri
//                startActivity(intent)
//            }
//        }

//        if(NetInfo.isOnline(context)){
//            downloadPdfFile()
//            //PersistData.setBooleanData(context,AppConstant.isInstallTime,true)
//        }

//        if (checkAndRequestPermissions()){
//            if (!PersistData.getBooleanData(context,AppConstant.isInstallTime)){
//                try {
//                    if(NetInfo.isOnline(context)){
//                        downloadPdfFile()
//                        PersistData.setBooleanData(context,AppConstant.isInstallTime,true)
//                    }else{
//                        context?.let { AlertMessage.showMessage(it, "সতর্কতা!", "ইন্টারনেট সংযোগ নেই!") }
//                    }
//
//                }catch (e:Exception){
//
//                }
//
//            }
//        }else{
//            checkAndRequestPermissions()
//        }


        // result = checkPermission()
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
//                R.id.navigation_fav-> {
//                    title=resources.getString(R.string.favorites)
//                    loadFragment(FavoriteFragment())
//                    return@setOnNavigationItemSelectedListener true
//                }

                R.id.navigation_home-> {

                    val fm: FragmentManager = getSupportFragmentManager()
                    for (i in 0 until fm.getBackStackEntryCount()) {
                        fm.popBackStack()
                    }
                    val controller = MediaController(context)
                    if (controller.isShowing){
                        //controller.s
                    }

                    //title=resources.getString(R.string.home)
                    if (!NetInfo.isOnline(applicationContext)) {
                        //videoview.stopPlayback()
                        var mediaPlayer = MediaPlayer()
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop()
                            //mediaPlayer.stop()
                            mediaPlayer.release()

                        }
                        loadFragment(HomeFragmentOffline())
                        //Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                    }else{
                        //videoview.stopPlayback()
                        var mediaPlayer = MediaPlayer()
                        if (mediaPlayer.isPlaying()){
                            //mediaPlayer.stop()
                            mediaPlayer.stop()
                            mediaPlayer.release()

                        }
                        loadFragment(HomeFragmentOffline())
                        //loadFragment(HomeFragment())
                        //getAllData()
                    }
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_settings-> {
                    title=resources.getString(R.string.settings)

                    loadFragment(InfoFragment())

                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }


//        if(result){
//            checkFolder()
//        }
        if (!NetInfo.isOnline(applicationContext)) {
            loadFragment(HomeFragmentOffline())
            //getAllData()

            //Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }else{
            loadFragment(HomeFragmentOffline())
            //loadFragment(HomeFragment())
            //getAllData()
            //getTrackData()
        }

//        btn_update_fab.setOnClickListener {
//            getAllData()
//        }


//        if (!PersistData.getBooleanData(context,AppConstant.isInstallTime)){
//            try {
//                if(NetInfo.isOnline(context)){
//                    downloadPdfFile()
//                    PersistData.setBooleanData(context,AppConstant.isInstallTime,true)
//                }else{
//                    context?.let { AlertMessage.showMessage(it, "সতর্কতা!", "ইন্টারনেট সংযোগ নেই!") }
//                }
//
//            }catch (e:Exception){
//
//            }
//
//        }


    }

    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo)
            }
        }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                this,
                IMMEDIATE_APP_UPDATE_REQ_CODE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(
            findViewById<View>(android.R.id.content).rootView,
            "New app is ready!",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Install") { view: View? ->
                if (appUpdateManager != null) {
                    appUpdateManager!!.completeUpdate()
                }
            }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_dark))
            .show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                    applicationContext,
                    "Update canceled by user! Result Code: $resultCode", Toast.LENGTH_LONG
                ).show()
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(
                    applicationContext,
                    "Update success! Result Code: $resultCode", Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Update Failed! Result Code: $resultCode",
                    Toast.LENGTH_LONG
                ).show()
                checkUpdate()
            }
        }
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







    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, fragment)
        //transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun getAllData() {
        if (!this!!.context?.let { NetInfo.isOnline(it) }!!)
        {
            context?.let { AlertMessage.showMessage(it, "সতর্কতা!", "ইন্টারনেট সংযোগ নেই!") }
        }
        var hud = KProgressHUD.create(this@MainActivity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("অপেক্ষা করুন....")
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
                //Log.e("AppConstant.alldata","size: "+AppConstant.alldata[0].categories.size)
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
                    AppConstant.getContent(context).clear()
                    AppConstant.getCatagories(context).clear()
                    AppConstant.getGeneralsettings(context).clear()
                    AppConstant.getSubCatagories(context).clear()

                    AppConstant.saveCatagories(applicationContext, allDataresponse!![0].categories)
                    AppConstant.saveSubCatagories(applicationContext, allDataresponse!![0].sub_categories)
                    AppConstant.saveContent(applicationContext,allDataresponse!![0].contents)
                    AppConstant.saveGeneralsettings(applicationContext,allDataresponse!![0].general_settings)



                    AppConstant.home4Cat.clear()
                    AppConstant.home4Cat = allDataresponse!![0].categories as java.util.ArrayList<Category>
                    AppConstant.home4Cat.removeAt(0)
                    AppConstant.home4Cat.removeAt(4)
                    AppConstant.getHome4Catagories(context).clear()
                    AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)


                    loadFragment(HomeFragmentOffline())



                }

                //downloadPdfFile()

            }

            override fun onFailure(call: Call<AllDataResponse>, t:Throwable) {
                hud.dismiss()
                //downloadPdfFile()
            }
        })

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

        showMessageFinish()

//        val transaction = supportFragmentManager.beginTransaction()
//        if (AppConstant.isHome){
//            transaction.replace(R.id.container, HomeFragmentOffline())
//            transaction.addToBackStack(null)
//            transaction.commit()
//            pressCount = 1
//        }else{
//
//            transaction.replace(R.id.container, VideoFragmentOffline())
//            transaction.addToBackStack(null)
//            transaction.commit()
//
//        }
//
//        if (AppConstant.isVidSoft){
//            transaction.replace(R.id.container, VideoFragmentOfflineSoftSkill())
//            transaction.addToBackStack(null)
//            transaction.commit()
//            pressCount = 0
//        }else{
//            transaction.replace(R.id.container, VideoFragmentOffline())
//            transaction.addToBackStack(null)
//            transaction.commit()
//            pressCount = 0
//        }


//        if(pressCount ==1){
//            finish()
//        }
        //transaction.replace(R.id.container, VideoFragment())

//        val count = supportFragmentManager.backStackEntryCount
//        if (count == 0) {
//            super.onBackPressed()
//            finish()
//        } else {
//            supportFragmentManager.popBackStack()
//        }


//        val fragments = supportFragmentManager.backStackEntryCount
//        if (fragments == 0) {
//            finish()
//        } else {
//            if (fragmentManager.backStackEntryCount > 1) {
//                fragmentManager.popBackStack()
//            } else {
//                super.onBackPressed()
//            }
//        }

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
                        PersistData.setIntData(context,AppConstant.currentTrackNumber, trackResponse!![0].track_no)

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
        builder.setTitle("আপডেট উপলব্ধ!")
        builder.setMessage("আপনি কি আপডেট করতে চান?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("হ্যাঁ") { dialog, which ->
            dialog.dismiss()
            getAllData()
        }

        builder.setNegativeButton("না") { dialog, which ->
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


    private fun checkAndRequestPermissions(): Boolean {

//        val camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        val listPermissionsNeeded = java.util.ArrayList<String>()

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
                if (!PersistData.getBooleanData(context,AppConstant.isInstallTime)){
                    try {
                        if(NetInfo.isOnline(context)){
                            downloadPdfFile()
                            PersistData.setBooleanData(context,AppConstant.isInstallTime,true)
                        }else{
                            context?.let { AlertMessage.showMessage(it, "সতর্কতা!", "ইন্টারনেট সংযোগ নেই!") }
                        }

                    }catch (e:Exception){

                    }

                }
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

    /*
	 * show alert dialog P: context, title and message
	 */
    fun showMessageFinish() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("সতর্কতা!")
        builder.setMessage("আপনি কি এপ থেকে বের হতে চান?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("হ্যাঁ") { dialog, which ->
            dialog.dismiss()
            finish()

        }

        builder.setNegativeButton("না") { dialog, which ->
            dialog.dismiss()

        }

//        builder.setNeutralButton("Maybe") { dialog, which ->
//            Toast.makeText(applicationContext,
//                "Maybe", Toast.LENGTH_SHORT).show()
//        }
        builder.show()
    }

    private fun appUpdate() {
        //Toast.makeText(getBaseContext(),"App update", Toast.LENGTH_SHORT).show();
        try {
            currentVersion = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        Toast.makeText(getBaseContext(),"currentVersion"+currentVersion, Toast.LENGTH_SHORT).show();

        CheckIsUpdateReady(
            //"https://play.google.com/store/apps/details?id=$packageName&hl=en",
            "https://play.google.com/store/apps/details?id=com.uttoron",
            object : UrlResponce() {
                override fun onReceived(resposeStr: String?) {
                    Toast.makeText(getBaseContext(),"resposeStr: "+resposeStr, Toast.LENGTH_SHORT).show();

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

