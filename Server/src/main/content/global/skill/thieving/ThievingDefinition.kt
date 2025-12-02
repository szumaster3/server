package content.global.skill.thieving

import core.api.*
import core.api.utils.WeightBasedTable
import core.game.event.ResourceProducedEvent
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.GameWorld.ticks
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import core.tools.StringUtils.isPlusN
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery as Objects

object ThievingDefinition {

    /**
     * Represents stall data.
     */
    enum class Stall(full: Array<Int>, empty: Array<Int>, var level: Int, var rewards: Array<Item>, var experience: Double, var delay: Int, var message: String) {
        VEGETABLE_STALL(arrayOf(Objects.VEG_STALL_4706, Objects.VEG_STALL_4708), arrayOf(Objects.MARKET_STALL_634), 2, arrayOf(Item(Items.ONION_1957, 1), Item(Items.CABBAGE_1965, 1), Item(Items.POTATO_1942, 1), Item(Items.TOMATO_1982, 1), Item(Items.GARLIC_1550, 1)), 10.0, 4, "vegetables",),
        BAKER_STALL(arrayOf(Objects.BAKER_S_STALL_2561, Objects.BAKERY_STALL_6163, Objects.BAKER_S_STALL_34384), arrayOf(Objects.MARKET_STALL_634, Objects.MARKET_STALL_6984, Objects.MARKET_STALL_34381), 5, arrayOf(Item(Items.CAKE_1891, 1), Item(Items.BREAD_2309, 1), Item(Items.CHOCOLATE_SLICE_1901, 1)), 16.0, 4, "bread"),
        CRAFTING_STALL(arrayOf(Objects.CRAFTING_STALL_4874, Objects.CRAFTING_STALL_6166), arrayOf(Objects.BAMBOO_DESK_4797, Objects.MARKET_STALL_6984), 5, arrayOf(Item(Items.RING_MOULD_1592, 1), Item(Items.NECKLACE_MOULD_1597, 1), Item(Items.CHISEL_1755, 1)), 16.0, 12, "tool"),
        TEA_STALL(arrayOf(Objects.TEA_STALL_635, Objects.TEA_STALL_6574), arrayOf(Objects.MARKET_STALL_634, Objects.MARKET_STALL_6573), 5, arrayOf(Item(Items.CUP_OF_TEA_712, 1)), 16.0, 12, "tea"),
        SILK_STALL(arrayOf(Objects.SILK_STALL_34383, Objects.SILK_STALL_2560), arrayOf(Objects.MARKET_STALL_34381, Objects.MARKET_STALL_634), 20, arrayOf(Item(Items.SILK_950, 1)), 24.0, 13, "silk"),
        WINE_STALL(arrayOf(Objects.MARKET_STALL_14011), arrayOf(Objects.MARKET_STALL_634), 22, arrayOf(Item(Items.JUG_1935, 1), Item(Items.JUG_OF_WATER_1937, 1), Item(Items.JUG_OF_WINE_1993, 1), Item(Items.BOTTLE_OF_WINE_7919, 1)), 27.0, 27, "wine",),
        MARKET_SEED_STALL(arrayOf(Objects.SEED_STALL_7053), arrayOf(Objects.MARKET_STALL_634), 27, arrayOf(Item(Items.MARIGOLD_SEED_5096, 1), Item(Items.ROSEMARY_SEED_5097, 1), Item(Items.REDBERRY_SEED_5101, 1), Item(Items.POTATO_SEED_5318, 1), Item(Items.ONION_SEED_5319, 1), Item(Items.CABBAGE_SEED_5324, 1)), 10.0, 19, "seeds",),
        FUR_STALL(arrayOf(Objects.FUR_STALL_34387, Objects.FUR_STALL_2563, Objects.FUR_STALL_4278), arrayOf(Objects.MARKET_STALL_34381, Objects.MARKET_STALL_634, Objects.MARKET_STALL_634), 35, arrayOf(Item(Items.FUR_6814, 1), Item(Items.GREY_WOLF_FUR_958, 1)), 36.0, 25, "fur"),
        FISH_STALL(arrayOf(Objects.FISH_STALL_4277, Objects.FISH_STALL_4705, Objects.FISH_STALL_4707), arrayOf(Objects.MARKET_STALL_634, Objects.MARKET_STALL_634, Objects.MARKET_STALL_634), 42, arrayOf(Item(Items.RAW_SALMON_331, 1), Item(Items.RAW_TUNA_359, 1), Item(Items.RAW_LOBSTER_377, 1)), 42.0, 27, "fish"),
        CROSSBOW_STALL(arrayOf(Objects.CROSSBOW_STALL_17031), arrayOf(Objects.MARKET_STALL_6984), 49, arrayOf(Item(Items.BRONZE_BOLTS_877, 3), Item(Items.BRONZE_LIMBS_9420, 1), Item(Items.WOODEN_STOCK_9440, 1)), 52.0, 19, "crossbow parts"),
        SILVER_STALL(arrayOf(Objects.SILVER_STALL_2565, Objects.SILVER_STALL_6164, Objects.SILVER_STALL_34382), arrayOf(Objects.MARKET_STALL_634, Objects.MARKET_STALL_6984, Objects.MARKET_STALL_34381), 50, arrayOf(Item(Items.SILVER_ORE_442, 1)), 54.0, 50, "silver"),
        SPICE_STALL(arrayOf(Objects.SPICE_STALL_34386, Objects.CRAFTING_STALL_6166), arrayOf(Objects.MARKET_STALL_34381, Objects.MARKET_STALL_6984), 65, arrayOf(Item(Items.SPICE_2007, 1)), 81.0, 134, "spices"),
        GEM_STALL(arrayOf(Objects.GEM_STALL_2562, Objects.GEM_STALL_6162, Objects.GEM_STALL_34385), arrayOf(Objects.MARKET_STALL_634, Objects.MARKET_STALL_6984, Objects.MARKET_STALL_34381), 75, arrayOf(Item(Items.UNCUT_SAPPHIRE_1623, 1), Item(Items.EMERALD_1605, 1), Item(Items.RUBY_1603, 1), Item(Items.DIAMOND_1601, 1)), 160.0, 300, "gems"),
        SCIMITAR_STALL(arrayOf(Objects.SCIMITAR_STALL_4878), arrayOf(Objects.BAMBOO_DESK_4797), 65, arrayOf(Item(Items.IRON_SCIMITAR_1323, 1)), 100.0, 134, "equipment"),
        MAGIC_STALL(arrayOf(Objects.MAGIC_STALL_4877), arrayOf(Objects.BAMBOO_DESK_4797), 65, arrayOf(Item(Items.AIR_RUNE_556, 1), Item(Items.EARTH_RUNE_557, 1), Item(Items.FIRE_RUNE_554, 1), Item(Items.WATER_RUNE_555, 1), Item(Items.LAW_RUNE_563, 1)), 100.0, 134, "equipment"),
        GENERAL_STALL(arrayOf(Objects.GENERAL_STALL_4876), arrayOf(Objects.BAMBOO_DESK_4797), 5, arrayOf(Item(Items.EMPTY_POT_1931, 1), Item(Items.HAMMER_2347, 1), Item(Items.TINDERBOX_590, 1)), 16.0, 12, "goods"),
        FOOD_STALL(arrayOf(Objects.FOOD_STALL_4875), arrayOf(Objects.BAMBOO_DESK_4797), 5, arrayOf(Item(Items.BANANA_1963, 1)), 16.0, 12, "food"),
        CANDLES(arrayOf(Objects.CANDLES_19127), arrayOf(Objects.CANDLES_19127), 20, arrayOf(Item(Items.CANDLE_36, 1)), 20.0, 0, "candles"),
        COUNTER(arrayOf(Objects.COUNTER_2793), arrayOf(Objects.COUNTER_2791), 15, arrayOf(Item(Items.ROCK_CAKE_2379, 1)), 6.5, 12, "rocks"), ;

