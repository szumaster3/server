package content.region.kandarin.quest.merlin.dialogue

import content.region.kandarin.quest.merlin.cutscene.CrateCutscene
import core.api.sendDialogue
import core.api.sendDialogueLines
import core.api.sendDialogueOptions
import core.api.setComponentVisibility
import core.game.dialogue.DialogueFile
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs.consts.Animations

class CrateDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> {
                sendDialogueLines(player!!, "The crate is empty. It's just about big enough to hide inside.")
                stage++
            }

            1 -> {
                sendDialogueOptions(player!!, "Hide inside the crate?", "Yes.", "No.")
                stage++
            }

            2 -> {
                if (buttonID == 1) {
                    sendDialogue(player!!, "You climb inside the crate and wait.")
                    player!!.animate(Animation.create(Animations.HUMAN_BURYING_BONES_827))
                    stage++
                } else {
                    sendDialogue(player!!, "You leave the empty crate alone.")
                    stage = END_DIALOGUE
                }
            }

            3 -> {
                CrateCutscene(player!!).start()
                sendDialogue(player!!, "You wait.")
                stage++
            }

            4 -> {
                sendDialogue(player!!, "And wait...")
                stage++
            }

            5 -> {
                sendDialogue(
                    player!!,
                    "You hear voices outside the crate. <col=08088A>Is this your crate Arnhein?</col>",
                )
                stage++
            }

            6 -> {
                sendDialogue(
                    player!!,
                    "<col=8A0808>Yeah I think so. Pack it aboard as soon as you can.</col> <col=8A0808>I'm on a tight schedule for deliveries!</col>",
                )
                stage++
            }

            7 -> {
                sendDialogue(
                    player!!,
                    "You feel the crate being lifted <col=08088A>Oof. Wow, this is pretty heavy!</col> <col=08088A>I never knew candles weighed so much!</col>",
                )
                stage++
            }

            8 -> {
                sendDialogue(player!!, "<col=8A0808>Quit your whining, and stow it in the hold.</col>")
                stage++
            }

            9 -> {
                sendDialogue(player!!, "You feel the crate being put down inside the ship.")
                stage++
            }

            10 -> {
                sendDialogue(player!!, "You wait...")
                stage++
            }

            11 -> {
                sendDialogue(player!!, "And wait...")
                stage++
            }

            12 -> {
                sendDialogue(player!!, "<col=8A0808>Casting off!</col>")
                stage++
            }

            13 -> {
                sendDialogue(player!!, "You feel the ship start to move.")
                stage++
            }

            14 -> {
                sendDialogue(player!!, "Feels like you're now out at sea.")
                stage++
            }

            15 -> {
                sendDialogue(player!!, "The ship comes to a stop.")
                stage++
            }

            16 -> {
                sendDialogue(
                    player!!,
                    "<col=8A0808>Unload Mordred's deliveries onto the jetty.</col> <col=08088A>Aye-aye cap'n!</col>",
                )
                stage++
            }

            17 -> {
                sendDialogue(player!!, "You feel the crate being lifted.")
                stage++
            }

            18 -> {
                sendDialogue(
                    player!!,
                    "You can hear someone mumbling outside the crate. <col=08088A>...stupid Arhein... making me... candles... <col=08088A>never weigh THIS much....hurts....union about this!...",
                )
                stage++
            }

            19 -> {
                sendDialogue(player!!, "<col=08088A>...if....MY ship be different!... <col=08088A>stupid Arhein...")
                stage++
            }

            20 -> {
                sendDialogue(player!!, "You feel the crate being put down.")
                stage++
            }

            21 -> {
                setComponentVisibility(player!!, 228, 6, true)
                setComponentVisibility(player!!, 228, 9, false)
                sendDialogueOptions(player!!, "Would you like to get back out of the crate?", "Yes.", "No.")
                stage++
            }

            22 -> {
                if (buttonID == 1) {
                    sendDialogue(player!!, "You climb out of the crate.")
                    stage++
                } else {
                    sendDialogue(player!!, "You decide to stay in the crate.")
                    stage = 30
                }
            }

            23 -> {
                CrateCutscene(player!!).runStage(100)

                player!!.unlock()
                player!!.properties.teleportLocation = Location.create(2778, 3401, 0)
                end()
                stage = END_DIALOGUE
            }

            30 -> {
                sendDialogue(player!!, "You wait...")
                stage++
            }

            31 -> {
                sendDialogue(player!!, "And wait...")
                stage = 21
            }
        }
    }
}
