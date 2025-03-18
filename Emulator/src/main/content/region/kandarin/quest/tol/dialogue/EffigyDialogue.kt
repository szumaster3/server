package content.region.kandarin.quest.tol.dialogue

import content.region.kandarin.quest.tol.handlers.TowerOfLifeUtils
import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.setVarbit
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class EffigyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, "Tower of Life")) {
            0 ->
                when (stage) {
                    START_DIALOGUE ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Hi. Interesting building you have here.",
                        ).also { stage++ }
                    1 -> playerl(FaceAnim.FRIENDLY, "What's it for?").also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You like it? It's simply fabulous, isn't it? A real marvel of modern design made just for my fellow alchemists and I!",
                        ).also { stage++ }

                    3 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I see some builders over there. So I assume it's not yet finished.",
                        ).also { stage++ }

                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "What a keen eye this " + (if (player.isMale) "LAD" else "LASS") +
                                " has! But imagine this: a gargantuan cylinder of expert design, incorporating an inventive inner spiral walkway to multiple planes of wondrous inhabitance!",
                        ).also { stage++ }

                    5 -> playerl(FaceAnim.FRIENDLY, "So, it's a tower.").also { stage++ }
                    6 -> npcl(FaceAnim.FRIENDLY, "Oh, no, no, no, no.").also { stage++ }
                    7 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "It's going to have a use that will change this world!",
                        ).also { stage++ }
                    8 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Or at least it would if we could ever get it finished.",
                        ).also { stage++ }
                    9 -> npcl(FaceAnim.FRIENDLY, "See those builders?").also { stage++ }
                    10 -> playerl(FaceAnim.FRIENDLY, "They don't seem to be building much.").also { stage++ }
                    11 -> npcl(FaceAnim.FRIENDLY, "Exactly").also { stage++ }
                    12 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "They've gone on strike for some petty reason about the tower being too weird. I really can't understand those men.",
                        ).also { stage++ }

                    13 -> playerl(FaceAnim.FRIENDLY, "Shame.").also { stage++ }
                    14 -> npcl(FaceAnim.FRIENDLY, "Hey! Maybe you could talk to them?").also { stage++ }
                    15 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You look like a sturdy " + (if (player.isMale) "LAD" else "LASS") + ".",
                        ).also { stage++ }

                    16 -> npcl(FaceAnim.FRIENDLY, "Maybe you could finish the work?").also { stage++ }
                    17 -> playerl(FaceAnim.FRIENDLY, "Me?").also { stage++ }
                    18 -> npcl(FaceAnim.FRIENDLY, "Come on! This is the chance of a lifetime!").also { stage++ }
                    19 -> options("Sure, why not.", "Sorry, feeling a bit ill today.").also { stage++ }
                    20 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Sure, why not.").also { stage = 21 }
                            2 ->
                                playerl(FaceAnim.FRIENDLY, "Sorry, feeling a bit ill today.").also {
                                    stage =
                                        END_DIALOGUE
                                }
                        }

                    21 -> npcl(FaceAnim.FRIENDLY, "That's fantastic! Oh, I'm so pleased.").also { stage++ }
                    22 -> playerl(FaceAnim.FRIENDLY, "Calm down. I'm not making any promises.").also { stage++ }
                    23 -> npcl(FaceAnim.FRIENDLY, "Oh, I have a good feeling about this.").also { stage++ }
                    24 -> npcl(FaceAnim.FRIENDLY, "Right").also { stage++ }
                    25 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Go have a word with Bonafido the head builder. See if you can't get him off his backside to do some work. Oh, and I'm Effigy by the way.",
                        ).also { stage++ }

                    26 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Nice to meet you Effigy, I'm ${player.username}. Hold on and I'll see what I can do!",
                        ).also { stage++ }

                    27 -> {
                        end()
                        setQuestStage(player, Quests.TOWER_OF_LIFE, 1)
                        setVarbit(player, 3337, 1, true)
                    }
                }

            3 ->
                if (getAttribute(player, TowerOfLifeUtils.TOL_TOWER_ACCESS, 0) == 1) {
                    when (stage) {
                        START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Guess what!").also { stage++ }
                        1 -> npcl(FaceAnim.FRIENDLY, "What?").also { stage++ }
                        2 -> playerl(FaceAnim.FRIENDLY, "I'm a fully-fledged builder!").also { stage++ }
                        3 -> npcl(FaceAnim.FRIENDLY, "Marvelous! So you can bring this tower to life!").also { stage++ }
                        4 ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "I'm not sure about that, but I'll take a look and see what I can do.",
                            ).also { stage++ }

                        5 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Oh, what a wondrous person you are!",
                            ).also { stage = END_DIALOGUE }
                    }
                }

            4 ->
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "I've fixed all the machinery.").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "Hurrah! Splendiferous!").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Now listen, what does it all do? Why are you-").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Years of devoted work; it's complete!").also { stage++ }
                    4 -> npcl(FaceAnim.FRIENDLY, "Hey, guys! It's finished!").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "At last! Our work is complete!").also { stage++ }
                    6 -> npcl(FaceAnim.FRIENDLY, "I can almost taste the riches!").also { stage++ }
                    7 -> npcl(FaceAnim.FRIENDLY, "To the top of the tower, fellow alchemists!").also { stage++ }
                    8 -> playerl(FaceAnim.FRIENDLY, "Wait! What about-").also { stage = END_DIALOGUE }
                }

            6 ->
                if (getAttribute(player, TowerOfLifeUtils.TOL_CUTSCENE, false)) {
                    when (stage) {
                        START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Effigy!").also { stage++ }
                        1 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "I know, I know. This is a truly dangerous creation, of all proportions.",
                            ).also { stage++ }

                        2 -> playerl(FaceAnim.FRIENDLY, "I hope you learn from this.").also { stage++ }
                        3 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "I will, I will. Next time we'll use a stronger cage.",
                            ).also { stage++ }

                        4 -> playerl(FaceAnim.FRIENDLY, "What?!").also { stage++ }
                        5 -> npcl(FaceAnim.FRIENDLY, "A bad joke.").also { stage++ }
                        6 -> playerl(FaceAnim.FRIENDLY, "I'll just ignore that.").also { stage++ }
                        7 ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "So, what about that Homunculus up there? You can't just leave it.",
                            ).also { stage++ }

                        8 -> npcl(FaceAnim.FRIENDLY, "Maybe you could go have a talk with it?").also { stage++ }
                        9 -> playerl(FaceAnim.FRIENDLY, "Why me? You created it!").also { stage++ }
                        10 -> npcl(FaceAnim.FRIENDLY, "Pleeeaaase.").also { stage++ }
                        11 -> playerl(FaceAnim.FRIENDLY, "No.").also { stage++ }
                        12 -> npcl(FaceAnim.FRIENDLY, "Pretty please with a cherry on top?").also { stage++ }
                        13 -> playerl(FaceAnim.FRIENDLY, "No way!").also { stage++ }
                        14 ->
                            npcl(
                                FaceAnim.FRIENDLY,
                                "I'll make sure you're compensated. You won't be forgotten for this!",
                            ).also { stage++ }

                        15 ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "Why do I get the feeling I'm doomed to go face that creature on my own?",
                            ).also { stage++ }

                        16 -> npcl(FaceAnim.FRIENDLY, "Good! I knew you would help us!").also { stage++ }
                        17 ->
                            playerl(
                                FaceAnim.FRIENDLY,
                                "Oh well. My mum did always tell me that experience teaches fools.",
                            ).also { stage = END_DIALOGUE }
                    }
                }

            8 ->
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Effigy, I need a word with you.").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "You've killed it?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Not quite...").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Boo.").also { stage++ }
                    4 -> npcl(FaceAnim.FRIENDLY, "On no, look!").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "Arrghhh! Have mercy!").also { stage++ }
                    6 -> npcl(FaceAnim.FRIENDLY, "You set it free. Oh, please don't hurt me!").also { stage++ }
                    7 -> npcl(FaceAnim.FRIENDLY, "Hahaha.").also { stage++ }
                    8 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Me not hurt. Now tell. What you plans? What you do with me?",
                        ).also { stage++ }

                    9 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Please, we just wanted to experiment. We wanted to create life. There's a dungeon under the tower. Long ago, we found a strange source of energy that we believe was left behind by the great Guthix. He used to",
                        ).also { stage++ }

                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "create life with such ease. We realised we should be able to use this powerful substance, but we needed to bring together our magic along with the logical construction of the builders. It seemed through our experiments that",
                        ).also { stage++ }

                    11 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "even magic needs a certain level of rules and laws to work.",
                        ).also { stage++ }

                    12 -> npcl(FaceAnim.FRIENDLY, "Bad play with. Naughty men. Go! Go, never return!").also { stage++ }
                    13 -> npcl(FaceAnim.FRIENDLY, "Right away, right away!").also { stage++ }
                    14 ->
                        npcl(FaceAnim.FRIENDLY, "Go look dungeon, ${player.username}. Please meet there.").also {
                            stage = END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.EFFIGY_5578)
}
