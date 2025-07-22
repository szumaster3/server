package content.region.misthalin.varrock.quest.firedup

import content.minigame.allfiredup.plugin.AFUBeacon
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests

/**
 * Represents the All Fired Up quest.
 */
@Initializable
class AllFiredUp : Quest(Quests.ALL_FIRED_UP, 157, 156, 1) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!King Roald?? in !!Varrock??", line++)
            line(player, "!!Palace??.", line++)
            line++
            line++
            line(player, "Minimum requirements:", line++)
            line(player, "I need level !!43 Firemaking?? to start the quest.", line++, getStatLevel(player, Skills.FIREMAKING) >= 43)
            line(player, "I need to have completed !!${Quests.PRIEST_IN_PERIL}??.", line++, isQuestComplete(player, Quests.PRIEST_IN_PERIL))
            limitScrolling(player, line, true)
        } else {
            line(player, "I have agreed to help King Roald test the beacon network", line++, true)
            line(player, "that he hopes will serve as an early warning system,", line++, true)
            line(player, "should Misthalin and Asgarnia come under attack from", line++, true)
            line(player, "Morytania or the Wilderness.", line++, true)

            if (stage > 10) {
                line(player, "I've spoken with the head fire-tender, Blaze Sharpeye", line++, true)
                line(player, "who is stationed near the Temple of Paterdomus, at the", line++, true)
                line(player, "source of the River Salve.", line++, true)
            } else if (stage == 10) {
                line(player, "!!King Roald?? asked me to talk to the head fire-tender !!Blaze??", line++, false)
                line(player, "!!Sharpeye?? who is stationed near the !!Temple of??", line++, false)
                line(player, "!!Paterdomus??, at the source of the !!River Salve??.", line++, false)
            }

            if (stage > 20) {
            } else if (stage == 20) {
                line(player, "!!Blaze?? has asked me to light the nearby !!beacon??.", line++, false)
                line(player, "To light the !!beacon??, I need to add !!twenty logs?? of the same", line++, false)
                line(player, "type and then light the fire with a !!tinderbox??.", line++, false)
            }

            if (stage > 30) {
                line(player, "I've lit the beacon near Blaze by loading it with twenty logs", line++, true)
                line(player, "and lighting them with a tinderbox.", line++, true)
            } else if (stage == 30) {
                line(player, "!!Blaze?? has asked me to light the nearby !!beacon??.", line++, false)
                line(player, "I've placed !!twenty logs?? on the !!beacon?? and lit the fire with", line++, false)
                line(player, "my !!tinderbox??. Now that it's blazing brightly, perhaps I", line++, false)
                line(player, "should speak with !!Blaze??.", line++, false)
            }

            if (stage > 40) {
            } else if (stage == 40) {
                line(player, "!!Blaze?? has now asked me to light the !!beacon?? tended by", line++, false)
                line(player, "!!Squire Fyre??, which is located just west of the !!Rag and??", line++, false)
                line(player, "!!Bone Man??'s hovel.", line++, false)
                line(player, "I need permission from !!Squire Fyre?? to light the !!beacon??", line++, false)
            }

            if (stage > 50) {
            } else if (stage == 50) {
                line(player, "!!Blaze?? has now asked me to light the !!beacon?? tended by", line++, false)
                line(player, "!!Squire Fyre??, which is located just west of the !!Rag and??", line++, false)
                line(player, "!!Bone Man??'s hovel.", line++, false)
                line(player, "To light the !!beacon??, I need to add !!twenty logs?? of the same", line++, false)
                line(player, "type and then light the fire with a !!tinderbox??.", line++, false)
            }

            if (stage > 60) {
                line(player, "Blaze has now asked me to light the beacon tended by", line++, true)
                line(player, "Squire Fyre, near the Rag and Bone Man's hovel. I've", line++, true)
                line(player, "loaded it with logs and set it alight; it's now blazing", line++, true)
                line(player, "brightly.", line++, true)
            } else if (stage == 60) {
                line(player, "!!Blaze?? has now asked me to light the !!beacon?? tended by", line++, false)
                line(player, "!!Squire Fyre??, which is located just west of the !!Rag and??", line++, false)
                line(player, "!!Bone Man??'s hovel.", line++, false)
                line(player, "I've placed twenty logs on the !!beacon?? and lit the fire with", line++, false)
                line(player, "my tinderbox. Now that it's blazing brightly, perhaps I", line++, false)
                line(player, "should speak with Blaze.", line++, false)
            }

            if (stage > 70) {
            } else if (stage == 70) {
                line(player, "!!Blaze?? has now asked me to maintain the nearby !!beacon??.", line++, false)
                line(player, "To maintain the !!beacon??, I need to add !!five logs?? of the same", line++, false)
                line(player, "type.", line++, false)
            }

            if (stage > 80) {
                line(player, "Blaze has explained how to maintain a beacon. When the", line++, true)
                line(player, "fire begins to die out, five more logs can be added to", line++, true)
                line(player, "restore a beacon to its blazing state. I've tended the", line++, true)
                line(player, "beacon near Blaze and have reported back to him.", line++, true)
            } else if (stage == 80) {
                line(player, "!!Blaze?? has now asked me to maintain the nearby !!beacon??.", line++, false)
                line(player, "To maintain the !!beacon??, I need to add !!five logs?? of the same", line++, false)
                line(player, "type.", line++, false)
                line(player, "I've placed five logs on the !!beacon?? to restore it to its", line++, false)
                line(player, "blazing state. Now that it's blazing brightly, perhaps I should", line++, false)
                line(player, "speak with Blaze.", line++, false)
            }

            if (stage > 90) {
            } else if (stage == 90) {
                line(player, "I need to talk to !!King Roald?? in !!Varrock Palace?? to claim my", line++, false)
                line(player, "reward.", line++, false)
            }

            if (stage == 100) {
                line(player, "I spoke to King Roald who thanked me for helping and", line++, true)
                line(player, "rewarded me with 20,000gp and 5,500 Firemaking XP. He", line++, true)
                line(player, "told me that if I'm able to light six, ten and all fourteen", line++, true)
                line(player, "fires simultaneously, he'll have further rewards for me.", line++, true)
                line++
                line++
                line(player, "<col=FF0000>QUEST COMPLETE!", line)
            }
            limitScrolling(player, line, true)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        sendString(player, "20,000gp,", 277, 8 + 2)
        sendString(player, "5,500 Firemaking XP", 277, 9 + 2)
        sendString(player, "Full access to the beacon network", 277, 10 + 2)
        sendString(player, "1 Quest Point", 277, 11 + 2)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.TINDERBOX_590, 235)
        rewardXP(player, Skills.FIREMAKING, 5500.0)
        if (freeSlots(player) == 0) {
            addItemOrBank(player, Items.COINS_995, 20000)
        } else {
            addItem(player, Items.COINS_995, 20000)
        }
        AFUBeacon.resetAllBeacons(player)
        setVarbit(player, 1283, 0)
        updateQuestTab(player)
    }

    override fun getConfig(player: Player?, stage: Int): IntArray {
        if (stage == 100) return intArrayOf(1282, 90)
        if (stage > 0) {
            return intArrayOf(1282, 1)
        } else {
            return intArrayOf(1282, 0)
        }
    }

    override fun newInstance(`object`: Any?): Quest = this
}
