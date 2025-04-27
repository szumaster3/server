package content.region.kandarin.quest.seaslug

import core.api.addItemOrDrop
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

/**
 * Represents the Sea slug quest.
 *
 * Sources:
 * - [YouTube #1](https://www.youtube.com/watch?v=MlBZXxD5yq4)
 * - [YouTube #2](https://youtu.be/FmVIqJFP91Q?si=IiIYFT25jPYwkFUH)
 * - [YouTube #3](https://www.youtube.com/watch?v=Xh-tE6Bdv1U)
 */
@Initializable
class SeaSlug : Quest(Quests.SEA_SLUG, 109, 108, 1, Vars.VARP_QUEST_SEA_SLUG_PROGRESS_159, 0, 1, 13) {

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Caroline?? who is !!East??", line++, false)
            line(player, "!!of Ardougne??.", line++, false)
            line++
            line(player, "Requirements:", line++, false)
            line(player, "You'll need level !!30 Firemaking??.", line++)
            line++
        }

        if (stage == 2) {
            line(player, "I have spoken to !!Caroline?? and agreed to help.", line++)
            line++
        }

        if (stage == 5) {
            line(player, "I have spoken to !!Caroline?? and agreed to help.", line++, true)
            line++
            line(player, "I have !!Holgart?? the !!Swamp Paste?? and his boat is now ready", line++)
            line(player, "to take me to the !!Fishing Platform??.", line++)
            line++
        }

        if (stage == 10) {
            line(player, "I have spoken to !!Caroline?? and agreed to help.", line++, true)
            line++
            line(player, "I have !!Holgart?? the !!Swamp Paste?? and his boat is now ready", line++, true)
            line(player, "to take me to the !!Fishing Platform??.", line++, true)
            line++
            line(player, "I've found !!Kennith??, he's !!hiding behind some boxes??.", line++)
            line++
        }

        if (stage == 15) {
            line(player, "I have spoken to !!Caroline?? and agreed to help.", line++, true)
            line++
            line(player, "I have !!Holgart?? the !!Swamp Paste?? and his boat is now ready", line++, true)
            line(player, "to take me to the !!Fishing Platform??.", line++, true)
            line++
            line(player, "I've found !!Kennith??, he's !!hiding behind some boxes??.", line++, true)
            line++
            line(player, "I've found !!Kent?? on a small island", line++)
            line(player, "Kent has me to help !!Kennith escape??.", line++)
            line++
        }

        if (stage == 20) {
            line(player, "I have spoken to !!Caroline?? and agreed to help.", line++, true)
            line++
            line(player, "I have !!Holgart?? the !!Swamp Paste?? and his boat is now ready", line++, true)
            line(player, "to take me to the !!Fishing Platform??.", line++, true)
            line++
            line(player, "I've found !!Kennith??, he's !!hiding behind some boxes??.", line++, true)
            line++
            line(player, "I've found !!Kent?? on a small island", line++, true)
            line(player, "Kent has me to help !!Kennith escape??.", line++, true)
            line++
            line(player, "After speaking to !!Bailey??, I found out that !!Sea Slugs??", line++)
            line(player, "are afraid of !!heat??.", line++)
            line++
            line(player, "I should find a way of lighting this damp torch.", line++)
            line++
        }

        if (stage == 25) {
            line(player, "I have spoken to !!Caroline?? and agreed to help.", line++, true)
            line++
            line(player, "I have !!Holgart?? the !!Swamp Paste?? and his boat is now ready", line++, true)
            line(player, "to take me to the !!Fishing Platform??.", line++, true)
            line++
            line(player, "I've found !!Kennith??, he's !!hiding behind some boxes??.", line++, true)
            line++
            line(player, "I've found !!Kent?? on a small island", line++, true)
            line(player, "Kent has me to help !!Kennith escape??.", line++, true)
            line++
            line(player, "After speaking to !!Bailey??, I found out that !!Sea Slugs??", line++, true)
            line(player, "are afraid of !!heat??.", line++, true)
            line++
            line(player, "I should find a way of lighting this damp torch.", line++, true)
            line++
            line(player, "I've created an opening to let Kennith escape", line++)
            line(player, "Kennith can't get downstairs without some help", line++)
            line++
        }

        if (stage >= 50) {
            line(player, "I have spoken to !!Caroline?? and agreed to help.", line++, true)
            line++
            line(player, "I have !!Holgart?? the !!Swamp Paste?? and his boat is now ready", line++, true)
            line(player, "to take me to the !!Fishing Platform??.", line++, true)
            line++
            line(player, "I've found !!Kennith??, he's !!hiding behind some boxes??.", line++, true)
            line++
            line(player, "I've found !!Kent?? on a small island", line++, true)
            line(player, "Kent has me to help !!Kennith escape??.", line++, true)
            line++
            line(player, "After speaking to !!Bailey??, I found out that !!Sea Slugs??", line++, true)
            line(player, "are afraid of !!heat??.", line++, true)
            line++
            line(player, "I should find a way of lighting this damp torch.", line++, true)
            line++
            line(player, "I've created an opening to let Kennith escape", line++, true)
            line(player, "Kennith can't get downstairs without some help", line++, true)
            line++
            line(player, "I've !!used the Crane?? to lower !!Kennith?? into the !!boat??.", line++)
            line++
        }

        if (stage == 100) {
            line(player, "I have spoken to !!Caroline?? and agreed to help.", line++, true)
            line++
            line(player, "I have !!Holgart?? the !!Swamp Paste?? and his boat is now ready", line++, true)
            line(player, "to take me to the !!Fishing Platform??.", line++, true)
            line++
            line(player, "I've found !!Kennith??, he's !!hiding behind some boxes??.", line++, true)
            line++
            line(player, "I've found !!Kent?? on a small island", line++, true)
            line(player, "Kent has me to help !!Kennith escape??.", line++, true)
            line++
            line(player, "After speaking to !!Bailey??, I found out that !!Sea Slugs??", line++, true)
            line(player, "are afraid of !!heat??.", line++, true)
            line++
            line(player, "I should find a way of lighting this damp torch.", line++, true)
            line++
            line(player, "I've created an opening to let Kennith escape", line++, true)
            line(player, "Kennith can't get downstairs without some help", line++, true)
            line++
            line(player, "I've !!used the Crane?? to lower Kennith into the !!boat??.", line++, true)
            line++
            line(player, "I've spoken to !!Caroline?? and she thanked me", line++, true)
            line(player, "for !!rescuing her family?? from the !!Sea Slugs??", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.SEA_SLUG_1466)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "7175 Fishing XP", ln++)
        drawReward(player, "Oyster pearls", ln)
        rewardXP(player, Skills.FISHING, 7175.0)
        addItemOrDrop(player, Items.OYSTER_PEARLS_413, 1)
    }

    override fun newInstance(`object`: Any?): Quest = this
}
