package com.github.moonstruck.capooadventure.Ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.component.InventoryComponent
import com.github.moonstruck.capooadventure.component.ItemComponent
import com.github.moonstruck.capooadventure.component.PlayerComponent
import com.github.moonstruck.capooadventure.event.EntityAddItemEvent
import com.github.moonstruck.capooadventure.input.PlayerInputProcessor
import com.github.quillraven.fleks.Qualifier

class InventoryModel(
    val world: com.github.quillraven.fleks.World,
    @Qualifier("GameStage") val gameStage: Stage,
//    private val playerInputProcessor: PlayerInputProcessor,
    ) : PropertyChangeSource(), EventListener {

    private val playerCmps = world.mapper<PlayerComponent>()
    private val inventoryCmps = world.mapper<InventoryComponent>()
    private val itemCmps = world.mapper<ItemComponent>()
//    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    var playerItems by propertyNotify(listOf<ItemModel>())

    init {
        gameStage.addListener(this)
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is EntityAddItemEvent -> {
                if (event.entity in playerCmps) {
                    playerItems =  inventoryCmps[event.entity].items.map {
                        val itemCmp = itemCmps[it]
                        ItemModel(
                            it.id,
                            itemCmp.itemType.category,
                            itemCmp.itemType.uiAtlasKey,
                            itemCmp.slotIdx,
                            itemCmp.equipped
                        )
                    }
                }
            }
            else -> return false
        }

        return true
    }
}
