package com.github.moonstruck.capooadventure.screen


import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.github.moonstruck.capooadventure.Ui.View.GameView
import com.github.moonstruck.capooadventure.Ui.disposeSkin
import com.github.moonstruck.capooadventure.Ui.loadSkin
import com.github.moonstruck.capooadventure.event.MapChangeEvent
import com.github.moonstruck.capooadventure.event.fire
import com.github.moonstruck.capooadventure.system.*
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors
import com.github.moonstruck.capooadventure.CapooAdventure
import com.github.moonstruck.capooadventure.Ui.View.gameView
import com.github.moonstruck.capooadventure.Ui.View.inventoryView
import com.github.moonstruck.capooadventure.Ui.model.GameModel
import com.github.moonstruck.capooadventure.Ui.model.InventoryModel
import com.github.moonstruck.capooadventure.component.*
import com.github.moonstruck.capooadventure.component.FloatingTextComponent.*
import com.github.moonstruck.capooadventure.input.PlayerInputProcessor
import com.github.moonstruck.capooadventure.input.gdxInputProcessor
import com.github.quillraven.fleks.ComponentMapper

class GameScreen(game : CapooAdventure) : KtxScreen {
    private val gameStage = game.gameStage
    private val uiStage = game.uiStage
    private val textureAtlas = TextureAtlas("game.atlas")
    private var currentMap: TiledMap? = null
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    private val eWorld = world{
        injectables {
            add(phWorld)
            add("GameStage",gameStage)
            add("UiStage",uiStage)
            add(textureAtlas)
        }

        components {
            add<ImageComponent.Companion.ImageComponentListener>()
            add<PhysicComponent.Companion.PhysicComponentListener>()
            add<FloatingTextComponent.Companion.FloatingTextComponentListener>()
            add<StateComponent.Companion.StateComponentListener>()
            add<AiComponent.Companion.AiComponentListener>()
        }

        systems {
            add<EntitySpawnSystem>()
            add<CollisionSpawnSystem>()
            add<CollisionDespawnSystem>()
            add<MoveSystem>()
            add<AttackSystem>()
            add<LootSystem>()
            add<InventorySystem>()
            add<DeadSystem>()
            add<LifeSystem>()
            add<PhysicSystem>()
            add<AnimationSystem>()
            add<StateSystem>()
            add<AiSystem>()
            add<CameraSystem>()
            add<FloatingTextSystem>()
            add<RenderSystem>()
            add<AudioSystem>()
            add<DebugSystem>()
        }
    }

    init {
        loadSkin()

        uiStage.actors {
            gameView(GameModel(eWorld,gameStage, playerInputProcessor = PlayerInputProcessor(eWorld)))
            inventoryView(InventoryModel(eWorld,gameStage)){
                this.isVisible=true
            }
        }

        eWorld.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
            }
        }
    }
    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
    }


    override fun show() {
        log.debug { "GameScreen get shown" }

        currentMap = TmxMapLoader().load("map.tmx")
        gameStage.fire(MapChangeEvent(currentMap!!))

//        uiStage.actors {
//            gameView(GameModel(eWorld,gameStage, PlayerInputProcessor(eWorld)))
//        }
        gdxInputProcessor(uiStage)

    }
    override fun render(delta: Float) {
        val dt = delta.coerceAtMost(0.25f)
        GdxAI.getTimepiece().update(dt)
        eWorld.update(delta.coerceAtMost(0.25f))
    }

    override fun dispose() {
        gameStage.disposeSafely()
        textureAtlas.disposeSafely()
        eWorld.dispose()
        currentMap.disposeSafely()
        phWorld.disposeSafely()
        disposeSkin()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}

