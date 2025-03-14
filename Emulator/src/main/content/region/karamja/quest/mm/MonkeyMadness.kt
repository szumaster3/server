package content.region.karamja.quest.mm

import core.api.addItemOrDrop
import core.api.quest.updateQuestTab
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class MonkeyMadness : Quest(Quests.MONKEY_MADNESS, 88, 87, 3, Vars.VARP_QUEST_MONKEY_MADNESS_PROGRESS_365, 0, 1, 9) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12
        if (stage == 0) {
            line(player, "In !!The Grand Tree??, I exposed the treachery of !!King??", line++)
            line(player, "!!Narnode Shareen's?? then High Tree Guardian, !!Glough.??", line++)
            line(player, "!!Glough?? has since been deposed. To start this quest I must", line++)
            line(player, "check how !!King Shareen?? is faring.", line++)
            line++
            line(player, "To complete this quest I need:", line++)
            line(player, "- To be able to defeat a !!level 195 Jungle Demon??", line++)
            line++
        }
        if (stage >= 10) {
            line(player, "Narnode has asked if i would find out if the 10th squad", line++, stage > 10)
            line(player, "ever reached the shipyard.", line++, stage > 10)
            line++
            line(player, "Narnode has given me a copy of the Royal seal so that I", line++, stage > 10)
            line(player, "may identify myself as his envoy.", line++, stage > 10)
            line++
            line(player, "I have agreed to investigate the !!Karamjan?? shipyard.", line++, stage > 10)
        }
        if (stage >= 20) {
            line(player, "Narnode asked to speak to !!Daero?? in order to", line++, stage > 20)
            line(player, "prepare some orders.", line++, stage > 20)
            line++
        }
        if (stage >= 24) {
            line(player, "I have found that the 10th squad was indeed blown off", line++, stage > 24)
            line(player, "course and never reached the shipyard. I am currently on", line++, stage > 24)
            line(player, "a mission for king Narnode Shareen to investigate their", line++, stage > 24)
            line(player, "whereabouts.", line++, stage > 24)
            line++
            line(player, "!!Chapter 2??", line++, false)
            line++
            line(player, "I have made my rather painful way to a large village", line++, stage > 24)
            line(player, "populated by a various types of monkey.", line++, stage > 24)
        }
        if (stage >= 40) {
            line(player, "King Narnode suspects that someone has forged his royal seal.", line++, stage > 40)
            line(player, "Find Glough and notify him about the situation.", line++, stage > 40)
            line++
        }
        if (stage >= 45) {
            line(player, "Glough says the humans are going to invade! Report back to the king.", line++, stage > 45)
            line++
        }
        if (stage >= 46) {
            line(player, "Talk to the prisoner at the top of the tree", line++, stage > 46)
            line++
        }
        if (stage >= 47) {
            line(player, "Search Glough's home for clues.", line++, stage > 47)
            line++
        }
        if (stage >= 55) {
            line(player, "Glough has placed guards on the front gate to stop you escaping!", line++, stage > 55)
            line(player, "Use King Narnode's glider pilot fly you away until things calm down.", line++, stage > 55)
            if (player.hasItem(Item(Items.LUMBER_ORDER_787))) {
                line(player, "Bring the lumber order to Charlie", line++, stage > 55)
            }
            line++
        }
        if (stage >= 60) {
            line(player, "Search Glough's girlfriend's house for his chest keys.", line++, stage > 60)
        }
        if (stage >= 60) {
            if (player.hasItem(Item(Items.GLOUGHS_KEY_788))) {
                line(player, "Find a use for Gloughs key and report back to the king.", line++, stage > 60)
            }
        }
        if (stage >= 70) {
            line(player, "Find a use for the strange twigs from King Narnode Shareen.", line++, stage > 70)
        }
        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.MSPEAK_AMULET_4022)
        drawReward(player, "3 Quest Points", ln++)
        drawReward(player, "10,000 coins", ln++)
        drawReward(player, "3 diamonds", ln)
        addItemOrDrop(player, Items.COINS_995, 10000)
        addItemOrDrop(player, Items.DIAMOND_1601, 3)
        updateQuestTab(player)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
