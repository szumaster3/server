package content.global.activity.champion.dialogue

import content.data.GameAttributes
import content.global.activity.champion.plugin.ChampionDefinition
import content.global.activity.champion.plugin.ChampionScrollsDropHandler
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the dialogue plugin used for the Larxus NPC.
 */
@Initializable
class LarxusDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        npc = NPC(NPCs.LARXUS_3050)

        val defeatAll = getAttribute(player, GameAttributes.ACTIVITY_CHAMPIONS_CHALLENGE_DEFEAT_ALL, false)

        showTopics(
            IfTopic(
                "Leon D'Cour has issued you a challenge, he has stated there will be no items allowed except those you're wearing. Do you want to accept the challenge?",
                5,
                showCondition = defeatAll,
                speaker = npc
            )
        )

        when (stage) {
            START_DIALOGUE -> {
                face(findNPC(NPCs.LARXUS_3050)!!, player!!, 1)
                npcl(FaceAnim.NEUTRAL, "Is there something I can help you with?")
                stage++
            }

            1 -> {
                val hasScroll = anyInInventory(player, *ChampionScrollsDropHandler.SCROLLS)
                if (hasScroll) {
                    options("I was given a challenge, what now?", "What is this place?", "Nothing thanks.")
                    stage = 2
                } else {
                    options("What is this place?", "Nothing thanks.")
                    stage = 6
                }
            }

            2 -> when (buttonId) {
                1 -> playerl(FaceAnim.HALF_ASKING, "I was given a challenge, what now?").also { stage = 3 }
                2 -> playerl(FaceAnim.HALF_ASKING, "What is this place?").also { stage = 4 }
                3 -> playerl(FaceAnim.HALF_ASKING, "Nothing thanks.").also { stage = END_DIALOGUE }
            }

            3 -> npcl(FaceAnim.NEUTRAL, "Well pass it here and we'll get you started.").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.NEUTRAL, "This is the champions' arena, the champions of various races use it to duel those they deem worthy of the honour.").also { stage = END_DIALOGUE }
            5 -> {
                end()
                openDialogue(player, LarxusDialogueFile())
            }
            6 -> when (buttonId) {
                1 -> playerl(FaceAnim.HALF_ASKING, "What is this place?").also { stage = 4 }
                2 -> playerl(FaceAnim.HALF_ASKING, "Nothing thanks.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.LARXUS_3050)
}

/**
 * Handles dialogue for starting the challenge or showing scroll info.
 */
class LarxusDialogueFile(private val challengeStart: Boolean = false) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.LARXUS_3050)
        val scrollId = ChampionScrollsDropHandler.SCROLLS.firstOrNull { inInventory(player!!, it) }
        val entry = scrollId?.let { ChampionDefinition.fromScroll(it) }
        if (!challengeStart) return

        when (stage) {
            0 -> {
                val prefix = "So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For"

                if (entry?.varbitId != null && getVarbit(player!!, entry.varbitId) == 1) {
                    removeItem(player!!, scrollId)
                    npc("You've already defeated this Champion, the challenge is", "void.")
                    stage = END_DIALOGUE
                    return
                }

                val scrollMessage = when (scrollId) {
                    Items.CHAMPION_SCROLL_6798 -> "$prefix this fight you're not allowed to use any Prayer's. Do you still want to proceed?"
                    Items.CHAMPION_SCROLL_6799 -> "$prefix this fight you're only allowed to take Weapons, no other items are allowed. Do you still want to proceed?"
                    Items.CHAMPION_SCROLL_6800 -> "$prefix this fight you're only allowed to use Melee attacks, no Ranged or Magic. Do you still want to proceed?"
                    Items.CHAMPION_SCROLL_6801 -> "$prefix this fight you're only allowed to use Magic attacks, no Melee or Ranged. Do you still want to proceed?"
                    Items.CHAMPION_SCROLL_6802 -> "$prefix this fight you're not allowed to use any Melee attacks. Do you still want to proceed?"
                    Items.CHAMPION_SCROLL_6803 -> "$prefix this fight you're not allowed to use any Special Attacks. Do you still want to proceed?"
                    Items.CHAMPION_SCROLL_6804 -> "$prefix this fight you're not allowed to use any Ranged attacks. Do you still want to proceed?"
                    Items.CHAMPION_SCROLL_6805 -> "$prefix this fight you're allowed to use any Weapons or Armour. Do you still want to proceed?"
                    Items.CHAMPION_SCROLL_6806 -> "$prefix this fight you're only allowed to use Ranged attacks, no Melee or Magic. Do you still want to proceed?"
                    Items.CHAMPION_SCROLL_6807 -> "$prefix this fight you're not allowed to use any Magic attacks. Do you still want to proceed?"
                    else -> null
                }

                scrollMessage?.let { npcl(it) }
                stage = if (scrollMessage != null) 1 else 2

                if (scrollMessage == null && getAttribute(player!!, GameAttributes.ACTIVITY_CHAMPIONS_CHALLENGE_DEFEAT_ALL, false)) {
                    options("Yes, let me at him!", "No, thanks I'll pass.")
                    stage = 2
                } else if (scrollMessage == null) {
                    end()
                    sendMessage(player!!, "Nothing interesting happens.")
                }
            }

            1 -> options("Yes, let me at him!", "No, thanks I'll pass.").also { stage = 2 }
            2 -> when (buttonID) {
                1 -> playerl("Yes, let me at him!").also { stage = 3 }
                2 -> playerl("No, thanks I'll pass.").also { stage = END_DIALOGUE }
            }
            3 -> npcl(FaceAnim.HAPPY, "Your challenger is ready, please go down through the trapdoor when you're ready.").also { stage = 4 }
            4 -> {
                end()
                setAttribute(player!!, GameAttributes.ACTIVITY_CHAMPION_CHALLENGE, entry?.scrollId)
            }
        }
    }
}