        var fullIDs: List<Int> = ArrayList(listOf(*full))
        var empty_ids: List<Int> = ArrayList(listOf(*empty))

        fun getEmpty(id: Int): Int {
            val fullIndex = fullIDs.indexOf(id)
            return empty_ids[fullIndex]
        }

        val randomLoot: Item
            get() = rewards[RandomFunction.random(rewards.size)]

        companion object {
            private val idMap =
                HashMap<Int, Stall>().apply {
                    Stall.values().forEach { entry ->
                        entry.fullIDs.forEach { id -> putIfAbsent(id, entry) }
                    }
                }

            fun stealFromStall(player: Player, node: Scenery, stall: Stall) {
                if (player.inCombat()) {
                    sendMessage(player, "You can't steal from the market stall during combat!")
                    return
                }
                if (getStatLevel(player, Skills.THIEVING) < stall.level) {
                    sendMessage(
                        player,
                        "You need to be level ${stall.level} to steal from the ${node.name.lowercase()}."
                    )
                    return
                }
                if (player.inventory.freeSlots() == 0) {
                    sendMessage(player, "You don't have enough inventory space.")
                    return
                }

                if (player.location.isInRegion(10553) && !isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                    if (stall.fullIDs.contains(4278)) {
                        sendDialogue(player, "The fur trader is staring at you suspiciously. You cannot steal from his stall while he distrusts you.")
                        return
                    }
                    if (stall.fullIDs.contains(4277)) {
                        sendDialogue(player, "The fishmonger is staring at you suspiciously. You cannot steal from his stall while he distrusts you.")
                        return
                    }
                }

                player.animate(Animation(Animations.HUMAN_MULTI_USE_832))
                player.locks.lockInteractions(2)

                queueScript(player, 0, QueueStrength.WEAK) {
                    delayScript(player, 2)
                    val item = stall.randomLoot
                    val success = RandomFunction.random(15) >= 4

                    if (!success) {
                        for (npc in RegionManager.getLocalNpcs(player.location, 8)) {
                            if (!npc.properties.combatPulse.isAttacking && (npc.id == 32 || npc.id == 2236)) {
                                npc.sendChat("Hey! Get your hands off there!")
                                npc.properties.combatPulse.attack(player)
                            }
                        }
                        if (stall == Stall.CANDLES) {
                            stun(player, 15, false)
                            impact(player, 3, ImpactHandler.HitsplatType.NORMAL)
                            player.sendMessage("A higher power smites you.")
                        }
                        return@queueScript stopExecuting(player)
                    }


                    player.inventory.add(item)
                    player.getSkills().addExperience(Skills.THIEVING, stall.experience, true)
                    if (stall == SILK_STALL) {
                        player.getSavedData().globalData.setSilkSteal(System.currentTimeMillis() + 1800000)
                    }
                    if (node.isActive) {
                        replaceScenery(node, node.transform(stall.getEmpty(node.id)).id, stall.delay)
                    }

                    sendMessage(
                        player,
                        "You steal ${if (isPlusN(item.name)) "an" else "a"} ${item.name.lowercase()} from the ${
                            stall.name.lowercase().replace('_', ' ')
                        }."
                    )
                    player.dispatch(ResourceProducedEvent(item.id, item.amount, node, 0))
                    return@queueScript stopExecuting(player)
                }
            }
        }
    }

    /**
     * Represents pickpocket data.
     */
    enum class Pickpocket(val ids: IntArray, val requiredLevel: Int, val low: Double, val high: Double, val experience: Double, val stunDamageMin: Int, val stunDamageMax: Int, val stunTime: Int, val message: String?, val table: WeightBasedTable) {
        MAN(ThievingLootTable.MAN_IDS, 1, 180.0, 240.0, 8.0, 1, 1, 5, "What do you think you're doing?", ThievingLootTable.MAN_PICKPOCKET_LOOT),
        CURATOR_HAIG_HELEN(ThievingLootTable.CURATOR_HAIG_HELEN_IDS, 1, 180.0, 240.0, 8.0, 1, 1, 5, null, ThievingLootTable.CURATOR_HAIG_HELEN_PICKPOCKET_LOOT),
        GANG_OF_THIEVE(ThievingLootTable.GANG_OF_THIEVE_IDS, 1, 180.0, 240.0, 8.0, 1, 1, 5, "What do you think you're doing?", ThievingLootTable.GANG_OF_THIEVE_PICKPOCKET_LOOT),
        FARMER(ThievingLootTable.FARMER_IDS, 10, 180.0, 240.0, 14.5, 1, 1, 5, "What do you think you're doing?", ThievingLootTable.FARMER_PICKPOCKET_LOOT),
        MALE_HAM_MEMBER(ThievingLootTable.HAM_MALE_IDS, 20, 117.0, 240.0, 22.5, 1, 3, 4, "What do you think you're doing?", ThievingLootTable.HAM_PICKPOCKET_LOOT),
        FEMALE_HAM_MEMBER(ThievingLootTable.HAM_FEMALE_IDS, 15, 135.0, 240.0, 18.5, 1, 3, 4, "Stop! @name is a thief!", ThievingLootTable.HAM_PICKPOCKET_LOOT),
        WARRIOR(ThievingLootTable.WARRIOR_IDS, 25, 84.0, 240.0, 26.0, 2, 2, 5, "What do you think you're doing?", ThievingLootTable.WARRIOR_PICKPOCKET_LOOT),
        VILLAGER(ThievingLootTable.VILLAGER_IDS, 30, 74.0, 240.0, 8.0, 2, 2, 5, "Thief! Thief! Get away from me.", ThievingLootTable.WARRIOR_PICKPOCKET_LOOT),
        ROGUE(ThievingLootTable.ROGUE_IDS, 32, 74.0, 240.0, 35.5, 2, 2, 5, "What do you think you're doing?", ThievingLootTable.ROGUE_PICKPOCKET_LOOT),
        CAVE_GOBLIN(ThievingLootTable.CAVE_GOBLIN_IDS, 36, 72.0, 240.0, 40.0, 1, 1, 5, null, ThievingLootTable.CAVE_GOBLIN_PICKPOCKET_LOOT),
        MASTER_FARMER(ThievingLootTable.MASTER_FARMER_IDS, 38, 90.0, 240.0, 43.0, 3, 3, 5, "Cor blimey, mate! What are ye doing in me pockets?", ThievingLootTable.MASTER_FARMER_PICKPOCKET_LOOT),
        GUARD(ThievingLootTable.GUARD_IDS , 40, 50.0, 240.0, 46.5, 2, 2, 5, "What do you think you're doing?", ThievingLootTable.GUARD_PICKPOCKET_LOOT),
        FREMENNIK_CITIZEN(ThievingLootTable.FREMENNIK_IDS, 45, 65.0, 240.0, 65.0, 2, 2, 5, "You stay away from me Outlander!", ThievingLootTable.FREMENNIK_PICKPOCKET_LOOT),
        BEARDED_BANDIT(ThievingLootTable.BEARDED_BANDIT_IDS, 45, 50.0, 240.0, 65.0, 5, 5, 5, "What do you think you're doing?", ThievingLootTable.BEARDED_BANDIT_PICKPOCKET_LOOT),
        DESERT_BANDIT(ThievingLootTable.DESERT_BANDIT_IDS, 53, 50.0, 240.0, 79.5, 3, 3, 5, "I'll kill you for that!", ThievingLootTable.DESERT_BANDIT_PICKPOCKET_LOOT),
        KNIGHT_OF_ADROUGNE(ThievingLootTable.KNIGHT_OF_ADROUGNE_IDS, 55, 50.0, 240.0, 84.3, 3, 3, 6, null, ThievingLootTable.KNIGHT_OF_ADROUGNE_PICKPOCKET_LOOT),
        YANILLE_WATCHMAN(ThievingLootTable.YANILLE_WATCHMAN_IDS, 65, 50.0, 240.0, 137.5, 3, 3, 5, "What do you think you're doing?", ThievingLootTable.YANILLE_WATCHMAN_PICKPOCKET_LOOT),
        MENAPHITE_THUG(ThievingLootTable.MENAPHITE_THUG_IDS, 65, 50.0, 240.0, 137.5, 5, 5, 5, "I'll kill you for that!", ThievingLootTable.MENAPHITE_THUG_PICKPOCKET_LOOT),
        PALADIN(ThievingLootTable.PALADIN_IDS, 70, 50.0, 150.0, 151.75, 3, 3, 5, "Hey! Get your hands off there!", ThievingLootTable.PALADIN_PICKPOCKET_LOOT),
        GNOME(ThievingLootTable.GNOME_IDS, 75, 8.0, 120.0, 198.5, 1, 1, 5, "What do you think you're doing?", ThievingLootTable.GNOME_PICKPOCKET_LOOT),
        HERO(ThievingLootTable.HERO_IDS, 80, 6.0, 100.0, 273.3, 6, 6, 6, "What do you think you're doing?", ThievingLootTable.HERO_PICKPOCKET_LOOT);

        companion object {
            /**
             * A map that links object IDs to their corresponding [Pickpocket] enum entry.
             */
            val idMap: MutableMap<Int, Pickpocket> = HashMap(Pickpocket.values().size * 5)

            init {
                Pickpocket.values().forEach { pickpocket ->
                    pickpocket.ids.forEach { id ->
                        idMap[id] = pickpocket
                    }
                }
            }

            /**
             * Gets the [Pickpocket] instance associated with a specific object id.
             *
             * @param id The object ID used for lookup.
             * @return The corresponding [Pickpocket] instance, or `null` if not found.
             */
            @JvmStatic
            fun forID(id: Int): Pickpocket? = idMap[id]
        }

        /**
         * Computes the chance of successfully pickpocketing based on the thieving level.
         *
         * @param player The [Player] attempting to pickpocket.
         * @return A [Double] representing the success probability.
         */
        fun getSuccessChance(player: Player): Double =
            RandomFunction.getSkillSuccessChance(low, high, player.skills.getLevel(Skills.THIEVING))
    }

    @JvmStatic
    fun pickpocketRoll(player: Player, low: Double, high: Double, table: WeightBasedTable): ArrayList<Item>? {
        var successMod = 0.0
        if (inInventory(player, Items.GLOVES_OF_SILENCE_10075, 1)) {
            successMod += 3
        }
        val chance = RandomFunction.randomDouble(1.0, 100.0)
        val failThreshold =
            RandomFunction.getSkillSuccessChance(low, high, getStatLevel(player, Skills.THIEVING)) + successMod
        if (chance > failThreshold) {
            return null
        } else {
            return table.roll()
        }
    }

    /**
     * Represents door available to force open data.
     */
    enum class Doors(val locations: Array<Location>, val level: Int, val experience: Double, val isLockpick: Boolean = false, val flipped: Boolean = false) {
        DOOR1(arrayOf(Location.create(3014, 3182)), 1, 0.0),
        DOOR2(arrayOf(Location.create(2672, 3308)), 1, 3.8),
        DOOR3(arrayOf(Location.create(2672, 3301)), 14, 15.0),
        DOOR4(arrayOf(Location.create(2610, 3316)), 15, 15.0),
        DOOR5(arrayOf(Location.create(3190, 3957)), 32, 25.0, isLockpick = true),
        DOOR6(arrayOf(Location.create(2565, 3356)), 46, 37.5),
        DOOR7(arrayOf(Location.create(2579, 3286, 1)), 61, 50.0),
        DOOR8(arrayOf(Location.create(2579, 3307, 1)), 61, 50.0),
        DOOR9(arrayOf(Location.create(3018, 3187)), 1, 0.0),
        DOOR10(arrayOf(Location.create(2601, 9482)), 82, 0.0, isLockpick = true),
        DOOR11(arrayOf(Location.create(3044, 3956)), 39, 35.0, isLockpick = true, flipped = true),
        DOOR12(arrayOf(Location.create(3041, 3959)), 39, 35.0, isLockpick = true, flipped = true),
        DOOR13(arrayOf(Location.create(3038, 3956)), 39, 35.0, isLockpick = true, flipped = true);

        private fun isOnCorrectSide(player: Player, obj: Scenery): Boolean {
            val dx = player.location.x - obj.location.x
            val dz = player.location.z - obj.location.z

            val isNorth = dz > 0
            val isEast = dx > 0
            val correctSide = !(isNorth || isEast)
            return if (flipped) !correctSide else correctSide
        }

        /**
         * Opens the door if the player is on the correct side of the door.
         * @param player The player opening the door.
         * @param door The door being opened.
         */
        fun open(player: Player, door: Scenery) {
            if (!isOnCorrectSide(player, door)) {
                sendMessage(player, "The door is locked.")
                return
            }
            handleAutowalkDoor(player, door)
            sendMessage(player, "You go through the door.")
        }

        /**
         * Checks if the player is on the correct side of the door to interact with it.
         *
         * @param player The player interacting with the door.
         * @param door The door being interacted with.
         * @return True if the player is inside the door, otherwise false.
         */
        private fun isInside(player: Player, door: Scenery): Boolean {
            var inside = false
            val dir = Direction.getLogicalDirection(player.location, door.location)
            val direction = door.direction
            if (direction == Direction.SOUTH && dir == Direction.WEST) {
                inside = true
            } else if (direction == Direction.EAST && dir == Direction.SOUTH) {
                inside = true
            } else if (direction == Direction.NORTH && dir == Direction.EAST) {
                inside = true
            }
            return inside
        }

        /**
         * Attempts to pick the lock on the door. If successful, the door is opened.
         * @param player The player attempting to pick the lock.
         * @param door The door being unlocked.
         */
        fun pickLock(player: Player, door: Scenery) {
            val success = RandomFunction.random(12) >= 4
            if (isInside(player, door.asScenery()) != flipped) {
                sendMessage(player, "The door is already unlocked.")
                return
            }
            if (getStatLevel(player, Skills.THIEVING) < level) {
                sendMessage(player, "You attempt to pick the lock.")
                val hit = RandomFunction.random(10) < 5
                impact(player, RandomFunction.random(1, 3), ImpactHandler.HitsplatType.NORMAL)
                sendMessage(player, if (hit) "You have activated a trap on the lock." else "You fail to pick the lock.", 1)
                return
            }
            if (isLockpick && !inInventory(player, Items.LOCKPICK_1523)) {
                sendMessage(player, "You need a lockpick in order to pick this lock.")
                return
            }
            if (success) {
                rewardXP(player, Skills.THIEVING, experience)
                handleAutowalkDoor(player, door.asScenery())
                escape(player)
            }
            sendMessage(player, "You attempt to pick the lock.")
            sendMessage(player, "You " + (if (success) "manage" else "fail") + " to pick the lock.", 1)
        }

        companion object {
            val DOOR_IDS = intArrayOf(Objects.DOOR_2550, Objects.DOOR_2551, Objects.DOOR_2554, Objects.DOOR_2555, Objects.DOOR_2556, Objects.DOOR_2557, Objects.DOOR_2558, Objects.DOOR_2559, Objects.DOOR_5501, Objects.DOOR_7246, Objects.DOOR_13314, Objects.DOOR_13317, Objects.DOOR_13320, Objects.DOOR_13323, Objects.DOOR_13326, Objects.DOOR_13344, Objects.DOOR_13345, Objects.DOOR_13346, Objects.DOOR_13347, Objects.DOOR_13348, Objects.DOOR_13349, Objects.DOOR_15759, Objects.DOOR_34005, Objects.DOOR_34805, Objects.DOOR_34806, Objects.DOOR_34812, Objects.CELL_DOOR_40186)
            fun forLocation(loc: Location): Doors? =
                values().firstOrNull { door -> door.locations.any { it == loc } }
        }

        private fun escape(player: Player) {
            if (getAttribute(player, "shantay-jail", false)) {
                removeAttribute(player, "shantay-jail")
                sendNPCDialogueWithDelay(
                    player, 2, NPCs.SHANTAY_836,
                    "You should be in jail! But if you get into more trouble, you'll be back."
                )
            }
        }
    }

    enum class Chests(val objectIds: IntArray, val level: Int, val xp: Double, val rewards: Array<Item>, val respawnTicks: Int) {
        TEN_COIN(intArrayOf(shared.consts.Scenery.CHEST_2566), 13, 7.8, arrayOf(Item(Items.COINS_995, 10)), 7),
        NATURE_RUNE(intArrayOf(shared.consts.Scenery.CHEST_2567), 28, 25.0, arrayOf(Item(Items.COINS_995, 3), Item(Items.NATURE_RUNE_561, 1)), 8),
        FIFTY_COIN(intArrayOf(shared.consts.Scenery.CHEST_2568), 43, 125.0, arrayOf(Item(Items.COINS_995, 50)), 55),
        STEEL_ARROWHEADS(intArrayOf(shared.consts.Scenery.CHEST_2573), 47, 150.0, arrayOf(Item(41, 5)), 210),
        BLOOD_RUNES(intArrayOf(shared.consts.Scenery.CHEST_2569), 59, 250.0, arrayOf(Item(Items.COINS_995, 500), Item(Items.BLOOD_RUNE_565, 2)), 135),
        PALADIN(intArrayOf(shared.consts.Scenery.CHEST_2570), 72, 500.0, arrayOf(Item(Items.COINS_995, 1000), Item(Items.RAW_SHARK_383, 1), Item(Items.ADAMANTITE_ORE_449, 1), Item(Items.UNCUT_SAPPHIRE_1623, 1)), 120);

        private var respawnUntil = 0

        val isRespawning: Boolean
            get() = ticks < respawnUntil

        fun markRespawn() {
            respawnUntil = ticks + (respawnTicks / 0.6).toInt()
        }

        /**
         * Opening without searching = trap damage.
         */
        fun open(player: Player, obj: Scenery) {
            if (isRespawning) {
                sendMessage(player, "It looks like this chest has already been looted.")
            } else {
                lock(player, 2)
                sendMessage(player, "You have activated a trap on the chest.")
                impact(player, hitDamage(player), HitsplatType.NORMAL)
            }
        }

        /**
         * Searching for traps
         */
        fun searchTraps(player: Player, scenery: Scenery) {
            player.faceLocation(scenery.location)

            if (isRespawning) {
                sendMessage(player, "It looks like this chest has already been looted.")
                return
            }

            if (getStatLevel(player, Skills.THIEVING) < level) {
                animate(player, Animations.HUMAN_OPEN_CHEST_536, false)
                lock(player, 2)
                sendMessage(player, "You search the chest for traps.")
                sendMessage(player, "You find nothing.", 1)
                return
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "Not enough inventory space.")
                return
            }

            lock(player, 6)
            animate(player, Animations.HUMAN_OPEN_CHEST_536, false)
            sendMessage(player, "You find a trap on the chest...")
            player.impactHandler.disabledTicks = 6

            queueScript(player, 0) {
                delayScript(player, 2)
                sendMessage(player, "You disable the trap.")

                delayScript(player, 2)
                animate(player, Animation.create(Animations.HUMAN_OPEN_CHEST_536))
                player.faceLocation(scenery.location)
                sendMessage(player, "You open the chest.")

                delayScript(player, 2)
                rewards.forEach { player.inventory.add(it, player) }
                sendMessage(player, "You find treasure inside!")
                rewardXP(player, Skills.THIEVING, xp)

                if (scenery.isActive) {
                    replaceScenery(scenery, 2574, 3)
                }

                markRespawn()
                stopExecuting(player)
            }
        }

        companion object {
            fun forId(id: Int): Chests? = values().firstOrNull { id in it.objectIds }
            fun hitDamage(player: Player): Int = maxOf(2, player.skills.lifepoints / 12)
            val OBJECT_IDS: IntArray = values().flatMap { it.objectIds.asList() }.toIntArray()
        }
    }
}

