package content.region.asgarnia.handlers

import core.api.sendNPCDialogue
import core.api.sendPlainDialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.DeathTask
import core.game.world.map.RegionManager.getLocalNpcs
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class GoblinVillageListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.GOBLIN_444, IntType.NPC, "Talk-To") { player, _ ->
            sendNPCDialogue(player, NPCs.GOBLIN_444, "Go away, human!", FaceAnim.OLD_ANGRY1)
            return@on true
        }

        on(Scenery.SIGNPOST_31301, IntType.SCENERY, "read") { player, _ ->
            var population = 3
            val npcs = getLocalNpcs(player, 50)
            for (n in npcs) {
                if (n.name == "Goblin" && !DeathTask.isDead(n)) {
                    population++
                }
                sendPlainDialogue(player, false, "Welcome to Goblin Village.", "Current population: $population")
            }
            return@on true
        }
    }
}
