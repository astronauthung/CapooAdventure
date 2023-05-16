package com.github.moonstruck.capooadventure.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.moonstruck.capooadventure.Ui.View.InventoryView
import com.github.moonstruck.capooadventure.Ui.View.inventoryView
import com.github.moonstruck.capooadventure.Ui.model.InventoryModel
import com.github.moonstruck.capooadventure.Ui.model.ItemModel
import com.github.moonstruck.capooadventure.component.InventoryComponent
import com.github.moonstruck.capooadventure.component.ItemCategory
import com.github.moonstruck.capooadventure.component.PlayerComponent
import com.github.moonstruck.capooadventure.input.PlayerInputProcessor
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.scene2d.actors

class InventoryUiScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(320f,180f))
    private val eWorld = world{}
    private val playerEntity : Entity
//    private val model = InventoryModel(world {},stage, playerInputProcessor = PlayerInputProcessor(world {}))
    private val model = InventoryModel(eWorld,stage)
    private lateinit var inventoryView: InventoryView
    init {
        playerEntity = eWorld.entity{
            add<PlayerComponent>()
            add<InventoryComponent>()
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width,height,true)
    }
    override fun show() {
        stage.clear()
        stage.addListener(model)
        stage.actors {
            inventoryView = inventoryView(model)
        }
        Gdx.input.inputProcessor = stage
        stage.isDebugAll = false
    }
    override fun render(delta: Float) {
        if(Gdx.input.isKeyPressed(Input.Keys.R)) {
            hide()
            show()
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            inventoryView.item(ItemModel(-1, ItemCategory.BOOTS, "shoe", 1, false))
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            inventoryView.item(ItemModel(-1, ItemCategory.HELMET, "hat", 3, false))
        }

        stage.act()
        stage.draw()
    }


    override fun dispose() {
        stage.dispose()
    }


}
