package content.region.fremennik.quest.horror.handlers

import content.region.fremennik.quest.horror.JossikLighthouseDialogue
import core.api.*
import core.api.quest.setQuestStage
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class DagonnothBabyNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return DagonnothBabyNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAGANNOTH_3591)
    }

    companion object {
        @JvmStatic
        fun spawnDagannothBaby(player: Player) {
            val dag = DagonnothBabyNPC(NPCs.DAGANNOTH_1347)
            dag.location = location(2520, 4645, 0)
            dag.isWalks = true
            dag.isAggressive = true
            dag.isActive = false

            if (dag.asNpc() != null && dag.isActive) {
                dag.properties.teleportLocation = dag.properties.spawnLocation
            }
            dag.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(2, dag) {
                    override fun pulse(): Boolean {
                        if (player.location.withinDistance(dag.location, 20)) {
                            dag.init()
                        }
                        registerHintIcon(player, dag)
                        dag.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            lock(killer, 2)
            setQuestStage(killer, Quests.HORROR_FROM_THE_DEEP, 60)
            clearHintIcon(killer)
            queueScript(killer, 1, QueueStrength.SOFT) {
                face(findNPC(NPCs.JOSSIK_1335)).also {
                    openDialogue(killer, JossikLighthouseDialogue())
                }
                return@queueScript stopExecuting(killer)
            }
        }
        clear()
        super.finalizeDeath(killer)
    }
}
