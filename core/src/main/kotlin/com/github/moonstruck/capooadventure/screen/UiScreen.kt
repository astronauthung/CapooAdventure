package com.github.moonstruck.capooadventure.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.hide
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.moonstruck.capooadventure.Ui.Drawables
import com.github.moonstruck.capooadventure.Ui.View.GameView
import com.github.moonstruck.capooadventure.Ui.View.gameView
import com.github.moonstruck.capooadventure.Ui.disposeSkin
import com.github.moonstruck.capooadventure.Ui.loadSkin
import com.github.moonstruck.capooadventure.Ui.model.GameModel
import com.github.moonstruck.capooadventure.component.LifeComponent
import com.github.moonstruck.capooadventure.component.PlayerComponent
import com.github.moonstruck.capooadventure.event.EntityDamageEvent
import com.github.moonstruck.capooadventure.event.fire
import com.github.moonstruck.capooadventure.input.PlayerInputProcessor
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors

class UiScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(320f,180f))
    private val eWorld = world{}
    private val playerEntity : Entity
    private val model = GameModel(world {},stage, playerInputProcessor = PlayerInputProcessor(world {},stage))
    private lateinit var gameView: GameView
    init {
        playerEntity = eWorld.entity{
            add<PlayerComponent>()
            add<LifeComponent>{
                max = 5f
                life = 3f
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width,height,true)
    }
    override fun show() {
        stage.clear()
        stage.addListener(model)
        stage.actors {
          gameView = gameView(model)
        }
        Gdx.input.inputProcessor = stage
        stage.isDebugAll = true
    }
    override fun render(delta: Float) {
        if(Gdx.input.isKeyPressed(Input.Keys.R)) {
            hide()
            show()
        }else if(Gdx.input.isKeyPressed(Input.Keys.E)){
            gameView.popup("You found something [#ff0000]cool[]!")
        }else if(Gdx.input.isKeyPressed(Input.Keys.F)){
            stage.fire(EntityDamageEvent(playerEntity))
        }
        stage.act()
        stage.draw()
    }


    override fun dispose() {
        stage.dispose()
    }


}
