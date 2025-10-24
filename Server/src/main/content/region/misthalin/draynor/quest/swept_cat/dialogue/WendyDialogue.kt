package content.region.misthalin.draynor.quest.swept_cat.dialogue

import content.global.skill.summoning.objectpets.Pets
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Wendy dialogue.
 */
@Initializable
class WendyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.HAPPY, "Oh, hello.")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                if (!isQuestComplete(player, Quests.SWEPT_AWAY))
                    options("Who are you?", "What are you doing?", "You look a little lost.")
                else
                    options(
                        "Who are you?",
                        "What are you doing?",
                        "You look a little lost.",
                        "Let's talk about purple cats.",
                        "Can you remove the purple from a cat?"
                    )
                stage = 1
            }

            1 -> when (buttonId) {
                1 -> { playerl(FaceAnim.HALF_ASKING, "Who are you?"); stage = 10 }
                2 -> { playerl(FaceAnim.HALF_ASKING, "What are you doing?"); stage = 20 }
                3 -> { playerl(FaceAnim.HALF_ASKING, "If you don't mind my saying so, you look a little lost."); stage = 30 }
                4 -> { playerl(FaceAnim.HALF_ASKING, "Can you make my cat purple?"); stage = 60 }
                5 -> { playerl(FaceAnim.HALF_ASKING, "Can you remove the purple from a cat?"); stage = 70 }
            }

            10 -> {
                npcl(null, "Me? I'm Wendy. I'm learning the ways of witchcraft from Maggie here. She's very clever, you know...and very patient.")
                stage = 0
            }

            20 -> {
                npcl(FaceAnim.FRIENDLY, "I'm travelling with Maggie, trying to learn the ways of witchcraft.")
                stage++
            }

            21 -> { player("And what are you learning at the moment?"); stage++ }
            22 -> { npcl(FaceAnim.FRIENDLY, "I'm learning all about - um, hold on...let me just think..."); stage++ }
            23 -> { npcl(FaceAnim.FRIENDLY, "It was something really quite fascinating, I'm sure..."); stage++ }
            24 -> { npcl(FaceAnim.THINKING, "Uh..."); stage++ }
            25 -> { npcl(FaceAnim.THINKING, "Hmm..."); stage++ }
            26 -> { player("Look, don't worry about it, really."); stage++ }
            27 -> {
                npcl(FaceAnim.FRIENDLY, "Sorry, I can't quite recall at the moment, but I'm sure it will come back to me, sooner or later.")
                stage = 0
            }

            30 -> {
                npcl(FaceAnim.FRIENDLY, "It's funny - I get that a lot! I don't think I'm lost...not at the moment, at least.")
                stage++
            }
            31 -> {
                npcl(FaceAnim.FRIENDLY, "Keeping track of where one is isn't a problem, is it? I think it's more that I keep forgetting where I'm going.")
                stage++
            }
            32 -> {
                options("What do you mean?", "So where are you going?")
                stage = 33
            }
            33 -> when (buttonId) {
                1 -> { playerl(FaceAnim.HALF_ASKING, "What do you mean?"); stage = 40 }
                2 -> { playerl(FaceAnim.HALF_ASKING, "So where are you going?"); stage = 50 }
            }

            40 -> { npcl(FaceAnim.FRIENDLY, "Well, it's just that there are so many interesting things to do and see, really!"); stage++ }
            41 -> { npcl(FaceAnim.FRIENDLY, "It's all well and good to make plans for myself, but I always seem to get distracted along the way."); stage++ }
            42 -> { playerl(FaceAnim.HALF_ASKING, "Distracted? By what?"); stage++ }
            43 -> { npcl(null, "Oh, well, you know. A pretty flower. A little impling flying by. A fluffy cat that looks like it needs a scratch under its chin."); stage++ }
            44 -> { npcl(null, "I see things like that and my best-laid plans just seem to fly right out of my head!"); stage++ }
            45 -> { player("Hmm, I can see how they might."); stage = 0 }

            50 -> { npcl(null, "Well, Maggie has been teaching me the basics of witchcraft."); stage++ }
            51 -> { npcl(null, "I'm sure she sent me out to collect something but, come to think of it, I can't quite remember what it was."); stage++ }
            52 -> { playerl(null, "Well, perhaps it was something for a potion? Or a spell?"); stage++ }
            53 -> { npcl(null, "Oo, that sounds possible! Do you remember what it might have been?"); stage++ }
            54 -> { playerl(null, "Well, given that I wasn't there for the conversation, I have no way of knowing that, I'm afraid."); stage++ }
            55 -> { npcl(null, "Oh. Sad."); stage++ }
            56 -> { npcl(null, "Wait, what were we talking about?"); stage = 0 }

            60 -> {
                val familiar = player?.familiarManager?.familiar
                val inv = player?.inventory?.toArray() ?: arrayOfNulls<Item>(0)
                val petID = inv.firstOrNull { it != null && Pets.forId(it.id) != null }

                when {
                    familiar == null && petID == null -> npcl(null, "Of course. Just bring your cat to me and let me hold her for a moment. I'll sort her out in no time.").also { stage = 100 }
                    familiar != null -> npcl(null, "Of course. Just pick up your cat and hand her to me so that I can take a closer look.").also { stage = 100 }
                    else -> npcl(null, "Of course. Just hand your cat to me so that I can take a closer look.").also { stage = 100 }
                }
            }

            70 -> {
                npcl(FaceAnim.AFRAID, "I'm afraid that once the spell is cast, it's cast for good. I haven't figured out how to undo the purple yet.")
                stage++
            }

            71 -> { player(FaceAnim.HALF_CRYING, "Ah, right. Hmm."); stage = 100 }
            100 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue = WendyDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.WENDY_8201)
}