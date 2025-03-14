package content.region.misthalin.quest.imp

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.rewardXP
import core.api.sendItemOnInterface
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
        player ?: return
        if (getStage(player) == 0) {
            line(player, "I can start this quest by speaking to !!Wizard Mizgog?? who is", line++)
            line(player, "in the !!Wizard's Tower??", line++)
            line(player, "There are no requirements for this quest.", line++)
        } else if (getStage(player) == 10) {
            line(player, "<str>I have spoken to Wizard Mizgog.", line++)
            line(player, "I need to collect some items by killing !!Imps??.", line++)
            if (inInventory(player, Items.BLACK_BEAD_1474) &&
                inInventory(player, Items.RED_BEAD_1470) &&
                inInventory(
                    player,
                    Items.WHITE_BEAD_1476,
                ) &&
                inInventory(player, Items.YELLOW_BEAD_1472)
            ) {
                line(player, "I have collected all the missing beads and need to return", line++)
                line(player, "them to !!Wizard Mizgog??.", line++)
                return
            }
            line++
            line(
                player,
                if (!inInventory(player, Items.BLACK_BEAD_1474)) "!!1 Black Bead??" else "<str>1 Black Bead",
                line++,
            )
            line(player, if (!inInventory(player, Items.RED_BEAD_1470)) "!!1 Red Bead??" else "<str>1 Red Bead", line++)
            line(
                player,
                if (!inInventory(player, Items.WHITE_BEAD_1476)) "!!1 White Bead??" else "<str>1 White Bead",
                line++,
            )
            line(
                player,
                if (!inInventory(player, Items.YELLOW_BEAD_1472)) "!!1 Yellow Bead??" else "<str>1 Yellow Bead",
                line++,
            )
        } else {
            line++
            line(player, "I have spoken to Wizard Mizgog.", line++, true)
            line(player, "I have collected all the beads.", line++, true)
            line(player, "Wizard Mizgog thanked me for finding his beads and gave", line++, true)
            line(player, "me and Amulet of Accuracy.", line++ + 1, true)
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        sendItemOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.AMULET_OF_ACCURACY_1478)

        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "875 Magic XP", ln++)
        drawReward(player, "Amulet of Accuracy", ln)
        rewardXP(player, Skills.MAGIC, 875.0)
        addItemOrDrop(player, Items.AMULET_OF_ACCURACY_1478)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
