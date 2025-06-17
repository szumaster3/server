package content.region.morytania.phasmatys.dialogue

import core.api.*
import core.api.quest.hasRequirement
import core.game.dialogue.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.*
import kotlin.random.Random

@Initializable
class PiratePeteDialogue(player: Player? = null) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!hasRequirement(player!!, Quests.RUM_DEAL)) {
            sendDialogue(player, "The Pirate Pete is too busy to talk.").also { stage = END_DIALOGUE }
        } else {
            // Quest dialogue:
            // openDialogue(player, PiratePeteDialogueFile())
            // After questComplete / hasRequirement dialogue:
            if(npc.id == NPCs.PIRATE_PETE_2826) {
                player(FaceAnim.HALF_ASKING, "Do I know you?")
                stage = 47
            } else {
                npc(FaceAnim.HALF_ASKING, "Want a lift to Braindeath Island?")
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> showTopics(
                Topic("Okay", 1),
                Topic("Not now", 20),
                Topic("Why do I get a headache every time I see you?", 23),
                Topic("Are you any relation to Party Pete?", 32)
            )

            1 -> {
                val random = Random.nextInt(0, 6)
                when (random) {
                    0 -> npcl(FaceAnim.FRIENDLY, "All right. I'll need to use my Magical Teleporting Bottle.").also { stage = 2 }
                    1 -> npcl(FaceAnim.FRIENDLY, "All right. You know that it's going to cost extra to take your friend as well, right?").also { stage = 7 }
                    2 -> npcl(FaceAnim.FRIENDLY, "Certainly. By the way, you have a spider in your hair.").also { stage = 8 }
                    3 -> npcl(FaceAnim.FRIENDLY, "Err... sure...").also { stage = 11 }
                    4 -> npcl(FaceAnim.FRIENDLY, "Turn around.").also { stage = 12 }
                    5 -> npcl(FaceAnim.FRIENDLY,"Well I'll be more than happy to...").also { stage = 16 }
                    6 -> npc(FaceAnim.FRIENDLY,"Would you like a hand with your stuff? You seem to", "have dropped something.").also { stage = 18 }
                }
            }
            2 -> playerl(FaceAnim.FRIENDLY,"Magical Teleporting Bottle?").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Yes. Just turn around three times, then clap your hands and say the place you want to go.").also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY,"This I have to see.").also { stage++ }
            5 -> playerl(FaceAnim.FRIENDLY,"Oh well, here goes.").also { stage++ }
            6 -> playerl(FaceAnim.NEUTRAL,"One...").also { stage = 19 }

            7 -> playerl(FaceAnim.ASKING,"What friend?").also { stage = 19 }

            8 -> playerl(FaceAnim.FRIENDLY,"Oh no! Get it out, get it out!").also { stage++ }
            9 -> npcl(FaceAnim.FRIENDLY, "Sure, turn around and I'll get it for you.").also { stage++ }
            10 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage++ }
            11 -> playerl(FaceAnim.THINKING, "Why are you looking over my shoulder?").also { stage = 19 }

            12 -> npcl(FaceAnim.FRIENDLY, "Turn around.").also { stage++ }
            13 -> playerl(FaceAnim.FRIENDLY,"What?").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY,  "Turn around.").also { stage++ }
            15 -> playerl(FaceAnim.FRIENDLY," Well, I don't see what this will accomplish, but...").also { stage = 19 }

            16 -> npcl(FaceAnim.FRIENDLY, "Egad! Did you see that?").also { stage++ }
            17 -> playerl(FaceAnim.HALF_THINKING, "What? Where?").also { stage = 19 }

            18 -> playerl(FaceAnim.HALF_THINKING, "Where?").also { stage = 19 }

            19 -> end().also { travel(player, npc) }

            20 -> npcl(FaceAnim.HALF_ASKING, "I'm getting an awful headache talking to you. Any idea why?").also { stage++ }
            21 -> player(FaceAnim.NEUTRAL, "No idea whatsoever.").also { stage = 22 }
            22 -> end()

            23 -> npcl(FaceAnim.FRIENDLY, "Well, it's possibly the weight of all of your expensive items giving you a sore back.").also { stage++ }
            24 -> npcl(FaceAnim.FRIENDLY, "As a doctor I can tell you that sometimes a bad back can manifest as a headache.").also { stage++ }
            25 -> playerl(FaceAnim.FRIENDLY, "You're a doctor?").also { stage++ }
            26 -> npcl(FaceAnim.FRIENDLY, "I'm on a break.").also { stage++ }
            27 -> npcl(FaceAnim.FRIENDLY, "Regardless, I can tell you that if you hand me your most expensive items, then the pain will disappear.").also { stage++ }
            28 -> npcl(FaceAnim.FRIENDLY, "CoughonceyouturnaroundagainCough!").also { stage++ }
            29 -> playerl(FaceAnim.FRIENDLY, "What was that last bit?").also { stage++ }
            30 -> npcl(FaceAnim.FRIENDLY, "Nothing...").also { stage++ }
            31 -> end()

            32 -> npcl(FaceAnim.SAD, "Yes I am, he's my cousin.").also { stage++ }
            33 -> playerl(FaceAnim.HALF_ASKING, "Well, you don't sound too happy about it. What happened?").also { stage++ }
            34 -> npcl(FaceAnim.NEUTRAL, "Well, I arranged with all my friends to have a party at his place.").also { stage++ }
            35 -> npcl(FaceAnim.NEUTRAL, "But then I humiliated myself by trying to dance with the knights.").also { stage++ }
            36 -> npcl(FaceAnim.NEUTRAL, "All of them collapsed on me in a horrific, jangling pile.").also { stage++ }
            37 -> npcl(FaceAnim.NEUTRAL, "I tried to salvage the night by having all the balloons come down...").also { stage++ }
            38 -> playerl(FaceAnim.HALF_ASKING, "So what happened?").also { stage++ }
            39 -> npcl(FaceAnim.ANNOYED, "I didn't know that someone had swapped the balloons with cannonballs!").also { stage++ }
            40 -> npcl(FaceAnim.HALF_GUILTY, "The casualties were horrific...").also { stage++ }
            41 -> npcl(FaceAnim.HALF_GUILTY, "That was the worst fifth birthday party in the history of the world.").also { stage++ }
            42 -> playerl(FaceAnim.NEUTRAL, "I'm sure it wasn't that bad.").also { stage++ }
            43 -> npcl(FaceAnim.NEUTRAL, "Not according to the Official History of Gielinor!").also { stage++ }
            44 -> npcl(FaceAnim.HALF_CRYING, "Every edition... the pictures bring it all back...").also { stage++ }
            45 -> playerl(FaceAnim.HALF_GUILTY, "Ouch...").also { stage++ }
            46 -> end()

            47 -> npcl(FaceAnim.NEUTRAL, "Yes, you owe me some money.").also { stage++ }
            48 -> npcl(FaceAnim.NEUTRAL, "Want a lift to Port Phasmatys?").also { stage = 0 }
        }
        return true
    }

    private fun travel(player : Player, npc : NPC) {

        player.lock()
        findNPC(NPCs.PIRATE_PETE_2825)?.let { animate(it, Animations.HUMAN_THROW_DART_806) }

        faceLocation(player, Location(3680, 3533, 0))
        sendGraphics(Graphics(org.rs.consts.Graphics.STUN_BIRDIES_ABOVE_HEAD_80, 96), player.location)
        playAudio(player, Sounds.STUNNED_2727, 1)
        sendChat(player, "Ow!")

        submitIndividualPulse(
            player,
            object : Pulse(1) {
                var counter = 0
                override fun pulse(): Boolean {
                    when(counter++) {
                        0 -> {
                            sendGraphics(Graphics(org.rs.consts.Graphics.STUN_BIRDIES_ABOVE_HEAD_80, 96), player.location)
                            playAudio(player, Sounds.STUNNED_2727, 1)
                        }

                        1 -> openOverlay(player, Components.FADE_TO_BLACK_115)
                        3 -> {
                            val destination = if(npc.id == NPCs.PIRATE_PETE_2826) Location(3680, 3536, 0) else Location(2162, 5114, 1)
                            player.teleporter.send(destination)
                        }
                        6 -> openOverlay(player, Components.FADE_FROM_BLACK_170)
                        7 -> player.unlock()
                        8 -> {
                            openDialogue(player, PiratePeteTravelDialogue())
                        }
                    }
                    return true
                }
            },
        )
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PIRATE_PETE_2825,NPCs.PIRATE_PETE_2826)
}

private class PiratePeteTravelDialogue : DialogueFile() {

    private val randomDialogue = arrayOf(
        "...got into a fight with my evil twin.",
        "...hit your head on my oars while I was rowing over. Twice.",
        "...managed to fall onto my boot while I was practicing kicking.",
        "...missed your mouth while drinking from a bottle. Hence the bottle-shaped bruises.",
        "...slipped and fell down some stairs.",
        "...slipped and slid into a brick wall."
    )

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.PIRATE_PETE_2825)
        when (stage) {
            0 -> {
                player("Ooooh... my head...")
                stage++
            }
            1 -> {
                val index = Random.nextInt(randomDialogue.size)
                npcl(FaceAnim.NEUTRAL, randomDialogue[index])
                stage++
            }
            2 -> {
                player("Wow... I'm lucky I wasn't seriously hurt!")
                stage++
            }
            3 -> end()
        }
    }
}