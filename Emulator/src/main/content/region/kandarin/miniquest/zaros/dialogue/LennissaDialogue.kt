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
class LennissaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            player("All your base are belong to us.")
            return true
        }
        if (CurseOfZaros.hasTag(player, npc.id) || CurseOfZaros.hasComplete(player)) {
            val item = Items.GHOSTLY_ROBE_6108
            if (!CurseOfZaros.hasItems(player, item)) {
                player(
                    FaceAnim.SAD,
                    "Could I have that robe bottom again? I seem to have",
                    "misplaced it somewhere...",
                ).also {
                    stage =
                        57
                }
            } else {
                sendDialogue(player, "You have already talked to this NPC.").also { stage = END_DIALOGUE }
            }
            return true
        }
        player("Hello.", "You would be Lennissa, I take it?")
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                npc(
                    "Who are you?",
                    "Where did you hear my name?",
                    "How comes it that you can see and speak to me?",
                ).also {
                    stage++
                }
            2 -> player("Well, a ghost called Kharrim directed me towards you.").also { stage++ }
            3 -> npc("So that weasel Kharrim has been blighted by this curse ", "too?").also { stage++ }
            4 ->
                npc(
                    "Ha, a good thing too.",
                    "If anybody deserved such a fate it would be one such",
                    "as him.",
                ).also { stage++ }
            5 -> player("I guess you didn't get along then?").also { stage++ }
            6 -> npc("No, evil scum such as he should never have been", "allowed to walk this world.").also { stage++ }
            7 -> npc("What lies has he told you to come here?", "Have you come to try and kill me?").also { stage++ }
            8 ->
                player(
                    "Well, I'm not sure I could if I tried, but",
                    "that is not why I have come to you.",
                ).also { stage++ }
            9 ->
                npc(
                    "Then speak, and speak well, for I may yet be dead, but",
                    "am still a danger to those who cross me.",
                ).also {
                    stage++
                }
            10 ->
                player(
                    "Actually, I'm trying to work out why all of you invisible",
                    "ghosts seem to have been cursed.",
                ).also {
                    stage++
                }
            11 -> player("I'm not sure how exactly, but the trail seems to have led me", "to you...").also { stage++ }
            12 ->
                npc(
                    "That makes no sense...",
                    "I served Saradomin faithfully my entire life, then all of",
                    "a sudden I find myself reduced to this state!",
                ).also {
                    stage++
                }
            13 -> player("Well, it seems as though that may have been the", "cause...").also { stage++ }
            14 -> npc(FaceAnim.PANICKED, "What?", "Explain yourself.").also { stage++ }
            15 ->
                player(
                    "As I understand it, it has something to do with the",
                    "Staff of Armadyl, and your refusal to tell Saradomin",
                    " that it had been stolen...",
                ).also {
                    stage++
                }
            16 -> npc("What? But...", "But that is not how it happened at all!").also { stage++ }
            17 -> options("tell em your story", "Goodbye then").also { stage++ }
            18 ->
                when (buttonId) {
                    1 ->
                        player(
                            "Then please, go ahead and tell me the events of that day",
                            "in your own words...",
                        ).also { stage++ }

                    2 ->
                        player("Well, that's all fascinating, but I just don't particularly care.", "Bye-bye.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            19 ->
                npc(
                    "Let me see...",
                    "I had been working as a spy for my Lord Saradomin,",
                    "amongst the forces of... the Empty Lord.",
                ).also {
                    stage++
                }
            20 -> player("'The Empty Lord'? Who is that?").also { stage++ }
            21 -> npc("I will not give you his name, for to do so would give", "him power here.").also { stage++ }
            22 ->
                npc(
                    "Let us just say that he was a fearsome deity, whose",
                    "strength was greater than all the gods we knew of on",
                    "this realm at the time.",
                ).also {
                    stage++
                }
            23 ->
                npc(
                    "It is probably worth mentioning that at this time we had",
                    "no knowledge of the mysterious nature god Guthix.",
                ).also {
                    stage++
                }
            24 -> player("So he was stronger than Saradomin?").also { stage++ }
            25 -> npc("As Saradomin was, yes.", "As Saradomin is now?", "Who can say?").also { stage++ }
            26 -> player("I see.", "Please continue.").also { stage++ }
            27 -> npc("As I say, I was working as a spy within the very camp", "of my Lord's enemies.").also { stage++ }
            28 ->
                npc(
                    "I knew that should I have been caught, I risked being",
                    "killed upon the spot, but my combat skills were always",
                    "formidable, and if truth be told, there was a fair amount",
                    "of dissent amongst... 'his' followers anyway.",
                ).also {
                    stage++
                }
            29 -> player("How do you mean 'dissent'?").also { stage++ }
            30 -> npc("Ah, to not understand this, you must have led a", "sheltered life...").also { stage++ }
            31 ->
                npc(
                    "Let me tell you this:",
                    "Evil will always breed more evil, and will never be",
                    "satisfied with what it has.",
                ).also {
                    stage++
                }
            32 ->
                npc(
                    "The Empty Lord chose to ally himself with the dark",
                    "creatures of this world, fully aware that their own",
                    "natures would cause them to rally against his rule, and",
                    "take every opportunity they could to betray him.",
                ).also {
                    stage++
                }
            33 ->
                npc(
                    "This has always been the nature of evil.",
                    "Perhaps he thought his power could prevent such",
                    "treachery?",
                ).also {
                    stage++
                }
            34 ->
                npc(
                    "This allowed me freedom amongst their camp, for it was",
                    "always easy to point the finger of suspicion at some",
                    "unsuspecting necromancer or foolish Mahjarrat. If it",
                    "seemed as though my activities had been discovered.",
                ).also {
                    stage++
                }
            35 ->
                npc(
                    "Similarly, should I ever have been caught in the act of my",
                    "sabotage, it was all too easy to bribe whoever found me",
                    "or persuade them into believing it was just some minor",
                    "treachery of my own, rather than my work for my",
                ).also {
                    stage++
                }
            36 -> npc("Lord Saradomin.").also { stage++ }
            37 ->
                player(
                    "Okay...",
                    "Well, that makes sense, but I don't understand what the",
                    "Staff of Armadyl had to do with this...?",
                ).also {
                    stage++
                }
            38 ->
                npc(
                    "As I told you, the Empty Lord was extremely",
                    "powerful, but not so powerful that he could rule over",
                    "the other deities of this world without opposition.",
                ).also {
                    stage++
                }
            39 ->
                npc(
                    "Should he have made a move against any other god,",
                    "then he could still have been easily brought down by the",
                    "combined efforts of the others.",
                ).also {
                    stage++
                }
            40 -> npc("The theft of Armadyl's staff changed this however.").also { stage++ }
            41 ->
                npc(
                    "If he had taken possession of this god-weapon, then his",
                    "power would have been so great that he could have",
                    "overthrown all on this world, and made it into his own",
                    "image!",
                ).also {
                    stage++
                }
            42 -> npc("I could not allow such a thing to happen!").also { stage++ }
            43 ->
                npc(
                    "I went immediately",
                    "to my comrade Dhalak, the mage,",
                    "and told him that a message had come to the lair",
                    "offering this weapon for sale!",
                ).also {
                    stage++
                }
            44 ->
                npc(
                    "I knew that as soon as my Lord Saradomin heard this, he",
                    "would contact Armadylcto inform them of the theft,",
                    "and the matter would have been resolved quickly and",
                    "discreetly.",
                ).also {
                    stage++
                }
            45 ->
                player(
                    "So you passed this information to Dhalak instead of",
                    "taking it to Saradomin yourself?",
                ).also { stage++ }
            46 -> npc("To my eternal shame, I indeed failed my Lord", "Saradomin...").also { stage++ }
            47 ->
                npc(
                    "I could not risk taking the message directly, for I",
                    "feared my disguise had been uncovered.",
                ).also {
                    stage++
                }
            48 ->
                npc(
                    "Lucien particularly had been taking an unhealthy",
                    "interest in my activities, and I had a gut feeling that to",
                    "make any obvious moves against the Empty Lord would",
                    "have been my undoing.",
                ).also {
                    stage++
                }
            49 ->
                npc(
                    "But Dhalak was a noble man!",
                    "I cannot believe that he would not have taken my",
                    "message immediately to Lord Saradomin!",
                ).also {
                    stage++
                }
            50 -> player("Well, it seems like he didn't, but I don't know why not...").also { stage++ }
            51 ->
                npc(
                    "Please adventurer, discover what foul fate must have",
                    "befallen him for him to have neglected his duty!",
                ).also {
                    stage++
                }
            52 ->
                npc(
                    "I have not much to offer as reward, but for these spare",
                    "robes I wore while on assignment...",
                ).also {
                    stage++
                }
            53 ->
                npc("Please find him and discover why I am cursed like this!").also {
                    CurseOfZaros.tagDialogue(player, npc.id)
                    addItemOrDrop(player, Items.GHOSTLY_ROBE_6108, 1)
                    stage++
                }
            54 -> player("Where would I be able to find this Dhalak then?").also { stage++ }
            55 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    CurseOfZaros.getLocationDialogue(npc.id, npc.location)?.joinToString("\n") ?: "",
                ).also { stage++ }
            56 -> player("Okay, well I'll try and find him for you.").also { stage = END_DIALOGUE }
            57 -> {
                end()
                if (freeSlots(player) == 0) {
                    sendDialogue(player, "You don't have space for the reward.")
                    return true
                }
                npc(
                    FaceAnim.CALM_TALK,
                    "Certainly, I am not sure how, but it returned to me by",
                    "some magic or other.",
                )
                addItem(player!!, Items.GHOSTLY_ROBE_6108, 1)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LennissaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MYSTERIOUS_GHOST_2401)
    }
}
