package content.region.karamja.dialogue.shilovillage

import content.region.karamja.handlers.shilo.ShiloVillageListener
import core.api.anyInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class YanniSalikaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello there!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Greetings Bwana! My name is Yanni and I buy and sell",
                    "antiques.",
                ).also { stage++ }

            1 ->
                options(
                    "Tell me about this antiques business.",
                    "Is there anything else interesting to do around here?",
                    "Hmm, sorry, not interested.",
                ).also { stage++ }

            2 ->
                when (buttonId) {
                    1 -> player("Tell me about this antiques business.").also { stage++ }
                    2 -> player("Is there anything else interesting to do around", "here?").also { stage = 5 }
                    3 -> player("Hmm, sorry, not interested.").also { stage = 9 }
                }

            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I buy antiques and other interesting items. If you have any interesting items that you might want to sell me, please let me see them and I'll offer you a fair price.",
                ).also { stage++ }

            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Would you like me to have a look at your items and give you a quote?",
                ).also { stage = 7 }

            5 ->
                npc(FaceAnim.FRIENDLY, "You sound a bit bored of Shilo Village...", "what's wrong?").also {
                    stage = 100
                }

            6 ->
                npcl(FaceAnim.FRIENDLY, "Fair enough, come back if you change your mind.").also {
                    stage = END_DIALOGUE
                }

            7 ->
                options(
                    "Yes, please!",
                    "Maybe some other time?",
                    "Is there anything else interesting to do around here?",
                    "Hmm, sorry, not interested.",
                ).also { stage++ }

            8 ->
                when (buttonId) {
                    1 -> player("Yes, please!").also { stage = 10 }
                    2 -> player("Maybe some other time?").also { stage++ }
                    3 -> player("Is there anything else interesting to do around here?").also { stage = 5 }
                    4 -> player("Hmm, sorry, not interested.").also { stage = 14 }
                }

            9 -> npc(FaceAnim.FRIENDLY, "Sure thing. Have a nice day Bwana.").also { stage = END_DIALOGUE }
            10 -> npc("Great Bwana!").also { stage++ }
            11 -> {
                if (!anyInInventory(player, *ShiloVillageListener.ANTIQUE_ITEMS)) {
                    end()
                    npcl(FaceAnim.FRIENDLY, "Sorry Bwana, you have nothing I am interested in.")
                } else {
                    end()
                    npcl(
                        FaceAnim.FRIENDLY,
                        "And that's the only item I am interested in. If you want to sell me that item, simply show it to me.",
                    )
                }
            }

            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Those are the items I am interested in Bwana. If you want to sell me those items, simply show them to me.",
                ).also { stage++ }

            13 -> end()
            14 -> npc("Fair enough, come back if you change", "your mind.").also { stage = END_DIALOGUE }
            100 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Nothing. I was just wondering if there was anything else to do?",
                ).also { stage++ }

            101 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, if you're bored, you could do me a small favour and nip to see the jungle forester.",
                ).also { stage++ }

            102 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "I need a piece of red mahogany to resurface an",
                    "antique wardrobe.",
                ).also { stage++ }

            103 ->
                options(
                    "You want me to do you a favour?",
                    "An adventurer of my calibre, isn't that overkill.",
                    "What are you going to give me if I do it?",
                    "Nah thanks, I've got bigger fish to fry.",
                    "Okay, see you in a tick!",
                ).also { stage++ }

            104 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "You want me to do you a favour?").also { stage++ }
                    2 ->
                        playerl(FaceAnim.FRIENDLY, "An adventurer of my calibre, isn't that overkill.").also {
                            stage = 106
                        }

                    3 -> playerl(FaceAnim.FRIENDLY, "What are you going to give me if I do it?").also { stage = 107 }
                    4 -> playerl(FaceAnim.FRIENDLY, "Nah thanks, I've got bigger fish to fry.").also { stage = 108 }
                    5 -> playerl(FaceAnim.FRIENDLY, "Okay, see you in a tick!").also { stage = 109 }
                }

            105 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, if you've got nothing better to do... I mean, you looked bored.",
                ).also { stage = 103 }

            106 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Cooolll! Sorry your most amazingness, didn't realise you were too above it all to do someone a favour!",
                ).also { stage = 103 }

            107 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Give you? I don't think you heard me... I asked for a favour! It means that I'll 'owe you one' when you need something doing. That's what a favour is!",
                ).also { stage = 103 }

            108 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Fair enough! Hope it all goes well... but, you know, you looked bored so I just thought I'd suggest it.",
                ).also { stage = 103 }

            109 -> npcl(FaceAnim.FRIENDLY, "Great, nice of you to do it for me.").also { stage++ }
            110 -> options("I'll get going then!", "Where do I meet this jungle forester?").also { stage++ }
            111 ->
                when (buttonId) {
                    1 -> player("That'd be helpful!").also { stage = END_DIALOGUE }
                    2 -> player("Where do I meet this jungle forester?").also { stage++ }
                }

            112 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Oh, sorry, I thought you knew the area. You'll find a jungle forester in between the southern edge of Shilo Village and the Kharazi Jungle.",
                ).also { stage++ }

            113 -> npcl(FaceAnim.FRIENDLY, "They're always trying to find ways into that place.").also { stage++ }
            114 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I hear that some old timer from the Legends Guild keeps sending new recruits down there... only Zamorak knows why.",
                ).also { stage++ }

            115 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.YANNI_SALIKA_515)
    }
}
