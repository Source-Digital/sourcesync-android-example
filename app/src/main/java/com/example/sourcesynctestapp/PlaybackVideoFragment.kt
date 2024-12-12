package com.example.sourcesynctestapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.FrameLayout
import android.graphics.drawable.GradientDrawable

/** Handles video playback with media controls. */
class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (_, title, description, _, _, videoUrl) =
            activity?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as Movie

        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        val playerAdapter = MediaPlayerAdapter(activity)
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)

        mTransportControlGlue = PlaybackTransportControlGlue(getActivity(), playerAdapter)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.title = title
        mTransportControlGlue.subtitle = description
        mTransportControlGlue.playWhenPrepared()

        playerAdapter.setDataSource(Uri.parse(videoUrl))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create container for preview text and circle
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 8, 16, 8)
        }

        // Add preview text with its own background
        val textView = TextView(context).apply {
            text = "Activation Preview Text"
            textSize = 16f
            setTextColor(Color.WHITE)
            setPadding(16, 8, 16, 8)
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#CC000000"))  // Semi-transparent black background
                cornerRadius = 8f  // Slightly rounded corners
            }
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 12  // Increased spacing between text and circle
            }
        }
        container.addView(textView)

        // Add larger circle
        val circle = View(context).apply {
            layoutParams = LinearLayout.LayoutParams(80, 80).apply {  // Doubled size from 20 to 40
                gravity = Gravity.CENTER_VERTICAL
            }
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.RED)
            }
        }
        container.addView(circle)

        // Add container to the fragment's root view with positioning
        (view as ViewGroup).addView(container, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END  // Position at top-right
            topMargin = 24  // Space from top
            marginEnd = 24  // Space from right
        })
    }

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }
}