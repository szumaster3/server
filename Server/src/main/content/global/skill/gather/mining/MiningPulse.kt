package content.global.skill.gather.mining

import content.data.skill.SkillingTool
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.Node
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.drop.DropFrequency
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.ChanceItem
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import core.tools.prependArticle
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Scenery as Objects

/**
 * Represents pulse that used to handle mining interaction.
 */
class MiningPulse(private val player: Player, private val node: Node) : Pulse(1, player, node) {

    private var resource: MiningNode? = null
    private var ticks = 0
    private var resetAnimation = true

    private var isMiningEssence = false
    private var isMiningGems = false
    private var isMiningGranite = false
    private var isMiningSandstone = false
    private var isMiningMagicStone = false
    private var isMiningObsidian = false

    private val perfectGoldOreLocations = listOf(
        Location(2735, 9695, 0),
        Location(2737, 9689, 0),
        Location(2740, 9684, 0),
        Location(2737, 9683, 0)
    )

    /**
     * Sends mining base message based on the type of resource.
     */
    private fun sendMiningMessage(type: Int) {
        if (type == 0) {
            val msg = if (isMiningObsidian) "You swing your pick at the wall."
            else "You swing your pickaxe at the rock."
            sendMessage(player, msg)
        }
    }

    override fun start() {
        resource = MiningNode.forId(node.id) ?: return

        if (MiningNode.isEmpty(node.id)) {
            sendMessage(player, "This rock contains no ore.")
        }

        if (resource!!.id == Objects.ROCKS_2099 && node.location !in perfectGoldOreLocations) {
            resource = MiningNode.forId(shared.consts.Scenery.ROCKS_2098)
        }

        isMiningEssence    = resource!!.id in listOf(Objects.RUNE_ESSENCE_2491, Objects.ROCK_16684)
        isMiningGems       = resource!!.identifier == MiningNode.GEM_ROCK_0.identifier
        isMiningSandstone  = resource!!.identifier == MiningNode.SANDSTONE.identifier
        isMiningGranite    = resource!!.identifier == MiningNode.GRANITE.identifier
        isMiningMagicStone = resource!!.identifier == MiningNode.MAGIC_STONE_0.identifier
        isMiningObsidian   = resource!!.identifier == MiningNode.OBSIDIAN_0.identifier

        if (checkRequirements()) {
            super.start()
            sendMiningMessage(0)
        }
    }

    override fun pulse(): Boolean {
        if (!checkRequirements()) return true
        playAnimation()
        return handleReward()
    }

    override fun stop() {
        if (resetAnimation) {
            animate(player, Animation(-1, Animator.Priority.HIGH))
        }
        super.stop()
        sendMiningMessage(1)
    }

    /**
     * Checks whether the player meets all requirements to mine this resource.
     */
    private fun checkRequirements(): Boolean {
        if (getDynLevel(player, Skills.MINING) < resource!!.level) {
            sendMessage(player, "You need a Mining level of ${resource!!.level} to mine this rock.")
            return false
        }

        val allPickaxes = SkillingTool.values().filter {
            inEquipmentOrInventory(player, it.id)
        }

        if (allPickaxes.isEmpty()) {
            sendMessage(player, "You do not have a pickaxe to use.")
            return false
        }

        if (player.getSkills().getLevel(Skills.MINING) < resource!!.level) {
            sendMessage(player, "You need a Mining level of ${resource!!.level} to mine this rock.")
            return false
        }

        val usablePickaxe = allPickaxes
            .filter { player.getSkills().getLevel(Skills.MINING) >= it.level }
            .maxByOrNull { it.level }

        if (usablePickaxe == null) {
            sendMessage(player, "You need a pickaxe to mine this rock. You do not have a pickaxe which you have the Mining level to use.")
            return false
        }

        if (resource!!.identifier == 19.toByte() && !hasRequirement(player, Quests.TOKTZ_KET_DILL, false)) {
            sendDialogue(player, "You do not know the technique to mine stone slabs.")
            return false
        }

        if (resource!!.identifier == 19.toByte() && usablePickaxe in listOf(SkillingTool.INFERNO_ADZE, SkillingTool.INFERNO_ADZE2)) {
            sendDialogue(player, "I don't think I should use the Inferno Adze in here.")
            return false
        }

        if (freeSlots(player) == 0) {
            val prefix = "Your inventory is too full to hold any more"
            val messages = mapOf(
                4.toByte()  to "$prefix limestone.",
                13.toByte() to "$prefix gems.",
                14.toByte() to "$prefix essence.",
                15.toByte() to "$prefix sandstone.",
                16.toByte() to "$prefix granite.",
                19.toByte() to "$prefix obsidian."
            )
            messages[resource!!.identifier]?.let {
                sendDialogue(player, it)
                return false
            }

            if (resource!!.identifier == 18.toByte() && inInventory(player, Items.MAGIC_STONE_4703)) {
                sendMessage(player, "You have already mined some stone. You don't need any more.")
                return false
            }

            val resourceReward = getItemName(resource!!.reward).lowercase()
            sendDialogue(player, "Your inventory is too full to hold any more $resourceReward.")
            return false
        }

        return true
    }

