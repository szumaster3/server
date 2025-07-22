package content.region.kandarin.ardougne.east.quest.biohazard.npc

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.isQuestInProgress
import core.api.setQuestStage
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class MournerNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = MournerNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.MOURNER_370)

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            if (isQuestInProgress(killer, Quests.BIOHAZARD, 6, 15)) {
                sendMessage(killer.asPlayer(), "You search the mourner...")
                if (!inInventory(killer.asPlayer(), Items.KEY_2832)) {
                    addItemOrDrop(killer.asPlayer(), Items.KEY_2832)
                    killer.asPlayer().sendMessage("and find a key.", 1)
                    setQuestStage(killer.asPlayer(), Quests.BIOHAZARD, 8)
                } else {
                    sendMessage(killer.asPlayer(), "...but find nothing.")
                }
            }
            super.finalizeDeath(killer)
        }
    }
}
