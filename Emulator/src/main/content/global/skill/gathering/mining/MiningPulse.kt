package content.global.skill.gathering.mining

import content.data.items.SkillingTool
import core.api.*
import core.cache.def.impl.ItemDefinition
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
import org.rs.consts.Items

class MiningPulse(
    private val player: Player,
    private val node: Node,
) : Pulse(1, player, node) {
    private var resource: MiningNode? = null
    private var isMiningEssence = false
    private var isMiningGems = false
    private var isMiningGranite = false
    private var isMiningSandstone = false
    private var isMiningMagicStone = false
    private var ticks = 0
    private var resetAnimation = true

    private val perfectGoldOreLocations =
        listOf(
            Location(2735, 9695, 0),
            Location(2737, 9689, 0),
            Location(2740, 9684, 0),
            Location(2737, 9683, 0),
        )

    fun message(
        player: Player,
        type: Int,
    ) {
        if (type == 0) {
            return sendMessage(player, "You swing your pickaxe at the rock.")
        }
    }

    override fun pulse(): Boolean {
        if (!checkRequirements()) {
            return true
        }
        animate()
        return reward()
    }

    override fun stop() {
        if (resetAnimation) {
            animate(player, Animation(-1, Animator.Priority.HIGH))
        }
        super.stop()
        message(player, 1)
    }

    override fun start() {
        resource = MiningNode.forId(node.id)
        if (MiningNode.isEmpty(node.id)) {
            sendMessage(player, "This rock contains no ore.")
        }
        if (resource == null) {
            return
        }

        if (resource!!.id == org.rs.consts.Scenery.ROCKS_2099 && !perfectGoldOreLocations.contains(node.location)) {
            resource = MiningNode.forId(org.rs.consts.Scenery.ROCKS_2098)
        }
        if (resource!!.id == org.rs.consts.Scenery.RUNE_ESSENCE_2491 || resource!!.id == org.rs.consts.Scenery.ROCK_16684) {
            isMiningEssence = true
        }
        if (resource!!.identifier == MiningNode.GEM_ROCK_0.identifier) {
            isMiningGems = true
        }
        if (resource!!.identifier == MiningNode.SANDSTONE.identifier) {
            isMiningSandstone = true
        }
        if (resource!!.identifier == MiningNode.GRANITE.identifier) {
            isMiningGranite = true
        }
        if (resource!!.identifier == MiningNode.MAGIC_STONE_0.identifier) {
            isMiningMagicStone = true
        }
        if (checkRequirements()) {
            super.start()
            message(player, 0)
        }
    }

    fun checkRequirements(): Boolean {
        if (getDynLevel(player, Skills.MINING) < resource!!.level) {
            sendMessage(player, "You need a mining level of ${resource!!.level} to mine this rock.")
            return false
        }
        if (SkillingTool.getPickaxe(player) == null) {
            sendMessage(player, "You do not have a pickaxe to use.")
            return false
        }
        if (freeSlots(player) == 0) {
            if (resource!!.identifier == 4.toByte()) {
                sendDialogue(player, "Your inventory is too full to hold any more limestone.")
                return false
            }
            if (resource!!.identifier == 13.toByte()) {
                sendDialogue(player, "Your inventory is too full to hold any more gems.")
                return false
            }
            if (resource!!.identifier == 14.toByte()) {
                sendDialogueLines(player, "Your inventory is too full to hold any more essence.")
                return false
            }
            if (resource!!.identifier == 15.toByte()) {
                sendDialogue(player, "Your inventory is too full to hold any more sandstone.")
                return false
            }
            if (resource!!.identifier == 16.toByte()) {
                sendDialogue(player, "Your inventory is too full to hold any more granite.")
                return false
            }
            if (resource!!.identifier == 18.toByte() && inInventory(player, Items.MAGIC_STONE_4703)) {
                sendMessage(player, "You have already mined some stone. You don't need any more.")
                return false
            }
            val item = getItemName(resource!!.reward).lowercase()
            sendDialogue(
                player,
                "Your inventory is too full to hold any more $item.",
            )
            return false
        }
        return true
    }

    fun animate() {
        animate(
            player,
            (
                if (resource?.id != 2491 ||
                    resource?.id != 16684
                ) {
                    SkillingTool.getPickaxe(player)!!.animation
                } else {
                    SkillingTool
                        .getPickaxe(
                            player,
                        )!!
                        .animation + 6128
                }
            ),
        )
    }

    fun reward(): Boolean {
        if (!checkReward()) {
            return false
        }

        if (++ticks % (if (isMiningEssence) 1 else 4) != 0) {
            return false
        }

        var reward = resource!!.reward
        var rewardAmount: Int
        if (reward > 0) {
            reward = calculateReward(reward)
            rewardAmount = calculateRewardAmount(reward)

            player.dispatch(ResourceProducedEvent(reward, rewardAmount, node))

            val experience = resource!!.experience * rewardAmount
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
            val rewardName = getItemName(reward).lowercase()

            if (isMiningGems) {
                sendMessage(player, "You get ${prependArticle(rewardName)}.")
            } else if (isMiningGranite) {
                sendMessage(player, "You manage to quarry some granite.")
            } else if (isMiningSandstone) {
                sendMessage(player, "You manage to quarry some sandstone.")
            } else if (isMiningMagicStone) {
                sendMessage(player, "You manage to mine some stone.")
            } else {
                sendMessage(player, "You manage to get some ${rewardName.lowercase()}.")
            }

            addItemOrDrop(player, reward, rewardAmount)

            if (!isMiningEssence) {
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
                    val gem = GEM_REWARDS.random()
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

            if (resource!!.id == 4030 && !isMiningEssence && resource!!.respawnRate != 0) {
                removeScenery(node as Scenery)
                GameWorld.Pulser.submit(
                    object : Pulse(resource!!.respawnDuration, player) {
                        override fun pulse(): Boolean {
                            SceneryBuilder.add(Scenery(4027, node.location))
                            return true
                        }
                    },
                )
                node.isActive = false
                return false
            }

            if (!isMiningEssence && resource!!.respawnRate != 0) {
                SceneryBuilder.replace(
                    node as Scenery,
                    Scenery(
                        resource!!.emptyId,
                        node.getLocation(),
                        node.type,
                        node.rotation,
                    ),
                    resource!!.respawnDuration,
                )
                node.setActive(false)
                return true
            }
        }
        return false
    }

    private fun calculateRewardAmount(reward: Int): Int {
        var amount = 1

        if (!isMiningEssence && player.achievementDiaryManager.getDiary(DiaryType.VARROCK)!!.level != -1) {
            when (reward) {
                Items.CLAY_434, Items.COPPER_ORE_436, Items.TIN_ORE_438, Items.LIMESTONE_3211, Items.BLURITE_ORE_668, Items.IRON_ORE_440, Items.ELEMENTAL_ORE_2892, Items.SILVER_ORE_442, Items.COAL_453 ->
                    if (player.achievementDiaryManager.armour >=
                        0 &&
                        RandomFunction.random(
                            100,
                        ) <= 4
                    ) {
                        amount += 1
                        sendMessage(player, "The Varrock armour allows you to mine an additional ore.")
                    }

                Items.GOLD_ORE_444, Items.GRANITE_500G_6979, Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983, Items.MITHRIL_ORE_447 ->
                    if (player.achievementDiaryManager.armour >=
                        1 &&
                        RandomFunction.random(
                            100,
                        ) <= 3
                    ) {
                        amount += 1
                        sendMessage(player, "The Varrock armour allows you to mine an additional ore.")
                    }

                Items.ADAMANTITE_ORE_449 ->
                    if (player.achievementDiaryManager.armour >= 2 &&
                        RandomFunction.random(100) <= 2
                    ) {
                        amount += 1
                        sendMessage(player, "The Varrock armour allows you to mine an additional ore.")
                    }
            }
        }

        if (player.hasActiveState("shooting-star")) {
            if (RandomFunction.getRandom(5) == 3) {
                sendMessage(player, "...you manage to mine a second ore thanks to the Star Sprite.")
                amount += 1
            }
        }
        return amount
    }

    private fun calculateReward(reward: Int): Int {
        var reward = reward
        if (resource == MiningNode.SANDSTONE || resource == MiningNode.GRANITE) {
            val value = RandomFunction.randomize(if (resource == MiningNode.GRANITE) 3 else 4)
            reward += value shl 1
            rewardXP(player, Skills.MINING, value * 10.toDouble())
        } else if (isMiningEssence && getDynLevel(player, Skills.MINING) >= 30) {
            reward = Items.PURE_ESSENCE_7936
        } else if (isMiningGems) {
            reward = RandomFunction.rollWeightedChanceTable(MiningNode.GEM_ROCK_REWARD).id
        }
        return reward
    }

    private fun checkReward(): Boolean {
        val level = 1 + getDynLevel(player, Skills.MINING) + getFamiliarBoost(player, Skills.MINING)
        val hostRatio = Math.random() * (100.0 * resource!!.rate)
        var toolRatio = SkillingTool.getPickaxe(player)!!.ratio
        val clientRatio = Math.random() * ((level - resource!!.level) * (1.0 + toolRatio))
        return hostRatio < clientRatio
    }

    companion object {
        private val GEM_REWARDS =
            arrayOf(
                ChanceItem(Items.UNCUT_SAPPHIRE_1623, 1, DropFrequency.COMMON),
                ChanceItem(Items.UNCUT_EMERALD_1621, 1, DropFrequency.COMMON),
                ChanceItem(Items.UNCUT_RUBY_1619, 1, DropFrequency.UNCOMMON),
                ChanceItem(Items.UNCUT_DIAMOND_1617, 1, DropFrequency.RARE),
            )
    }

    init {
        super.stop()
    }
}
