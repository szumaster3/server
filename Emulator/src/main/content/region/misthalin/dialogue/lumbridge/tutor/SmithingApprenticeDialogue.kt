package content.region.misthalin.dialogue.lumbridge.tutor

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SmithingApprenticeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when (getStatLevel(player, Skills.SMITHING)) {
            in 15..28 -> playerl(FaceAnim.HALF_ASKING, "I already know about the basics of smelting, got any tips?")
            in 29..99 -> playerl(FaceAnim.HALF_ASKING, "What do I do after smelting the ore?")
            else -> playerl(FaceAnim.HALF_GUILTY, "Can you teach me the basics of smelting please?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (getStatLevel(player, Skills.SMITHING)) {
            in 1..14 ->
                when (stage) {
                    0 ->
                        sendItemDialogue(
                            player,
                            Items.NULL_5084,
                            "Look for this icon on your minimap to find a furnace to smelt ores into metal.",
                        ).also {
                            stage++
                        }
                    1 ->
                        if (!anyInInventory(player, *ores)) {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "You'll need to have mined some ore to smelt first. Go see the mining tutor to the south if you're not sure how to do this.",
                            ).also {
                                stage++
                            }
                        } else {
                            npcl(
                                FaceAnim.HALF_GUILTY,
                                "I see you have some ore with you to smelt, so let's get started.",
                            ).also {
                                stage =
                                    3
                            }
                        }
                    2 ->
                        sendItemDialogue(
                            player,
                            Items.NULL_5082,
                            "Look for this icon to the south of here, in the swamp.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    3 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Click on the furnace to bring up a menu of metal bars you can try to make from your ore.",
                        ).also {
                            stage++
                        }
                    4 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "When you have a full inventory, take it to the bank, you can find it on the roof of the castle in Lumbridge.",
                        ).also {
                            stage++
                        }
                    5 ->
                        sendItemDialogue(
                            player,
                            Items.NULL_5080,
                            "To find a bank, look for this symbol on your minimap after climbing the stairs of the Lumbridge Castle to the top. There are banks all over the world with this symbol.",
                        ).also {
                            stage++
                        }
                    6 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "If you have a hammer with you, you can smith the bronze bars into equipment on the anvil outside.",
                        ).also {
                            stage++
                        }
                    7 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm afraid the weather over the years has rusted it down, so it can only be used to work bronze.",
                        ).also {
                            stage++
                        }
                    8 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Alternatively you can run up to Varrock. Look for my Master, the Smithing Tutor, in the west of the city, he can help you smith better gear.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            in 21..28 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I find it useful to stockpile my ore before smelting it in one go, or even do it on the way to the bank in such places as Al Kharid or Falador. But you can do it anyway you want, one load at a time is fine too!",
                        ).also {
                            stage++
                        }
                    1 -> npcl(FaceAnim.FRIENDLY, "Not too much can go wrong with smelting.").also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You will lose some iron when smelting that ore, but a Ring of Forging or the Superheat Item spell will soon solve that.",
                        ).also {
                            stage++
                        }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You should also investigate Keldagrim, the dwarven city, you may find better places to smelt your ore with friends.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }

            in 29..99 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Ahh you'll need to learn to smith. Go to Varrock, that's where my master, the Smithing Tutor, plies his trade. Ask him to teach you how to smith.",
                        ).also {
                            stage++
                        }
                    1 ->
                        sendItemDialogue(
                            player,
                            Items.NULL_5086,
                            "Look for this icon in the west of Varrock.",
                        ).also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "Smelt some ore and store it in the bank. Grab a hammer from the general store before you go too!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SMELTING_TUTOR_4904, NPCs.SMITHING_TUTOR_7959, NPCs.SMITHING_TUTOR_7970)
    }

    companion object {
        private val ores =
            intArrayOf(
                Items.COPPER_ORE_436,
                Items.TIN_ORE_438,
                Items.IRON_ORE_440,
                Items.COAL_453,
                Items.BLURITE_ORE_668,
                Items.SILVER_ORE_442,
                Items.RUNITE_ORE_451,
                Items.MITHRIL_ORE_447,
                Items.ADAMANTITE_ORE_449,
                Items.PERFECT_GOLD_ORE_446,
                Items.GOLD_ORE_444,
                Items.ELEMENTAL_ORE_2892,
            )
    }
}
