package com.espresso.pbmobile

import android.view.animation.Animation

class AnimationListener(private val onAnimationEnd: () -> Unit) : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {}
    override fun onAnimationStart(animation: Animation?) {}
    override fun onAnimationEnd(animation: Animation?) {
        onAnimationEnd()
    }
}
