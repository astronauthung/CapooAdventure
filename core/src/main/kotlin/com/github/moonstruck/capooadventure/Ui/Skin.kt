package com.github.moonstruck.capooadventure.Ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin
import ktx.style.touchpad

fun loadSkin() : Skin
{
    return skin(TextureAtlas("ui/ui.atlas"))
    {
        skin->
        touchpad {
            background = skin.getDrawable("touch_pad")
            knob = skin.getDrawable("touch_knob")
        }
    }.also {
        Scene2DSkin.defaultSkin = it
    }
}

fun disposeSkin()
{
    Scene2DSkin.defaultSkin.disposeSafely()
}
