package content.region.asgarnia.falador.plugin.partyroom

import core.api.animate
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.NPCs

@Initializable
class PartyPeteDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hi!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Hi! I'm, Party Pete. Welcome to the Party Room!").also { stage++ }
            1 ->
                options(
                    "So, what's this room for?",
                    "What's the big lever over there for?",
                    "What's the gold chest for?",
                    "I wanna party!",
                    "More.",
                ).also {
                    stage = 3
                }
            3 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "So, what's this room for?").also { stage = 50 }
                    2 -> player(FaceAnim.HALF_GUILTY, "What's the big lever over there for?").also { stage = 55 }
                    3 -> player(FaceAnim.HALF_GUILTY, "What's the gold chest for?").also { stage = 61 }
                    4 -> player(FaceAnim.HALF_GUILTY, "I wanna party!").also { stage = 65 }
                    5 -> options(
                        "I love your hair!",
                        "Why's there a chameleon in here?",
                        "Back.",
                    ).also {
                        stage = 5
                    }
                }
            5 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "I love your hair!").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Why's there a chameleon in here?").also { stage = 9 }
                    3 -> options(
                        "So, what's this room for?",
                        "What's the big lever over there for?",
                        "What's the gold chest for?",
                        "I wanna party!",
                        "More.",
                    ).also {
                        stage = 3
                    }
                }
            6 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Isn't it groovy? I liked it so much, I had extras made for my party goers. Would you like to buy one?",
                ).also {
                    stage++
                }
            7 -> options("Yes", "No").also { stage++ }
            8 ->
                when (buttonId) {
                    1 -> end().also { openNpcShop(player, npc.id) }
                    2 -> end()
                }
            9 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Karma's my pet. I got him for Christmas one year. He keeps the Party Room free of flies, and he loves watching me dance. Karma karma karma cha...",
                ).also {
                    stage++
                }
            10 ->
                options(
                    "Can you talk to him?",
                    "Christmas is over.",
                    "Aww, that's nice.",
                    "Got any cake?",
                ).also { stage++ }
            11 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_ASKING,"Can you talk to him?").also { stage++ }
                    2 -> player(FaceAnim.HALF_ASKING, "Christmas is over. Why've you still got him hanging", "around?").also { stage = 13 }
                    3 -> player("Aww, that's nice.").also { stage = 10 }
                    4 -> player(FaceAnim.HALF_ASKING,"Got any cake?").also { stage = 14 }
                }

            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Sure, I talk to the little fellow all the time. My Summoning level's not high enough to understand what he says back, but he's still great company.",
                ).also {
                    stage =
                        10
                }
            13 ->
                npcl(FaceAnim.FRIENDLY, "I couldn't chuck the little chappy out! A pet is for life!").also {
                    stage =
                        10
                }
            14 ->
                npcl(
                    FaceAnim.SAD,
                    "Sadly no cake at the moment, try coming back January 1st... But the party never ends!",
                ).also {
                    stage =
                        END_DIALOGUE
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
                animate(npc, Animation(Animations.PARTY_PETE_NOD_DANCE_784))
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PARTY_PETE_659)
}
