package content.region.karamja.quests.junglepotion.handlers

import core.api.freeSlots
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest

import core.game.world.map.Location
import org.rs.consts.Quests
import org.rs.consts.Scenery

class JunglePotionListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles search the jungle scenery.
         */

        on(JUNGLE_OBJECTIVE, IntType.SCENERY, "search") { player, node ->
            val quest = player.getQuestRepository().getQuest(Quests.JUNGLE_POTION)
            JungleObjective.forId(node.id)?.let { search(player, quest, node.asScenery(), it) }
            return@on true
        }

        /*
         * Handles search the rocks.
         */

        on(Scenery.ROCKS_2584, IntType.SCENERY, "search") { player, _ ->
            player.dialogueInterpreter.open("jogre_dialogue")
            return@on true
        }

        /*
         * Handles climb the rope.
         */

        on(Scenery.HAND_HOLDS_2585, IntType.SCENERY, "climb") { player, _ ->
            player.dialogueInterpreter.open("jogre_dialogue", true, true)
            return@on true
        }

    }

    private fun search(player: Player, quest: Quest, scenery: Node, objective: JungleObjective) {
        if (quest.getStage(player) < objective.stage) {
            sendMessage(player, "Unfortunately, you find nothing of interest.")
            return
        }
        if (freeSlots(player) < 1) {
            sendMessage(player,"You don't have enough inventory space.")
            return
        }
        sendMessage(player,"You search the " + scenery.name.lowercase() + "...")
        objective.search(player, scenery)
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, Scenery.HAND_HOLDS_2585) { _, _ ->
            return@setDest Location.create(2830, 9521, 0)
        }
    }

    companion object {
        val JUNGLE_OBJECTIVE = JungleObjective.values().map { it.objectId }.toIntArray()
    }
}