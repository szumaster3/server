package core.game.world.map.zone.impl

import content.region.island.tutorial.plugin.*
import core.ServerConstants
import core.api.*
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.system.task.Pulse
import core.game.world.GameWorld.settings
import core.game.world.map.zone.MapZone
import core.worker.ManagementEvents.publish
import proto.management.JoinClanRequest

/**
 * Represents the tutorial zone area.
 */
class TutorialZone : MapZone("tutorial island", true) {

    override fun configure() {
        for (regionId in REGIONS) {
            registerRegion(regionId)
        }
    }

    override fun teleport(entity: Entity, type: Int, node: Node?): Boolean {
        if (entity is Player) {
            val p = entity
            val a = Rights.ADMINISTRATOR
            if (p.rights == a) {
                return super.teleport(entity, type, node)
            }
            if (p.getAttribute(TutorialStage.TUTORIAL_STAGE, -1) < 72) {
                lockTeleport(p)
            } else {
                submitWorldPulse(
                    object : Pulse() {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                18 -> {
                                    completeTutorial(p)
                                    return true
                                }
                            }
                            return false
                        }
                    })
            }
        }
        return super.teleport(entity, type, node)
    }

    companion object {
        /**
         * Represents the region ids.
         */
        private val REGIONS = intArrayOf(
            12079,
            12180,
            12592,
            12436,
            12335,
            12336
        )

        /**
         * Handles completing the tutorial.
         *
         * @param player the player who complete tutorial.
         */
        fun completeTutorial(player: Player) {
            setVarbit(player, TutorialStage.FLASHING_ICON, 0)
            setVarp(player, 281, 1000, true)
            setAttribute(player, "/save:tutorial:complete", true)
            setAttribute(player, "/save:tutorial:stage", 73)

            player.unhook(TutorialCastReceiver)
            player.unhook(TutorialKillReceiver)
            player.unhook(TutorialFireReceiver)
            player.unhook(TutorialResourceReceiver)
            player.unhook(TutorialUseWithReceiver)
            player.unhook(TutorialInteractionReceiver)
            player.unhook(TutorialButtonReceiver)

            if (settings != null && settings!!.enable_default_clan) {
                player.communication.currentClan = ServerConstants.SERVER_NAME
                val clanJoin = JoinClanRequest.newBuilder()
                clanJoin.setClanName(ServerConstants.SERVER_NAME)
                clanJoin.setUsername(player.name)
                publish(clanJoin.build())
            }

            closeOverlay(player)
            player.inventory.clear()
            player.bank.clear()
            player.equipment.clear()
            player.inventory.add(*TutorialStage.STARTER_PACK)
            player.bank.add(TutorialStage.STARTER_BANK)
            player.interfaceManager.restoreTabs()
            player.interfaceManager.setViewedTab(3)
            player.interfaceManager.openDefaultTabs()
            player.dialogueInterpreter.sendDialogues(
                "Welcome to Lumbridge! To get more help, simply click on the",
                "Lumbridge Guide or one of the Tutors - these can be found by",
                "looking for the question mark icon on your minimap. If you find you",
                "are lost at any time, look for a signpost or use the Lumbridge Home",
                "Teleport spell."
            )
            setAttribute(player, "close_c_", true)
        }

    }
}
