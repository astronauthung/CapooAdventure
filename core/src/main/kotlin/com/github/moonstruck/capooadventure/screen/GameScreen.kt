package com.github.moonstruck.capooadventure.screen


import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.moonstruck.capooadventure.component.*
import com.github.moonstruck.capooadventure.event.MapChangeEvent
import com.github.moonstruck.capooadventure.event.fire
import com.github.moonstruck.capooadventure.system.*
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2

class GameScreen : KtxScreen {
    private val stage : Stage = Stage(ExtendViewport(16f, 9f))
    private val textureAtlas = TextureAtlas("game.atlas")
    private var currentMap: TiledMap? = null
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    private val eWorld: World = World{
        inject(stage)
        inject(textureAtlas)
        inject(phWorld)

        componentListener<ImageComponent.Companion.ImageComponentListener>()
        componentListener<PhysicComponent.Companion.PhysicComponentListener>()

        system<EntitySpawnSystem>()
        system<PhysicSystem>()
        system<AnimationSystem>()
        system<RenderSystem>()
        system<DebugSystem>()
    }
    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }


    override fun show() {
        log.debug { "GameScreen get shown" }

        eWorld.systems.forEach { system ->
            if (system is EventListener) {
                stage.addListener(system)
            }
        }
        currentMap = TmxMapLoader().load("map.tmx")
        stage.fire(MapChangeEvent(currentMap!!))
    }
    override fun render(delta: Float) {
        eWorld.update(delta.coerceAtMost(0.25f))
    }

    override fun dispose() {
        stage.disposeSafely()
        textureAtlas.disposeSafely()
        eWorld.dispose()
        currentMap.disposeSafely()
        phWorld.disposeSafely()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}

