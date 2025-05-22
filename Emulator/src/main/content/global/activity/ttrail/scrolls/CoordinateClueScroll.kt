package content.global.activity.ttrail.scrolls

import content.global.activity.ttrail.ClueLevel
import core.api.sendString
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents a coordinate clue scroll.
 */
abstract class CoordinateClueScroll(
    name: String?, clueId: Int, level: ClueLevel?, location: Location?,
    val clue: String?
) : MapClueScroll(name, clueId, level, Components.TRAIL_MAP09_345, location, 0) {

    /**
     * Displays the clue text on the interface when the player reads the clue.
     *
     * @param player The player reading the clue.
     */
    override fun read(player: Player) {
        for (i in 1..8) {
            sendString(player,"", interfaceId, i)
        }
        super.read(player)
        sendString(player,"<br><br><br><br><br>" + clue?.replace("<br>", "<br><br>"), interfaceId, 1)
    }

    /**
     * Handles digging at the clue location.
     *
     * @param player The player digging at the clue location.
     */
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

    /**
     * Spawns a wizard to attack the player as part of a hard clue scroll.
     *
     * @param player The player to spawn the wizard for.
     */
    private fun spawnWizard(player: Player) {
        val id = if (!player.skullManager.isWilderness) NPCs.SARADOMIN_WIZARD_1264 else NPCs.ZAMORAK_WIZARD_1007
        val wizard = NPC.create(id, player.location.transform(1, 0, 0))
        player.setAttribute("t-wizard", wizard)
        wizard.setAttribute("clue", this)
        wizard.setAttribute("player", player)
        wizard.init()
        wizard.graphics(Graphics.create(86))
        wizard.faceTemporary(player, 1)
        wizard.sendChat(if (id == NPCs.SARADOMIN_WIZARD_1264) "For Saradomin!" else "Die human!")
        wizard.properties.combatPulse.attack(player)
    }

    companion object {
        /**
         * The sextant item used for coordinate clues.
         */
        val SEXTANT: Item = Item(Items.SEXTANT_2574)

        /**
         * The watch item used for coordinate clues.
         */
        val WATCH: Item = Item(Items.WATCH_2575)

        /**
         * The chart item used for coordinate clues.
         */
        val CHART: Item = Item(Items.CHART_2576)

        /**
         * A location used in clue validation (e.g., the clock tower).
         */
        val CLOCK_TOWER: Location = Location(2440, 3161, 0)
    }
}
