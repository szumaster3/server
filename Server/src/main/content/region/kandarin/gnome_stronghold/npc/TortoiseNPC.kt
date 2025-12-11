package content.region.kandarin.gnome_stronghold.npc

import core.api.sendChat
import core.api.spawnProjectile
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
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
    private val driverChat = listOf(
        "You Beast!", "This is for Dobbie!", "Tortoise Murderer!"
    )

    private fun center(): Location = location.transform(1, 1, 0)

    private fun getProjectileOffset(style: CombatStyle, target: Location): Location {
        val c = center()
        val dx = target.x - c.x
        val dy = target.y - c.y

        var offsetX = 0
        var offsetY = 0

        if (dx == 0 && dy == 0) return c

        if (Math.abs(dx) >= Math.abs(dy)) {
            offsetX = 0
            offsetY = if (style == CombatStyle.MAGIC) -1 else 1
            if (dx < 0) offsetY = -offsetY
        } else {
            offsetX = if (style == CombatStyle.MAGIC) -1 else 1
            if (dy < 0) offsetX = -offsetX
            offsetY = 0
        }

        return c.transform(offsetX, offsetY, 0)
    }

    private fun spawnGnome(id: Int, pos: Location, facing: Direction, target: Player): NPC {
        val npc = NPC.create(id, pos)
        npc.direction = facing
        npc.isRespawn = false
        npc.init()
        npc.setAttribute("parentTortoise", this)
        npc.setAttribute("target", target)

        spawnedGnomes.add(npc)
        gnomeDespawnTicks[npc] = GameWorld.ticks + DESPAWN_DELAY
        return npc
    }

    private val combatHandler = object : MultiSwingHandler(
        false,
        SwitchAttack(CombatStyle.MELEE.swingHandler, Animation(3960)),
        SwitchAttack(CombatStyle.RANGE.swingHandler, Animation(3954)),
        SwitchAttack(CombatStyle.MAGIC.swingHandler, Animation(3955))
    ) {
        override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
            val style = state?.style ?: return
            val target = victim ?: return

            val start = getProjectileOffset(style, target.location)
            val speed = (46 + center().getDistance(target.location) * 10).toInt()

            val (gfx, anim) = when (style) {
                CombatStyle.RANGE -> 10 to 3954
                CombatStyle.MAGIC -> 500 to 3955
                else -> null to 3960
            }

            if (gfx != null) {
                spawnProjectile(start, target.location, gfx, 60, 30, 1, speed, 15)
            }

            entity.animate(Animation(anim))
        }
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = combatHandler

    init {
        isAggressive = false
        setSize(3)
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        val player = killer as? Player ?: return

        val facing = Direction.getDirection(location, player.location)
        val c = center()

        val magePos = c.transform(-1, 0, 0)
        val rangerPos = c.transform(1, 0, 0)
        val driverPos = c

        val driver = spawnGnome(NPCs.GNOME_DRIVER_3815, driverPos, facing, player)
        val mage = spawnGnome(NPCs.GNOME_MAGE_3816, magePos, facing, player)
        val archer = spawnGnome(NPCs.GNOME_ARCHER_3814, rangerPos, facing, player)

        sendChat(driver, "Nooooo! Dobbie's dead!")
        sendChat(archer, "Argh!")
        sendChat(mage, "Attack the infidel!")
    }

    override fun tick() {
        super.tick()

        val iterator = spawnedGnomes.iterator()
        while (iterator.hasNext()) {
            val gnome = iterator.next()

            if (GameWorld.ticks >= (gnomeDespawnTicks[gnome] ?: 0)) {
                gnome.clear()
                iterator.remove()
                gnomeDespawnTicks.remove(gnome)
                continue
            }

            val target = gnome.getAttribute<Player>("target") ?: continue
            val driver = spawnedGnomes.firstOrNull { it.id == NPCs.GNOME_DRIVER_3815 }
            val archer = spawnedGnomes.firstOrNull { it.id == NPCs.GNOME_ARCHER_3814 }

            when (gnome.id) {
                NPCs.GNOME_DRIVER_3815 -> {
                    gnome.attack(target)
                    if (RandomUtils.random(9) == 0) gnome.sendChat(RandomUtils.randomChoice(driverChat))
                }

                NPCs.GNOME_ARCHER_3814 -> if ((driver?.skills?.lifepoints ?: 0) <= 0) gnome.attack(target)
                NPCs.GNOME_MAGE_3816 -> if ((archer?.skills?.lifepoints ?: 0) <= 0) gnome.attack(target)
                else -> gnome.attack(target)
            }
        }
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = TortoiseNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.TORTOISE_3808)
}