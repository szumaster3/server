package content.region.misthalin.varrock.quest.dragon.handlers.npc

import content.region.misthalin.varrock.quest.dragon.DragonSlayer
import core.api.inBank
import core.api.inInventory
import core.api.quest.getQuestStage
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests

class WormbrainNPC : AbstractNPC {
    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = WormbrainNPC(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            if (getQuestStage(killer, Quests.DRAGON_SLAYER) == 20 &&
                !inInventory(
                    killer,
                    DragonSlayer.WORMBRAIN_PIECE.id,
                ) &&
                !inBank(killer, DragonSlayer.WORMBRAIN_PIECE.id)
            ) {
                GroundItemManager.create(DragonSlayer.WORMBRAIN_PIECE, getLocation(), killer)
                sendMessage(killer, "Wormbrain drops a map piece on the floor.")
            }
        }
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        if (entity is Player) {
            val player = entity
            if (getQuestStage(player, Quests.DRAGON_SLAYER) != 20) {
                if (message) {
                    sendMessage(player, "The goblin is already in prison. You have no reason to attack him.")
                }
                return false
            }
        }
        return super.isAttackable(entity, style, message)
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.WORMBRAIN_745)
}
