package com.github.moonstruck.capooadventure.component

import com.github.quillraven.fleks.Entity

data class PortalComponent(
    var id: Int = -1,
    var toMap : String = "",
    var toPortal: Int = -1,
    var triggerEntities : MutableSet<Entity> = mutableSetOf()
) {
}
