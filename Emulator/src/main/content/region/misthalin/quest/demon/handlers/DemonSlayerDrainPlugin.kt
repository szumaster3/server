package content.region.misthalin.quest.demon.handlers

import core.api.setAttribute
import core.api.setVarp
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Plugin
import org.rs.consts.Quests

class DemonSlayerDrainPlugin : UseWithHandler(1929) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(17424, OBJECT_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val quest = player.getQuestRepository().getQuest(Quests.DEMON_SLAYER)
        if (player.inventory.remove(BUCKET_OF_WATER)) {
            player.inventory.add(BUCKET)
            player.animate(ANIMATION)
            player.packetDispatch.sendMessage("You pour the liquid down the drain.")
            if (quest.getStage(player) != 20) {
                return true
            }
            if (player.getAttribute("demon-slayer:just-poured", false)) {
                return true
            }
            if (!player.hasItem(DemonSlayerUtils.FIRST_KEY)) {
                player.getSavedData().questData.demonSlayer[0] = false
            }
            if (quest.getStage(player) == 20 &&
                !player.hasItem(DemonSlayerUtils.FIRST_KEY) &&
                !player.getSavedData().questData.demonSlayer[0]
            ) {
                player.dialogueInterpreter.sendDialogues(
                    player,
                    null,
                    "OK, I think I've washed the key down into the sewer.",
                    "I'd better go down and get it!",
                )
                player.getSavedData().questData.demonSlayer[0] = true
                setVarp(player, 222, 2660610, true)
                setAttribute(player, "demon-slayer:just-poured", true)
                return true
            }
        }
        return true
    }

    override fun getDestination(
        player: Player,
        with: Node,
    ): Location {
        return Location(3225, 3495)
    }

    companion object {
        private val ANIMATION = Animation(827)
        private val BUCKET_OF_WATER = Item(1929)
        private val BUCKET = Item(1925)
    }
}
