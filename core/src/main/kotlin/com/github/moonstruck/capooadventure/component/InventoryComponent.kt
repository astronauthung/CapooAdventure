package com.github.moonstruck.capooadventure.component

import com.github.quillraven.fleks.Entity

class InventoryComponent {
    val items = mutableListOf<Entity>()
    val itemsToAdd = mutableListOf<ItemType>()

    companion object {
        const val INVENTORY_CAPACITY = 18
    }
}

