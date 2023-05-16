package com.github.moonstruck.capooadventure.Ui.Widget

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload
import com.github.moonstruck.capooadventure.Ui.model.ItemModel
import com.github.moonstruck.capooadventure.component.ItemCategory

class InventoryDragSource (
    val inventorySlot: InventorySlot
) : Source(inventorySlot) {

    val isGear: Boolean
        get() = inventorySlot.isGear

    val supportedCategory: ItemCategory
        get() = inventorySlot.supportedCategory

    override fun dragStart(
        event: InputEvent?,
        x: Float,
        y: Float,
        pointer: Int
    ): Payload? {
        if (inventorySlot.itemModel == null) {
            return null
        }
        return Payload().apply {
            //lay cac object
            `object` = inventorySlot.itemModel
            dragActor = Image(inventorySlot.itemDrawable).apply {
                setSize(DRAG_ACTOR_SIZE, DRAG_ACTOR_SIZE)
            }
            inventorySlot.item(null,)
        }
    }

    override fun dragStop(
        event: InputEvent,
        x: Float,
        y: Float,
        pointer: Int,
        payload: Payload,
        target: Target?
    ) {
        if (target == null) {
            inventorySlot.item(payload.`object` as ItemModel)
        }
    }
    companion object {
        const val DRAG_ACTOR_SIZE = 22f
    }
}

class InventoryDragTarget (
    private val inventorySlot: InventorySlot,
    private val onDrop: (sourceSlot: InventorySlot, targetSlot: InventorySlot, itemModel: ItemModel) -> Unit,
    private val supportedItemCategory: ItemCategory? = null,
) : Target(inventorySlot) {

    private val isGear: Boolean
        get() = supportedItemCategory != null

    private fun isSupported(category: ItemCategory) : Boolean = supportedItemCategory == category
    //keo vat pham, neu ko hop le thi tra lai vi tri cu
    override fun drag(
        source: Source,
        payload: Payload,
        x: Float,
        y: Float,
        pointer: Int
    ): Boolean {
        val itemModel = payload.`object` as ItemModel
        val dragSource = source as InventoryDragSource
        val srcCategory = dragSource.supportedCategory

        return if (isGear && isSupported(itemModel.category)) {
            true
        } else if (!isGear && dragSource.isGear && (inventorySlot.isEmpty || inventorySlot.itemCategory == srcCategory)) {
            true
        } else if (!isGear && !dragSource.isGear) {
            true
        } else {
            payload.dragActor.color = Color.RED
            false
        }
    }

    override fun reset(source: Source, payload: Payload) {
        payload.dragActor.color = Color.WHITE
    }

    override fun drop(
        source: Source,
        payload: Payload,
        x: Float,
        y: Float,
        pointer: Int
    ) {
        onDrop (
            (source as InventoryDragSource).inventorySlot,
            actor as InventorySlot,
            payload.`object` as ItemModel
        )
    }

}
