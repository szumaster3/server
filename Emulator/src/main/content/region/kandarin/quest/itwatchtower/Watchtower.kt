package content.region.kandarin.quest.itwatchtower

import content.data.GameAttributes
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
class Watchtower : Quest(Quests.WATCHTOWER, 131, 130, 4, Vars.VARP_QUEST_WATCHTOWER_PROGRESS_212, 0, 1, 13) {

    override fun drawJournal(player: Player, stage: Int, ) {
        super.drawJournal(player, stage)
        var line = 12

        if (stage == 0) {
            line(player, "I can start this quest by speaing to the !!Watchtower??", line++)
            line(player, "!!wizard?? on the top floor of Yanille's !!Watchtower??.", line++)
            line++
            line(player, "To complete this quest I need:", line++)
            line(player, "!!Level 14 Magic??", line++, hasLevelStat(player, Skills.MAGIC, 14))
            line(player, "!!Level 40 Mining??", line++, hasLevelStat(player, Skills.MINING, 40))
            line(player, "!!Level 14 Herblore??", line++, hasLevelStat(player, Skills.HERBLORE, 14))
            line(player, "!!Level 15 Thieving??", line++, hasLevelStat(player, Skills.THIEVING, 15))
            line(player, "!!Level 25 Agility??", line++, hasLevelStat(player, Skills.AGILITY, 25))
            line(player, "The north west guard wants a !!sign of friendship??.", line++)
            limitScrolling(player, line, true)
        }

        if (stage == 1) {
            line(player, "I accepted the challenge of finding the lost crystals.", line++)
            line++
        }

        if (stage == 2) {
            line(player, "I accepted the challenge of finding the lost crystals.", line++, true)
            line++
            if(inInventory(player, Items.FINGERNAILS_2384)) {
                line(player, "I found some !!fingernails?? as evidence.", line++)
                line(player, "I should take them to the !!Watchtower wizard??.", line++)
                line++
            }
            line++
        }

        if (stage == 20) {
            line(player, "I found some !!fingernails?? as evidence.", line++, true)
            line(player, "I should take them to the !!Watchtower wizard??.", line++, true)
            line++
            line(player, "I was given !!a map?? by the guard.", line++, true)
        }

        if(stage == 10) {
            line(player, "I need to search the !!skavid caves??.", line++, true)
            line(player, "I found my way into the skavid caves.", line++, true)
        }

        if (stage == 60) {
            line(player, "I used some cave nightshade to distract the enclave guard.", line++, true)
            line(player, "I need to defeat the ogre shamans and find the other crystals.", line++)
        }

        if(stage == 70) {
            line(player, "I tried to defeat the shamans, but they are protected by powerful magics!", line++)
            line(player, "I should speak with the Watchtower wizard to see if he has any advice for me.", line++)
        }

        if(stage == 80) {
            line(player, "I have made the ogre potion.", line++, true)
        }

        if(stage == 85) {
            line(player, "I gave the potion to the wizard. He infused it into a magic ogre potion.", line++, true)
            line(player, "I need to defeat the ogre shamans.", line++)
        }

        if(stage == 90) {
            line(player, "I killed all the ogre shamans.", line++, true)
            line(player, "I need to return all the crystals to the Watchtower wizard.", line++)
        }

        if (stage == 100) {
            line(player, "I helped the wizards in Yanille, and rescued the lost crystals.", line++, true)
            line(player, "I defeated the ogres and their minions.", line++)
            line(player, "I read the scroll and gained a new teleport spell.", line++, getAttribute(player, GameAttributes.WATCHTOWER_TELEPORT, false))
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
            line++
            line(player, "I can return to the Watchtower at any time. My task here is done.", line++)
            limitScrolling(player, line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.OGRE_RELIC_2372, 1)
        drawReward(player, "4 Quest Points", ln++)
        drawReward(player, "Watchtower Teleport spell", ln++)
        drawReward(player, "15,250 Magic XP", ln++)
        drawReward(player, "5000 coins", ln)
        rewardXP(player, Skills.MAGIC, 15250.0)
        addItemOrDrop(player, Items.COINS_995, 5000)
        removeAttributes(player, GameAttributes.WATCHTOWER_ROCK_CAKE)
    }

    override fun newInstance(`object`: Any?): Quest = this
}