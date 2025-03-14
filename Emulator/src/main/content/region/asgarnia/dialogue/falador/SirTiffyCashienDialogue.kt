package content.region.asgarnia.dialogue.falador

import content.data.RespawnPoint
import content.data.setRespawnLocation
import content.region.asgarnia.quest.rd.cutscene.SirTiffyCashienDialogueFile
import core.ServerConstants
import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.isQuestComplete
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SirTiffyCashienDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestInProgress(player!!, Quests.RECRUITMENT_DRIVE, 1, 99)) {
            openDialogue(player, SirTiffyCashienDialogueFile(), npc)
            return true
        } else {
            playerl(FaceAnim.HAPPY, "Hello.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    "What ho, " + (if (player.isMale) "sirrah" else "milady") + ". Spiffing day for a walk in the park,",
                    "what?",
                ).also { stage++ }
            1 -> player("Spiffing?").also { stage++ }
            2 ->
                npc(
                    "Absolutely, top-hole! Well, can't stay and chat all day,",
                    "dontchaknow! Ta-ta for now!",
                ).also { stage++ }
            3 -> {
                if (!isQuestComplete(player, Quests.RECRUITMENT_DRIVE)) {
                    playerl(FaceAnim.HALF_GUILTY, "Erm...goodbye.").also { stage = END_DIALOGUE }
                } else {
                    options(
                        "Do you have any jobs for me yet?",
                        "Can you explain the Gaze of Saradomin to me?",
                        "Can I buy some armours?",
                        "Can I switch respawns please?",
                        "Goodbye.",
                    ).also {
                        stage =
                            10
                    }
                }
            }

            10 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Do you have any jobs for me yet?").also { stage = 11 }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I don't really understand this 'Gaze of Saradomin' thing... Do you think you could explain what it does for me?",
                        ).also {
                            stage =
                                13
                        }
                    3 -> playerl(FaceAnim.FRIENDLY, "Can I buy some armours?").also { stage = 19 }
                    4 ->
                        if (player.properties.spawnLocation == ServerConstants.HOME_LOCATION) {
                            playerl(
                                FaceAnim.FRIENDLY,
                                "Hi Tiffy, I was wondering, can I change my respawn point to Falador?",
                            ).also {
                                stage =
                                    22
                            }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "Can I change my respawn point back to Lumbridge?").also {
                                stage = 21
                            }
                        }

                    5 -> playerl(FaceAnim.FRIENDLY, "Goodbye.").also { stage = END_DIALOGUE }
                }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Sorry dear " + (if (player!!.isMale) "boy" else "gal") +
                        " but we are still in the process of organising. I'm sure that we will have something for you soon, so please feel free to check back later.",
                ).also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "Anything else I can do for you in the meantime?").also { stage = 10 }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Certainly " + (if (player!!.isMale) "sirrah" else "milady") +
                        " As you know, we Temple Knights are personally favoured by Saradomin himself. And when I say personally favoured, I don't mean that some time in the future he's going to buy us all a drink!",
                ).also { stage++ }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "He watches over us, and, when we die, we're offered the chance to respawn in Falador Castle, ready to get on with our adventures.",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Now, this doesn't happen if we die in especially evil places like the Wilderness, but it can be a big help if you're out slaying dragons or whatever.",
                ).also {
                    stage++
                }
            16 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Our equipment isn't protected any more than usual, but it's a small price to pay to be hale and hearty again, what?",
                ).also {
                    stage++
                }
            17 -> playerl(FaceAnim.FRIENDLY, "Thank you.").also { stage = 12 }
            18 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Was there something else you wanted to ask good old Tiffy, " +
                        (if (player!!.isMale) "sirrah" else "milady") +
                        "?",
                ).also { stage = 10 }
            19 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Of course, dear " + (if (player!!.isMale) "sirrah" else "milady") + ".",
                ).also { stage++ }
            20 -> {
                end()
                openNpcShop(player, NPCs.SIR_TIFFY_CASHIEN_2290)
            }
            21 ->
                npcl(FaceAnim.HALF_GUILTY, "What? You're saying you want to respawn in Lumbridge? Are you sure?").also {
                    stage =
                        26
                }
            22 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah, so you'd like to respawn in Falador, the good old homestead! Are you sure?",
                ).also {
                    stage++
                }
            23 ->
                options(
                    "Yes, I want to respawn in Falador.",
                    "Actually, no thanks. I like my respawn point.",
                ).also { stage++ }
            24 ->
                when (buttonId) {
                    1 -> player("Yes, I want to respawn in Falador.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Actually, no thanks. I like my respawn point.").also { stage = 29 }
                }
            25 -> {
                npcl(FaceAnim.FRIENDLY, "Top-hole, what? Good old Fally is definitely the hot-spot nowadays!")
                player.setRespawnLocation(RespawnPoint.FALADOR)
                stage = END_DIALOGUE
            }
            26 ->
                options(
                    "Yes, I want to respawn in Lumbridge.",
                    "Actually, no thanks. I like my respawn point.",
                ).also { stage++ }
            27 ->
                when (buttonId) {
                    1 -> player("Yes, I want to respawn in Lumbridge.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Actually, no thanks. I like my respawn point.").also { stage = 29 }
                }
            28 -> {
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Why anyone would want to visit that smelly little swamp village of oiks is quite beyond me, I'm afraid, but the deed is done now.",
                )
                player.setRespawnLocation(RespawnPoint.LUMBRIDGE)
                stage = END_DIALOGUE
            }
            29 -> npcl(FaceAnim.FRIENDLY, " As you wish, what? Ta-ta for now.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_TIFFY_CASHIEN_2290)
    }
}
