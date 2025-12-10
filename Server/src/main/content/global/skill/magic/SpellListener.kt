package content.global.skill.magic

import core.api.playAudio
import core.api.playGlobalAudio
import core.api.sendMessage
import core.api.setAttribute
import core.cache.def.impl.ItemDefinition
import core.game.interaction.Listener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics

/**
 * Handling for spell casting logic.
 * @param bookName Name of the spellbook this listener belongs to.
 */
abstract class SpellListener(
    val bookName: String,
) : Listener {

    companion object {
        /**
         * Target type constants for spells.
         */
        @JvmField val NPC = -1
        @JvmField val OBJECT = -2
        @JvmField val ITEM = -3
        @JvmField val PLAYER = -4
        @JvmField val NONE = -5
        @JvmField val GROUND_ITEM = -6
    }

    /**
     * Registers a spell casting handler for a target type and range.
     */
    fun onCast(spellID: Int, type: Int, range: Int = 10, method: (player: Player, node: Node?) -> Unit) {
        SpellListeners.add(spellID, type, bookName, range, method)
    }

    /**
     * Registers a spell casting handler for target ids.
     */
    fun onCast(spellID: Int, type: Int, vararg ids: Int, range: Int = 10, method: (player: Player, node: Node?) -> Unit) {
        SpellListeners.add(spellID, type, ids, bookName, range, method)
    }

    /**
     * Checks if the player meets the requirements for casting a spell.
     * Throws [IllegalStateException] if requirements are not met.
     */
    fun requires(player: Player, magicLevel: Int = 0, runes: Array<Item> = arrayOf(), specialEquipment: IntArray = intArrayOf()) {
        if (player.getAttribute("magic-delay", 0) > GameWorld.ticks) {
            throw IllegalStateException("You are currently delayed from casting spells.")
        }
        if (player.getAttribute("tablet-spell", false)) return

        if (player.skills.getLevel(Skills.MAGIC) < magicLevel) {
            sendMessage(player, "You need a Magic level of $magicLevel to cast this spell.")
            throw IllegalStateException("Insufficient magic level.")
        }

        for (rune in runes) {
            if (!SpellUtils.hasRune(player, rune)) {
                sendMessage(player, "You don't have enough ${rune.definition.name.lowercase()}s to cast this spell.")
                throw IllegalStateException("Not enough runes.")
            }
        }

        for (item in specialEquipment) {
            if (!player.equipment.contains(item, 1)) {
                sendMessage(player, "You need a ${ItemDefinition.forId(item).name} to cast this.")
                throw IllegalStateException("Missing special equipment.")
            }
        }
    }

    /**
     * Removes runes used for the current spell cast from the inventory.
     */
    fun removeRunes(player: Player, removeAttr: Boolean = true) {
        player.inventory.remove(*player.getAttribute("spell:runes", ArrayList<Item>()).toTypedArray())
        if (removeAttr) {
            player.removeAttribute("spell:runes")
            player.removeAttribute("tablet-spell")
        }
    }

    /**
     * Adds Magic experience to the player, unless casting from a tablet.
     */
    fun addXP(player: Player, amount: Double) {
        if (player.getAttribute("tablet-spell", false)) return
        player.skills.addExperience(Skills.MAGIC, amount)
    }

    /**
     * Visualizes the spell using an [Animation] and [Graphics].
     * Optionally plays a sound, either globally or locally.
     */
    fun visualizeSpell(player: Player, anim: Animation, gfx: Graphics, soundID: Int = -1, delay: Int = 0, global: Boolean = true) {
        if (player.getAttribute("tablet-spell", false)) return
        player.visualize(anim, gfx)
        if (soundID != -1) {
            if (global) playGlobalAudio(player.location, soundID, delay)
            else playAudio(player, soundID, delay)
        }
    }

    /**
     * Visualizes the spell using animation and graphics ids.
     * Optionally plays a sound, either globally or locally.
     */
    fun visualizeSpell(player: Player, anim: Int, gfx: Int, height: Int = 0, soundID: Int = -1, delay: Int = 0, global: Boolean = true) {
        if (player.getAttribute("tablet-spell", false)) return
        player.visualize(Animation(anim), Graphics(gfx, height))
        if (soundID != -1) {
            if (global) playGlobalAudio(player.location, soundID, delay)
            else playAudio(player, soundID, delay)
        }
    }

    /**
     * Sets a casting delay on the player, longer if it is a teleport spell.
     */
    fun setDelay(player: Player, isTeleport: Boolean = false) {
        player.setAttribute("magic-delay", GameWorld.ticks + if (!isTeleport) 3 else 5)
    }

    /**
     * Sets a custom casting delay on the player.
     */
    fun setDelay(player: Player, delay: Int) {
        setAttribute(player, "magic-delay", GameWorld.ticks + delay)
    }

    /**
     * Interrupts the player's current spellcasting pulses.
     */
    fun interrupt(player: Player) {
        player.pulseManager.clear()
    }

    /**
     * Opens the magic tab for the player.
     */
    fun showMagicTab(player: Player) {
        player.interfaceManager.setViewedTab(6)
    }
}
