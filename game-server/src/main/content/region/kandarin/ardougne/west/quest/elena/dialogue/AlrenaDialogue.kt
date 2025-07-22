package content.region.kandarin.ardougne.west.quest.elena.dialogue

import content.region.kandarin.ardougne.west.quest.elena.plugin.PlagueCityPlugin
import core.api.*
import core.api.getQuestStage
import core.api.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Alrena dialogue.
 *
 * # Relations
 * - [Plague City][content.region.kandarin.ardougne.west.quest.elena.PlagueCity]
 */
@Initializable
class AlrenaDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.PLAGUE_CITY) == 1) {
            playerl(FaceAnim.FRIENDLY, "Hello, Edmond has asked me to help find your daughter.")
        } else {
            playerl(FaceAnim.FRIENDLY, "Hello Madam.")
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {
            0 ->
                when (stage) {
                    START_DIALOGUE -> npcl(FaceAnim.NEUTRAL, "Oh, hello there.").also { stage = 2 }
                    2 -> playerl(FaceAnim.FRIENDLY, "Are you ok?").also { stage++ }
                    3 ->
                        npcl(FaceAnim.NEUTRAL, "Not too bad... I've just got some troubles on my mind...").also {
                            stage = END_DIALOGUE
                        }
                }

            1 ->
                when (stage) {
                    START_DIALOGUE ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Yes he told me. I've begun making your special gas mask, but I need some dwellberries to finish it.",
                        ).also { stage = 2 }

                    2 ->
                        if (inInventory(player!!, Items.DWELLBERRIES_2126)) {
                            playerl(FaceAnim.NEUTRAL, "Yes I've got some here.").also { stage++ }
                        } else {
                            npcl(
                                FaceAnim.FRIENDLY,
                                "The place to look is in McGrubor's Wood to the west of Seers village.",
                            ).also { stage = 8 }
                        }

                    3 -> {
                        sendItemDialogue(
                            player!!,
                            Items.DWELLBERRIES_2126,
                            "You give the dwellberries to Alrena.",
                        ).also { stage++ }
                        removeItem(player!!, Items.DWELLBERRIES_2126)
                    }

                    4 ->
                        sendDialogue(
                            player!!,
                            "Alrena crushes the berries into a smooth paste. She then smears the paste over a strange mask.",
                        ).also { stage++ }

                    5 ->
                        npc(
                            FaceAnim.FRIENDLY,
                            "There we go, all done. While in West Ardougne you must wear",
                            "this at all times, or you could catch the plague.",
                        ).also { stage++ }

                    6 -> sendItemDialogue(player!!, Items.GAS_MASK_1506, "Alrena gives you the mask.").also { stage++ }
                    7 -> {
                        end()
                        addItem(player!!, Items.GAS_MASK_1506)
                        setQuestStage(player!!, Quests.PLAGUE_CITY, 2)
                        setAttribute(player!!, PlagueCityPlugin.BUCKET_USES_ATTRIBUTE, 0)
                        sendNPCDialogue(
                            player!!,
                            NPCs.ALRENA_710,
                            "I'll make a spare mask. I'll hide it in the wardrobe in case the mourners come in.",
                        )
                    }

                    8 -> playerl(FaceAnim.NEUTRAL, "Ok, I'll go and get some.").also { stage = END_DIALOGUE }
                }

            2 ->
                when (stage) {
                    START_DIALOGUE ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Hello darling, I think Edmond had a good idea of how to get into West Ardougne, you should hear his idea.",
                        ).also { stage = 2 }

                    2 -> playerl(FaceAnim.FRIENDLY, "Alright I'll go and see him now.").also { stage = END_DIALOGUE }
                }

            3 ->
                when (stage) {
                    START_DIALOGUE -> {
                        if (getAttribute(player, PlagueCityPlugin.BUCKET_USES_ATTRIBUTE, 0) == 1) {
                            npcl(FaceAnim.FRIENDLY, "Hello darling, how's that tunnel coming along?").also { stage = 5 }
                        } else {
                            npcl(FaceAnim.FRIENDLY, "How's the tunnel going?").also { stage = 2 }
                        }
                    }

                    2 -> playerl(FaceAnim.FRIENDLY, "I'm getting there.").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "One of the mourners has been sniffing around asking questions about you and Edmond, you should keep an eye out for him.",
                        ).also { stage++ }

                    4 -> playerl(FaceAnim.FRIENDLY, "Ok, thanks for the warning.").also { stage = END_DIALOGUE }
                    5 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I just need to soften the soil a little more and then we'll start digging.",
                        ).also { stage = END_DIALOGUE }
                }

            4 ->
                when (stage) {
                    START_DIALOGUE ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Hi, have you managed to get through to West Ardougne?",
                        ).also { stage = 2 }

                    2 -> playerl(FaceAnim.FRIENDLY, "Not yet, but I should be going soon.").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Make sure you wear your mask while you're over there! I can't think of a worse way to die.",
                        ).also { stage++ }

                    4 -> playerl(FaceAnim.FRIENDLY, "Ok, thanks for the warning.").also { stage = END_DIALOGUE }
                }

            5 ->
                when (stage) {
                    START_DIALOGUE -> npcl(FaceAnim.FRIENDLY, "Hello, any word on Elena?").also { stage = 2 }
                    2 -> playerl(FaceAnim.FRIENDLY, "Not yet I'm afraid.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Is there anything else I can do to help?").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "It's alright, I'll get her back soon.").also { stage++ }
                    5 ->
                        if (inEquipment(player, Items.GAS_MASK_1506)) {
                            npcl(FaceAnim.FRIENDLY, "That's the spirit, dear.").also { stage = END_DIALOGUE }
                        } else {
                            npcl(
                                FaceAnim.FRIENDLY,
                                "That's the spirit, dear. Don't forget that there's a spare gas mask in the wardrobe if you need one.",
                            ).also { stage = END_DIALOGUE }
                        }
                }

            in 6..7 ->
                when (stage) {
                    START_DIALOGUE ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Hi, have you managed to get through to West Ardougne?",
                        ).also { stage = 2 }

                    2 -> playerl(FaceAnim.FRIENDLY, "Not yet, but I should be going soon.").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Make sure you wear your mask while you're over there! I can't think of a worse way to die.",
                        ).also { stage++ }

                    4 ->
                        if (inEquipment(player, Items.GAS_MASK_1506)) {
                            playerl(FaceAnim.FRIENDLY, "Okay, thanks for the warning.").also { stage = END_DIALOGUE }
                        } else {
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Don't forget, I've got a spare one hidden in the wardrobe if you need it.",
                            ).also { stage++ }
                        }

                    5 -> playerl(FaceAnim.FRIENDLY, "Great, thanks Alrena!").also { stage = END_DIALOGUE }
                }

            in 8..14 ->
                when (stage) {
                    START_DIALOGUE -> npcl(FaceAnim.FRIENDLY, "Hello, any word on Elena?").also { stage = 2 }
                    2 -> playerl(FaceAnim.FRIENDLY, "Not yet I'm afraid.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Is there anything else I can do to help?").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "Do you have a picture of Elena?").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Yes. There should be one in the house somewhere. Let me know if you need anything else.",
                        ).also { stage = END_DIALOGUE }
                }

            in 15..98 ->
                when (stage) {
                    START_DIALOGUE -> npcl(FaceAnim.FRIENDLY, "Hello, any word on Elena?").also { stage = 2 }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Not yet I'm afraid, I need to find some Snape grass first, any idea where I'd find some?",
                        ).also { stage++ }

                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "It's not common round here, though I hear it's easy to find by the coast south west of Falador.",
                        ).also { stage++ }

                    4 -> playerl(FaceAnim.FRIENDLY, "Thanks, I'll go take a look.").also { stage++ }
                    5 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I also need to get some chocolate powder for a hangover cure for the city warder.",
                        ).also { stage++ }

                    6 -> npcl(FaceAnim.FRIENDLY, "Well I don't have any chocolate, but this may help.").also { stage++ }
                    7 ->
                        sendItemDialogue(
                            player!!,
                            Items.PESTLE_AND_MORTAR_233,
                            "Alrena hands you a pestle and mortar.",
                        ).also { stage++ }

                    8 -> playerl(FaceAnim.FRIENDLY, "Thanks.").also { stage++ }
                    9 -> {
                        end()
                        addItem(player!!, Items.PESTLE_AND_MORTAR_233)
                    }
                }

            99 ->
                when (stage) {
                    START_DIALOGUE ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Thank you, thank you! Elena beat you back by minutes.",
                        ).also { stage = END_DIALOGUE }
                }

            100 ->
                when (stage) {
                    START_DIALOGUE ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Thank you for rescuing my daughter! Elena has told me of your bravery in entering a house that could have been plague infected. I can't thank you enough!",
                        ).also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ALRENA_710)
}
