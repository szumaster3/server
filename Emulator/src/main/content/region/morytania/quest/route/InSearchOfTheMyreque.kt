package content.region.morytania.quest.route

import core.api.quest.isQuestComplete
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

class InSearchOfTheMyreque :
    Quest(Quests.IN_SEARCH_OF_THE_MYREQUE, 80, 79, 2, Vars.VARP_QUEST_IN_SEARCH_OF_MYREQUE_PROGRESS_387, 0, 1, 101) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return
        if (stage == 0) {
            line(player, "I can start this quest by talking to a !!stranger?? in the '!!Hair ", line++, stage >= 3)
            line(player, "!!of the dog Inn??' located in the town of !!Canafis?? in the land of", line++, stage >= 3)
            line(player, "!!Morytania.??", line++, stage >= 3)
            line++
            line(player, "I need to complete the following quest:-", line++, stage >= 3)
            if (isQuestComplete(player, Quests.NATURE_SPIRIT)) {
                line(player, "<str><col=000000>Nature Spirit</col></str>", line++, false)
            } else {
                line(player, Quests.NATURE_SPIRIT, line++, false)
            }
            line(player, "I also need to be able to defeat a level !!97 foe??.", line++, false)
        }
        if (stage >= 1) {
            line(player, "I was set a quest by !!Vanstrom Klause?? to !!deliver weapons?? to a", line++, stage >= 1)
            line(player, "group called !!the Myreque??.", line++, stage >= 1)
            line++
        }
        if (stage >= 2) {
            line(player, "After negotiating with a !!boatman??, fixing a broken rope bridge", line++, stage >= 1)
            line(
                player,
                "and answering a guards questions I eventually found the group called !!the Myreque??.",
                line++,
                stage >= 1,
            )
            line(
                player,
                "However, it seems that I was tricked by Vanstrom who turned out to be a vampyre following me to the Myreque's hideout.",
                line++,
                stage >= 1,
            )
            line(player, "He killed Sani and Harold, both young members of the Myreque.", line++, stage >= 1)
            line++
        }
        if (stage >= 3) {
            line(player, "During the quest I found a quicker route between Mort'ton and Canifis,", line++, stage >= 1)
            line(
                player,
                "and I feel that I better understand the reason for the darkness of Morytania.",
                line++,
                stage >= 1,
            )
            line(player, "Perhaps in the future, I can offer help to the Myreque?", line++, stage >= 1)
            line++
        }
        if (stage == 100) {
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.STAFF_1379, 230, 277, 5)
        drawReward(player, "2 Quest Points", ln++)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
