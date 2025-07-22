package content.region.kandarin.plugin.barbtraining.plugin

import content.region.kandarin.plugin.barbtraining.BarbarianTraining
import content.region.kandarin.plugin.barbtraining.plugin.BarbFishSpotManager.Companion.getNewLoc
import content.region.kandarin.plugin.barbtraining.plugin.BarbFishSpotManager.Companion.getNewTTL
import content.region.kandarin.plugin.barbtraining.plugin.BarbFishSpotManager.Companion.usedLocations
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class BarbFishingPlugin : InteractionListener {
    private val fishCuttingIds = intArrayOf(
        Items.LEAPING_TROUT_11328,
        Items.LEAPING_SALMON_11330,
        Items.LEAPING_STURGEON_11332
    )
    private val barbFishingSpot = NPCs.FISHING_SPOT_1176
    private val barbarianBed = Scenery.BARBARIAN_BED_25268
    private val barbarianRod = Items.BARBARIAN_ROD_11323

    override fun defineListeners() {
        on(barbarianBed, IntType.SCENERY, "search") { player, _ ->
            if (getAttribute(player, BarbarianTraining.FISHING_BASE, false)) {
                if (!inInventory(player, barbarianRod) && freeSlots(player) > 0) {
                    sendMessage(player, "You find a heavy fishing rod under the bed and take it.")
                    addItem(player, barbarianRod)
                } else {
                    sendMessage(player, "You don't find anything that interests you.")
                }
            } else {
                sendMessage(player, "You don't find anything that interests you.")
            }
            return@on true
        }

        on(barbFishingSpot, IntType.NPC, "fish") { player, _ ->
            if (!getAttribute(player, BarbarianTraining.FISHING_START, false)) {
                sendMessage(player, "You must begin the relevant section of Otto Godblessed's barbarian training.")
                return@on true
            }

            sendMessage(player, "You cast out your line...")
            submitIndividualPulse(player, BarbFishingPulse(player))
            return@on true
        }

        onUseWith(IntType.ITEM, Items.KNIFE_946, *fishCuttingIds) { player, _, with ->
            val slots = freeSlots(player)
            val hasOffcuts = inInventory(player, Items.FISH_OFFCUTS_11334)
            if (slots < 2 && (slots < 1 || !hasOffcuts)) {
                sendMessage(player, "You don't have enough space in your pack to attempt cutting open the fish.")
                return@onUseWith false
            }
            submitIndividualPulse(player, BarbFishCuttingPulse(player, with.id))
            return@onUseWith true
        }

    }

}

private class BarbFishingPulse(player: Player) : SkillPulse<NPC>(player, NPC(NPCs.FISHING_SPOT_1176)) {

