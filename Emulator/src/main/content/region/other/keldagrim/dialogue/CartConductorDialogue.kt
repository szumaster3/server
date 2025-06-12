package content.region.other.keldagrim.dialogue

import content.data.GameAttributes
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

private const val DWARVEN_MINE_CONDUCTOR = NPCs.CART_CONDUCTOR_2186
private const val WHITE_WOLF_CONDUCTOR = NPCs.CART_CONDUCTOR_2185
private const val KELDAGRIM_CONDUCTOR = NPCs.CART_CONDUCTOR_2182
private const val SECOND_KELDAGRIM_CONDUCTOR = NPCs.CART_CONDUCTOR_2183
private const val RESTING_CONDUCTOR = NPCs.CART_CONDUCTOR_2184

/**
 * Represents Cart conductor dialogues.
 */
@Initializable
class CartConductorDialogue(
    player: Player? = null,
) : Dialogue(player) {

    private var visitedKeldagrim = false

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        visitedKeldagrim = player.getAttribute(GameAttributes.MINECART_TRAVEL_UNLOCK, false)

        if (!visitedKeldagrim) {
            npcl(FaceAnim.OLD_NORMAL, "Sorry, but I can only take people who have been there before.")
            stage = 100
        } else {
            when (npc.id) {
                KELDAGRIM_CONDUCTOR -> npcl(FaceAnim.OLD_NORMAL, "Tickets, tickets, get your tickets here!")
                SECOND_KELDAGRIM_CONDUCTOR -> npcl(FaceAnim.OLD_NORMAL, "Yes ${if (player.isMale) "sir" else "m'am"}? Can I help you at all?")
                RESTING_CONDUCTOR -> npcl(FaceAnim.OLD_NORMAL, "Wot you want?")
                else -> npcl(FaceAnim.OLD_NORMAL, "Anything I can help you with?")
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                showTopics(
                    IfTopic("Who are you?", 1, npc.id == DWARVEN_MINE_CONDUCTOR || npc.id == WHITE_WOLF_CONDUCTOR),
                    IfTopic("What is this place?", 2, npc.id == KELDAGRIM_CONDUCTOR || npc.id == SECOND_KELDAGRIM_CONDUCTOR || npc.id == RESTING_CONDUCTOR),
                    Topic("Where can you take me?", 3),
                    Topic("I'd like to buy a ticket.", 10),
                    Topic("I have to go.", 11)
                )
            }

            1 -> {
                when (npc.id) {
                    WHITE_WOLF_CONDUCTOR -> npcl(FaceAnim.OLD_DRUNK_LEFT, "Whooooo? Meeeee? Oh, I make sure *hic* the cart traffic proceedsh *hic* in an orderly fishion. Fashion. Yes.")
                    else -> npcl(FaceAnim.OLD_NORMAL, "I'm an employee of the Keldagrim carts. I make sure the carts in this area run on time and that people pay their fares.")
                }
                stage = 0
            }

            2 -> {
                when (npc.id) {
                    RESTING_CONDUCTOR -> npcl(FaceAnim.OLD_DEFAULT, "Can't you see I'm 'aving a li'l break?")
                    KELDAGRIM_CONDUCTOR -> npcl(FaceAnim.OLD_DEFAULT, "This is the Keldagrim cart-station, human.")
                    else -> npcl(FaceAnim.OLD_DEFAULT, "You're in the main waiting area of Keldagrim cart-station.")
                }
                stage = 5
            }

            3 -> when (npc.id) {
                DWARVEN_MINE_CONDUCTOR -> npcl(FaceAnim.OLD_DEFAULT, "This track leads right up to Keldagrim. Only stop.").also { stage = 0 }
                WHITE_WOLF_CONDUCTOR -> npcl(FaceAnim.OLD_DRUNK_LEFT, "North, north, north *hic* north to Keldagrim!").also { stage = 0 }
                else -> npcl(FaceAnim.OLD_NORMAL, "Let's see, we can take you to the mines under Ice Mountain and to the Grand Exchange in Varrock.").also { stage = if (isQuestComplete(player, Quests.FISHING_CONTEST)) 4 else 0 }
            }

            4 -> {
                npcl(FaceAnim.OLD_NORMAL, "And since you're a friend of our brothers under White Wolf Mountain, we could take you there as well.")
                stage = 0
            }

            5 -> when (npc.id) {
                RESTING_CONDUCTOR -> player(FaceAnim.NEUTRAL, "Now that you mention it, yes.").also { stage = END_DIALOGUE }
                else -> player(FaceAnim.HALF_ASKING, "Cart-station? What goes on here then?").also { stage++ }
            }

            6 -> npcl(FaceAnim.OLD_DEFAULT, "Keldagrim is the hub of all the traffic of the dwarven realm. Almost every dwarven outpost has railtracks leading to Keldagrim, connecting them for easy travel and transportation of goods.").also { stage++ }

            7 -> player(FaceAnim.HAPPY, "Amazing! And what powers the carts? Magic?").also { stage++ }
            8 -> npcl(FaceAnim.OLD_DEFAULT, "Oh, no no, we dwarves don't use magic. All of this is powered by steam engines, developed over the past few centuries after the Consortium came to power.").also { stage++ }
            9 -> player("Thanks for the info.").also { stage = 0 }
            10 -> {
                when (npc.id) {
                    DWARVEN_MINE_CONDUCTOR -> npcl(FaceAnim.OLD_NORMAL, "Oh, you don't need a ticket. The Consortium has decided to grant humans free mine cart travel to and from Keldagrim.")
                    WHITE_WOLF_CONDUCTOR -> npcl(FaceAnim.OLD_DRUNK_LEFT, "One ticket to ehm...*hic* Keldagrim. That's 2... 3...*hic* nothing! It's free! Issa new econo...ec'nomic 'nitiative. All humans travel to Keldagrim...for free!")
                    else -> npcl(FaceAnim.OLD_NORMAL, "You don't need a ticket. The Consortium has decided to grant free mine cart travel to all humans.")
                }
                stage++
            }

            11 -> {
                when (npc.id) {
                    DWARVEN_MINE_CONDUCTOR -> npcl(FaceAnim.OLD_NORMAL, "Just remember, wherever you go, you go there faster through Keldagrim carts.")
                    WHITE_WOLF_CONDUCTOR -> npcl(FaceAnim.OLD_DRUNK_LEFT, "Just remember, wherever you go...ehm...*hic* oh, forget it.")
                    else -> npcl(FaceAnim.OLD_NORMAL, "Safe travels!")
                }
                stage = 100
            }

            100 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CartConductorDialogue(player)

    override fun getIds(): IntArray = intArrayOf(
        DWARVEN_MINE_CONDUCTOR,
        WHITE_WOLF_CONDUCTOR,
        KELDAGRIM_CONDUCTOR,
        SECOND_KELDAGRIM_CONDUCTOR
    )
}
