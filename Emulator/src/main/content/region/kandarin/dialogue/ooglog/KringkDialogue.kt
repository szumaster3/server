package content.region.kandarin.dialogue.ooglog

import core.api.*
import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class KringkDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        npc = NPC(NPCs.KRINGK_7058)
        when (stage) {
            START_DIALOGUE -> {
                if (!hasRequirement(player, Quests.AS_A_FIRST_RESORT)) {
                    playerl(FaceAnim.FRIENDLY, "What's going on here?").also { stage++ }
                } else {
                    npcl(
                        FaceAnim.CHILD_NORMAL,
                        "I would offer haircut, but it hard to do style for puny human with puny head. Me know. You want wig intead? Me give you big-big discount!",
                    ).also {
                        stage =
                            5
                    }
                }
            }

            1 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Me very busy. No have time to talk to puny creature like you.",
                ).also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "Well, excuse me!").also { stage++ }
            3 -> npcl(FaceAnim.CHILD_NORMAL, "No excuse for you - you in my way.").also { stage++ }
            4 -> end()
            5 -> {
                setTitle(player, 2)
                sendDialogueOptions(
                    player,
                    "Would you like to buy an ogre wig for 50 gp?",
                    "Yes, please.",
                    "No, thank you.",
                ).also {
                    stage++
                }
            }
            6 ->
                when (buttonId) {
                    1 -> player("Yes, please.").also { stage++ }
                    2 -> player("No, thank you.").also { stage = 4 }
                }
            7 -> {
                if (freeSlots(player) == 0) {
                    npcl(
                        FaceAnim.CHILD_NORMAL,
                        "You no have enough room to hold it, human. Come back when you have space.",
                    ).also {
                        stage =
                            4
                    }
                    return true
                }
                if (!removeItem(player, Item(Items.COINS_995, 50))) {
                    npcl(FaceAnim.CHILD_NORMAL, "You no have enough shiny pretties, human.").also { stage = 4 }
                } else {
                    npcl(
                        FaceAnim.CHILD_NORMAL,
                        "There you go. Nice wig for you, made from de freshest wolfsie bones.",
                    ).also {
                        stage =
                            8
                    }
                }
            }
            8 -> {
                end()
                addItemOrDrop(player, Items.OGRE_WIG_12559)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return KringkDialogue(player)
    }

    override fun getIds(): IntArray = intArrayOf(7099)
}
