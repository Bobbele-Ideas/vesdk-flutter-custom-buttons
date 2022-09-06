package com.example.custom_view_demo

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.MainThread
import ly.img.android.pesdk.annotations.OnEvent
import ly.img.android.pesdk.ui.model.data.TitleData
import ly.img.android.pesdk.ui.model.state.UiState
import ly.img.android.pesdk.ui.model.state.UiStateMenu
import ly.img.android.pesdk.ui.widgets.ImgLyTitleBar

internal class TitleBarReplacement @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImgLyTitleBar(context, attrs, defStyleAttr) {
    private val titleBox = findViewById<View>(R.id.actionBarTitleBox)
    private val linkedLogo = findViewById<ImageView>(R.id.logo)
    private val uiStateMenu = stateHandler[UiStateMenu::class]

    init {
        UiState.replaceTitle(TitleData(UiStateMenu.MAIN_TOOL_ID, ""))

        linkedLogo.setOnClickListener {
           uiStateMenu.notifySaveClicked()
        }
    }

    @OnEvent(UiStateMenu.Event.TOOL_STACK_CHANGED)
    @MainThread
    fun onToolChange(uiStateMenu: UiStateMenu) {
        val acceptButton = findViewById<View>(R.id.acceptButton)
        val discardButton = findViewById<View>(R.id.cancelButton)
        val animatableViews = listOf<View>(acceptButton, discardButton, linkedLogo)

        if (uiStateMenu.currentPanelData.id == UiStateMenu.MAIN_TOOL_ID) {
            ObjectAnimator.ofFloat(linkedLogo, "alpha", linkedLogo.alpha, 1f).also {
                it.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                        linkedLogo.visibility = View.VISIBLE
                    }

                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {}
                })
                it.duration = 300
                it.start()
            }
            discardButton.alpha = 0f
            acceptButton.alpha = 0f

            titleBox.visibility = GONE
            acceptButton.visibility = GONE
            discardButton.visibility = GONE
        } else {
            animatableViews.forEach { view ->
                val isLinkedLogo = view == linkedLogo
                ObjectAnimator.ofFloat(view, "alpha", view.alpha, if (isLinkedLogo) 0f else 1f).also {
                    it.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {
                            view.visibility = if (isLinkedLogo) VISIBLE else GONE
                        }

                        override fun onAnimationRepeat(animation: Animator?) {}
                        override fun onAnimationCancel(animation: Animator?) {}
                        override fun onAnimationEnd(animation: Animator?) {
                            view.visibility = if (isLinkedLogo) GONE else VISIBLE
                        }
                    })
                    it.duration = 300
                    it.start()
                }
            }
            titleBox.visibility = View.VISIBLE
        }
    }

    @OnEvent(UiStateMenu.Event.ENTER_TOOL)
    @MainThread
    override fun onMenuStateEnter() = super.onMenuStateEnter()

    @OnEvent(UiStateMenu.Event.LEAVE_TOOL, UiStateMenu.Event.LEAVE_AND_REVERT_TOOL)
    @MainThread
    override fun onMenuStateLeave() = super.onMenuStateLeave()
}