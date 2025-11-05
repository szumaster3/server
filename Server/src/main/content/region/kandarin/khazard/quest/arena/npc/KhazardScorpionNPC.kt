package content.region.kandarin.khazard.quest.arena.npc

import content.region.kandarin.khazard.quest.arena.dialogue.GeneralKhazardDialogue
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class KhazardScorpionNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = KhazardScorpionNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.KHAZARD_SCORPION_271)

    companion object {
        @JvmStatic
        fun spawnScorpion(player: Player) {
            val scorpion = KhazardScorpionNPC(NPCs.KHAZARD_SCORPION_271)
            scorpion.location = location(2604, 3159, 0)
            scorpion.isWalks = true
            scorpion.isAggressive = true
            scorpion.isActive = false

            if (scorpion.asNpc() != null && scorpion.isActive && getAttribute(player, "spawn-scorpion", false)) {
                scorpion.properties.teleportLocation = scorpion.properties.spawnLocation
            }
            scorpion.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(2, scorpion) {
                    override fun pulse(): Boolean {
                        scorpion.init()
                        scorpion.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            if (getQuestStage(killer, Quests.FIGHT_ARENA) == 88) {
                setQuestStage(killer, Quests.FIGHT_ARENA, 89)
            }
            removeAttribute(killer, "spawn-scorpion")
            openDialogue(killer, GeneralKhazardDialogue())
        }
        clear()
        super.finalizeDeath(killer)
    }
}
