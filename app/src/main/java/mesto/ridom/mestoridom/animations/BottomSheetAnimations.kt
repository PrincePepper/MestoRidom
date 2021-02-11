package mesto.ridom.mestoridom.animations

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.animation.Animation
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mesto.ridom.mestoridom.dpToPixels

fun hideTopText(context: Context, hint1: TextView, hint2: TextView, recyclerView: RecyclerView, editText: FrameLayout, duration: Long) {
    val topTranslation = context.dpToPixels(60)
    AnimatorSet().apply {
        playTogether(
                *listOf(
                        ObjectAnimator.ofFloat(hint1, "translationY", hint1.translationY, hint1.translationY - topTranslation),
                        ObjectAnimator.ofFloat(hint2, "translationY", hint2.translationY, hint2.translationY - topTranslation),
                        ObjectAnimator.ofFloat(recyclerView, "translationY", recyclerView.translationY, recyclerView.translationY - topTranslation),
                        ObjectAnimator.ofFloat(editText, "translationY", editText.translationY, editText.translationY - topTranslation),
                        ObjectAnimator.ofFloat(hint1, "alpha", hint1.alpha, 0f),
                        ObjectAnimator.ofFloat(hint2, "alpha", hint2.alpha, 0f),
                        ObjectAnimator.ofFloat(hint1, "scaleX", hint1.scaleX, hint1.scaleX * .3f),
                        ObjectAnimator.ofFloat(hint2, "scaleX", hint2.scaleX, hint2.scaleX * .3f),
                        ObjectAnimator.ofFloat(hint1, "scaleY", hint1.scaleY, hint1.scaleY * .3f),
                        ObjectAnimator.ofFloat(hint2, "scaleY", hint2.scaleY, hint2.scaleY * .3f)
                ).map {
                    it.apply {
                        this.duration = duration
                    }
                }.toTypedArray()
        )
        //start()
    }
}