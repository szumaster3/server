package content.global.skill.summoning.familiar.dialogue

import core.api.inEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Obsidian golem dialogue.
 */
@Initializable
class ObsidianGolemDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = ObsidianGolemDialogue(player)

    /**
     * Instantiates a new Obsidian golem dialogue.
     */
    constructor()

    /**
     * Instantiates a new Obsidian golem dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inEquipment(player, Items.FIRE_CAPE_6570, 1)) {
            npcl(FaceAnim.CHILD_NORMAL, "Truly, you are a powerful warrior, Master!")
            stage = 0
            return true
        }
        when ((Math.random() * 3).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "How many foes have you defeated, Master?")
                stage = 5
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Master! We are truly a mighty duo!")
                stage = 10
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Do you ever doubt your programming, Master?")
                stage = 15
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Let us go forth and prove our strength, Master!")
                stage++
            }

            1 -> {
                playerl(FaceAnim.HALF_ASKING, "Where would you like to prove it?")
                stage++
            }

            2 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "The caves of the TzHaar are filled with monsters for us to defeat, Master! TzTok-Jad shall quake in his slippers!",
                )
                stage++
            }

            3 -> {
                playerl(FaceAnim.HALF_ASKING, "Have you ever met TzTok-Jad?")
                stage++
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Alas, Master, I have not. No Master has ever taken me to see him.")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Quite a few, I should think.")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Was your first foe as mighty as the volcano, Master?")
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Um, not quite.")
                stage++
            }

            8 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I am sure it must have been a deadly opponent, Master!")
                stage++
            }

            9 -> {
                player(FaceAnim.FRIENDLY, "*Cough*", "It might have been a chicken.", "*Cough*")
                stage = END_DIALOGUE
            }

            10 -> {
                playerl(FaceAnim.HALF_ASKING, "Do you think so?")
                stage++
            }

            11 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Of course, Master! I am programmed to believe so.")
                stage++
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "Do you do anything you're not programmed to?")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "No, Master.")
                stage++
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "I guess that makes things simple for you...")
                stage = END_DIALOGUE
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "I don't have programming. I can think about whatever I like.")
                stage++
            }

            16 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What do you think about, Master?")
                stage++
            }

            17 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Oh, simple things: the sound of one hand clapping, where the gods come from...Simple things.",
                )
                stage++
            }

            18 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Paradox check = positive. Error. Reboot.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.OBSIDIAN_GOLEM_7345, NPCs.OBSIDIAN_GOLEM_7346)
}
