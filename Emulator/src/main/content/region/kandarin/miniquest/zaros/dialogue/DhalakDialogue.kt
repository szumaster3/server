package content.region.kandarin.miniquest.zaros.dialogue

import content.region.kandarin.miniquest.zaros.CurseOfZaros
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class DhalakDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            player("All your base are belong to us.")
            return true
        }

        if (CurseOfZaros.hasTag(player, npc.id) || CurseOfZaros.hasComplete(player)) {
            val item = Items.GHOSTLY_HOOD_6109
            if (!CurseOfZaros.hasItems(player, item)) {
                player(FaceAnim.SAD, "Could I have that hat again? I seem to have", "misplaced it somewhere...").also {
                    stage =
                        39
                }
            } else {
                sendDialogue(player, "You have already talked to this NPC.").also { stage = END_DIALOGUE }
            }
            return true
        }

        player("Hello Dhalak.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    "You see my form, hear my words, and know my name,",
                    "yet your face I recognise not...",
                ).also { stage++ }
            1 -> npc("Be you some mighty sorcerer to bind me so?").also { stage++ }
            2 ->
                if (getStatLevel(player, Skills.MAGIC) < 99) {
                    player("Um...", "Well, not really...").also { stage++ }
                } else {
                    player("Well, I don't mean to brag,", "but I guess I am with my level 99 magic...").also { stage++ }
                }
            3 ->
                player(
                    "But that is besides the point. It is not I who has trapped",
                    "you here as a ghost.",
                ).also { stage++ }
            4 -> npc("Then how comes it to be that you know my name, stranger?").also { stage++ }
            5 -> player("Lennissa told me about you, and where to find you.").also { stage++ }
            6 ->
                npc(
                    "Lennissa? Oh that poor sweet girl... Has my foolishness",
                    "cursed her as well as myself???",
                ).also { stage++ }
            7 -> player("Your foolishness?").also { stage++ }
            8 -> npc("The story shames me, stranger. I wouldst rather keep it", "unto myself.").also { stage++ }
            9 -> options("tell em your story", "Goodbye then").also { stage++ }
            10 ->
                when (buttonId) {
                    1 ->
                        player(
                            "Look, I don't want to force you into telling me, but",
                            "perhaps sharing it with someone might relieve your",
                            "guilt?",
                        ).also {
                            stage++
                        }

                    2 ->
                        player("Well, that's all fascinating, but I just don't particularly care.", "Bye-bye.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            11 ->
                npc(
                    "Aye... Perhaps it might at that. So what has Lennissa told",
                    "you of the events of the day the curse befell me?",
                ).also {
                    stage++
                }
            12 ->
                player(
                    "Well, she told me that she was working as an undercover agent",
                    "of Saradomin amongst the followers of some 'Empty Lord', and",
                    "when news of the theft of a god-weapon reached her, she passed",
                    "the message on to you instead of taking it to Saradomin because",
                    "she was scared her cover might be blown.",
                ).also {
                    stage++
                }
            13 -> npc("Aye, that is a fair account of events...").also { stage++ }
            14 -> player("But I don't understand why you didn't take her message to", "Saradomin?").also { stage++ }
            15 ->
                npc(
                    "Stranger, my foolishness was a result of my respect for",
                    "Saradomin, not as a result of any attempted treachery!",
                ).also {
                    stage++
                }
            16 ->
                player(
                    "So why didn't you pass on Lennissa's message? As I understand",
                    "it, something happened with the staff that could have been",
                    "avoided if you had passed her message on!",
                ).also {
                    stage++
                }
            17 ->
                npc(
                    "*sigh* I know not what occurred with that god-weapon, but I",
                    "have my suspicions... Let me explain myself. I was Lennissa's",
                    "immediate superior, and I was often her contact for missions.",
                ).also {
                    stage++
                }
            18 ->
                npc(
                    "Because of this role, I had access to a larger picture of",
                    "what was happening than she herself did, and I was not only",
                    "well aware that her presence amongst the enemy camp was",
                    "detected, but I also was aware that there was a growing",
                    "faction amongst them who were plotting against their master.",
                ).also {
                    stage++
                }
            19 -> player("Their master being...?").also { stage++ }
            20 ->
                npc(
                    "That I will not tell you. I will tempt the fates no more",
                    "than I already have done. But anyway, it had become clear to",
                    "me that the Mahjarrat who had been liberated from the control",
                    "of Icthlarin did not much appreciate one form of slavery to",
                    "another.",
                ).also {
                    stage++
                }
            21 ->
                npc(
                    "Under the leadership of the mighty Zamorak, they were making",
                    "plans to overthrow their master and take his power for",
                    "themselves.",
                ).also {
                    stage++
                }
            22 ->
                npc(
                    "As powerful, long-lived and evil as they were, they were still",
                    "just mortal, and I made the decision that it would be of benefit",
                    "to my Lord Saradomin for his mightiest rival to be distracted",
                    "by such internal conflicts.",
                ).also {
                    stage++
                }
            23 -> player("So that is why you decided not to pass the report from Lennissa on?").also { stage++ }
            24 ->
                npc(
                    "Yes, but my guilt is more than simply inaction... I knew that",
                    "with such a weapon, Zamorak would be capable of launching an",
                    "attack that could actually stand a chance of success.",
                ).also {
                    stage++
                }
            25 ->
                npc(
                    "But I also knew that he would never be able to get a chance",
                    "to use it in battle, for being a god-weapon its presence",
                    "would have sung out to their leader.",
                ).also {
                    stage++
                }
            26 -> player("I'm guessing you did something about that, then?").also { stage++ }
            27 ->
                npc(
                    "Indeed I did. To my eternal shame, I decided that I would",
                    "assist Zamorak and his henchmen in their battle by secretly",
                    "casting a spell of concealment upon the staff so that they",
                    "might use it secretly against their master.",
                ).also {
                    stage++
                }
            28 -> player("So Zamorak knew about this?").also { stage++ }
            29 ->
                npc(
                    "No, nobody except myself, and now you, knew that I cast such",
                    "a spell... Had I known what a threat to my Lord Saradomin he",
                    "would later become, I wouldst have taken the message to",
                    "Saradomin immediately! Alas, it is all too easy to see your",
                    "mistakes after having made them...",
                ).also {
                    stage++
                }
            30 ->
                player(
                    "I'm confused. What exactly happened with the staff anyway?",
                    "And why have all those various random people been cursed",
                    "because of it?",
                ).also {
                    stage++
                }
            31 ->
                npc(
                    "I cannot answer your question with anything other than my",
                    "own suppositions, but I do know of one who might be able to,",
                    "and if any man deserved to be cursed for their actions that",
                    "day, it was he!",
                ).also {
                    stage++
                }
            32 -> player("Who are you speaking of?").also { stage++ }
            33 ->
                npc(
                    "His name is Viggora. He was an evil man, brutal and vicious,",
                    "and deadly with a blade. He was one of the few humans",
                    "Zamorak allowed to rise to a position of power amongst his",
                    "rebels, possibly because he imitated those same qualities of",
                    "Zamorak.",
                ).also {
                    stage++
                }
            34 ->
                npc(
                    "If anyone knows what Zamorak did with that god-weapon to have",
                    "caused this curse to have befallen us, it would have been he,",
                    "for he would have been fighting on Zamorak's very right-hand",
                    "side in their rebellion.",
                ).also {
                    stage++
                }
            35 ->
                npc(
                    "Please, if this curse can be lifted, find Viggora and find out",
                    "what he wrought upon us! I have no wealth nor magic to aid you,",
                    "but take my hood as reward; It has served me well these",
                    "centuries past, and may bring you luck.",
                ).also {
                    CurseOfZaros.tagDialogue(player, npc.id)
                    addItemOrDrop(player, Items.GHOSTLY_HOOD_6109, 1)
                    stage++
                }
            36 -> player("Where would you suggest I look for Viggora?").also { stage++ }
            37 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    CurseOfZaros.getLocationDialogue(npc.id, npc.location)?.joinToString("\n") ?: "",
                ).also { stage++ }
            38 -> player("Okay, well I'll try and find him for you then.").also { stage = END_DIALOGUE }
            39 -> {
                end()
                if (freeSlots(player) == 0) {
                    sendDialogue(player, "You don't have space for the reward.")
                    return true
                }
                npc(FaceAnim.CALM, "Certainly, I am not sure how, but it returned to me by some magic or other.")
                addItem(player!!, Items.GHOSTLY_HOOD_6109, 1)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DhalakDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MYSTERIOUS_GHOST_2398)
    }
}
