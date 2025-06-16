package content.region.morytania.phasmatys.quest.ahoy

import content.region.morytania.phasmatys.quest.ahoy.plugin.GhostsAhoyUtils
import core.api.quest.isQuestComplete
import core.api.removeAttributes
import core.api.rewardXP
import core.api.sendItemZoomOnInterface
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class GhostsAhoy : Quest(Quests.GHOSTS_AHOY, 68, 67, 2, Vars.VARBIT_QUEST_GHOST_AHOY_PROGRESS_217, 0, 1, 8) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return
        if (stage == 0) {
            line(player, "to start this quest I need to speak to !!Velorina??,", line++, false)
            line(player, "a ghost of !!Port Phasmatys??", line++, false)
            line++
            line(player, "I must have at least !!level 25 agility??, !!level 20 cooking??,", line++, false)
            line(player, "and be enable to defeat a !!level 32 monster??.", line++, false)
            line++
            line(player, "I must also have completed the following quests:", line++, false)
            if (isQuestComplete(player, Quests.PRIEST_IN_PERIL)) {
                line(player, "<str><col=000000>${Quests.PRIEST_IN_PERIL}</col></str>", line++, false)
            } else {
                line(player, "!!Priest in Peril??", line++, false)
            }
            if (isQuestComplete(player, Quests.THE_RESTLESS_GHOST)) {
                line(player, "<str><col=000000>${Quests.THE_RESTLESS_GHOST}</col></str>", line++, false)
            } else {
                line(player, "!!The Restless Ghost??", line++, false)
            }
            line++
        }

        if (stage == 1) {
            line(player, "I have spoken with !!Velorina??,", line++, false)
            line(player, "who has told me the sad history of the ghosts of !!Port Phasmatys??.", line++, false)
            line(player, "She has asked me to plead with Necrovarus in the Phasmatyan Temple", line++, false)
            line(player, "to let any ghost who so wishes pass over into the next world.", line++, false)
            line++
        }

        if (stage == 2) {
            line(player, "I pleaded with Necrovarus, to no avail.", line++, false)
            line++
        }

        if (stage == 3) {
            line(player, "Velorina was crestfallen at !!Necrovarus'?? refusal to lift his ban,", line++, false)
            line(player, "and she told me of a woman who fled Port Phasmatys", line++, false)
            line(player, "before the townsfolk died, and to seek her out, as she", line++, false)
            line(player, "may know of a way around Necrovarus' stubbornness.", line++, false)
            line++
        }

        if (stage == 4) {
            line(player, "I found the old woman, who told me of an enchantment", line++, false)
            line(player, "she can perform on the !!Amulet of Ghostspeak??,", line++, false)
            line(player, "which will then let me command Necrovarus to do my bidding.", line++, false)
            line++
        }

        if (stage == 99) {
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
        player ?: return
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
