package com.github.moonstruck.capooadventure.Ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.github.moonstruck.capooadventure.Ui.get
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.style.*


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

    INVENTORY_SLOT("frame_fgd2"),
    INVENTORY_SLOT_HELMET("hat"),
    INVENTORY_SLOT_WEAPON("sword"),
    INVENTORY_SLOT_ARMOR("pants"),
    INVENTORY_SLOT_BOOTS("shoe"),

    TOUCH_PAD("touch_pad"),
    TOUCH_KNOB("touch_knob"),
    TOUCH_ATTACK("touch_attack"),

    TEST_INVENTORY("frame_fgd2")
}
operator fun Skin.get(drawable: Drawables): Drawable = this.getDrawable(drawable.atlasKey)

enum class Labels{
    FRAME,
    TITLE,
    LARGE;

    val skinKey = this.name.lowercase()
}

enum class Fonts(
    val atlasRegionKey: String,
    val scaling : Float,
){
    DEFAULT("font1",0.25f),
    BIG("font1", 0.5f);

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
        label(Labels.TITLE.skinKey) {
            font = skin[Fonts.BIG]
            fontColor = Color.SLATE
            background = skin[Drawables.FRAME_FGD].apply{
                leftWidth = 3f
                rightWidth = 2f
                topHeight = 1f
            }
        }
        label(Labels.LARGE.skinKey) {
            font = skin[Fonts.BIG]
        }
        touchpad {
            background = skin[Drawables.TOUCH_PAD]
            knob = skin[Drawables.TOUCH_KNOB]
        }
        imageButton {
            imageUp = skin[Drawables.TOUCH_ATTACK]
            imageDown = skin[Drawables.TOUCH_ATTACK]
        }
        imageButton("inventoryOpen"){
            imageUp = skin[Drawables.TEST_INVENTORY]
            imageDown = skin[Drawables.TEST_INVENTORY]
        }
    }
}

fun disposeSkin()
{
    Scene2DSkin.defaultSkin.disposeSafely()
}
