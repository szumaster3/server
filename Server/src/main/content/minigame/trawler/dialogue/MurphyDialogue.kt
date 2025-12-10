package content.minigame.trawler.dialogue

import core.api.addItem
import core.api.freeSlots
import core.api.inInventory
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Murphy dialogue.
 */
@Initializable
class MurphyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(!inInventory(player, Items.SEXTANT_2574)) {
            player(FaceAnim.FRIENDLY, "Ahoy there!")
            stage = 30
        } else {
            playerl(FaceAnim.FRIENDLY, "Good day to you Sir.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Well hello my brave adventurer.").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "What are you up to?").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Getting ready to go fishing of course. There's no time to waste!").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "I've got all the supplies I need from the shop at the end of the pier.").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "They sell good rope, although their bailing buckets aren't too effective.").also { stage++ }

            5 -> options(
                "What fish do you catch?",
                "Your boat doesn't look too safe.",
                "Could I help?",
            ).also { stage++ }

            6 -> when (buttonId) {
                1 -> player("What fish do you catch?").also { stage++ }
                2 -> player("Your boat doesn't look too safe.").also { stage = 8 }
                3 -> player("Could I help?").also { stage = 11 }
            }
            7 -> npcl(FaceAnim.FRIENDLY, "I get all sorts, anything that lies on the sea bed, you never know what you're going to get until you pull up the net!").also { stage = 5 }
            8 -> npcl(FaceAnim.FRIENDLY, "That's because it's not, the darn thing's full of holes.").also { stage++ }
            9 -> playerl(FaceAnim.FRIENDLY, "Oh, so I suppose you can't go out for a while?").also { stage++ }
            10 -> npcl(FaceAnim.FRIENDLY, "Oh no, I don't let a few holes stop an experienced sailor like me. I could sail these seas in a barrel, I'll be going out soon enough.").also { stage = 5 }
            11 -> npcl(FaceAnim.FRIENDLY, "Well of course you can! I'll warn you though, the seas are merciless and without fishing experience you won't catch much.").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "You need a Fishing level of 15 or above to catch any fish on the trawler.").also { stage++ }
            13 -> npcl(FaceAnim.FRIENDLY, "On occasions the net rips, so you'll need some rope to repair it.").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "Repairing the net is difficult in the harsh conditions so you'll find it easier with a higher Crafting level.").also { stage++ }
            15 -> playerl(FaceAnim.WORRIED, "Right...ok.").also { stage++ }
            16 -> npcl(FaceAnim.FRIENDLY, "There's also a slight problem with leaks.").also { stage++ }
            17 -> playerl(FaceAnim.SCARED, "Leaks?!").also { stage++ }
            18 -> npcl(FaceAnim.FRIENDLY, "Nothing some swamp paste won't fix...").also { stage++ }
            19 -> playerl(FaceAnim.HALF_ASKING, "Swamp paste?").also { stage++ }
            20 -> npcl(FaceAnim.HALF_ASKING, "Oh, and one more thing... I hope you're a good swimmer?").also { stage++ }

            21 -> options(
                "Actually, I think I'll leave it.",
                "I'll be fine, let's go.",
                "What's swamp paste?",
            ).also { stage++ }

            22 -> when (buttonId) {
                1 -> player("Actually, I think I'll leave it.").also { stage++ }
                2 -> player("I'll be fine, let's go.").also { stage = 24 }
                3 -> player("What's swamp paste?").also { stage = 25 }
            }
            23 -> npcl(FaceAnim.FRIENDLY, "Bloomin' land lovers!!!").also { stage = 50 }
            24 -> npcl(FaceAnim.FRIENDLY, "Aye aye! Meet me on board the trawler. I just need to get a few things together.").also { stage = 50 }
            25 -> npcl(FaceAnim.FRIENDLY, "Swamp tar mixed with flour which is then heated over a fire.").also { stage++ }
            26 -> playerl(FaceAnim.HALF_ASKING, "Where can I find swamp tar?").also { stage++ }
            27 -> npcl(FaceAnim.FRIENDLY, "Unfortunately the only supply of swamp tar is in the swamps below Lumbridge.").also { stage = 50 }

            30 -> npcl(FaceAnim.FRIENDLY, "Ahoy!").also { stage++ }
            31 -> playerl(FaceAnim.FRIENDLY,"I'm trying to learn how to be a navigator.").also { stage++ }
            32 -> npc(FaceAnim.FRIENDLY, "Well, you've come to the right place, m'hearty! What do","you need to know?").also { stage++ }
            33 -> player(FaceAnim.FRIENDLY, "The professor said that I need to have a sextant. Do", "you know where I can get one?").also { stage++ }
            34 -> npc(FaceAnim.FRIENDLY, "Hmm. I used to use a sextant when I was a young", "fella.").also { stage++ }
            35 -> playerl(FaceAnim.FRIENDLY,"Do you still have it?").also { stage++ }
            36 -> npcl(FaceAnim.FRIENDLY, "Aye.").also { stage++ }
            37 -> player("Could I have it?").also { stage++ }
            38 -> npcl(FaceAnim.FRIENDLY, "Aye.").also { stage++ }
            39 -> {
                if (freeSlots(player) == 0) {
                    npcl(FaceAnim.NEUTRAL, "You don't have enough space for the sextant. Come back to me when you do.")
                    return true
                }
                addItem(player, Items.SEXTANT_2574, 1)
                sendItemDialogue(player, Items.SEXTANT_2574, "Murphy has given you his old sextant.")
                stage++
            }
            40 -> player(FaceAnim.THINKING, "Don't you still need it?").also { stage++ }
            41 -> npc(FaceAnim.FRIENDLY, "I can tell from the taste of the sea spray where I am,", "m'hearty!").also { stage++ }
            42 -> player(FaceAnim.HAPPY, "Wow!").also { stage = 50 }
            50 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = MurphyDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.MURPHY_464)
}
