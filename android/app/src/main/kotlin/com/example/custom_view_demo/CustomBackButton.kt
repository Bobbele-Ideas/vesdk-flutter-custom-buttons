package com.example.custom_view_demo

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.MainThread
import androidx.appcompat.widget.AppCompatImageButton
import ly.img.android.pesdk.annotations.OnEvent
import ly.img.android.pesdk.backend.model.state.manager.StateHandler
import ly.img.android.pesdk.ui.model.state.UiStateMenu

class CustomBackButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageButton(context, attrs), View.OnClickListener {

    private var stateHandler = if (!isInEditMode) {
        StateHandler.findInViewContext(context)
    } else StateHandler(context)

    private val uiStateMenu = stateHandler[UiStateMenu::class]

    init {
        setImageResource(R.drawable.imgly_icon_back)
        setOnClickListener(this)
    }

    @MainThread
    @OnEvent(value = [UiStateMenu.Event.ENTER_TOOL, UiStateMenu.Event.LEAVE_TOOL, UiStateMenu.Event.LEAVE_AND_REVERT_TOOL], triggerDelay = 30)
    fun onToolChanged() {
        visibility = if (UiStateMenu.MAIN_TOOL_ID == uiStateMenu.currentPanelData.id) {
            VISIBLE
        } else {
            GONE
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        stateHandler.registerSettingsEventListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stateHandler.unregisterSettingsEventListener(this)
    }

    override fun onClick(v: View) {
        uiStateMenu.notifyCloseClicked()
    }
}