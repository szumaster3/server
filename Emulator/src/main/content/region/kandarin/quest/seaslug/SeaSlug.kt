package content.region.kandarin.quest.seaslug

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
class SeaSlug : Quest(Quests.SEA_SLUG, 109, 108, 1, Vars.VARP_QUEST_SEA_SLUG_PROGRESS_159, 0, 1, 13) {
    companion object {
        const val ATTRIBUTE_TALK_WITH_KENT = "seaslug:kent-dialogue"
    }

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Caroline?? who is !!East??", line++, false)
            line(player, "!!of Ardougne??", line++, false)
            line++
            line(player, "Requirements:", line++, false)
            line(
                player,
                if (hasLevelStat(
                        player,
                        Skills.FIREMAKING,
                        30,
                    )
                ) {
                    "--- You'll need level 30 Firemaking/--"
                } else {
                    "You'll need level !!30 Firemaking??"
                },
                line++,
            )
            line++
        }

        if (stage >= 1) {
            line(player, "I have spoken to !!Caroline?? and agreed to help.", line++, stage > 5)
            line++
        }

        if (stage >= 4) {
            line(player, "I have !!Holgart?? the !!Swamp Paste?? and his boat is now ready", line++, stage >= 5)
            line(player, "to take me to the !!Fishing Platform??.", line++, stage >= 5)
            line++
        }

        if (stage == 10) {
            line(player, "I've found !!Kennith??, he's !!hiding behind some boxes??.", line++, true)
            line++
        }

        if (stage >= 15) {
            line(player, "I've found !!Kent?? on a small island", line++, true)
            line++
            line(player, "Kent has me to help !!Kennith escape??", line++, stage >= 20)
            line++
        }

        if (stage >= 20) {
            line(player, "After speaking to !!Bailey??, I found out that !!Sea Slugs??", line++, stage >= 25)
            line(player, "are afraid of !!heat??.", line++, stage >= 25)
            line++
            line(player, "I should find a way of lighting this damp torch.", line++, stage >= 25)
            line++
        }

        if (stage >= 25) {
            line(player, "I've created an opening to let Kennith escape", line++, stage >= 30)
            line(player, "Kennith can't get downstairs without some help", line++, stage >= 30)
            line++
        }

        if (stage >= 50) {
            line(player, "I've !!used the Crane?? to lower Kennith into the !!boat??", line++, stage > 50)
            line++
        }

        if (stage == 100) {
            line(player, "I've spoken to !!Caroline?? and she thanked me", line++, true)
            line(player, "for !!rescuing her family?? from the !!Sea Slugs??", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        sendItemOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.SEA_SLUG_1466)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "7175 Fishing XP", ln++)
        drawReward(player, "Oyster pearls", ln)
        rewardXP(player, Skills.FISHING, 7175.0)
        addItemOrDrop(player, Items.OYSTER_PEARLS_413)
        removeAttribute(player, ATTRIBUTE_TALK_WITH_KENT)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
