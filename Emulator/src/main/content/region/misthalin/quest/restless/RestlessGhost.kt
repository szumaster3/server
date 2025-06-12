package content.region.misthalin.quest.restless

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
class RestlessGhost : Quest(Quests.THE_RESTLESS_GHOST, 25, 24, 1, Vars.VARP_QUEST_RESTLESS_GHOST_PROGRESS_107, 0, 4, 5) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11

        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Father Aereck?? in the", line++)
            line(player, "!!church?? next to !!Lumbridge Castle??.", line++)
            line(player, "I must be unafraid of a !!Level 13 Skeleton??.", line++)
            line++
            limitScrolling(player, line, true)
        }
        if (stage == 10) {
            line(player, "Father Aereck asked me to help him deal with the Ghost in", line++, true)
            line(player, "the graveyard next to the church.", line++, true)
            line(player, "I should find !!Father Urhney?? who is an expert on !!ghosts??.", line++, stage >= 20)
            line(player, "He lives in a !!shack?? in !!Lumbridge Swamp??.", line++, stage >= 20)
            line++
        }
        if (stage == 20) {
            line(player, "I should find Father Urhney who is an expert on ghosts.", line++, true)
            line(player, "He lives in a shack in Lumbridge Swamp.", line++, true)
            line(player, "I should talk to the !!Ghost?? to find out why it is haunting the", line++, stage >= 30)
            line(player, "!!graveyard crypt??.", line++, stage >= 30)
            line++
        }
        if (stage == 30) {
            line(player, "I found Father Urhney in the swamp south of Lumbridge. He", line++, true)
            line(player, "gave me an Amulet of Ghostspeak to talk to the ghost.", line++, true)
            line(player, "I spoke to the Ghost and he told me he could not rest in", line++, true)
            line(player, "peace because an evil wizard had stolen his skull.", line++, true)
            line(player, "I should go and search the !!Wizard's Tower South West of", line++, stage >= 40)
            line(player, "!!Lumbridge?? for the !!Ghost's Skull??.", line++, stage >= 40)
            line++
        }
        if (stage == 40) {
            line(player, "I found the Ghost's Skull in the basement of the Wizards'", line++, true)
            line(player, "Tower. It was guarded by a skeleton, but I took it anyways.", line++, true)
            line(player, "I should take the !!Skull?? back to the !!Ghost?? so it can rest.", line++, stage >= 100)
            line++
        }
        if (stage == 100) {
            line(player, "Father Aereck asked me to help him deal with the Ghost in", line++, true)
            line(player, "the graveyard next to the church.", line++, true)
            line(player, "I found Father Urhney in the swamp south of Lumbridge. He", line++, true)
            line(player, "gave me an Amulet of Ghostspeak to talk to the ghost.", line++, true)
            line(player, "I spoke to the Ghost and he told me he could not rest in", line++, true)
            line(player, "peace because an evil wizard had stolen his skull.", line++, true)
            line(player, "I found the Ghost's Skull in the basement of the Wizards'", line++, true)
            line(player, "Tower. It was guarded by a skeleton, but I took it anyways.", line++, true)
            line(player, "I placed the Skull in the Ghost's coffin, and allowed it to", line++, true)
            line(player, "rest in peace once more, with gratitude for my help.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
        limitScrolling(player, line, false)
    }

    override fun finish(player: Player) {
        super.finish(player)
        drawReward(player, "1 Quest Point", 10)
        drawReward(player, "1125 Prayer XP", 11)
        sendString(player, "You have completed ${Quests.THE_RESTLESS_GHOST}!", Components.QUEST_COMPLETE_SCROLL_277, 4)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.SKULL_964, 240)
        rewardXP(player, Skills.PRAYER, 1125.0)
        closeChatBox(player)
        setVarp(player, Vars.VARP_RESTLESS_GHOST_728, 31, true)
        removeAttribute(player, "restless-ghost:urhney")
    }

    override fun newInstance(`object`: Any?): Quest = this
}
