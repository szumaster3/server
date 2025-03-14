package core.game.system.config

import content.global.activity.ttrail.ClueLevel
import core.ServerConstants
import core.api.log
import core.cache.def.impl.NPCDefinition
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.impl.Animator
import core.game.world.update.flag.context.Animation
import core.tools.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader

class NPCConfigParser {
    companion object {
        const val WEAKNESS = "weakness"
        const val LIFEPOINTS = "lifepoints"
        const val ATTACK_LEVEL = "attack_level"
        const val STRENGTH_LEVEL = "strength_level"
        const val DEFENCE_LEVEL = "defence_level"
        const val RANGE_LEVEL = "range_level"
        const val MAGIC_LEVEL = "magic_level"
        const val EXAMINE = "examine"
        const val SLAYER_TASK = "slayer_task"
        const val POISONOUS = "poisonous"
        const val AGGRESSIVE = "aggressive"
        const val RESPAWN_DELAY = "respawn_delay"
        const val ATTACK_SPEED = "attack_speed"
        const val POISON_IMMUNE = "poison_immune"
        const val BONUSES = "bonuses"
        const val START_GRAPHIC = "start_gfx"
        const val PROJECTILE = "projectile"
        const val END_GRAPHIC = "end_gfx"
        const val COMBAT_STYLE = "combat_style"
        const val AGGRESSIVE_RADIUS = "agg_radius"
        const val SLAYER_EXP = "slayer_exp"
        const val POISON_AMOUNT = "poison_amount"
        const val MOVEMENT_RADIUS = "movement_radius"
        const val SPAWN_ANIMATION = "spawn_animation"
        const val START_HEIGHT = "start_height"
        const val PROJECTILE_HEIGHT = "prj_height"
        const val END_HEIGHT = "end_height"
        const val CLUE_LEVEL = "clue_level"
        const val SPELL_ID = "spell_id"
        const val COMBAT_AUDIO = "combat_audio"
        const val MELEE_ANIMATION = "melee_animation"
        const val DEFENCE_ANIMATION = "defence_animation"
        const val DEATH_ANIMATION = "death_animation"
        const val RANGE_ANIMATION = "range_animation"
        const val MAGIC_ANIMATION = "magic_animation"
        const val PROTECT_STYLE = "protect_style"
    }

    val parser = JSONParser()
    var reader: FileReader? = null

    fun load() {
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "npc_configs.json")
        val configlist = parser.parse(reader) as JSONArray
        for (config in configlist) {
            val e = config as JSONObject
            val def = NPCDefinition.forId(e["id"].toString().toInt())
            val configs = def.handlers
            e.map {
                if (it.value.toString().isNotEmpty() && it.value.toString() != "null") {
                    when (it.key.toString()) {
                        "melee_animation",
                        "range_animation",
                        "magic_animation",
                        "death_animation",
                        "defence_animation",
                        ->
                            configs[it.key.toString()] =
                                Animation(it.value.toString().toInt(), Animator.Priority.HIGH)

                        "combat_style",
                        "protect_style",
                        ->
                            configs[it.key.toString()] =
                                CombatStyle.values()[it.value.toString().toInt()]

                        "clue_level" -> configs[it.key.toString()] = ClueLevel.values()[it.value.toString().toInt()]
                        "name", "examine" -> configs[it.key.toString()] = it.value.toString()
                        "combat_audio", "bonuses" ->
                            configs[it.key.toString()] =
                                it.value
                                    .toString()
                                    .split(",")
                                    .map { v -> v.toInt() }
                                    .toIntArray()

                        "force_talk" -> configs[it.key.toString()] = it.value.toString()

                        "spawn_animation",
                        "id",
                        "lifepoints",
                        "attack_level",
                        "strength_level",
                        "defence_level",
                        "range_level",
                        "movement_radius",
                        "agg_radius",
                        "attack_speed",
                        "poison_amount",
                        "respawn_delay",
                        "start_gfx",
                        "projectile",
                        "end_gfx",
                        "weakness",
                        "slayer_task",
                        "start_height",
                        "prj_height",
                        "end_height",
                        "spell_id",
                        "death_gfx",
                        "magic_level",
                        ->
                            configs[it.key.toString()] =
                                if (it.value.toString().isEmpty()) Unit else it.value.toString().toInt()

                        "slayer_exp" -> configs[it.key.toString()] = it.value.toString().toDouble()

                        "safespot",
                        "aggressive",
                        "poisonous",
                        "poison_immune",
                        "facing_booth",
                        "can_tolerate",
                        "water_npc",
                        -> configs[it.key.toString()] = it.value.toString().toBoolean()

                        else -> log(this::class.java, Log.WARN, "Unhandled key for npc config: ${it.key}")
                    }
                }
            }
            count++
        }

        log(this::class.java, Log.FINE, "Parsed $count NPC configurations.")
    }
}
