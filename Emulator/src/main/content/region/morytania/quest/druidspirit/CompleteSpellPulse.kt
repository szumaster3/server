package content.region.morytania.quest.druidspirit

import core.api.findLocalNPC
import core.api.openDialogue
import core.api.spawnProjectile
import core.api.unlock
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests

class CompleteSpellPulse(
    val player: Player,
) : Pulse(2) {
    var counter = 0
    val locations =
        arrayOf(
            Location.create(3444, 9740, 0),
            Location.create(3439, 9740, 0),
            Location.create(3439, 9737, 0),
            Location.create(3444, 9737, 0),
            Location.create(3444, 9735, 0),
            Location.create(3438, 9735, 0),
        )
    val dest: Location = Location.create(3441, 9738, 0)

    override fun pulse(): Boolean {
        when (counter++) {
            0 -> repeat(6) { spawnProjectile(locations[it], dest, 268, 0, 1000, 0, 40, 20) }
            1 -> player.questRepository.getQuest(Quests.NATURE_SPIRIT).setStage(player, 60)
            2 -> player.teleport(player.location.transform(0, 0, 1))
            3 ->
                openDialogue(
                    player,
                    NPCs.NATURE_SPIRIT_1051,
                    findLocalNPC(player, NPCs.NATURE_SPIRIT_1051) as NPC,
                ).also {
                    unlock(player)
                    return true
                }
        }
        return false
    }
}
