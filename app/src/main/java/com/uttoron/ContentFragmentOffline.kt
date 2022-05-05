package com.uttoron

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnRenderListener
import com.uttoron.utils.AppConstant
import kotlinx.android.synthetic.main.content_layout.*
import kotlinx.android.synthetic.main.video_play_layout.*
import java.io.File


class ContentFragmentOffline : Fragment(){
    private var mCurrentPosition = 0

    // Tag for the instance state bundle.
    private val PLAYBACK_TIME = "play_time"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_layout,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        tvTitle.text = AppConstant.subCatName

        Log.e("content",""+AppConstant.content)
//        tvContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Html.fromHtml(AppConstant.content, Html.FROM_HTML_MODE_COMPACT)
//        } else {
//            Html.fromHtml(AppConstant.content)
//        }

        val file = File("/sdcard/download/uttoron/"+AppConstant.subCatName+".pdf")

        pdfv.fromFile(file)
            .onRender(OnRenderListener { pages, pageWidth, pageHeight ->
                pdfv.fitToWidth()
            })
            .load()

//        if(AppConstant.subCatName.equals("সময় ব্যবস্থাপনা")){
//            pdfv.fromAsset("সময় ব্যবস্থাপনা.pdf")
//                .onRender(OnRenderListener { pages, pageWidth, pageHeight ->
//                    pdfv.fitToWidth() // optionally pass page number
//                })
//                .load()
//        }


//        PDFView.Configurator.onRender(OnRenderListener { pages, pageWidth, pageHeight ->
//            pdfView.fitToWidth() // optionally pass page number
//        })

        //webview.loadUrl("/sdcard/download/uttoron/"+AppConstant.catName+".mp4")
        //webview.loadData("/sdcard/download/uttoron/uttoronTest.pdf")


//        for ((index, value) in AppConstant.getContent(context).withIndex()) {
//
//            if (value.sub_category_id != null){
//                if (AppConstant.subCatId.equals(value.sub_category_id.toString())){
//                    tvContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        Html.fromHtml(value.content, Html.FROM_HTML_MODE_COMPACT)
//                    } else {
//                        Html.fromHtml(value.content)
//                    }
//                }
//            }
//        }



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


        if(AppConstant.subCatName.equals("সেইফটি ভিডিও")){

            vidView.visibility = View.VISIBLE
        }else{
            vidView.visibility = View.GONE
        }

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME)
        }
        val controller = MediaController(requireContext())
        controller.setMediaPlayer(videoview)
        videoview.setMediaController(controller)


        var safetyVidPath = "android.resource://" + requireContext().getPackageName() + "/"+R.raw.safetyvid
        imgPlay.setOnClickListener {

            imgThumbnil.setVisibility(VideoView.GONE)
            imgPlay.setVisibility(VideoView.GONE)
            buffering_textview.setVisibility(VideoView.VISIBLE)

            initializePlayer(safetyVidPath)
            videoview.setVideoURI(Uri.parse(safetyVidPath)) //the string of the URL mentioned above
            videoview.requestFocus()
            videoview.start()


        }
    }



    private fun initializePlayer(url: String) {
        // Show the "Buffering..." message while the video loads.
        buffering_textview.setVisibility(VideoView.VISIBLE)

        // Buffer and decode the video sample.
        val videoUri: Uri = Uri.parse(url)
        videoview.setVideoURI(videoUri)

        // Listener for onPrepared() event (runs after the media is prepared).
        videoview.setOnPreparedListener(
            MediaPlayer.OnPreparedListener { // Hide buffering message.
                buffering_textview.setVisibility(VideoView.INVISIBLE)

                // Restore saved position, if available.
                if (mCurrentPosition > 0) {
                    videoview.seekTo(mCurrentPosition)
                } else {
                    // Skipping to 1 shows the first frame of the video.
                    videoview.seekTo(1)
                }

                // Start playing!
                //videoview.start()
            })

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        videoview.setOnCompletionListener(
            MediaPlayer.OnCompletionListener {
                Toast.makeText(
                    requireContext(),
                    "",
                    Toast.LENGTH_SHORT
                ).show()

                // Return the video position to the start.
                videoview.seekTo(0)
            })
    }


    override fun onStart() {
        super.onStart()

        // Load the media each time onStart() is called.
        //initializePlayer()
    }

    override fun onPause() {
        super.onPause()

        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoview.pause()
        }
    }

    override fun onStop() {
        super.onStop()

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer()
    }

    private fun releasePlayer() {
        videoview.stopPlayback()
    }

}