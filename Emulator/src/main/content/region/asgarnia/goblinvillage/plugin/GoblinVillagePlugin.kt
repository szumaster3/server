package content.region.asgarnia.goblinvillage.plugin

import core.api.sendNPCDialogue
import core.api.sendPlainDialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.DeathTask
import core.game.world.map.RegionManager.getLocalNpcs
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class GoblinVillagePlugin: InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction with goblin at Tree gnome village labyrinth.
         */

        on(NPCs.GOBLIN_444, IntType.NPC, "Talk-To") { player, _ ->
            sendNPCDialogue(player, NPCs.GOBLIN_444, "Go away, human!", FaceAnim.OLD_ANGRY1)
            return@on true
        }

        /*
         * Handles showing current population at Gnome Village.
         */

        on(Scenery.SIGNPOST_31301, IntType.SCENERY, "read") { player, _ ->
            var population = 3
            val localNPC = getLocalNpcs(player, 50)
            for (n in localNPC) {
                if (n.name == "Goblin" && !DeathTask.isDead(n)) {
                    population++
                }
                sendPlainDialogue(player, false, "Welcome to Goblin Village.", "Current population: $population")
            }
            return@on true
        }
    }
}
