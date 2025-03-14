package content.region.kandarin.dialogue.stronghold

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class GnomeCoachDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val clue = Items.CLUE_SCROLL_7282

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC

        npcl(FaceAnim.OLD_NORMAL, "Run faster! Faster!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.HALF_GUILTY, "Excuse me?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_NORMAL, "Oh, sorry, I didn't notice you there.").also { stage++ }
            2 -> playerl(FaceAnim.HALF_GUILTY, "I take it you're a coach?").also { stage++ }
            3 -> npcl(FaceAnim.OLD_NORMAL, "That I am. Now get to the point, I'm very busy!").also { stage++ }
            4 ->
                options(
                    "What's the History of Gnome ball?",
                    "Are there any special tactics?",
                    "Does the game bring fame?",
                    "Yawn",
                ).also { stage++ }

            5 ->
                when (buttonId) {
                    1 -> player("What's the History of Gnome ball?").also { stage++ }
                    2 -> player("Are there any special tactics?").also { stage = 38 }
                    3 -> player("Does the game bring fame?").also { stage = 67 }
                    4 -> player("Yawn").also { stage = 74 }
                }

            6 -> npcl(FaceAnim.OLD_NORMAL, "Be more precise, stranger.").also { stage++ }
            7 ->
                options(
                    "How did it all start?",
                    "What is the ball made from?",
                    "Those nets are strange.",
                ).also { stage++ }

            8 ->
                when (buttonId) {
                    1 -> player("How did it all start?").also { stage++ }
                    2 -> player("What is the ball made from?").also { stage = 24 }
                    3 -> player("Those nets are strange.").also { stage = 29 }
                }

            9 -> npc(FaceAnim.OLD_NORMAL, "Be more precise, stranger.").also { stage++ }
            10 -> npcl(FaceAnim.OLD_NORMAL, "Strangely enough, by accident.").also { stage++ }
            11 -> playerl(FaceAnim.HALF_GUILTY, "Accident?").also { stage++ }
            12 -> npcl(FaceAnim.OLD_NORMAL, "Don't interrupt!").also { stage++ }
            13 -> playerl(FaceAnim.HALF_GUILTY, "Sorry.").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "A great many years ago, before I was even born, there were two young Gnomes. Brothers to be exact.",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "They were always arguing with each other over the most trivial things - who was the faster runner or who was the tallest.",
                ).also {
                    stage++
                }
            16 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "One day they were up at the swamp throwing stones at the toads.",
                ).also { stage++ }
            17 -> playerl(FaceAnim.HALF_GUILTY, "As you do.").also { stage++ }
            18 -> npcl(FaceAnim.OLD_NORMAL, "Yep. You know nothing tastes better than toasted toad?").also { stage++ }
            19 -> playerl(FaceAnim.HALF_GUILTY, "Oh, I'm sure.").also { stage++ }
            20 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Anyway, they decided to compete to see who could kill the most toads. This gradually became a daily game, to which they invited friends and family to join in. It all evolved from there.",
                ).also {
                    stage++
                }
            21 -> npcl(FaceAnim.OLD_NORMAL, "Creating a playing arena, making up teams and leagues.").also { stage++ }
            22 -> playerl(FaceAnim.HALF_GUILTY, "Interesting.").also { stage++ }
            23 -> npcl(FaceAnim.OLD_NORMAL, "Anything else?").also { stage = 4 }
            24 -> npcl(FaceAnim.OLD_NORMAL, "Toads of course!").also { stage++ }
            25 -> playerl(FaceAnim.HALF_GUILTY, "Toads?").also { stage++ }
            26 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "What with the swamp nearby, and their robust skin, there is nothing better to stretch over some common sand.",
                ).also {
                    stage++
                }
            27 -> playerl(FaceAnim.HALF_GUILTY, "I'd never have guessed.").also { stage++ }
            28 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Well, of course. You humans know nothing! Anything else?",
                ).also { stage = 4 }
            29 -> npcl(FaceAnim.OLD_NORMAL, "Ah, made from the finest thread in all the land.").also { stage++ }
            30 -> playerl(FaceAnim.HALF_GUILTY, "And that is?").also { stage++ }
            31 -> npcl(FaceAnim.OLD_NORMAL, "Gnome hair.").also { stage++ }
            32 -> playerl(FaceAnim.HALF_GUILTY, "You're kidding, right?").also { stage++ }
            33 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I have no time for jokes. It's a serious matter, we go through plenty of nets every day.",
                ).also {
                    stage++
                }
            34 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "So that's why you don't see many Gnomes with long hair?",
                ).also { stage++ }
            35 -> npcl(FaceAnim.OLD_NORMAL, "Exactly.").also { stage++ }
            36 -> playerl(FaceAnim.HALF_GUILTY, "The world makes sense at last.").also { stage++ }
            37 -> npcl(FaceAnim.OLD_NORMAL, "Anything else?").also { stage = 4 }
            38 -> npcl(FaceAnim.OLD_NORMAL, "Well of course, like what?").also { stage++ }

            39 ->
                options(
                    "How about in passing?",
                    "How about teamwork?",
                    "What about my personal physique?",
                    "Any information would be appreciated!",
                ).also { stage++ }

            40 ->
                when (buttonId) {
                    1 -> player("How about in passing?").also { stage++ }
                    2 -> player("How about teamwork?").also { stage = 48 }
                    3 -> player("What about my personal physique?").also { stage = 51 }
                    4 -> player("Any information would be appreciated!").also { stage = 59 }
                }

            41 -> npcl(FaceAnim.OLD_NORMAL, "There's the good old one-two tactic.").also { stage++ }
            42 -> playerl(FaceAnim.HALF_GUILTY, "And that is?").also { stage++ }
            43 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "You don't know anything do you? You pass the ball onto a team mate, giving him all the attention of your opposition, and giving you the chance to run further ahead.",
                ).also {
                    stage++
                }
            44 -> playerl(FaceAnim.HALF_GUILTY, "Then what?").also { stage++ }
            45 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Well, he passes back to you and you've made an advantage. It's useful in a variety of situations!",
                ).also {
                    stage++
                }
            46 -> playerl(FaceAnim.HALF_GUILTY, "I'll remember that one.").also { stage++ }
            47 -> npcl(FaceAnim.OLD_NORMAL, "Anything else?").also { stage = 39 }
            48 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Well, don't forget that you can block the opposition whilst a team mate has the ball. Very useful if you want to stop or slow down anyone approaching him.",
                ).also {
                    stage++
                }
            49 -> playerl(FaceAnim.HALF_GUILTY, "Block for team mates. Got it.").also { stage++ }
            50 -> npcl(FaceAnim.OLD_NORMAL, "Anything else?").also { stage = 39 }
            51 -> npcl(FaceAnim.OLD_NORMAL, "You'd look better if you were shorter.").also { stage++ }
            52 -> playerl(FaceAnim.HALF_GUILTY, "I mean in regards to scoring more goals!").also { stage++ }
            53 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I see. Your agility is important if you want to avoid being tackled, so that would be a good attribute to improve.",
                ).also {
                    stage++
                }
            54 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Also many Gnomes say that being able to attack well with a ranged weapon makes you better at scoring goals.",
                ).also {
                    stage++
                }
            55 -> playerl(FaceAnim.HALF_GUILTY, "I suppose I should go away and work on those skills.").also { stage++ }
            56 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Yes, although playing the game itself will improve these skills too.",
                ).also { stage++ }
            57 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Also you should remember that your distance from the goal affects your chance of scoring, so get close enough and your ranged ability won't matter as much.",
                ).also {
                    stage++
                }
            58 -> npcl(FaceAnim.OLD_NORMAL, "Anything else?").also { stage = 39 }
            59 -> npcl(FaceAnim.OLD_NORMAL, "Hmm, some general facts?").also { stage++ }
            60 -> playerl(FaceAnim.HALF_GUILTY, "Please!").also { stage++ }
            61 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "OK. Have you noticed the different colour clothes for your opposing Gnomes?",
                ).also {
                    stage++
                }
            62 -> playerl(FaceAnim.HALF_GUILTY, "Nope.").also { stage++ }
            63 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "The colours correspond to their skill level. The red ones are the professionals. They've been playing since before you were born!",
                ).also {
                    stage++
                }
            64 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "The orange ones are in the middle of their careers, whereas the yellow players know very little. Just like you!",
                ).also {
                    stage++
                }
            65 -> playerl(FaceAnim.HALF_GUILTY, "Thanks... Anything else?").also { stage++ }
            66 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "It's always good to get a group of friends to play with you. That way you can score goals more easily, and if you take it in turns to score the goals you'll all reap the rewards.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            67 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "You mean you've never heard of Speedy Sam The Striker or Norda 'Steal Arms' van Druman?",
                ).also {
                    stage++
                }
            68 -> playerl(FaceAnim.HALF_GUILTY, "Afraid not.").also { stage++ }
            69 -> npcl(FaceAnim.OLD_NORMAL, "Not even Great Thunder Legs MacKnee?").also { stage++ }
            70 -> playerl(FaceAnim.HALF_GUILTY, "Nope.").also { stage++ }
            71 -> npcl(FaceAnim.OLD_NORMAL, "Well, they're all richer than you'll ever be!").also { stage++ }
            72 -> playerl(FaceAnim.HALF_GUILTY, "Good for them.").also { stage++ }
            73 -> npcl(FaceAnim.OLD_NORMAL, "Indeed, anything else?").also { stage = 4 }

            74 -> npcl(FaceAnim.OLD_NORMAL, "I beg your pardon?").also { stage++ }
            75 -> player(FaceAnim.HALF_GUILTY, "Erm... thanks, catch you later.").also { stage = END_DIALOGUE }

            100 -> player("Yes, I am!").also { stage++ }
            101 -> npcl(FaceAnim.OLD_NORMAL, "Here is your challenge. Good luck!").also { stage++ }
            102 -> {
                end()
                if (removeItem(player, clue)) {
                    sendMessage(player, "The coach has given you a challenge scroll!")
                    sendItemDialogue(player, clue + 1, "The coach has given you a challenge scroll!")
                    setAttribute(player, "/save:clue:gnome-coach", true)
                    addItem(player, (clue + 1))
                } else {
                    npcl(FaceAnim.OLD_NORMAL, "I beg your pardon?").also { stage = 75 }
                }
            }

            103 -> {
                sendInputDialogue(player, true, "Enter amount:") { value: Any ->
                    if (value == 11) {
                        end()
                        removeAttribute(player, "clue:gnome-coach")
                        npcl(FaceAnim.OLD_HAPPY, "Spot on!")
                    } else {
                        npcl(FaceAnim.OLD_NORMAL, "How can that be? Go check again!").also { stage = END_DIALOGUE }
                    }
                }
            }
        }

        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GnomeCoachDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GNOME_COACH_2802)
    }
}
