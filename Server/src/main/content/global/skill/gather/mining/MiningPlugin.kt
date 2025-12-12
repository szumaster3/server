package content.global.skill.gather.mining

import content.data.skill.SkillingTool
import content.global.activity.star.ShootingStarBonus
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.npc.drop.DropFrequency
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.ChanceItem
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import core.tools.colorize
import core.tools.prependArticle
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Scenery as Objects

/**
 * Handles mining interactions.
 * @author Ceikry
 * @author Bushtail (maintenance July 2022)
 */
class MiningPlugin : InteractionListener {

    private val gemRewards = arrayOf(
        ChanceItem(Items.UNCUT_SAPPHIRE_1623, 1, DropFrequency.COMMON),
        ChanceItem(Items.UNCUT_EMERALD_1621,  1, DropFrequency.COMMON),
        ChanceItem(Items.UNCUT_RUBY_1619,     1, DropFrequency.UNCOMMON),
        ChanceItem(Items.UNCUT_DIAMOND_1617,  1, DropFrequency.RARE),
    )

    override fun defineListeners() {

        /*
         * Handles mining.
         */

        defineInteraction(
            IntType.SCENERY,
            MiningNode.values().map { it.id }.toIntArray(),
            "mine",
            persistent = true,
            allowedDistance = 1,
            handler = ::handleMining
        )

        /*
         * Handles prospecting the resources.
         */

        on(IntType.SCENERY, "prospect") { player, node ->
            val rock = MiningNode.forId(node.asScenery().id)
            if (rock == null) {
                sendMessage(player, "There is no ore currently available in this rock.")
                return@on true
            }

            sendMessage(player, "You examine the rock for ores...")

            val messages = mapOf(
                13.toByte() to "This rock contains gems.",
                15.toByte() to "This rock is sandstone.",
                16.toByte() to "This rock is granite.",
                18.toByte() to "This rock contains a magical kind of stone.",
                19.toByte() to "This rock contains obsidian."
            )

            val message = messages[rock.identifier] ?: "This rock contains ${getItemName(rock.reward).lowercase()}."

            queueScript(player, 3, QueueStrength.SOFT) {
                sendMessage(player, message)
                stopExecuting(player)
            }

            return@on true
        }
    }

    /*
     Essence
     * Animations.BRONZE_PICKAXE_6753
     * Animations.IRON_PICKAXE_6754
     * Animations.STEEL_PICKAXE_ALT3_6755
     * Animations.ADAMANT_PICKAXE_6756
     * Animations.MITHRIL_PICKAXE_6757
     * Animations.DRAGON_PICKAXE_6758
     * Animations.INFERNO_ADZE_10223

     Obsidian
     * Animations.BRONZE_PICKAXE_10343
     * Animations.IRON_PICKAXE_10344
     * Animations.STEEL_PICKAXE_ALT_10345
     * Animations.MITHRIL_PICKAXE_10346
     * Animations.ADAMANT_PICKAXE_10347
     * Animations.RUNE_PICKAXE_10342

    private fun destroyPickaxe(player: Player, pickaxe: SkillingTool) {
        val radius = 2
        val randomOffsetX = RandomFunction.random(-radius, radius)
        val randomOffsetY = RandomFunction.random(-radius, radius)
        val newLocation = player.location.transform(randomOffsetX, randomOffsetY, 0)

        val path = Pathfinder.find(player.location, newLocation)
        val destination = path.points.last()

        val headSpawn = Location(destination.x, destination.y, player.location.z)

        val pickHead = when (PickaxeHead.getProduct()) {
            Items.BRONZE_PICKAXE_1265.asItem() -> Items.BRONZE_PICK_HEAD_480
            Items.IRON_PICKAXE_1267.asItem() -> Items.IRON_PICK_HEAD_482
            Items.STEEL_PICKAXE_1269.asItem() -> Items.STEEL_PICK_HEAD_484
            Items.MITHRIL_PICKAXE_1273.asItem() -> Items.MITHRIL_PICK_HEAD_486
            Items.ADAMANT_PICKAXE_1271.asItem() -> Items.ADAMANT_PICK_HEAD_488
            Items.RUNE_PICKAXE_1275.asItem() -> Items.RUNE_PICK_HEAD_490
            else -> {
                log(this.javaClass, Log.ERR, "Unrecognized pickaxe for id: ${PickaxeHead.getProduct()}")
                return
            }
        }

        if (pickaxeHead != null) {
            var pickaxe = pickaxeHead.pickaxe.toInt()
            if (inEquipment(player, pickaxe)) {
                removeItem(player, pickaxe, Container.EQUIPMENT)
                addItem(player, Items.PICKAXE_HANDLE_466, pickaxe.asItem().slot, Container.EQUIPMENT)
                player.equipment.refresh()
            } else if (inInventory(player, pickaxe)) {
                replaceSlot(player, pickaxe.asItem().slot, Item(Items.PICKAXE_HANDLE_466))
                addItem(player, Items.PICKAXE_HANDLE_466)
            }
        } else {
            log(this.javaClass, Log.ERR, "Could not find PickaxeHead for id [$pickHead]")
        }
        playAudio(player, Sounds.LOST_PICKAXE_2306)
        sendMessage(player, colorize("%RThe head of your pickaxe snaps off and goes flying!"))
        GroundItemManager.create(Item(pickHead), headSpawn, player)
    }
    */

