package content.region.kandarin.dialogue.piscatoris

import content.global.travel.RowingBoat
import core.api.*
import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class KathyCorkatDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!hasRequirement(player, Quests.SWAN_SONG)) {
            if (npc.id == NPCs.KATHY_CORKAT_3831) {
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'll be glad to get away from this place, dearie! Fancy a lift up the river?",
                ).also {
                    stage =
                        20
                }
            } else {
                npcl(FaceAnim.WORRIED, "Oh dear, oh dear, oh dear...")
            }
        } else {
            if (npc.id == NPCs.KATHY_CORKAT_3831) {
                npcl(FaceAnim.FRIENDLY, "Are ye wantin' a lift up the river, dearie?").also { stage = 23 }
            } else {
                npcl(FaceAnim.FRIENDLY, "Hello dearie. Heading up north to the Fishing Colony, are we?").also {
                    stage =
                        18
                }
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "Oh dear?").also { stage++ }
            1 -> npcl(FaceAnim.WORRIED, "It's terrible!").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "What's terrible?").also { stage++ }
            3 -> npcl(FaceAnim.HALF_ASKING, "Oh, never mind, dearie. What can I do for you?").also { stage++ }
            4 ->
                options(
                    "Can you take me downstream in your boat?",
                    "I don't think I want anything, thanks.",
                ).also { stage++ }

            5 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Can you take me downstream in your boat?").also { stage = 8 }
                    2 -> playerl(FaceAnim.NEUTRAL, "I don't think I want anything, thanks.").also { stage = 6 }
                }

            6 -> npcl(FaceAnim.FRIENDLY, "Everybody wants something, dearie!").also { stage++ }
            7 -> playerl(FaceAnim.FRIENDLY, "Huh? Oh, forget it!").also { stage = END_DIALOGUE }
            8 -> npcl(FaceAnim.FRIENDLY, "Ooh, it's dangerous up there at the moment!").also { stage++ }
            9 -> {
                if (!inEquipment(player, Items.RING_OF_CHAROSA_6465)) {
                    setTitle(player, 2)
                    sendDialogueOptions(
                        player!!,
                        "What would you like to say?",
                        "I'm not afraid - let's go!",
                        "Dangerous? Nah, forget it!",
                    )
                } else {
                    setTitle(player, 3)
                    sendDialogueOptions(
                        player!!,
                        "What would you like to say?",
                        "I'm not afraid - let's go!",
                        "Dangerous? Nah, forget it!",
                        "A lady this beautiful shouldn't be so afraid!",
                    )
                }
                stage++
            }

            10 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "I'm not afraid - let's go!").also { stage++ }
                    2 -> playerl(FaceAnim.THINKING, "Dangerous? Nah, forget it!").also { stage = 6 }
                    3 -> playerl(FaceAnim.FRIENDLY, "A lady this beautiful shouldn't be so afraid!").also { stage = 26 }
                }

            11 -> npcl(FaceAnim.FRIENDLY, "It's dangerous up there for me too, dearie.").also { stage++ }
            12 -> playerl(FaceAnim.FRIENDLY, "Would it help if I gave you some money?").also { stage++ }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ooh, that's better! I'll take you up north for 50 shiny coins.",
                ).also { stage++ }

            14 -> options("Alright, I'll pay.", "Not a chance!").also { stage++ }
            15 ->
                when (buttonId) {
                    1 ->
                        if (!removeItem(player, Item(Items.COINS_995, 50))) {
                            playerl(FaceAnim.NEUTRAL, "Sorry, I don't have enough coins for that.").also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "Alright, I'll pay.").also { stage = 17 }
                        }

                    2 -> playerl(FaceAnim.FRIENDLY, "Not a chance!").also { stage = 16 }
                }

            16 -> npcl(FaceAnim.FRIENDLY, "Then you can find your own way north, dearie!").also { stage = END_DIALOGUE }
            17 -> {
                end()
                RowingBoat.sail(player, NPC(NPCs.KATHY_CORKAT_3830))
            }

            18 -> options("Yes please.", "No thanks.").also { stage++ }
            19 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes please.").also { stage = 17 }
                    2 -> playerl(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
                }

            20 -> options("Yes please.", "No thanks.").also { stage++ }
            21 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes please.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
                }

            22 -> {
                end()
                RowingBoat.sail(player!!, NPC(NPCs.KATHY_CORKAT_3831))
            }

            23 -> {
                setTitle(player!!, 3)
                sendDialogueOptions(
                    player!!,
                    "What would you like to say?",
                    "Why don't you row right into the Colony?",
                    "Yes please.",
                    "No thanks.",
                ).also {
                    stage++
                }
            }

            24 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Why don't you row right into the Colony? It'd make it much easier to get in there.",
                        ).also {
                            stage++
                        }
                    2 -> playerl(FaceAnim.FRIENDLY, "Yes please.").also { stage = 22 }
                    3 -> playerl(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
                }

            25 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Oh, Franklin doesn't like it if I take my boat too close. He says I'll damage the nets and scare the fish. But were you wanting a lift up to the river?",
                ).also {
                    stage =
                        20
                }
            26 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Oooh, you shouldn't tease a poor girl so! But I'll take you downstream if you like!",
                ).also {
                    stage =
                        17
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return KathyCorkatDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KATHY_CORKAT_3830, NPCs.KATHY_CORKAT_3831)
    }
}
