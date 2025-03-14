package content.region.kandarin.handlers.npc

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class BattlefieldNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    private var nextActionTime = 0L
    private var isGnome = false

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return BattlefieldNPC(id, location)
    }

    override fun init() {
        super.init()
        isGnome = id in GNOMES
    }

    override fun tick() {
        super.tick()
        val currentTime = System.currentTimeMillis()

        if (currentTime >= nextActionTime) {
            if (!properties.combatPulse.isAttacking) {
                findAndAttackEnemy()
            } else if (RandomFunction.random(1, 3) == 1) {
                chatDuringCombat()
            }
            nextActionTime = currentTime + RandomFunction.random(3000, 5000)
        }
    }

    private fun findAndAttackEnemy() {
        val enemies = getNearbyEnemies()
        val target = enemies.firstOrNull() ?: return
        target.lock(5)
        properties.combatPulse.attack(target)
    }

    private fun chatDuringCombat() {
        val enemy = properties.combatPulse.getVictim() as? NPC ?: return
        if (enemy.location.getDistance(getLocation()) <= 4 && enemy.id in getOpposingFaction()) {
            sendChat(getRandomChat())
        }
    }

    private fun getNearbyEnemies(): List<NPC> {
        val opposingFaction = getOpposingFaction()
        return getLocalNpcs(this, 8).filter { npc ->
            npc.id in opposingFaction && !npc.properties.combatPulse.isAttacking && npc != this
        }
    }

    private fun getOpposingFaction(): IntArray {
        return if (isGnome) TROOPERS else GNOMES
    }

    private fun getRandomChat(): String {
        val chatArray = if (isGnome) GNOMES_FORCE_CHAT else TROOPERS_FORCE_CHAT
        return chatArray.random()
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
                NPCs.KHAZARD_TROOPER_2245,
                NPCs.KHAZARD_TROOPER_2246,
                NPCs.GNOME_67,
                NPCs.MOUNTED_TERRORBIRD_GNOME_1752,
                NPCs.GNOME_TROOP_2247,
                NPCs.GNOME_2249,
                NPCs.GNOME_2250,
                NPCs.GNOME_2251,
                NPCs.TORTOISE_3808,
                NPCs.MOUNTED_TERRORBIRD_GNOME_6109,
            )
        private val GNOMES =
            intArrayOf(
                NPCs.GNOME_67,
                NPCs.MOUNTED_TERRORBIRD_GNOME_1752,
                NPCs.GNOME_TROOP_2247,
                NPCs.GNOME_2249,
                NPCs.GNOME_2250,
                NPCs.GNOME_2251,
                NPCs.TORTOISE_3808,
                NPCs.MOUNTED_TERRORBIRD_GNOME_6109,
            )
        private val TROOPERS = intArrayOf(NPCs.KHAZARD_TROOPER_2245, NPCs.KHAZARD_TROOPER_2246)
        private val TROOPERS_FORCE_CHAT =
            arrayOf(
                "Get off of me, ya little pest!",
                "Die, you filthy goblin!",
                "Victory will be ours!",
                "We will crush you!",
            )
        private val GNOMES_FORCE_CHAT =
            arrayOf(
                "Stop right there, Khazard scum!",
                "Tally Ho!",
                "We are the true force of the battlefield!",
                "Feel the wrath of the goblins!",
            )
    }
}
