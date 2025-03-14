package content.region.asgarnia.quest.hetty

import core.api.closeChatBox
import core.api.inInventory
import core.api.quest.updateQuestTab
import core.api.rewardXP
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class WitchPotion : Quest(Quests.WITCHS_POTION, 31, 30, 1, Vars.VARP_QUEST_WITCHS_POTION_PROGRESS_67, 0, 1, 3) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11

        when (getStage(player)) {
            0 -> {
                line(player, "I can start this quest by speaking to !!Hetty?? in her house in", line++)
                line(player, "!!Rimmington??, West of !!Port Sarim??.", line)
            }

            20 -> {
                line(player, "I spoke to Hetty in her house at Rimmington. hetty told me", line++)
                line(player, "she could increase my magic power if I can bring her", line++)
                line(player, "certain ingredients for a potion.", line++)
                line(player, "Hetty needs me to bring her the following:", line++)
                if (inInventory(player, Items.ONION_1957, 1)) {
                    line(player, "I have an onion with me", line++, true)
                } else {
                    line(player, "!!An onion??", line++)
                }
                if (inInventory(player, Items.ONION_1957, 1)) {
                    line(player, "I have an onion with me", line++, true)
                } else {
                    line(player, "!!An onion??", line++)
                }
                if (inInventory(player, Items.RATS_TAIL_300, 1)) {
                    line(player, "I have a rat's tail with me", line++, true)
                } else {
                    line(player, "!!A rat's tail??", line++)
                }
                if (inInventory(player, Items.BURNT_MEAT_2146, 1)) {
                    line(player, "I have a piece of burnt meat with me", line++, true)
                } else {
                    line(player, "!!A piece of burnt meat??", line++)
                }
                if (inInventory(player, Items.EYE_OF_NEWT_221, 1)) {
                    line(player, "I have an eye of newt with me", line, true)
                } else {
                    line(player, "!!An eye of newt??", line)
                }
            }

            40 -> {
                line(player, "I brought her an onion, a rat's tail, a piece of burnt meat", line++, true)
                line(player, "and eye of newt which she used to make a potion.", line++, true)
                line(player, "I should drink from the !!cauldron?? and improve my magic!", line, true)
            }

            100 -> {
                line(player, "I brought her an onion, a rat's tail, a piece of burnt meat", line++, true)
                line(player, "and an eye of newt which she used to make a potion.", line++, true)
                line(player, "I drank from the cauldron and my magic power increased!", line++, true)
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!", line)
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var line = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.EYE_OF_NEWT_221, 240)
        drawReward(player, "1 Quest Point", line++)
        drawReward(player, "325 Magic XP", line)

        rewardXP(player, Skills.MAGIC, 325.0)
        closeChatBox(player)
        updateQuestTab(player)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
