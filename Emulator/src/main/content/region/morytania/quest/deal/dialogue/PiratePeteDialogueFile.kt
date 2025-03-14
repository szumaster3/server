package content.region.morytania.quest.deal.dialogue

import content.region.morytania.quest.deal.cutscene.MysteriousTeleportCutscene
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class PiratePeteDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.PIRATE_PETE_2825)
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "Hello there!").also { stage++ }
            1 -> npc(FaceAnim.NEUTRAL, "Mornin'.").also { stage++ }
            2 -> npc(FaceAnim.SUSPICIOUS, "Hey...you're an adventurer, right?").also { stage++ }
            3 -> player(FaceAnim.FRIENDLY, "Yes I am!").also { stage++ }
            4 -> player(FaceAnim.FRIENDLY, "Got any quests for me?").also { stage++ }
            5 -> npc(FaceAnim.NEUTRAL, "Yeah, I do, as a matter of fact!").also { stage++ }
            6 -> npc(FaceAnim.NEUTRAL, "(Ahem.)").also { stage++ }
            7 ->
                npc(
                    FaceAnim.SAD,
                    "I am a poor, dispossessed nobleman, forced by",
                    "circumstance to lurk in the middle of nowhere, soliciting",
                    "help from passers-by.",
                ).also {
                    stage++
                }
            8 ->
                npc(
                    FaceAnim.SAD,
                    "You see, my fiendish half-brother has seized my estates",
                    "and forced me into exile.",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    FaceAnim.SAD,
                    "The simple lemon farmers suffer under his tyrannous",
                    "yoke, and only a brave adventurer can lift his iron boot",
                    "from the neck of the poor.",
                ).also {
                    stage++
                }
            10 ->
                npc(
                    FaceAnim.SAD,
                    "To reclaim my lands I will need to have my family",
                    "sword returned to me so that I may present it as proof",
                    "of my rulership.",
                ).also {
                    stage++
                }
            11 -> npc(FaceAnim.ASKING, "Will you help me find my family sword?").also { stage++ }
            12 -> options("Yes!", "No.").also { stage++ }
            13 ->
                when (buttonID) {
                    1 ->
                        player(
                            FaceAnim.NEUTRAL,
                            "Yes! Your uncorroborated sob story has touched my",
                            "heart. When do we set off?",
                        ).also {
                            stage +=
                                3
                        }
                    2 -> player("No, I don't think I'll help you out this time.").also { stage++ }
                }
            14 ->
                npc(
                    FaceAnim.SAD,
                    "Look, I think I'll wait here for someone a little",
                    "more... you know...",
                ).also { stage++ }
            15 -> npc(FaceAnim.SAD, "...heroic.").also { stage = END_DIALOGUE }
            16 -> npc(FaceAnim.HAPPY, "You'll help! Wonderful!").also { stage++ }
            17 ->
                npc(
                    FaceAnim.SAD,
                    "But, alas, my half brother has a powerful ally, the",
                    "mighty demon...",
                ).also { stage++ }
            18 -> npc(FaceAnim.SUSPICIOUS, "Err...").also { stage++ }
            19 -> npc(FaceAnim.SUSPICIOUS, "Err...Barrelor!").also { stage++ }
            20 ->
                npc(
                    FaceAnim.SAD,
                    "Yes, the mighty, fearsome, tall, deadly, oaken, round",
                    "demon Barrelor the Destroyer.",
                ).also {
                    stage++
                }
            21 -> player(FaceAnim.HALF_ASKING, "Barrelor?").also { stage++ }
            22 -> npc(FaceAnim.ANGRY, "That's what I said!").also { stage++ }
            23 ->
                npc(
                    FaceAnim.SAD,
                    "Barrelor is an awesome opponent, and to reclaim my",
                    "family sword you will need to defeat him, for he guards",
                    "it within the deadly Trapped Pit of Barrelor.",
                ).also {
                    stage++
                }
            24 -> npc(FaceAnim.HALF_ASKING, "Wanna give it a shot?").also { stage++ }
            25 -> options("Of course, I fear no demon!", "Not a chance, this sounds too dangerous.").also { stage++ }
            26 ->
                when (buttonID) {
                    1 -> player(FaceAnim.HAPPY, "Of course, I fear no demon!").also { stage++ }
                    2 -> player("Not a chance, this sounds too dangerous.").also { stage = 14 }
                }
            27 -> npc(FaceAnim.HAPPY, "Atta " + if (!player!!.isMale) "girl" else "boy" + "!").also { stage++ }
            28 ->
                npc(
                    FaceAnim.HAPPY,
                    "When I am reinstated in my rightful place, I will not",
                    "be a very wealthy man, as my half-brother has",
                    "squandered my family fortune.",
                ).also {
                    stage++
                }
            29 ->
                npc(
                    FaceAnim.HAPPY,
                    "However, I will gladly give you every bent penny of",
                    "what is left, and starve in the gutter with my many,",
                    "adorable children, if you say you will help me.",
                ).also {
                    stage++
                }
            30 ->
                options(
                    "Nonsense! Keep the money!",
                    "Great, I'll take the cash in used coins please.",
                ).also { stage++ }
            31 ->
                when (buttonID) {
                    1 ->
                        player(
                            FaceAnim.HAPPY,
                            "Nonsense! Keep the money! I will dispose of this evil",
                            "half-brother of yours and leave you what little money is",
                            "left to feed your family.",
                        ).also {
                            stage +=
                                2
                        }
                    2 -> player("Great, I'll take the cash in used coins please.").also { stage++ }
                }
            32 -> npc("Er...").also { stage = 14 }
            33 -> npc(FaceAnim.HAPPY, "Wonderful! Just pick up your diversion and we'll leave!").also { stage++ }
            34 ->
                player(FaceAnim.HALF_ASKING, "What diversion?").also {
                    if (player!!.location.x == 3681) {
                        player!!.faceLocation(Location(3683, 3537, 0))
                    } else {
                        player!!.faceLocation(Location(3680, 3534, 0))
                    }
                    stage++
                }

            35 -> {
                end()
                lock(player!!, 3)
                findNPC(NPCs.PIRATE_PETE_2825)?.let { animate(it, Animations.HUMAN_THROW_DART_806) }
                sendChat(player!!, "Ow!")
                submitIndividualPulse(
                    player!!,
                    object : Pulse(3) {
                        override fun pulse(): Boolean {
                            sendGraphics(
                                Graphics(org.rs.consts.Graphics.STUN_BIRDIES_ABOVE_HEAD_80, 96),
                                player!!.location,
                            )
                            playAudio(player!!, Sounds.STUNNED_2727, 1)
                            MysteriousTeleportCutscene(player!!).start()
                            return true
                        }
                    },
                )
            }
        }
    }
}
