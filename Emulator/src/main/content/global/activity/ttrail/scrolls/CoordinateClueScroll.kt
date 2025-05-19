package content.global.activity.ttrail.scrolls

import content.global.activity.ttrail.ClueLevel
import core.api.sendString
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics

/**
 * Represents a coordinate clue scroll.
 */
abstract class CoordinateClueScroll
/**
 * Instantiates a new Coordinate clue scroll.
 *
 * @param name     the name
 * @param clueId   the clue id
 * @param level    the level
 * @param location the location
 * @param clue     the clue
 */(
    name: String?, clueId: Int, level: ClueLevel?, location: Location?,
    /**
     * Gets clue.
     *
     * @return the clue
     */
    val clue: String
) : MapClueScroll(name, clueId, level, 345, location, 0) {
    override fun read(player: Player) {
        for (i in 1..8) {
            sendString(player,"", interfaceId, i)
        }
        super.read(player)
        sendString(player,"<br><br><br><br><br>" + clue.replace("<br>", "<br><br>"), interfaceId, 1)
    }

    override fun dig(player: Player?) {
        val killedWizardClueId = player!!.getAttribute("killed-wizard", -1)
        if (level == ClueLevel.HARD && (killedWizardClueId == -1 || killedWizardClueId != clueId)) {
            val wizard = player.getAttribute<NPC>("t-wizard", null)
            if (wizard != null && wizard.isActive) {
                return
            }
            spawnWizard(player)
            return
        }
        super.dig(player)
        player.removeAttribute("killed-wizard")
    }

    private fun spawnWizard(player: Player) {
        val id = if (!player.skullManager.isWilderness) 1264 else 1007
        val wizard = NPC.create(id, player.location.transform(1, 0, 0))
        player.setAttribute("t-wizard", wizard)
        wizard.setAttribute("clue", this)
        wizard.setAttribute("player", player)
        wizard.init()
        wizard.graphics(Graphics.create(86))
        wizard.faceTemporary(player, 1)
        wizard.sendChat(if (id == 1264) "For Saradomin!" else "Die human!")
        wizard.properties.combatPulse.attack(player)
    }

    companion object {
        /**
         * The constant SEXTANT.
         */
        val SEXTANT: Item = Item(2574)

        /**
         * The constant WATCH.
         */
        val WATCH: Item = Item(2575)

        /**
         * The constant CHART.
         */
        val CHART: Item = Item(2576)

        /**
         * The constant CLOCK_TOWER.
         */
        val CLOCK_TOWER: Location = Location(2440, 3161, 0)
    }
}
