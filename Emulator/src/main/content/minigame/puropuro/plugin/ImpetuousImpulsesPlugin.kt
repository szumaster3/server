package content.minigame.puropuro.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.RegionManager.getObject
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Graphics
import org.rs.consts.Items
import org.rs.consts.Scenery

class ImpetuousImpulsesPlugin : InteractionListener {
    override fun defineListeners() {
        on(wheatSceneryIDs, IntType.SCENERY, "push-through") { player, node ->
            if (getStatLevel(player, Skills.HUNTER) < 17) {
                sendMessage(player, "You need a Hunting level of at least 17 to enter the maze.")
                return@on true
            }
            if (anyInInventory(player, Items.MAGIC_BOX_10025, Items.IMP_IN_A_BOX2_10027, Items.IMP_IN_A_BOX1_10028)) {
                sendDialogue(
                    player,
                    "Something prevents you from entering. You think the portal is offended by your imp boxes. They are not popular on imp and impling planes.",
                )
                return@on true
            }
            val dest = node.location.transform(Direction.getLogicalDirection(player.location, node.location), 1)
            if (getObject(dest) != null) {
                sendMessage(player, "The wheat here seems unusually stubborn. You cannot push through.")
                return@on true
            }
            if (RandomFunction.random(2) == 0) {
                sendMessage(player, "You use your strength to push through the wheat.")
            } else if (RandomFunction.random(2) == 1) {
                sendMessage(player, "You use your strength to push through the wheat in the most efficient fashion.")
            } else {
                sendMessage(player, "You push through the wheat. It's hard work though.")
            }
            setAttribute(player, "cantMove", true)
            forceMove(player, player.location, dest, 0, 265, null, 6595, null)
            return@on true
        }

        on(Items.IMPLING_SCROLL_11273, IntType.ITEM, "toggle-view") { player, _ ->
            if (!inZone(player, "puro puro")) {
                sendMessage(player, "You can't do that while you're outside of minigame area.")
                return@on true
            }
            if (player.interfaceManager.overlay != null) {
                closeOverlay(player)
            } else {
                openOverlay(player, Components.IMPLING_SCROLL_169)
            }
            return@on true
        }

        on(Items.JAR_GENERATOR_11258, IntType.ITEM, "butterfly-jar") { player, node ->
            val item = node.asItem()
            val jar = Item(Items.BUTTERFLY_JAR_10012)
            val percent = 1

            if (item.charge - percent < 0) {
                sendMessage(player, "Your jar generator doesn't have enough charges to make another butterfly jar.")
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space for that.")
                return@on true
            }

            lock(player, 5)
            visualize(player, 6592, Graphics.SACHEL_EMOTE_1117)
            addItem(player, jar.id)
            item.charge -= percent
            sendMessage(player, "Your jar generator generates a butterfly jar.")

            if (item.charge <= 0) {
                removeItem(player, item)
                sendMessage(player, "Your jar generator runs out of charges.")
            }
            return@on true
        }

        on(Items.JAR_GENERATOR_11258, IntType.ITEM, "impling-jar") { player, node ->
            val jar = Item(Items.IMPLING_JAR_11260)
            val item = node.asItem()
            val percent = 3

            if (item.charge - percent < 0) {
                sendMessage(player, "Your jar generator doesn't have enough charges to make another impling jar.")
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space for that.")
                return@on true
            }

            lock(player, 5)
            visualize(player, 6592, Graphics.SACHEL_EMOTE_1117)
            addItem(player, jar.id)
            item.charge -= percent
            sendMessage(player, "Your jar generator generates an impling jar.")

            if (item.charge <= 0) {
                removeItem(player, item)
                sendMessage(player, "Your jar generator runs out of charges.")
            }
            return@on true
        }

        on(Items.JAR_GENERATOR_11258, IntType.ITEM, "check") { player, node ->
            val item = node.asItem()
            val difference = item.charge - 1000
            val percent = difference + 100

            sendMessage(player, "Your jar generator has a charge percentage of $percent.")
            return@on true
        }
    }

    companion object {
        val wheatSceneryIDs =
            intArrayOf(
                Scenery.MAGICAL_WHEAT_25016,
                Scenery.MAGICAL_WHEAT_25029,
                Scenery.MAGICAL_WHEAT_25019,
                Scenery.MAGICAL_WHEAT_25018,
                Scenery.MAGICAL_WHEAT_25020,
                Scenery.MAGICAL_WHEAT_25021,
            )
    }
}
