package content.region.kandarin.quest.grandtree.dialogue

import core.api.face
import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Quests

class ShipyardWorkerGTDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> {
                face(npc!!, player!!, 1)
                npcl("Hey you! What are you up to?").also {
                    if (getQuestStage(player!!, Quests.THE_GRAND_TREE) == 55) {
                        setAttribute(player!!, "/save:grandtree:opt1", false)
                        setAttribute(player!!, "/save:grandtree:opt2", false)
                        setAttribute(player!!, "/save:grandtree:opt3", false)
                    }
                    stage++
                }
            }

            1 -> player("I'm trying to open the gate!").also { stage++ }
            2 -> npcl("I can see that! Why?").also { stage++ }
            3 ->
                options(
                    "I'm from the Ministry of Health and Safety.",
                    "Glough sent me.",
                    "I'm just looking around.",
                ).also { stage++ }

            4 ->
                when (buttonID) {
                    1 -> player("I'm from the Ministry of Health and Safety.").also { stage = 40 }
                    2 -> player("Glough sent me.").also { stage = 50 }
                    3 -> player("I'm just looking around.").also { stage = 11 }
                }

            11 -> npc("This ain't a museum! Leave now!").also { stage++ }
            12 -> player("I'll leave when I choose!").also { stage++ }
            13 -> npc("Well you're not on the list so you're not", "coming in. Go away.").also { stage++ }
            14 -> player("Well I'll just stand here then until you let me in.").also { stage++ }
            15 -> npc("You do that!").also { stage++ }
            16 -> player("I will!").also { stage++ }
            17 -> npc(FaceAnim.HALF_ASKING, "Yeah?").also { stage++ }
            18 -> player("Yeah!").also { stage++ }
            19 -> npc(FaceAnim.CALM, "...").also { stage++ }
            20 -> player(FaceAnim.CALM, "...").also { stage++ }
            21 -> player(FaceAnim.ASKING, "So are you going to let me in then?").also { stage++ }
            22 -> npcl("No.").also { stage++ }
            23 -> player("...").also { stage++ }
            24 -> npcl("...").also { stage++ }
            25 -> player(FaceAnim.HALF_ASKING, "You bored yet?").also { stage++ }
            26 -> npc("No, I can stand here all day.").also { stage++ }
            27 -> player("...").also { stage++ }
            28 -> npc("...").also { stage++ }
            29 -> player("Alright you win. I'll find another way in.").also { stage++ }
            30 -> npc("No you won't.").also { stage++ }
            31 -> player("Yes I will.").also { stage++ }
            32 ->
                npc("I'm not starting that again. Maybe if", "I ignore you you'll go away...").also {
                    stage = END_DIALOGUE
                }

            40 -> npc("Never 'erd of 'em.").also { stage++ }
            41 -> player("You will respect my authority!").also { stage++ }
            42 -> npc("Get out of here before I give you a beating!").also { stage = END_DIALOGUE }
            50 -> npc("Hmm... really? What for?").also { stage++ }
            51 -> player("You're wasting my time! Take me to your superior!").also { stage++ }
            52 -> npc("OK. Password.").also { stage++ }
            53 -> options("Ka.", "Ko.", "Ke.").also { stage++ }
            54 ->
                when (buttonID) {
                    1 ->
                        player("Ka.").also {
                            setAttribute(player!!, "/save:grandtree:opt1", true)
                            stage++
                        }

                    2 -> player("Ko.").also { stage++ }
                    3 -> player("Ke.").also { stage++ }
                }

            55 -> options("Lo.", "Lu.", "Le.").also { stage++ }
            56 ->
                when (buttonID) {
                    1 -> player("Lo.").also { stage++ }
                    2 ->
                        player("Lu.").also {
                            setAttribute(player!!, "/save:grandtree:opt2", true)
                            stage++
                        }

                    3 -> playerl("Le.").also { stage++ }
                }

            57 -> options("Mon.", "Min.", "Men.").also { stage++ }
            58 ->
                when (buttonID) {
                    1 -> player("Mon.").also { stage++ }
                    2 ->
                        player("Min.").also {
                            setAttribute(player!!, "/save:grandtree:opt3", true)
                            stage++
                        }

                    3 -> playerl("Men.").also { stage++ }
                }

            59 -> {
                if (getAttribute(player!!, "/save:grandtree:opt1", false) &&
                    getAttribute(
                        player!!,
                        "/save:grandtree:opt2",
                        false,
                    ) &&
                    getAttribute(player!!, "/save:grandtree:opt3", false)
                ) {
                    npc("Sorry to have kept you.").also { stage = 60 }
                } else {
                    npc("You have no idea!").also { stage = END_DIALOGUE }
                }
            }

            60 -> {
                end()
                DoorActionHandler.autowalkFence(
                    player!!,
                    Scenery(2438, Location(2945, 3041, 0)),
                    2432,
                    2439,
                )
            }
        }
    }
}
