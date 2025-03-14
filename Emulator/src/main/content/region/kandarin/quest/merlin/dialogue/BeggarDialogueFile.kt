package content.region.kandarin.quest.merlin.dialogue

import content.region.kandarin.quest.merlin.handlers.BeggarNPC
import content.region.kandarin.quest.merlin.handlers.MerlinUtils
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.global.action.DoorActionHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class BeggarDialogueFile(
    val door: Scenery?,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.BEGGAR_252)

        when (stage) {
            0 -> {
                if (player!!.getAttribute(MerlinUtils.ATTR_STATE_TALK_BEGGAR, false) == true) {
                    npcl(FaceAnim.NEUTRAL, "Have you got any bread for me yet?")
                    stage = 30
                } else {
                    npcl(FaceAnim.SAD, "Please kind sir... my family and I are starving...").also { stage++ }
                }
            }

            1 ->
                npcl(
                    FaceAnim.SAD,
                    "Could you find it in your heart to spare me a simple loaf of bread?",
                ).also { stage++ }

            2 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "Yes certainly.", 3),
                    Topic(FaceAnim.NEUTRAL, "No I don't have any bread with me.", 20),
                )

            3 -> {
                player!!.setAttribute(MerlinUtils.ATTR_STATE_TALK_BEGGAR, true)

                if (player!!.inventory.contains(Items.BREAD_2309, 1)) {
                    sendDialogue(player!!, "You give the bread to the beggar.")
                    stage++
                } else {
                    playerl(FaceAnim.NEUTRAL, "... except I don't have any bread on me at the moment...")
                    stage = END_DIALOGUE
                }
            }

            4 -> {
                player!!.inventory.remove(Item(Items.BREAD_2309, 1))
                npcl(FaceAnim.HAPPY, "Thank you very much!")
                stage++
            }

            5 -> {
                transformToLady(player!!)
                sendDialogue(player!!, "The beggar has turned into the Lady of the Lake!")
                stage++
            }

            6 -> {
                npc = NPC(NPCs.THE_LADY_OF_THE_LAKE_250)
                npcl(FaceAnim.NEUTRAL, "Well done. You have passed my test.")
                stage++
            }

            7 -> {
                npc = NPC(NPCs.THE_LADY_OF_THE_LAKE_250)
                npcl(FaceAnim.NEUTRAL, "Here is Excalibur, guard it well.")
                stage++
            }

            8 -> {
                player!!.setAttribute(MerlinUtils.ATTR_STATE_CLAIM_EXCALIBUR, true)

                disappearLady(player!!)
                player!!.inventory.add(Item(Items.EXCALIBUR_35, 1))
                end()
                stage = END_DIALOGUE
            }

            20 -> {
                npcl(FaceAnim.NEUTRAL, "Well, if you get some, you know where to come.").also { stage++ }
            }

            21 -> {
                player!!.setAttribute(MerlinUtils.ATTR_STATE_TALK_BEGGAR, true)
                DoorActionHandler.handleAutowalkDoor(player!!, door!!, Location.create(3015, door!!.location.y, 0))
                end()
                stage = END_DIALOGUE
            }

            30 -> {
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "Yes, here you go.", 4),
                    Topic(FaceAnim.NEUTRAL, "No, I still have none.", END_DIALOGUE),
                )
            }
        }
    }

    private fun disappearLady(player: Player) {
        val lady = player.getAttribute<NPC>(MerlinUtils.TEMP_ATTR_LADY, null)
        lady.clear()
        player.removeAttribute(MerlinUtils.TEMP_ATTR_LADY)
    }

    private fun transformToLady(player: Player) {
        val beggar = player.getAttribute<NPC>(MerlinUtils.TEMP_ATTR_BEGGAR, null)
        beggar.transform(NPCs.THE_LADY_OF_THE_LAKE_250)
        beggar.face(player)

        player.removeAttribute(MerlinUtils.TEMP_ATTR_BEGGAR)
        player.setAttribute(MerlinUtils.TEMP_ATTR_LADY, beggar)
    }

    fun initBeggar(player: Player) {
        val beggar = BeggarNPC(NPCs.BEGGAR_252, Location.create(3016, 3247, 0))
        player.setAttribute(MerlinUtils.TEMP_ATTR_BEGGAR, beggar)
        beggar.player = player
        beggar.lock()
        beggar.face(player)
        beggar.init()
    }
}
