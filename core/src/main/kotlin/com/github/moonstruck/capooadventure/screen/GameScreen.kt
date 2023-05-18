package com.github.moonstruck.capooadventure.screen


import box2dLight.Light
import box2dLight.RayHandler
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.EventListener
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
import com.github.moonstruck.capooadventure.Ui.View.*
import com.github.moonstruck.capooadventure.Ui.model.GameModel
import com.github.moonstruck.capooadventure.Ui.model.InventoryModel
import com.github.moonstruck.capooadventure.component.*
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
    private val rayHandler = RayHandler(phWorld).apply {
        //set loai anh sang
        RayHandler.useDiffuseLight(true)

        Light.setGlobalContactFilter(LightComponent.playerCategory, 1, LightComponent.enviromentCategory)

        //set mau hoa tron
        setAmbientLight(Color.ROYAL)
    }

    private val eWorld = world{
        injectables {
            add(phWorld)
            add("GameStage",gameStage)
            add("UiStage",uiStage)
            add(textureAtlas)
            add(rayHandler)
        }

        components {
            add<ImageComponent.Companion.ImageComponentListener>()
            add<PhysicComponent.Companion.PhysicComponentListener>()
            add<FloatingTextComponent.Companion.FloatingTextComponentListener>()
            add<StateComponent.Companion.StateComponentListener>()
            add<AiComponent.Companion.AiComponentListener>()
            add<LightComponent.Companion.LightComponentListener>()
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
            add<LightSystem>()
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
            gameView(GameModel(eWorld,gameStage, playerInputProcessor = PlayerInputProcessor(eWorld,gameStage,uiStage)))
            inventoryView(InventoryModel(eWorld,gameStage)){
                this.isVisible=false
            }
            pauseView { this.isVisible = false }
        }

        eWorld.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
            }
        }
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

    private fun pauseWorld(pause:Boolean){
        val mandatorySystems = setOf(
            AnimationSystem::class,
            CameraSystem::class,
            RenderSystem::class,
            DebugSystem::class,
        )

        eWorld.systems
            .filter { it::class !in mandatorySystems }
            .forEach{
                it.enabled = !pause
            }
        uiStage.actors.filterIsInstance<PauseView>().first().isVisible = pause
    }

    override fun pause()  = pauseWorld(true)

    override fun resume() = pauseWorld(false)

    override fun resize(width: Int, height: Int) {
        val screenX = gameStage.viewport.screenX
        val screenY = gameStage.viewport.screenY
        val screenW = gameStage.viewport.screenWidth
        val screenH = gameStage.viewport.screenHeight
        rayHandler.useCustomViewport(screenX, screenY, screenW, screenH)
    }

    override fun render(delta: Float) {
        val dt = delta.coerceAtMost(0.25f)
        GdxAI.getTimepiece().update(dt)
        eWorld.update(dt)
    }

    override fun dispose() {
        textureAtlas.disposeSafely()
        eWorld.dispose()
        currentMap.disposeSafely()
        phWorld.disposeSafely()
        rayHandler.disposeSafely()
        disposeSkin()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}

