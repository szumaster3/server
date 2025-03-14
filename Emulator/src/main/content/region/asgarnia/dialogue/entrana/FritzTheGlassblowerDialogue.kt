package content.region.asgarnia.dialogue.entrana

import core.api.addItem
import core.api.inInventory
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class FritzTheGlassblowerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!player.getSavedData().globalData.isFritzGlass()) {
            npc(FaceAnim.HALF_GUILTY, "Hello adventurer, welcome to the Entrana furnace.")
        } else {
            npc(
                FaceAnim.HALF_GUILTY,
                "Ah " + player.username + ", have you come to sell me some molten",
                "glass?",
            ).also {
                stage =
                    100
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Would you like me to explain my craft to you?").also { stage++ }
            1 ->
                options(
                    "Yes please. I'd be fascinated to hear what you do.",
                    "No thanks, I doubt I'll ever turn my hand to glassblowing.",
                ).also {
                    stage++
                }
            2 ->
                when (buttonId) {
                    1 ->
                        player(FaceAnim.HALF_GUILTY, "Yes please. I'd be fascinated to hear what you do.").also {
                            stage =
                                10
                        }
                    2 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "No thanks, I doubt I'll ever turn my hand to glassblowing.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm extremely pleased to hear that! I've always wanted",
                    "an apprentice. Let me talk to you through the secrets of",
                    "glassblowing.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Glass is made from soda ash and silica. We get out",
                    "soda ash by collecting seaweed from the rocks - the",
                    "prevailing currents make the north-west corner of the",
                    "island the best place to find it, it can also be found in",
                ).also {
                    stage++
                }
            12 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "your nets sometimes when you're fishing, on Karamja",
                    "island or at the Piscatoris Fishing Colonly in the nets",
                    "there. To turn seaweed into soda ash, all you need to",
                    "do is burn it on a fire. Feel free to use the range in",
                ).also {
                    stage++
                }
            13 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "my house for that; it's the one directly west of here.",
                    "Next we collect sand from the sandpit that you'll also",
                    "find just west of here, there are other located in",
                    "Yanille and Shilo Village.",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You'll need a bucket to cary it in. Tell you what, you",
                    "can have this old one of mine.",
                ).also {
                    stage++
                }
            15 -> {
                if (!player.inventory.add(Item(1925))) {
                    GroundItemManager.create(GroundItem(Item(1925), player.location, player))
                }
                player.getSavedData().globalData.setFritzGlass(true)
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Bring the sand and the soda ash back here and melt",
                    "them together in the furnace, and there you have it -",
                    "molten glass!",
                )
                stage = 16
            }
            16 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "There are many things you can use the molten glass",
                    "for once you have made it. Depending on how talented",
                    "you are, you could try turning it into something, like a",
                    "fishbowl, for example. If you'd like to try your hand at",
                ).also {
                    stage++
                }
            17 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "the fine art of glassblowing you can use my spare",
                    "glassblowing pipe. I think I left it on the chest of",
                    "drawers in my house this morning.",
                ).also {
                    stage++
                }
            18 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Alternatively I am always happy to buy the molten glass",
                    "from you, saves me running about making it for",
                    "myself.",
                ).also {
                    stage++
                }
            19 -> player(FaceAnim.HALF_GUILTY, "That sounds good. How much will you pay me?").also { stage++ }
            20 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Tell you what, because you've been interested in my",
                    "art, I'll pay you the premium price of 20 gold pieces",
                    "for each piece of molten glass you bring me.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            100 -> {
                options("Yes.", "No.")
                stage = 101
            }
            101 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Yes.").also { stage = 110 }
                    2 -> player(FaceAnim.HALF_GUILTY, "No.").also { stage = 120 }
                }
            110 ->
                if (!player.inventory.containsItem(MOLTEN_GLASS)) {
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "Umm, not much point me trying to pay you for glass",
                        "you don't have, is there?",
                    )
                    stage = 111
                } else {
                    val amt = player.inventory.getAmount(MOLTEN_GLASS)
                    val remove = Item(MOLTEN_GLASS.id, amt)
                    if (!inInventory(player, remove.id)) {
                        end()
                        return true
                    }
                    if (removeItem(player, remove)) {
                        end()
                        addItem(player, Items.COINS_995, amt * 20)
                        npc(FaceAnim.HALF_GUILTY, "Pleasure doing business with you " + player.username + ".")
                    }
                }
            111 -> player(FaceAnim.HALF_GUILTY, "Well, actually, if you don't mind...").also { stage++ }
            112 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I guess you've never heard of a rhetorical question",
                    "then. I'll make it simple for you. You bring glass, me",
                    "pay shiny gold coins.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            120 -> npc(FaceAnim.HALF_GUILTY, "Oh.").also { stage++ }
            121 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "...errr, well should you get any I'm quite happy to pay",
                    "for it. Remember, I'll pay you 20 gold pieces for each",
                    "piece of molten glass you get for me.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FritzTheGlassblowerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FRITZ_THE_GLASSBLOWER_4909)
    }

    companion object {
        private val MOLTEN_GLASS = Item(Items.MOLTEN_GLASS_1775)
    }
}
