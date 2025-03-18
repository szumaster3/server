package content.region.misthalin.quest.dragon.handlers.npc

import content.region.misthalin.quest.dragon.DragonSlayer
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ZombieRatNPC : AbstractNPC {
    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = ZombieRatNPC(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val p = killer
            var quest = p.getQuestRepository().getQuest(Quests.DRAGON_SLAYER)
            if (RandomFunction.random(0, 4) == 2) {
                GroundItemManager.create(DragonSlayer.RED_KEY, getLocation(), killer)
            }
            quest = p.getQuestRepository().getQuest(Quests.WITCHS_POTION)
            if (quest.getStage(p) in 1..99) {
                GroundItemManager.create(RAT_TAIL, getLocation(), p)
            }
            GroundItemManager.create(Item(526), getLocation(), p)
        }
    }

    override fun getIds(): IntArray = ID

    companion object {
        private val ID = intArrayOf(NPCs.ZOMBIE_RAT_6088, NPCs.ZOMBIE_RAT_6089, NPCs.ZOMBIE_RAT_6090)
        private val RAT_TAIL = Item(Items.RATS_TAIL_300, 1)
    }
}
