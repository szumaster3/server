package content.region.asgarnia.dialogue.taverley

import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SylasDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!hasRequirement(player, Quests.GRIM_TALES)) {
            player("Hello!")
        } else {
            player("Hello there. My name's ${player.username}.").also { stage = 2 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.HALF_GUILTY, "Can't talk! Things to collect, magic beans to plant!").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "Uh...okay, goodbye.").also { stage = END_DIALOGUE }

            2 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ahhh, good day young adventurer. A good day indeed! Sylas is the name. It is lucky for you that you met me.",
                ).also { stage++ }

            3 -> playerl(FaceAnim.HALF_ASKING, "Why, what do you mean?").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well it just so happens, Player that I have something you simply must have!",
                ).also { stage++ }

            5 -> playerl(FaceAnim.HALF_ASKING, "Really? What is it?").also { stage++ }
            6 -> npcl(FaceAnim.FRIENDLY, "Beans, my friend! Beans!").also { stage++ }
            7 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Why would I want beans exactly? I mean, beans are good, of course, but I'm pretty sure I can get them anywhere.",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ahhh, but not beans like this, young Player. No, not at all! There ‘magic' beans.",
                ).also { stage++ }

            9 -> playerl(FaceAnim.HALF_ASKING, "Ok then. So what do they do exactly?").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah excellent question! Excellent question indeed! What do they do? What do they do? Yes, yes! A most excellent question!",
                ).also { stage++ }

            11 -> playerl(FaceAnim.FRIENDLY, "O...kay. So, are you going to tell me what they do?").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You must plant them in the ground. Water them well. Then the magic will work, it's...er...magic...yes! Plant them, Player.",
                ).also { stage++ }

            13 -> playerl(FaceAnim.HALF_ASKING, "So, can I have the beans then?").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "All in good time, my excitable young friend. All in good time. Few things in this world are free, you know, and beans - magical or otherwise - are no exception!",
                ).also { stage++ }

            15 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "So, how much gold do you want for the beans, if I decide to buy them?",
                ).also { stage++ }

            16 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah, well you see now I am a collector of rare and unusual trinkets and objects. Not much in value, but very important to me.",
                ).also { stage++ }

            17 -> playerl(FaceAnim.FRIENDLY, "Oh, I see. Ok then.").also { stage++ }
            18 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Right, well I think that for the magic beans, I will need something magical back and I know just the right items that would help my collection.",
                ).also { stage++ }

            19 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Ok, so if I get these items, you will trade them for the beans. Is that right?",
                ).also { stage++ }

            20 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes! Excellent! You do catch on quick! I can see that we will become great business partners indeed. Oh, yes. Yes indeed! Hmm-ha! Yes.",
                ).also { stage++ }

            21 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "What items do you need me to find then - assuming I still want those beans of course. Not that do, but I might.",
                ).also { stage++ }

            22 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, there are two. The first item should be nice and simple for you to get for me. I want a griffin feather. Now, this is not just any feather, this one is special.",
                ).also { stage++ }

            23 -> playerl(FaceAnim.HALF_ASKING, "What's so special about it?").also { stage++ }
            24 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "There is a griffin names Grimgnash who lives in a nest on one of the peaks of White Wolf Mountain. It is one of his feathers that I want. ",
                ).also { stage++ }

            25 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "But although this task should be easy, you must be careful Player. Grimgnash has very bad temper and even sharper claws!",
                ).also { stage++ }

            26 -> playerl(FaceAnim.FRIENDLY, "Ok, thanks for the advice.").also { stage++ }

            27 ->
                options(
                    "What do you need the feather for?",
                    "How do I find Grimgnash again?",
                    "What's the other item I need to find?",
                    "Do you have any weird items in your collection?",
                    "I should be off, I think.",
                ).also { stage++ }

            28 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "What do you need the feather for?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_ASKING, "How do I find Grimgnash again?").also { stage = 34 }
                    3 -> playerl(FaceAnim.HALF_ASKING, "What's the other item I need to find?").also { stage = 39 }
                    4 ->
                        playerl(FaceAnim.HALF_ASKING, "Do you have any weird items in your collection?").also {
                            stage = 44
                        }

                    5 -> playerl(FaceAnim.HALF_ASKING, "I should be off, I think.").also { stage = 55 }
                }

            29 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I told you, player. It is for my collection of magical items and trinkets, weird and wonderful thereof.",
                ).also { stage++ }

            30 -> playerl(FaceAnim.HALF_ASKING, "Thereof?").also { stage++ }
            31 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, I was quite happy with that word. I think it went rather well in that last sentence, don't you?",
                ).also { stage++ }

            32 -> playerl(FaceAnim.FRIENDLY, "I guess. If you say so.").also { stage++ }
            33 -> npcl(FaceAnim.HALF_ASKING, "Yes, Excellent! Now...er...was there anything else?").also { stage = 27 }

            34 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "When he's not chewing on some poor soul's bones or raining terror on an unsuspecting village, he can be found in his nest, atop White Wolf Mountain.",
                ).also { stage++ }

            35 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Excuse me...say what now? He chews people's bones? I thought you said this was an easy task?",
                ).also { stage++ }

            36 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Oh, I daresay he has to eat sometimes. But I don't think he'd take too much fancy for you. Not much meat on you at all. Oh dear me, no!",
                ).also { stage++ }

            37 -> playerl(FaceAnim.FRIENDLY, "You know, I'm not sure that was even a compliment.").also { stage++ }
            38 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "I'm sure it was meant to be! Don't worry, Player, you'll be fine. Now was there anything else?",
                ).also { stage = 27 }

            39 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Right, well, the second item is this. I know of a dwarf named Rupert the Beard who has a very fine dwarven helmet. I'd like you to see if you can obtain it for me.",
                ).also { stage++ }

            40 -> playerl(FaceAnim.HALF_ASKING, "Ok. Where can I find him?").also { stage++ }
            41 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Travel north-east from here and between the Goblin Village and Ice Mountain you will find Rupert in a tower there.",
                ).also { stage++ }

            42 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Ok, that seems simple enough. I'll get the helmet for you as well.",
                ).also { stage++ }

            43 -> npcl(FaceAnim.ASKING, "Excellent! Yes, yes. Now was there anything else?").also { stage = 27 }

            44 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Haha! Weird items? Oh yes. Yes indeed! Yes. Many items. Many items. Yes.",
                ).also { stage++ }

            45 -> playerl(FaceAnim.FRIENDLY, "Ummm...I see. Well, can you tell me about any of them?").also { stage++ }
            46 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Hmm. Well let's see now. I have the second eye of a mutant two-eyed cyclops. That was a rare find indeed! And at a tidy price too!",
                ).also { stage++ }

            47 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "A two-eyed cyclops? Uh...wouldn't that be a normal giant perhaps?",
                ).also { stage++ }

            48 ->
                npcl(
                    FaceAnim.ASKING,
                    "Whatever do you mean? How many mutant two-eyed cyclops do you think are out there? Not many, I'd wager!",
                ).also { stage++ }

            49 -> playerl(FaceAnim.FRIENDLY, "Umm...no, I guess not. Well, what else do you have?").also { stage++ }
            50 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah, well I'm glad you asked. My most prized possession to date: Through my connections, I managed to acquire a horn-less Unicorn!",
                ).also { stage++ }

            51 -> playerl(FaceAnim.HALF_ASKING, "Really? That's amazing! A Unicorn without a horn?").also { stage++ }
            52 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes. Yes indeed! Indeed yes! And I'm very proud of it, too! It was an exceptionally lucky find, I must say!",
                ).also { stage++ }

            53 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "I expect so. Well, on to other things for now then, I guess.",
                ).also { stage++ }

            54 -> npcl(FaceAnim.ASKING, "Of course. Was there anything else you wanted?").also { stage = 27 }

            55 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Excellent, excellent! Best of luck then, my friend. I'll be right here waiting for you to return.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SylasDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SYLAS_5987)
    }
}
