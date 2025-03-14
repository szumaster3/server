package content.global.handlers.iface

import core.api.sendMessage
import core.game.interaction.InterfaceListener
import org.rs.consts.Components

class SummoningTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.LORE_STATS_SIDE_662) { player, _, opcode, buttonID, _, _ ->
            when (buttonID) {
                51 -> {
                    if (player.familiarManager.hasFamiliar()) {
                        player.familiarManager.familiar.call()
                    } else {
                        sendMessage(player, "You don't have a follower.")
                    }
                }

                67 -> {
                    if (player.familiarManager.hasFamiliar()) {
                        if (player.familiarManager.familiar.isInvisible ||
                            !player.familiarManager.familiar.location.withinDistance(
                                player.location,
                            )
                        ) {
                            sendMessage(
                                player,
                                "Your familiar is too far away to use that Scroll, or it cannot see you.",
                            )
                            return@on true
                        }
                        if (!player.familiarManager.familiar.isBurdenBeast) {
                            sendMessage(player, "Your familiar is not a beast of burden.")
                            return@on true
                        }
                        val beast =
                            player.familiarManager.familiar as content.global.skill.summoning.familiar.BurdenBeast
                        if (beast.container.isEmpty()) {
                            sendMessage(player, "Your familiar is not carrying any items.")
                            return@on true
                        }
                        beast.withdrawAll()
                        return@on true
                    }
                    sendMessage(player, "You don't have a follower.")
                }

                53 -> {
                    if (player.familiarManager.hasFamiliar()) {
                        if (opcode == 155) {
                            player.dialogueInterpreter.open("dismiss_dial")
                        } else if (opcode == 196) {
                            if (player.familiarManager.familiar is content.global.skill.summoning.pet.Pet) {
                                val pet = player.familiarManager.familiar as content.global.skill.summoning.pet.Pet
                                player.familiarManager.removeDetails(pet.getItemIdHash())
                            }
                            player.familiarManager.dismiss()
                        }
                    } else {
                        sendMessage(player, "You don't have a follower.")
                    }
                }

                else -> {
                    if (player.familiarManager.hasFamiliar()) {
                        player.familiarManager.familiar.executeSpecialMove(
                            content.global.skill.summoning.familiar
                                .FamiliarSpecial(player),
                        )
                    } else {
                        sendMessage(player, "You don't have a follower.")
                    }
                }
            }
            return@on true
        }
    }
}
