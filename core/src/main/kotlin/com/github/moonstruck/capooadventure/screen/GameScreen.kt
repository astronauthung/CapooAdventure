package com.github.moonstruck.capooadventure.screen


//import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
//import com.badlogic.gdx.graphics.g2d.TextureRegion
//import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
//import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.moonstruck.capooadventure.component.AnimationActor
import com.github.moonstruck.capooadventure.component.AnimationComponent
import com.github.moonstruck.capooadventure.component.AnimationType
import com.github.moonstruck.capooadventure.component.ImageComponent
import com.github.moonstruck.capooadventure.event.MapChangeEvent
import com.github.moonstruck.capooadventure.event.fire
import com.github.moonstruck.capooadventure.system.AnimationSystem
import com.github.moonstruck.capooadventure.system.EntitySpawnSystem
import com.github.moonstruck.capooadventure.system.RenderSystem
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger
import java.time.Clock.system

class GameScreen : KtxScreen {
    private val stage : Stage = Stage(ExtendViewport(16f, 9f))
    private val textureAtlas = TextureAtlas("game.atlas")
    private var currentMap: TiledMap? = null

    private val world: World = World{
        inject(stage)
        inject(textureAtlas)
        componentListener<ImageComponent.Companion.ImageComponentListener>()

        system<EntitySpawnSystem>()
        system<AnimationSystem>()
        system<RenderSystem>()
    }
    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }


    override fun show() {
        log.debug { "GameScreen get shown" }

        world.systems.forEach { system ->
            if (system is EventListener) {
                stage.addListener(system)
            }
        }
        currentMap = TmxMapLoader().load("map.tmx")
        stage.fire(MapChangeEvent(currentMap!!))
    }
    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        stage.dispose()
        textureAtlas.dispose()
        world.dispose()
        currentMap.disposeSafely()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}

