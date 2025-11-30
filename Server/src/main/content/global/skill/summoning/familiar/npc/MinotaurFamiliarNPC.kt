package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.playGlobalAudio
import core.api.stun
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds
import kotlin.math.floor

@Initializable
class MinotaurFamiliarNPC : Plugin<Any?> {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        definePlugin(BronzeMinotaurNPC())
        definePlugin(IronMinotaurNPC())
        definePlugin(SteelMinotaurNPC())
        definePlugin(MithrilMinotaurNPC())
        definePlugin(AdamantMinotaurNPC())
        definePlugin(RuneMinotaurNPC())
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any? {
        return null
    }


    fun bullRush(familiar: Familiar, special: FamiliarSpecial, maxHit: Int): Boolean {
        val target = special.node as Entity
        if (!familiar.canCombatSpecial(target)) {
            return false
        }
        playGlobalAudio(familiar.location, Sounds.MINOTAUR_SPECIAL_4337)
        familiar.sendFamiliarHit(target, RandomFunction.random(maxHit))
        Projectile.magic(familiar, target, 1497, 80, 36, 70, 10).send()
        familiar.visualize(Animation.create(8026), Graphics.create(1496))
        if (!(familiar is BronzeMinotaurNPC || familiar is RuneMinotaurNPC) && RandomFunction.random(10) < 6) {
            val ticks = 2 + floor(familiar.location.getDistance(target.location) * 0.5).toInt()
            Pulser.submit(object : Pulse(ticks) {
                override fun pulse(): Boolean {
                    stun(target, 4)
                    return true
                }
            })
        }
        return true
    }


    inner class BronzeMinotaurNPC
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.BRONZE_MINOTAUR_6853) :
        Familiar(owner, id, 3000, Items.BRONZE_MINOTAUR_POUCH_12073, 6, WeaponInterface.STYLE_DEFENSIVE) {
        override fun construct(owner: Player, id: Int): Familiar {
            return BronzeMinotaurNPC(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return bullRush(this, special, 4)
        }

        override fun isPoisonImmune(): Boolean {
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.BRONZE_MINOTAUR_6853, NPCs.BRONZE_MINOTAUR_6854)
        }
    }


    inner class IronMinotaurNPC
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.IRON_MINOTAUR_6855) :
        Familiar(owner, id, 3700, Items.IRON_MINOTAUR_POUCH_12075, 6, WeaponInterface.STYLE_DEFENSIVE) {
        override fun construct(owner: Player, id: Int): Familiar {
            return IronMinotaurNPC(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return bullRush(this, special, 6)
        }

        override fun isPoisonImmune(): Boolean {
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.IRON_MINOTAUR_6855, NPCs.IRON_MINOTAUR_6856)
        }
    }


    inner class SteelMinotaurNPC
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.STEEL_MINOTAUR_6857) :
        Familiar(owner, id, 4600, Items.STEEL_MINOTAUR_POUCH_12077, 6, WeaponInterface.STYLE_DEFENSIVE) {
        override fun construct(owner: Player, id: Int): Familiar {
            return SteelMinotaurNPC(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return bullRush(this, special, 9)
        }

        override fun getCombatStyle(): CombatStyle {
            return CombatStyle.MELEE
        }

        override fun isPoisonImmune(): Boolean {
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.STEEL_MINOTAUR_6857, NPCs.STEEL_MINOTAUR_6858)
        }
    }


    inner class MithrilMinotaurNPC
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.MITHRIL_MINOTAUR_6859) :
        Familiar(owner, id, 5500, Items.MITHRIL_MINOTAUR_POUCH_12079, 6, WeaponInterface.STYLE_DEFENSIVE) {
        override fun construct(owner: Player, id: Int): Familiar {
            return MithrilMinotaurNPC(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return bullRush(this, special, 13)
        }

        override fun isPoisonImmune(): Boolean {
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.MITHRIL_MINOTAUR_6859, NPCs.MITHRIL_MINOTAUR_6860)
        }
    }


    inner class AdamantMinotaurNPC
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.ADAMANT_MINOTAUR_6861) :
        Familiar(owner, id, 6600, Items.ADAMANT_MINOTAUR_POUCH_12081, 6, WeaponInterface.STYLE_DEFENSIVE) {
        override fun construct(owner: Player, id: Int): Familiar {
            return AdamantMinotaurNPC(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return bullRush(this, special, 16)
        }

        override fun getCombatStyle(): CombatStyle {
            return CombatStyle.MELEE
        }

        override fun isPoisonImmune(): Boolean {
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.ADAMANT_MINOTAUR_6861, NPCs.ADAMANT_MINOTAUR_6862)
        }
    }


    inner class RuneMinotaurNPC
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.RUNE_MINOTAUR_6863) :
        Familiar(owner, id, 15100, Items.RUNE_MINOTAUR_POUCH_12083, 6, WeaponInterface.STYLE_DEFENSIVE) {
        override fun construct(owner: Player, id: Int): Familiar {
            return RuneMinotaurNPC(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return bullRush(this, special, 20)
        }

        override fun isPoisonImmune(): Boolean {
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.RUNE_MINOTAUR_6863, NPCs.RUNE_MINOTAUR_6864)
        }
    }
}