    /**
     * Handles mining a rock [node] by the [player].
     */
    private fun handleMining(player: Player, node: Node, state: Int): Boolean {
        val resource = MiningNode.forId(node.id)
        val tool = SkillingTool.getPickaxe(player)

        val isEssence = resource!!.id in intArrayOf(
            Objects.RUNE_ESSENCE_2491,
            /*
             * Lunar essence rock.
             */
            Objects.ROCK_16684
        )
        val isGems       = resource.identifier == MiningNode.GEM_ROCK_0.identifier
        val isGranite    = resource.identifier == MiningNode.GRANITE.identifier
        val isSandstone  = resource.identifier == MiningNode.SANDSTONE.identifier
        val isMagicStone = resource.identifier == MiningNode.MAGIC_STONE_0.identifier
        val isObsidian   = resource.identifier == MiningNode.OBSIDIAN_0.identifier

        if (!finishedMoving(player)) return true

        if (state == 0) {
            if (!checkRequirements(player, resource, node)) {
                player.scripts.reset()
                return true
            }

            if (!isEssence) sendMessage(player,
                if (isObsidian) "You swing your pick at the wall." else "You swing your pickaxe at the rock."
            )

            anim(player, resource, tool!!)
            return delayScript(player, getDelay(player, resource, tool))
        }

        anim(player, resource, tool!!)
        if (!checkReward(player, resource, tool)) {
            return delayScript(player, getDelay(player, resource, tool))
        }

        var reward = calculateReward(player, resource, isEssence, isGems, resource.reward)
        val rewardAmount = calculateRewardAmount(player, isEssence, reward)
        player.dispatch(ResourceProducedEvent(reward, rewardAmount, node))
        rewardXP(player, Skills.MINING, resource.experience * rewardAmount)

        // Special rewards: bracelet of clay, Witchaven gold, etc.
        handleSpecialRewards(player, resource, reward, rewardAmount, isEssence)

        // Send messages to player.
        val rewardName = getItemName(if (reward == Items.PERFECT_GOLD_ORE_446) Items.GOLD_ORE_444 else reward).lowercase()
        when {
            isGems       -> sendMessage(player, "You get ${prependArticle(rewardName)}.")
            isGranite    -> sendMessage(player, "You manage to quarry some granite.")
            isSandstone  -> sendMessage(player, "You manage to quarry some sandstone.")
            isMagicStone -> sendMessage(player, "You manage to mine some stone.")
            isObsidian   -> sendMessage(player, "You manage to mine some obsidian.")
            !isEssence   -> sendMessage(player, "You manage to mine some $rewardName.")
        }

        addItemOrDrop(player, reward, rewardAmount)

        // Chance-based gem rewards.
        handleGemFinds(player, isEssence)

        // Resource respawn logic.
        handleRespawn(player, node, resource, isEssence, isGems)

        return true
    }

    /**
     * Calculates the reward amount for mining, including diary and bonus effects.
     */
    private fun calculateRewardAmount(player: Player, isMiningEssence: Boolean, reward: Int): Int {
        var amount = 1
        if (!isMiningEssence && player.achievementDiaryManager.getDiary(DiaryType.VARROCK)?.level != -1) {
            amount += when (reward) {
                Items.CLAY_434, Items.COPPER_ORE_436, Items.TIN_ORE_438,
                Items.LIMESTONE_3211, Items.BLURITE_ORE_668, Items.IRON_ORE_440,
                Items.ELEMENTAL_ORE_2892, Items.SILVER_ORE_442, Items.COAL_453 ->
                    if (player.achievementDiaryManager.armour >= 0 && RandomFunction.random(100) < 4) 1 else 0
                Items.GOLD_ORE_444, Items.GRANITE_500G_6979, Items.GRANITE_2KG_6981,
                Items.GRANITE_5KG_6983, Items.MITHRIL_ORE_447 ->
                    if (player.achievementDiaryManager.armour >= 1 && RandomFunction.random(100) < 3) 1 else 0
                Items.ADAMANTITE_ORE_449 ->
                    if (player.achievementDiaryManager.armour >= 2 && RandomFunction.random(100) < 2) 1 else 0
                else -> 0
            }
        }
        if (hasTimerActive<ShootingStarBonus>(player) && RandomFunction.getRandom(5) == 3) amount += 1
        return amount
    }