    /**
     * Plays the mining animation for the current resource.
     */
    private fun playAnimation() {
        val pickaxe = SkillingTool.getPickaxe(player) ?: return
        val anim = when {
            isMiningEssence  -> pickaxe.animation + 6128
            isMiningObsidian -> pickaxe.animation + 9718
            else -> pickaxe.animation
        }
        animate(player, anim)
    }

    /**
     * Handles reward logic.
     */
    private fun handleReward(): Boolean {
        if (!checkReward()) return false
        if (++ticks % (if (isMiningEssence) 1 else 4) != 0) return false

        // Calculate the reward type and amount.
        var reward = calculateReward(resource!!.reward)
        val rewardAmount = calculateRewardAmount(reward)
        player.dispatch(ResourceProducedEvent(reward, rewardAmount, node))

        // Give xp for mined resource.
        rewardXP(player, Skills.MINING, resource!!.experience * rewardAmount)
        // Handle bracelet of clay effect if the mined resource is clay.
        handleBraceletOfClay(reward)
        // Send a message.
        sendRewardMessage(reward)
        // Add the reward.
        addItemOrDrop(player, reward, rewardAmount)
        // Handle chance to find a gem while mining.
        handleGemChance()
        // Handle resource respawn.
        handleRespawn()
        return true
    }

    private fun handleBraceletOfClay(reward: Int) {
        if (reward != Items.CLAY_434) return
        val bracelet = getItemFromEquipment(player, EquipmentSlot.HANDS)
        if (bracelet?.id == Items.BRACELET_OF_CLAY_11074) {
            var charges = player.getAttribute("jewellery-charges:bracelet-of-clay", 28)
            charges--
            sendMessage(player, "Your bracelet of clay softens the clay for you.")
            if (charges <= 0 && removeItem(player, bracelet, Container.EQUIPMENT)) {
                sendMessage(player, "Your bracelet of clay crumbles to dust.")
                charges = 28
            }
            setAttribute(player, "/save:jewellery-charges:bracelet-of-clay", charges)
        }
    }

    private fun sendRewardMessage(reward: Int) {
        val rewardName = getItemName(reward).lowercase()
        val msg = when {
            isMiningGems       -> "You get ${prependArticle(rewardName)}."
            isMiningGranite    -> "You manage to quarry some granite."
            isMiningSandstone  -> "You manage to quarry some sandstone."
            isMiningMagicStone -> "You manage to mine some stone."
            isMiningObsidian   -> "You manage to mine some obsidian."
            else -> "You manage to get some $rewardName."
        }
        sendMessage(player, msg)
    }

    private fun handleGemChance() {
        if (isMiningEssence) return
        var chance = 282
        val ring = getItemFromEquipment(player, EquipmentSlot.RING)
        if (ring?.id == Items.RING_OF_WEALTH_2572) chance = (chance / 1.5).toInt()
        val necklace = getItemFromEquipment(player, EquipmentSlot.NECK)
        if (necklace?.id in Items.AMULET_OF_GLORY_1705..Items.AMULET_OF_GLORY4_1713) chance = (chance / 1.5).toInt()
        if (RandomFunction.roll(chance)) {
            val gem = GEM_REWARDS.random()
            sendMessage(player, "You find a ${gem.name}!")
            if (freeSlots(player) == 0) sendMessage(player, "You do not have enough space, gem dropped on the floor.")
            addItemOrDrop(player, gem.id)
        }
    }

