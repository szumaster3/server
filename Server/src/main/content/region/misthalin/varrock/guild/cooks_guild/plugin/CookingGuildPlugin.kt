package content.region.misthalin.varrock.guild.cooks_guild.plugin

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class CookingGuildPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles opening the Cook's Guild doors.
         */

        on(intArrayOf(Scenery.DOOR_2712, Scenery.DOOR_26810), IntType.SCENERY, "open") { player, node ->
            val requiredItems = anyInEquipment(
                player,
                Items.CHEFS_HAT_1949,
                Items.COOKING_CAPE_9801,
                Items.COOKING_CAPET_9802,
                Items.VARROCK_ARMOUR_3_11758,
            )

            when (node.id) {
                26810 -> {
                    val hasVarrockArmour = inEquipment(player, Items.VARROCK_ARMOUR_3_11758)
                    val cookingLevel = getStatLevel(player, Skills.COOKING)
                    val isMembers = GameWorld.settings?.isMembers ?: true

                    if (player.location.x <= 3143 && (!hasVarrockArmour || cookingLevel < 99)) {
                        if (!isMembers) {
                            sendNPCDialogue(player, NPCs.HEAD_CHEF_847, "The bank's closed. You just can't get the staff these days.")
                        } else {
                            sendNPCDialogue(player, NPCs.HEAD_CHEF_847, "You need to have completed the hard Varrock diary and have 99 Cooking to enter.")
                        }
                    } else {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    }
                }

                2712 -> {
                    val cookingLevel = getStatLevel(player, Skills.COOKING)
                    val hasVarrockArmour = inEquipment(player, Items.VARROCK_ARMOUR_3_11758)
                    val yPos = player.location.y

                    when {
                        cookingLevel < 32 -> {
                            sendNPCDialogue(player, NPCs.HEAD_CHEF_847,
                                if (requiredItems)
                                    "Sorry. Only the finest chefs are allowed in here. Get your cooking level up to 32."
                                else
                                    "Sorry. Only the finest chefs are allowed in here. Get your cooking level up to 32 and come back wearing a chef's hat."
                            )
                        }

                        !requiredItems && yPos <= 3443 -> {
                            sendNPCDialogueLines(
                                player,
                                NPCs.HEAD_CHEF_847,
                                FaceAnim.NEUTRAL,
                                false,
                                "You can't come in here unless you're wearing a chef's",
                                "hat, or something like that."
                            )
                        }

                        else -> {
                            if (hasVarrockArmour) {
                                sendNPCDialogue(player, NPCs.HEAD_CHEF_847, "My word! A master explorer of Varrock! Come in, come in! You are more than welcome in here, my friend!")
                            }
                            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        }
                    }
                }
            }

            return@on true
        }

    }
}