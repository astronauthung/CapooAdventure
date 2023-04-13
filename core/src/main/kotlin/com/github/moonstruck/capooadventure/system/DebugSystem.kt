package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem
import ktx.assets.disposeSafely

class DebugSystem (
    private val phWorld: World,
    private val stage: Stage
) :IntervalSystem(enabled = true) {

    private lateinit var physicRenderer: Box2DDebugRenderer

    init {
        if (enabled) {
            physicRenderer = Box2DDebugRenderer()
        }
    }

    override fun onTick() {
        physicRenderer.render(phWorld, stage.camera.combined)
    }
    override fun onDispose() {
        if (enabled) {
            physicRenderer.disposeSafely()
        }
    }
}
