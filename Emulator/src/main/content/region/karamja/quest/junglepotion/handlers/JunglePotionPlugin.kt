package content.region.karamja.quest.junglepotion.handlers

import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.plugin.Plugin
import org.rs.consts.Quests

class JunglePotionPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(2584).handlers["option:search"] = this
        SceneryDefinition.forId(2585).handlers["option:climb"] = this
        for (s in JungleObjective.values()) {
            SceneryDefinition.forId(s.objectId).handlers["option:search"] = this
        }
        SceneryBuilder.add(
            Scenery(2585, Location.create(2828, 9522, 0), 8, 0),
        )
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.JUNGLE_POTION)
        when (node.id) {
            2584 -> {
                player.dialogueInterpreter.open("jogre_dialogue")
                return true
            }

            2585 -> {
                player.dialogueInterpreter.open("jogre_dialogue", true, true)
                return true
            }
        }
        when (option) {
            "search" -> search(player, quest, node as Scenery, JungleObjective.forId(node.getId()))
        }
        return true
    }

    private fun search(
        player: Player,
        quest: Quest,
        scenery: Scenery,
        objective: JungleObjective,
    ) {
        if (quest.getStage(player) < objective.stage) {
            player.sendMessage("Unfortunately, you find nothing of interest.")
            return
        }
        if (player.inventory.freeSlots() < 1) {
            player.sendMessage("You don't have enough inventory space.")
            return
        }
        player.sendMessage("You search the " + scenery.name.lowercase() + "...")
        objective.search(player, scenery)
    }

    override fun getDestination(
        n: Node,
        node: Node,
    ): Location? {
        if (node.id == 2585) {
            return Location.create(2830, 9521, 0)
        }
        return null
    }
}
