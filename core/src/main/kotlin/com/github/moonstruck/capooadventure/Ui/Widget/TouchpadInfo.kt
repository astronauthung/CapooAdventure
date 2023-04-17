package com.github.moonstruck.capooadventure.Ui.Widget

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.github.moonstruck.capooadventure.Ui.View.GameView
import ktx.actors.plusAssign
import ktx.scene2d.*

class TouchpadInfo(skin: Skin) : WidgetGroup(), KGroup {
    private val touchBackground : Image = Image(skin.getDrawable("touch_pad"))
    private val touchKnob : Image = Image(skin.getDrawable("touch_knob"))

    init {
        this += touchBackground
        this += touchKnob
    }

    override fun getPrefWidth() = touchBackground.drawable.minWidth

    override fun getPrefHeight() = touchBackground.drawable.minHeight
}

@Scene2dDsl
fun <S> KWidget<S>.touchpadInfo(
    skin: Skin = Scene2DSkin.defaultSkin,
    init: TouchpadInfo.(S) -> Unit = {}
) : TouchpadInfo = actor(TouchpadInfo(skin),init)
