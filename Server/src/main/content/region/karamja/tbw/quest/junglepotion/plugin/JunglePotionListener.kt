package content.region.karamja.tbw.quest.junglepotion.plugin

import core.api.getSceneryName
import core.api.openDialogue
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.world.map.Location
import shared.consts.Quests
import shared.consts.Scenery

class JunglePotionListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles search the jungle scenery.
         */

        on(JUNGLE_OBJECTIVE, IntType.SCENERY, "search") { player, node ->
            val quest = player.getQuestRepository().getQuest(Quests.JUNGLE_POTION)
            JungleObject.forId(node.id)?.let { search(player, quest, node.asScenery(), it) }
            return@on true
        }

        /*
         * Handles search the rocks.
         */

        on(Scenery.ROCKS_2584, IntType.SCENERY, "search") { player, _ ->
            openDialogue(player, "jogre_dialogue")
            return@on true
        }

        /*
         * Handles climb the rope.
         */

        on(Scenery.HAND_HOLDS_2585, IntType.SCENERY, "climb") { player, _ ->
            openDialogue(player, "jogre_dialogue", true, true)
            return@on true
        }

    }

    private fun search(player: Player, quest: Quest, scenery: core.game.node.scenery.Scenery, loc: JungleObject) {
        sendMessage(player,"You search the " + getSceneryName(scenery.id).lowercase() + "...")
        if (quest.getStage(player) < loc.stage) {
            sendMessage(player, "Unfortunately, you find nothing of interest.")
            return
        }
        loc.search(player, scenery)
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, Scenery.HAND_HOLDS_2585) { _, _ ->
            return@setDest Location.create(2830, 9521, 0)
        }
    }

    companion object {
        val JUNGLE_OBJECTIVE = JungleObject.values().map { it.objectId }.toIntArray()
    }
}