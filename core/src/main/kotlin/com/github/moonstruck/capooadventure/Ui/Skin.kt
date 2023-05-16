package com.github.moonstruck.capooadventure.Ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.github.moonstruck.capooadventure.Ui.get
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.set
import ktx.style.skin
import ktx.style.touchpad


enum class Drawables(
    val atlasKey:String,
){
    CHAR_INFO_BGD("avatar"),
    PLAYER("player"),
    SLIME("slime"),
    LIFE_BAR("blood"),
    MANA_BAR("energy"),
    FRAME_BGD("frame_bgd"),
    FRAME_FGD("frame_fgd1"),
}
operator fun Skin.get(drawable: Drawables): Drawable = this.getDrawable(drawable.atlasKey)

enum class Labels{
    FRAME;

    val skinKey = this.name.lowercase()
}

enum class Fonts(
    val atlasRegionKey: String,
    val scaling : Float,
){
    DEFAULT("font1",0.25f);

    val skinKey = "Font_${this.name.lowercase()}"
    val fontPath = "ui/${this.atlasRegionKey}.fnt"
}
operator fun Skin.get(font: Fonts): BitmapFont = this.getFont(font.skinKey)

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
    Scene2DSkin.defaultSkin = skin(TextureAtlas("ui/ui.atlas")){skin->
        Fonts.values().forEach { fnt->
            skin[fnt.skinKey] = BitmapFont(Gdx.files.internal(fnt.fontPath)).apply {
                data.setScale(fnt.scaling)
                data.markupEnabled = true
            }
        }

        label(Labels.FRAME.skinKey) {
            font = skin[Fonts.DEFAULT]
            background = skin[Drawables.FRAME_FGD].apply{
                leftWidth = 3f
                rightWidth = 2f
                topHeight = 1f
            }
        }
    }
}

fun disposeSkin()
{
    Scene2DSkin.defaultSkin.disposeSafely()
}
