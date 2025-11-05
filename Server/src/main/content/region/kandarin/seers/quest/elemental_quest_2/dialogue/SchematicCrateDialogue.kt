package content.region.kandarin.seers.quest.elemental_quest_2.dialogue

import content.region.kandarin.seers.quest.elemental_quest_2.plugin.EW2Utils
import core.api.addItemOrDrop
import core.api.sendItemDialogue
import core.api.sendOptions
import core.game.dialogue.DialogueFile

class SchematicCrateDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                sendOptions(
                    player!!,
                    "There are two schematics here...",
                    "Crane schematic",
                    "Lever schematic",
                ).also { stage++ }

            1 ->
                when (buttonID) {
                    1 -> {
                        sendItemDialogue(player!!, EW2Utils.CraneSchematic, "You take a copy of the schematic drawing.")
                        addItemOrDrop(player!!, EW2Utils.CraneSchematic)
                        end()
                    }

                    2 -> {
                        sendItemDialogue(player!!, EW2Utils.LeverSchematic, "You take a copy of the schematic drawing.")
                        addItemOrDrop(player!!, EW2Utils.LeverSchematic)
                        end()
                    }
                }
        }
    }
}
