package com.alex.newstime.util

import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.alex.newstime.R
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandler
import kotlinx.android.synthetic.main.controller_article.view.*

class DetailAnimationChangeHandler() : TransitionChangeHandler() {

    lateinit var title: String
    lateinit var url: String

    constructor(title: String, url: String) : this() {
        this.title = title
        this.url = url
    }

    override fun getTransition(container: ViewGroup, from: View?, to: View?, isPush: Boolean): Transition {
        if (to !is android.widget.ScrollView) return TransitionSet()

        to.findViewById<TextView>(R.id.textView_title).transitionName = title
        to.findViewById<ImageView>(R.id.imageView).transitionName = url

        val flagTransition = TransitionSet()
            .addTransition(ChangeClipBounds())
            .addTransition(ChangeTransform().apply { reparentWithOverlay = false })
            .addTransition(Slide().addTarget(to.textView_content))
            .setDuration(400)

        return TransitionSet()
            .addTransition(flagTransition)
            .setInterpolator(FastOutSlowInInterpolator())
    }
}