    private val fishingBait = anyInInventory(
        player,
        Items.FISHING_BAIT_313,
        Items.FEATHER_314,
        Items.ROE_11324,
        Items.FISH_OFFCUTS_11334,
        Items.CAVIAR_11326,
    )

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.FISHING) < 48) {
            sendMessage(player, "You need a fishing level of at least 48 to fish here.")
            return false
        }
        if (getStatLevel(player, Skills.AGILITY) < 15 || getStatLevel(player, Skills.STRENGTH) < 15) {
            player.sendMessages(
                "You need a ",
                if (getStatLevel(player, Skills.AGILITY) < 15 && getStatLevel(
                        player,
                        Skills.STRENGTH,
                    ) < 15
                ) {
                    "agility and strength"
                } else if (getStatLevel(player, Skills.AGILITY) < 15) {
                    "agility"
                } else {
                    "strength"
                },
                " level of at least 15 to fish here.",
            )
            return false
        }
        if (!inInventory(player, Items.BARBARIAN_ROD_11323)) {
            sendMessage(player, "You need a barbarian fishing rod to fish here.")
            return false
        }

        if (player.inventory.isFull) {
            sendMessage(player, "You don't have enough space in your inventory.")
            return false
        }

        if (!fishingBait) {
            sendMessage(player, "You don't have any bait with which to fish.")
            return false
        }
        return true
    }

    override fun animate() {
        animate(player, Animations.ROD_FISHING_622, true)
    }

    override fun reward(): Boolean {
        val stragiXP = arrayOf(5, 6, 7)
        val fishXP = arrayOf(50, 70, 80)
        val reward = getRandomFish()
        val success = rollSuccess(
            when (reward.id) {
                Items.LEAPING_TROUT_11328 -> 48
                Items.LEAPING_SALMON_11330 -> 58
                Items.LEAPING_STURGEON_11332 -> 70
                else -> 99
            },
        )
        val index = (when (reward.id) {
            Items.LEAPING_TROUT_11328 -> 0
            Items.LEAPING_SALMON_11330 -> 1
            Items.LEAPING_STURGEON_11332 -> 2
            else -> 0
        })
        sendMessage(player, "You attempt to catch fish.")
        if (success) {
            if (!removeItem(player, Items.FISH_OFFCUTS_11334)) {
                removeItem(player, Items.FEATHER_314)
            }
            player.inventory.add(reward)
            rewardXP(player, Skills.FISHING, fishXP[index].toDouble())
            rewardXP(player, Skills.AGILITY, stragiXP[index].toDouble())
            rewardXP(player, Skills.STRENGTH, stragiXP[index].toDouble())
            sendMessage(player, "You catch a ${reward.name.lowercase()}.")

            if (getAttribute(player, BarbarianTraining.FISHING_BASE, false)) {
                removeAttribute(player, BarbarianTraining.FISHING_BASE)
                setAttribute(player, BarbarianTraining.FISHING_FULL, true)
                sendDialogueLines(
                    player,
                    "You feel you have learned more of barbarian ways. Otto might wish",
                    "to talk to you more.",
                )
            }
        }
        super.setDelay(5)
        return player.inventory.freeSlots() == 0
    }

    private fun rollSuccess(fish: Int): Boolean {
        val level = 1 + player.skills.getLevel(Skills.FISHING) + player.familiarManager.getBoost(Skills.FISHING)
        val hostRatio: Double = Math.random() * fish
        val clientRatio: Double = Math.random() * (level * 3.0 - fish)
        return hostRatio < clientRatio
    }

    private fun getRandomFish(): Item {
        val fish = arrayOf(Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330, Items.LEAPING_STURGEON_11332)
        val fishing = player.skills.getLevel(Skills.FISHING)
        val strength = player.skills.getLevel(Skills.STRENGTH)
        val agility = player.skills.getLevel(Skills.AGILITY)

        var possibleIndex = 0
        if (fishing >= 58 && (strength >= 30 && agility >= 30)) possibleIndex++
        if (fishing >= 70 && (strength >= 45 && agility >= 45)) possibleIndex++
        return Item(fish[RandomFunction.random(possibleIndex + 1)])
    }
}

private class BarbFishCuttingPulse(val player: Player, val fish: Int) : Pulse(0) {

    override fun pulse(): Boolean {
        player.animator.animate(Animation(Animations.CRAFT_KNIFE_5244))
        player.inventory.remove(Item(fish))
        player.inventory.add(Item(Items.FISH_OFFCUTS_11334))
        player.inventory.add(
            Item(
                when (fish) {
                    Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330 -> Items.ROE_11324
                    Items.LEAPING_STURGEON_11332 -> Items.CAVIAR_11326
                    else -> 0
                },
            ),
        )

        player.skills.addExperience(
            Skills.COOKING,
            when (fish) {
                Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330 -> 10.0
                Items.LEAPING_STURGEON_11332 -> 15.0
                else -> 0.0
            },
        )

        sendMessage(player, "You cut open the fish and extract some roe, but the rest of the fish is reduced to")
        sendMessage(player, "useless fragments, which you discard.")
        return true
    }
}