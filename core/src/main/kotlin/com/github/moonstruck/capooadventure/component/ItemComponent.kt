package com.github.moonstruck.capooadventure.component

enum class ItemCategory {
    UNDEFINED,
    HELMET,
    ARMOR,
    BOOTS,
    WEAPON
}

enum class ItemType (
    val category: ItemCategory,
    val uiAtlasKey: String,
) {
    UNDEFINED(ItemCategory.UNDEFINED, ""),
    HELMET(ItemCategory.HELMET, "hat"),
    BOOTS(ItemCategory.BOOTS, "shoe"),
    WEAPON(ItemCategory.WEAPON, "sword"),
    ARMOR(ItemCategory.ARMOR, "pants")
}

class ItemComponent {
    var itemType: ItemType = ItemType.UNDEFINED
    //mac dinh la -1
    var slotIdx: Int = -1
    var equipped: Boolean = false
}