    private fun handleRespawn() {
        if (resource!!.respawnRate == 0 || isMiningEssence) return

        when (resource!!.id) {
            Objects.PILE_OF_ROCK_4030 -> {
                removeScenery(node as Scenery)
                GameWorld.Pulser.submit(object : Pulse(resource!!.respawnDuration, player) {
                    override fun pulse(): Boolean {
                        SceneryBuilder.add(Scenery(Objects.PILE_OF_ROCK_4027, node.location))
                        return true
                    }
                })
                node.setActive(false)
            }
            Objects.OBSIDIAN_WALL_31229 -> {
                SceneryBuilder.replaceWithTempBeforeNew(
                    node.asScenery(),
                    node.asScenery().transform(Objects.OBSIDIAN_WALL_31230),
                    node.asScenery().transform(Objects.OBSIDIAN_WALL_9376),
                    resource!!.respawnDuration,
                    true
                )
            }
            else -> {
                SceneryBuilder.replace(
                    node as Scenery,
                    Scenery(resource!!.emptyId, node.getLocation(), node.type, node.rotation),
                    resource!!.respawnDuration
                )
                node.setActive(false)
            }
        }
    }

    private fun calculateRewardAmount(reward: Int): Int {
        var amount = 1
        if (!isMiningEssence) {
            val diary = player.achievementDiaryManager.getDiary(DiaryType.VARROCK)
            if (diary != null && diary.level != -1) {
                amount += diaryBonus(reward)
            }
            if (player.hasActiveState("shooting-star") && RandomFunction.getRandom(5) == 3) {
                sendMessage(player, "...you manage to mine a second ore thanks to the Star Sprite.")
                amount += 1
            }
        }
        return amount
    }

    /**
     * Handles varrock armour diary bonus.
     */
    private fun diaryBonus(reward: Int): Int {
        var bonus = 0
        val varrockArmourMessage = "The Varrock armour allows you to mine an additional ore."
        when (reward) {
            Items.CLAY_434, Items.COPPER_ORE_436, Items.TIN_ORE_438, Items.LIMESTONE_3211,
            Items.BLURITE_ORE_668, Items.IRON_ORE_440, Items.ELEMENTAL_ORE_2892, Items.SILVER_ORE_442, Items.COAL_453 ->
                if (player.achievementDiaryManager.armour >= 0 && RandomFunction.random(100) <= 4) {
                    bonus += 1
                    sendMessage(player, varrockArmourMessage)
                }

            Items.GOLD_ORE_444, Items.GRANITE_500G_6979, Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983, Items.MITHRIL_ORE_447 ->
                if (player.achievementDiaryManager.armour >= 1 && RandomFunction.random(100) <= 3) {
                    bonus += 1
                    sendMessage(player, varrockArmourMessage)
                }

            Items.ADAMANTITE_ORE_449 ->
                if (player.achievementDiaryManager.armour >= 2 && RandomFunction.random(100) <= 2) {
                    bonus += 1
                    sendMessage(player, varrockArmourMessage)
                }
        }
        return bonus
    }

    private fun calculateReward(reward: Int): Int {
        var result = reward
        when {
            resource == MiningNode.SANDSTONE || resource == MiningNode.GRANITE -> {
                val value = RandomFunction.randomize(if (resource == MiningNode.GRANITE) 3 else 4)
                result += value shl 1
                rewardXP(player, Skills.MINING, value * 10.0)
            }
            isMiningEssence && getDynLevel(player, Skills.MINING) >= 30 -> result = Items.PURE_ESSENCE_7936
            isMiningGems -> result = RandomFunction.rollWeightedChanceTable(MiningNode.GEM_ROCK_REWARD).id
        }
        return result
    }

    private fun checkReward(): Boolean {
        val level = 1 + getDynLevel(player, Skills.MINING) + getFamiliarBoost(player, Skills.MINING)
        val hostRatio = Math.random() * (100.0 * resource!!.rate)
        val toolRatio = SkillingTool.getPickaxe(player)!!.ratio
        val clientRatio = Math.random() * ((level - resource!!.level) * (1.0 + toolRatio))
        return hostRatio < clientRatio
    }

    companion object {
        private val GEM_REWARDS = arrayOf(
            ChanceItem(Items.UNCUT_SAPPHIRE_1623, 1, DropFrequency.COMMON),
            ChanceItem(Items.UNCUT_EMERALD_1621,  1, DropFrequency.COMMON),
            ChanceItem(Items.UNCUT_RUBY_1619,     1, DropFrequency.UNCOMMON),
            ChanceItem(Items.UNCUT_DIAMOND_1617,  1, DropFrequency.RARE)
        )
    }

    init {
        super.stop()
    }
}