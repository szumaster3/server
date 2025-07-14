package content.region.desert.alkharid.quest.feud.plugin

import content.region.desert.alkharid.quest.feud.dialogue.DrunkenAliFeudDialogue
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Vars

class TheFeudPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles giving a beer to Drunken Ali.
         */

        onUseWith(IntType.NPC, Items.BEER_1917, NPCs.DRUNKEN_ALI_1863) { player, used, with ->
            val theFeud = getVarbit(player, Vars.VARBIT_QUEST_THE_FEUD_PROGRESS_334)
            val npc = with.asNpc()

            if(used.id != Items.BEER_1917) {
                sendNPCDialogue(player, npc.id, "Eh? What's this? I don't want that, get me a beer.", FaceAnim.DRUNK)
                return@onUseWith true
            }

            if(theFeud == 0) {
                val random = (0..7).random()
                when (random) {
                    0 -> sendChat(npc, "I've got a lovely bunch of coconuts, doobidy doo.")
                    1 -> sendNPCDialogue(player, npc.id, "Thank you my friend - now if you don't mind I'm having a conversation with my imaginary friend Bob here.")
                    2 -> {
                        sendChat(npc, "Did you hear the one about the man who walked into a bar?")
                        sendChat(npc, "He hurt his foot! Hah!", 2)
                    }
                    3 -> {
                        sendChat(npc, "Wha'? Are you lookin' at me? Are you lookin' at me?")
                        sendChat(npc, "You know something? You're drunk, you are.", 2)
                    }
                    4 -> sendChat(npc, "Why does nobody like me?")
                    5 -> sendChat(npc, "Cheers for the beers!")
                    6 -> sendChat(npc, "What you looking at.")
                    7 -> {
                        sendChat(npc, "Did you hear the one where the camel walks into the bar and orders a pint?.")
                        sendChat(npc, "The barman asks 'Why the long face!'", 2)
                    }
                }
                return@onUseWith true
            }

            if(theFeud >= 1 && removeItem(player, used.asItem())) {
                val count = getVarbit(player, Vars.VARBIT_QUEST_THE_FEUD_DRUNKEN_ALI_BEER_COUNT_318)
                setVarbit(player, Vars.VARBIT_QUEST_THE_FEUD_DRUNKEN_ALI_BEER_COUNT_318, count + 1, true)
                openDialogue(player, DrunkenAliFeudDialogue(count + 1))
            }

            return@onUseWith true
        }
    }
}