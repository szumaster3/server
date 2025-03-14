package content.region.kandarin.quest.itwatchtower

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
class WatchTower : Quest(Quests.WATCHTOWER, 131, 130, 4, Vars.VARP_QUEST_WATCHTOWER_PROGRESS_212, 0, 1, 13) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var line = 12

        if (stage == 0) {
            line(player, "I can start this quest by speaing to the !!Watchtower??", line++)
            line(player, "!!wizard?? on the top floor of Yanille's !!Watchtower??.", line++)
            line++
            line(player, "To complete this quest I need:", line++)
            line(player, "!!Level 14 Magic??", line++, getStatLevel(player, Skills.MAGIC) >= 14)
            line(player, "!!Level 40 Mining??", line++, getStatLevel(player, Skills.MINING) >= 40)
            line(player, "!!Level 14 Herblore??", line++, getStatLevel(player, Skills.HERBLORE) >= 14)
            line(player, "!!Level 15 Thieving??", line++, getStatLevel(player, Skills.THIEVING) >= 15)
            line(player, "!!Level 25 Agility??", line++, getStatLevel(player, Skills.AGILITY) >= 25)
            line(player, "The north west guard wants a sign of !!friendship??.", line++)
            line++
        }

        if (stage >= 1) {
            line(player, "I accepted the challenge of finding the lost crystals.", line++)
            line++
        }

        if (inInventory(player, Items.FINGERNAILS_2384)) {
            line(player, "I found some fingernails as evidence.", line++, getQuestStage(player, Quests.WATCHTOWER) >= 2)
            line(
                player,
                "I should take them to the Watchtower wizard.",
                line++,
                getQuestStage(player, Quests.WATCHTOWER) >= 2,
            )
            line++
        }

        if (stage >= 99) {
            line(player, "I was given a map by the guard.", line++)
            line(player, "I need to search the skavid caves.", line++)
            line(player, "I found my way into the skavid caves.", line++)
            line(player, "I used some cave nightshade to distract the enclave guard.", line++)
            line(player, "I need to defeat the ogre shamans and find the other crystals.", line++)
            line(player, "I tried to defeat the shamans, but they are protected by powerful magics!", line++)
            line(player, "I should speak with the Watchtower wizard to see if he has any advice for me.", line++)
            line(player, "I have made the ogre potion.", line++)
            line(player, "I gave the potion to the wizard. He infused it into a magic ogre potion.", line++)
            line(player, "I need to defeat the ogre shamans.", line++)
            line(player, "I killed all the ogre shamans.", line++)
            line(player, "I need to return all the crystals to the Watchtower wizard.", line++)
            line(player, "I helped the wizards in Yanille, and rescued the lost crystals.", line++)
            line(player, "I defeated the ogres and their minions.", line++)
            line(player, "I read the scroll and gained a new teleport spell.", line++)
            line(player, "I can return to the Watchtower at any time. My task here is done.", line++)
        }

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.OGRE_RELIC_2372, 1)
        drawReward(player, "4 Quest Points", ln++)
        drawReward(player, "Watchtower Teleport spell", ln++)
        drawReward(player, "15,250 Magic XP", ln++)
        drawReward(player, "5000 Coins", ln++)
        rewardXP(player, Skills.MAGIC, 1500.0)
        addItemOrDrop(player, Items.COINS_995, 5000)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
// 4 quest points
// 15,250 Magic experience
// 5,000 coins
// A spell scroll, which will teach you how to use the Watchtower Teleport spell
// Access to Gu'Tanoth and the Ogre Enclave
// 2 Treasure Hunter keys (Ironman accounts will not receive these)
