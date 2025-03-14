package content.region.kandarin.miniquest.knightwave

import core.api.getAttribute
import core.api.quest.hasRequirement
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SquireDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        if (!hasRequirement(player, Quests.KINGS_RANSOM)) return true
        when {
            getAttribute(player, KnightWaveAttributes.KW_BEGIN, false) ->
                npc("Good day, my lord. Is there anything I can do", "for you?").also {
                    stage =
                        14
                }
            getAttribute(player, KnightWaveAttributes.KW_COMPLETE, false) ->
                npc(
                    "Congratulations on succeeding in the Knight Waves,",
                    "${if (!player.isMale) "my lady" else "my lord"}.",
                ).also {
                    stage =
                        29
                }
            else -> npc("Greetings, brave knight!")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.THINKING, "Hello. What's through that door?").also { stage++ }
            1 ->
                npc(
                    FaceAnim.HAPPY,
                    "That door leads to the training ground for the Knights",
                    "of the Round Table.",
                ).also {
                    stage++
                }
            2 -> playerl(FaceAnim.FRIENDLY, "Can I go in there?").also { stage++ }
            3 ->
                npc(
                    "Of course, my lord. But I must tell you the rules first.",
                    "Would you like to hear them now?",
                ).also {
                    stage++
                }
            4 -> options("Yes please.", "No, thank you.").also { stage++ }
            5 ->
                when (buttonId) {
                    1 -> player("Yes please.").also { stage++ }
                    2 -> player("No, thank you.").also { stage = END_DIALOGUE }
                }
            6 ->
                npc(
                    "The training ground is a melee combat area only.",
                    "Merlin has cast a spell over the grounds to prevent",
                    "anyone using Prayer, Ranged or Magic.",
                ).also {
                    stage++
                }
            7 ->
                npc(
                    "Upon entering the grounds, you will fight each of the",
                    "Round Table Knights in turn. Each knight is more",
                    "skilled than the last, out all have different attack styles",
                    "that can be exploited. There are eight knights in all, and",
                ).also {
                    stage++
                }
            8 ->
                npc(
                    "each will do damage not only to your hitpoints, but also",
                    "your combat stats. If you should die in the grounds",
                    "Merlin will teleport you back here. You will not lose",
                    "any of your items.",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    "If you feel you cannot complete the battle, you may",
                    "exit the training grounds. However, if you exit or die,",
                    "you will have to start over with the first knight the next",
                    "time you enter.",
                ).also {
                    stage++
                }
            10 ->
                npc(
                    "If you are a great warrior and defeat Sir Lancelot,",
                    "mightiest of all knights, you will receive great rewards.",
                    "Two new prayers will become available to you and your",
                    "respawn point will be set to Camelot, as well as 20,000",
                ).also {
                    stage++
                }
            11 -> npc("XP in to each of your combat stats.").also { stage++ }
            12 ->
                npc(
                    "If you do not want your respawn point set to Camelot,",
                    "Merlin can change it for you.",
                ).also { stage++ }
            13 ->
                npc("Would you like me to repeat any of that for you?").also {
                    setAttribute(player, KnightWaveAttributes.KW_BEGIN, true)
                    stage++
                }
            14 ->
                options(
                    "Tell me about the training ground.",
                    "Tell me about dying or leaving the grounds.",
                    "Tell me about the rewards.",
                    "No, thank you.",
                ).also {
                    stage++
                }
            15 ->
                when (buttonId) {
                    1 -> player("Tell me about the training ground.").also { stage++ }
                    2 -> player("Tell me about dying or leaving the grounds.").also { stage = 20 }
                    3 -> player("Tell me about the rewards.").also { stage = 26 }
                    4 -> player("No, thank you.").also { stage = END_DIALOGUE }
                }
            16 ->
                npc(
                    "Only melee combat is allowed within the training",
                    "grounds. Merlin will prevent the use of Prayer,",
                    "Ranged and Magic.",
                ).also {
                    stage++
                }
            17 -> player("Why?").also { stage++ }
            18 ->
                npc(
                    "These grounds are for only the best knights of the",
                    "land. It is not just testing your combat ability, but your",
                    "honour and your chivalry. You must face your enemy",
                    "head on, honourably and bravely.",
                ).also {
                    stage++
                }
            19 ->
                npc(
                    "Be watchful of your stats, as the Knights can do",
                    "damage to your combat stats as well as your hitpoints.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            20 ->
                npc(
                    "As these are only training grounds, we keep our knights",
                    "safe. If you should die, you will be teleported back",
                    " to here and all of your items will be safe.",
                ).also {
                    stage++
                }
            21 ->
                npc(
                    "However, leaving the training grounds, in any way, will",
                    "mean you will have to start from the beginning the next",
                    "time you enter. These grounds do not favour cowards.",
                ).also {
                    stage++
                }
            22 -> player("I have to start again? What do you mean?").also { stage++ }
            23 ->
                npc(
                    "You fight each knight of the Round Table, starting with",
                    "Sir Bedivere and finishing with Sir Lancelot. Leaving",
                    "means you will have to begin fighting Sir Bedivere again",
                    "and work your way back up.",
                ).also {
                    stage++
                }
            24 -> player("What if I log out during the fights?").also { stage++ }
            25 ->
                npc(
                    "If you log out during a fight, upon logging back in you",
                    "will fight the knight you were fighting when you",
                    "logged out.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            26 ->
                npc(
                    "If you are a great warrior and defeat Sir Lancelot,",
                    "mightiest of all knights, you will receive great rewards.",
                    "Two new prayers will become available to you and your",
                    "respawn point will be set to Camelot, as well as 20,000",
                ).also {
                    stage++
                }
            27 -> npc("XP in to each of your combat stats.").also { stage++ }
            28 ->
                npc("If you do not want your respawn point set to Camelot,", "Merlin can change it for you.").also {
                    stage =
                        END_DIALOGUE
                }
            29 -> player(FaceAnim.HALF_ASKING, "Thank you. Can I go back inside?").also { stage++ }
            30 ->
                npcl(FaceAnim.NEUTRAL, "I'm afraid not. Once you have won, you cannot re-enter.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SQUIRE_6169)
    }
}
