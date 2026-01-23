package content.global.activity.champions.dialogue

import content.data.GameAttributes
import content.global.activity.champions.plugin.ChampionDefinition
import content.global.activity.champions.plugin.ChampionScrollsDropHandler
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

/**
 * Represents the dialogue plugin used for the Larxus NPC.
 */
@Initializable
class LarxusDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        npc = NPC(NPCs.LARXUS_3050)
        val defeatAll = getAttribute(player, GameAttributes.ACTIVITY_CHAMPIONS_CHALLENGE_DEFEAT_ALL, false)
        val activityComplete = getAttribute(player, GameAttributes.ACTIVITY_CHAMPIONS_COMPLETE, false)
        if(defeatAll && !activityComplete) {
            npc(FaceAnim.NEUTRAL, "Leon D'Cour has issued you a challenge, he has stated", "there will be no items allowed expect those you're", "wearing. Do you want to accept the challenge?")
            stage = 4
            return true
        }
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.NEUTRAL, "Is there something I can help you with?").also { stage++ }
            1 -> showTopics(
                IfTopic(FaceAnim.HALF_ASKING,"I've defeated all the champions, what now?", 5, activityComplete),
                IfTopic(FaceAnim.HALF_ASKING,"I was given a challenge, what now?", 2, hasScroll(player) && !activityComplete),
                Topic(FaceAnim.HALF_ASKING,"What is this place?", 3),
                Topic(FaceAnim.NEUTRAL,"Nothing thanks.",END_DIALOGUE)
            )
            2 -> npcl(FaceAnim.NEUTRAL, "Well pass it here and we'll get you started.").also { stage = END_DIALOGUE }
            3 -> npcl(FaceAnim.NEUTRAL, "This is the champions' arena, the champions of various races use it to duel those they deem worthy of the honour.").also { stage = END_DIALOGUE }
            4 -> {
                end()
                openDialogue(player, LarxusDialogueFile(false))
            }
            5 -> npc(FaceAnim.NEUTRAL, "Well keep a watch out, more champions may rise to test", "your mettle in the future.").also { stage = END_DIALOGUE }
        }
        return true
    }

    private fun hasScroll(player: Player?) = ChampionScrollsDropHandler.SCROLLS.any { player?.inventory?.getItem(it.asItem()) != null }

    override fun getIds(): IntArray = intArrayOf(NPCs.LARXUS_3050)
}

/**
 * Handles dialogue for starting the champions challenge.
 */
class LarxusDialogueFile(private val challengeStart: Boolean = false, private val scrollItem: Item? = null) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.LARXUS_3050)
        if (!challengeStart || scrollItem == null) return

        val scrollId = scrollItem.id
        val entry = ChampionDefinition.fromScroll(scrollId)

        when (stage) {
            0 -> {
                val npc = findNPC(NPCs.LARXUS_3050) ?: return
                val player = player ?: return

                face(npc, player, 2)

                entry?.varbitId?.let { varbitId ->
                    if (getVarbit(player, varbitId) == 1) {
                        end()
                        removeItem(player, scrollId)
                        npc(FaceAnim.NEUTRAL, "You've already defeated this Champion, the challenge is", "void.")
                        return
                    }
                }

                val prefix = "So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're"
                val question = "Do you still want to proceed?"

                val scrollMessages = mapOf(
                    Items.CHAMPION_SCROLL_6798 to "$prefix not allowed to use any Prayer's. $question",     // Earth Warrior
                    Items.CHAMPION_SCROLL_6799 to "$prefix only allowed to take Weapons, no other items are allowed. $question", // Ghoul
                    Items.CHAMPION_SCROLL_6800 to "$prefix only allowed to use Melee attacks, no Ranged or Magic. $question", // Giant
                    Items.CHAMPION_SCROLL_6801 to "$prefix only allowed to use Magic attacks, no Melee or Ranged. $question", // Goblin
                    Items.CHAMPION_SCROLL_6802 to "$prefix not allowed to use any Melee attacks. $question", // Hobgoblin
                    Items.CHAMPION_SCROLL_6803 to "$prefix not allowed to use any Special Attacks. $question", // Imp
                    Items.CHAMPION_SCROLL_6804 to "$prefix not allowed to use any Ranged attacks. $question", // Jogre
                    Items.CHAMPION_SCROLL_6805 to "$prefix allowed to use any Weapons or Armour. $question", // Lesser Demon
                    Items.CHAMPION_SCROLL_6806 to "$prefix only allowed to use Ranged attacks, no Melee or Magic. $question", // Skeleton
                    Items.CHAMPION_SCROLL_6807 to "$prefix not allowed to use any Magic attacks. $question" // Zombie
                )
                scrollMessages[scrollId]?.let { message ->
                    npcl(FaceAnim.NEUTRAL, message)
                }
                stage = 1
            }
            1 -> showTopics(
                Topic(FaceAnim.FRIENDLY,"Yes, let me at him!", 2),
                Topic(FaceAnim.NEUTRAL,"No, thanks I'll pass.", END_DIALOGUE)
            )
            2 -> {
                npcl(FaceAnim.NEUTRAL, "Your challenger is ready, please go down through the trapdoor when you're ready.")
                val trapdoorLoc = getScenery(Location.create(3184, 9758, 0))
                replaceScenery(trapdoorLoc!!.asScenery(), Scenery.CHAMPION_STATUE_10557, 100)
                scrollItem.let { item ->
                    val usedScroll = player!!.inventory.getItem(item)
                    usedScroll?.let { setCharge(it, it.id) }
                }
                stage = END_DIALOGUE
            }
        }
    }
}