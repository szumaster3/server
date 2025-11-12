package content.global.random.event.certers

import content.data.GameAttributes
import content.global.random.RandomEventNPC
import core.api.animate
import core.api.playAudio
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.emote.Emotes
import core.tools.RandomFunction
import shared.consts.NPCs
import shared.consts.Sounds

/**
 * Represents the Certer NPC.
 * @author Ceikry
 */
val certersNPC = listOf(NPCs.NILES_2536, NPCs.MILES_2537, NPCs.GILES_2538).random()
// If in region 11924 = npc + 820

class CerterNPC(
    override var loot: WeightBasedTable? = null
) : RandomEventNPC(certersNPC) {

    private lateinit var pName: String
    private lateinit var phrases: Array<String>

    override fun tick() {
        if (!timerPaused) {
            if (ticksLeft <= 2) {
                player.lock(2)
                sendChat(phrases[4])
            } else if (ticksLeft <= 280 && ticksLeft % 20 == 0) {
                sendChat(phrases[RandomFunction.random(1, 3)])
            }
        }
        super.tick()
    }

    override fun talkTo(npc: NPC) {
        player.setAttribute(GameAttributes.RE_PAUSE, true)
        player.dialogueInterpreter.open(CerterDialogue(true), npc)
    }

    override fun init() {
        super.init()
        pName = player.username.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
        phrases = arrayOf(
            "Greetings $pName, I need your help.",
            "Ehem... Hello $pName, please talk to me!",
            "Hello, are you there $pName?",
            "It's really rude to ignore someone, $pName!",
            "No-one ignores me!"
        )
        player.setAttribute(GameAttributes.RE_PAUSE, false)
        player.setAttribute(GameAttributes.CERTER_REWARD, false)
        sendChat(phrases[0])
        animate(this, Emotes.BOW.animation, true)
        playAudio(player, Sounds.HAND_WAVE_2302)
    }
}
