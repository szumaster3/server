package content.region.wilderness.handlers.revs

import content.region.wilderness.handlers.revs.RevenantController.Companion.registerRevenant
import content.region.wilderness.handlers.revs.RevenantController.Companion.unregisterRevenant
import core.api.playAudio
import core.api.playGlobalAudio
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.skill.Skills
import core.game.system.config.NPCConfigParser
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.impl.WildernessZone
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Sounds
import kotlin.math.abs

@Initializable
class RevenantsNPC
    @JvmOverloads
    constructor(
        id: Int = -1,
        location: Location? = null,
        val routes: Array<Array<Location>>? = null,
    ) : AbstractNPC(id, location) {
        private var swingHandler: CombatSwingHandler? = null
        val type: RevenantsType? = RevenantsType.forId(id)

        override fun configure() {
            isWalks = true
            isRespawn = false
            isAggressive = true
            isRenderable = true
            setDefaultBehavior()
            getAggressiveHandler().radius = 64 * 2
            getAggressiveHandler().chanceRatio = 9
            getAggressiveHandler().isAllowTolerance = false
            properties.combatTimeOut = 120
            configureBonuses()
            super.configure()
            this.swingHandler =
                RevenantCombatHandler(properties.attackAnimation, properties.magicAnimation, properties.rangeAnimation)
            setAttribute("food-items", 20)
        }

        override fun init() {
            super.init()
            registerRevenant(this)
            val spawnAnim = definition.getConfiguration(NPCConfigParser.SPAWN_ANIMATION, -1)
            if (spawnAnim != -1) {
                animate(Animation(spawnAnim))
            }
        }

        override fun clear() {
            super.clear()
            unregisterRevenant(this, true)
        }

        override fun finalizeDeath(killer: Entity) {
            super.finalizeDeath(killer)
            playGlobalAudio(killer.location, 4063)
        }

        override fun tick() {
            skills.pulse()
            walkingQueue.update()
            if (viewport.region.isActive) {
                updateMasks.prepare(this)
            }
            if (!DeathTask.isDead(this)) {
                val curhp = getSkills().lifepoints
                val maxhp = getSkills().getStaticLevel(Skills.HITPOINTS)
                val fooditems = getAttribute("food-items", 0)
                if (curhp < maxhp / 2 && fooditems > 0 && getAttribute("eat-delay", 0) < ticks) {
                    lock(3)
                    properties.combatPulse.delayNextAttack(3)
                    getSkills().heal(maxhp / 6)
                    for (p in getLocalPlayers(this)) {
                        playAudio(p, Sounds.EAT_2393)
                    }
                    setAttribute("eat-delay", ticks + 6)
                    setAttribute("food-items", fooditems - 1)
                }
            }
            behavior.tick(this)
            if (aggressiveHandler != null && aggressiveHandler.selectTarget()) {
                return
            }
        }

        override fun sendImpact(state: BattleState) {
            if (state.estimatedHit > type!!.maxHit) {
                state.estimatedHit = RandomFunction.random(type.maxHit - 5, type.maxHit)
            }
        }

        override fun getAudio(index: Int): Audio? {
            return null
        }

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC {
            return RevenantsNPC(id, location, null)
        }

        override fun setNextWalk() {
            nextWalk = ticks + RandomFunction.random(7, 15)
        }

        override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
            return swingHandler!!
        }

        override fun canMove(location: Location): Boolean {
            for (zone in SAFE_ZONES) {
                if (zone.insideBorder(location)) {
                    return false
                }
            }
            return true
        }

        override fun getMovementDestination(): Location {
            if (!pathBoundMovement || movementPath == null || movementPath.size < 1) {
                return getLocation().transform(
                    -5 + RandomFunction.random(getWalkRadius()),
                    -5 + RandomFunction.random(getWalkRadius()),
                    0,
                )
            }
            val l = movementPath[movementIndex++]
            if (movementIndex == movementPath.size) {
                movementIndex = 0
            }
            return l
        }

        override fun getWalkRadius(): Int {
            return 20
        }

        override fun continueAttack(
            target: Entity,
            style: CombatStyle,
            message: Boolean,
        ): Boolean {
            return if (target is Player) hasAcceptableCombatLevel(target.asPlayer()) else true
        }

        override fun isAttackable(
            entity: Entity,
            style: CombatStyle,
            message: Boolean,
        ): Boolean {
            if (entity is Player) {
                if (!hasAcceptableCombatLevel(entity.asPlayer()) && !entity.asPlayer().isAdmin) {
                    if (message) {
                        entity.asPlayer().sendMessage(
                            "The level difference between you and your opponent is too great.",
                        )
                    }
                    return false
                }
            }
            if (entity is content.global.skill.summoning.familiar.Familiar) {
                val owner = entity.owner ?: return false
                if (!hasAcceptableCombatLevel(owner)) {
                    return false
                }
            }
            return super.isAttackable(entity, style, message)
        }

        override fun canSelectTarget(target: Entity): Boolean {
            if (target !is Player) {
                return false
            }
            return hasAcceptableCombatLevel(target.asPlayer())
        }

        override fun getIds(): IntArray {
            return intArrayOf(
                6604,
                6635,
                6655,
                6666,
                6677,
                6697,
                6703,
                6715,
                6605,
                6612,
                6616,
                6620,
                6636,
                6637,
                6638,
                6639,
                6651,
                6656,
                6657,
                6658,
                6667,
                6678,
                6679,
                6680,
                6681,
                6693,
                6698,
                6699,
                6704,
                6705,
                6706,
                6707,
                6716,
                6717,
                6718,
                6719,
                6606,
                6621,
                6628,
                6640,
                6659,
                6682,
                6694,
                6708,
                6720,
                6622,
                6631,
                6641,
                6660,
                6668,
                6683,
                6709,
                6721,
                6608,
                6642,
                6661,
                6684,
                6710,
                6722,
                6727,
                6613,
                6623,
                6643,
                6652,
                6662,
                6669,
                6671,
                6674,
                6685,
                6695,
                6700,
                6711,
                6723,
                6607,
                6609,
                6614,
                6617,
                6625,
                6632,
                6644,
                6663,
                6675,
                6686,
                6701,
                6712,
                6724,
                6728,
                6645,
                6687,
                6646,
                6688,
                6647,
                6689,
                6610,
                6615,
                6618,
                6624,
                6626,
                6629,
                6633,
                6648,
                6653,
                6664,
                6670,
                6672,
                6690,
                6696,
                6702,
                6713,
                6725,
                6729,
                6649,
                6691,
                6611,
                6619,
                6627,
                6630,
                6634,
                6650,
                6654,
                6665,
                6673,
                6676,
                6692,
                6714,
                6726,
                6730,
                6998,
                6999,
            )
        }

        private fun configureBonuses() {
            for (i in properties.bonuses.indices) {
                properties.bonuses[i] = 40 + (4 * (properties.combatLevel / 2))
            }
        }

        private fun configureRoute() {
            if (routes.isNullOrEmpty()) {
                return
            }
            configureMovementPath(*RandomFunction.getRandomElement(routes))
        }

        private fun hasAcceptableCombatLevel(player: Player): Boolean {
            var level = WildernessZone.getWilderness(this)
            if (player.skullManager.level < level) {
                level = player.skullManager.level
            }
            val combat = properties.currentCombatLevel
            val targetCombat = player.properties.currentCombatLevel
            return abs((combat - targetCombat).toDouble()) <= level
        }

        companion object {
            private val SAFE_ZONES =
                arrayOf(
                    ZoneBorders(3074, 3651, 3193, 3774),
                    ZoneBorders(3264, 3672, 3279, 3695),
                    ZoneBorders(3081, 3909, 3129, 3954),
                    ZoneBorders(3350, 3869, 3391, 3900),
                )
        }
    }
