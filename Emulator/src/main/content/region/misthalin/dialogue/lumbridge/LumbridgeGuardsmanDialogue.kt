package content.region.misthalin.dialogue.lumbridge

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomUtils
import org.rs.consts.NPCs

@Initializable
class LumbridgeGuardsmanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc == null) return false
        val randomGreetings = listOf("Greetings.", "Good day.", "Howdy.", "Salutations!", "Nice to meet you.")
        player(FaceAnim.FRIENDLY, RandomUtils.randomChoice(randomGreetings))
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                val randomResponse =
                    listOf("Well met, adventurer.", "Well, hello there.", "Good day to you, adventurer.")
                npcl(FaceAnim.FRIENDLY, RandomUtils.randomChoice(randomResponse)).also { stage++ }
            }

            1 ->
                options(
                    "Tell me more about the Lumbridge Guardsmen.",
                    "What is there to do around here?",
                    "Tell me about Lumbridge.",
                    "What are you guarding?",
                    "Bye.",
                ).also {
                    stage++
                }

            2 ->
                when (buttonId) {
                    1 -> {
                        val randomDialogue =
                            listOf(
                                "I spoke to a few people who asked me about signing up for the guardsmen. To be asked, you need to be a true local. I mean, when the call to arms was raised, our troops were waiting here, ready for action.",
                                "I won't pretend that we're an elite fighting force, but we know how to work with the castle's defences. That means just a few of us can hold a fairly strong defence, if we ever need to again.",
                                "The Guardsmen are mainly local farmers who come to the town's defence in hours of need. We were here and ready, for example, when Saradomin and Zamorak fought the Battle of Lumbridge.",
                            )
                        npcl(FaceAnim.FRIENDLY, RandomUtils.randomChoice(randomDialogue)).also { stage = 1 }
                    }

                    2 -> {
                        val randomDialogue =
                            listOf(
                                "The battlefield is not the only threat to Lumbridge. Goblins have invaded to the east, and we could use your help to keep the numbers down!",
                                "Although the battle is over, the problems of the world have not gone away. There are many people who need help nearby. Perhaps Xenia or Explorer Jack have some work!",
                                "If you want to train your creative skills, there are trees to cut, or you could collect leather from the cow fields to the east.",
                            )
                        npcl(FaceAnim.FRIENDLY, RandomUtils.randomChoice(randomDialogue)).also { stage = 1 }
                    }

                    3 -> {
                        val randomDialogue =
                            listOf(
                                "Lumbridge used to be a safe haven where you could find your feet. It is safe again now, but I wonder if we will ever recover what we've lost.",
                                "It used to be much nicer here, before the Battle of Lumbridge. Sure, we had goblins raiding from time to time, but they're easy compared to Zamorakian demons!",
                                "Lumbridge was devastated by the battle between Zamorak and Saradomin. I'm glad it's over, and we have a chance to rebuild.",
                            )
                        npcl(FaceAnim.FRIENDLY, RandomUtils.randomChoice(randomDialogue)).also { stage = 1 }
                    }

                    4 -> {
                        val randomDialogue =
                            listOf(
                                "We guard the castle against any resurgence in fighting, such as the Battle of Lumbridge. We must always be ready, especially in these troubled times.",
                                "We work for the safety of the people and the Duke, and must be vigilant at all times against potential threats, be they acts of god or goblin invasion.",
                                "Although we are in a time of relative peace, the Battle of Lumbridge taught us that we always need to be prepared for any eventuality.",
                            )
                        npcl(FaceAnim.FRIENDLY, RandomUtils.randomChoice(randomDialogue)).also { stage = 1 }
                    }

                    5 -> playerl(FaceAnim.FRIENDLY, "Bye.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LumbridgeGuardsmanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.GUARDSMAN_BRAWN_7867,
            NPCs.GUARDSMAN_BRAWN_7887,
            NPCs.GUARDSMAN_DANTE_7865,
            NPCs.GUARDSMAN_DANTE_7885,
            NPCs.GUARDSMAN_DESHAWN_7866,
            NPCs.GUARDSMAN_DESHAWN_7886,
            NPCs.GUARDSMAN_PAZEL_7889,
            NPCs.GUARDSMAN_PAZEL_7903,
            NPCs.GUARDSMAN_PEALE_7890,
            NPCs.GUARDSMAN_PEALE_7904,
        )
    }
}
