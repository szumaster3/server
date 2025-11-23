package content.region.morytania.port_phasmatys.quest.ahoy

import content.region.morytania.port_phasmatys.quest.ahoy.plugin.GhostsAhoyUtils
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import shared.consts.Components
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class GhostsAhoy : Quest(Quests.GHOSTS_AHOY, 68, 67, 2, Vars.VARBIT_QUEST_GHOST_AHOY_PROGRESS_217, 0, 1, 8) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {

            fun questText(name: String, completed: Boolean) =
                if (completed) "<str><col=000000>$name</col></str>"
                else "!!$name??"

            fun reqLevelText(level: Int, skill: Int): String {
                val name = Skills.SKILL_NAME[skill]
                return if (hasLevelStat(player, skill, level))
                    "<str><col=000000>level $level $name</col></str>"
                else
                    "!!level $level $name??"
            }

            line(player, "To start this quest I need to speak to !!Velorina??,", line++, false)
            line(player, "a ghost of !!Port Phasmatys??", line++, false)
            line++

            val agilityReq = reqLevelText(25, Skills.AGILITY)
            val cookingReq = reqLevelText(20, Skills.COOKING)

            line(player, "I must have at least $agilityReq, $cookingReq,", line++, false)
            line(player, "and be able to defeat a !!level 32 monster??.", line++, false)
            line++

            line(player, "I must also have completed the following quests:", line++, false)

            line(player, questText(Quests.PRIEST_IN_PERIL, isQuestComplete(player, Quests.PRIEST_IN_PERIL)), line++, false)
            line(player, questText(Quests.THE_RESTLESS_GHOST, isQuestComplete(player, Quests.THE_RESTLESS_GHOST)), line++, false)

            line++
        }

        if (stage == 1) {
            line(player, "I have spoken with !!Velorina??,", line++, true)
            line(player, "who has told me the sad history of the ghosts of !!Port Phasmatys??.", line++, true)
            line++
            line(player, "She has asked me to plead with Necrovarus in the Phasmatyan Temple", line++, false)
            line(player, "to let any ghost who so wishes pass over into the next world.", line++, false)
            line++
        }

        if (stage == 2) {
            line(player, "I have spoken with !!Velorina??,", line++, true)
            line(player, "who has told me the sad history of the ghosts of !!Port Phasmatys??.", line++, true)
            line++
            line(player, "She has asked me to plead with Necrovarus in the Phasmatyan Temple", line++, true)
            line(player, "to let any ghost who so wishes pass over into the next world.", line++, true)
            line++
            line(player, "I pleaded with Necrovarus, to no avail.", line++, false)
            line++
        }

        if (stage == 3) {
            line(player, "I have spoken with !!Velorina??,", line++, true)
            line(player, "who has told me the sad history of the ghosts of !!Port Phasmatys??.", line++, true)
            line++
            line(player, "She has asked me to plead with Necrovarus in the Phasmatyan Temple", line++, true)
            line(player, "to let any ghost who so wishes pass over into the next world.", line++, true)
            line++
            line(player, "I pleaded with Necrovarus, to no avail.", line++, true)
            line++
            line(player, "Velorina was crestfallen at !!Necrovarus'?? refusal to lift his ban,", line++, false)
            line(player, "and she told me of a woman who fled Port Phasmatys", line++, false)
            line(player, "before the townsfolk died, and to seek her out, as she", line++, false)
            line(player, "may know of a way around Necrovarus' stubbornness.", line++, false)
            line++
        }

        if (stage == 4) {
            line(player, "I have spoken with !!Velorina??,", line++, true)
            line(player, "who has told me the sad history of the ghosts of !!Port Phasmatys??.", line++, true)
            line++
            line(player, "She has asked me to plead with Necrovarus in the Phasmatyan Temple", line++, true)
            line(player, "to let any ghost who so wishes pass over into the next world.", line++, true)
            line++
            line(player, "I pleaded with Necrovarus, to no avail.", line++, true)
            line++
            line(player, "Velorina was crestfallen at !!Necrovarus'?? refusal to lift his ban,", line++, true)
            line(player, "and she told me of a woman who fled Port Phasmatys", line++, true)
            line(player, "before the townsfolk died, and to seek her out, as she", line++, true)
            line(player, "may know of a way around Necrovarus' stubbornness.", line++, true)
            line++
            line(player, "I found the old woman, who told me of an enchantment", line++, false)
            line(player, "she can perform on the !!Amulet of Ghostspeak??,", line++, false)
            line(player, "which will then let me command Necrovarus to do my bidding.", line++, false)
            line++
        }

        if (stage == 99) {
            line(player, "I have spoken with !!Velorina??,", line++, true)
            line(player, "who has told me the sad history of the ghosts of !!Port Phasmatys??.", line++, true)
            line++
            line(player, "She has asked me to plead with Necrovarus in the Phasmatyan Temple", line++, true)
            line(player, "to let any ghost who so wishes pass over into the next world.", line++, true)
            line++
            line(player, "I pleaded with Necrovarus, to no avail.", line++, true)
            line++
            line(player, "Velorina was crestfallen at !!Necrovarus'?? refusal to lift his ban,", line++, true)
            line(player, "and she told me of a woman who fled Port Phasmatys", line++, true)
            line(player, "before the townsfolk died, and to seek her out, as she", line++, true)
            line(player, "may know of a way around Necrovarus' stubbornness.", line++, true)
            line++
            line(player, "I found the old woman, who told me of an enchantment", line++, true)
            line(player, "she can perform on the !!Amulet of Ghostspeak??,", line++, true)
            line(player, "which will then let me command Necrovarus to do my bidding.", line++, true)
            line++
            line(player, "I brought the old woman the !!Book of Haricanto??,", line++, false)
            line(player, "the !!Robes of Necrovarus??, and a translation manual.", line++, false)
            line++
            line(player, "The old woman used the items I brought her to", line++, false)
            line(player, "perform the enchantment on the Amulet of Ghostspeak.", line++, false)
            line(player, "I have commanded !!Necrovarus?? to remove his ban.", line++, false)
            line(player, "I have told !!Velorina?? that !!Necrovarus?? has been commanded", line++, false)
            line(player, "to remove his ban, and to allow any ghost who so desires", line++, false)
            line(player, "to pass over into the next plane of existence.", line++, false)
            line++
        }

        if (stage == 100) {
            line++
            line(player, "Velorina gave me the Ectophial in return,", line++, false)
            line(player, "which I can use to teleport to the !!Temple of Phasmatys??.", line++, false)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.ECTOPHIAL_4251, 230)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "2,400 Prayer XP", ln++)
        drawReward(player, "Free passage into Port Phasmatys", ln)
        rewardXP(player, Skills.PRAYER, 2400.0)
        setVarbit(player, Vars.VARBIT_QUEST_GHOST_AHOY_PROGRESS_217, 8, true)
        removeAttributes(
            player,
            GhostsAhoyUtils.shipFlag,
            GhostsAhoyUtils.shipBottom,
            GhostsAhoyUtils.shipSkull,
            GhostsAhoyUtils.rightShip,
            GhostsAhoyUtils.colorMatching,
            GhostsAhoyUtils.windSpeed,
            GhostsAhoyUtils.lastMapScrap,
            GhostsAhoyUtils.petitionsigns,
            GhostsAhoyUtils.petitionstart,
            GhostsAhoyUtils.petitioncomplete,
        )
    }

    override fun newInstance(`object`: Any?): Quest = this
}
