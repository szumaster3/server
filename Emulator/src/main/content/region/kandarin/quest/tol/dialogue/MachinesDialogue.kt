package content.region.kandarin.quest.tol.dialogue

import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE

class MachinesDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> npc("The mahcine appears unfinished. You're going to need some materials:").also { stage++ }
            1 -> npc("4 coloured balls, 3 pieces of metal sheeting and 4 valve wheels").also { stage++ }
            2 -> options("Yes", "No").also { stage++ }
            3 ->
                when (buttonID) {
                    1 -> player("Yes").also { stage = 4 }
                    2 -> player("No").also { stage = END_DIALOGUE }
                }

            4 -> npc("You built the machine! It still needs calibrating, though.").also { stage++ }
            5 ->
                npc(
                    "The pressure seems to be affected by holes in the pipes. Perhaps I can block them up with the balls.",
                ).also {
                    stage = END_DIALOGUE
                }

            100 -> npc("The machine is working!").also { stage = END_DIALOGUE }
        }
    }

    class PipeMachineDialogue : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            when (stage) {
                0 -> npc("The mahcine appears unfinished. You're going to need some materials:").also { stage++ }
                1 -> npc("6 rivets, 4 metal pipes, and 5 metal rings.").also { stage++ }
                2 -> options("Yes", "No").also { stage++ }
                3 ->
                    when (buttonID) {
                        1 -> player("Yes").also { stage = 4 }
                        2 -> player("No").also { stage = END_DIALOGUE }
                    }

                4 -> npc("You built the machine! It still needs calibrating, though.").also { stage++ }
                5 ->
                    npc(
                        "Looks like the pipes need connecting, to allow the liquid to travel further up the tower!",
                    ).also {
                        stage = END_DIALOGUE
                    }

                100 -> npc("The machine is working!").also { stage = END_DIALOGUE }
            }
        }
    }

    class CageDialogue : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            when (stage) {
                0 -> npc("The cage appears unfinished. You're going to need some materials:").also { stage++ }
                1 -> npc("5 metal bars and 4 bottles of binding fluid.").also { stage++ }
                2 -> options("Yes", "No").also { stage++ }
                3 ->
                    when (buttonID) {
                        1 -> player("Yes").also { stage = 4 }
                        2 -> player("No").also { stage = END_DIALOGUE }
                    }

                4 -> npc("You built the cage!").also { stage++ }
                5 ->
                    npc("Some of the bars need to be completed - you need to get the sizes correct, though.").also {
                        stage = END_DIALOGUE
                    }

                100 -> npc("The cage is complete!").also { stage = END_DIALOGUE }
            }
        }
    }

    class TowerDialogue : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            when (stage) {
                0 ->
                    npc("The tower should be in working order now! Best go and tell Effigy!").also {
                        stage = END_DIALOGUE
                    }
            }
        }
    }
}
