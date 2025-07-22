package content.region.kandarin.feldip.jiggig.quest.zogre.npc

import content.region.kandarin.feldip.jiggig.quest.zogre.dialogue.ZavisticRarveDialogueFiles
import content.region.kandarin.feldip.jiggig.quest.zogre.plugin.ZogreUtils
import core.api.openDialogue
import core.api.poofClear
import core.api.setAttribute
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import org.rs.consts.NPCs

class ZavisticRarveNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var clearTime = 0
    private val player: Player? = null

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = ZavisticRarveNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.ZAVISTIC_RARVE_2059)

    override fun handleTickActions() {
        super.handleTickActions()
        if (player != null) {
            if (player.location.getDistance(getLocation()) > 4 || !player.isActive || clearTime++ > 300) {
                poofClear(this)
                core.api.removeAttribute(player, ZogreUtils.NPC_ACTIVE)
            }
        }
    }

    companion object {
        @JvmStatic
        fun spawnWizard(player: Player) {
            val wizard = ZavisticRarveNPC(NPCs.ZAVISTIC_RARVE_2059)
            wizard.location = Location.create(2598, 3087, 0)
            wizard.isWalks = false
            wizard.isAggressive = false
            wizard.isActive = false

            if (wizard.asNpc() != null && wizard.isActive) {
                wizard.properties.teleportLocation = wizard.properties.spawnLocation
            }
            wizard.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(0, wizard) {
                    override fun pulse(): Boolean {
                        wizard.init()
                        setAttribute(player, ZogreUtils.NPC_ACTIVE, true)
                        openDialogue(player, ZavisticRarveDialogueFiles())
                        return true
                    }
                },
            )
        }
    }
}