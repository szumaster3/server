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
class SergeantAbramDialogue(
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
                    1 -> playerl(FaceAnim.FRIENDLY, "Tell me more about the Lumbridge Guardsmen.").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_ASKING, "What is there to do around here?").also { stage = 5 }
                    3 -> playerl(FaceAnim.NEUTRAL, "Tell me about Lumbridge.").also { stage = 8 }
                    4 -> playerl(FaceAnim.THINKING, "What are you guarding?").also { stage = 13 }
                    5 -> playerl(FaceAnim.HAPPY, "Bye.").also { stage = END_DIALOGUE }
                }

            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I spoke to a few people who asked me about joining the Lumbridge Guardsmen. To be asked, you need to be a true local.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I mean, when the call to arms is raised, you don't want your troops to be scattered across the world: they need to be waiting here, ready to spring in to action.",
                ).also {
                    stage =
                        1
                }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "If you'd like to make a bit of spending money, try speaking to the skill tutors.",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "They go through a lot of supplies, so I'm sure they'd be happy to pay you to make the supplies they need.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "If you're interested in learning to make leather armour, there's the cow field to get hides, and through the eastern tollgate you can reach the tanner and Crafting Shop.",
                ).also {
                    stage =
                        1
                }
            8 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "It used to be much nicer here, before the goblins overran the east side of town.",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You'd think that the Guardsmen would be sent to flush them out, but for every one we slay, three more appear in its place.",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "If I'm honest, I don't much care for it here. I understand why so many do enjoy the lifestyle, but I long for the city life.",
                ).also {
                    stage++
                }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "If I do well in my job here, perhaps I could move to Varrock, or Falador, and join the guards there. I suppose it's a good place to find your feet in the world.",
                ).also {
                    stage++
                }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "There's reasonable fishing in the river, and with lots of farmland around it's rare that you go hungry. Yes, there's plenty worse places you could live in than Lumbridge.",
                ).also {
                    stage =
                        1
                }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Peace, happiness, tranquillity, that sort of thing. So, let me know if you see anything upsetting. Dragons in cellars, goblins under beds, that sort of thing.",
                ).also {
                    stage++
                }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I suppose I'm guarding the castle, and the Lumbridge way of life. Although, there was once a dragon near here, and I helped defend the castle from it. That was an exciting time!",
                ).also {
                    stage =
                        1
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SergeantAbramDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.SERGEANT_ABRAM_7888,
            NPCs.SERGEANT_ABRAM_7902,
        )
    }
}
