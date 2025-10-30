package content.region.kandarin.miniquest.secret_ghost.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items

/**
 * Represents the [RennardDialogue] & [RennardDialogueFile].
 */
@Initializable
class RennardDialogue(player: Player? = null) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, RennardDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): Dialogue = RennardDialogue(player)
    override fun getIds(): IntArray = intArrayOf(2382, 2383, 2384)
}

private class RennardDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> {
                val sequence = getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0)
                val hasItem = hasAnItem(player!!, Items.GHOSTLY_GLOVES_6110).container != null
                when {
                    // Talk without ghostspeak amulet.
                    !inEquipment(player!!, Items.GHOSTSPEAK_AMULET_552) -> {
                        openDialogue(player!!, RandomDialogue(), npc!!)
                    }
                    // Talk again if lost ghostly item.
                    !hasItem && getAttribute(player!!, GameAttributes.ZAROS_RENNARD_TALK, false) -> {
                        player(FaceAnim.SAD, "I lost those gloves you gave me...", "Can I have some more please?").also { stage = 48 }
                    }
                    // Talk again.
                    getAttribute(player!!, GameAttributes.ZAROS_RENNARD_TALK, false) -> {
                        playerl(FaceAnim.ASKING,"Can you tell me where I can find Kharrim again?").also { stage = (100 + getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0)) }
                    }
                    // First talk.
                    else -> {
                        // Correct path.
                        if(2381 + sequence == npc!!.id) {
                            player("Hello.", "You must be Rennard.").also { stage++ }
                        } else {
                            openDialogue(player!!, WrongLocationDialogue(), npc!!)
                        }
                    }
                }
            }
            1 -> npc(FaceAnim.SCARED, "What be this?", "You both see me and hear me, and also know my","name?").also { stage++ }
            2 -> npc(FaceAnim.ANGRY, "Tell me what devilry brings you here, and speak quick", "about it afore I gut you like a fish!").also { stage++ }
            3 -> player("Well apart from the fact I ain't scared of no ghost, I", "am here because I have spoken to Valdez.").also { stage++ }
            4 -> npc(FaceAnim.HALF_THINKING,"Valdez?", "Who be that?", "Some foul necromancer?").also { stage++ }
            5 -> player("No, he was a ghost I met near Glarial's tomb.").also { stage++ }
            6 -> player("He seems convinced that the artefact you stole from", "him is responsible for him becoming cursed to be an", "invisible ghost.").also { stage++ }
            7 -> player("Seems like he might be onto something too, given the", "state of you.").also { stage++ }
            8 -> npc(FaceAnim.HALF_THINKING,"A curse ye say...", "Aye, that makes sense...").also { stage++ }
            9 -> npc(FaceAnim.SAD, "And there was I thinking the fate be the fault of the", "thieving and murdering I spent me life a-doing...").also { stage++ }
            10 -> npc(FaceAnim.CALM_TALK, "So it all began the day I stole that staff, ye say?","Aye, that be a story I have never told another soul...").also { stage++ }
            11 -> options("Tell em your story", "Goodbye then").also { stage++ }
            12 -> when (buttonID) {
                1 -> player("Why don't you tell me what happened?", "I might be able to help...").also { stage++ }
                2 -> player("Well, that's all fascinating, but I just don't particularly care.", "Bye-bye.").also { stage = END_DIALOGUE }
            }
            13 -> npc(FaceAnim.NEUTRAL,"Well, I was making me merry way along, having just", "pulled of a glorious jewellery heist from a bunch of", "stinking dwarves...").also { stage++ }
            14 -> player(FaceAnim.ANGRY, "Hey, that's no way to talk about dwarves!", "Some of my best friends are short!").also { stage++ }
            15 -> npc(FaceAnim.NEUTRAL,"Ah, yer misunderstand me ${if(player!!.isMale) "lad" else "lass"}, I wasn't generalising", "about the whole dwarf species, I had just stolen a", "bundle of jewels from a very specific group of dwarves", "who happened to have an odious stench about them!").also { stage++ }
            16 -> player("Oh.", "Well I guess that's okay then.", "Please continue.").also { stage++ }
            17 -> npc(FaceAnim.NEUTRAL,"Well, as I headed on me merry way, hoping the foul", "odours that lingered in me nostrils would soon pass, I", "see in front of me this explorer fella, all decked out in", "his fine clothing, and carrying some long package").also { stage++ }
            18 -> npc(FaceAnim.NEUTRAL,"bundled in rags.").also { stage++ }
            19 -> npc(FaceAnim.NEUTRAL,"So I says to myself, 'Rennard', I says, 'Rennard, why", "would some fella all dressed in finery be carrying", "something wrapped in dirty rags?'.").also { stage++ }
            20 -> npc(FaceAnim.NEUTRAL,"So I thinks to meself a little more, 'Rennard', I thinks,", "'Rennard, maybe that fella has something valuable in", "there, and covered it in dirty rags so it don't look so", "valuable'.").also { stage++ }
            21 -> npc(FaceAnim.NEUTRAL,"So I coshed this fella round the back of his head with", "me bag of jewels, picked up his package and was on me", "merry way afore he comes to.").also { stage++ }
            22 -> player(FaceAnim.HALF_THINKING,"So what happened then?").also { stage++ }
            23 -> npc(FaceAnim.NEUTRAL,"Well, I makes me way to the closest tavern I knew of", "that catered to my sort of people...").also { stage++ }
            24 -> player(FaceAnim.HALF_THINKING,"You mean thieves?").also { stage++ }
            25 -> npc(FaceAnim.NEUTRAL,"Right ye are, so I makes me way to the nearest", "friendly tavern, and unwraps the bundle to see what it", "had inside.").also { stage++ }
            26 -> player(FaceAnim.HALF_THINKING,"The Staff of Armadyl?").also { stage++ }
            27 -> npc(FaceAnim.HALF_THINKING, "Was it?", "Ah, I never knew that...").also { stage++ }
            28 -> npc(FaceAnim.NEUTRAL,"Anysways, I unwrapped this staff, and sees it be a god-", "weapon;", "I may be just a common thief, but I recognises a", "weapon not made by mortal hands when I sees one.").also { stage++ }
            29 -> player(FaceAnim.HALF_THINKING,"So what did you do then?").also { stage++ }
            30 -> npc(FaceAnim.NEUTRAL,"Well, I knew such a weapon would be of great value", "to...").also { stage++ }
            31 -> npc(FaceAnim.NEUTRAL,"Now, that's funny.", "Can't remember his name, now.", "The powerful god, lived in the North-east.", "Took the Mahjarrat away from under Icthlarin's").also { stage++ }
            32 -> npc(FaceAnim.NEUTRAL,"control.").also { stage++ }
            33 -> npc(FaceAnim.NEUTRAL,"Anyway, I hired me a messenger to go off and let him", "know I had something I was prepared to sell that I", "thought he'd be interested in...").also { stage++ }
            34 -> npc(FaceAnim.NEUTRAL,"Now WHY can't I remember his name?", "Very odd that...").also { stage++ }
            35 -> player(FaceAnim.HALF_THINKING,"So you sold the staff to this god you can't remember?").also { stage++ }
            36 -> npc(FaceAnim.NEUTRAL,"Well, that's the other funny thing...", "He never showed up, he sent some General or other", "instead.").also { stage++ }
            37 -> npc(FaceAnim.NEUTRAL,"Hmmm...", "You know...", "Thinking back on that, I'm getting the feeling that", "messenger did a little doublecross of his own, and took").also { stage++ }
            38 -> npc(FaceAnim.NEUTRAL,"me message to the wrong fella.").also { stage++ }
            39 -> player("So what was this Generals' name?").also { stage++ }
            40 -> npc(FaceAnim.NEUTRAL,"His name was Zamorak.", "I remember thinking at the time it was odd, because the", "fella was a mighty powerful warrior, but he was never fully", "trusted by...").also { stage++ }
            41 -> npc(FaceAnim.ANGRY, "WHY can't I remember his name???").also { stage++ }
            42 -> player("So you suspect the messenger might have taken the", "message to the wrong person?", "So you think it was an accident or deliberate?").also { stage++ }
            43 -> npc(FaceAnim.NEUTRAL,"Well that I can't tell ya, but if something happened to", "get me cursed, it's likely the messenger would know", "what more than me.").also { stage++ }
            44 -> npc(FaceAnim.ANGRY, "His name was Kharrim, and if he caused me to be", "stuck like this, I'm gonna fillet him like a dog, ghost or", "no!").also { stage++ }
            45 -> npc(FaceAnim.NEUTRAL, "I tell ye what, you've given me much of think about so","I'd like to offer yer a gift;", "Here, take these, they were the gloves I stole me first","cake with, they might bring yer some luck.").also {
                setAttribute(player!!, GameAttributes.ZAROS_RENNARD_TALK, true)
                addItemOrDrop(player!!, Items.GHOSTLY_GLOVES_6110, 1)
                stage++
            }
            46 -> player(FaceAnim.HALF_THINKING, "Where can I find Kharrim then?").also { stage = (100 + getAttribute(player!!, GameAttributes.ZAROS_PATH_SEQUENCE, 0)) }
            47 -> player(FaceAnim.NEUTRAL, "Okay, well I'll try and find him for you then.").also { stage = END_DIALOGUE }
            48 -> {
                if (freeSlots(player!!) == 0) {
                    end()
                    sendDialogue(player!!, "You don't have space for the reward.")
                } else {
                    npc(FaceAnim.SAD, "It seems as though the curse that keeps me here", "extends to my very clothing...")
                    stage++
                }
            }
            49 -> {
                end()
                addItem(player!!, Items.GHOSTLY_GLOVES_6110, 1)
                npc(FaceAnim.CALM, "Here, take them, some evil power returned them to", "me.")
            }
            // First path.
            101 -> npc(FaceAnim.HALF_GUILTY, "Kharrim the messenger...", "The last I'd heard of that weasel he was claiming he'd", "found some underground deposit of runite ore guarded", "by demons and dragons.").also { stage = 201 }
            201 -> npc(FaceAnim.HALF_GUILTY, "I suspect he was pulling some scam or other, but if you", "know of such a place, that might be a good place to", "start checking.").also { stage = 47 }
            // Second path.
            102 -> npc(FaceAnim.HALF_GUILTY, "Kharrim the messenger...", "Well, he was always a devoted follower of old General", "Zamorak, and if I remember rightly Zamorak set up a", "small base in an old temple near Dareeyak...").also { stage = 202 }
            202 -> npc(FaceAnim.HALF_GUILTY, "You might want to check around there.").also { stage = 47 }
            // Third path.
            103 -> npc(FaceAnim.HALF_GUILTY, "Kharrim the messenger...", "Last I'd heard of him, he'd headed off to Carrallagar", "to seek his fortune. Ya might want to check around", "there somewhere.").also { stage = 47 }
        }
    }
}
