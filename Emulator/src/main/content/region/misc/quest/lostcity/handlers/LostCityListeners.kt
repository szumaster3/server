package content.region.misc.quest.lostcity.handlers

import content.data.items.SkillingTool
import content.global.skill.gathering.woodcutting.WoodcuttingPulse
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LostCityListeners : InteractionListener {
    init {
        SHAMUS.init()
        SHAMUS.isWalks = true
        SHAMUS.isInvisible = true
    }

    companion object {
        val SHAMUS = NPC(NPCs.SHAMUS_654, Location(3138, 3211, 0))

        fun disappearShamus() {
            SHAMUS.isInvisible = true
        }
    }

    private fun handleShamusTree(player: Player): Boolean {
        if (SkillingTool.getHatchet(player) == null) {
            sendMessage(player, "You do not have an axe which you have the level to use.")
            return true
        }
        showShamus(player)
        return true
    }

    private fun showShamus(player: Player) {
        if (SHAMUS.isInvisible) {
            SHAMUS.isInvisible = false
            SHAMUS.properties.teleportLocation = SHAMUS.properties.spawnLocation
        }
        sendNPCDialogue(
            player,
            NPCs.SHAMUS_654,
            "Hey! Yer big elephant! Don't go choppin' down me house, now!",
            FaceAnim.OLD_EVIL1,
        )
        GameWorld.Pulser.submit(
            object : Pulse(100) {
                override fun pulse(): Boolean {
                    if (SHAMUS.dialoguePlayer == null) {
                        SHAMUS.isInvisible = true
                        return true
                    }
                    return false
                }
            },
        )
    }

    override fun defineListeners() {
        on(org.rs.consts.Scenery.TREE_2409, IntType.SCENERY, "chop") { player, _ ->
            if (SkillingTool.getHatchet(player) == null) {
                sendMessage(player, "You do not have an axe which you have the level to use.")
                return@on true
            }
            showShamus(player)
            return@on true
        }

        on(org.rs.consts.Scenery.DOOR_2406, IntType.SCENERY, "open") { player, node ->
            DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
            val quest = Quests.LOST_CITY
            val isOutsideShed = player.location.x < node.location.x
            val hasRequirements =
                (
                    inEquipment(player, Items.DRAMEN_STAFF_772) ||
                        inEquipment(
                            player,
                            Items.LUNAR_STAFF_9084,
                        )
                ) &&
                    (getQuestStage(player, quest) > 20) &&
                    isOutsideShed
            if (hasRequirements) {
                var count = 0
                submitWorldPulse(
                    object : Pulse(2) {
                        override fun pulse(): Boolean {
                            when (count++) {
                                0 -> {
                                    sendMessageWithDelay(player, "The world starts to shimmer...", 1)
                                    teleport(player, Location(2452, 4473, 0), TeleportType.FAIRY_RING)
                                }

                                1 -> return isQuestComplete(player, quest)
                                2 -> {
                                    finishQuest(player, quest)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
            return@on true
        }

        on(org.rs.consts.Scenery.DRAMEN_TREE_1292, IntType.SCENERY, "chop down") { player, node ->
            val questStage = getQuestStage(player, Quests.LOST_CITY)
            if (SkillingTool.getHatchet(player) == null) {
                sendMessage(player, "You do not have an axe which you have the level to use.")
                return@on true
            }
            if (questStage < 20) {
                return@on true
            }
            if (questStage == 20) {
                if (player.getAttribute("treeSpawned", false)) {
                    return@on true
                }
                val spirit = TreeSpiritNPC(NPCs.TREE_SPIRIT_655, Location(2862, 9734, 0))
                spirit.target = player
                spirit.init()
                spirit.attack(player)
                setAttribute(player, "treeSpawned", true)
                spirit.sendChat("You must defeat me before touching the tree!")
                return@on true
            }

            player.pulseManager.run(WoodcuttingPulse(player, node as Scenery))
            return@on true
        }

        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.DRAMEN_BRANCH_771) { player, _, _ ->
            if (getStatLevel(player, Skills.CRAFTING) < 31) {
                sendDialogue(player, "You need a crafting level of 31 to do this.")
                return@onUseWith false
            }

            if (removeItem(player, Item(Items.DRAMEN_BRANCH_771, 1), Container.INVENTORY)) {
                sendMessage(player, "You carve the branch into a staff.")
                addItem(player, Items.DRAMEN_STAFF_772, 1, Container.INVENTORY)
            }
            return@onUseWith true
        }
    }
}
