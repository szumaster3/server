package content.global.skill.gathering.mining

import content.data.GameAttributes
import content.data.items.SkillingTool
import content.global.activity.shootingstar.StarBonus
import core.api.*
import core.api.movement.finishedMoving
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
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import core.tools.prependArticle
import org.rs.consts.Items

class MiningListener : InteractionListener {
    private val gemRewards =
        arrayOf(
            ChanceItem(Items.UNCUT_SAPPHIRE_1623, 1, DropFrequency.COMMON),
            ChanceItem(Items.UNCUT_EMERALD_1621, 1, DropFrequency.COMMON),
            ChanceItem(Items.UNCUT_RUBY_1619, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.UNCUT_DIAMOND_1617, 1, DropFrequency.RARE),
        )

    override fun defineListeners() {
        defineInteraction(
            IntType.SCENERY,
            MiningNode.values().map { it.id }.toIntArray(),
            "mine",
            persistent = true,
            allowedDistance = 1,
            handler = ::handleMining,
        )

        on(IntType.SCENERY, "prospect") { player, node ->
            val rock = MiningNode.forId(node.asScenery().id)

            if (rock == null) {
                sendMessage(player, "There is no ore currently available in this rock.")
                return@on true
            }

            sendMessage(player, "You examine the rock for ores...")

            when (MiningNode.forId(node.id)!!.identifier) {
                13.toByte() -> {
                    queueScript(player, 3, QueueStrength.SOFT) {
                        sendMessage(player, "This rock contains gems.")
                        return@queueScript stopExecuting(player)
                    }
                    return@on true
                }

                15.toByte() -> {
                    queueScript(player, 3, QueueStrength.SOFT) {
                        sendMessage(player, "This rock is sandstone.")
                        return@queueScript stopExecuting(player)
                    }
                    return@on true
                }

                16.toByte() -> {
                    queueScript(player, 3, QueueStrength.SOFT) {
                        sendMessage(player, "This rock is granite.")
                        return@queueScript stopExecuting(player)
                    }
                    return@on true
                }

                18.toByte() -> {
                    queueScript(player, 3, QueueStrength.SOFT) {
                        sendMessage(player, "This rock contains a magical kind of stone.")
                        return@queueScript stopExecuting(player)
                    }
                    return@on true
                }

                2.toByte() -> {
                    val tutorialStage = getAttribute(player, GameAttributes.TUTORIAL_STAGE, -1)
                    if (tutorialStage < 71) {
                        player.dialogueInterpreter.sendBoldInput("This rock contains tin.")
                    } else {
                        sendMessage(player, "This rock contains ${Item(rock.reward).name.lowercase()}.")
                    }
                    return@on true
                }

                1.toByte() -> {
                    val tutorialStage = getAttribute(player, GameAttributes.TUTORIAL_STAGE, -1)
                    if (tutorialStage < 71) {
                        player.dialogueInterpreter.sendBoldInput("This rock contains copper.")
                    } else {
                        sendMessage(player, "This rock contains ${Item(rock.reward).name.lowercase()}.")
                    }
                    return@on true
                }

                else -> {
                    queueScript(player, 3, QueueStrength.SOFT) {
                        sendMessage(player, "This rock contains ${Item(rock.reward).name.lowercase()}.")
                        return@queueScript stopExecuting(player)
                    }
                    return@on true
                }
            }
        }
    }

