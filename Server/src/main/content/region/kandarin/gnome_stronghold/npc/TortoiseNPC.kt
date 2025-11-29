package content.region.kandarin.gnome_stronghold.npc

import core.api.sendChat
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.world.update.flag.context.Animation
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomUtils
import shared.consts.NPCs

/**
 * Handles Tortoise with riders behavior.
 */
@Initializable
class TortoiseNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private val DESPAWN_DELAY = 80
    private val gnomeDespawnTicks = mutableMapOf<NPC, Int>()
    private val spawnedGnomes = mutableListOf<NPC>()
    private val driverChat = listOf("You Beast!", "This is for Dobbie!", "Tortoise Murderer!")
    private val isMultiZone = this.properties.isMultiZone

    private val combatHandler = MultiSwingHandler(
        false,
        SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(if(isMultiZone) 3957 else 3960)),
        SwitchAttack(CombatStyle.RANGE.swingHandler, Animation(if(isMultiZone) 3956 else 3954)),
        SwitchAttack(CombatStyle.MAGIC.swingHandler, Animation(if(isMultiZone) 3956 else 3955))
    )

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = combatHandler

    init {
        isAggressive = false
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)

        if (killer !is Player) return
        val player = killer

        val driver = NPC.create(NPCs.GNOME_DRIVER_3815, location.transform(1, 0, 0))
        val archer = NPC.create(NPCs.GNOME_ARCHER_3814, location.transform(0, 1, 0))
        val mage = NPC.create(NPCs.GNOME_MAGE_3816, location.transform(-1, 0, 0))

        for (npc in listOf(driver, archer, mage)) {
            npc.isRespawn = false
            npc.init()
            npc.setAttribute("parentTortoise", this)
            npc.setAttribute("target", player)
            spawnedGnomes.add(npc)
            gnomeDespawnTicks[npc] = GameWorld.ticks + DESPAWN_DELAY
        }

        sendChat(driver, "Nooooo! Dobbie's dead!")
        sendChat(archer, "Argh!")
        sendChat(mage, "Attack the infidel!")
    }

    override fun tick() {
        super.tick()

        val iterator = spawnedGnomes.iterator()
        while (iterator.hasNext()) {
            val gnome = iterator.next()

            val despawnTick = gnomeDespawnTicks[gnome] ?: continue
            if (GameWorld.ticks >= despawnTick) {
                gnome.clear()
                iterator.remove()
                gnomeDespawnTicks.remove(gnome)
                continue
            }

            val target = gnome.getAttribute<Player>("target") ?: continue
            val singleCombat = !gnome.properties.isMultiZone

            if (singleCombat) {
                when (gnome.id) {
                    NPCs.GNOME_DRIVER_3815 -> {
                        gnome.attack(target)
                        if (RandomUtils.random(9) == 0) {
                            gnome.sendChat(RandomUtils.randomChoice(driverChat))
                        }
                    }
                    NPCs.GNOME_ARCHER_3814 -> {
                        val driverDead =
                            (spawnedGnomes
                                .firstOrNull { it.id == NPCs.GNOME_DRIVER_3815 }
                                ?.skills
                                ?.lifepoints ?: 0) <= 0
                        if (driverDead) gnome.attack(target)
                    }
                    NPCs.GNOME_MAGE_3816 -> {
                        val archerDead =
                            (spawnedGnomes
                                .firstOrNull { it.id == NPCs.GNOME_ARCHER_3814 }
                                ?.skills
                                ?.lifepoints ?: 0) <= 0
                        if (archerDead) gnome.attack(target)
                    }
                }
            } else {
                gnome.attack(target)
                if (gnome.id == NPCs.GNOME_DRIVER_3815 && RandomUtils.random(9) == 0) {
                    sendChat(gnome, RandomUtils.randomChoice(driverChat))
                }
            }
        }
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        TortoiseNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.TORTOISE_3808)
}
