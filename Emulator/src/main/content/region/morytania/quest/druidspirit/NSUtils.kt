package content.region.morytania.quest.druidspirit

import content.region.morytania.handlers.npc.GhastNPC
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.Sounds

object NSUtils {
    fun flagFungusPlaced(player: Player) {
        setAttribute(player, "/save:ns:fungus_placed", true)
    }

    fun flagCardPlaced(player: Player) {
        setAttribute(player, "/save:ns:card_placed", true)
    }

    fun hasPlacedFungus(player: Player): Boolean {
        return getAttribute(player, "ns:fungus_placed", false)
    }

    fun hasPlacedCard(player: Player): Boolean {
        return getAttribute(player, "ns:card_placed", false)
    }

    fun onStone(player: Player): Boolean {
        return player.location.equals(3440, 3335, 0)
    }

    fun getGhastKC(player: Player): Int {
        return getAttribute(player, "ns:ghasts_killed", 0)
    }

    fun incrementGhastKC(player: Player) {
        setAttribute(player, "/save:ns:ghasts_killed", getGhastKC(player) + 1)
        val msg =
            when (getGhastKC(player)) {
                1 -> "That's one down, two more to go."
                2 -> "Two down, only one more to go."
                3 -> "That's it! I've killed all 3 Ghasts!"
                else -> ""
            }

        if (!msg.isEmpty()) {
            sendMessage(player, msg)
        }
    }

    fun activatePouch(
        player: Player,
        attacker: GhastNPC,
    ): Boolean {
        var shouldAddEmptyPouch = false
        val pouchAmt = amountInInventory(player, Items.DRUID_POUCH_2958)
        if (pouchAmt == 1) shouldAddEmptyPouch = true
        if (pouchAmt > 0 && removeItem(player, Items.DRUID_POUCH_2958, Container.INVENTORY)) {
            if (shouldAddEmptyPouch) {
                addItem(player, Items.DRUID_POUCH_2957)
            }
            spawnProjectile(player, attacker, 268)
            submitWorldPulse(
                object : Pulse() {
                    var ticks = 0

                    override fun pulse(): Boolean {
                        when (ticks++) {
                            2 ->
                                visualize(
                                    attacker,
                                    -1,
                                    Graphics(org.rs.consts.Graphics.FIRST_CONTACT_GOES_WITH_ABOVE_269, 125),
                                )

                            3 ->
                                attacker.transform(attacker.id + 1).also {
                                    attacker.attack(player)
                                    attacker.setAttribute("woke", getWorldTicks())
                                    return true
                                }
                        }
                        return false
                    }
                },
            )
            return true
        }
        return false
    }

    fun cleanupAttributes(player: Player) {
        removeAttribute(player, "ns:fungus_placed")
        removeAttribute(player, "ns:card_placed")
    }

    @JvmStatic
    fun castBloom(player: Player): Boolean {
        var success = false
        if (player.skills.prayerPoints < 1) {
            sendMessage(player, "You don't have enough prayer points to do this.")
            return false
        }
        lock(player, 1)
        handleVisuals(player)
        val locs = player.location.surroundingTiles
        for (o in locs) {
            val obj = RegionManager.getObject(o)
            if (obj != null) {
                if (obj.name.equals("Rotting log", ignoreCase = true) && player.skills.prayerPoints >= 1) {
                    if (player.location.withinDistance(obj.location, 2)) {
                        SceneryBuilder.replace(obj, obj.transform(3509))
                        success = true
                    }
                }
                if (obj.name.equals("Rotting branch", ignoreCase = true) && player.skills.prayerPoints >= 1) {
                    if (player.location.withinDistance(obj.location, 2)) {
                        SceneryBuilder.replace(obj, obj.transform(3511))
                        success = true
                    }
                }
                if (obj.name.equals("A small bush", ignoreCase = true) && player.skills.prayerPoints >= 1) {
                    if (player.location.withinDistance(obj.location, 2)) {
                        SceneryBuilder.replace(obj, obj.transform(3513))
                        success = true
                    }
                }
            }
        }
        return success
    }

    private fun handleVisuals(player: Player) {
        player.skills.decrementPrayerPoints(RandomFunction.random(1, 5).toDouble())
        playAudio(player, Sounds.CAST_BLOOM_1493)
        val aroundPlayer = player.location.surroundingTiles
        for (location in aroundPlayer) {
            player.packetDispatch.sendGlobalPositionGraphic(
                org.rs.consts.Graphics.SMALLS_STARS_SILVER_SICKLEB_263,
                location,
            )
        }
    }
}
