package core.game.worldevents.events.halloween

import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.NPCs

class ZombieHalloweenPlugin : InteractionListener {

    override fun defineListeners() {

        on(NPCs.ZOMBIE_2863, IntType.NPC, "call-over") { player,node ->
            dialogue(player) {
                npc(node.id, "Durrrr...")
                player("Hey! Come over here!")
                npc(node.id, "Who? Me?")
                player("Yes, you!")
                npc(node.id, "Give me a moment...I can't move that fast.")
                player("Why not?")
                npc(node.id, "I used to be a magician's assistant until the accident...")
                player("What accident?")
                npc(node.id, "Looook...")
                end {
                    // Transform
                }
            }
            return@on true
        }

        on(NPCs.ZOMBIE_2866, IntType.NPC, "insult") { player,node ->
            dialogue(player) {
                npc(node.id, "Braaaaains...")
                player("Zombie, it has come to my attention that you are a fool, a nitwit if you will.")
                player("I propose that your head, instead of being filled with the brains you so crave, is instead filled with a mixture of crushed lemons and kelp!")
                npc(node.id, "Oh noooo..I'd best take it off then...")
                player("What?")
                end {
                    // Transform
                }
            }
            return@on true
        }

        on(NPCs.ZOMBIE_2869, IntType.NPC, "scare") { player,node ->
            dialogue(player) {
                npc(node.id, "Traaa laaa laaa...")
                player("Oogaboogabooga!")
                npc(node.id, "Aaaaaargh!")
                end {
                    // Transform
                }
            }
            return@on true
        }


    }
}