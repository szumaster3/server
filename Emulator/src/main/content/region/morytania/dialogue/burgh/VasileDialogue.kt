package content.region.morytania.dialogue.burgh

import core.api.quest.getQuestStage
import core.api.sendChat
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class VasileDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.IN_AID_OF_THE_MYREQUE) < 1) {
            end()
            npcl(FaceAnim.HALF_GUILTY, "Get out of it! You'se aint commin' in 'ere to get our's blood or's our food!")
            sendChat(npc, "Get out of it! You'se aint commin' in 'ere to get our's blood or's our food!", 2)
        } else {
            npcl(
                FaceAnim.HALF_GUILTY,
                "Hey it's the Hero! Wow! You're amazing, how on earth did you manage to tackle those two juvinates and Gadderanks? And the town is lovely, you've done a great job!",
            )
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("You're more than welcome.").also { stage++ }
            1 ->
                options(
                    "What do you do here?",
                    "How do you like your town?",
                    "What's happening around here?",
                    "Is there anything I can do to help out?",
                    "Ok, thanks.",
                ).also {
                    stage++
                }

            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "What do you do here?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "How do you like your town?").also { stage = 4 }
                    3 -> player(FaceAnim.HALF_GUILTY, "What's happening around here?").also { stage = 5 }
                    4 -> player(FaceAnim.HALF_GUILTY, "Is there anything I can do to help out?").also { stage = 6 }
                    5 -> player(FaceAnim.HALF_GUILTY, "Ok, thanks.").also { stage = END_DIALOGUE }
                }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Huh! I'm Vasile. Ravaging after food is what I mostly do. Occasionally, I'll happen across a rat - and that's a good day - just like today!",
                ).also {
                    stage =
                        1
                }
            4 -> npcl(FaceAnim.HALF_GUILTY, "It's just perfect, thanks so much for your efforts.").also { stage = 1 }
            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh, not much really, we're all still hiding from the vampyres and eking out a living from this terrible place, hopefully soon we'll be strong enough to make the journey to the temple on the Salve.",
                ).also {
                    stage =
                        1
                }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Not as far as I know, we have some basic amenities now which is so very useful, we're all incredibly grateful to you. I've never really seen or known a hero before, so I have nothing to compare one to, but I suspect in most people's judgement, you would be one!",
                ).also {
                    stage++
                }
            7 -> npcl(FaceAnim.HALF_GUILTY, "I for one am very pleased to have met you.").also { stage = 1 }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return VasileDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VASILE_3563)
    }
}
