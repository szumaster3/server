package content.region.karamja.jungle

import core.api.hasRequirement
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Regions

/**
 * Handles behavior for Giant Ant Worker and Soldier NPCs.
 *
 * In the Jade Vine Maze, these ants can drop body parts
 * only before and during the returning clarence mini-quest
 * each part has a 1/16 drop chance, and they drop in a set order.
 */
class GiantAntWorkerNPC : NPCBehavior(NPCs.GIANT_ANT_WORKER_6379, NPCs.GIANT_ANT_SOLDIER_6380) {

    companion object {
        private val BODY_PARTS = listOf(
            Items.HAND_11763,
            Items.FOOT_11764,
            Items.TORSO_11765,
            Items.LEFT_ARM_11766,
            Items.RIGHT_ARM_11767,
            Items.LEFT_LEG_11768,
            Items.RIGHT_LEG_11769
        )
    }

    override fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {
        super.onDropTableRolled(self, killer, drops)

        if (killer !is Player) return

        if (self.location.regionId != Regions.JADE_VINE_MAZE_11566) return
        if (!hasRequirement(killer, Quests.THE_HAND_IN_THE_SAND, false)) return
//      if(isQuestComplete(killer, Quests.THE_HAND_IN_THE_SAND) && getAttribute(killer, GameAttributes.RETURNING_CLARENCE_COMPLETE, false))  return
        for (part in BODY_PARTS) {
            val hasPart = killer.inventory.contains(part, 1) || killer.bank.contains(part, 1)
            if (!hasPart && RandomFunction.roll(16)) {
                drops.add(Item(part))
                break
            }
        }
    }
}