    /**
     * Calculates which item id should be rewarded, including essence and gem special cases.
     */
    private fun calculateReward(player: Player, resource: MiningNode, isMiningEssence: Boolean, isMiningGems: Boolean, reward: Int): Int {
        var result = reward
        when {
            resource == MiningNode.SANDSTONE || resource == MiningNode.GRANITE -> {
                val value = RandomFunction.randomize(if (resource == MiningNode.GRANITE) 3 else 4)
                result += value shl 1
                rewardXP(player, Skills.MINING, value * 10.0)
            }
            isMiningEssence &&
                    // !GameWorld.settings!!.isMembers
                    getDynLevel(player, Skills.MINING) >= 30 -> result = Items.PURE_ESSENCE_7936

            isMiningGems -> result = RandomFunction.rollWeightedChanceTable(MiningNode.GEM_ROCK_REWARD).id
        }
        return result
    }

    /**
     * Checks if a mining action successfully produces a reward.
     */
    private fun checkReward(player: Player, resource: MiningNode?, tool: SkillingTool): Boolean {
        if (resource?.identifier == 14.toByte()) return true
        val level = 1 + getDynLevel(player, Skills.MINING) + getFamiliarBoost(player, Skills.MINING)
        val hostRatio = Math.random() * (100.0 * resource!!.rate)
        val clientRatio = Math.random() * ((level - resource.level) * (1.0 + tool.ratio))
        return hostRatio < clientRatio
    }

    /**
     * Gets the mining delay.
     */
    fun getDelay(player: Player, resource: MiningNode, pickaxe: SkillingTool): Int {
        if (resource.id in intArrayOf(Objects.RUNE_ESSENCE_2491, Objects.ROCK_16684)) {
            return 1
        }

        val playerLevel = player.getSkills().getLevel(Skills.MINING)
        val oreLevel = resource.level

        var delay = pickaxe.getMiningDelay()

        val levelDifference = playerLevel - oreLevel
        if (levelDifference > 0) {
            delay -= levelDifference / 10
        }

        if (delay < 1) delay = 1

        player.debug(colorize("Resource Level: [%DG$oreLevel</col>], Mining Level: [%B$playerLevel</col>], Delay: [%R$delay</col>]"))
        return delay
    }

    /**
     * Plays the mining animation for [player] on [resource] with [tool].
     */
    fun anim(player: Player, resource: MiningNode, tool: SkillingTool) {
        val anim = when (resource?.identifier) {
            14.toByte() -> tool.animation + 6128
            19.toByte() -> tool.animation + 9718
            else -> tool.animation
        }
        if (animationFinished(player)) animate(player, anim)
    }

    /**
     * Checks all requirements for mining a [resource] by [player].
     */
    fun checkRequirements(player: Player, resource: MiningNode, node: Node): Boolean {
        val allPickaxes = SkillingTool.values().filter {
            inEquipmentOrInventory(player, it.id)
        }

        if (allPickaxes.isEmpty()) {
            sendMessage(player, "You do not have a pickaxe to use.")
            return false
        }

        if (player.getSkills().getLevel(Skills.MINING) < resource.level) {
            sendMessage(player, "You need a Mining level of ${resource.level} to mine this rock.")
            return false
        }

        val usablePickaxe = allPickaxes
            .filter { player.getSkills().getLevel(Skills.MINING) >= it.level }
            .maxByOrNull { it.level }

        if (usablePickaxe == null) {
            sendMessage(player, "You need a pickaxe to mine this rock. You do not have a pickaxe which you have the Mining level to use.")
            return false
        }

        if (resource.identifier == 19.toByte() && !hasRequirement(player, Quests.TOKTZ_KET_DILL)) {
            sendDialogue(player, "You do not know the technique to mine stone slabs.")
            return false
        }

        if (resource.identifier == 19.toByte() && usablePickaxe in listOf(SkillingTool.INFERNO_ADZE, SkillingTool.INFERNO_ADZE2)) {
            sendDialogue(player, "I don't think I should use the Inferno Adze in here.")
            return false
        }

        if (freeSlots(player) == 0) {
            val prefix = "Your inventory is too full to hold any more"
            val message = when (resource.identifier) {
                4.toByte()  -> "$prefix limestone."
                13.toByte() -> "$prefix gems."
                14.toByte() -> "$prefix essence."
                15.toByte() -> "$prefix sandstone."
                16.toByte() -> "$prefix granite."
                19.toByte() -> "$prefix obsidian."
                else -> "$prefix ${getItemName(resource.reward).lowercase()}."
            }
            sendDialogue(player, message)
            return false
        }
        return node.isActive
    }

