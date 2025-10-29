package content.region.kandarin.feldip.jiggig.quest.zogre.npc

import content.region.kandarin.feldip.jiggig.quest.zogre.plugin.ZogreUtils
import core.api.*
import core.api.produceGroundItem
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Graphics
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Vars

@Initializable
class SlashBashNPC(
    id: Int = 0,
    location: Location? = null,
    private val owner: Player? = null
) : AbstractNPC(id, location) {

    private var despawnTime = 0

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        SlashBashNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.SLASH_BASH_2060)

    override fun handleTickActions() {
        super.handleTickActions()
        val p = owner ?: return
        if (p.location.getDistance(location) > 10 || !p.isActive || despawnTime++ > 1030) {
            removeAttribute(p, ZogreUtils.SLASH_BASH_ACTIVE)
            poofClear(this)
        }
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker as? Player ?: return

        val modifier = when {
            state.spell?.spellId == 22 || inEquipment(player, Items.COMP_OGRE_BOW_4827) -> 0.5
            else -> 0.25
        }

        state.estimatedHit = (state.estimatedHit * modifier).toInt()
        if (state.secondaryHit > 0)
            state.secondaryHit = (state.secondaryHit * modifier).toInt()
    }

    override fun finalizeDeath(killer: Entity?) {
        val player = killer as? Player ?: return super.finalizeDeath(killer)

        produceGroundItem(player, OGRE_ARTIFACT, 1, location)
        produceGroundItem(player, Items.OURG_BONES_4834, RandomFunction.random(1, 3), location)
        produceGroundItem(player, Items.ZOGRE_BONES_4812, RandomFunction.random(1, 2), location)

        setVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 12, true)
        removeAttribute(player, ZogreUtils.SLASH_BASH_ACTIVE)
        clearHintIcon(player)

        super.finalizeDeath(killer)
        clear()
    }

    companion object {
        private const val OGRE_ARTIFACT = Items.OGRE_ARTEFACT_4818

        @JvmStatic
        fun spawnSlashBash(player: Player) {
            val boss = SlashBashNPC(NPCs.SLASH_BASH_2060, null, player).apply {
                location = Location.getRandomLocation(Location.create(2480, 9445, 0), 2, true)
                isWalks = true
                isNeverWalks = false
                isAggressive = true
            }

            registerLogoutListener(player, "slash-bash") { boss.clear() }
            setAttribute(player, ZogreUtils.SLASH_BASH_ACTIVE, true)

            val spawnGraphics = core.game.world.update.flag.context.Graphics(Graphics.RE_PUFF_86)
            GameWorld.Pulser.submit(object : Pulse(2, boss) {
                override fun pulse(): Boolean {
                    visualize(boss, -1, spawnGraphics)
                    boss.init()
                    registerHintIcon(player, boss)
                    sendMessage(player, "Something stirs behind you!")
                    boss.attack(player)
                    return true
                }
            })
        }
    }
}
