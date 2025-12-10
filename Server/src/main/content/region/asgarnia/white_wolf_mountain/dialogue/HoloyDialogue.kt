package content.region.asgarnia.white_wolf_mountain.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Holoy dialogue.
 */
@Initializable
class HoloyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npcl(FaceAnim.OLD_DEFAULT, "Oh, hello.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "Hello, what's in those boxes?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "Crossbows, are you interested?").also { stage++ }
            2 -> playerl(FaceAnim.NEUTRAL, "Maybe, are they any good?").also { stage++ }
            3 -> npcl(FaceAnim.OLD_DEFAULT, "Are they any good?! They're dwarven engineering at its best!").also { stage++ }
            4 -> showTopics(
                Topic(FaceAnim.FRIENDLY, "How do I make one for myself?", 5),
                Topic(FaceAnim.HALF_THINKING, "What about magic logs?", 20),
                Topic(FaceAnim.FRIENDLY, "What about ammo?", 30),
                Topic(FaceAnim.NEUTRAL, "Thanks for telling me. Bye!", END_DIALOGUE)
            )
            5 -> npcl(FaceAnim.OLD_DEFAULT, "Well, firstly you'll need to chop yourself some wood, then use a knife on the wood to whittle out a nice crossbow stock like these here.").also { stage++ }
            6 -> playerl(FaceAnim.HAPPY, "Wood fletched into stock... check.").also { stage++ }
            7 -> npcl(FaceAnim.OLD_DEFAULT, "Then get yourself some metal and a hammer and smith yourself some limbs for the bow, mind that you use the right metals and woods though as some wood is too light to use with some metal and vice versa.").also { stage++ }
            8 -> playerl(FaceAnim.HALF_THINKING, "Which goes with which?").also { stage++ }
            9 -> npcl(FaceAnim.OLD_DEFAULT, "Wood and Bronze as they're basic materials, Oak and Blurite, Willow and Iron, Steel and Teak, Mithril and Maple, Adamantite and Mahogany and finally Runite and Yew.").also { stage++ }
            10 -> playerl(FaceAnim.NEUTRAL, "Ok, so I have my stock and a pair of limbs... what now?").also { stage++ }
            11 -> npcl(FaceAnim.OLD_DEFAULT, "Simply take a hammer and smack the limbs firmly onto the stock. You'll then need a string, only they're not the same as normal bows.").also { stage++ }
            12 -> npcl(FaceAnim.OLD_DEFAULT, "You'll need to dry some large animal's meat to get sinew, then spin that on a spinning wheel, it's the only thing we've found to be strong enough for a crossbow.").also { stage = 4 }
            20 -> playerl(FaceAnim.HALF_THINKING, "What about magic logs?").also { stage++ }
            21 -> npcl(FaceAnim.OLD_DEFAULT, "Well.. I don't rightly know... us dwarves don't work with magic, we prefer gold and rock. Much more stable. I guess you could ask the humans at the rangers guild to see if they can do something but I don't want anything to do with it!").also { stage++ }
            22 -> player("Thanks for telling me. Bye!").also { stage = END_DIALOGUE }
            30 -> playerl(FaceAnim.HALF_ASKING, "What about ammo?").also { stage++ }
            31 -> npcl(FaceAnim.OLD_DEFAULT, "You can smith yourself lots of different bolts, don't forget to flight them with feathers like you do arrows though.").also { stage++ }
            32 -> npcl(FaceAnim.OLD_DEFAULT, "There's also the option of tipping any untipped bolt with gems then enchanting them with runes. This can have some pretty powerful effects.").also { stage++ }
            33 -> playerl(FaceAnim.SAD, "Oh my poor bank, how will I store all those?!").also { stage++ }
            34 -> npcl(FaceAnim.OLD_DEFAULT, "Find Hirko in Keldagrim, he also sells crossbow parts and I'm sure he has something you can use to store bolts in.").also { stage++ }
            35 -> player("Thanks for the info.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = HoloyDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.HOLOY_4559)
}
