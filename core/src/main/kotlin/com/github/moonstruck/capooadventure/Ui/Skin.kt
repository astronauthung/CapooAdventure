package com.github.moonstruck.capooadventure.Ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin
import ktx.style.touchpad


enum class Drawables(
    val atlasKey:String,
){
    CHAR_INFO_BGD("avatar"),
    PLAYER("player"),
    LIFE_BAR("blood"),
    MANA_BAR("energy"),
}
operator fun Skin.get(drawable: Drawables): Drawable = this.getDrawable(drawable.atlasKey)

fun loadSkin()
{
//    return skin(TextureAtlas("ui/ui.atlas"))
//    {
////        skin->
////        touchpad {
////            background = skin.getDrawable("touch_pad")
////            knob = skin.getDrawable("touch_knob")
////        }
//    }.also {
//        Scene2DSkin.defaultSkin = it
//    }
    Scene2DSkin.defaultSkin = skin(TextureAtlas("ui/ui.atlas")){
        label {
            font = BitmapFont(Gdx.files.internal("damage.fnt"))
        }
    }
}

fun disposeSkin()
{
    Scene2DSkin.defaultSkin.disposeSafely()
}
