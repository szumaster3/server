package content.region.misthalin.draynor_village.quest.haunted

import content.region.misthalin.draynor_village.quests.haunted.plugin.ErnestChickenNPC
import content.region.misthalin.draynor_village.quests.haunted.plugin.ErnestNPC
import core.api.removeAttributes
import core.api.sendItemZoomOnInterface
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.ClassScanner.definePlugins
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

/**
 * Represents the Ernest the Chicken quest.
 */
@Initializable
class ErnestTheChicken : Quest(Quests.ERNEST_THE_CHICKEN, 19, 18, 4, Vars.VARP_QUEST_ERNEST_THE_CHICKEN_PROGRESS_32, 0, 1, 3) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        if (getStage(player) == 0) {
            line(player, "I can start this quest by speaking to !!Veronica?? who is", line++)
            line(player, "outside !!Draynor Manor??.", line++)
            line(player, "There aren't any requirements for this quest.", line++)
        } else if (stage == 10) {
            line(player, "I have spoken to Veronica", line++)
            line(player, "I need to find !!Ernest??", line++)
            line(player, "He went into !!Draynor Manor?? and hasn't returned", line++)
        } else if (stage == 20) {
            line(player, "I have spoken to Veronica", line++)
            line(player, "I've spoken to Dr Oddenstein, and discovered Ernest is a", line++)
            line(player, "chicken", line++)
            line(player, "I need to bring !!Dr Oddenstein?? parts for his machine", line++)
            line(player, if (player.inventory.containsItem(OIL_CAN)) "<str>1 Oil Can" else RED + "1 Oil Can", line++)
            line(player, if (player.inventory.containsItem(PRESSURE_GAUGE)) { "<str>1 Pressure Gauge" } else { RED + "1 Pressure Gauge" }, line++)
            line(player, if (player.inventory.containsItem(RUBBER_TUBE)) "<str>1 Rubber Tube" else RED + "1 Rubber Tube", line++)
        } else if (stage == 100) {
            line(player, "I have spoken to Veronica", line++, true)
            line(player, "I have collected all the parts for the machine", line++, true)
            line(player, "Dr Oddenstein thanked me for helping fix his machine", line++, true)
            line(player, "We turned Ernest back to normal and he rewarded me", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    override fun finish(player: Player) {
        player.unlock()
        super.finish(player)
        var line = 10
        drawReward(player, "4 Quest Points", line++)
        drawReward(player, "300 coins", line)
        sendMessage(player, "Ernest hands you 300 coins.")

        removeAttributes(player, "piranhas-killed", "pressure-gauge")
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.FEATHER_314, 230)
        if (!player.inventory.add(COINS)) {
            GroundItemManager.create(COINS, player.location, player)
        }
    }

    override fun newInstance(`object`: Any?): Quest {
        definePlugins(ErnestNPC(), ErnestChickenNPC())
        return this
    }

    companion object {
        private val OIL_CAN = Item(Items.OIL_CAN_277)
        private val PRESSURE_GAUGE = Item(Items.PRESSURE_GAUGE_271)
        private val RUBBER_TUBE = Item(Items.RUBBER_TUBE_276)
        private val COINS = Item(Items.COINS_995, 300)
    }
}
