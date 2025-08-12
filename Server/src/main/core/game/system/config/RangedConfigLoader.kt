package core.game.system.config

import com.google.gson.Gson
import com.google.gson.JsonArray
import core.ServerConstants
import core.api.log
import core.cache.def.impl.ItemDefinition
import core.game.node.entity.combat.equipment.Ammunition
import core.game.node.entity.combat.equipment.BoltEffect
import core.game.node.entity.combat.equipment.RangeWeapon
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.Log
import java.io.FileReader

class RangedConfigLoader {
    private val gson = Gson()

    fun load() {
        var count = 0

        FileReader(ServerConstants.CONFIG_PATH + "ammo_configs.json").use { reader ->
            val configs = gson.fromJson(reader, JsonArray::class.java)
            for (entry in configs) {
                val e = entry.asJsonObject

                var dbowgfx: Graphics? = null

                val projectile = e.get("projectile").asString.split(",")
                if (e.get("darkbow_graphic")?.asString?.isNotBlank() == true) {
                    val darkbow = e.get("darkbow_graphic").asString.split(",")
                    dbowgfx = Graphics(darkbow[0].toInt(), darkbow[1].toInt())
                }

                val gfx = e.get("start_graphic").asString.split(",")
                val ammo = Ammunition(
                    e.get("itemId").asInt,
                    Graphics(gfx[0].toInt(), gfx[1].toInt()),
                    dbowgfx,
                    Projectile.create(
                        NPC(0, Location(0, 0, 0)),
                        NPC(0, Location(0, 0, 0)),
                        projectile[0].toInt(),
                        projectile[1].toInt(),
                        projectile[2].toInt(),
                        projectile[3].toInt(),
                        projectile[4].toInt(),
                        projectile[5].toInt(),
                        projectile[6].toInt()
                    ),
                    e.get("poison_damage").asInt
                )

                val effect = BoltEffect.forId(e.get("itemId").asInt)
                if (effect != null) {
                    ammo.effect = effect
                }
                Ammunition.putAmmunition(e.get("itemId").asInt, ammo)
                count++
            }
        }

        count = 0
        FileReader(ServerConstants.CONFIG_PATH + "ranged_weapon_configs.json").use { reader ->
            val configs = gson.fromJson(reader, JsonArray::class.java)
            for (entry in configs) {
                val e = entry.asJsonObject
                val id = e.get("itemId").asInt

                val weapon = RangeWeapon(
                    id,
                    Animation(e.get("animation").asInt),
                    ItemDefinition.forId(id).getConfiguration("attack_speed", 4),
                    e.get("ammo_slot").asInt,
                    e.get("weapon_type").asInt,
                    e.get("drop_ammo").asBoolean,
                    e.get("ammunition").asString.split(",").map { it.toInt() }
                )
                RangeWeapon.putRangeWeapon(weapon)
                count++
            }
        }

        log(this::class.java, Log.DEBUG, "Parsed $count ranged weapon configs.")
    }
}
