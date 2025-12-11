package content.region.kandarin.yanille.quest.handsand

import com.google.gson.JsonObject
import content.data.GameAttributes
import core.ServerStore
import core.api.displayQuestItem
import core.api.hasLevelStat
import core.api.rewardXP
import core.api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Vars

/**
 * Represents the Hand in the Sand quest journal.
 */
@Initializable
class TheHandintheSand : Quest(Quests.THE_HAND_IN_THE_SAND, 72, 71, 1, Vars.VARBIT_QUEST_THE_HAND_IN_THE_SAND_PROGRESS_1527, 0, 1, 160) {

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11

        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Bert?? in !!Yanille?? in the", line++)
            line(player, "house near the !!Sandpit??.", line++)
            line(player, "Before I begin I will need to:", line++)
            line(player, "!!Have level 17 Thieving??.", line++, hasLevelStat(player, Skills.THIEVING, 17))
            line(player, "!!Have level 49 Crafting??.", line++, hasLevelStat(player, Skills.THIEVING, 49))
        }

        if (stage == 1) {
            line(player, "I should speak to the Guard Captain.", line++)
        }

        if (stage == 2) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I need to show the hand to the !!Wizards?? in !!Yanille??.", line++)
        }

        if (stage == 3) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
        }

        if (stage == 4) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
        }
        if (stage == 5) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
        }
        if (stage == 6) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
            line(player, "I have taken the scroll to Zavistic Rarve.", line++, true)
        }
        if (stage == 7) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
            line(player, "I have taken the scroll to Zavistic Rarve.", line++, true)
        }
        if (stage == 9) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
            line(player, "I have taken the scroll to Zavistic Rarve.", line++, true)
            line(player, "I have distracted Sandy successfully.", line++, true)
            line(player, "I have drugged Sandy's coffee.", line++, true)
        }
        if (stage == 10) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
            line(player, "I have taken the scroll to Zavistic Rarve.", line++, true)
            line(player, "I have distracted Sandy successfully.", line++, true)
            line(player, "I have drugged Sandy's coffee.", line++, true)
            line(player, "I have interrogated Sandy.", line++, true)
        }
        if (stage == 11) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
            line(player, "I have taken the scroll to Zavistic Rarve.", line++, true)
            line(player, "I have distracted Sandy successfully.", line++, true)
            line(player, "I have drugged Sandy's coffee.", line++, true)
            line(player, "I have interrogated Sandy.", line++, true)
            line(player, "I have returned the information from the orb.", line++, true)
        }

        if (stage == 12) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
            line(player, "I have taken the scroll to Zavistic Rarve.", line++, true)
            line(player, "I have distracted Sandy successfully.", line++, true)
            line(player, "I have drugged Sandy's coffee.", line++, true)
            line(player, "I have interrogated Sandy.", line++, true)
            line(player, "I have returned the information from the orb.", line++, true)
            line(player, "The Sandpit has been enchanted.", line++, true)
            line(player, "I have retrieved the head of a wizard.", line++)
        }
        if(stage == 13) {
            line(player, "I have spoken to the Guard Captain.", line++, true)
            line(player, "I have shown the hand to the Wizards in Yanille.", line++, true)
            line(player, "I have Bert's copy of the Rota.", line++, true)
            line(player, "I have Sandy's copy of the Rota.", line++, true)
            line(player, "I have taken the scroll to Zavistic Rarve.", line++, true)
            line(player, "I have distracted Sandy successfully.", line++, true)
            line(player, "I have drugged Sandy's coffee.", line++, true)
            line(player, "I have interrogated Sandy.", line++, true)
            line(player, "I have returned the information from the orb.", line++, true)
            line(player, "The Sandpit has been enchanted.", line++, true)
            line(player, "I have retrieved the head of a wizard.", line++, true)
            line(player, "The dead wizard has been buried and Sandy arrested for murder.", line++, true)
        }
        val store = getStoreFile()
        val username = player?.username?.lowercase() ?: ""
        val alreadyClaimed = store[username]?.asBoolean ?: false
        val lastClaim = player.getAttribute<Long>(GameAttributes.HAND_SAND_LAST_SAND_CLAIM) ?: 0L
        val now = System.currentTimeMillis()

        val cooldown = 24 * 60 * 60 * 1000L
        val timeLeft = (lastClaim + cooldown) - now

        if (stage == 100) {
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!", line++, false)
            line++
            line(player, "Every day I may ask !!Bert?? to transport !!some sand?? to my bank.", line++)
            if (alreadyClaimed) {
                line(player, "You've already collected your sand today. Come back tomorrow!", line)
            } else {
                if (timeLeft <= 0) {
                    line(player, "You can collect your sand now.", line)
                } else {
                    val hoursLeft = (timeLeft / (1000 * 60 * 60.0)).coerceAtLeast(0.0)
                    line(player, "You'll need to wait about ${"%.1f".format(hoursLeft)} hours to collect your sand.", line)
                }
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        displayQuestItem(player, Items.SANDY_HAND_6945)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "1000 Thieving XP", ln++)
        drawReward(player, "9000 Crafting XP", ln++)
        drawReward(player, "Secret reward from Bert", ln)
        rewardXP(player, Skills.THIEVING, 1000.0)
        rewardXP(player, Skills.CRAFTING, 9000.0)
        setVarbit(player, Vars.VARBIT_QUEST_THE_HAND_IN_THE_SAND_PROGRESS_1527, 160, true)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    companion object {
        /**
         * The bert secret reward.
         */
        @JvmStatic
        fun getStoreFile(): JsonObject = ServerStore.getArchive("daily-sand")
    }

}
//1 quest point
//1,000 Thieving experience
//9,000 Crafting experience
//Able to buy pink dye from Betty
//Bert will ship 84 buckets of sand to your bank if you talk to him after the quest. This number is increased to 120 after completing the elite Ardougne Tasks. This can be done once per day. Clicking on the quest's name will give you the time left before you can ask him again.
//2 Treasure Hunter keys (Ironman accounts will not receive these)
