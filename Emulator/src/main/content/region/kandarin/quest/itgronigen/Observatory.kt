package content.region.kandarin.quest.itgronigen

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class Observatory :
    Quest(Quests.OBSERVATORY_QUEST, 96, 95, 2, Vars.VARP_QUEST_OBSERVATORY_QUEST_PROGRESS_112, 0, 1, 7) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11

        if (stage >= 0) {
            line(player, "I can start this quest by talking to !!professor??, in the", line++, stage >= 1)
            line(player, "Observatory reception, !!south-west of Ardougne??.", line++, stage >= 1)
        }

        if (stage >= 1) {
            line(player, "Seems the Observatory telescope needs repairing,", line++, stage >= 10)
            line(player, "due to the nearby goblins. The !!professor?? wants me to help by", line++, stage >= 10)
            line(player, "getting the following, with the help of his !!assistant??:", line++, stage >= 10)
            line++
            line(
                player,
                "!!3 plain wooden planks??",
                line++,
                amountInInventory(player, Items.PLANK_960) < 3 || stage > 99,
            )
            line(player, "!!1 bronze bar??", line++, inInventory(player, Items.BRONZE_BAR_2349) || stage > 99)
            line(player, "!!1 molten glass??", line++, inInventory(player, Items.MOLTEN_GLASS_1775) || stage > 99)
            line(player, "!!1 lens mould??", line++, inInventory(player, Items.LENS_MOULD_602) || stage > 99)
            line++
        }

        if (stage >= 2) {
            line(player, "The !!professor?? was pleased to have all the pieces needed", line++, stage >= 12)
            line(player, "to fix the telescope. Apparently, the professor's last", line++, stage >= 12)
            line(player, "attempt at Crafting ended in disaster. So, he wants me to", line++, stage >= 12)
            line(player, "create the !!lens?? by using the !!molten glass?? with the !!mould??.", line++, stage >= 12)
            line(player, "Fine by me!", line++, stage >= 12)
            line(player, "The !!professor?? has gone ahead to the !!Observatory??. He", line++, stage >= 99)
            line(player, "wants me to meet him there by travelling through", line++, stage >= 99)
            line(player, "the dungeon below it.", line++, stage >= 99)
            line(player, "I hope I get to look through the telescope!", line++, stage >= 99)
            line++
        }

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++, false)
            line++
            line(player, "I should probably see what the !!assistant?? thinks of all this.", line++, false)
            line(player, "He should be pleased.", line++, false)
            line(player, "I had a word with the professor's assistant and he gave me", line++, false)
            line(player, "some !!wine??! What a pleasant chap. He also mentioned about", line++, false)
            line(player, "!!Scorpius?? and a grave - most cryptic indeed!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.NULL_11601, 230, Components.QUEST_COMPLETE_SCROLL_277, 5)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "2,250 Crafting XP", ln++)
        drawReward(player, "A payment depending on", ln++)
        drawReward(player, "which constellation you", ln++)
        drawReward(player, "observed", ln)
        rewardXP(player, Skills.CRAFTING, 2250.0)
        removeAttribute(player, "observatory:goblin-spawn")
        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_17_3652, 1, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
