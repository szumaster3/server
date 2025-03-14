package content.region.misthalin.dialogue.monastery

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape.isMaster
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BrotherJeredDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (isMaster(player, Skills.PRAYER)) {
            player("Can I buy a Skillcape of Prayer?")
            stage = 200
        } else if (inInventory(player, Items.HOLY_ELIXIR_13754, 1) &&
            inInventory(
                player,
                Items.SPIRIT_SHIELD_13734,
                1,
            )
        ) {
            player("Can you bless this spirit shield for me?")
            stage = 100
        } else {
            options("What can you do to help a bold adventurer like myself?", "Praise be to Saradomin!")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 ->
                        if (!inInventory(player, Items.UNBLESSED_SYMBOL_1716, 1)) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "I can tell you about holy symbols or the Skillcape of Prayer.",
                            ).also { stage++ }
                        } else if (amountInInventory(player, Items.UNBLESSED_SYMBOL_1716) > 1) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Well I can bless those stars of Saradomin you have, or I could tell you about the Skillcape of Prayer!",
                            ).also { stage = 9 }
                        } else {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "Well I can bless that star of Saradomin you have, or I could tell you about the Skillcape of Prayer!",
                            ).also { stage = 8 }
                        }

                    2 ->
                        npcl(FaceAnim.HALF_GUILTY, "Yes! Praise he who brings life to this world.").also {
                            stage = END_DIALOGUE
                        }
                }

            1 -> options("Tell me about holy symbols.", "Tell me about the Skillcape of Prayer.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_GUILTY, "Tell me about holy symbols.").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "Tell me about the Skillcape of Prayer.").also { stage = 4 }
                }

            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If you have a silver star, which is the holy symbol of Saradomin, then I can bless it. Then if you are wearing it, it will help you when you are praying.",
                ).also { stage = 1 }

            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The Skillcape of Prayer is the hardest of all the skillcapes to get; it requires much devotion to acquire but also imbues the wearer with the ability to briefly fly! Is there something else I can do for you?",
                ).also { stage++ }

            5 ->
                options(
                    "Yes, what else can you do for a bold adventurer like myself?",
                    "No, thank you.",
                ).also { stage++ }

            6 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "Yes, what else can you do for a bold adventurer like myself?",
                        ).also { stage++ }

                    2 -> playerl(FaceAnim.HALF_GUILTY, "No, thank you.").also { stage = END_DIALOGUE }
                }

            7 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I can tell you about holy symbols or the Skillcape of Prayer.",
                ).also { stage = 0 }

            8 ->
                options(
                    "Yes please, bless my star.",
                    "Tell me about the Skillcape of Prayer.",
                    "None of those, thanks.",
                ).also { stage++ }

            9 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_GUILTY, "Yes please, bless my star.").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "Tell me about the Skillcape of Prayer.").also { stage = 4 }
                    3 -> playerl(FaceAnim.HALF_GUILTY, "None of those, thanks.").also { stage = END_DIALOGUE }
                }

            10 -> {
                end()
                var amount = amountInInventory(player, Items.UNBLESSED_SYMBOL_1716)
                if (!removeItem(player, Item(Items.UNBLESSED_SYMBOL_1716, amount))) {
                    sendMessage(player, "You don't have required items.")
                } else {
                    sendItemDialogue(
                        player,
                        Items.UNBLESSED_SYMBOL_1716,
                        "Jered closes his eyes and softly chants. The symbol is imbued with his blessing.",
                    )
                    addItem(player, Items.HOLY_SYMBOL_1718, amount)
                }
            }

            100 -> npc("Yes I certainly can, but", "for 1,000,000 coins.").also { stage++ }
            101 -> options("Okay, sounds good.", "Oh, no thanks..").also { stage++ }
            102 ->
                when (buttonId) {
                    1 ->
                        if (inInventory(player, Items.COINS_995, 1000000)) {
                            player("Okay, sounds good!").also { stage = 110 }
                        } else {
                            player("I think I have the money right here, actually.").also { stage = 120 }
                        }

                    2 -> player("Oh. No, thank you, then.").also { stage = END_DIALOGUE }
                }

            110 -> {
                npc("Here you are.")
                if (player.inventory.remove(
                        Item(Items.HOLY_ELIXIR_13754),
                        Item(Items.SPIRIT_SHIELD_13734),
                        Item(995, 1000000),
                    )
                ) {
                    player.inventory.add(Item(Items.BLESSED_SPIRIT_SHIELD_13736))
                }
                stage++
            }

            111 -> player("Thank you!").also { stage = END_DIALOGUE }
            120 -> npc("That's too bad then.").also { stage = END_DIALOGUE }

            200 -> npc("Certainly! Right when you give me 99000 coins.").also { stage++ }
            201 -> options("Okay, here you go.", "No").also { stage++ }
            202 ->
                when (buttonId) {
                    1 -> player("Okay, here you go.").also { stage++ }
                    2 -> end()
                }

            203 -> {
                if (purchase(player, Skills.PRAYER)) {
                    npc("There you go! Enjoy.")
                    end()
                } else {
                    playerl(FaceAnim.HALF_GUILTY, "No thanks, I can't afford one of those.")
                }
                end()
            }

            301 -> playerl(FaceAnim.FRIENDLY, "What can you do to help a bold adventurer like myself?").also { stage++ }

            302 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, seeing as you are so devout in praising the gods, I could sell you a Skillcape of Prayer.",
                ).also { stage++ }

            303 -> playerl(FaceAnim.FRIENDLY, "Yes, please. So few people have Skillcapes of Prayer!").also { stage++ }

            304 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm sorry, I can't sell you one here. Try talking to me on a members' world.",
                ).also { stage++ }

            305 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I don't do this for anyone, but I've got a special offer today! Normally I require 99,000 coins for this prestigious item, but for you it's half price. Only 92,000 coins!",
                ).also { stage++ }

            306 -> playerl(FaceAnim.FRIENDLY, "92,000 coins? That's much too expensive.").also { stage++ }
            307 -> playerl(FaceAnim.FRIENDLY, "I know where to find you if I change my mind.").also { stage++ }
            308 -> playerl(FaceAnim.FRIENDLY, "I think I have the money right here, actually.").also { stage++ }

            309 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Unfortunately all skillcapes are only available with a free hood, it's part of a skill promotion deal; buy one get one free, you know. So, you'll need to free up some backpack space before I can sell you one.",
                ).also { stage++ }

            310 -> playerl(FaceAnim.FRIENDLY, "But unfortunately, I was mistaken.").also { stage++ }
            311 -> npcl(FaceAnim.FRIENDLY, "Well, come back and see me when you do.").also { stage++ }
            312 -> npcl(FaceAnim.FRIENDLY, "Excellent! Wear that cape with pride my friend.").also { stage++ }

            313 -> playerl(FaceAnim.FRIENDLY, "99,000 coins? That's much too expensive.").also { stage++ }
            314 -> playerl(FaceAnim.FRIENDLY, "I know where to find you if I change my mind.").also { stage++ }
            315 -> playerl(FaceAnim.FRIENDLY, "I think I have the money right here, actually.").also { stage++ }

            316 ->
                sendDoubleItemDialogue(
                    player,
                    Items.PRAYER_CAPE_9759,
                    Items.PRAYER_CAPET_9760,
                    "${player.username} receives prayer cape and prayer hood. Player has 99,000 coins removed from them.",
                ).also { stage++ }

            317 -> npcl(FaceAnim.FRIENDLY, "Excellent! Wear that cape with pride my friend.").also { stage++ }
            318 -> playerl(FaceAnim.FRIENDLY, "No thanks, I can't afford one of those.").also { stage++ }
            319 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I am sorry to hear that. If you should find yourself in wealthier times come back and see me.",
                ).also { stage++ }

            320 -> npcl(FaceAnim.FRIENDLY, "Well I can bless that star of Saradomin you have.").also { stage++ }

            321 -> playerl(FaceAnim.FRIENDLY, "Yes please, bless my star.").also { stage++ }
            322 -> playerl(FaceAnim.FRIENDLY, "No thank you.").also { stage++ }

            323 -> npcl(FaceAnim.FRIENDLY, "Well I can bless those stars of Saradomin you have.").also { stage++ }

            324 -> playerl(FaceAnim.FRIENDLY, "Yes please, bless my stars.").also { stage++ }
            325 -> playerl(FaceAnim.FRIENDLY, "No thank you.").also { stage++ }
            326 -> npcl(FaceAnim.FRIENDLY, "Praise be to Saradomin!").also { stage++ }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return BrotherJeredDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BROTHER_JERED_802)
    }
}
