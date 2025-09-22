package content.minigame.blastfurnace.plugin

import com.google.gson.JsonObject
import content.global.skill.smithing.Bar
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs

/**
 * Blast Furnace minigame.
 */
class BlastFurnace : MapArea, PersistPlayer, TickListener {
    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(bfArea)

    override fun savePlayer(player: Player, save: JsonObject) {
        val state = playerStates[player.details.uid]
        if (state != null) {
            save.add("bf-state", state.toJson())
        }
    }

    override fun parsePlayer(player: Player, data: JsonObject) {
        playerStates.remove(player.details.uid)
        if (data.has("bf-state")) {
            val stateObj = data.getAsJsonObject("bf-state")
            getPlayerState(player).readJson(stateObj)
        }
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            playersInArea.add(entity)
            val state = getPlayerState(entity)
            for (ore in state.oresOnBelt) ore.createNpc()
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            playersInArea.remove(entity)
            val state = getPlayerState(entity)
            for (ore in state.oresOnBelt) {
                ore.npcInstance?.clear()
                ore.npcInstance = null
            }
        }
    }

    override fun tick() {
        if (state.beltBroken || state.cogBroken) pedaler = null
        if (state.potPipeBroken || state.pumpPipeBroken) pumper = null
        state.tick(pumper != null, pedaler != null)

        if (playersInArea.size > 0) {
            updateVisuals()
            if (getWorldTicks() % 2 == 0) {
                updatePedaler()
                updatePumper()
            }
            processBars()
        }

        pumper = null
        pedaler = null
    }

    /**
     * Handles pumper logic (XP, damage if overheated).
     */
    private fun updatePumper() {
        if (pumper != null) {
            if (state.stoveTemp == 0) return
            if (state.furnaceTemp == 100 && RandomFunction.roll(5)) {
                impact(pumper!!, (0.2 * pumper!!.skills.maximumLifepoints).toInt())
                sendMessage(pumper!!, "A blast of hot air cooks you a bit.")
                pumper!!.pulseManager.clear()
                return
            }
            rewardXP(pumper!!, Skills.STRENGTH, 4.0)
        }
    }

    /**
     * Handles pedaler logic (XP, stamina drain, belt movement).
     */
    private fun updatePedaler() {
        if (pedaler == null) return
        var oresPedaled = false
        for (state in playerStates.values) {
            if (state.oresOnBelt.isEmpty()) continue
            state.updateOres()
            oresPedaled = true
        }
        if (oresPedaled) {
            rewardXP(pedaler!!, Skills.AGILITY, 2.0)
            pedaler!!.settings.runEnergy -= 2
        }
    }

    /**
     * Updates furnace scenery visuals (pipes, belt, stove, animations).
     */
    private fun updateVisuals() {
        sceneryController.updateStove(state.stoveTemp)
        sceneryController.updateBreakable(
            state.potPipeBroken,
            state.pumpPipeBroken,
            state.beltBroken,
            state.cogBroken,
        )
        sceneryController.updateAnimations(pedaler != null, state.beltBroken, state.cogBroken)
    }

    /**
     * Converts ores into bars if furnace is at optimal temperature.
     */
    private fun processBars() {
        if (state.furnaceTemp !in 51..66) return
        var totalProcessed = 0
        for (player in playersInArea) {
            if (getPlayerState(player).processOresIntoBars()) totalProcessed++
        }
        if (totalProcessed == 0) return
        for (player in playersInArea) rewardXP(player, Skills.SMITHING, totalProcessed.toDouble())
    }

    companion object {
        /**
         * Furnace area borders.
         */
        val bfArea = ZoneBorders(1934, 4955, 1958, 4975)

        /**
         * Players currently inside the area.
         */
        val playersInArea = ArrayList<Player>()

        /**
         * Tracks Blast Furnace state per player.
         */
        val playerStates = HashMap<Int, BFPlayerState>()

        /**
         * Shared machine state (temperatures, pipes, belts, etc.).
         */
        val state = BlastState()

        /**
         * Controls furnace-related scenery updates.
         */
        val sceneryController = BFSceneryController()

        /**
         * Player currently pedaling the conveyor.
         */
        var pedaler: Player? = null

        /**
         * Player currently pumping the furnace.
         */
        var pumper: Player? = null

        /**
         * Checks if a player is within the minigame area.
         */
        fun insideBorders(player: Player): Boolean = bfArea.insideBorder(player.location)

        /**
         * Places ores from player inventory onto the belt.
         *
         * @param id The ore id.
         * @param accountForSkill The smithing level.
         */
        fun placeAllOre(
            p: Player,
            id: Int = -1,
            accountForSkill: Boolean = false,
        ) {
            val oreCounts = HashMap<Int, Int>()
            val oreContainer = getOreContainer(p)
            val level = if (accountForSkill) getStatLevel(p, Skills.SMITHING) else 99

            for (item in p.inventory.toArray()) {
                if (item == null) continue
                if (getNpcForOre(item.id) == -1) continue
                if (id != -1 && item.id != id) continue

                val bar = getBarForOreId(item.id, oreContainer.coalAmount(), level)!!
                if (bar.level > level) continue

                oreCounts[item.id] = (oreCounts[item.id] ?: 0) + item.amount
            }

            for ((oreId, amount) in oreCounts) {
                var maxAmt = oreContainer.getAvailableSpace(oreId, level)

                if (oreId == Items.COPPER_ORE_436 || oreId == Items.TIN_ORE_438) {
                    maxAmt += (BlastUtils.ORE_LIMIT - getAmountOnBelt(
                        p,
                        oreId,
                    ))
                }

                if (oreId == Items.COAL_453) {
                    maxAmt -= getAmountOnBelt(p, oreId)
                } else {
                    maxAmt -= getTotalOreOnBelt(p)
                }

                maxAmt = maxAmt.coerceAtMost(amount).coerceAtLeast(0)
                if (maxAmt == 0) continue

                if (removeItem(p, Item(oreId, maxAmt))) addOreToBelt(p, oreId, maxAmt)
            }
        }

        /**
         * Gets or creates Blast Furnace state for player.
         */
        fun getPlayerState(p: Player): BFPlayerState {
            if (playerStates[p.details.uid] != null) return playerStates[p.details.uid]!!
            val state = BFPlayerState(p)
            playerStates[p.details.uid] = state
            return state
        }

        /**
         * Gets the ore container for player.
         */
        fun getOreContainer(p: Player): BFOreContainer = getPlayerState(p).container

        /**
         * Adds ore entity to the conveyor belt.
         */
        fun addOreToBelt(p: Player, id: Int, amount: Int): BFBeltOre {
            val beltOre = BFBeltOre(p, id, amount, BFBeltOre.ORE_START_LOC)
            beltOre.createNpc()
            getPlayerState(p).oresOnBelt.add(beltOre)
            return beltOre
        }

        /**
         * Gets the total amount of a ore type on belt.
         */
        fun getAmountOnBelt(p: Player, id: Int): Int {
            var total = 0
            for (ore in getPlayerState(p).oresOnBelt) {
                if (ore.id == id) total += ore.amount
            }
            return total
        }

        /**
         * Gets the total amount of all ores (except coal) on p belt.
         */
        fun getTotalOreOnBelt(p: Player): Int {
            var total = 0
            for (ore in getPlayerState(p).oresOnBelt) if (ore.id != Items.COAL_453) total += ore.amount
            return total
        }

        /**
         * Calculates coal required for a given bar type.
         */
        fun getNeededCoal(bar: Bar): Int {
            var coalAmount = 0

            if (bar.ores.size == 1) return coalAmount

            for (ore in bar.ores) {
                if (ore.id == Items.COAL_453) {
                    coalAmount = ore.amount
                    break
                }
            }
            if (coalAmount > 1) coalAmount /= 2
            return coalAmount
        }

        /**
         * Gets the bar type for a given ore id.
         */
        fun getBarForOreId(id: Int, coalAmount: Int, level: Int): Bar? = when (id) {
            Items.COPPER_ORE_436, Items.TIN_ORE_438 -> Bar.BRONZE
            Items.IRON_ORE_440 -> if (coalAmount >= 1 && level >= Bar.STEEL.level) Bar.STEEL else Bar.IRON
            Items.PERFECT_GOLD_ORE_446 -> Bar.PERFECT_GOLD
            else -> Bar.forOre(id)
        }

        /**
         * Gets the NPC representing an ore rock on the belt.
         */
        fun getNpcForOre(id: Int): Int = when (id) {
            Items.TIN_ORE_438 -> NPCs.TIN_ORE_2554
            Items.COPPER_ORE_436 -> NPCs.COPPER_ORE_2555
            Items.IRON_ORE_440 -> NPCs.IRON_ORE_2556
            Items.MITHRIL_ORE_447 -> NPCs.MITHRIL_ORE_2557
            Items.ADAMANTITE_ORE_449 -> NPCs.ADAMANTITE_ORE_2558
            Items.RUNITE_ORE_451 -> NPCs.RUNITE_ORE_2559
            Items.SILVER_ORE_442 -> NPCs.SILVER_ORE_2560
            Items.GOLD_ORE_444 -> NPCs.GOLD_ORE_2561
            Items.COAL_453 -> NPCs.COAL_2562
            Items.PERFECT_GOLD_ORE_446 -> NPCs.PERFECT_GOLD_ORE_2563
            else -> -1
        }

        /**
         * Entrance fee calculation.
         * +Charos ring
         * +Smithing level
         */
        fun getEntranceFee(hasCharos: Boolean, smithLevel: Int): Int {
            if (smithLevel >= BlastUtils.SMITH_REQ) return 0
            return if (hasCharos) BlastUtils.ENTRANCE_FEE / 2 else BlastUtils.ENTRANCE_FEE
        }

        /**
         * Start minigame.
         */
        fun enter(player: Player, feePaid: Boolean) {
            if (feePaid && !hasTimerActive<BFTempEntranceTimer>(player)) registerTimer(player, BFTempEntranceTimer())
            teleport(player, BlastUtils.ENTRANCE_LOC)
        }
    }
}
