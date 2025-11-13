package content.region.kandarin.gnome_stronghold.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents Trainer Nacklepen dialogue.
 */
@Initializable
class TrainerNacklepenDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        showTopics(
            Topic("Who are you then?", 0, false),
            // TODO: If the player has not completed The Eyes of Glouphrie.
            Topic("What's that cute creature doing here?", 16,false)
        )
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Hurumph. Nacklepen, tortoise trainer extraordinaire.").also { stage++ }
            1 -> playerl(FaceAnim.NEUTRAL, "What's extraordinary about you?").also { stage++ }
            2 -> npcl(FaceAnim.OLD_NORMAL, "Do you know any other tortoise trainers?").also { stage++ }
            3 -> playerl(FaceAnim.NEUTRAL, "Well, no.").also { stage++ }
            4 -> npcl(FaceAnim.OLD_NORMAL, "Well there you go then.").also { stage++ }
            5 -> options(
                    "So what are you training these tortoises to do?",
                    "Why tortoises?",
                    "Where did these tortoises come from?",
                    "I've heard enough"
                ).also { stage++ }
            6 -> when (buttonId) {
                1 -> {
                    npcl(FaceAnim.OLD_NORMAL, "Tortoises are naturally pacifistic animals that eat nothing but cabbages. I am required to beat those peaceful tendencies out of the softies and turn them into hardened battle-trained killers. Firstly I acclimatise them to excessive aural stimulation and simple physical confrontation.").also { stage = 7 }
                }
                2 -> {
                    playerl(FaceAnim.NEUTRAL, "So why tortoises?").also { stage = 10 }
                }
                3 -> {
                    playerl(FaceAnim.NEUTRAL, "Where do these tortoises come from?").also { stage = 13 }
                }
                4 -> end()
            }

            // Training explanation
            7 -> playerl(FaceAnim.NEUTRAL, "You shout at them and beat them with sticks?").also { stage++ }
            8 -> npcl(FaceAnim.OLD_NORMAL, "Of course. Then I re-educate them to become comfortable with riders and military combat situations.").also { stage++ }
            9 -> playerl(FaceAnim.NEUTRAL, "You sit on their backs and prod them with sharp pointy things.").also { stage = 5 }

            // Why tortoises explanation
            10 -> npcl(FaceAnim.OLD_NORMAL, "Isn't it obvious?").also { stage++ }
            11 -> npcl(FaceAnim.OLD_NORMAL, "Surely even to your untrained eye that giant tortoises are the perfect mobile armoured field unit?").also { stage++ }
            12 -> npcl(FaceAnim.OLD_NORMAL, "They are sturdy animals, and well protected by their shell. They can even swim! All we need to do is put a bit of fire in their bellies.").also { stage = 5 }

            // Where from explanation
            13 -> npcl(FaceAnim.OLD_NORMAL, "We breed them.").also { stage++ }
            14 -> npcl(FaceAnim.OLD_NORMAL, "The originals were found by gnome explorers back in King Healthorg the Great's rule. On their discovery King Healthorg authorised a small party to go there and transport a breeding pair back here.").also { stage++ }
            15 -> npcl(FaceAnim.OLD_NORMAL, "He kept one for himself to ride into battle. Trouble is they take a lot of skill and time to breed, so we're only just getting them battle ready.").also { stage = 5 }

            // Cute creature handling
            16 -> npcl(FaceAnim.OLD_NORMAL, "Hurumph. Don't ask me. I didn't put it there. I keep trying to get my tortoises to stomp on it, but it keeps running away.").also { stage++ }
            17 -> playerl(FaceAnim.NEUTRAL, "That's not very nice, they are kind of cute.").also { stage++ }
            18 -> npcl(FaceAnim.OLD_NORMAL, "Will these creatures make useful foot soldiers in the gnome Army? I think not.").also { stage++ }
            19 -> playerl(FaceAnim.NEUTRAL, "I wonder why it's here then?").also { stage++ }
            20 -> npcl(FaceAnim.OLD_NORMAL, "How would I know! I've got tortoises to train - you have wasted enough of my time with trivialities, Goodbye.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = TrainerNacklepenDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.TRAINER_NACKLEPEN_3818)
}
