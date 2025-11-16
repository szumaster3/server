package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class AbyssalParasiteDialogue : Dialogue {

    override fun newInstance(player: Player?): Dialogue = AbyssalParasiteDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    private val npcLines = listOf(
        "Ongk n'hd?"    to 0,
        "Noslr'rh..."   to 5,
        "Ace'e e ur'y!" to 9,
        "Tdsa tukk!"    to 10,
        "Tdsa tukk!"    to 12
    )

    private val playerResponses = mapOf(
        0  to listOf(FaceAnim.HALF_WORRIED to "Oh, I'm not feeling so well.", FaceAnim.SAD to "Please have mercy!", FaceAnim.AFRAID to "I shouldn't have eaten that kebab. Please stop talking!"),
        5  to listOf(FaceAnim.HALF_ASKING to "What's the matter?", FaceAnim.HALF_ASKING to "Could you...could you mime what the problem is?"),
        9  to listOf(FaceAnim.HALF_ASKING to "I want to help it but, aside from the language gap its noises make me retch!"),
        10 to listOf(FaceAnim.HALF_WORRIED to "I think I'm going to be sick... The noises! Oh, the terrifying noises."),
        12 to listOf(FaceAnim.AFRAID to "Oh, the noises again.")
    )

    private val npcFollowUps = mapOf(
        0   to listOf("Uge f't es?", "F'tp ohl't?"),
        5   to listOf("Kdso Seo...", "Yiao itl!"),
        12  to listOf("Hem s'htee?")
    )

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val (line, startStage) = npcLines.random()
        stage = startStage
        npcl(FaceAnim.CHILD_NORMAL, line)
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val responses = playerResponses[stage] ?: run {
            stage = END_DIALOGUE
            return true
        }

        val index = (buttonId - 1).coerceIn(responses.indices)
        val (anim, text) = responses[index]
        playerl(anim, text)

        val followUps = npcFollowUps[stage]
        if (followUps != null && index < followUps.size) {
            npcl(FaceAnim.CHILD_NORMAL, followUps[index])
        }

        stage = END_DIALOGUE
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ABYSSAL_PARASITE_6818, NPCs.ABYSSAL_PARASITE_6819)
}
