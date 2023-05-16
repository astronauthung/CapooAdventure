package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.component.InventoryComponent
import com.github.moonstruck.capooadventure.component.ItemComponent
import com.github.moonstruck.capooadventure.component.ItemType
import com.github.moonstruck.capooadventure.event.EntityAddItemEvent
import com.github.moonstruck.capooadventure.event.fire
import com.github.quillraven.fleks.*


@AllOf([InventoryComponent::class])
class InventorySystem (
    private val inventoryCmps: ComponentMapper<InventoryComponent>,
    private val itemCmps: ComponentMapper<ItemComponent>,
    @Qualifier("GameStage") private val gameStage: Stage,
) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        val inventory = inventoryCmps[entity]
        if (inventory.itemsToAdd.isEmpty()) {
            return
        }

        inventory.itemsToAdd.forEach { itemType ->
            val slotIdx: Int = emptySlotIndex(inventory)
            if (slotIdx == -1) {
                return
            }

            val newItem = spawnItem(itemType, slotIdx)
            inventory.items += newItem
            gameStage.fire(EntityAddItemEvent(entity, newItem))
        }
        inventory.itemsToAdd.clear()
    }

    private fun spawnItem(itemType: ItemType, slotIdx: Int): Entity {
        return world.entity {
            add<ItemComponent> {
                this.itemType = itemType
                this.slotIdx = slotIdx
            }
        }
    }

    private fun emptySlotIndex(inventory: InventoryComponent): Int {
        for (i in 0 until InventoryComponent.INVENTORY_CAPACITY) {
            if (inventory.items.none {itemCmps[it].slotIdx == i}) {
                return i
            }
        }
        return -1
    }
}
