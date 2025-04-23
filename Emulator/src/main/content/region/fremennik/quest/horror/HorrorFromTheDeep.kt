package content.region.fremennik.quest.horror

import content.data.GameAttributes
import core.api.*
import core.game.component.CloseEvent
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class HorrorFromTheDeep : Quest(Quests.HORROR_FROM_THE_DEEP, 77, 76, 2, Vars.VARBIT_QUEST_HORROR_FROM_THE_DEEP_PROGRESS_34, 0, 1, 10) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        val progress = getVarbit(player, Vars.VARBIT_QUEST_HORROR_FROM_THE_DEEP_PROGRESS_34)
        val bridgeUnlock = getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_BRIDGE, 0)
        val godBooks = getAttribute(player, GameAttributes.GOD_BOOKS, false)
        var line = 11
        if (progress == 0) {
            line(player, "I can start this quest by speaking to !!Larrissa?? at the", line++)
            line(player, "!!Lighthouse?? to the !!North?? of the !!Barbarian Outpost??.", line++)
            line(player, "To complete this quest I need:", line++)
            line(player, if (hasLevelStat(player, Skills.AGILITY, 35)) "---Level 35 agility/--" else "!!Level 35 agility??", line++)
            line(player, if (hasLevelStat(player, Skills.MAGIC, 13)) { "---Level 13 or higher magic will be an advantage/--" } else { "!!Level 13 or higher magic will be an advantage??" }, line++,)
            line(player, "!!I must also be able to defeat strong level 100 enemies??", line++)
            limitScrolling(player, line, true)
            line++
        }
        if (progress == 1) {
            line(player, "I travelled to an isolated !!Lighthouse?? north of the !!Barbarian outpost??, ", line++, stage >= 2)
            line(player, "to find a !!Fremennik?? girl called !!Larrissa?? locked outside, ", line++, stage >= 2)
            line(player, "and worried about her boyfriend !!Jossik??.", line++, stage >= 2)
            line++
        }

        if (stage == 1) {
            line(player, "I need to !!repair the bridge?? leading to Rellekka.", line++, bridgeUnlock == 2)
            line(player, "I also need to get the lighthouse key from her cousin Gunnjorn.", line++, hasAnItem(player, Items.LIGHTHOUSE_KEY_3848).container != null || stage >= 31)
            line++
        }
        if (stage == 2) {
            line(player, "I recovered a !!spare key?? from Larrissa's cousin !!Gunnjorn??", line++, inInventory(player, Items.LIGHTHOUSE_KEY_3848))
            line(player, "and repaired the !!bridge?? to Rellekka with some planks.", line++, getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_BRIDGE, 0) == 2)
            line(player, "I also need to use the key I got from Gunnjorn", line++, true)
            line(player, "to enter the lighthouse.", line++, true)
            line++
        }
        if (stage == 20) {
            line(player, "I should talk to !!Larrissa??.", line++, true)
            line++
        }
        if (stage == 31) {
            line(player, "Now I need to find some way of fixing the lighthouse lamp.", line++, true)
            line++
        }
        if (stage == 40) {
            line(player, "I repaired the bridge leading to Rellekka and got a key from Gunnjorn", line++, true)
            line(player, "so I could enter the lighthouse.", line++, true)
            line(player, "I have re-tarred the lighthouse torch.", line++, true)
            line(player, "I have relit the lighthouse torch.", line++, true)
            line++
        }
        if (stage == 40 && getAttribute(player, GameAttributes.QUEST_HFTD_STRANGE_WALL_DISCOVER, false)) {
            line(player, "After I entered the !!lighthouse??, and repaired the !!lighting mechanism??,", line++, true)
            line(player, "I discovered a !!strange wall?? that blocked the entrance to an underground", line++, true)
            line(player, "cavern, where !!Jossik?? was.", line++, true)
            line++
        }
        if (stage == 50) {
            line(player, "I found Jossik in an underground cavern, behind a strange wall", line++, true)
            line(player, "where he had been attacked by some sea creatures.", line++, true)
            line(player, "I must defeat these sea monsters to save him!", line++, true)
            line++
        }
        if (stage == 60) {
            line(player, "After I killed some strange sea monsters, I managed", line++, true)
            line(player, "to get !!Jossik?? out of the cavern and back to the", line++, true)
            line(player, "lighthouse.", line++, true)
            line++
        }
        if (stage == 100) {
            line(player, "I repaired the bridge leading to Rellekka and got a key from Gunnjorn", line++, true)
            line(player, "so I could enter the lighthouse.", line++, true)
            line(player, "I managed to repair the lighthouse light with some molten glass,", line++, true)
            line(player, "some swamp tar and a tinderbox.", line++, true)

            line(player, "I found Jossik in an underground cavern, behind a strange wall", line++, true)
            line(player, "where he had been attacked by some sea creatures.", line++, true)
            line(player, "After I entered the lighthouse, and repaired the lighting mechanism,", line++, true)
            line(player, "I discovered a strange wall that blocked the entrance to an underground cavern,", line++, true)
            line(player, "where Jossik was.", line++, true)

            line(player, "After I killed some strange sea monsters,", line++, true)
            line(player, "I managed to get Jossik out of the cavern and back to the lighthouse.", line++, true)
            line(player, "I found a strange casket on the dead body of a sea monster,", line++, true)
            line(player, "which Jossik said he could tell me about.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line++, false)
            line++

            line(player, "I found a !!strange casket?? on the dead body of the !!sea monster??, ", line++, godBooks)
            line(player, "which !!Jossik?? said he could tell me about.", line++, godBooks)

            limitScrolling(player, line, false)
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        /*
         * Trivia: The only one interface that has different text in journal.
         */
        sendString(player, "You have survived the ${Quests.HORROR_FROM_THE_DEEP}!", Components.QUEST_COMPLETE_SCROLL_277, 4)
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.RUSTY_CASKET_3849)
        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "4662 XP in each of: Ranged,", ln++)
        drawReward(player, "Magic, Strength", ln)
        setVarbit(player, Vars.VARBIT_QUEST_HORROR_FROM_THE_DEEP_PROGRESS_34, 10, true)
        rewardXP(player, Skills.RANGE, 4662.0)
        rewardXP(player, Skills.MAGIC, 4662.0)
        rewardXP(player, Skills.STRENGTH, 4662.0)

        player.interfaceManager.getComponent(Components.QUEST_COMPLETE_SCROLL_277).closeEvent = CloseEvent { player, _ ->
            runTask(player, 1) {
                openDialogue(player, JossikLighthouseDialogue())
            }
            return@CloseEvent true
        }
    }

    override fun newInstance(`object`: Any?): Quest = this
}