    /*
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

    private fun handleMining(
        player: Player,
        node: Node,
        state: Int,
    ): Boolean {
        val resource = MiningNode.forId(node.id)
        val tool = SkillingTool.getPickaxe(player)
        val isEssence = resource!!.id in intArrayOf(2491, 16684)
        val isGems = resource.identifier == MiningNode.GEM_ROCK_0.identifier
        val isGranite = resource.identifier == MiningNode.GRANITE.identifier
        val isSandstone = resource.identifier == MiningNode.SANDSTONE.identifier
        val isMagicStone = resource.identifier == MiningNode.MAGIC_STONE_0.identifier

        if (!finishedMoving(player)) {
            return true
        }

        if (state == 0) {
            if (!checkRequirements(player, resource, node)) {
                player.scripts.reset()
                return true
            }
            if (!isEssence) {
                sendMessage(player, "You swing your pickaxe at the rock.")
            }

            anim(player, resource, tool!!)
            return delayScript(player, getDelay())
        }

        anim(player, resource, tool!!)
        if (!checkReward(player, resource, tool)) {
            return delayScript(player, getDelay())
        }

        var reward = resource.reward
        var rewardAmount: Int
        if (reward > 0) {
            reward = calculateReward(player, resource, isEssence, isGems, reward)
            rewardAmount = calculateRewardAmount(player, isEssence, reward)
            player.dispatch(ResourceProducedEvent(reward, rewardAmount, node))

            val experience = resource.experience * rewardAmount
            rewardXP(player, Skills.MINING, experience)

            if (reward == Items.CLAY_434) {
                val bracelet = getItemFromEquipment(player, EquipmentSlot.HANDS)
                if (bracelet != null && bracelet.id == Items.BRACELET_OF_CLAY_11074) {
                    var charges = player.getAttribute("jewellery-charges:bracelet-of-clay", 28)
                    charges--
                    reward = Items.SOFT_CLAY_1761
                    sendMessage(player, "Your bracelet of clay softens the clay for you.")
                    if (charges <= 0) {
                        if (removeItem(player, bracelet, Container.EQUIPMENT)) {
                            sendMessage(player, "Your bracelet of clay crumbles to dust.")
                            charges = 28
                        }
                    }
                    setAttribute(player, "/save:jewellery-charges:bracelet-of-clay", charges)
                }
            }
            val familyCrestZone = ZoneBorders(2728, 9696, 2742, 9681)
            if (reward == Items.GOLD_ORE_444 && inBorders(player, familyCrestZone)) {
                reward = Items.PERFECT_GOLD_ORE_446
            }

            val rewardName =
                if (reward == 446) {
                    getItemName(444).lowercase()
                } else {
                    getItemName(reward).lowercase()
                }

            if (isGems) {
                sendMessage(player, "You get ${prependArticle(rewardName)}.")
            } else if (isGranite) {
                sendMessage(player, "You manage to quarry some granite.")
            } else if (isSandstone) {
                sendMessage(player, "You manage to quarry some sandstone.")
            } else if (isMagicStone) {
                sendMessage(player, "You manage to mine some stone.")
            } else if (!isEssence) {
                sendMessage(player, "You manage to mine some ${rewardName.lowercase()}.")
            }

            addItemOrDrop(player, reward, rewardAmount)
            if (!isEssence) {
                var chance = 282
                var altered = false
                val ring = getItemFromEquipment(player, EquipmentSlot.RING)
                if (ring != null && ring.name.lowercase().contains("ring of wealth")) {
                    chance = (chance / 1.5).toInt()
                    altered = true
                }
                val necklace = getItemFromEquipment(player, EquipmentSlot.NECK)
                if (necklace != null && necklace.id in 1705..1713) {
                    chance = (chance / 1.5).toInt()
                    altered = true
                }
                if (RandomFunction.roll(chance)) {
                    val gem = gemRewards.random()
                    sendMessage(player, "You find a ${gem.name}!")
                    if (freeSlots(player) == 0) {
                        sendMessage(
                            player,
                            "You do not have enough space in your inventory, so you drop the gem on the floor.",
                        )
                    }
                    addItemOrDrop(player, gem.id)
                }
            }
        }

        if (resource.id == 4030 && !isEssence && resource.respawnRate != 0) {
            removeScenery(node as Scenery)
            GameWorld.Pulser.submit(
                object : Pulse(resource.respawnDuration, player) {
                    override fun pulse(): Boolean {
                        SceneryBuilder.add(Scenery(4027, node.location))
                        return true
                    }
                },
            )
            node.isActive = false
            return false
        }

        if (resource.id in 9030..9032) {
            SceneryBuilder.replaceWithTempBeforeNew(
                node.asScenery(),
                node.asScenery().transform(resource.emptyId + 4),
                node.asScenery().transform(resource.emptyId),
                25,
                true,
            )
        } else if (!isEssence && resource!!.respawnRate != 0) {
            SceneryBuilder.replace(
                node as Scenery,
                Scenery(
                    resource.emptyId,
                    node.getLocation(),
                    node.type,
                    node.rotation,
                ),
                resource.respawnDuration,
            )
            node.setActive(false)
            return true
        }
        return true
    }

    private fun calculateRewardAmount(
        player: Player,
        isMiningEssence: Boolean,
        reward: Int,
    ): Int {
        var amount = 1

        if (!isMiningEssence && player.achievementDiaryManager.getDiary(DiaryType.VARROCK)!!.level != -1) {
            when (reward) {
                Items.CLAY_434, Items.COPPER_ORE_436, Items.TIN_ORE_438, Items.LIMESTONE_3211,
                Items.BLURITE_ORE_668, Items.IRON_ORE_440, Items.ELEMENTAL_ORE_2892, Items.SILVER_ORE_442,
                Items.COAL_453,
                ->
                    if (player.achievementDiaryManager.armour >= 0 && RandomFunction.random(100) < 4) {
                        amount += 1
                        sendMessage(player, "The Varrock armour allows you to mine an additional ore.")
                    }

                Items.GOLD_ORE_444, Items.GRANITE_500G_6979, Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983,
                Items.MITHRIL_ORE_447,
                ->
                    if (player.achievementDiaryManager.armour >= 1 &&
                        RandomFunction.random(100) < 3
                    ) {
                        amount += 1
                        sendMessage(player, "The Varrock armour allows you to mine an additional ore.")
                    }

                Items.ADAMANTITE_ORE_449 ->
                    if (player.achievementDiaryManager.armour >= 2 &&
                        RandomFunction.random(100) < 2
                    ) {
                        amount += 1
                        sendMessage(player, "The Varrock armour allows you to mine an additional ore.")
                    }
            }
        }

        if (hasTimerActive<StarBonus>(player)) {
            if (RandomFunction.getRandom(5) == 3) {
                sendMessage(player, "...you manage to mine a second ore thanks to the Star Sprite.")
                amount += 1
            }
        }

        return amount
    }

    private fun calculateReward(
        player: Player,
        resource: MiningNode,
        isMiningEssence: Boolean,
        isMiningGems: Boolean,
        reward: Int,
    ): Int {
        var reward = reward

        if (resource == MiningNode.SANDSTONE || resource == MiningNode.GRANITE) {
            val value = RandomFunction.randomize(if (resource == MiningNode.GRANITE) 3 else 4)
            reward += value shl 1
            rewardXP(player, Skills.MINING, value * 10.toDouble())
        } else if (isMiningEssence && getDynLevel(player, Skills.MINING) >= 30) {
            reward = 7936
        } else if (isMiningGems) {
            reward = RandomFunction.rollWeightedChanceTable(MiningNode.GEM_ROCK_REWARD).id
        }

        return reward
    }

    private fun checkReward(
        player: Player,
        resource: MiningNode?,
        tool: SkillingTool,
    ): Boolean {
        val level = 1 + getDynLevel(player, Skills.MINING) + getFamiliarBoost(player, Skills.MINING)
        val hostRatio = Math.random() * (100.0 * resource!!.rate)
        var toolRatio = tool.ratio
        val clientRatio = Math.random() * ((level - resource.level) * (1.0 + toolRatio))

        return hostRatio < clientRatio
    }

    fun getDelay(): Int {
        return 1
    }

    fun anim(
        player: Player,
        resource: MiningNode?,
        tool: SkillingTool,
    ) {
        val isEssence = resource!!.id in intArrayOf(org.rs.consts.Scenery.RUNE_ESSENCE_2491, org.rs.consts.Scenery.ROCK_16684)
        if (animationFinished(player)) {
            animate(player, if (!isEssence) tool.animation else tool.animation + 6128, true)
        }
    }

    fun checkRequirements(
        player: Player,
        resource: MiningNode,
        node: Node,
    ): Boolean {
        if (getDynLevel(player, Skills.MINING) < resource.level) {
            sendMessage(player, "You need a mining level of ${resource.level} to mine this rock.")
            return false
        }
        if (SkillingTool.getPickaxe(player) == null) {
            sendMessage(player, "You do not have a pickaxe to use.")
            return false
        }
        if (freeSlots(player) == 0) {
            sendMessage(player, "Your inventory is too full to hold any more ore.")
            return false
        }
        if(inInventory(player, Items.MAGIC_STONE_4703)) {
            sendMessage(player, "You have already mined some stone. You don't need any more.")
            return false
        }
        return node.isActive
    }
}
