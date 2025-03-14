package content.region.wilderness.handlers.npc

import core.api.getPathableRandomLocalCoordinate
import core.api.playAudio
import core.api.playGlobalAudio
import core.api.utils.BossKillCounter
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Sounds

@Initializable
class ChaosElementalNPC
    @JvmOverloads
    constructor(
        id: Int = -1,
        location: Location? = null,
    ) : AbstractNPC(id, location) {
        private val COMBAT_HANDLER: MultiSwingHandler = ChaosCombatHandler()

        override fun tick() {
            super.tick()
            if (!isActive) {
                properties.combatPulse.stop()
            }
        }

        override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
            return COMBAT_HANDLER
        }

        override fun sendImpact(state: BattleState) {
            if (state.estimatedHit > 28) {
                state.estimatedHit = RandomFunction.random(20, 28)
            }
            super.sendImpact(state)
        }

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC {
            return ChaosElementalNPC(id, location)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.CHAOS_ELEMENTAL_3200)
        }

        override fun finalizeDeath(killer: Entity) {
            super.finalizeDeath(killer)
            BossKillCounter.addToKillCount(killer as Player, this.id)
        }

        class ChaosCombatHandler : MultiSwingHandler(*ATTACKS) {
            override fun impact(
                entity: Entity?,
                victim: Entity?,
                state: BattleState?,
            ) {
                super.impact(entity, victim, state)
                val attack = super.current
                if (victim is Player) {
                    val player = victim.asPlayer() ?: return
                    if (attack.projectile!!.projectileId == org.rs.consts.Graphics.MULTICOLORS_2_557) {
                        playGlobalAudio(location = player.location, id = Sounds.CHAOS_ELEMENTAL_DISCORD_IMPACT_350)
                    } else if (attack.projectile.projectileId == 554) {
                        playAudio(player, Sounds.CHAOS_ELEMENTAL_CONFUSION_IMPACT_346)
                        val loc = getPathableRandomLocalCoordinate(player, 10, entity!!.location, 3)
                        player.teleport(loc)
                    } else if (attack.projectile.projectileId == org.rs.consts.Graphics.GREEN_BALLS_SPIN_551) {
                        playGlobalAudio(location = player.location, id = Sounds.CHAOS_ELEMENTAL_MADNESS_IMPACT_353)
                        if (player.inventory.freeSlots() < 1 || player.equipment.itemCount() < 1) {
                            return
                        }
                        var e: Item? = null
                        var tries = 0
                        while (e == null && tries < 30) {
                            e = player.equipment.toArray()[RandomFunction.random(player.equipment.itemCount())]
                            tries++
                            if (e != null && player.inventory.hasSpaceFor(e)) {
                                break
                            }
                            e = null
                        }
                        if (e == null) {
                            return
                        }
                        player.lock(1)
                        if (!player.equipment.containsItem(e)) {
                            return
                        }
                        if (player.equipment.remove(e)) {
                            player.inventory.add(e)
                        }
                    }
                }
            }

            fun getRandomLoc(entity: Entity): Location {
                val l = entity.location
                val negative = RandomFunction.random(2) == 1
                return l.location.transform(
                    (
                        if (negative) {
                            RandomFunction.random(-10, 10)
                        } else {
                            RandomFunction.random(
                                0,
                                10,
                            )
                        }
                    ),
                    (if (negative) RandomFunction.random(-10, 10) else RandomFunction.random(0, 10)),
                    0,
                )
            }

            companion object {
                private val PROJECTILE_ANIM = Animation(Animations.CHAOS_ELEMENTAL_PROJECTILE_3148)
                private val PRIMARY_PROJECTILE: Projectile =
                    Projectile.create(
                        null as Entity?,
                        null,
                        org.rs.consts.Graphics.MULTICOLORS_2_557,
                        60,
                        55,
                        41,
                        46,
                        20,
                        255,
                    )
                private val ATTACKS =
                    arrayOf(
                        SwitchAttack(
                            CombatStyle.MELEE.swingHandler,
                            PROJECTILE_ANIM,
                            Graphics(org.rs.consts.Graphics.MULTICOLORS_556),
                            null,
                            PRIMARY_PROJECTILE,
                        ),
                        SwitchAttack(
                            CombatStyle.RANGE.swingHandler,
                            PROJECTILE_ANIM,
                            Graphics(org.rs.consts.Graphics.MULTICOLORS_556),
                            null,
                            PRIMARY_PROJECTILE,
                        ),
                        SwitchAttack(
                            CombatStyle.MAGIC.swingHandler,
                            PROJECTILE_ANIM,
                            Graphics(org.rs.consts.Graphics.MULTICOLORS_556),
                            null,
                            PRIMARY_PROJECTILE,
                        ),
                        object : SwitchAttack(
                            CombatStyle.MAGIC.swingHandler,
                            PROJECTILE_ANIM,
                            Graphics(org.rs.consts.Graphics.RED_550_553),
                            null,
                            Projectile.create(
                                null as Entity?,
                                null,
                                org.rs.consts.Graphics.RED_FLYING_554,
                                60,
                                55,
                                41,
                                46,
                                20,
                                255,
                            ),
                        ) {
                            override fun canSelect(
                                entity: Entity?,
                                victim: Entity?,
                                state: BattleState?,
                            ): Boolean {
                                return true
                            }
                        },
                        object : SwitchAttack(
                            CombatStyle.MAGIC.swingHandler,
                            PROJECTILE_ANIM,
                            Graphics(org.rs.consts.Graphics.GREEN_BALLS_550),
                            null,
                            Projectile.create(
                                null as Entity?,
                                null,
                                org.rs.consts.Graphics.GREEN_BALLS_SPIN_551,
                                60,
                                55,
                                41,
                                46,
                                20,
                                255,
                            ),
                        ) {
                            override fun canSelect(
                                entity: Entity?,
                                victim: Entity?,
                                state: BattleState?,
                            ): Boolean {
                                return true
                            }
                        },
                    )
            }
        }
    }
