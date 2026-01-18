package content.global.skill.gather.fishing

import content.data.GameAttributes
import content.global.plugins.item.equipment.fog_gloves.FOGGlovesManager
import content.global.skill.summoning.familiar.Forager
import content.region.kandarin.baxtorian.BarbarianTraining
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs

class FishingListener : InteractionListener {

    override fun defineListeners() {
        val spotIds = FishingSpot.values().flatMap { it.ids.toList() }.toIntArray()

        defineInteraction(
            IntType.NPC,
            spotIds,
            "net",
            "lure",
            "bait",
            "harpoon",
            "cage",
            "fish",
            persistent = true,
            allowedDistance = 1,
            handler = ::handleFishing
        )
    }

    private fun handleFishing(player: Player, node: Node, state: Int): Boolean {
        val npc = node as? NPC ?: return clearScripts(player)
        val spot = FishingSpot.forId(npc.id) ?: return clearScripts(player)
        val option = spot.getOptionByName(getUsedOption(player)) ?: return clearScripts(player)

        val forager = player.familiarManager.familiar as? Forager

        if (!finishedMoving(player))
            return restartScript(player)

        if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
            if (!inInventory(player, Items.SMALL_FISHING_NET_303) &&
                npc.id == NPCs.TUTORIAL_FISHING_SPOT_952
            ) {
                sendNPCDialogue(
                    player,
                    NPCs.SURVIVAL_EXPERT_943,
                    "Hang on a minute! Let's first make sure you know how to make a fire to cook those."
                )
                return clearScripts(player)
            }
        }

        if (state == 0) {
            if (!checkRequirements(player, option, node))
                return clearScripts(player)

            forager?.let {
                Pathfinder.find(it, player.location.transform(player.direction)).walk(it)
            }

            sendStartMessage(player, spot, option)
        }

        if (clockReady(player, Clocks.SKILLING)) {

            playFishingAnimation(player, option)
            forager?.handlePassiveAction()

            val fish = option.rollFish(player)
            if (fish == null) {
                delayClock(player, Clocks.SKILLING, 5)
                return keepRunning(player)
            }

            if (!hasSpaceFor(player, fish.item) || !option.removeBait(player)) {
                return clearScripts(player)
            }

            giveFishReward(player, fish, option, node)

            delayClock(player, Clocks.SKILLING, 5)

            if (!checkRequirements(player, option, node))
                return clearScripts(player)
        }

