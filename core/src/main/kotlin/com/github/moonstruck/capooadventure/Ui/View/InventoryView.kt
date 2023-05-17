package com.github.moonstruck.capooadventure.Ui.View

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.utils.Align
import com.github.moonstruck.capooadventure.Ui.Drawables
import com.github.moonstruck.capooadventure.Ui.Labels
import com.github.moonstruck.capooadventure.Ui.Widget.InventoryDragSource
import com.github.moonstruck.capooadventure.Ui.Widget.InventoryDragTarget
import com.github.moonstruck.capooadventure.Ui.Widget.InventorySlot
import com.github.moonstruck.capooadventure.Ui.Widget.inventorySlot
import com.github.moonstruck.capooadventure.Ui.get
import com.github.moonstruck.capooadventure.Ui.model.InventoryModel
import com.github.moonstruck.capooadventure.Ui.model.ItemModel
import ktx.actors.plusAssign
import ktx.scene2d.*

class InventoryView (
    private val model: InventoryModel,
    skin: Skin
) : KTable, Table(skin) {

    //store item
    private val invSlots = mutableListOf<InventorySlot>()
    private val gearSlots = mutableListOf<InventorySlot>()


    init {
        //UI
        val titlePading = 15f

        setFillParent(true)
        //set 2 table, 1 in left (all item can equip), another one in right side (gear for player)

        //table 1
        table {inventoryTableCell ->
            background = skin[Drawables.FRAME_BGD]

            label(text = "Inventory", style = Labels.TITLE.skinKey, skin) {
                this.setAlignment(Align.center)
                //position of box text INVENTORY
                it.expandX().fill()
                    .pad(-35f, titlePading, 0f, titlePading)
                    .top()
                    .row()
            }
            table {invSlotTableCell ->
                for (i in 1..18) {
                    this@InventoryView.invSlots += inventorySlot(skin = skin) { slotCell ->
                        slotCell.padBottom(2f)
                        //ngat slot
                        if (i%6 == 0) {
                            slotCell.row()
                        } else {
                            slotCell.padRight(2f)
                        }
                    }
                }
                inventoryTableCell.expand().fill()
            }
            //size of BGD
            inventoryTableCell.expand().width(200f).height(120f).left().center()
        }
        //table 2

        table {gearTableCell ->
            background = skin[Drawables.FRAME_BGD]
            label(text = "Gear", style = Labels.TITLE.skinKey, skin) {
                this.setAlignment(Align.center)
                //position of box text INVENTORY
                it.expandX().fill()
                    .pad(-10f, titlePading, 0f, titlePading)
                    .top()
                    .row()
            }
            table { gearInnerTableCell ->
                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_HELMET, skin) {
                    it.padBottom(2f).colspan(2).row()
                }
                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_WEAPON, skin) {
                    it.padBottom(2f).padRight(2f)
                }
                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_ARMOR, skin) {
                    it.padBottom(2f).row()
                }
                this@InventoryView.gearSlots += inventorySlot(Drawables.INVENTORY_SLOT_BOOTS, skin) {
                    it.padBottom(2f).colspan(2).row()
                }

                gearInnerTableCell.expand().fill()
            }

            gearTableCell.expand().width(90f).height(120f).left().center()

        }

        setupDragAndDrop()
        //data binding


        //equipped item
        model.onPropertyChange(InventoryModel::playerItems) { itemModels ->
            clearInventoryAndGear()
            itemModels.forEach {
                if (it.equipped) {
                    gear(it)
                } else {
                    item(it)
                }
            }

        }
    }

    private fun clearInventoryAndGear() {
        invSlots.forEach { it.item(null) }
        gearSlots.forEach { it.item(null) }
    }

    private fun setupDragAndDrop() {
        val dnd = DragAndDrop()
        dnd.setDragActorPosition(
            InventoryDragSource.DRAG_ACTOR_SIZE*0.5f,
            -InventoryDragSource.DRAG_ACTOR_SIZE*0.5f
        )
        invSlots.forEach { slot ->
            dnd.addSource(InventoryDragSource(slot))
            dnd.addTarget(InventoryDragTarget(slot, ::onItemDropped))
        }
        gearSlots.forEach { slot ->
            dnd.addSource(InventoryDragSource(slot))
            dnd.addTarget(InventoryDragTarget(slot, ::onItemDropped, slot.supportedCategory))
        }
    }
    private fun onItemDropped (
        sourceSlot: InventorySlot,
        targetSlot: InventorySlot,
        itemModel: ItemModel
    ) {
        if (sourceSlot == targetSlot) {
            return
        }

        //swap items
        sourceSlot.item(targetSlot.itemModel)
        targetSlot.item(itemModel)

        //update model
        val sourceItem = sourceSlot.itemModel
        if(sourceSlot.isGear){
            model.equip(itemModel,false)
            if(sourceItem!=null){
                model.equip(sourceItem,true)
            }
        }else if(sourceItem!= null){
            model.inventoryItem(invSlots.indexOf(sourceSlot),sourceItem)
        }

        if(targetSlot.isGear){
            if(sourceItem!=null){
                model.equip(sourceItem,false)
            }
            model.equip(itemModel,true)
        }else{
            model.inventoryItem(invSlots.indexOf(targetSlot),itemModel)
        }
    }

    fun item(itemModel: ItemModel) {
        invSlots[itemModel.slotIdx].item(itemModel)
    }
    fun gear(itemModel: ItemModel) {
        gearSlots.firstOrNull{it.supportedCategory == itemModel.category}?.item(itemModel)
    }
}

@Scene2dDsl
fun <S> KWidget<S>.inventoryView (
    model: InventoryModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: InventoryView.(S) -> Unit = {}
): InventoryView = actor(InventoryView(model, skin), init)
