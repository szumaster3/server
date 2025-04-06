package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Cockatrice familiar npc.
 */
@Initializable
class CockatriceFamiliarNPC : Plugin<Any?> {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        definePlugin(SpiritCockatrice())
        definePlugin(SpiritGuthatrice())
        definePlugin(SpiritZamatrice())
        definePlugin(SpiritPengatrice())
        definePlugin(SpiritCoraxatrice())
        definePlugin(SpiritVulatrice())
        definePlugin(SpiritSaratrice())
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any? {
        return null
    }

    /**
     * Petrifying gaze boolean.
     *
     * @param familiar the familiar
     * @param special  the special
     * @param skill    the skill
     * @return the boolean
     */
    fun petrifyingGaze(familiar: Familiar, special: FamiliarSpecial, skill: Int): Boolean {
        val target = special.target
        if (!familiar.canCombatSpecial(target)) {
            return false
        }
        familiar.faceTemporary(target, 2)
        familiar.visualize(Animation.create(7762), Graphics.create(1467))
        Pulser.submit(object : Pulse(1, familiar.owner, familiar, target) {
            override fun pulse(): Boolean {
                if (skill == 5) {
                    target.skills.decrementPrayerPoints(3.0)
                } else {
                    target.getSkills().updateLevel(skill, -3, 0)
                }
                Projectile.magic(familiar, target, 1468, 40, 36, 71, 10).send()
                familiar.sendFamiliarHit(target, 10, Graphics.create(1469))
                return true
            }
        })
        return true
    }

    /**
     * The type Spirit cockatrice.
     */
    inner class SpiritCockatrice
    /**
     * Instantiates a new Spirit cockatrice.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Spirit cockatrice.
     */
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_COCKATRICE_6875) :
        Forager(owner, id, 3600, Items.SP_COCKATRICE_POUCH_12095, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG) {
        override fun construct(owner: Player, id: Int): Familiar {
            return SpiritCockatrice(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return petrifyingGaze(this, special, Skills.DEFENCE)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.SPIRIT_COCKATRICE_6875, NPCs.SPIRIT_COCKATRICE_6876)
        }
    }

    /**
     * The type Spirit guthatrice.
     */
    inner class SpiritGuthatrice
    /**
     * Instantiates a new Spirit guthatrice.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Spirit guthatrice.
     */
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_GUTHATRICE_6877) :
        Forager(owner, id, 3600, Items.SP_GUTHATRICE_POUCH_12097, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG) {
        override fun construct(owner: Player, id: Int): Familiar {
            return SpiritGuthatrice(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return petrifyingGaze(this, special, Skills.ATTACK)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.SPIRIT_GUTHATRICE_6877, NPCs.SPIRIT_GUTHATRICE_6878)
        }
    }

    /**
     * The type Spirit zamatrice.
     */
    inner class SpiritZamatrice
    /**
     * Instantiates a new Spirit zamatrice.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Spirit zamatrice.
     */
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_ZAMATRICE_6881) :
        Forager(owner, id, 3600, Items.SP_ZAMATRICE_POUCH_12101, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG) {
        override fun construct(owner: Player, id: Int): Familiar {
            return SpiritZamatrice(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return petrifyingGaze(this, special, Skills.STRENGTH)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.SPIRIT_ZAMATRICE_6881, NPCs.SPIRIT_ZAMATRICE_6882)
        }
    }

    /**
     * The type Spirit pengatrice.
     */
    inner class SpiritPengatrice
    /**
     * Instantiates a new Spirit pengatrice.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Spirit pengatrice.
     */
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_PENGATRICE_6883) :
        Forager(owner, id, 3600, Items.SP_PENGATRICE_POUCH_12103, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG) {
        override fun construct(owner: Player, id: Int): Familiar {
            return SpiritPengatrice(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return petrifyingGaze(this, special, Skills.MAGIC)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.SPIRIT_PENGATRICE_6883, NPCs.SPIRIT_PENGATRICE_6884)
        }
    }

    /**
     * The type Spirit coraxatrice.
     */
    inner class SpiritCoraxatrice
    /**
     * Instantiates a new Spirit coraxatrice.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Spirit coraxatrice.
     */
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_CORAXATRICE_6885) :
        Forager(owner, id, 3600, Items.SP_CORAXATRICE_POUCH_12105, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG) {
        override fun construct(owner: Player, id: Int): Familiar {
            return SpiritCoraxatrice(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return petrifyingGaze(this, special, Skills.SUMMONING)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.SPIRIT_CORAXATRICE_6885, NPCs.SPIRIT_CORAXATRICE_6886)
        }
    }

    /**
     * The type Spirit vulatrice.
     */
    inner class SpiritVulatrice
    /**
     * Instantiates a new Spirit vulatrice.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Spirit vulatrice.
     */
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_VULATRICE_6887) :
        Forager(owner, id, 3600, Items.SP_VULATRICE_POUCH_12107, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG) {
        override fun construct(owner: Player, id: Int): Familiar {
            return SpiritVulatrice(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return petrifyingGaze(this, special, Skills.RANGE)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.SPIRIT_VULATRICE_6887, NPCs.SPIRIT_VULATRICE_6888)
        }
    }

    /**
     * The type Spirit saratrice.
     */
    inner class SpiritSaratrice
    /**
     * Instantiates a new Spirit saratrice.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Spirit saratrice.
     */
    @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_SARATRICE_6879) :
        Forager(owner, id, 3600, Items.SP_SARATRICE_POUCH_12099, 3, WeaponInterface.STYLE_CAST, COCKATRICE_EGG) {
        override fun construct(owner: Player, id: Int): Familiar {
            return SpiritSaratrice(owner, id)
        }

        override fun specialMove(special: FamiliarSpecial): Boolean {
            return petrifyingGaze(this, special, Skills.PRAYER)
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.SPIRIT_SARATRICE_6879, NPCs.SPIRIT_SARATRICE_6880)
        }
    }

    companion object {
        private val COCKATRICE_EGG = Item(Items.COCKATRICE_EGG_12109)
    }
}
