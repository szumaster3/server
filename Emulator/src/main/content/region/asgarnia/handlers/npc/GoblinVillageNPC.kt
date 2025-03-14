package content.region.asgarnia.handlers.npc

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class GoblinVillageNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    private var delay = 0L
    private var green = true

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return GoblinVillageNPC(id, location)
    }

    override fun init() {
        super.init()
        for (i in RED_GOBLINS) {
            if (id == i) {
                green = false
                break
            }
        }
    }

    override fun tick() {
        super.tick()
        if (delay < System.currentTimeMillis() && !properties.combatPulse.isAttacking) {
            val rand = RandomFunction.random(1, 4)
            if (rand == 2) {
                val surround = getLocalNpcs(this, 10)
                for (n in surround) {
                    if (n.id == id) {
                        continue
                    }
                    if (n.properties.combatPulse.isAttacking) {
                        continue
                    }
                    for (i in if (green) RED_GOBLINS else GREEN_GOBLINS) {
                        if (n.id == i) {
                            n.lock(5)
                            properties.combatPulse.attack(n)
                            break
                        }
                    }
                }
            }
            delay = System.currentTimeMillis() + 5000
        } else {
            val rand = RandomFunction.random(3)
            if (rand != 1) {
                return
            }
            val enemy = properties.combatPulse.getVictim() ?: return
            if (enemy !is NPC) {
                return
            }
            if (enemy.getLocation().getDistance(getLocation()) > 4) {
                return
            }
            var goblin = false
            for (i in if (green) RED_GOBLINS else GREEN_GOBLINS) {
                if (i == enemy.getId()) {
                    goblin = true
                    break
                }
            }
            if (goblin && RandomFunction.random(0, 3) == 2) {
                sendChat(
                    if (green) {
                        GREEN_MESSAGES[RandomFunction.random(GREEN_MESSAGES.size)]
                    } else {
                        RED_MESSAGES[
                            RandomFunction.random(
                                RED_MESSAGES.size,
                            ),
                        ]
                    },
                )
            }
        }
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
    }

    override fun getIds(): IntArray {
        return ID
    }

    companion object {
        private val ID =
            intArrayOf(
                NPCs.GOBLIN_4483,
                NPCs.GOBLIN_4488,
                NPCs.GOBLIN_4489,
                NPCs.GOBLIN_4484,
                NPCs.GOBLIN_4491,
                NPCs.GOBLIN_4485,
                NPCs.GOBLIN_4486,
                NPCs.GOBLIN_4492,
                NPCs.GOBLIN_4487,
                NPCs.GOBLIN_4481,
                NPCs.GOBLIN_4479,
                NPCs.GOBLIN_4482,
                NPCs.GOBLIN_4480,
            )
        private val RED_GOBLINS =
            intArrayOf(
                NPCs.GOBLIN_4483,
                NPCs.GOBLIN_4484,
                NPCs.GOBLIN_4485,
                NPCs.GOBLIN_4481,
                NPCs.GOBLIN_4479,
                NPCs.GOBLIN_4482,
                NPCs.GOBLIN_4480,
            )
        private val GREEN_GOBLINS =
            intArrayOf(
                NPCs.GOBLIN_4488,
                NPCs.GOBLIN_4489,
                NPCs.GOBLIN_4491,
                NPCs.GOBLIN_4486,
                NPCs.GOBLIN_4492,
                NPCs.GOBLIN_4487,
            )
        private val RED_MESSAGES =
            arrayOf("Red armour best!", "Green armour stupid!", "Red!", "Red not green!", "Stupid greenie!")
        private val GREEN_MESSAGES = arrayOf("Green armour best!", "Green!", "Stupid reddie!")
    }
}
