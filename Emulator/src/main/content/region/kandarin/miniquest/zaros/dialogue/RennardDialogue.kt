package content.region.kandarin.miniquest.zaros.dialogue

import content.region.kandarin.miniquest.zaros.CurseOfZaros
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class RennardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            player("All your base are belong to us.")
            return true
        }

        if (CurseOfZaros.hasTag(player, npc.id) || CurseOfZaros.hasComplete(player)) {
            val item = Items.GHOSTLY_GLOVES_6110
            if (!CurseOfZaros.hasItems(player, item)) {
                player(FaceAnim.SAD, "I lost those gloves you gave me...", "Can I have some more please?").also {
                    stage =
                        38
                }
            } else {
                sendDialogue(player, "You have already talked to this NPC.").also { stage = END_DIALOGUE }
            }
            return true
        }

        player("Hello.", "You must be Rennard.")
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> npc("H-hello!").also { stage++ }
            2 -> player("So what's up?").also { stage++ }
            3 -> npc("I cannot believe it!", "You can see me?", "You understand my words?").also { stage++ }
            4 -> player("Sure can.", "So why are you hanging out here?").also { stage++ }
            5 ->
                npc(
                    FaceAnim.SAD,
                    "My tale is one of woe...",
                    "No doubt you will have little interest in hearing it...",
                ).also {
                    stage++
                }
            6 ->
                npc(
                    FaceAnim.SAD,
                    "Though it has been so many moons since last I had",
                    "company in this endless non-life...",
                ).also {
                    stage++
                }
            7 -> options("tell em your story", "Goodbye then").also { stage++ }
            8 ->
                when (buttonId) {
                    1 ->
                        player(
                            "Well, actually I would like to know what happened to",
                            "you to turn you into an invisible ghost.",
                        ).also {
                            stage++
                        }

                    2 ->
                        player("Well, that's all fascinating, but I just don't particularly care.", "Bye-bye.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            9 -> player("If only so I can make sure it doesn't happen to me...").also { stage++ }
            10 ->
                npc(
                    "My name is Valdez.",
                    "I served my Lord Saradomin faithfully for many years,",
                    "as an explorer of this strange land we had been brought",
                    "to.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    "I remember the day this curse fell upon me clearly...",
                    "I had just discovered a huge temple, hidden below the",
                    "ground, of one of Saradomins compatriots.",
                ).also {
                    stage++
                }
            12 ->
                npc(
                    FaceAnim.THINKING,
                    "I am unsure who built it, or why they had left it",
                    "seemingly abandoned, but inside I located a great",
                    "treasure...",
                ).also {
                    stage++
                }
            13 -> npc("It was the godstaff of Armadyl.", "Oh, how I rue my choice that day!").also { stage++ }
            14 -> player("Choice?").also { stage++ }
            15 ->
                npc(
                    "Aye, stranger.",
                    "I chose that day to take it so that my Lord Saradomin's",
                    "power and prestige could be increased by its possession.",
                ).also {
                    stage++
                }
            16 ->
                npc(
                    FaceAnim.HAPPY,
                    "A god-weapon!",
                    "Do you have any comprehension of the difficulty and",
                    "rarity in obtaining such a thing?",
                ).also {
                    stage++
                }
            17 ->
                npc(
                    "To find such an artefact of power just lying around, it",
                    "is almost incomprehensible...",
                ).also { stage++ }
            18 ->
                npc(
                    FaceAnim.SAD,
                    "So it was there in that deserted temple that I made my",
                    "choice.",
                    "I took the staff, and left that temple for Entrana",
                    "immediately.",
                ).also {
                    stage++
                }
            19 -> npc(FaceAnim.SAD, "This was the cause of my cursed state.").also { stage++ }
            20 -> player("What, you mean you gave it to Saradomin and in", "return he cursed you???").also { stage++ }
            21 -> player("Seems kind of ungrateful if you ask me...").also { stage++ }
            22 ->
                npc(
                    "No stranger, you misunderstand completely...",
                    "Firstly my gracious Lord would never treat anyone in",
                    "such a manner;",
                    "If he felt it was beyond my bounds as a mere mortal",
                ).also {
                    stage++
                }
            23 ->
                npc(
                    "to hold such an artefact, he would simply have",
                    "commanded me to return it to whence I had claimed it,",
                    "and I being eternally loyal would have obeyed without",
                    "question...",
                ).also {
                    stage++
                }
            24 -> player(FaceAnim.ASKING, "And secondly?").also { stage++ }
            25 -> npc("And secondly, I never managed to pass the artefact on", "to my Lord...").also { stage++ }
            26 ->
                npc(
                    FaceAnim.SAD,
                    "The vile thief Rennard accosted me as I made my way",
                    "to Entrana, and after defeating me with a sneak attack,",
                    "plundered the staff from my person, and left me for",
                    "dead...",
                ).also {
                    stage++
                }
            27 ->
                npc(
                    FaceAnim.SAD,
                    "I do not know what became of the staff, but I can feel",
                    "in my very bones whatever its final fate was, it is",
                    "somehow related to this curse upon me...",
                ).also {
                    stage++
                }
            28 -> player("Wow.", "Tough break!").also { stage++ }
            29 ->
                npc(
                    "I am sorry to bore you with my tale stranger, please",
                    "allow me to compound my rudeness by asking you for",
                    "one small favour, small to perform?",
                ).also {
                    stage++
                }
            30 ->
                player(
                    "Eh, I won't make any promises, but if it's nothing too",
                    "annoying I guess I can help you out.",
                ).also {
                    stage++
                }
            31 -> npc("Many thanks stranger, this existence tortures me...").also { stage++ }
            32 ->
                npc(
                    "I need to find Rennard and if he has the staff yet",
                    "reclaim it, or find out what hideous deed he performed",
                    "to curse me so!",
                ).also {
                    stage++
                }
            33 ->
                npc("I have nothing I may offer you save my piece of", "clothing, please take it as payment...").also {
                    CurseOfZaros.tagDialogue(player, npc.id)
                    addItemOrDrop(player, Items.GHOSTLY_GLOVES_6110, 1)
                    stage++
                }
            34 -> player("Where can I find Rennard then?").also { stage++ }
            35 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    CurseOfZaros.getLocationDialogue(npc.id, npc.location)?.joinToString("\n") ?: "",
                ).also { stage++ }
            36 -> npc("the reach of the authorities that pursued them...").also { stage++ }
            37 -> player("Okay, well I'll try and find him for you then.").also { stage = END_DIALOGUE }
            38 -> {
                if (freeSlots(player) == 0) {
                    sendDialogue(player, "You don't have space for the reward.")
                    return true
                }
                npc(FaceAnim.SAD, "It seems as though the curse that keeps me here", "extends to my very clothing...")
                addItem(player!!, Items.GHOSTLY_GLOVES_6110, 1)
                stage++
            }

            39 -> {
                end()
                npc(FaceAnim.CALM, "Here, take them, some evil power returned them to", "me.")
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return RennardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MYSTERIOUS_GHOST_2397)
    }
}
