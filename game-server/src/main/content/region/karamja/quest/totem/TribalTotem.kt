package content.region.karamja.quest.totem

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

/**
 * Represents the Tribal Totem quest.
 */
@Initializable
class TribalTotem : Quest(Quests.TRIBAL_TOTEM, 126, 125, 1, Vars.VARP_QUEST_TRIBAL_TOTEM_PROGRESS_200, 0, 1, 5) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        val thievingLevel = getStatLevel(player, Skills.THIEVING)
        val started = getQuestStage(player, Quests.TRIBAL_TOTEM) > 0

        if (!started) {
            line(player, "I can start this quest by speaking to !!Kangai Mau?? in !!the??", line++)
            line(player, "!!Shrimp & Parrot?? restaurant in !!Brimhaven??.", line++)
            line(player, "To complete this quest I need:", line++)
            line(player, "!!Level 21 Thieving??", line, thievingLevel >= 21)
        }

        if (stage in 1..100) {
            line(player, "I agreed to help !!Kangai Mau?? in Brimhaven recover the tribal", line++, stage >= 10)
            line(player, "totem stolen from his village by!!Lord Handelmort??.", line++, stage >= 10)
            line++

            if (stage >= 20) {
                line++
                line(player, "I found a package for !!Lord Handelmort?? at the !!G.P.D.T.??", line++, stage >= 20)
                line(player, "!!Depot?? and swapped the label for !!Wizard Cromperty's??", line++, stage >= 20)
                line(player, "experimental teleport block.", line++, stage >= 20)
            }

            if (stage >= 30) {
                line++
                line(player, "I tricked the !!G.P.D.T.?? men into delivering the teleport", line++, stage >= 30)
                line(player, "block to !!Lord Handelmort?? and used it to teleport", line++, stage >= 30)
                line(player, "myself inside the mansion.", line++, stage >= 30)
            }
        }

        if (stage == 100) {
            line++
            line(player, "After bypassing the traps and security inside the mansion,", line++)
            line(player, "I reclaimed the totem and returned it to Kangai Mau,", line++)
            line(player, "who rewarded me for my efforts.", line++)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.TOTEM_1857)
        drawReward(player, "1 Quest point", ln++)
        drawReward(player, "1,775 Thieving XP", ln++)
        drawReward(player, "5 Swordfish", ln)
        rewardXP(player, Skills.THIEVING, 1775.0)
        addItemOrDrop(player, Items.SWORDFISH_373, 5)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
