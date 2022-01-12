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
import com.uttoron.asynctask.SaveBitmapTask
import com.uttoron.callback.DownloadListener
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
private const val outputDir = "Android11Permissions"

class MainActivity : AppCompatActivity()  {

    var fileN: String? = null
    val MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123
    var result = false
    var urlString: String? = null
    var downloadDialog: Dialog? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = checkPermission()
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_fav-> {
                    title=resources.getString(R.string.favorites)
                    loadFragment(FavoriteFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_home-> {
                    title=resources.getString(R.string.home)
                    if (!NetInfo.isOnline(applicationContext)) {
                        loadFragment(HomeFragmentOffline())
                        //Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                    }else{
                        loadFragment(HomeFragmentOffline())
                        //loadFragment(HomeFragment())
                        //getAllData()
                    }
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_settings-> {
                    title=resources.getString(R.string.settings)
                    loadFragment(SettingsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }


        if(result){
            checkFolder()
        }
        if (!NetInfo.isOnline(applicationContext)) {
            loadFragment(HomeFragmentOffline())
            //Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }else{
            loadFragment(HomeFragmentOffline())
            //loadFragment(HomeFragment())
            //getAllData()
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

        var hud = KProgressHUD.create(this@MainActivity)
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

                    AppConstant.saveHome4Catagories(applicationContext,AppConstant.home4Cat)

                    loadFragment(HomeFragment())

                }
            }

            override fun onFailure(call: Call<AllDataResponse>, t:Throwable) {
                hud.dismiss()
            }
        })

    }

    var pressCount = 0
    override fun onBackPressed() {
        pressCount = pressCount+1
        if (pressCount == 1){
            Toast.makeText(applicationContext,"To exit from app, press again",Toast.LENGTH_SHORT).show()
        }else if (pressCount == 2){
            pressCount = 0
            finish()
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
}