    /**
     * Handles bracelet of clay and perfect gold special rewards.
     */
    private fun handleSpecialRewards(player: Player, resource: MiningNode, reward: Int, amount: Int, isEssence: Boolean) {
        if (reward == Items.CLAY_434) {
            val bracelet = getItemFromEquipment(player, EquipmentSlot.HANDS)
            if (bracelet?.id == Items.BRACELET_OF_CLAY_11074) {
                var charges = player.getAttribute("jewellery-charges:bracelet-of-clay", 28)
                charges--
                sendMessage(player, "Your bracelet of clay softens the clay for you.")
                if (charges <= 0) {
                    if (removeItem(player, bracelet, Container.EQUIPMENT)) sendMessage(player, "Your bracelet of clay crumbles to dust.")
                    charges = 28
                }
                setAttribute(player, "/save:jewellery-charges:bracelet-of-clay", charges)
            }
        }

        val inBorders = inBorders(player, ZoneBorders(2728, 9696, 2742, 9681))
        if (inBorders && reward == Items.GOLD_ORE_444) {
            addItemOrDrop(player, Items.PERFECT_GOLD_ORE_446, amount)
        }
    }

    /**
     * Handles chance-based gem drops.
     */
    private fun handleGemFinds(player: Player, isEssence: Boolean) {
        if (isEssence) return
        var chance = 282
        val ring = getItemFromEquipment(player, EquipmentSlot.RING)
        if (ring != null && ring.name.lowercase().contains("ring of wealth")) chance = (chance / 1.5).toInt()
        val necklace = getItemFromEquipment(player, EquipmentSlot.NECK)
        if (necklace != null && necklace.id in Items.AMULET_OF_GLORY_1705..Items.AMULET_OF_GLORY4_1713) chance = (chance / 1.5).toInt()
        if (RandomFunction.roll(chance)) {
            val gem = gemRewards.random()
            sendMessage(player, "You find a ${gem.name}!")
            if (freeSlots(player) == 0) sendMessage(player, "You do not have enough space, so you drop the gem on the floor.")
            addItemOrDrop(player, gem.id)
        }
    }

    /**
     * Handles all resource respawn mechanics.
     */
    private fun handleRespawn(player: Player, node: Node, resource: MiningNode, isEssence: Boolean, isGems: Boolean) {
        if (resource.id == Objects.PILE_OF_ROCK_4030 && !isEssence && resource.respawnRate != 0) {
            removeScenery(node as Scenery)
            GameWorld.Pulser.submit(object : Pulse(resource.respawnDuration, player) {
                override fun pulse(): Boolean {
                    SceneryBuilder.add(Scenery(Objects.PILE_OF_ROCK_4027, node.location))
                    return true
                }
            })
            node.setActive(false)
        } else if (resource.id == Objects.OBSIDIAN_WALL_31229 && !isEssence && resource.respawnRate != 0) {
            SceneryBuilder.replaceWithTempBeforeNew(
                node.asScenery(),
                node.asScenery().transform(Objects.OBSIDIAN_WALL_31230),
                node.asScenery().transform(Objects.OBSIDIAN_WALL_9376),
                resource.respawnDuration,
                true
            )
        } else if (resource.id in Objects.GEM_ROCK_9030..Objects.GEM_ROCK_9032) {
            SceneryBuilder.replaceWithTempBeforeNew(
                node.asScenery(),
                node.asScenery().transform(resource.emptyId + 4),
                node.asScenery().transform(resource.emptyId),
                25,
                true
            )
        } else if (!isEssence && resource.respawnRate != 0) {
            SceneryBuilder.replace(
                node as Scenery,
                Scenery(resource.emptyId, node.getLocation(), node.type, node.rotation),
                resource.respawnDuration
            )
            node.setActive(false)
        }
    }
}