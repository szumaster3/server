package content.region.fremennik.quest.viking.dialogue

import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.isQuestComplete
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
class SkulgrimenDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (inInventory(player, Items.UNUSUAL_FISH_3703, 1)) {
            playerl(FaceAnim.HAPPY, "Hi there. I got your fish, so can I have that bowstring for Sigli now?")
            stage = 20
            return true
        } else if (inInventory(player, Items.CUSTOM_BOW_STRING_3702, 1)) {
            playerl(FaceAnim.ASKING, "So about this bowstring... was it hard to make or something?")
            stage = 25
            return true
        } else if (getAttribute(player, "sigmundreturning", false)) {
            playerl(FaceAnim.ASKING, "Is this trade item for you?")
            stage = 26
            return true
        }
        if (getAttribute(player, "sigmund-steps", 0) == 7) {
            playerl(
                FaceAnim.ASKING,
                "I don't suppose you have any idea where I could find an exotic and extremely odd fish, do you?",
            )
            stage = 15
            return true
        } else if (getAttribute(player, "sigmund-steps", 0) == 6) {
            playerl(
                FaceAnim.ASKING,
                "I don't suppose you have any idea where I could find a finely balanced custom bowstring, do you?",
            )
            stage = 1
            return true
        } else if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npcl(
                FaceAnim.HAPPY,
                "Hello again, ${getAttribute(player, "fremennikname", "fremmyname")}. Come to see what's for sale?",
            )
            stage = 101
            return true
        } else {
            playerl(FaceAnim.HAPPY, "Hello!")
            stage = 100
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                npcl(
                    FaceAnim.THINKING,
                    "Aye, I have a few in stock. What would an outerlander be wanting with equipment like that?",
                ).also { stage++ }

            2 ->
                playerl(
                    FaceAnim.HAPPY,
                    "It's for Sigli. It needs to be weighted precisely to suit his hunting bow.",
                ).also { stage++ }

            3 ->
                npcl(
                    FaceAnim.HAPPY,
                    "For Sigli eh? Well, I made his bow in the first place, so I'll be able to select the right string for you... just one small problem.",
                ).also { stage++ }

            4 -> playerl(FaceAnim.THINKING, "What's that?").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.THINKING,
                    "This string you'll be wanting... Very special it is. Take a lot of time to recreate. Not sure you have the cash for it.",
                ).also { stage++ }

            6 -> playerl(FaceAnim.THINKING, "Then maybe you'll accept something else...?").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Heh. Good thinking outerlander. Well, it's true, there is more to life than just making money. Making weapons is good money, but it's not why I do it. I'll tell you what.",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.HAPPY,
                    "I heard a rumour that one of the fishermen down by the docks caught some weird looking fish as they were fishing the other day. From what I hear this fish is unique.",
                ).also { stage++ }

            9 ->
                npcl(
                    FaceAnim.HAPPY,
                    "From what I hear this fish is unique. Nobody's ever seen its like before. This intrigues me. I'd like to have it for myself.",
                ).also { stage++ }

            10 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Make a good trophy. You get me that fish, I give you the bowstring. What do you say? We got a deal?",
                ).also { stage++ }

            11 ->
                playerl(FaceAnim.HAPPY, "Sounds good to me.").also {
                    player.incrementAttribute("sigmund-steps", 1)
                    stage = END_DIALOGUE
                }

            15 -> npcl(FaceAnim.EXTREMELY_SHOCKED, "What? There's another one?").also { stage++ }
            16 -> playerl(FaceAnim.ANNOYED, "Er... no, it's the one for you that I'm looking for...").also { stage++ }
            17 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "Ah. I see. I already told you. Some guy down by the docks was bragging. Best ask there, I reckon.",
                ).also { stage = END_DIALOGUE }

            20 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Ohh... That's a nice fish. Very pleased. Here. Take the bowstring. You fulfilled agreement. Only fair I do same. Good work outerlander.",
                ).also {
                    removeItem(player, Items.UNUSUAL_FISH_3703)
                    addItemOrDrop(player, Items.CUSTOM_BOW_STRING_3702, 1)
                    stage++
                }

            21 -> playerl(FaceAnim.HAPPY, "Thanks!").also { stage = END_DIALOGUE }
            25 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Not hard. Just a trick to it. Takes skill to learn, but when learnt, easy. Sigli will be happy. Finest bowstring on continent. Will suit his needs perfectly.",
                ).also { stage = END_DIALOGUE }

            26 -> npcl(FaceAnim.ANNOYED, "Not for me, I'm afraid.").also { stage = END_DIALOGUE }
            100 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Sorry. I can't sell weapons to outerlanders. Wouldn't be right. Against our beliefs.",
                ).also { stage = END_DIALOGUE }

            101 -> {
                if (anyInInventory(
                        player,
                        Items.DAGANNOTH_HIDE_6155,
                        Items.ROCK_SHELL_CHUNK_6157,
                        Items.ROCK_SHELL_SHARD_6159,
                        Items.ROCK_SHELL_SPLINTER_6161,
                    )
                ) {
                    setTitle(player, 2)
                    sendDialogueOptions(
                        player,
                        "What would you like to ask about?",
                        "Rock Crab Armour",
                        "Show me what you've got.",
                    ).also { stage++ }
                } else {
                    end()
                    openNpcShop(player, NPCs.SKULGRIMEN_1303)
                }
            }

            102 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Hello there. I have these shards of rock crab shell, and believe you might be able to make them into armour for me...?",
                        ).also { stage++ }

                    2 -> {
                        end()
                        openNpcShop(player, NPCs.SKULGRIMEN_1303)
                    }
                }

            103 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "No problem, ${getAttribute(player, "fremennikname", "fremmyname")}. What armour you want?",
                ).also { stage++ }

            104 -> options("A fine helm", "Sturdy bodyarmour", "Powerful leg armour", "Nothing").also { stage++ }
            105 ->
                when (buttonId) {
                    1 -> npcl(FaceAnim.HALF_ASKING, "It'll cost 5,000 coins. That ok?").also { stage += 2 }
                    2 -> npcl(FaceAnim.HALF_ASKING, "It'll cost 10,000 coins. That ok?").also { stage += 4 }
                    3 -> npcl(FaceAnim.HALF_ASKING, "It'll cost 7,500 coins. That ok?").also { stage += 6 }
                    4 -> playerl(FaceAnim.HAPPY, "Actually, I don't want anything.").also { stage++ }
                }

            106 -> npcl(FaceAnim.FRIENDLY, "AS you wish.").also { stage = END_DIALOGUE }
            107 -> options("YES", "NO").also { stage++ }
            108 ->
                when (buttonId) {
                    1 -> {
                        if (amountInInventory(player, Items.COINS_995) < 5000 ||
                            !inInventory(
                                player,
                                Items.DAGANNOTH_HIDE_6155,
                            ) ||
                            !inInventory(player, Items.ROCK_SHELL_CHUNK_6157)
                        ) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Sorry. Need 1 piece of dark daggermouth hide, 1 rock-shell chunk and 5,000 coins to make you helmet. Come back when you got it.",
                            ).also { stage = END_DIALOGUE }
                        } else {
                            removeItem(player, Item(Items.COINS_995, 5000))
                            removeItem(player, Item(Items.DAGANNOTH_HIDE_6155, 1))
                            removeItem(player, Item(Items.ROCK_SHELL_CHUNK_6157, 1))
                            addItemOrDrop(player, Items.ROCK_SHELL_HELM_6128)
                            npc("There you go. Fine helm. You want another?")
                            stage = 107
                        }
                    }

                    2 -> npcl(FaceAnim.FRIENDLY, "AS you wish.").also { stage = END_DIALOGUE }
                }

            109 -> options("YES", "NO").also { stage++ }
            110 ->
                when (buttonId) {
                    1 -> {
                        if (amountInInventory(player, Items.COINS_995) < 10000 ||
                            amountInInventory(
                                player,
                                Items.DAGANNOTH_HIDE_6155,
                            ) < 3 ||
                            !inInventory(player, Items.ROCK_SHELL_SHARD_6159)
                        ) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Apologies. Need 3 pieces of dark daggermouth hide, 1 rock-shell shard and 10,000 coins to make you strong armour. Come back when you got it.",
                            ).also { stage = END_DIALOGUE }
                        } else {
                            removeItem(player, Item(Items.COINS_995, 10000))
                            removeItem(player, Item(Items.DAGANNOTH_HIDE_6155, 3))
                            removeItem(player, Item(Items.ROCK_SHELL_SHARD_6160, 1))
                            addItemOrDrop(player, Items.ROCK_SHELL_PLATE_6129)
                            npc("There you go. Sturdy armour. You need another piece?")
                            stage = 109
                        }
                    }

                    2 -> npcl(FaceAnim.FRIENDLY, "AS you wish.").also { stage = END_DIALOGUE }
                }

            111 -> options("YES", "NO").also { stage++ }
            112 ->
                when (buttonId) {
                    1 -> {
                        if (amountInInventory(player, Items.COINS_995) < 7500 ||
                            amountInInventory(
                                player,
                                Items.DAGANNOTH_HIDE_6155,
                            ) < 2 ||
                            !inInventory(player, Items.ROCK_SHELL_SPLINTER_6161)
                        ) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Apologies. Need 2 pieces of dark daggermouth hide, 1 rock-shell splinter and 7,500 coins to make you leg armour. Come back when you got it.",
                            ).also { stage = END_DIALOGUE }
                        } else {
                            removeItem(player, Item(Items.COINS_995, 7500))
                            removeItem(player, Item(Items.DAGANNOTH_HIDE_6155, 2))
                            removeItem(player, Item(Items.ROCK_SHELL_SPLINTER_6162, 1))
                            addItemOrDrop(player, Items.ROCK_SHELL_PLATE_6129)
                            npc("There you go. Sturdy armour. You need another piece?")
                            stage = 111
                        }
                    }

                    2 -> npcl(FaceAnim.FRIENDLY, "AS you wish.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SKULGRIMEN_1303)
    }
}
