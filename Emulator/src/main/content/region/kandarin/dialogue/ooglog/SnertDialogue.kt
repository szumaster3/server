package content.region.kandarin.dialogue.ooglog

import core.api.hasRequirement
import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SnertDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.CHILD_FRIENDLY, "Hi, human.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val rand = RandomFunction.random(0, 8)
        if (hasRequirement(player, Quests.AS_A_FIRST_RESORT)) {
            when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Hi, ogre.").also { stage++ }
                1 -> options("How are you today?", "Can you tell me about this copper-coloured pool?").also { stage++ }
                2 ->
                    when (buttonId) {
                        1 -> playerl(FaceAnim.HALF_ASKING, "How are you today, little ogre?").also { stage++ }
                        2 ->
                            playerl(FaceAnim.HALF_ASKING, "Can you tell me about this copper-coloured pool?").also {
                                stage =
                                    7
                            }
                    }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "Me like pie!").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Doesn't everyone?").also { stage++ }
                5 -> npcl(FaceAnim.CHILD_NORMAL, "Pie! Pie! Pie! Pie! Pie!").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "Yes, dear, pie.").also { stage = END_DIALOGUE }
                7 -> npcl(FaceAnim.CHILD_NORMAL, "Yeah, it red!").also { stage++ }
                8 -> playerl(FaceAnim.FRIENDLY, "Yes, I can see that.").also { stage++ }
                9 -> npcl(FaceAnim.CHILD_NORMAL, "Then why you ask, silly human?").also { stage++ }
                10 ->
                    playerl(
                        FaceAnim.FRIENDLY,
                        "Well, it's hard to explain. I just sense a strange energy coming from this pool. Do you know anything about it?",
                    ).also {
                        stage++
                    }
                11 ->
                    npcl(
                        FaceAnim.CHILD_NORMAL,
                        "Oh, well, it called de 'Bandos pool' round here. It s'pposed to be special-special. It hard to explain to human creature like you. Maybe you need talk to auntie Seegud. She explain good about these things",
                    ).also {
                        stage++
                    }
                12 -> end()
            }
        } else {
            when (rand) {
                0 ->
                    when (stage) {
                        0 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
                        1 -> npcl(FaceAnim.CHILD_NORMAL, "Can I have some shiny pretties, human?").also { stage++ }
                        2 ->
                            playerl(
                                FaceAnim.HALF_ASKING,
                                "What makes you think I'd give my shiny pretties to you?",
                            ).also { stage++ }
                        3 ->
                            npcl(
                                FaceAnim.CHILD_NORMAL,
                                "That skinny lady at de bank say visitors bring lots of shiny pretties with them when they come visit Oo'glog. Me want my share!",
                            ).also {
                                stage++
                            }
                        4 ->
                            playerl(
                                FaceAnim.HALF_ASKING,
                                "Ah, you see, I've given your share to that skinny lady at the bank; you'll have to ask her for it yourself. Make sure you ask really loudly and growl at her every time she uses a word you don't understand, okay?",
                            ).also {
                                stage++
                            }
                        5 ->
                            npcl(
                                FaceAnim.CHILD_NORMAL,
                                "Yay! Thanks human. I'm gonna get my shiny pretties, even if I have to growl all afternoon.",
                            ).also {
                                stage++
                            }
                        6 -> end()
                    }

                1 ->
                    when (stage) {
                        0 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
                        1 ->
                            npcl(
                                FaceAnim.CHILD_NORMAL,
                                "Fine, please and thank you very much, madam sir!",
                            ).also { stage++ }
                        2 -> playerl(FaceAnim.HALF_ASKING, "You're very...polite.").also { stage++ }
                        3 ->
                            npcl(
                                FaceAnim.CHILD_NORMAL,
                                "Me know! Me practising! Balnea say I can have job in Customer Rations when I grow up if me polite enough.",
                            ).also {
                                stage++
                            }
                        4 -> playerl(FaceAnim.HALF_ASKING, "Don't you mean 'Customer Relations'?").also { stage++ }
                        5 ->
                            npcl(
                                FaceAnim.CHILD_NORMAL,
                                "Blechh! Dat not sound near as tasty as 'Customer Rations'. Me not sure me want de job after all.",
                            ).also {
                                stage++
                            }
                        6 -> end()
                    }

                2 ->
                    when (stage) {
                        0 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
                        1 -> npcl(FaceAnim.CHILD_NORMAL, "Hey, human. What did you bring me?").also { stage++ }
                        2 ->
                            playerl(
                                FaceAnim.HALF_ASKING,
                                "Hmm, let me think carefully about this. Oh, yes, I remember, now! Absolutely nothing.",
                            ).also {
                                stage++
                            }
                        3 -> npcl(FaceAnim.CHILD_NORMAL, "Aw, shucks.").also { stage++ }
                        4 -> end()
                    }

                3 ->
                    when (stage) {
                        0 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
                        1 ->
                            npcl(
                                FaceAnim.CHILD_NORMAL,
                                "How does it feel to be so puny wee small, human?",
                            ).also { stage++ }
                        2 ->
                            playerl(
                                FaceAnim.HALF_ASKING,
                                "Oh, I dunno. How does it feel to be so incredibly dense?",
                            ).also { stage++ }
                        3 -> npcl(FaceAnim.CHILD_NORMAL, "Uhh...what dat s'pposed to mean?").also { stage++ }
                        4 -> playerl(FaceAnim.HALF_ASKING, "Never mind.").also { stage++ }
                        5 -> end()
                    }

                4 ->
                    when (stage) {
                        0 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
                        1 -> npcl(FaceAnim.CHILD_NORMAL, "Me like pie!").also { stage++ }
                        2 -> playerl(FaceAnim.HALF_ASKING, "Doesn't everyone?").also { stage++ }
                        3 -> npcl(FaceAnim.CHILD_NORMAL, "Pie! Pie! Pie! Pie! Pie!").also { stage++ }
                        4 -> playerl(FaceAnim.HALF_ASKING, "Yes, dear, pie.").also { stage++ }
                        5 -> end()
                    }

                5 ->
                    when (stage) {
                        0 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
                        1 -> npcl(FaceAnim.CHILD_NORMAL, "Me wanna go visit Fycie 'n Bugs!").also { stage++ }
                        2 -> playerl(FaceAnim.HALF_ASKING, "Yes, they're both delightful individuals.").also { stage++ }
                        3 -> npcl(FaceAnim.CHILD_NORMAL, "Will you take me to see dem, human?").also { stage++ }
                        4 ->
                            playerl(
                                FaceAnim.HALF_ASKING,
                                "Didn't your mother ever teach you not to talk to strangers?",
                            ).also { stage++ }
                        5 -> end()
                    }

                6 ->
                    when (stage) {
                        0 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
                        1 -> npcl(FaceAnim.CHILD_NORMAL, "Me wants CHOMPY for dinner!").also { stage++ }
                        2 -> playerl(FaceAnim.HALF_ASKING, "Me hopes you GETS chompy for dinner!").also { stage++ }
                        3 ->
                            npcl(
                                FaceAnim.CHILD_NORMAL,
                                "What you talk weird like dat for, human? You sound silly!",
                            ).also { stage++ }
                        4 -> end()
                    }

                7 ->
                    when (stage) {
                        0 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
                        1 ->
                            npcl(
                                FaceAnim.CHILD_NORMAL,
                                "Not so very good. Me just drop me lunch in de pool.",
                            ).also { stage++ }
                        2 ->
                            playerl(
                                FaceAnim.HALF_ASKING,
                                "Ew...I think I can see it floating over there.",
                            ).also { stage++ }
                        3 ->
                            npcl(
                                FaceAnim.CHILD_NORMAL,
                                "Oh, thank you human! I have it as a snack next time I go for swim!",
                            ).also { stage++ }
                        4 -> end()
                    }

                8 ->
                    when (stage) {
                        0 -> playerl(FaceAnim.HALF_ASKING, "Hi, ogre! How are you today, little ogre?").also { stage++ }
                        1 ->
                            npcl(
                                FaceAnim.CHILD_NORMAL,
                                "You very cute, little creature. Me want to have a human as a pet. What you doing, human? You busy? You want be my little pet human creature?",
                            ).also {
                                stage++
                            }
                        2 -> playerl(FaceAnim.HALF_ASKING, "Uh...I think I'm busy at the moment.").also { stage++ }
                        3 -> npcl(FaceAnim.CHILD_NORMAL, "Pwetty please?").also { stage++ }
                        4 ->
                            playerl(
                                FaceAnim.HALF_ASKING,
                                "Look, sorry to disappoint, but this isn't going to happen.",
                            ).also { stage++ }
                        5 -> npcl(FaceAnim.CHILD_NORMAL, "Aw, you no fun, human.").also { stage++ }
                        6 -> end()
                    }
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SnertDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.SNERT_7067,
            NPCs.THUDDLEY_7072,
            NPCs.SNARRK_7070,
            NPCs.GRUBB_7075,
            NPCs.GRUNTHER_7076,
            NPCs.IRK_7071,
        )
    }
}
