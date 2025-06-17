package content.region.kandarin.yanille.quest.itwatchtower.dialogue

import content.data.GameAttributes
import core.api.addItemOrDrop
import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendItemDialogue
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Skavid dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.yanille.quest.itwatchtower.Watchtower]
 */
@Initializable
class SkavidDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val line = when (npc.id) {
            NPCs.SKAVID_865 -> "Cur bidith..."
            NPCs.SKAVID_867 -> "Bidith tanath..."
            NPCs.SKAVID_868 -> "Tanath gor..."
            NPCs.MAD_SKAVID_864 -> "Gor nod..."
            else -> "Gor cur..."
        }
        npc(FaceAnim.OLD_NEUTRAL, line)
        sendMessage(player, "The skavid is trying to communicate...")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val questStage = getQuestStage(player, Quests.WATCHTOWER)
        when (stage) {
            0 -> {
                if (getQuestStage(player, Quests.WATCHTOWER) < 25) {
                    player(" ???")
                    sendMessage(player, "You don't know any skavid words yet!")
                    stage = END_DIALOGUE
                    return true
                } else {
                    showTopics(
                        Topic("Cur.", if(questStage == 30 && npc.id == NPCs.SKAVID_867) 2 else 1),
                        Topic("Ar.",  if(questStage == 25 && npc.id == NPCs.SKAVID_866) 3 else 1),
                        Topic("Ig.",  if(questStage == 35 && npc.id == NPCs.SKAVID_865) 4 else 1),
                        Topic("Nod.", if(questStage == 40 && npc.id == NPCs.SKAVID_868) 5 else 1),
                        Topic("Gor.", if(questStage == 45 && npc.id == NPCs.MAD_SKAVID_864) 6 else 1)
                    )
                }
            }
            1 -> {
                player.incrementAttribute(GameAttributes.WATCHTOWER_SKAVID_UPSET)
                npc(FaceAnim.OLD_DEFAULT, " ???")
                sendMessage(player, "The response was wrong.")
                if(getAttribute(player, GameAttributes.WATCHTOWER_SKAVID_UPSET, 0) > 3) {
                    sendMessage(player, "It seems your response has upset the skavid.")
                }
                stage = END_DIALOGUE
            }
            2 -> {
                npc(FaceAnim.OLD_DEFAULT, "Cur, cur tanath!")
                sendMessage(player, "It seems the skavid understood you.")
                setQuestStage(player, Quests.WATCHTOWER, 30)
                stage = END_DIALOGUE
            }
            3 -> {
                npc(FaceAnim.OLD_DEFAULT, "Ar, ar cur!")
                sendMessage(player, "It seems the skavid understood you.")
                setQuestStage(player, Quests.WATCHTOWER, 35)
                stage = END_DIALOGUE
            }
            4 -> {
                npc(FaceAnim.OLD_DEFAULT, "Ig! Bidth ig!")
                sendMessage(player, "It seems the skavid understood you.")
                setQuestStage(player, Quests.WATCHTOWER, 40)
                stage = END_DIALOGUE
            }
            5 -> {
                npc(FaceAnim.OLD_DEFAULT, "Nod, gor nod!")
                sendMessage(player, "It seems the skavid understood you.")
                setQuestStage(player, Quests.WATCHTOWER, 45)
                stage = END_DIALOGUE
            }
            6 -> {
                npc(FaceAnim.OLD_DEFAULT, "Hehe! So you speak a little skavid, eh?")
                addItemOrDrop(player, Items.CRYSTAL_2381, 1)
                stage++
            }
            7 -> {
                npc(FaceAnim.OLD_DEFAULT, "I'm impressed. Here, take this prize. The others are", "with the shamans in the enclave - you'll never get them", "back.")
                stage++
            }
            8 -> {
                npc(FaceAnim.OLD_DEFAULT, "They hid one in the sacred rock - nothing can touch it", "now.")
                stage++
            }
            9 -> {
                sendItemDialogue(player, Items.CRYSTAL_2381, "The skavid gives you a large crystal.")
                setQuestStage(player, Quests.WATCHTOWER, 50)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MAD_SKAVID_864, NPCs.SKAVID_865, NPCs.SKAVID_866, NPCs.SKAVID_867)
}
