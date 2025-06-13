package content.minigame.allfiredup.dialogue

import content.minigame.allfiredup.plugin.AFUBeacon
import content.minigame.allfiredup.plugin.AFUSession
import core.api.getStatLevel
import core.api.sendDialogueOptions
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

private val VALID_LOGS =
    arrayOf(
        Items.LOGS_1511,
        Items.OAK_LOGS_1521,
        Items.WILLOW_LOGS_1519,
        Items.MAPLE_LOGS_1517,
        Items.YEW_LOGS_1515,
        Items.MAGIC_LOGS_1513,
    )

@Initializable
class BeaconKeeperDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var index = 0

    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)

        index = getIndexOf((args[0] as NPC).originalId, listOf(npc.id))
        val faceExpression =
            when (npc.id) {
                NPCs.STUBTHUMB_8054, NPCs.CRATE_8059, NPCs.NANUQ_8063 -> FaceAnim.OLD_ANGRY2
                NPCs.DORONBOL_8057 -> FaceAnim.CHILD_NEUTRAL
                else -> FaceAnim.ANNOYED
            }
        if (index == AFUBeacon.GWD.ordinal && getStatLevel(player, Skills.SUMMONING) < 81) {
            npc(faceExpression, "Awwf uurrrhur", "(You need 81 Summoning to communicate with Nanuq.)").also {
                stage = 15
            }
            return true
        }
        if (index == AFUBeacon.MONASTERY.ordinal && getStatLevel(player, Skills.PRAYER) < 53) {
            npc(
                faceExpression,
                "I will aid you when your devotion is",
                "strong enough.",
                "(You need 53 Prayer for him to assist you.)",
            ).also { stage = 15 }
            return true
        }
        npc(faceExpression, "Hello, adventurer.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val beacon = AFUBeacon.values()[index]
        val logs = getLogs(player, 5)
        val session: AFUSession? = player.getAttribute("afu-session", null)
        val faceExpression =
            if (npc.id in
                intArrayOf(
                    NPCs.STUBTHUMB_8054,
                    NPCs.CRATE_8059,
                    NPCs.NANUQ_8063,
                )
            ) {
                FaceAnim.OLD_NORMAL
            } else {
                FaceAnim.HALF_GUILTY
            }
        when (stage) {
            0 ->
                if (!GameWorld.settings!!.isMembers) {
                    end()
                    sendMessage(player, "You need to be on a members' world to access this content.")
                } else {
                    player("Hello!").also { stage++ }
                }

            1 ->
                if (beacon.getState(player) == BeaconState.LIT && session?.isWatched(index) == false) {
                    npc(faceExpression, "Hi. What you want?").also { stage++ }
                } else {
                    npc(faceExpression, "I'm busy here. No time to chat.").also { stage = 15 }
                }

            2 -> options("Could you look after this beacon for me?", "Nevermind.").also { stage = 10 }
            10 ->
                when (buttonId) {
                    1 -> player("Can you watch this beacon for me?").also { stage++ }
                    2 -> player("Nevermind.").also { stage = END_DIALOGUE }
                }

            11 -> npcl(faceExpression, "Certainly, adventurer. Do you have logs for me?").also { stage++ }
            12 ->
                sendDialogueOptions(
                    player,
                    "Do you want to give five logs to the fire tender?",
                    "Yes.",
                    "No.",
                ).also { stage++ }

            13 ->
                when (buttonId) {
                    1 -> {
                        if (logs.id != 0) {
                            player("Here are five logs for you.").also {
                                player.inventory.remove(logs)
                                session?.setWatcher(index, logs)
                                stage++
                            }
                        } else {
                            npc(
                                faceExpression,
                                "How you expect me to tend fire for you? You not have enough good logs for me to use!",
                            ).also { stage = END_DIALOGUE }
                        }
                    }

                    2 -> end()
                }

            14 ->
                npcl(
                    faceExpression,
                    "Thanks. I use these five logs to keep beacon burning whilst you away.",
                ).also { stage = END_DIALOGUE }

            15 -> player("You don't look busy to me.").also { stage++ }
            16 -> npc(faceExpression, "How little you understand.").also { stage++ }
            17 -> player("Exactly what I don't understand, then?").also { stage++ }
            18 -> npc(faceExpression, "It take too long to explain.").also { stage++ }
            19 -> player("Well, I've got nothing urgent I need to do - please", "explain.").also { stage++ }
            20 -> npc(faceExpression, "It not worth it. You annoy me, be off with you!").also { stage++ }
            21 -> player("Right, I'll see you later then, shall I?").also { stage++ }
            22 -> npc(faceExpression, "Begone!").also { stage++ }
            23 -> player("Bye!").also { stage++ }
            24 -> npc(faceExpression, "BEGONE!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = BeaconKeeperDialogue(player)

    fun getIndexOf(
        id: Int,
        ids: List<Int>,
    ): Int {
        if (id == NPCs.NANUQ_8065) return 0
        if (id == NPCs.NANUQ_8066) return 1
        for (index in ids.indices) {
            if (ids[index] == id) return index + 2
        }
        return -1
    }

    fun getLogs(
        player: Player,
        amount: Int,
    ): Item {
        var logId = 0
        for (log in VALID_LOGS) {
            if (player.inventory.getAmount(log) >= amount) {
                logId = log
                break
            }
        }
        return Item(logId, amount)
    }

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.NANUQ_8067,
            NPCs.NANUQ_8068,
            NPCs.NANUQ_8069,
            NPCs.NANUQ_8070,
            NPCs.NANUQ_8071,
            NPCs.NANUQ_8072,
            NPCs.NANUQ_8073,
            NPCs.NANUQ_8074,
            NPCs.NANUQ_8075,
            NPCs.NANUQ_8076,
        )
}
