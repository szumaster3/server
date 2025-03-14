package core.game.world.map.zone.impl

import content.data.GameAttributes
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items

@Initializable
class DesertZone :
    MapZone(DESERT_ZONE, true),
    Plugin<Any?> {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    override fun configure() {
        register(getRegionBorders(12589))
        register(getRegionBorders(12590))
        register(getRegionBorders(12591))
        register(getRegionBorders(12843))
        register(getRegionBorders(12844))
        register(getRegionBorders(12845))
        register(getRegionBorders(12847))
        register(getRegionBorders(12848))
        register(getRegionBorders(13100))
        register(getRegionBorders(13101))
        register(getRegionBorders(13102))
        register(getRegionBorders(13103))
        register(getRegionBorders(13355))
        register(getRegionBorders(13356))
        register(getRegionBorders(13357))
        register(getRegionBorders(13359))
        register(getRegionBorders(13360))
        register(getRegionBorders(13361))
        register(getRegionBorders(13611))
        register(getRegionBorders(13612))
        register(getRegionBorders(13613))
        register(getRegionBorders(13614))
        register(getRegionBorders(13615))
        register(getRegionBorders(13616))
        register(getRegionBorders(13617))
        register(getRegionBorders(13872))
        register(getRegionBorders(13873))
        register(ZoneBorders(3264, 3072, 3327, 3116))
        pulse.stop()
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val player = e
            if (player.getAttribute(TUTORIAL_STAGE, 0) > 71) {
                return true
            }
            player.setAttribute(DESERT_DELAY, ticks + getDelay(player))
            PLAYERS.add(player)
            if (!pulse.isRunning) {
                pulse.restart()
                pulse.start()
                Pulser.submit(pulse)
            }
        }
        return true
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (e is Player) {
            PLAYERS.remove(e)
            e.removeAttribute(DESERT_DELAY)
        }
        return super.leave(e, logout)
    }

    companion object {
        @JvmStatic private val WATER_SKINS =
            arrayOf(
                Item(Items.WATERSKIN4_1823),
                Item(Items.WATERSKIN3_1825),
                Item(Items.WATERSKIN2_1827),
                Item(Items.WATERSKIN1_1829),
            )

        @JvmStatic private val VESSILS =
            arrayOf(
                intArrayOf(Items.JUG_OF_WATER_1937, Items.JUG_1935),
                intArrayOf(Items.BUCKET_OF_WATER_1929, Items.BUCKET_1925),
                intArrayOf(Items.BOWL_OF_WATER_1921, Items.BOWL_1923),
                intArrayOf(Items.VIAL_OF_WATER_227, Items.VIAL_229),
            )

        @JvmStatic private val ANIMATION = Animation(Animations.EAT_OLD_829)

        @JvmStatic private val PLAYERS: MutableList<Player> = ArrayList(20)

        @JvmStatic private val DESERT_DELAY = "desert-delay"

        @JvmStatic private val TUTORIAL_STAGE = GameAttributes.TUTORIAL_STAGE

        @JvmStatic private val TUTORIAL_COMPLETE = GameAttributes.TUTORIAL_COMPLETE

        @JvmStatic private val DESERT_ZONE = "Desert Zone"

        @JvmStatic private val pulse: Pulse =
            object : Pulse(3) {
                override fun pulse(): Boolean {
                    for (player in PLAYERS) {
                        if (!getAttribute(player, TUTORIAL_COMPLETE, false) ||
                            player.interfaceManager.isOpened ||
                            player.interfaceManager.hasChatbox() ||
                            player.locks.isMovementLocked
                        ) {
                            continue
                        }
                        if (player.getAttribute(DESERT_DELAY, -1) < ticks) {
                            effect(player)
                        }
                    }
                    return PLAYERS.isEmpty()
                }
            }

        @JvmStatic private fun effect(player: Player) {
            player.setAttribute(DESERT_DELAY, ticks + getDelay(player))
            evaporate(player)
            if (drink(player)) {
                return
            }
            impact(
                player,
                RandomFunction.random(
                    1,
                    if (player.location.y <
                        2990
                    ) {
                        12
                    } else {
                        8
                    },
                ),
                ImpactHandler.HitsplatType.NORMAL,
            )
            sendMessage(player, "You start dying of thirst while you're in the desert.")
        }

        @JvmStatic fun evaporate(player: Player) {
            for (i in VESSILS.indices) {
                if (inInventory(player, VESSILS[i][0], 1)) {
                    if (removeItem(player, Item(VESSILS[i][0]))) {
                        addItem(player, VESSILS[i][1])
                        sendMessage(
                            player,
                            "The water in your " +
                                getItemName(VESSILS[i][0]).lowercase().replace("of water", "").trim { it <= ' ' } +
                                " evaporates in the desert heat.",
                        )
                    }
                }
            }
        }

        @JvmStatic fun drink(player: Player): Boolean {
            for (i in WATER_SKINS) {
                if (inInventory(player, i.id) && removeItem(player, i)) {
                    addItem(player, i.id + 2)
                    animate(player, ANIMATION)
                    sendMessage(player, "You take a drink of water.")
                    return true
                }
            }
            if (inInventory(player, Items.WATERSKIN0_1831, 1)) {
                sendMessage(player, "Perhaps you should fill up one of your empty waterskins.")
            } else {
                sendMessage(player, "You should get a waterskin for any travelling in the desert.")
            }
            return false
        }

        @JvmStatic private fun getDelay(player: Player): Int {
            var delay = 116
            if (inEquipment(player, Items.DESERT_SHIRT_1833, 1)) {
                delay += 17
            }
            if (inEquipment(player, Items.DESERT_ROBE_1835, 1)) {
                delay += 17
            }
            if (inEquipment(player, Items.DESERT_BOOTS_1837, 1)) {
                delay += 17
            }
            if (inEquipment(player, Items.DESERT_DISGUISE_4611, 1)) {
                delay += 12
            }

            val enchantedWaterTiara = inEquipment(player, Items.ENCHANTED_WATER_TIARA_11969)
            enchantedWaterTiara?.let { _ ->
                val charges = getCharge(Item(Items.ENCHANTED_WATER_TIARA_11969))
                delay += charges / 1000
            }

            return delay
        }
    }
}