        return keepRunning(player)
    }

    private fun sendStartMessage(player: Player, spot: FishingSpot, option: FishingOption) {
        if (isBarehandEnabled(player) && option.isHarpoonType()) {
            sendMessage(player, "You start to lure the fish.")
            return
        }

        when (option.optionName) {
            "cage" ->
                sendMessage(
                    player,
                    if (spot.name == "CageHarpoon")
                        "You attempt to catch a lobster."
                    else
                        "You attempt to catch a crayfish."
                )

            "harpoon" -> sendMessage(player, "You start harpooning fish.")
            "net" -> sendMessage(player, "You cast out your net...")
            "bait", "lure" -> {
                sendMessage(player, "You cast out your line...")
                sendMessage(player, "You attempt to catch a fish.")
            }

            else -> sendMessage(player, "You attempt to catch some fish...")
        }
    }

    private fun playFishingAnimation(player: Player, option: FishingOption) {
        val animationId =
            if (isBarehandEnabled(player) && option.isHarpoonType())
                Animations.BAREHAND_FISHING_6709
            else
                option.animationId

        player.animate(Animation(animationId))
    }

    private fun giveFishReward(
        player: Player,
        fish: Fish,
        option: FishingOption,
        node: Node
    ) {
        val item = fish.item
        var xp = fish.xp

        val bigFishId = Fish.getBigFish(fish)
        val chance = if (GameWorld.settings?.isDevMode == true) 10 else 5000

        if (bigFishId != null && RandomFunction.roll(chance)) {
            sendMessage(
                player,
                "You catch an enormous ${getItemName(fish.id).lowercase().replace("raw ", "")}!"
            )
            addItemOrDrop(player, bigFishId, 1)
        } else {
            val name = getItemName(fish.id)
                .lowercase()
                .replace("raw ", "")
                .replace("big ", "")

            sendMessage(player, "You catch a $name.")
            addItemOrDrop(player, item.id, item.amount)
        }

        // Barehand
        if (isBarehandEnabled(player) && option.isHarpoonType()) {
            val strXp = when (fish) {
                Fish.TUNA -> 8.0
                Fish.SWORDFISH -> 10.0
                Fish.SHARK -> 11.0
                else -> 0.0
            }
            rewardXP(player, Skills.STRENGTH, strXp)
        }

        // Gloves bonus
        if ((item.id == Items.RAW_SWORDFISH_371 && inEquipment(player, Items.SWORDFISH_GLOVES_12860)) ||
            (item.id == Items.RAW_SHARK_383 && inEquipment(player, Items.SHARK_GLOVES_12861))
        ) {
            xp += 100
            FOGGlovesManager.updateCharges(player)
        }

        if (isBarehandEnabled(player) &&
            option.isHarpoonType() &&
            !getAttribute(player, BarbarianTraining.FISHING_FULL, false)
        ) {
            sendDialogueLines(
                player,
                "You feel you have learned more of barbarian ways.",
                "Otto might wish to talk to you more."
            )
            setAttribute(player, BarbarianTraining.FISHING_FULL, true)
        }

        rewardXP(player, Skills.FISHING, xp)
        player.dispatch(ResourceProducedEvent(item.id, item.amount, node))
    }

    private fun checkRequirements(player: Player, option: FishingOption, node: Node): Boolean {
        val barehand = isBarehandEnabled(player)

        if (barehand && option.isHarpoonType()) {
            val minFishing = option.fishId.minOf {
                when (it) {
                    Fish.TUNA -> 55
                    Fish.SWORDFISH -> 70
                    Fish.SHARK -> 96
                    else -> 0
                }
            }
            val minStrength = option.fishId.minOf {
                when (it) {
                    Fish.TUNA -> 35
                    Fish.SWORDFISH -> 50
                    Fish.SHARK -> 76
                    else -> 0
                }
            }

            if (getStatLevel(player, Skills.FISHING) < minFishing ||
                getStatLevel(player, Skills.STRENGTH) < minStrength
            ) {
                sendDialogue(player, "You don't meet the requirements to barehand these fish.")
                return false
            }
        } else {
            if (!inInventory(player, option.tool) && !hasBarbTail(player, option)) {
                sendDialogue(player, "You need the proper equipment to fish here.")
                return false
            }
            if (!option.hasBait(player)) {
                sendDialogue(player, "You don't have any ${option.getBaitName().lowercase()} left.")
                return false
            }
            if (!hasLevelDyn(player, Skills.FISHING, option.requiredLevel)) {
                sendDialogue(
                    player,
                    "You need a Fishing level of at least ${option.requiredLevel} to ${option.optionName} these fish."
                )
                return false
            }
        }

        if (freeSlots(player) == 0) {
            sendDialogue(player, "You can't carry any more fish.")
            return false
        }

        return node.isActive && node.location.withinDistance(player.location, 1)
    }

    private fun hasBarbTail(player: Player, option: FishingOption): Boolean {
        val bh = FishingOption.BarbHarpoon.tool
        return (option == FishingOption.Harpoon || option == FishingOption.SharkHarpoon) &&
                (inInventory(player, bh) || inEquipment(player, bh))
    }

    private fun isBarehandEnabled(player: Player) =
        player.getAttribute(GameAttributes.BARBARIAN_BAREHAND_FISHING, false)

    private fun FishingOption.isHarpoonType() =
        this == FishingOption.Harpoon || this == FishingOption.SharkHarpoon
}
