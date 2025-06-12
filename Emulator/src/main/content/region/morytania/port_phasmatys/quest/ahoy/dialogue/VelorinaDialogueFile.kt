package content.region.morytania.port_phasmatys.quest.ahoy.dialogue

import core.api.addItemOrDrop
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendItemDialogue
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import core.game.world.GameWorld
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class VelorinaDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.GHOSTS_AHOY)
        npc = NPC(NPCs.VELORINA_1683)
        when (questStage) {
            0 ->
                when (stage) {
                    0 ->
                        npc(
                            "Take pity on me, please - eternity stretches out",
                            "before me and I am helpless in its grasp.",
                        ).also {
                            stage++
                        }
                    1 -> options("Why, what is the matter?", "Sorry, I'm scared of ghosts.").also { stage++ }
                    2 ->
                        when (buttonID) {
                            1 -> player("Why, what is the matter?").also { stage++ }
                            2 -> player("Sorry, I'm scared of ghosts.").also { stage = END_DIALOGUE }
                        }
                    3 ->
                        npc(
                            "Oh, I'm sorry - I was just wailing out loud. I didn't",
                            "mean to scare you.",
                        ).also { stage++ }
                    4 ->
                        player(
                            "No, that's okay - it takes more than a ghost to scare",
                            "me. What is wrong?",
                        ).also { stage++ }
                    5 -> npc("Do you know the history of our town?").also { stage++ }
                    6 -> options("Yes, I do. It is a very sad story.", "No - could you tell me?").also { stage++ }
                    7 ->
                        when (buttonID) {
                            1 -> player("Yes, I do. It is a very sad story.").also { stage++ }
                            2 -> player("No - could you tell me?").also { stage = 15 }
                        }
                    8 -> npc("Would you help us obtain our release into the next world?").also { stage++ }
                    9 -> options("Yes.", "Not right now.").also { stage++ }
                    10 ->
                        when (buttonID) {
                            1 -> player("Yes, of course I will.", "Tell me what you want me to do.").also { stage = 11 }
                            2 -> player("I'm sorry, but it isn't really my problem.").also { stage = 14 }
                        }
                    11 -> npc("Oh, thank you!").also { stage++ }
                    12 ->
                        npc(
                            "Necrovarus will not listen to those of us who are",
                            "already dead. He might rethink his position if someone",
                            "with a mortal soul pleaded our cause.",
                        ).also {
                            stage++
                        }
                    13 -> npc("If he declines, there may yet be another way.").also { stage = 32 }
                    14 ->
                        npc(
                            "No, you're right - it's our own fault.",
                            "If you do change your mind though, please come",
                            "back and help us.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    15 -> npc("Do you know why ghosts exist?").also { stage++ }
                    16 ->
                        player(
                            "A ghost is a soul left in limbo, unable to pass over to the",
                            "next world; they might have something left to do in this",
                            "world that torments them, or they might just have died",
                            "in a state of confusion.",
                        ).also {
                            stage++
                        }
                    17 ->
                        npc(
                            "Yes, that is normally the case. But here in Port",
                            "Phasmatys, we of this town once chose freely to become",
                            "ghosts!",
                        ).also {
                            stage++
                        }
                    18 -> player("Why on earth would you do such a thing?").also { stage++ }
                    19 ->
                        npc(
                            "It is a long story. Many years ago, this was a thriving",
                            "port, a trading centre to the eastern lands of",
                            "" + GameWorld.settings!!.name + "We became rich on the profits made from",
                            "the traders that came across the eastern seas.",
                        ).also { stage++ }
                    20 -> npc("We were very happy...", "until Lord Drakan noticed us.").also { stage++ }
                    21 ->
                        npc(
                            "He sent unholy creatures to demand that a blood-tithe",
                            "be paid to the Lord Vampyre, as is required from all in",
                            "the domain of Morytania. We had no choice but to",
                            "agree to his demands.",
                        ).also {
                            stage++
                        }
                    22 ->
                        npc(
                            "As the years went by, our numbers dwindled and many",
                            "spoke of abandoning the town for safer lands.",
                            "Then, Necrovarus came to us.",
                        ).also {
                            stage++
                        }
                    23 ->
                        npc(
                            "He came from the eastern lands, but of more than that,",
                            "little is known. Some say he was once a mage, some say",
                            "a priest. Either way, he was in possession of knowledge",
                            "totally unknown to even the most learned among us.",
                        ).also {
                            stage++
                        }
                    24 ->
                        npc(
                            "Necrovarus told us that he had been brought by a",
                            "vision he'd had of an underground source of power. He",
                            "inspired us to delve beneath the town, promising us the",
                            "power to repel the vampyres.",
                        ).also {
                            stage++
                        }
                    25 ->
                        npc(
                            "Deep underneath Phasmatys, we found a pool of green",
                            "slime that Necrovarus called ectoplasm. He showed us",
                            "how to build the Ectofuntus, which would turn the",
                            "ectoplasm into the power he had promised us.",
                        ).also {
                            stage++
                        }
                    26 ->
                        npc(
                            "Indeed, this Ectopower did repel the vampyres; they",
                            "would not enter Phasmatys once the Ectofuntus began",
                            "working. But little did we know that we had exchanged",
                            "one evil for yet another - Ectopower.",
                        ).also {
                            stage++
                        }
                    27 ->
                        npc(
                            "Little by little, we began to lose any desire for food",
                            "or water, and our desire for the Ectopower grew until it",
                            "dominated our thoughts entirely. Our bodies shrivelled",
                            "and, one by one, we died.",
                        ).also {
                            stage++
                        }
                    28 ->
                        npc(
                            "The Ectofuntus and the power it emanates keeps us",
                            "here as ghosts - some, like myself, content to remain in",
                            "this world, some becoming tortured souls who we do not",
                            "allow to pass our gates.",
                        ).also {
                            stage++
                        }
                    29 ->
                        npc(
                            "We would be able to pass over into the next world but",
                            "Necrovarus has used his power to create a psychic",
                            "barrier, preventing us.",
                        ).also {
                            stage++
                        }
                    30 ->
                        npc(
                            "We must remain here for all eternity, even unto the",
                            "very end of the world.",
                        ).also { stage++ }
                    31 -> player("That's a very sad story.").also { stage = 8 }
                    32 -> {
                        end()
                        setQuestStage(player!!, Quests.GHOSTS_AHOY, 1)
                    }
                }

            1 ->
                when (stage) {
                    0 -> npc("I sense that you have not yet spoken to Necrovarus.").also { stage++ }
                    1 -> player("No, I was just getting to that.").also { stage++ }
                    2 -> npc("Well, I suppose we do have all the time in the world.").also { stage = END_DIALOGUE }
                }

            2 ->
                when (stage) {
                    0 -> player("I'm sorry, but Necrovarus will not let you go.").also { stage++ }
                    1 -> npc("I feared as much. His spirit is a thing of fire and ", "wrath.").also { stage++ }
                    2 -> player("You spoke of another way.").also { stage++ }
                    3 ->
                        npc(
                            "It is only a small chance. During the building of the",
                            "Ectofuntus one of Necrovarus's disciples spoke out",
                            "against him. It is such a long time ago I cannot",
                            "remember her name, although I knew her as a friend.",
                        ).also {
                            stage++
                        }
                    4 ->
                        npc(
                            "She fled before the Ectofuntus took control over us, but",
                            "being a disciple of Necrovarus she would have been",
                            "privy to many of his darkest secrets. She may know of",
                            "a way to aid us without Necrovarus.",
                        ).also {
                            stage++
                        }
                    5 ->
                        options(
                            "Do you know where this woman can be found?",
                            "If it was such a long time ago, won't she be dead already?",
                        ).also {
                            stage++
                        }
                    6 ->
                        when (buttonID) {
                            1 -> player("Do you know where this woman can be found?").also { stage++ }
                            2 ->
                                player("If it was such a long time ago, won't she be", "dead already?").also {
                                    stage =
                                        8
                                }
                        }
                    7 ->
                        npc(
                            "I have a vision of a small wooden shack, the land it was",
                            "built on the unholy soil of Morytania. I sense the sea is",
                            "very close, and that there looms castles to the west and ",
                            "the east.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    8 ->
                        npc(
                            "She was a friend of mine. Had she died, I would have",
                            "felt her spirit pass over to the next world, though",
                            "I may not follow.",
                        ).also {
                            stage++
                        }
                    9 -> {
                        end()
                        setQuestStage(player!!, Quests.GHOSTS_AHOY, 3)
                    }
                }

            99 ->
                when (stage) {
                    0 ->
                        npc(
                            "You don't need to tell me ${player!!.username}",
                            "I sensed the removal of Necrovarus's psychic barrier!",
                        ).also {
                            stage++
                        }
                    1 -> player("Only happy to help out.").also { stage++ }
                    2 ->
                        npc(
                            "Here, take this as a thank you for the service",
                            "that you have given us.",
                        ).also { stage++ }
                    3 ->
                        sendItemDialogue(
                            player!!,
                            Items.ECTOPHIAL_4251,
                            "Velorina gives you a vial of bright green ectoplasm.",
                        ).also {
                            addItemOrDrop(player!!, Items.ECTOPHIAL_4251)
                            stage++
                        }
                    4 ->
                        npc(
                            "This is an Ectophial. If you ever want to come back to",
                            "Port Phasmatys, empty this on the floor beneath your feet,",
                            "and you will be instantly teleported to the",
                            " temple - the source of its power.",
                        ).also {
                            stage++
                        }
                    5 ->
                        npc(
                            "Remember that once the Ectophial has been used you need",
                            "to refill it from the Ectofuntus.",
                        ).also {
                            stage++
                        }
                    6 -> {
                        end()
                        finishQuest(player!!, Quests.GHOSTS_AHOY)
                    }
                }
        }
    }
}
