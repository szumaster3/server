package content.global.activity.ttrail.scrolls

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScroll
import core.api.sendString
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.entity.player.link.emote.Emotes.Companion.forId
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Graphics

/**
 * Represents an emote-based clue scroll.
 */
abstract class EmoteClueScroll(
    name: String?,
    clueId: Int,
    level: ClueLevel?,
    val emote: Emotes?,
    val commenceEmote: Emotes?,
    val equipment: Array<IntArray>?,
    private val clue: String?,
    vararg borders: ZoneBorders?
) : ClueScroll(
    name!!, clueId, level!!, 345, borders.filterNotNull().toTypedArray()
) {

    /**
     * Handles action button presses within the clue interface.
     */
    override fun actionButton(
        player: Player, interfaceId: Int, buttonId: Int, slot: Int, itemId: Int, opcode: Int
    ): Boolean {
        if (!player.inventory.contains(clueId, 1)) {
            return false
        }
        val emote = forId(buttonId) ?: return false
        if (emote === this.emote) {
            val oldUri = player.getAttribute<NPC>("uri", null)
            if (oldUri != null && oldUri.isActive) {
                return false
            }
            spawnUri(player)
        } else if (emote === commenceEmote) {
            player.setAttribute("commence-emote", true)
        }
        return if (this.emote === emote) {
            super.actionButton(player, interfaceId, buttonId, slot, itemId, opcode)
        } else {
            false
        }
    }

    /**
     * Reads the clue scroll, displaying its clue text to the player.
     *
     * @param player The player reading the clue.
     */
    override fun read(player: Player) {
        for (i in 1..8) {
            sendString(player, "", interfaceId, i)
        }
        super.read(player)
        sendString(player, clue!!.replace("<br>", "<br><br>"), interfaceId, 1)
    }

    /**
     * Spawns the NPC near the player to progress the clue scroll.
     *
     * @param player The player near whom Uri will spawn.
     */
    private fun spawnUri(player: Player) {
        var doubleAgent = level == ClueLevel.HARD && player.getAttribute("killed-agent", 0) != clueId

        var id = 5141
        if (doubleAgent) {
            val wilderness = player.skullManager.isWilderness
            if (wilderness) {
                id = 5141
                doubleAgent = false
            } else {
                id = 5145
            }
        }
        val uri = NPC.create(id, player.location.transform(1, 0, 0))
        player.setAttribute("uri", uri)
        player.removeAttribute("commence-emote")
        uri.setAttribute("double-agent", doubleAgent)
        uri.setAttribute("player", player)
        uri.setAttribute("clue", this)
        uri.init()
        uri.graphics(Graphics.create(86))
        uri.faceTemporary(player, 1)
        if (doubleAgent) {
            uri.sendChat("I expect you to die!")
            uri.properties.combatPulse.attack(player)
        }
    }

    /**
     * Checks if this clue scroll has a commence emote defined.
     *
     * @return True if a commence emote is set, false otherwise.
     */
    fun hasCommencedEmote(): Boolean {
        return commenceEmote != null
    }
}
