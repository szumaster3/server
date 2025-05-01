package content.global.ame.grave

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.api.ui.setMinimapState
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class GravediggerListener : InteractionListener {
    data class CoffinSet(val name: String, val coffinId: Int, val gravestoneId: Int, val graveId: Int, val emptyGraveId: Int, val content: Array<IntArray>)
    companion object {
        private const val COFFIN_INTERFACE = Components.GRAVEDIGGER_COFFIN_141
        private const val GRAVESTONE_INTERFACE = Components.GRAVEDIGGER_GRAVE_143
        private const val MAUSOLEUM = Scenery.MAUSOLEUM_12731
        private val ANIMATION = Animation(Animations.MULTI_BEND_OVER_827)
        val COFFIN = intArrayOf(Items.COFFIN_7587, Items.COFFIN_7588, Items.COFFIN_7589, Items.COFFIN_7590, Items.COFFIN_7591)
        private val GRAVE = intArrayOf(Scenery.GRAVE_12721, Scenery.GRAVE_12722, Scenery.GRAVE_12723, Scenery.GRAVE_12724, Scenery.GRAVE_12725)
        private val EMPTY_GRAVE = intArrayOf(Scenery.GRAVE_12726, Scenery.GRAVE_12727, Scenery.GRAVE_12728, Scenery.GRAVE_12729, Scenery.GRAVE_12730)
        private val GRAVESTONE = intArrayOf(Scenery.GRAVESTONE_12716, Scenery.GRAVESTONE_12717, Scenery.GRAVESTONE_12718, Scenery.GRAVESTONE_12719, Scenery.GRAVESTONE_12720)
        private val WOODCUTTING_TOOL = intArrayOf(Items.INFERNO_ADZE_13661, Items.DRAGON_AXE_6739, Items.RUNE_AXE_1359, Items.ADAMANT_AXE_1357, Items.MITHRIL_AXE_1355, Items.BLACK_AXE_1361, Items.STEEL_AXE_1353, Items.IRON_AXE_1349, Items.BRONZE_AXE_1351)
        private val COFFIN_CONTENTS = mapOf(
            "Cooks" to arrayOf(intArrayOf(Items.ITEM_7604, Items.ITEM_7601, Items.ITEM_7598), intArrayOf(Items.ITEM_7600, Items.ITEM_7598, Items.ITEM_7611), intArrayOf(Items.ITEM_7598, Items.ITEM_7598, Items.ITEM_7598)),
            "Farmer" to arrayOf(intArrayOf(Items.ITEM_7598, Items.ITEM_7598, Items.ITEM_7610), intArrayOf(Items.ITEM_7611, Items.ITEM_7609, Items.ITEM_7598), intArrayOf(Items.ITEM_7602, Items.ITEM_7598, Items.ITEM_7598)),
            "Potter" to arrayOf(intArrayOf(Items.ITEM_7598, Items.ITEM_7599, Items.ITEM_7608), intArrayOf(Items.ITEM_7613, Items.ITEM_7598, Items.ITEM_7598), intArrayOf(Items.ITEM_7598, Items.ITEM_7611, Items.ITEM_7598)),
            "Lumberjack" to arrayOf(intArrayOf(Items.ITEM_7611, Items.ITEM_7598, Items.ITEM_7598), intArrayOf(Items.ITEM_7598, Items.ITEM_7598, Items.ITEM_7603), intArrayOf(Items.ITEM_7598, Items.ITEM_7605, Items.ITEM_7612)),
            "Miner" to arrayOf(intArrayOf(Items.ITEM_7598, Items.ITEM_7598, Items.ITEM_7606), intArrayOf(Items.ITEM_7598, Items.ITEM_7597, Items.ITEM_7598), intArrayOf(Items.ITEM_7607, Items.ITEM_7598, Items.ITEM_7611))
        )
        private val COFFIN_SETS = listOf(
            CoffinSet("Lumberjack", COFFIN[0], GRAVESTONE[0], GRAVE[0], EMPTY_GRAVE[0], COFFIN_CONTENTS["Lumberjack"]!!),
            CoffinSet("Cooks", COFFIN[1], GRAVESTONE[1], GRAVE[1], EMPTY_GRAVE[1], COFFIN_CONTENTS["Cooks"]!!),
            CoffinSet("Miner", COFFIN[2], GRAVESTONE[2], GRAVE[2], EMPTY_GRAVE[2], COFFIN_CONTENTS["Miner"]!!),
            CoffinSet("Farmer", COFFIN[3], GRAVESTONE[3], GRAVE[3], EMPTY_GRAVE[3], COFFIN_CONTENTS["Farmer"]!!),
            CoffinSet("Potter", COFFIN[4], GRAVESTONE[4], GRAVE[4], EMPTY_GRAVE[4], COFFIN_CONTENTS["Potter"]!!)
        )

        fun reset(player: Player) {
            COFFIN_SETS.forEach { set ->
                val key = set.name
                removeAttributes(player, "coffin_set:$key", "coffin_id:$key", "gravestone_id:$key", "grave_id:$key", "empty_grave_id:$key", "coffin_content:$key")
            }
            COFFIN.forEach { coffinId -> removeAttribute(player, "coffin_used:$coffinId") }
        }

        fun cleanup(player: Player) {
            player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
            setMinimapState(player, 0)
            removeAttributes(player, GameAttributes.GRAVEDIGGER_SCORE, RandomEvent.save(), RandomEvent.logout())
            clearLogoutListener(player, RandomEvent.logout())
            reset(player)
        }

        fun init(player: Player) {
            val shuffledGravestones = GRAVESTONE.toList().shuffled().iterator()
            val shuffledGrave = GRAVE.toList().shuffled().iterator()
            val shuffledCoffin = COFFIN.toList().shuffled().iterator()
            val shuffledEmptyGrave = EMPTY_GRAVE.toList().shuffled().iterator()
            COFFIN_SETS.forEach { originalSet ->
                val key = originalSet.name
                player.apply {
                    setAttribute("coffin_set:$key", originalSet.name)
                    setAttribute("coffin_id:$key", shuffledCoffin.next())
                    setAttribute("gravestone_id:$key", shuffledGravestones.next())
                    setAttribute("grave_id:$key", shuffledGrave.next())
                    setAttribute("empty_grave_id:$key", shuffledEmptyGrave.next())
                    setAttribute("coffin_content:$key", originalSet.content.flatMap { it.toList() })
                }
            }
        }
    }

    override fun defineListeners() {
        on(COFFIN, IntType.ITEM, "check") { player, n ->
            val set = COFFIN_SETS.find { player.getAttribute<Int>("coffin_id:${it.name}") == n.id } ?: return@on true
            val coffinContent = set.content.flatMap { it.toList() }
            val components = (3..11).toList()
            openInterface(player, COFFIN_INTERFACE).also {
                coffinContent.forEachIndexed { index, itemId ->
                    if (index < components.size) {
                        sendItemOnInterface(player, COFFIN_INTERFACE, components[index], itemId, 1)
                    }
                }
            }
            return@on true
        }

        on(GRAVESTONE, IntType.SCENERY, "read") { player, node ->
            val set = COFFIN_SETS.find { player.getAttribute<Int>("gravestone_id:${it.name}") == node.id } ?: return@on true
            val gravestoneItemId = mapOf("Lumberjack" to Items.ITEM_7614, "Cooks" to Items.ITEM_7615, "Miner" to Items.ITEM_7616, "Farmer" to Items.ITEM_7617, "Potter" to Items.ITEM_7618)[set.name] ?: return@on true
            openInterface(player, GRAVESTONE_INTERFACE)
            sendItemOnInterface(player, GRAVESTONE_INTERFACE, 2, gravestoneItemId, 1)
            sendMessage(player, "You look at the gravestone.")
            return@on true
        }

        on(GRAVE, IntType.SCENERY, "take-coffin") { player, node ->
            val set = COFFIN_SETS.find { player.getAttribute<Int>("grave_id:${it.name}") == node.id } ?: return@on true
            val coffinID = player.getAttribute<Int>("coffin_id:${set.name}") ?: return@on true
            val emptyGraveID = player.getAttribute<Int>("empty_grave_id:${set.name}") ?: return@on true
            if (!hasSpaceFor(player, Item(coffinID, 1))) {
                player.sendMessage("You need space in your inventory to take the coffin.")
                return@on true
            }
            queueScript(player, 1, QueueStrength.NORMAL) {
                lock(player, 3)
                player.animate(ANIMATION)
                addItem(player, coffinID, 1)
                replaceScenery(node.asScenery(), emptyGraveID, -1)
                sendMessage(player, "You take the coffin from the grave.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, COFFIN, *EMPTY_GRAVE) { player, used, with ->
            val coffinSet = COFFIN_SETS.find { player.getAttribute<Int>("coffin_id:${it.name}") == used.id } ?: return@onUseWith true
            val key = coffinSet.name
            val assignedGraveId = player.getAttribute<Int>("grave_id:$key") ?: return@onUseWith true
            val assignedEmptyGraveId = player.getAttribute<Int>("empty_grave_id:$key") ?: return@onUseWith true
            val isCorrectGrave = with.id == assignedEmptyGraveId
            if (isCorrectGrave) {
                val currentPoints = player.getAttribute<Int>(GameAttributes.GRAVEDIGGER_SCORE) ?: 0
                player.setAttribute(GameAttributes.GRAVEDIGGER_SCORE, currentPoints + 1)
                player.debug("score: [$currentPoints]")
            }
            if (removeItem(player, used.asItem())) {
                queueScript(player, 1, QueueStrength.NORMAL) {
                    lock(player, 3)
                    player.animate(ANIMATION)
                    removeItem(player, used.asItem())
                    replaceScenery(with.asScenery(), assignedGraveId, -1)
                    sendMessage(player, "You put the coffin into the grave.")
                    stopExecuting(player)
                }
            }

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, WOODCUTTING_TOOL, Scenery.DEAD_TREE_12732) { player, _, _ ->
            if (inBorders(player, ZoneBorders(1921, 4993, 1934, 5006))) {
                sendMessages(player, "You don't need any wood.", "What are you planning on doing, making them a fresh coffin?")
            }
            return@onUseWith true
        }

        on(MAUSOLEUM, IntType.SCENERY, "deposit") { player, _ ->
            player.bank.openDepositBox()
            player.bank::refreshDepositBoxInterface
            return@on true
        }

        on(NPCs.LEO_3508, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, LeoDialogue(), npc)
            return@on true
        }
    }
}