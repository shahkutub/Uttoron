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
import kotlinx.android.synthetic.main.item_other_cat.view.*
import kotlinx.android.synthetic.main.video_layout.*
import kotlinx.android.synthetic.main.video_play_layout.*
import android.os.Build
import androidx.recyclerview.widget.LinearLayoutManager


class VideoFragmentOffline : Fragment(){


    // Current playback position (in milliseconds).
    private var mCurrentPosition = 0

    // Tag for the instance state bundle.
    private val PLAYBACK_TIME = "play_time"



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.video_layout,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //imgUttornVId.setImageURI(Uri.parse("/sdcard/download/uttoron/app_logo_icon"+".png"))

        tvCatname.text = AppConstant.catName
        //tvSloganTop.text = AppConstant.getGeneralsettings(requireContext())[0].slogan
        tvCatname.setOnClickListener {
            AppConstant.isHome = false

            AppConstant.subCatName = AppConstant.catName

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, ContentFragmentOffline())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val layoutManagerOtherCat = GridLayoutManager(context, 2)
        layoutManagerOtherCat.orientation = LinearLayoutManager.VERTICAL
        recyclevidCats!!.setLayoutManager(layoutManagerOtherCat)
        var usersAdapterOtherCat = OtherCatListAdapter(AppConstant.subCatList, requireContext())
        //recyclerDrotoJogajog!!.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        recyclevidCats!!.setAdapter(usersAdapterOtherCat)

        //for last odd item full width

        val num = AppConstant.subCatList.size
        if (num % 2 == 0){
            println("$num is even")
        }else{
            layoutManagerOtherCat.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
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


        imgBack.setOnClickListener {
            requireActivity().onBackPressed()
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.container, HomeFragmentOffline())
//            transaction.addToBackStack(null)
//            transaction.commit()
        }

        var url = ""

//        for ((index, value) in AppConstant.getCatagories(requireContext()).withIndex()) {
//            if(value.id.toString().equals(AppConstant.catID)){
//                url = value.video
//                break
//            }
//        }

        url = "/sdcard/download/uttoron/"+AppConstant.catName+".mp4"
        var path = ""

        if(AppConstant.catName == "সফটস্কিল"){
            imgThumbnil.setImageResource(R.drawable.thumbnail_softskill)
            path = "android.resource://" + requireContext().getPackageName() + "/"+R.raw.softskill



        }

        if(AppConstant.catName == "চাকুরীর সাধারণ নিয়ম কানুন"){
            imgThumbnil.setImageResource(R.drawable.thumbnail_jobrule)
            path = "android.resource://" + requireContext().getPackageName() + "/"+R.raw.job_general_role
        }

        if(AppConstant.catName == "চাকুরীতে উন্নয়নের উপায়"){
            imgThumbnil.setImageResource(R.drawable.thumbnail_jobdevelopment)
            path = "android.resource://" + requireContext().getPackageName() + "/"+R.raw.job_devlopment
        }

        if(AppConstant.catName == "আর্থিক ব্যবস্থাপনা ও পরিকল্পনা"){
            imgThumbnil.setImageResource(R.drawable.thumbnail_arthikbebosthapona)
            path = "android.resource://" + requireContext().getPackageName() + "/"+R.raw.arthik_bebosthhapona
        }

        if(AppConstant.catName == "স্বাস্থ্য ও সুরক্ষা"){
            imgThumbnil.setImageResource(R.drawable.thumbnail_health)
            path = "android.resource://" + requireContext().getPackageName() + "/"+R.raw.health
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

            initializePlayer(path)
            videoview.setVideoURI(Uri.parse(path)) //the string of the URL mentioned above
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


    inner class OtherCatListAdapter(var notifications: ArrayList<SubCategory>, var context: Context) :
        RecyclerView.Adapter<OtherCatListAdapter.UserViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_other_cat, parent, false)
        )
        override fun getItemViewType(position: Int): Int {
            return if (position == getItemCount() - 1) 0 else 1 // If the item is last, `itemViewType` will be 0
        }

        override fun getItemCount() = notifications.size
        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            holder.tvCatNameOther.text = notifications[position].name


            //holder.imgOtehrCat.setImageURI(Uri.parse("/sdcard/download/uttoron/"+notifications[position].name+".png"))
//            Glide.with(requireContext())
//                .asBitmap()
//                .load(notifications[position].icon)
//                .into(object : SimpleTarget<Bitmap?>() {
//                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                        holder.imgOtehrCat.setImageBitmap(resource)
//                    }
//                })


//            if(notifications.size > 4){
//                val params = holder.fullViewCat!!.layoutParams
//                params.height = R.dimen._80sdp
//                holder.fullViewCat!!.layoutParams = params
//                //holder.fullViewCat.height =
//            }

            holder.imgOtehrCat.visibility =View.GONE

            holder.fullViewCat.setOnClickListener {
                AppConstant.isHome = false

                AppConstant.subCatName = notifications[position].name
                AppConstant.subCatId = notifications[position].id.toString()

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, ContentFragmentOffline())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }


        inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvCatNameOther = view.tvCatNameOther
            val imgOtehrCat = view.imgOtehrCat
            val fullViewCat = view.fullViewCat

        }
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