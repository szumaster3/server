package content.region.asgarnia.dialogue.falador

import core.api.animate
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class PartyPeteDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hi!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Hi! I'm, Party Pete. Welcome to the Party Room!").also { stage++ }
            1 ->
                options(
                    "So, what's this room for?",
                    "What's the big lever over there for?",
                    "What's the gold chest for?",
                    "I wanna party!",
                    "Nothing.",
                ).also {
                    stage++
                }
            2 ->
                when (interfaceId) {
                    234 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.HALF_GUILTY, "So, what's this room for?").also { stage = 50 }
                            2 ->
                                player(
                                    FaceAnim.HALF_GUILTY,
                                    "What's the big lever over there for?",
                                ).also { stage = 55 }
                            3 -> player(FaceAnim.HALF_GUILTY, "What's the gold chest for?").also { stage = 61 }
                            4 -> player(FaceAnim.HALF_GUILTY, "I wanna party!").also { stage = 65 }
                            5 -> player(FaceAnim.HALF_GUILTY, "Nothing.").also { stage = END_DIALOGUE }
                        }
                }
            50 -> npc(FaceAnim.HALF_GUILTY, "This room is for partying the night away!").also { stage++ }
            51 -> player(FaceAnim.HALF_GUILTY, "How do you have a party in " + settings!!.name + "?").also { stage++ }
            52 -> npc(FaceAnim.HALF_GUILTY, "Get a few mates round, get the beers in and have fun!").also { stage++ }
            53 -> npc(FaceAnim.HALF_GUILTY, "Some players organise parties so kee an eye open!").also { stage++ }
            54 -> player(FaceAnim.HALF_GUILTY, "Woop! Thanks Pete!").also { stage = END_DIALOGUE }
            55 -> npc(FaceAnim.HALF_GUILTY, "Simple. With the lever you can do some fun stuff.").also { stage++ }
            56 -> player(FaceAnim.HALF_GUILTY, "What kind of stuff?").also { stage++ }
            57 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "A balloon drop costs 1000 gold. For this, you get 200",
                    "balloons dropped across the whole of the party room. You",
                    "canthen have fun popping the balloons!",
                ).also {
                    stage++
                }
            58 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Any items in the Party Drop Chest will be put into balloons",
                    "as soon as you pull the lever.",
                ).also {
                    stage++
                }
            59 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "When the balloons are released, you can burst them to",
                    "get at the items!",
                ).also {
                    stage++
                }
            60 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "For 500 gold, you can summon the Party Room Knights,",
                    "who will dance for your delight. Their singing isn't a",
                    "delight, though.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            61 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Any items in the chest will be dropped inside the ballons",
                    "when you pull the lever.",
                ).also {
                    stage++
                }
            62 -> player(FaceAnim.HALF_GUILTY, "Cool! Sounds like a fun way to do a drop party.").also { stage++ }
            63 -> npc(FaceAnim.HALF_GUILTY, "Exactly!").also { stage++ }
            64 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "A word of warning, though. Any items that you put into",
                    "the chest can't be taken out again, and it costs 1000 gold",
                    "pieces for each drop party.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            65 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I've won the Dance Trophy at the Kandarin Ball three",
                    "years in a trot!",
                ).also {
                    stage++
                }
            66 -> player(FaceAnim.HALF_GUILTY, "Show me your moves Pete!").also { stage++ }
            67 -> {
                animate(npc, Animation(784))
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PARTY_PETE_659)
    }
}
