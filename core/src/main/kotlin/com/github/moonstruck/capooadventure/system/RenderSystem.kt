package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.CapooAdventure.Companion.UNIT_SCALE
import com.github.moonstruck.capooadventure.component.ImageComponent
import com.github.moonstruck.capooadventure.event.MapChangeEvent
import com.github.quillraven.fleks.*
import com.github.quillraven.fleks.collection.compareEntity
import ktx.graphics.use
import ktx.tiled.forEachLayer


@AllOf([ImageComponent::class])

class RenderSystem(
    @Qualifier("GameStage") private val gameStage: Stage,
    @Qualifier("UiStage") private val uiStage: Stage,
    private val imageCmps: ComponentMapper<ImageComponent>
) : EventListener, IteratingSystem(
    comparator = compareEntity { e1, e2 -> imageCmps[e1].compareTo(imageCmps[e2]) }
)
{
    private val bgdLayers = mutableListOf<TiledMapTileLayer>()
    private val fgdLayers = mutableListOf<TiledMapTileLayer>()
    private val mapRenderer = OrthogonalTiledMapRenderer(null, UNIT_SCALE, gameStage.batch)
    private val orthocam = gameStage.camera as OrthographicCamera

    override fun onTick() {
        super.onTick()

            gameStage.viewport.apply()

            AnimatedTiledMapTile.updateAnimationBaseTime()
            mapRenderer.setView(orthocam)

            if (bgdLayers.isNotEmpty()) {
                gameStage.batch.use(orthocam.combined) {
                    bgdLayers.forEach { mapRenderer.renderTileLayer(it) }
                }
            }
            gameStage.run {
                act(deltaTime)
                draw()
            }

            if (fgdLayers.isNotEmpty()) {
                gameStage.batch.use(orthocam.combined) {
                    fgdLayers.forEach { mapRenderer.renderTileLayer(it) }
                }
            }

        uiStage.run {
            viewport.apply()
            act(deltaTime)
            draw()
        }

    }

    override fun onTickEntity(entity: Entity) {
        imageCmps[entity].image.toFront()
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is MapChangeEvent -> {
            mapRenderer.map = event.map
            bgdLayers.clear()
            fgdLayers.clear()

            event.map.forEachLayer<TiledMapTileLayer> { layer ->
                if (layer.name.startsWith("fgd_")) {
                    fgdLayers.add(layer)
                } else {
                    bgdLayers.add(layer)
                }
            }
            return true
            }
        }
        return false
    }

    override fun onDispose() {
        super.onDispose()
    }
}


