package core.game.system.config

import com.google.gson.Gson
import com.google.gson.JsonArray
import content.global.activity.ttrail.ClueLevel
import core.ServerConstants
import core.api.log
import core.cache.def.impl.NPCDefinition
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.impl.Animator
import core.game.world.update.flag.context.Animation
import core.tools.Log
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

    private val gson = Gson()

    fun load() {
        var count = 0
        FileReader(ServerConstants.CONFIG_PATH + "npc_configs.json").use { reader ->
            val configList = gson.fromJson(reader, JsonArray::class.java)
            for (element in configList) {
                val e = element.asJsonObject
                val id = e.get("id")?.asInt ?: continue
                val def = NPCDefinition.forId(id)
                val configs = def.handlers

                for ((key, jsonElement) in e.entrySet()) {
                    if (jsonElement == null || jsonElement.isJsonNull) continue

                    val valueStr = try {
                        jsonElement.asString
                    } catch (ex: UnsupportedOperationException) {
                        ""
                    }

                    if (valueStr.isEmpty() || valueStr == "null") continue

                    when (key) {
                        "melee_animation",
                        "range_animation",
                        "magic_animation",
                        "death_animation",
                        "defence_animation" -> {
                            val animId = jsonElement.asInt
                            configs[key] = Animation(animId, Animator.Priority.HIGH)
                        }

                        "combat_style",
                        "protect_style" -> {
                            val enumIndex = jsonElement.asInt
                            configs[key] = CombatStyle.values()[enumIndex]
                        }

                        "clue_level" -> {
                            val clueIndex = jsonElement.asInt
                            configs[key] = ClueLevel.values()[clueIndex]
                        }

                        "name", "examine", "force_talk" -> {
                            configs[key] = valueStr
                        }

                        "combat_audio", "bonuses" -> {
                            // Zakładam, że format jest np. "1,2,3"
                            val arr = valueStr.split(",").map { it.toInt() }.toIntArray()
                            configs[key] = arr
                        }

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
                        "magic_level" -> {
                            configs[key] = jsonElement.asInt
                        }

                        "slayer_exp" -> {
                            configs[key] = jsonElement.asDouble
                        }

                        "safespot",
                        "aggressive",
                        "poisonous",
                        "poison_immune",
                        "facing_booth",
                        "can_tolerate",
                        "water_npc" -> {
                            configs[key] = jsonElement.asBoolean
                        }

                        else -> {
                            log(this::class.java, Log.WARN, "Unhandled key for npc config: [$key]")
                        }
                    }
                }
                count++
            }
        }

        log(this::class.java, Log.DEBUG, "Parsed $count NPC configurations.")
    }
}