package core.game.node.entity.combat.equipment

import core.api.event.applyPoison
import core.api.playGlobalAudio
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld.ticks
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds
import kotlin.math.ceil

enum class BoltEffect(
    val itemId: Int,
    private val graphics: Graphics,
    private val sound: Audio,
) {
    OPAL(Items.OPAL_BOLTS_E_9236, Graphics.create(749), Audio(Sounds.LUCKY_LIGHTNING_2918)) {
        override fun impact(state: BattleState) {
            state.estimatedHit += RandomFunction.random(3, 20)
            if (state.estimatedHit > 29) {
                state.estimatedHit = 21
            }
            super.impact(state)
        }
    },

    JADE(Items.JADE_BOLTS_E_9237, Graphics(755), Audio(Sounds.EARTHS_FURY_2916)) {
        override fun impact(state: BattleState) {
            if (state.victim is Player) {
                val p = state.victim.asPlayer()
                p.lock(2)
            }
            super.impact(state)
        }

        override fun canFire(state: BattleState): Boolean {
            var success = false
            if (state.victim is Player) {
                val p = state.victim.asPlayer()
                val level = p.getSkills().getLevel(Skills.AGILITY).toDouble()
                val req = 80.0
                val successChance = ceil((level * 50 - req * 15) / req / 3 * 4)
                val roll = RandomFunction.random(99)
                success = successChance >= roll
            }
            return super.canFire(state) && !success
        }
    },

    PEARL(Items.PEARL_BOLTS_E_9238, Graphics.create(750), Audio(Sounds.SEA_CURSE_2920)) {
        override fun impact(state: BattleState) {
            state.estimatedHit += RandomFunction.random(3, 20)
            if (state.estimatedHit > 29) {
                state.estimatedHit = 21
            }
            super.impact(state)
        }

        override fun canFire(state: BattleState): Boolean {
            if (state.victim is Player) {
                if (state.victim
                        .asPlayer()
                        .equipment
                        .contains(1383, 1) ||
                    state.victim
                        .asPlayer()
                        .equipment
                        .contains(1395, 1)
                ) {
                    return false
                }
            }
            return super.canFire(state)
        }
    },

    TOPAZ(Items.TOPAZ_BOLTS_E_9239, Graphics.create(757), Audio(Sounds.DOWN_TO_EARTH_2914)) {
        override fun impact(state: BattleState) {
            if (state.victim is Player) {
                val p = state.victim.asPlayer()
                val level = (p.getSkills().getLevel(Skills.MAGIC) * 0.03).toInt()
                p.getSkills().updateLevel(Skills.MAGIC, -level, 0)
            }
            super.impact(state)
        }
    },

    SAPPHIRE(
        Items.SAPPHIRE_BOLTS_E_9240,
        Graphics(759, 100),
        Audio(Sounds.CLEAR_MIND_2912),
    ) {
        override fun impact(state: BattleState) {
            if (state.victim is Player && state.attacker is Player) {
                val p = state.victim.asPlayer()
                val player = state.attacker.asPlayer()
                val give = (p.getSkills().prayerPoints * 0.05).toInt()
                if (give > 0) {
                    p.getSkills().decrementPrayerPoints(give.toDouble())
                    player.getSkills().incrementPrayerPoints(give.toDouble())
                }
            }
            super.impact(state)
        }
    },

    EMERALD(Items.EMERALD_BOLTS_E_9241, Graphics(752), Audio(Sounds.MAGICAL_POISON_2919)) {
        override fun impact(state: BattleState) {
            applyPoison(state.victim, state.attacker, 40)
            super.impact(state)
        }
    },

    RUBY(
        Items.RUBY_BOLTS_E_9242,
        Graphics(754),
        Audio(Sounds.BLOOD_SACRIFICE_2911, 1),
    ) {
        override fun impact(state: BattleState) {
            var victimPoints = (state.victim.getSkills().lifepoints * 0.20).toInt()
            val playerPoints = (state.attacker.getSkills().lifepoints * 0.10).toInt()
            if (victimPoints >= 100 && state.victim.id == NPCs.CORPOREAL_BEAST_8133) {
                victimPoints = 100
            }
            state.estimatedHit = victimPoints
            state.attacker.impactHandler.manualHit(state.victim, playerPoints, HitsplatType.NORMAL)
            super.impact(state)
        }

        override fun canFire(state: BattleState): Boolean {
            val playerPoints = (state.attacker.getSkills().lifepoints * 0.10).toInt()
            if (playerPoints < 1) {
                return false
            }
            return super.canFire(state) && state.attacker.getSkills().lifepoints - playerPoints >= 1
        }
    },

    DIAMOND(Items.DIAMOND_BOLTS_E_9243, Graphics(758), Audio(Sounds.CROSSBOW_DIAMOND_2913)) {
        override fun impact(state: BattleState) {
            state.estimatedHit += RandomFunction.random(5, 14)
            super.impact(state)
        }
    },

    DRAGON(Items.DRAGON_BOLTS_E_9244, Graphics(756), Audio(Sounds.DRAGONS_BREATH_2915)) {
        override fun impact(state: BattleState) {
            state.estimatedHit += RandomFunction.random(17, 29)
            super.impact(state)
        }

        override fun canFire(state: BattleState): Boolean {
            if (state.victim is NPC) {
                val n = state.victim as NPC
                if (n.name.lowercase().contains("fire") || n.name.lowercase().contains("dragon")) {
                    return false
                }
            }
            if (state.victim is Player) {
                if (state.victim
                        .asPlayer()
                        .equipment
                        .contains(1540, 1) ||
                    state.victim
                        .asPlayer()
                        .equipment
                        .contains(11283, 1) ||
                    state.victim.hasFireResistance()
                ) {
                    return false
                }
            }
            return super.canFire(state)
        }
    },

    ONYX(Items.ONYX_BOLTS_E_9245, Graphics(753), Audio(Sounds.LIFE_LEECH_2917)) {
        override fun impact(state: BattleState) {
            val newDamage = (state.estimatedHit * 0.25).toInt()
            state.estimatedHit += newDamage
            state.attacker.getSkills().heal((state.estimatedHit * 0.25).toInt())
            state.attacker.setAttribute("onyx-effect", ticks + 12)
            super.impact(state)
        }

        override fun canFire(state: BattleState): Boolean {
            if (state.attacker.getAttribute("onyx-effect", 0) > ticks) {
                return false
            }
            if (state.victim is NPC) {
                val n = state.victim as NPC
                if (n.task != null && n.task.undead) {
                    return false
                }
            }
            return super.canFire(state)
        }
    }, ;

    open fun impact(state: BattleState) {
        val victim = state.victim
        val attacker = state.attacker
        if (attacker is Player) {
            playGlobalAudio(attacker.getLocation(), sound.id)
        }
        if (victim is Player) {
            playGlobalAudio(victim.getLocation(), sound.id)
        }
        victim.graphics(graphics)
    }

    open fun canFire(state: BattleState): Boolean {
        return RandomFunction.random(13) == 5
    }

    companion object {
        @JvmStatic fun forId(id: Int): BoltEffect? {
            for (effect in values()) {
                if (effect.itemId == id) {
                    return effect
                }
            }
            return null
        }
    }
}
