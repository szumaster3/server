package content.region.morytania.phas.dialogue

import core.api.addItemOrDrop
import core.api.inEquipment
import core.api.inInventory
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Innkeeper (Port Phasmatys) dialogue.
 */
@Initializable
class GhostInkeeperDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            npc("Woooo wooo wooooo woooo")
            stage = 10
            return true
        }
        player("Hello there!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 ->
                npc(
                    "Greetings, traveller. Welcome to 'The Green Ghost'",
                    "Tavern. What can I do you for?",
                ).also { stage++ }

            1 ->
                options(
                    "Can I buy a beer?",
                    "Can I hear some gossip?",
                    "What happened to the folk of this town?",
                    "Do you have any jobs I can do?",
                    "Nothing, thanks.",
                ).also { stage++ }

            2 ->
                when (buttonId) {
                    1 -> player("Can I buy a beer?").also { stage++ }
                    2 -> player("Can I hear some gossip?").also { stage = 20 }
                    3 -> player("What happened to the folk of this town?").also { stage = 30 }
                    4 -> player("Do you have any jobs I can do?").also { stage = 47 }
                    5 -> player("Nothing, thanks.").also { stage = END_DIALOGUE }
                }

            3 ->
                npc(
                    "Sorry, but our pumps dried up over half a century",
                    "ago. We of this village do not have much of a thirst",
                    "these days.",
                ).also { stage = END_DIALOGUE }

            20 -> npc("I suppose, as long as you keep it to yourself...").also { stage++ }
            21 ->
                npc(
                    "You see Gravingas out there in the marketplace? He",
                    "speaks for the silent majoirty of Port Phasmatys, for",
                    "those of us who would prefer to pass over into the next",
                    "world.",
                ).also { stage++ }

            22 ->
                npc(
                    "But old Gravingas is far too obvious in his methods.",
                    "Now Velorina, she's a ghost of a different colour",
                    "altogether. If you feel like helping our cause at all, go",
                    "speak to Velorina.",
                ).also { stage = END_DIALOGUE }

            30 -> npc("That's a long story, my friend, but you look like you", "have the time...").also { stage++ }
            31 -> npc("Do you know why ghosts exist?").also { stage++ }
            32 ->
                player(
                    "A ghost is a soul left in limbo, unable to pass over to the",
                    "next world; they might have something left to do in this",
                    "world that torments them, or they might just have died",
                    "in a state of confusion.",
                ).also { stage++ }

            33 ->
                npc(
                    "Yes, that is normally the case. But here in Port",
                    "Phasmatys, we of this town once chose freely to become",
                    "ghosts!",
                ).also { stage++ }

            34 -> player("Why on earth would you do such a thing?").also { stage++ }
            35 ->
                npc(
                    "It is a long story. Many years ago, this was a thriving",
                    "port, a trading centre to the eastern lands of",
                    "" + GameWorld.settings!!.name + "We became rich on the profits made from",
                    "the traders that came across the eastern seas.",
                ).also { stage++ }

            36 -> npc("We were very happy...", "until Lord Drakan noticed us.").also { stage++ }
            37 ->
                npc(
                    "He sent unholy creatures to demand that a blood-tithe",
                    "be paid to the Lord Vampyre, as is required from all in",
                    "the domain of Morytania. We had no choice but to",
                    "agree to his demands.",
                ).also { stage++ }

            38 ->
                npc(
                    "As the years went by, our numbers dwindled and many",
                    "spoke of abandoning the town for safer lands.",
                    "Then, Necrovarus came to us.",
                ).also { stage++ }

            39 ->
                npc(
                    "He came from the eastern lands, but of more than that,",
                    "little is known. Some say he was once a mage, some say",
                    "a priest. Either way, he was in possession of knowledge",
                    "totally unknown to even the most learned among us.",
                ).also { stage++ }

            40 ->
                npc(
                    "Necrovarus told us that he had been brought by a",
                    "vision he'd had of an underground source of power. He",
                    "inspired us to delve beneath the town, promising us the",
                    "power to repel the vampyres.",
                ).also { stage++ }

            41 ->
                npc(
                    "Deep underneath Phasmatys, we found a pool of green",
                    "slime that Necrovarus called ectoplasm. He showed us",
                    "how to build the Ectofuntus, which would turn the",
                    "ectoplasm into the power he had promised us.",
                ).also { stage++ }

            42 ->
                npc(
                    "Indeed, this Ectopower did repel the vampyres; they",
                    "would not enter Phasmatys once the Ectofuntus began",
                    "working. But little did we know that we had exchanged",
                    "one evil for yet another - Ectopower.",
                ).also { stage++ }

            43 ->
                npc(
                    "Little by little, we began to lose any desire for food",
                    "or water, and our desire for the Ectopower grew until it",
                    "dominated our thoughts entirely. Our bodies shrivelled",
                    "and, one by one, we died.",
                ).also { stage++ }

            44 ->
                npc(
                    "The Ectofuntus and the power it emanates keeps us",
                    "here as ghosts - some, like myself, content to remain in",
                    "this world, some becoming tortured souls who we do not",
                    "allow to pass our gates.",
                ).also { stage++ }

            45 ->
                npc(
                    "We would be able to pass over into the next world but",
                    "Necrovarus has used his power to create a psychic",
                    "barrier, preventing us.",
                ).also { stage++ }

            46 ->
                npc("We must remain here for all eternity, even unto the", "very end of the world.").also {
                    stage = END_DIALOGUE
                }

            47 ->
                npc(
                    "Yes, actually, I do. We have a very famous Master",
                    "Bowman named Robin staying with us at the moment.",
                    "Could you take him some clean bed linen for me?",
                ).also { stage++ }

            48 -> options("Yes, I'd be delighted.", "No, I didn't mean a job like that.").also { stage++ }
            49 ->
                when (buttonId) {
                    1 -> player("Yes, I'd be delighted.").also { stage++ }
                    2 -> player("No, I didn't mean a job like that.").also { stage = END_DIALOGUE }
                }

            50 -> {
                if (!inInventory(player, Items.BEDSHEET_4284)) {
                    npc(
                        "Oh, thank you. Be careful with that Robin,",
                        "though - he's far too full of himself, that one.",
                    ).also { stage = END_DIALOGUE }
                    addItemOrDrop(player, Items.BEDSHEET_4284)
                } else {
                    npc("Well, you could take that bedsheet through to", "Robin like I asked.").also {
                        stage = END_DIALOGUE
                    }
                }
            }

            10 -> sendDialogue(player, "You cannot understand the ghost.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GHOST_INNKEEPER_1700)
}
