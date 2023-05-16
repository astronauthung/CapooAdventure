package com.github.moonstruck.capooadventure.Ui.Widget

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Scaling
import com.github.moonstruck.capooadventure.Ui.Drawables
import com.github.moonstruck.capooadventure.Ui.get
import com.github.moonstruck.capooadventure.Ui.model.ItemModel
import com.github.moonstruck.capooadventure.component.ItemCategory
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.scene2d.*

class InventorySlot (
    private val slotItemBgd: Drawables?,
    private val skin: Skin
    ) : WidgetGroup(), KGroup {

        private val background = Image(skin[Drawables.INVENTORY_SLOT])
        private val slotItemInfo: Image? = if (slotItemBgd==null)null else Image(skin[slotItemBgd])
        private val itemImage = Image()

        var itemModel: ItemModel? = null

        val itemDrawable: Drawable
            get() = itemImage.drawable

        val isGear: Boolean
            get() = supportedCategory != ItemCategory.UNDEFINED

        val isEmpty: Boolean
            get() = itemModel == null

        val itemCategory: ItemCategory
            get() = itemModel?.category?: ItemCategory.UNDEFINED

        val supportedCategory: ItemCategory
            get() {
                return when (slotItemBgd) {
                    Drawables.INVENTORY_SLOT_HELMET -> ItemCategory.HELMET
                    Drawables.INVENTORY_SLOT_ARMOR -> ItemCategory.ARMOR
                    Drawables.INVENTORY_SLOT_BOOTS -> ItemCategory.BOOTS
                    Drawables.INVENTORY_SLOT_WEAPON -> ItemCategory.WEAPON
                    else -> ItemCategory.UNDEFINED
                }
            }

        init {
            this += background
            slotItemInfo?.let { info ->
                this += info.apply {
                    alpha = 0.33f
                    setPosition(3f, 3f)
                    setSize(14f,14f)
                    setScaling(Scaling.contain)
                }
            }
            this += itemImage.apply {
                setPosition(3f, 3f)
                setSize(14f,14f)
                setScaling(Scaling.contain)
            }
        }

    //size of background
    override fun getPrefWidth(): Float = background.drawable.minWidth

    override fun getPrefHeight(): Float = background.drawable.minHeight

    fun item(model: ItemModel?) {
        itemModel = model
        if (model == null) {
            itemImage.drawable = null
        } else {
            itemImage.drawable = skin.getDrawable(model.atlasKey)
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.inventorySlot (
    slotItemBgd:Drawables? = null,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: InventorySlot.(S) -> Unit = {}
) : InventorySlot = actor(InventorySlot(slotItemBgd, skin), init)
