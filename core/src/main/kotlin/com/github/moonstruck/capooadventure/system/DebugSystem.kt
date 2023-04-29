package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.system.AttackSystem.Companion.AABB_RECT
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import ktx.assets.disposeSafely
import ktx.graphics.use


class DebugSystem(
    private val physicWorld: World,
    @Qualifier("GameStage") private val stage: Stage,
) : IntervalSystem(enabled = true) {
    private lateinit var physicRenderer : Box2DDebugRenderer
    private lateinit var shapeRenderer: ShapeRenderer
    private val profiler by lazy { GLProfiler(Gdx.graphics).apply { enable() } }
    private val camera = stage.camera


    init {
        if(enabled){
            physicRenderer = Box2DDebugRenderer()
            shapeRenderer = ShapeRenderer()
        }
    }

    override fun onTick() {
        stage.isDebugAll = true
        Gdx.graphics.setTitle(
            buildString {
                append("FPS:${Gdx.graphics.framesPerSecond},")
                append("DrawCalls:${profiler.drawCalls},")
                append("Binds:${profiler.textureBindings},")
                append("Entities:${world.numEntities}")
            }
        )
        physicRenderer.render(physicWorld, camera.combined)
        shapeRenderer.use(ShapeRenderer.ShapeType.Line,stage.camera.combined){
            it.setColor(1f,0f,0f,0f)
            it.rect(AABB_RECT.x, AABB_RECT.y, AABB_RECT.width - AABB_RECT.x, AABB_RECT.height - AABB_RECT.y)
        }
        profiler.reset()
    }

    override fun onDispose() {
        if (enabled) {
            physicRenderer.disposeSafely()
            shapeRenderer.disposeSafely()
        }
    }
}
