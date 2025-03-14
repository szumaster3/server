package content.region.kandarin.quest.hazeelcult

import content.region.kandarin.quest.hazeelcult.handlers.HazeelCultListener
import core.api.addItemOrDrop
import core.api.getAttribute
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class HazeelCult : Quest(Quests.HAZEEL_CULT, 74, 73, 1, Vars.VARP_QUEST_HAZEEL_CULT_PROGRESS_223, 0, 1, 9) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        var attributeMahjarrat = getAttribute(player!!, HazeelCultListener.MAHJARRAT, true)
        var attributeCarnillean = getAttribute(player, HazeelCultListener.CARNILLEAN, true)

        super.drawJournal(player, stage)
        var line = 11
        player

        if (stage >= 0) {
            line(player, "I can start the quest by talking to !!Sir Ceril Carnillean?? at", line++, stage >= 1)
            line(player, "the house due !!West?? of !!Ardougne Zoo??", line++, stage >= 1)
            line++
        }
        if (stage >= 1) {
            line(player, "I spoke to !!Sir Ceril Carnillean?? at his house,", line++, stage >= 2)
            line(
                player,
                "and agreed to help him !!investigate?? the theft of a !!family heirloom??.",
                line++,
                stage >= 2,
            )
            line++
        }
        if (stage >= 2) {
            line(player, "I found a !!member of the cult?? called !!Clivet?? at the entrance", line++, stage >= 3)
            line(player, "to the !!cults hideout??, south of !!Ardougne??.", line++, stage >= 3)
            line++
        }

        if (stage >= 2 && attributeMahjarrat && !attributeCarnillean) {
            line(player, "", line++, false)
            line(player, "Having decided to assist the cult in their mission to revive !!Hazeel??,", line++, stage >= 4)
            line(player, "I tried to !!poison?? the !!Carnilleans?? but instead", line++, stage >= 4)
            line++
        }
        if (stage >= 3 && attributeMahjarrat && !attributeCarnillean) {
            line(player, "only managed to kill their pet dog.", line++, stage >= 4)
            line(player, "I spoke to !!Clivet?? and he told me that I had failed,", line++, stage >= 4)
            line(player, "but he gave me an !!amulet?? anyway.", line++, stage >= 4)
            line(player, "Then I managed to take the sewer rafts to the cult hideout.", line++, stage >= 4)
            line++
        }
        if (stage >= 5 && attributeMahjarrat && !attributeCarnillean) {
            line(player, "I found the scroll needed to revive !!Hazeel?? in a", line++, stage >= 6)
            line(player, "secret compartment upstairs at the !!Carnillean Mansion??.", line++, stage >= 6)
            line(player, "I gave !!Alomone?? the scroll from the !!Carnillean Mansion?? and", line++, stage >= 6)
            line(player, "helped perform the revivification of !!Hazeel??.", line++, stage >= 6)
            line++
        }
        if (stage == 100 && attributeMahjarrat && !attributeCarnillean) {
            line(player, "I was rewarded for all of my help", line++, stage == 100)
            line(player, "in returning !!Hazeel?? to his followers.", line++, stage == 100)
            line++
        }

        if (stage >= 2 && attributeCarnillean && !attributeMahjarrat) {
            line(player, "He told me a pack of lies about the !!Carnilleans??,", line++, stage >= 3)
            line(player, "then asked me to join the !!cult??. Obviously, I refused.", line++, stage >= 3)
            line(player, "I was still no closer to recovering the !!missing armour??.", line++, stage >= 3)
            line++
        }
        if (stage >= 3 && attributeCarnillean && !attributeMahjarrat) {
            line(player, "I managed to enter !!the hideout??, kill the !!cult leader?? and", line++, stage >= 4)
            line(player, "retrieve !!the armour??. I discovered that !!Jones the Butler??", line++, stage >= 4)
            line(player, "was secretly a !!member of the cult?? and a !!traitor??.", line++, stage >= 4)
            line++
        }
        if (stage >= 4 && attributeCarnillean && !attributeMahjarrat) {
            line(
                player,
                "I returned !!the armour??, but !!Ceril?? didn't believe !!Jones was involved??",
                line++,
                stage >= 5,
            )
            line(player, "with the cult and was responsible for the theft.", line++, stage >= 5)
            line++
        }
        if (stage >= 5 && attributeCarnillean && !attributeMahjarrat) {
            line(player, "I found undeniable evidence that the !!Butler?? was", line++, stage >= 100)
            line(player, "involved with the cult and gave it to !!Ceril??.", line++, stage >= 100)
            line++
        }
        if (stage == 100 && attributeCarnillean && !attributeMahjarrat) {
            line(player, "My name was cleared and", line++, stage == 100)
            line(player, "I graciously accepted the reward for all of my help.", line++, stage == 100)
            line++
        }
        if (stage == 100) {
            line(player, "<col=FF0000>QUEST COMPLETE!", line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10

        player.packetDispatch.sendItemZoomOnInterface(Items.COINS_995, 230, 277, 5)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "2,000 Coins", ln++)
        drawReward(player, "1500 Thieving XP", ln)
        rewardXP(player, Skills.THIEVING, 1500.0)
        addItemOrDrop(player, Items.COINS_995, 2005)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}
