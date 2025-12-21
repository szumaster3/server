package content.region.asgarnia.goblin_village.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.NPCs

@Initializable
class GoblinVillageNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    private var delay = 0L
    private var green = true

    override fun init() {
        super.init()
        green = id !in RED_GOBLINS
    }

    override fun tick() {
        super.tick()
        if (delay < System.currentTimeMillis() && !properties.combatPulse.isAttacking) {
            if ((1..4).random() == 2) {
                val surround = getLocalNpcs(this, 10)
                val enemies = surround.filter { n ->
                    n.id != id && !n.properties.combatPulse.isAttacking && (if (green) RED_GOBLINS else GREEN_GOBLINS).contains(
                        n.id
                    )
                }
                enemies.forEach {
                    it.lock(5)
                    properties.combatPulse.attack(it)
                }
            }
            delay = System.currentTimeMillis() + 5000
        } else {
            if (RandomFunction.random(3) != 1) return
            val enemy = properties.combatPulse.getVictim() as? NPC ?: return
            if (enemy.location.getDistance(getLocation()) > 4) return

            if ((if (green) RED_GOBLINS else GREEN_GOBLINS).contains(enemy.id) && (0..3).random() == 2) {
                val messages = if (green) GREEN_MESSAGES else RED_MESSAGES
                sendChat(messages[RandomFunction.random(messages.size)])
            }
        }
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        GoblinVillageNPC(id, location)

    override fun getIds(): IntArray = ID

    companion object {
        private val ID = intArrayOf(
            NPCs.GOBLIN_4483, NPCs.GOBLIN_4488, NPCs.GOBLIN_4489,
            NPCs.GOBLIN_4484, NPCs.GOBLIN_4491, NPCs.GOBLIN_4485,
            NPCs.GOBLIN_4486, NPCs.GOBLIN_4492, NPCs.GOBLIN_4487,
            NPCs.GOBLIN_4481, NPCs.GOBLIN_4479, NPCs.GOBLIN_4482,
            NPCs.GOBLIN_4480,
        )
        private val RED_GOBLINS = intArrayOf(
            NPCs.GOBLIN_4483, NPCs.GOBLIN_4484, NPCs.GOBLIN_4485,
            NPCs.GOBLIN_4481, NPCs.GOBLIN_4479, NPCs.GOBLIN_4482,
            NPCs.GOBLIN_4480,
        )
        private val GREEN_GOBLINS = intArrayOf(
            NPCs.GOBLIN_4488, NPCs.GOBLIN_4489, NPCs.GOBLIN_4491,
            NPCs.GOBLIN_4486, NPCs.GOBLIN_4492, NPCs.GOBLIN_4487,
        )
        private val RED_MESSAGES = arrayOf(
            "Red armour best!", "Green armour stupid!", "Red!", "Red not green!", "Stupid greenie!"
        )
        private val GREEN_MESSAGES = arrayOf(
            "Green armour best!", "Green!", "Stupid reddie!"
        )
    }
}