package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.component.TiledComponent
import com.github.moonstruck.capooadventure.event.CollisionDespawnEvent
import com.github.moonstruck.capooadventure.event.fire
import com.github.quillraven.fleks.*
import ktx.actors.stage


@AllOf([TiledComponent::class])
class CollisionDespawnSystem (
    private val tiledCmps: ComponentMapper<TiledComponent>,
    @Qualifier("GameStage") private val stage: Stage,
) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        with(tiledCmps[entity]) {
            if (nearbyEntities.isEmpty()) {
                stage.fire(CollisionDespawnEvent(cell))
                world.remove(entity)
            }
        }

    }
}
