package content.region.kandarin.ardougne.east.quest.biohazard.npc

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class MournerNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = MournerNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.MOURNER_370)

    override fun finalizeDeath(killer: Entity?) {
        val player = killer?.asPlayer() ?: return super.finalizeDeath(killer)

        if (getQuestStage(player, Quests.BIOHAZARD) < 6) {
            return super.finalizeDeath(killer)
        }

        sendMessage(player, "You search the mourner...")

        if (inInventory(player, Items.KEY_2832)) {
            sendMessage(player, "...but find nothing.")
        } else {
            addItemOrDrop(player, Items.KEY_2832)
            sendMessage(player, "and find a key.", 1)
            setQuestStage(player, Quests.BIOHAZARD, 8)
        }

        super.finalizeDeath(killer)
    }
}
