package content.region.fremennik.quest.viking

import core.api.*
import core.api.quest.getQuestStage
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class FremennikTrials :
    Quest(Quests.THE_FREMENNIK_TRIALS, 64, 63, 3, Vars.VARP_QUEST_FREMENNIK_TRIALS_PROGRESS_347, 0, 1, 10) {
    class SkillRequirement(
        val skill: Int?,
        val level: Int?,
    )

    val requirements = arrayListOf<SkillRequirement>()

    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 11
        val started = getQuestStage(player, Quests.THE_FREMENNIK_TRIALS) > 0

        if (!started) {
            line(player, "Requirements to complete quest:", line++)
            line += 1
            line(player, "Level 40 Woodcutting", line++, getStatLevel(player, Skills.WOODCUTTING) >= 40)
            line(player, "Level 40 Crafting", line++, getStatLevel(player, Skills.CRAFTING) >= 40)
            line(player, "Level 25 Fletching", line++, getStatLevel(player, Skills.FLETCHING) >= 25)
            line(player, "I must also be able to defeat a !!level 69 enemy?? and must", line++)
            line(player, "not be afraid of !!combat without any weapons or armour.", line++)
            line += 1
            line(player, "I can start this quest by speaking to !!Chieftan Brundt?? on", line++)
            line(player, "the !!Fremennik Longhall??, which is in the town of !!Rellekka?? to", line++)
            line(player, "the north of !!Sinclair Mansion??.", line)
        } else if (started && stage != 100) {
            line(player, "In order to join the Fremenniks, I need to", line++)
            line(player, "!!earn the approval?? of !!7 members?? of the elder council.", line++)
            line(player, "I've written down the members who I can try to help:", line++)
            line(player, "Manni the Reveller", line++, getAttribute(player, "fremtrials:manni-vote", false))
            line(player, "Swensen the Navigator", line++, getAttribute(player, "fremtrials:swensen-vote", false))
            line(player, "Sigli the Huntsman", line++, getAttribute(player, "fremtrials:sigli-vote", false))
            line(player, "Olaf the Bard", line++, getAttribute(player, "fremtrials:olaf-vote", false))
            line(player, "Thorvald the Warrior", line++, getAttribute(player, "fremtrials:thorvald-vote", false))
            line(player, "Sigmund the Merchant", line++, getAttribute(player, "fremtrials:sigmund-vote", false))
            line(player, "Peer the Seer", line++, getAttribute(player, "fremtrials:peer-vote", false))
            val voteCount = getAttribute(player, "fremtrials:votes", 0)
            var voteText = "$voteCount votes"
            if (voteCount == 1) {
                voteText = "1 vote"
            }
            line(player, "So far I have gotten $voteText.", line++)
        } else if (stage == 100) {
            line(player, "I made my way to the far north of !!Kandarin?? and found", line++)
            line(player, "the Barbarian hometown of !!Rellekka??. The tribe that live", line++)
            line(player, "there call themselves the !!Fremennik??, and offerred me the", line++)
            line(player, "chance to join them if I could pass their trials.", line++)
            line += 1
            line(player, "I managed to persuade !!seven?? of the !!twelve?? council of", line++)
            line(player, "elders to vote for me at their next meeting. and become an", line++)
            line(player, "honorary member of the !!Fremennik??.", line++)
            line += 1
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++)
            line++
            line(player, "They also gave me a new name:", line++)
            line(player, getAttribute(player, "fremennikname", "name"), line)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.FREMENNIK_HELM_3748, 235)
        drawReward(player, "3 Quest points, 2.8k XP in:", ln++)
        drawReward(player, "Strength, Defence, Attack,", ln++)
        drawReward(player, "Hitpoints, Fishing, Thieving,", ln++)
        drawReward(player, "Agility,Crafting, Fletching,", ln++)
        drawReward(player, "Woodcutting", ln)
        rewardXP(player, Skills.STRENGTH, 2812.4)
        rewardXP(player, Skills.DEFENCE, 2812.4)
        rewardXP(player, Skills.ATTACK, 2812.4)
        rewardXP(player, Skills.HITPOINTS, 2812.4)
        rewardXP(player, Skills.FISHING, 2812.4)
        rewardXP(player, Skills.THIEVING, 2812.4)
        rewardXP(player, Skills.AGILITY, 2812.4)
        rewardXP(player, Skills.CRAFTING, 2812.4)
        rewardXP(player, Skills.FLETCHING, 2812.4)
        rewardXP(player, Skills.WOODCUTTING, 2812.4)
    }

    override fun newInstance(`object`: Any?): Quest {
        requirements.add(SkillRequirement(Skills.FLETCHING, 25))
        requirements.add(SkillRequirement(Skills.CRAFTING, 40))
        requirements.add(SkillRequirement(Skills.WOODCUTTING, 40))
        return this
    }
}
