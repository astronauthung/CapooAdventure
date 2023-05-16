package com.github.moonstruck.capooadventure.Ui.model

import com.github.moonstruck.capooadventure.component.ItemCategory

class ItemModel (
    val itemEntityId: Int,
    val category: ItemCategory,
    val atlasKey: String,
    var slotIdx: Int,
    var equipped: Boolean
)
