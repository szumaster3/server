package content.region.kandarin.yanille.quest.itwatchtower.dialogue

import core.api.getQuestStage
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Gorad dialogue.
 *
 * # Relations
 * - [Watchtower Quest][content.region.kandarin.yanille.quest.itwatchtower.Watchtower]
 */
@Initializable
class GoradDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val questStage = getQuestStage(player, Quests.WATCHTOWER)

        if (questStage == 100) {
            sendMessage(player, "Gorad is busy; try again later.")
            return true
        }

        if (questStage >= 2) {
            player("I've come to knock your teeth out!")
            stage = 5
        } else {
            player("Hello!")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        when (stage) {
            0 -> player("I seek another task.").also { stage++ }
            1 -> npcl(FaceAnim.OLD_CALM_TALK2, "You know who I is?").also { stage++ }
            2 -> options("A big, ugly brown creature.", "I don't know who you are..").also { stage++ }
            3 -> when (buttonId) {
                1 -> player("A big, ugly brown creature.").also { stage = 6 }
                2 -> player("I don't know who you are.").also { stage++ }
            }
            4 -> npcl(FaceAnim.OLD_NORMAL, "I am Gorad, and you are tiny. Go now and I won't chase you!").also { stage = END_DIALOGUE }
            5 -> npc(FaceAnim.OLD_DEFAULT, "You shut your face! I smack you till you dead and", "sorry!").also { stage = 8 }
            6 -> npc(FaceAnim.OLD_DEFAULT,"The impudence! take that...").also { stage++ }
            7 -> {
                player.impactHandler.manualHit(npc, 1, ImpactHandler.HitsplatType.NORMAL)
                sendMessage(player, "The ogre punched you hard in the face!")
                stage = 8
            }
            8 -> {
                end()
                sendMessage(player, "You are under attack!")
                npc.attack(player)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GORAD_856)
}