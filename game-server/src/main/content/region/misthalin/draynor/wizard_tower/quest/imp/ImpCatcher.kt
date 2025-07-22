package content.region.misthalin.draynor.wizard_tower.quest.imp

import core.api.*
import core.api.allInInventory
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class ImpCatcher : Quest(Quests.IMP_CATCHER, 21, 20, 1, Vars.VARP_QUEST_IMP_CATCHER_PROGRESS_160, 0, 1, 2) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Wizard Mizgog?? who is", line++)
            line(player, "in the !!Wizard's Tower??.", line++)
            line(player, "There are no requirements for this quest.", line++)
        }

        if (stage == 1) {
            line(player, "I have spoken to Wizard Mizgog.", line++, true)
            line++

            line(player, "I need to collect some items by killing !!Imps??.", line++, allInInventory(player, *beads))
            if (beads.all { inInventory(player, it) }) {
                line(player, "I have collected all the missing beads and need to return", line++)
                line(player, "them to !!Wizard Mizgog??.", line++)
            } else {
                beads.forEach { itemId ->
                    val item = getItemName(itemId)
                    line(player, "!!1 $item??", line++, inInventory(player, itemId))
                }
            }
        }

        if(stage == 100) {
            line(player, "I have spoken to Wizard Mizgog.", line++, true)
            line(player, "I have collected all the beads.", line++, true)
            line++
            line(player, "Wizard Mizgog thanked me for finding his beads and", line++, true)
            line(player, "gave me an Amulet of Accuracy.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10

        sendItemOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.AMULET_OF_ACCURACY_1478)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "875 Magic XP", ln++)
        drawReward(player, "Amulet of Accuracy", ln)
        rewardXP(player, Skills.MAGIC, 875.0)
        addItemOrDrop(player, Items.AMULET_OF_ACCURACY_1478)
    }

    override fun newInstance(`object`: Any?): Quest = this

    companion object {
        val beads = intArrayOf(Items.WHITE_BEAD_1476, Items.RED_BEAD_1470, Items.YELLOW_BEAD_1472, Items.BLACK_BEAD_1474)
    }
}
