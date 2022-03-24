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
import kotlinx.android.synthetic.main.video_play_layout.*


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
        if (NetInfo.isOnline(context)){
           // tvSloganTop.text = AppConstant.getGeneralsettings(requireContext())[0].slogan

            for ((index, value) in AppConstant.getContent(requireContext()).withIndex()) {
                if (AppConstant.subCatName.equals(value.sub_category_name)){
                    if (value.content != null){
                        tvContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(value.content, Html.FROM_HTML_MODE_COMPACT)
                        } else {
                            Html.fromHtml(value.content)
                        }
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


        imgPlay.setOnClickListener {

            imgThumbnil.setVisibility(VideoView.GONE)
            imgPlay.setVisibility(VideoView.GONE)
            buffering_textview.setVisibility(VideoView.VISIBLE)

            initializePlayer("")
            videoview.setVideoURI(Uri.parse("")) //the string of the URL mentioned above
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