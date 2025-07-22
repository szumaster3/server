package core.game.system.config

import core.ServerConstants
import core.api.log
import core.cache.def.impl.ItemDefinition
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.link.audio.Audio
import core.game.world.update.flag.context.Animation
import core.tools.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader

class ItemConfigParser {
    companion object {
        const val TRADEABLE = "tradeable"
        const val LENDABLE = "lendable"
        const val SPAWNABLE = "spawnable"
        const val DESTROY = "destroy"
        const val TWO_HANDED = "two_handed"
        const val ALCHEMIZABLE = "alchemizable"
        const val HIGH_ALCHEMY = "high_alchemy"
        const val LOW_ALCHEMY = "low_alchemy"
        const val SHOP_PRICE = "shop_price"
        const val GE_PRICE = "grand_exchange_price"
        const val EXAMINE = "examine"
        const val WEIGHT = "weight"
        const val BONUS = "bonuses"
        const val ABSORB = "absorb"
        const val EQUIP_SLOT = "equipment_slot"
        const val ATTACK_SPEED = "attack_speed"
        const val REMOVE_HEAD = "remove_head"
        const val IS_HAT = "hat"
        const val REMOVE_BEARD = "remove_beard"
        const val REMOVE_SLEEVES = "remove_sleeves"
        const val STAND_ANIM = "stand_anim"
        const val STAND_TURN_ANIM = "stand_turn_anim"
        const val WALK_ANIM = "walk_anim"
        const val RUN_ANIM = "run_anim"
        const val TURN180_ANIM = "turn180_anim"
        const val TURN90CW_ANIM = "turn90cw_anim"
        const val TURN90CCW_ANIM = "turn90ccw_anim"
        const val WEAPON_INTERFACE = "weapon_interface"
        const val HAS_SPECIAL = "has_special"
        const val ATTACK_ANIMS = "attack_anims"
        const val DESTROY_MESSAGE = "destroy_message"
        const val REQUIREMENTS = "requirements"
        const val GE_LIMIT = "ge_buy_limit"
        const val DEFENCE_ANIMATION = "defence_anim"
        const val ATTACK_AUDIO = "attack_audios"
        const val EQUIP_AUDIO = "equip_audio"
        const val POINT_PRICE = "point_price"
        const val BANKABLE = "bankable"
        const val RARE_ITEM = "rare_item"
        const val TOKKUL_PRICE = "tokkul_price"
        const val RENDER_ANIM_ID = "render_anim"
        const val ARCHERY_TICKET_PRICE = "archery_ticket_price"
        const val CASTLE_WARS_TICKET_PRICE = "castle_wars_ticket_price"
    }

    val parser = JSONParser()
    var reader: FileReader? = null

    fun load() {
        var count = 0
        reader = FileReader(ServerConstants.CONFIG_PATH + "item_configs.json")
        val configList = parser.parse(reader) as JSONArray
        for (config in configList) {
            val e = config as JSONObject
            val def = ItemDefinition.forId(e["id"].toString().toInt())
            val configs = def.handlers
            val requirements = HashMap<Int, Int>()
            e.map {
                if (it.value.toString().isNotEmpty() && it.value.toString() != "null") {
                    when (it.key.toString()) {
                        // Special cases
                        "defence_anim" ->
                            configs[it.key.toString()] =
                                Animation(it.value.toString().toInt(), Animator.Priority.HIGH)

                        "requirements" -> {
                            configs[it.key.toString()] = requirements
                            it.value.toString().split("-").map { en ->
                                val tokens = en.replace("{", "").replace("}", "").split(",")
                                requirements.put(tokens[0].toInt(), tokens[1].toInt())
                            }
                        }

                        "attack_audios" ->
                            configs[it.key.toString()] =
                                it.value
                                    .toString()
                                    .split(",")
                                    .map { i -> Audio(i.toInt()) }
                                    .toTypedArray()

                        "attack_anims" ->
                            configs[it.key.toString()] =
                                it.value
                                    .toString()
                                    .split(",")
                                    .map { i -> Animation(i.toInt(), Animator.Priority.HIGH) }
                                    .toTypedArray()

                        // int arrays
                        "absorb",
                        "bonuses",
                        ->
                            configs[it.key.toString()] =
                                it.value
                                    .toString()
                                    .split(",")
                                    .map { i -> i.toInt() }
                                    .toIntArray()

                        // booleans
                        "fun_weapon",
                        "rare_item",
                        "bankable",
                        "two_handed",
                        "has_special",
                        "remove_sleeves",
                        "remove_beard",
                        "remove_head",
                        "hat",
                        "destroy",
                        "lendable",
                        "tradeable",
                        -> configs[it.key.toString()] = it.value.toString().toBoolean()

                        "alchemizable" -> configs[it.key.toString()] = it.value.toString().toBoolean()

                        // doubles
                        "weight" -> configs[it.key.toString()] = it.value.toString().toDouble()

                        // ints
                        "equip_audio",
                        "point_price",
                        TOKKUL_PRICE,
                        ARCHERY_TICKET_PRICE,
                        CASTLE_WARS_TICKET_PRICE,
                        "ge_buy_limit",
                        "weapon_interface",
                        "attack_speed",
                        "equipment_slot",
                        "render_anim",
                        "turn90ccw_anim",
                        "turn90cw_anim",
                        "turn180_anim",
                        "run_anim",
                        "walk_anim",
                        "stand_anim",
                        "low_alchemy",
                        "high_alchemy",
                        "grand_exchange_price",
                        "id",
                        -> configs[it.key.toString()] = it.value.toString().toInt()

                        // Strings
                        "examine",
                        "destroy_message",
                        "name",
                        -> configs[it.key.toString()] = it.value.toString()
                    }
                }
            }
            count++
        }

        log(this::class.java, Log.FINE, "Parsed $count item configs.")
    }
}
