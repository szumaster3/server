package content.region.misc.quest.lostcity.dialogue

import core.api.quest.getQuestStage
import core.api.quest.startQuest
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class WarriorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        when (getQuestStage(player, Quests.LOST_CITY)) {
            10 ->
                playerl(
                    FaceAnim.THINKING,
                    "So let me get this straight: I need to search the trees around here for a leprechaun; and then when I find him, he will tell me where this 'Zanaris' is?",
                ).also { stage = 1000 }

            20, 21 -> playerl(FaceAnim.HAPPY, "Have you found anything yet?").also { stage = 2000 }
            100 ->
                playerl(
                    FaceAnim.HAPPY,
                    "Hey, thanks for all the information. It REALLY helped me out in finding the lost city of Zanaris and all.",
                ).also { stage = 3000 }

            else -> npcl(FaceAnim.NEUTRAL, "Hello there traveller.").also { stage = 1 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                showTopics(
                    Topic(FaceAnim.THINKING, "What are you camped out here for?", 100),
                    Topic(FaceAnim.HALF_THINKING, "Do you know any good adventures I can go on?", 101),
                )

            2 ->
                showTopics(
                    Topic(FaceAnim.ASKING, "Please tell me.", 200),
                    Topic(FaceAnim.ANGRY, "I don't think you've found a good adventure at all!", 250),
                )

            3 ->
                showTopics(
                    Topic(FaceAnim.ASKING, "Who's Zanaris?", 301),
                    Topic(FaceAnim.ASKING, "What's Zanaris?", 302),
                    Topic(FaceAnim.ASKING, "What makes you think it's out here?", 300),
                )

            4 ->
                showTopics(
                    Topic(FaceAnim.ASKING, "If it's hidden how are you planning to find it?", 400),
                    Topic(FaceAnim.LAUGH, "There's no such thing!", 450),
                )

            5 -> options("Please tell me.", "Looks like you don't know either.").also { stage = 500 }
            6 -> playerl(FaceAnim.HAPPY, "So a leprechaun knows where Zanaris is eh?").also { stage = 600 }
            7 -> playerl(FaceAnim.HAPPY, "Thanks for the help!").also { stage = 700 }
            8 ->
                end().also {
                    startQuest(player, Quests.LOST_CITY)
                }

            100 ->
                npcl(
                    FaceAnim.HAPPY,
                    "We're looking for Zanaris...GAH! I mean we're not here for any particular reason at all.",
                ).also { stage = 3 }

            101 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well we're on an adventure right now. Mind you, this is OUR adventure and we don't want to share it - find your own!",
                ).also { stage = 2 }

            200 -> npcl(FaceAnim.NEUTRAL, "No.").also { stage++ }
            201 -> playerl(FaceAnim.SAD, "Please?").also { stage++ }
            202 -> npcl(FaceAnim.ANNOYED, "No!").also { stage++ }
            203 -> playerl(FaceAnim.SAD, "PLEEEEEEEEEEEEEEEEEEEEEEEEASE???").also { stage++ }
            204 -> npcl(FaceAnim.ANGRY, "NO!").also { stage = END_DIALOGUE }
            250 ->
                npcl(
                    FaceAnim.ANGRY,
                    "Hah! Adventurers of our calibre don't just hang around in forests for fun, whelp!",
                ).also { stage++ }

            251 -> playerl(FaceAnim.THINKING, "Oh Really?").also { stage++ }
            252 -> playerl(FaceAnim.THINKING, "What are you camped out here for?").also { stage = 100 }
            300 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Don't you know of the legends that tell of the magical city, hidden in the swamp... Uh, no, you're right, we're wasting our time here.",
                ).also { stage = 4 }

            301 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Ahahahaha! Zanaris isn't a person! It's a magical hidden city filled with treasure and rich... uh, nothing. It's nothing.",
                ).also { stage = 4 }

            302 ->
                npcl(
                    FaceAnim.HALF_THINKING,
                    "I don't think we want other people competing with us to find it. Forget I said anything.",
                ).also { stage = 5 }

            400 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, we don't want to tell anyone else about that, because we don't want anyone else sharing in all that glory and treasure.",
                ).also { stage = 5 }

            450 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "When we've found Zanaris you'll... GAH! I mean, we're not here for any particular reason at all.",
                ).also { stage = 3 }

            500 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.ASKING, "Please tell me.").also { stage = 200 }
                    2 ->
                        playerl(
                            FaceAnim.THINKING,
                            "Well, it looks to ME like YOU don't know EITHER, seeing as you're all just sat around here.",
                        ).also { stage++ }
                }

            501 ->
                npcl(
                    FaceAnim.ANGRY,
                    "Of course we know! We just haven't found which tree the stupid leprechaun's hiding in yet!",
                ).also { stage++ }

            502 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "GAH! I didn't mean to tell you that! Look, just forget I said anything okay?",
                ).also { stage = 6 }

            600 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Ye.. uh, no. No, not at all. And even if he did - which he doesn't - he DEFINITELY ISN'T hiding in some tree around here. Nope, definitely not. Honestly.",
                ).also { stage = 7 }

            700 ->
                npcl(
                    FaceAnim.WORRIED,
                    "Help? What help? I didn't help! Please don't say I did, I'll get in trouble!",
                ).also { stage = 8 }

            1000 ->
                npcl(
                    FaceAnim.WORRIED,
                    "What? How did you know that? Uh... I mean, no, no you're very wrong. Very wrong, and not right at all, and I definitely didn't tell you about that at all.",
                ).also { stage = END_DIALOGUE }

            2000 ->
                npcl(
                    FaceAnim.SAD,
                    "We're still searching for Zanaris...GAH! I mean we're not doing anything here at all.",
                ).also { stage++ }

            2001 -> playerl(FaceAnim.SAD, "I haven't found it yet either.").also { stage = END_DIALOGUE }
            3000 ->
                npcl(
                    FaceAnim.SAD,
                    "Oh please don't say that anymore! If the rest of my party knew I'd helped you they'd probably throw me out and make me walk home by myself!",
                ).also { stage++ }

            3001 ->
                npcl(
                    FaceAnim.ASKING,
                    "So anyway, what have you found out? Where is the fabled Zanaris? Is it all the legends say it is?",
                ).also { stage++ }

            3002 ->
                playerl(FaceAnim.HAPPY, "You know.... I think I'll keep that to myself.").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.WARRIOR_650)
}
