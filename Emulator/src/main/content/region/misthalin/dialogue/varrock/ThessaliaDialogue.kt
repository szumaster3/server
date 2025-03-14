package content.region.misthalin.dialogue.varrock

import core.api.*
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

@Initializable
class ThessaliaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (args.size == 3) {
            if (player.equipment.isEmpty) {
                if (player.isMale) {
                    end()
                    openInterface(player, Components.THESSALIA_CLOTHES_MALE_591)
                } else {
                    end()
                    openInterface(player, Components.THESSALIA_CLOTHES_FEMALE_594)
                }
            } else {
                npc(
                    FaceAnim.WORRIED,
                    "You can't try them on while wearing armour. Take",
                    "it off and speak to me again.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            }
            return true
        }
        npc = args[0] as NPC
        npc(FaceAnim.ASKING, "Would you like to buy any fine clothes?")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (inInventory(player, Items.FROG_TOKEN_6183)) {
                    options("What do you have?", "No, thank you.", "I have a frog token...").also { stage++ }
                } else {
                    options("What do you have?", "No, thank you.").also { stage++ }
                }
            }

            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "What do you have?").also { stage = 10 }
                    2 -> player(FaceAnim.NEUTRAL, "No, thank you.").also { stage = END_DIALOGUE }
                    3 ->
                        if (inInventory(player, Items.FROG_TOKEN_6183)) {
                            npc(
                                FaceAnim.FRIENDLY,
                                "That entitles you to a free costume!",
                                "Do you want a frog mask or a " + (if (player.isMale) "prince" else "princess") +
                                    " outfit?",
                            ).also {
                                stage =
                                    52
                            }
                        }
                }

            10 ->
                npc(
                    FaceAnim.HAPPY,
                    "I have a number of fine pieces of clothing on sale or,",
                    "if you prefer, I can offer you an exclusive",
                    "total clothing makeover?",
                ).also {
                    stage++
                }
            11 -> options("Tell me more about this makeover.", "I'd just like to buy some clothes.").also { stage++ }
            12 ->
                when (buttonId) {
                    1 -> player(FaceAnim.THINKING, "Tell me more about this makeover.").also { stage = 20 }
                    2 -> player(FaceAnim.NEUTRAL, "I'd just like to buy some clothes.").also { stage = 50 }
                }
            20 -> npc(FaceAnim.HAPPY, "Certainly!").also { stage++ }
            21 ->
                npc(
                    FaceAnim.HAPPY,
                    "Here at Thessalia's fine clothing boutique, we offer a",
                    "unique service where we will totally revamp your outfit",
                    "to your choosing, for... wait for it...",
                ).also {
                    stage++
                }
            22 ->
                npc(
                    FaceAnim.AMAZED,
                    "A fee of only 500 gold coins! Tired of always wearing",
                    "the same old outfit, day in, day out? This is the service",
                    "for you!",
                ).also {
                    stage++
                }
            23 ->
                npc(
                    FaceAnim.ASKING,
                    "So what do you say? Interested? We can change either",
                    "your top, or your legwear for only 500 gold an item!",
                ).also {
                    stage++
                }
            24 ->
                options(
                    "I'd like to change my outfit, please.",
                    "I'd just like to buy some clothes.",
                ).also { stage++ }
            25 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "I'd like to change my outfit, please.").also { stage = 30 }
                    2 -> player(FaceAnim.HAPPY, "I'd just like to buy some clothes.").also { stage = 50 }
                }
            30 -> {
                if (player.equipment.isEmpty) {
                    playAudio(player, Sounds.GOO_HIT_273)
                    npc(
                        FaceAnim.HAPPY,
                        "Just select what style and colour you would like from",
                        "this catalogue, and then give me the 1000 gold when",
                        "you've picked.",
                    ).also {
                        stage++
                    }
                } else {
                    npc(
                        FaceAnim.WORRIED,
                        "You can't try them on while wearing armour. Take",
                        "it off and speak to me again.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                }
            }

            31 -> {
                if (player.equipment.isEmpty) {
                    if (player.isMale) {
                        end()
                        openInterface(player, Components.THESSALIA_CLOTHES_MALE_591)
                    } else {
                        end()
                        openInterface(player, Components.THESSALIA_CLOTHES_FEMALE_594)
                    }
                }
            }
            49 -> npc(FaceAnim.FRIENDLY, "That's ok! Just come back when you do have it!").also { stage = END_DIALOGUE }
            50 -> {
                end()
                openNpcShop(player, NPCs.THESSALIA_548)
            }
            51 -> npc("Well, please return if you change your mind.").also { stage = END_DIALOGUE }
            52 ->
                options(
                    "Frog mask, please!",
                    "" + (if (player.isMale) "Prince" else "Princess") + " outfit, please!",
                ).also {
                    stage++
                }
            53 ->
                when (buttonId) {
                    1 -> {
                        if (removeItem(player, Items.FROG_TOKEN_6183)) {
                            end()
                            npc(FaceAnim.FRIENDLY, "There you go.")
                            addItemOrDrop(player, Items.FROG_MASK_6188, 1)
                        }
                    }
                    2 -> {
                        npc(FaceAnim.FRIENDLY, "There you go.")
                        if (player.isMale && removeItem(player, Items.FROG_TOKEN_6183)) {
                            addItemOrDrop(player, Items.PRINCE_TUNIC_6184, 1)
                            addItemOrDrop(player, Items.PRINCE_LEGGINGS_6185, 1)
                        } else if (removeItem(player, Items.FROG_TOKEN_6183)) {
                            addItemOrDrop(player, Items.PRINCESS_BLOUSE_6186, 1)
                            addItemOrDrop(player, Items.PRINCESS_SKIRT_6187, 1)
                        }
                        end()
                    }
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ThessaliaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THESSALIA_548)
    }
}
