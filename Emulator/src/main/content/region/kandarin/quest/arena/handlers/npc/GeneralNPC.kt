package content.region.kandarin.quest.arena.handlers.npc

import content.region.kandarin.quest.arena.dialogue.JeremyServilDialogueFile
import content.region.kandarin.quest.arena.handlers.FightArenaListener.Companion.General
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GeneralNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return GeneralNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GENERAL_KHAZARD_258)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        General.asNpc().isRespawn = true
        General.respawnTick = 10
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            if (getQuestStage(killer, Quests.FIGHT_ARENA) == 97) {
                setQuestStage(killer, Quests.FIGHT_ARENA, 98)
                setAttribute(killer, "/save:quest:arena-optional-task", true)
            }
            openDialogue(killer.asPlayer(), JeremyServilDialogueFile())
        }
        super.finalizeDeath(killer)
    }
}
