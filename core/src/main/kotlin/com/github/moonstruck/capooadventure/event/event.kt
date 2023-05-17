package com.github.moonstruck.capooadventure.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.Ui.model.GameModel
import com.github.quillraven.fleks.Entity


fun Stage.fire(event: Event) = this.root.fire(event)

data class MapChangeEvent(val map: TiledMap) : Event()

class CollisionDespawnEvent(val cell: Cell) : Event()

data class EntityAttackEvent(val atlasKey: String) : Event()

data class EntityDeathEvent(val atlasKey: String) : Event()

class EntityLootEvent : Event()

class EntityDamageEvent(val entity: Entity) : Event()

class EntityAggroEvent(val entity: Entity): Event()

class EntityAddItemEvent(val entity: Entity, val item: Entity): Event()

class GamePauseEvent:Event()
class GameResumeEvent:Event()
