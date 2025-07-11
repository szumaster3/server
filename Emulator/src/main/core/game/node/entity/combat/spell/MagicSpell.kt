package core.game.node.entity.combat.spell

import core.api.playGlobalAudio
import core.game.component.Component
import core.game.event.SpellCastEvent
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.combat.spell.MagicStaff.Companion.forId
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld.ticks
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Plugin

/**
 * Abstract base class for magic spells.
 *
 * @property book The spellbook this spell belongs to.
 * @property level Required Magic level to cast the spell.
 * @property experience Experience gained when casting this spell.
 * @property animation Optional animation played when casting.
 * @property graphics Optional graphics effect when casting.
 * @property audio Optional audio played globally when casting.
 * @property castRunes Array of runes required to cast the spell.
 */
abstract class MagicSpell @JvmOverloads constructor(
    val book: SpellBook = SpellBook.MODERN,
    val level: Int = 0,
    @JvmField val experience: Double = 0.0,
    @JvmField protected val animation: Animation? = null,
    @JvmField protected val graphics: Graphics? = null,
    @JvmField val audio: Audio? = null,
    val castRunes: Array<Item?>? = arrayOfNulls(0),
) : Plugin<SpellType?> {

    /**
     * The unique id of this spell.
     */
    @JvmField
    var spellId: Int = 0

    /**
     * Delay (in ticks) between casts of this spell.
     */
    open val delay: Int get() = 3

    /**
     * Executes the spell logic on the given target.
     *
     * @param entity The caster of the spell.
     * @param target The target node of the spell.
     * @return True if the spell successfully cast, false otherwise.
     */
    abstract fun cast(entity: Entity, target: Node): Boolean

    /**
     * Visualizes the casting effects such as animation, graphics and audio.
     *
     * @param entity The caster of the spell.
     * @param target The target node of the spell.
     */
    open fun visualize(entity: Entity, target: Node) {
        entity.graphics(graphics)
        entity.animate(animation)
        playGlobalAudio(entity.location, audio!!.id, 20)
    }

    // Checks if the player is using a staff that substitutes for a rune.
    private fun usingStaff(p: Player, rune: Int): Boolean {
        val weapon = p.equipment[3] ?: return false
        val staff = forId(rune) ?: return false
        return staff.staves.contains(weapon.id)
    }

    /**
     * Checks if the caster meets all requirements to cast this spell,
     * including level, cooldowns, and runes.
     *
     * @param caster The entity attempting to cast the spell.
     * @param message Whether to send failure messages.
     * @param remove Whether to remove the required runes upon success.
     * @return True if all requirements are met, false otherwise.
     */
    open fun meetsRequirements(caster: Entity, message: Boolean, remove: Boolean): Boolean {
        if (!checkLevelRequirement(caster, message)) return false

        if (caster is Player) {
            val spell = caster.properties.autocastSpell
            val weapon = caster.equipment[3]
            if (spell != null && weapon != null) {
                val slayer = weapon.name.contains("layer's staff")
                val voidKnight = weapon.name.contains("knight mace")
                if ((spell.spellId == 31 && !slayer) || (spell.spellId == 42 && !voidKnight)) {
                    caster.packetDispatch.sendMessage("You need the proper staff to autocast this spell.")
                    return false
                }
            }

            if ((spellId == 12 || spellId == 30 || spellId == 56) && caster.getAttribute("entangleDelay", 0) > ticks) {
                caster.sendMessage("You have recently cast a binding spell.")
                return false
            }

            if (castRunes == null) return true

            val toRemove = mutableListOf<Item?>()
            for (item in castRunes) {
                if (!hasRune(caster, item, toRemove, message)) return false
            }
            if (remove) toRemove.forEach { caster.inventory.remove(it) }
        }
        return true
    }

    // Checks if caster has required Magic level.
    private fun checkLevelRequirement(caster: Entity, message: Boolean): Boolean {
        if (caster is Player && caster.getSkills().getLevel(Skills.MAGIC, this is CombatSpell) < level) {
            if (message) caster.packetDispatch.sendMessage("You need a Magic level of $level to cast this spell.")
            return false
        }
        return true
    }

    // Checks and accumulates runes to be removed for the spell.
    private fun hasRune(p: Player, item: Item?, toRemove: MutableList<Item?>, message: Boolean): Boolean {
        if (usingStaff(p, item!!.id)) return true

        val hasBase = p.inventory.contains(item.id, item.amount)
        if (hasBase) {
            toRemove.add(item)
            return true
        }

        val baseAmt = p.inventory.getAmount(item.id)
        if (baseAmt > 0) toRemove.add(Item(item.id, baseAmt))

        var amtRemaining = item.amount - baseAmt
        val combos = CombinationRune.eligibleFor(Runes.forId(item.id)!!)

        for (r in combos) {
            if (amtRemaining <= 0) break
            val amt = p.inventory.getAmount(r.id)
            if (amt > 0) {
                val toTake = amtRemaining.coerceAtMost(amt)
                toRemove.add(Item(r.id, toTake))
                amtRemaining -= toTake
            }
        }

        if (amtRemaining <= 0) return true

        if (message) p.packetDispatch.sendMessage("You don't have enough ${item.name}s to cast this spell.")
        return false
    }

    /**
     * Adds experience to the caster based on the hit dealt.
     *
     * @param entity The caster entity.
     * @param hit The amount of damage dealt.
     */
    open fun addExperience(entity: Entity, hit: Int) {
        entity.getSkills().addExperience(Skills.MAGIC, experience, true)
        if (entity !is Player || hit < 1) return

        entity.getSkills().addExperience(Skills.HITPOINTS, hit * 1.33, true)
        if (entity.getProperties().attackStyle!!.style == WeaponInterface.STYLE_DEFENSIVE_CAST) {
            val xp = (CombatSwingHandler.EXPERIENCE_MOD * hit) / 2.0
            entity.getSkills().addExperience(Skills.DEFENCE, xp, true)
            entity.getSkills().addExperience(Skills.MAGIC, xp, true)
        } else {
            entity.getSkills().addExperience(Skills.MAGIC, hit * CombatSwingHandler.EXPERIENCE_MOD, true)
        }
    }

    /** Returns the required magic level to cast this spell */
    fun levelRequirement(): Int = level

    override fun fireEvent(identifier: String, vararg args: Any): Any? = null

    /** Returns the experience gained when casting this spell */
    open fun getExperience(player: Player): Double = experience

    companion object {
        /**
         * Attempts to cast a spell from a player's spellbook on a target.
         *
         * @param p The player casting the spell.
         * @param book The spellbook the spell belongs to.
         * @param spellId The spell ID.
         * @param target The target node.
         * @return True if the spell was cast successfully, false otherwise.
         */
        fun castSpell(p: Player, book: SpellBook, spellId: Int, target: Node): Boolean {
            if (p.getAttribute("magic-delay", 0) > ticks) return false

            val spell = book.getSpell(spellId) ?: return false
            if (spell.book != book || p.spellBookManager.spellBook != book.interfaceId) return false

            if (target !== p && target.location != null && !target.location.withinDistance(p.location, 15)) return false
            p.faceLocation(target.location)

            if (spell is CombatSpell) {
                // do nothing
            } else if (target is Entity) {
                p.faceTemporary(target, 1)
            }

            if (!spell.cast(p, target)) return false

            if (book != SpellBook.LUNAR && p.getAttribute("spell:swap", 0) != 0) {
                p.removeAttribute("spell:swap")
                p.spellBookManager.setSpellBook(SpellBook.LUNAR)
                p.interfaceManager.openTab(Component(SpellBook.LUNAR.interfaceId))
            }

            if (spell !is CombatSpell) {
                p.getSkills().addExperience(Skills.MAGIC, spell.getExperience(p), true)
            }

            if (p.getAttribute("magic-delay", 0) <= ticks) {
                p.setAttribute("magic-delay", ticks + spell.delay)
            }

            p.dispatch(SpellCastEvent(book, spellId, target))
            return true
        }
    }
}