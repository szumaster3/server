package content.region.morytania.port_phasmatys.quest.ahoy.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class OldCroneAhoyDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.GHOSTS_AHOY)
        npc = NPC(NPCs.OLD_CRONE_1695)
        when {
            questStage == 3 -> when (stage) {
                0 -> player("Were you once a disciple of Necrovarus in the Temple", "of Phasmatys, old woman?").also { stage++ }
                1 -> npc("I don't remember; I am so old and my memory goes", "back only so far...").also { stage++ }
                2 -> player("Is there anything that can help to refresh your", "memory?").also { stage++ }
                3 -> npc("Yes, I would love a nice hot cup of nettle tea.").also { stage++ }
                4 -> {
                    if (!inInventory(player!!, Items.NETTLE_TEA_4239)) {
                        player("Do you know where I can find nettles around here?").also { stage++ }
                    } else {
                        player("Here's some tea for you, like you asked.").also { stage = 6 }
                    }
                }

                5 -> npc("I believe they grow wild in the Haunted Forest.").also { stage = END_DIALOGUE }
                6 -> npc("Yes, but it's not in my special cup! I only ever drink", "from my special cup!").also { stage++ }
                7 -> player("I see. Can I have this special cup, then?").also { stage++ }
                8 -> {
                    end()
                    sendItemDialogue(player!!, Items.PORCELAIN_CUP_4244, "The old crone gives you her special cup.")
                    addItemOrDrop(player!!, Items.PORCELAIN_CUP_4244)
                }
            }

            inInventory(player!!, Items.CUP_OF_TEA_4245) && questStage >= 3 -> when (stage) {
                0 -> player("Here's a lovely cup of tea for you, in your own special cup.").also { stage++ }
                1 -> npc("Oh no, it hasn't got milk in it. I only drink", "tea with milk, I'm afraid.").also { stage++ }
                2 -> {
                    end()
                    setQuestStage(player!!, Quests.GHOSTS_AHOY, 4)
                }
            }

            questStage == 4 && !inInventory(player!!, Items.MODEL_SHIP_4253) && inInventory(player!!, Items.CUP_OF_TEA_4246) -> when (stage) {
                0 -> player("Here's a lovely cup of milky tea for you, in your", "own special cup.").also { stage++ }
                1 -> sendDialogue(player!!, "As the old woman drinks the cup of milky tea, enlightenment glows from within her eyes.").also {
                    removeItem(player!!, Items.CUP_OF_TEA_4246)
                    addItemOrDrop(player!!, Items.PORCELAIN_CUP_4244)
                    stage++
                }
                2 -> npc("Ah, that's better. Now, let me see... Yes, I was once a", "disciple of Necrovarus.").also { stage++ }
                3 -> player("I have come from Velorina. She needs your help.").also { stage++ }
                4 -> npc("Velorina? That name sounds familiar...").also { stage++ }
                5 -> npc("Yes, Velorina was once a very good friend! It has been", "many years since we spoke last. How is she?").also { stage++ }
                6 -> player("She's a ghost, I'm afraid.").also { stage++ }
                7 -> npc("They are all dead, then. Even Velorina. I should have", "done something to stop what was happening, instead of", "running away.").also { stage++ }
                8 -> player("She and many others want to pass on into the next", "world, but Necrovarus will not let them. Do you know", "of any way to help them?").also { stage++ }
                9 -> npc("There might be one way...").also { stage++ }
                10 -> npc("Do you have such a thing as a ghostspeak amulet?").also { stage++ }
                11 -> if (!inEquipment(player!!, Items.GHOSTSPEAK_AMULET_552)) {
                    player("I don't have that on me.").also { stage++ }
                } else {
                    player("Yes, I'm wearing one right now.").also { stage++ }
                }
                12 -> if (!inEquipment(player!!, Items.GHOSTSPEAK_AMULET_552)) {
                    npc("Well, that's bad luck.").also { stage++ }
                } else {
                    npc("Well, that's a stroke of luck.").also { stage++ }
                }
                13 -> npc("There is an enchantment that I can perform on such", "an amulet that will give it the power of command over", "ghosts. It will work only once, but it will enable you to", "command Necrovarus to let the ghosts pass on.").also { stage++ }
                14 -> player("What do you need to perform the enchantment?").also { stage++ }
                15 -> npc("Necrovarus had a magical robe for which he must have", "no more use. Only these robes hold the power needed", "to perform the enchantment.").also { stage++ }
                16 -> npc("All his rituals come from a book written by an ancient", "sorcerer from the East called Haricanto. Bring me this", "strange book.").also { stage++ }
                17 -> npc("I cannot read the strange letters of the eastern lands.", "I will need something to help me translate the book.").also { stage++ }
                18 -> options("You are doing so much for me - is there anything I can do for you?", "Remind me - what can I do about Necrovarus?", "What did you want me to get for you?", "I'll go and search for those items you asked for.").also { stage++ }
                19 -> when (buttonID) {
                    1 -> player("You are doing so much for me - is there anything", "I can do for you?").also { stage++ }
                    2 -> player("Remind me - what can I do about Necrovarus?").also { stage = 13 }
                    3 -> player("What did you want me to get for you?").also { stage = 15 }
                    4 -> player("I'll go and search for those items you asked for.").also { stage = END_DIALOGUE }
                }
                20 -> npc("I have lived here on my own for many years, but not", "always. When I left Port Phasmatys, I took my", "son with me. He grew up to be a fine boy, always in love", "with the sea.").also { stage++ }
                21 -> npc("He was about twelve years old when he ran away with", "some pirates to be a cabin boy. I never saw him again.").also { stage++ }
                22 -> player("That's the second saddest story I have heard today.").also { stage++ }
                23 -> npc("If you ever see him, please give him this...and tell him", "that his mother still loves him.").also { stage++ }
                24 -> sendItemDialogue(player!!, Items.MODEL_SHIP_4253, "The old woman gives you a toy boat.").also { stage++ }
                25 -> player("Was this his boat?").also { stage++ }
                26 -> npc("Yes, he made it himself. It is a model of the very ship", "in which he sailed away. The paint has peeled off and it", "has lost its flag, but I could never throw it away.").also { stage++ }
                27 -> player("If I find him, I will pass it on.").also { stage++ }
                28 -> {
                    end()
                    addItemOrDrop(player!!, Items.MODEL_SHIP_4253)
                }
            }

            questStage == 4 && inInventory(player!!, Items.MODEL_SHIP_4253) && !inInventory(player!!, Items.CUP_OF_TEA_4246) -> when (stage) {
                0 -> player("I am afraid I have not found your son yet.").also { stage++ }
                1 -> npc("I never expected that you would find him - although if you do, please let me know.").also { stage++ }
                2 -> options("Remind me - what can I do about Necrovarus?", "What did you want me to get for you?", "I'll go and search for those items you asked for.").also { stage++ }
                3 -> when (buttonID) {
                    1 -> player("Remind me - what can I do about Necrovarus?").also { stage = 4 }
                    2 -> player("What did you want me to get for you?").also { stage = 5 }
                    3 -> player("I'll go and search for those items you asked for.").also { stage = END_DIALOGUE }
                }
                4 -> npc("There is an enchantment that I can perform on such", "an amulet that will give it the power of command over", "ghosts. It will work only once, but it will enable you to", "command Necrovarus to let the ghosts pass on.").also { stage = END_DIALOGUE }
                5 -> npc("Necrovarus had a magical robe for which he must have", "no more use. Only these robes hold the power needed", "to perform the enchantment.").also { stage++ }
                6 -> npc("All his rituals come from a book written by an ancient", "sorcerer from the East called Haricanto. Bring me this", "strange book.").also {
                    stage++
                }
                7 -> npc("I cannot read the strange letters of the eastern lands.", "I will need something to help me translate the book.").also { stage = END_DIALOGUE }
            }

            questStage == 6 -> when (stage) {
                0 -> player("Good news! I have found your son!").also { stage++ }
                1 -> npc("Goodness! Where is he?").also { stage++ }
                2 -> player("He lives on a shipwreck to the east of here.", "He remembers you and wishes you well.").also { stage++ }
                3 -> npc("Oh thank you! I will travel to see him as soon as", "I am able!!").also { stage = END_DIALOGUE }
            }

            questStage == 80 -> when (stage) {
                0 -> player("I have something for you.").also { stage++ }
                1 -> npc("Good - the robes of Necrovarus.").also { stage++ }
                2 -> npc("The Book of Haricanto! I have no idea how you came", "by this, but well done!!").also { stage++ }
                3 -> npc("A translation manual - yes, this should do the job.").also { stage++ }
                4 -> npcl("Wonderful; that's everything I need.").also { stage++ }
                5 -> npc("I will now perform the ritual of enchantment.").also { stage++ }
                6 -> {
                    end()
                    animate(findNPC(NPCs.OLD_CRONE_1695)!!, Animation(Animations.HUMAN_OLD_DANCE_818))
                    queueScript(player!!, 3, QueueStrength.SOFT) {
                        addItemOrDrop(player!!, Items.GHOSTSPEAK_AMULET_4250, 1)
                        sendItemDialogue(player!!, Items.GHOSTSPEAK_AMULET_4250, "The ghostspeak amulet emits a green glow from its gem.")
                        sendMessage(player!!, "The amulet of ghostspeak glows green from the crone's enchantment.")
                        return@queueScript stopExecuting(player!!)
                    }
                }
            }
        }
    }
}
