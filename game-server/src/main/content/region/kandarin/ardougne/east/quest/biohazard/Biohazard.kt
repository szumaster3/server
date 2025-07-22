package content.region.kandarin.ardougne.east.quest.biohazard

import content.data.GameAttributes
import core.api.isQuestComplete
import core.api.removeAttributes
import core.api.rewardXP
import core.api.sendItemZoomOnInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class Biohazard : Quest(Quests.BIOHAZARD, 36, 35, 3, Vars.VARP_QUEST_BIOHAZARD_PROGRESS_68, 0, 1, 16) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Elena?? who is in !!East??", line++)
            line(player, "!!Ardougne??.", line++)
            line++
            line(player, "Requirements:", line++)
            line(player, "!!I need to complete ${Quests.PLAGUE_CITY} before I can attempt this??", line++, isQuestComplete(player, Quests.PLAGUE_CITY))
            line(player, "!!Quest??.", line++, isQuestComplete(player, Quests.PLAGUE_CITY))
            line++
        }

        if (stage > 0) {
            line(player, "I've spoken to Elena in East Ardougne. She told me that", line++, true)
            line(player, "the Mourners confiscated her Distillator while she was", line++, true)
            line(player, "helping plague victims in West Ardougne. She's asked me", line++, true)
            line(player, "to help her recover it.", line++, true)
            line++
        }

        if (stage == 1) {
            line(player, "I need to get into !!West Ardougne?? to find !!Elena's Distillator??.", line++, true)
            line(player, "However, the !!Mourners?? have blocked the tunnel I used", line++, true)
            line(player, "before. !!Elena?? sugested I talk to !!Jerico??, an old friend of", line++, true)
            line(player, "her fathers, and ask for his help in crossing the wall. His", line++, true)
            line(player, "home is north of the chapel in !!East Ardougne??.", line++, true)
            line++
        }

        if (stage >= 1) {
            line(player, "The Mourners blocked the tunnel I used to enter West", line++, true)
            line(player, "Ardougne before. However, I spoke to Jerico, an old friend", line++, true)
            line(player, "of Elena's father, about finding another way into the city.", line++, true)
            line++
        }

        if (stage >= 2) {
            line(player, "!!Jerico?? told me that !!Omart??, a friend of his, should be able", line++, true)
            line(player, "to help me get into !!West Ardougne??. He is waiting for me at", line++, true)
            line(player, "the southern end of the wall.", line++, true)
            line++
        }

        if (stage == 2) {
            line(player, "!!Omart??, a friend of !!Jerico??, has agreed to help me cross the", line++, true)
            line(player, "wall into !!West Ardougne??. However, I need to distract the", line++, true)
            line(player, "guards in the !!Watchtower?? first. Following the suggestion", line++, true)
            line(player, "of !!Omart??, I threw some !!Bird Feed?? near the tower. Now I just", line++, true)
            line(player, "need to release some !!Pigeons?? nearby and they should", line++, true)
            line(player, "distract the guards. !!Jerico?? should be able to supply the", line++, true)
            line(player, "!!Pigeons??.", line++, true)
            line++
        }

        if (stage == 3) {
            line(player, "!!Omart??, a friend of !!Jerico??, has agreed to help me cross the", line++, true)
            line(player, "wall into !!West Ardougne??. He is waiting for me at the", line++, true)
            line(player, "southern end of the wall.", line++, true)
            line++
        }

        if (stage >= 3) {
            line(player, "Omart and Kilron, friends of Jerico, helped me cross the", line++, true)
            line(player, "wall into West Ardougne.", line++, true)
            line++
        }

        if (stage == 4) {
            line(player, "!!Elena's Distillator?? should be somewhere in the !!Mourner??", line++, true)
            line(player, "!!Headquarters?? in the north east of !!West Ardougne??. I need", line++, true)
            line(player, "to get in and recover it.", line++, true)
            line++
        }

        if (stage == 7) {
            line(player, "!!Elena's Distillator?? should be somewhere in the !!Mourner??", line++, true)
            line(player, "!!Headquarters?? in the north east of !!West Ardougne??. I need", line++, true)
            line(player, "to get in and recover it. I used a !!Rotten Apple?? to poison", line++, true)
            line(player, "the !!Mourner's Stew?? which might help me.", line++, true)
            line++
        }

        if (stage == 8) {
            line(player, "I managed to get into the !!Mourner Headquarters?? in !!West??", line++, true)
            line(player, "!!Ardougne??. I should see if I can find !!Elena's Distillator??", line++, true)
            line(player, "inside.", line++, true)
            line++
        }

        if (stage == 10) {
            line(player, "I managed to get into the !!Mourner Headquarters?? in !!West??", line++, true)
            line(player, "!!Ardougne??. I found !!Elena's Distillator?? inside. I should return", line++, true)
            line(player, "it to her.", line++, true)
            line++
        }

        if (stage >= 10) {
            line(player, "I managed to get into the Mourner Headquarters in West", line++, true)
            line(player, "Ardougne. I found Elena's Distillator and returned it", line++, true)
            line(player, "to her.", line++, true)
            line++
        }

        if (stage == 11) {
            line(player, "!!Elena?? ran some tests on a !!Plague Sample?? using her", line++, true)
            line(player, "!!Distillator??. However, she wasn't able to discover anything.", line++, true)
            line(player, "She asked me to go to the !!Chemist?? in !!Rimmington?? to pick up", line++, true)
            line(player, "some !!Touch Paper?? before continuing on to take some", line++, true)
            line(player, "items to !!Guidor?? in !!Varrock??. She warned me that carrying", line++, true)
            line(player, "some of the items into !!Varrock?? might be problematic and", line++, true)
            line(player, "suggested I ask the !!Chemist?? for help with this.", line++, true)
            line++
        }

        if (stage > 11) {
            line(player, "Elena ran some tests on a Plague Sample using her", line++, true)
            line(player, "Distillator. However, she wasn't able to discover anything.", line++, true)
            line++
        }

        if (stage >= 11) {
            line(player, "!!Elena?? asked me to take some items to !!Guidor?? in !!Varrock??.", line++, true)
            line(player, "I've been warned that carrying some of these items into", line++, true)
            line(player, "!!Varrock?? might be problematic. The !!Chemist?? in !!Rimmington??", line++, true)
            line(player, "recommended I ask his !!Errand Boys?? for help with this.", line++, true)
            line(player, "However, he warned me they like to steal things so", line++, true)
            line(player, "suggested I only give them something they won't have", line++, true)
            line(player, "need of themselves.", line++, true)
            line++
        }

        if (stage > 15) {
            line(player, "!!Elena?? asked me to take some items to !!Guidor?? in !!Varrock??. I", line++, true)
            line(player, "was warned that carrying some of these items into !!Varrock??", line++, true)
            line(player, "might be problematic so I gave them to the !!Chemist's??", line++, true)
            line(player, "!!Errand Boys?? to smuggle in. I should meet them at the", line++, true)
            line(player, "!!Dancing Donkey Inn?? in south east !!Varrock??.", line++, true)
            line++
        }

        if (stage == 14) {
            line(player, "!!Elena?? asked me to take some items to !!Guidor??. I can find his", line++, true)
            line(player, "home in the south east corner of !!Varrock??.", line++, true)
            line++
        }

        if (stage > 14) {
            line(player, "Elena asked me to take some items to Guidor in Varrock so", line++, true)
            line(player, "that he could run his own tests on the Plague Sample.", line++, true)
            line++
        }

        if (stage == 15) {
            line(player, "!!Guidor?? ran his own tests on the !!Plague Sample?? and", line++, true)
            line(player, "discovered a disturbing truth, that the !!Plague?? of !!West??", line++, true)
            line(player, "!!Ardougne?? is a lie. I should return to !!Elena?? and tell her.", line++, true)
            line++
        }

        if (stage > 15) {
            line(player, "Guidor ran his tests and discovered a disturbing truth,", line++, true)
            line(player, "that the Plague of West Ardougne is a lie.", line++, true)
            line++
        }

        if (stage == 99) {
            line(player, "I returned to !!Elena?? and told her the truth about the !!Plague??.", line++, true)
            line(player, "She asked me to confront !!King Lathas?? immediately. I can", line++, true)
            line(player, "find him in !!Ardougne Castle??.", line++)
            line++
        }

        if (stage == 100) {
            line(player, "I confronted King Lathas and he revealed the truth to me.", line++, true)
            line(player, "The Plague is a hoax he created to keep people safe from", line++, true)
            line(player, "his brother, King Tyras, who was corrupted by the Dark", line++, true)
            line(player, "Lord on an expedition to the west. King Lathas rewarded", line++, true)
            line(player, "me and warned me to prepare for the challenges to come.", line++, true)
            line++
            line(player, "%%QUEST COMPLETE!&&", line)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var line = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.DISTILLATOR_420, 230)
        drawReward(player, "3 Quest Points", line++)
        drawReward(player, "1250 Thieving XP", line)
        rewardXP(player, Skills.THIEVING, 1250.0)
        removeAttributes(player, GameAttributes.FEED_ON_FENCE, GameAttributes.ELENA_REPLACE)
    }

    override fun newInstance(`object`: Any?): Quest = this
}
