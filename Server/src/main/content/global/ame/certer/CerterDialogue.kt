package content.global.ame.certer

import content.data.GameAttributes
import core.api.addItemOrDrop
import core.api.animate
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.node.entity.impl.PulseType
import core.game.system.timer.impl.AntiMacro
import core.tools.END_DIALOGUE
import shared.consts.Animations
import shared.consts.Components

/**
 * Represents the Certer dialogue.
 * @author Ceikry (March 18, 2021)
 */
class CerterDialogue(
    val initial: Boolean,
) : DialogueFile() {
    val CERTER_INTERFACE = Components.MACRO_CERTER_IDENTIFY_AN_OBJECT_184

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (initial && !player!!.getAttribute(GameAttributes.CERTER_REWARD, false)) {
            when (stage) {
                0 ->
                    npc(
                        "Ah, hello, ${player!!.username.replaceFirstChar {
                            if (it.isLowerCase()) {
                                it.titlecase()
                            } else {
                                it
                                    .toString()
                            }
                        }}. Could you",
                        "please help me identify this?",
                    ).also { stage++ }

                1 -> {
                    end()
                    player!!.interfaceManager.open(Component(CERTER_INTERFACE))
                }
            }
        } else {
            player!!.setAttribute(GameAttributes.RE_PAUSE, true)
            val isCorrect = player!!.getAttribute(GameAttributes.CERTER_CORRECT, false)
            val receivedReward = player!!.getAttribute(GameAttributes.CERTER_REWARD, false)
            if (receivedReward == true) {
                stage = 1
            }
            when (stage) {
                0 ->
                    if (!isCorrect) {
                        npc("Sorry, I don't think so.").also {
                            player!!.setAttribute(GameAttributes.CERTER_REWARD, true)
                            stage = END_DIALOGUE
                            AntiMacro.terminateEventNpc(player!!)
                        }
                    } else {
                        npc("Thank you, I hope you like your present. I must be", "leaving now though.").also {
                            player!!.setAttribute(GameAttributes.CERTER_REWARD, true)
                            stage = END_DIALOGUE
                            AntiMacro.rollEventLoot(player!!).forEach { addItemOrDrop(player!!, it.id, it.amount) }
                        }
                    }
            }
        }
    }

    override fun end() {
        super.end()
        if (player!!.getAttribute(GameAttributes.CERTER_REWARD, false)) {
            npc!!.pulseManager.clear(PulseType.STANDARD)
            animate(npc!!, Animations.WAVE_863)
            AntiMacro.terminateEventNpc(player!!)
        } else {
            player!!.setAttribute(GameAttributes.RE_PAUSE, false)
        }
    }
}
