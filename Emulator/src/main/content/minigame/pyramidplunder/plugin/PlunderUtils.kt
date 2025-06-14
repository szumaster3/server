package content.minigame.pyramidplunder.plugin

import core.api.*
import core.game.component.Component
import core.game.dialogue.FaceAnim
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Utility functions for [PyramidPlunderMinigame].
 */
object PlunderUtils {

    /**
     * Checks if the player is currently in a Pyramid Plunder game.
     *
     * @param player The player to check.
     * @return True if the player is in the game, false otherwise.
     */
    fun hasPlayer(player: Player): Boolean = PlunderData.playerLocations[player] != null

    /**
     * Registers a player into the Pyramid Plunder game.
     *
     * @param player The player to register.
     */
    fun registerPlayer(player: Player) {
        PlunderData.players.add(player)
        PlunderData.timeLeft[player] = 500
    }

    /**
     * Unregisters a player from the Pyramid Plunder game.
     *
     * @param player The player to unregister.
     */
    fun unregisterPlayer(player: Player) {
        PlunderData.players.remove(player)
        PlunderData.playerLocations.remove(player)
        PlunderData.timeLeft.remove(player)
        removeAttribute(player, "pyramid-entrance")
    }

    /**
     * Expels a player from the Pyramid Plunder game, teleporting them to the entrance.
     *
     * @param player The player to expel.
     * @param summonMummy Whether the guardian mummy should be summoned.
     */
    fun expel(player: Player, summonMummy: Boolean) {
        teleport(player, Location.create(3288, 2802, 0))
        unregisterPlayer(player)
        resetOverlay(player)
        resetObjectVarbits(player)

        if (!summonMummy) {
            sendMessage(player, "You've run out of time and the guardian mummy has thrown you out.")
        } else {
            sendMessage(player, "You've been expelled by the guardian mummy.")
        }
    }

    /**
     * Decrements the time remaining for all players and returns those whose time is up.
     *
     * @return A list of players whose time has run out.
     */
    fun decrementTimeRemaining(): ArrayList<Player> {
        val timesUp = ArrayList<Player>()
        PlunderData.timeLeft.forEach { (player, left) ->
            if (left <= 0) timesUp.add(player).also {
                sendNPCDialogueLines(
                    player,
                    NPCs.GUARDIAN_MUMMY_4476,
                    FaceAnim.OLD_NOT_INTERESTED,
                    false,
                    "You've had your five minutes of plundering. Now be",
                    "off with you!"
                )
                sendMessage(player, "Your time is up...")
            }
            PlunderData.timeLeft[player] = left - 1
            updateOverlay(player)
        }
        return timesUp
    }

    /**
     * Loads the next room for the player in the Pyramid Plunder minigame.
     *
     * @param player The player whose next room to load.
     */
    fun loadNextRoom(player: Player) {
        val current = PlunderData.playerLocations[player]

        val next: PlunderRoom = if (current == null) {
            getRoom(1)
        } else {
            getRoom(current.room + 1)
        }

        if (PlunderData.playerLocations.filter { it.value == next }.isEmpty()) {
            PlunderData.doors[next.room - 1] = PlunderData.doorVarbits.random()
        }

        teleport(player, next.entrance)
        PlunderData.playerLocations[player] = next
    }

    /**
     * Gets the Plunder room by its number.
     *
     * @param number The room number.
     * @return The corresponding PlunderRoom object.
     */
    fun getRoom(number: Int): PlunderRoom = PlunderData.rooms[number - 1]

    /**
     * Gets the current Plunder room of the player.
     *
     * @param player The player whose room to retrieve.
     * @return The PlunderRoom the player is in, or null if not found.
     */
    fun getRoom(player: Player): PlunderRoom? = PlunderData.playerLocations[player]

    /**
     * Gets the level of the Plunder room the player is currently in.
     *
     * @param player The player whose room level to retrieve.
     * @return The room level.
     */
    fun getRoomLevel(player: Player): Int = 11 + (10 * getRoom(player)!!.room)

    /**
     * Calculates the destination of the spear based on the player's current room.
     *
     * @param player The player whose spear destination to calculate.
     * @return The location of the spear.
     */
    fun getSpearDestination(player: Player): Location {
        val room = getRoom(player)!!
        return when (room.spearDirection) {
            Direction.NORTH -> player.location.transform(0, 3, 0)
            Direction.SOUTH -> player.location.transform(0, -3, 0)
            Direction.WEST -> player.location.transform(-3, 0, 0)
            Direction.EAST -> player.location.transform(3, 0, 0)
            else -> player.location
        }
    }

    /**
     * Gets the experience reward for urns in the current room.
     *
     * @param player The player whose urn XP to retrieve.
     * @param check Whether to check for the specific urn XP or calculate the general XP.
     * @return The XP reward.
     */
    fun getUrnXp(player: Player, check: Boolean): Double {
        val room = getRoom(player)!!.room
        return if (check) {
            when (room) {
                1 -> 20.0
                2 -> 30.0
                3 -> 50.0
                4 -> 70.0
                5 -> 100.0
                6 -> 150.0
                7 -> 225.0
                8 -> 275.0
                else -> 0.0
            }
        } else {
            when (room) {
                1 -> 60.0
                2 -> 90.0
                3 -> 150.0
                4 -> 215.0
                5 -> 300.0
                6 -> 450.0
                7 -> 675.0
                8 -> 825.0
                else -> 0.0
            }
        }
    }

    /**
     * Resets the object varbits for the player.
     *
     * @param player The player whose varbits to reset.
     */
    fun resetObjectVarbits(player: Player) {
        setVarp(player, 821, 0)
        setVarp(player, 820, 0)
        PlunderData.doorVarbits.forEach { setVarbit(player, it, 0) }
    }

    /**
     * Opens the Pyramid Plunder overlay interface for the player.
     *
     * @param player The player to open the overlay for.
     */
    fun openOverlay(player: Player) {
        player.interfaceManager.openOverlay(Component(Components.NTK_OVERLAY_428))
        updateOverlay(player)
    }

    /**
     * Updates the Pyramid Plunder overlay interface for the player.
     *
     * @param player The player whose overlay to update.
     */
    fun updateOverlay(player: Player) {
        setVarbit(player, 2375, 500 - (PlunderData.timeLeft[player] ?: 0))
        setVarbit(player, 2376, 11 + (getRoom(player)!!.room * 10))
        setVarbit(player, 2377, getRoom(player)!!.room)
    }

    /**
     * Resets the Pyramid Plunder overlay for the player.
     *
     * @param player The player whose overlay to reset.
     */
    fun resetOverlay(player: Player) {
        setVarbit(player, 2375, 0)
        player.packetDispatch.resetInterface(Components.NTK_OVERLAY_428)
        player.interfaceManager.closeOverlay()
    }

    /**
     * Retrieves the door ID for the player's current room.
     *
     * @param player The player whose door ID to retrieve.
     * @return The door ID.
     */
    fun getDoor(player: Player): Int {
        if (getRoom(player) == null) return -1
        val room = getRoom(player)!!.room
        return PlunderData.doors[room - 1]
    }

    /**
     * Rolls for a Pharaoh's Sceptre for the player.
     *
     * @param player The player to roll the sceptre for.
     * @return True if the player rolled for the sceptre, false otherwise.
     */
    fun rollSceptre(player: Player): Boolean {
        val room = getRoom(player)!!.room
        val chance = when (room) {
            1 -> 1500
            2 -> 1350
            3 -> 1250
            4 -> 1150
            else -> 1000
        }

        if (RandomFunction.roll(chance)) {
            expel(player, true)

            runTask(player, 2) {
                if (freeSlots(player) == 0) {
                    player.dialogueInterpreter.sendItemMessage(
                        Items.PHARAOHS_SCEPTRE_9050,
                        "You find a golden sceptre but don't have space to",
                        "carry the sceptre. Clear some space in your pack and",
                        "pick the sceptre up, if you want it..."
                    )
                }
                addItemOrDrop(player, Items.PHARAOHS_SCEPTRE_9050)
            }
            return true
        }
        return false
    }

    /**
     * Retrieves the experience reward for the Sarcophagus in the current room.
     *
     * @param player The player whose sarcophagus XP to retrieve.
     * @return The XP reward.
     */
    fun getSarcophagusXp(player: Player): Double {
        val room = getRoom(player)!!.room
        return when (room) {
            1 -> 20.0
            2 -> 30.0
            3 -> 50.0
            4 -> 70.0
            5 -> 100.0
            6 -> 150.0
            7 -> 225.0
            8 -> 275.0
            else -> 0.0
        }
    }

    /**
     * Retrieves the experience reward for the Chest in the current room.
     *
     * @param player The player whose chest XP to retrieve.
     * @return The XP reward.
     */
    fun getChestXp(player: Player): Double {
        if (getRoom(player) == null) {
            expel(player, false)
            return 0.0
        }
        val room = getRoom(player)!!.room
        return when (room) {
            1 -> 60.0
            2 -> 90.0
            3 -> 150.0
            4 -> 215.0
            5 -> 300.0
            6 -> 450.0
            7 -> 675.0
            8 -> 825.0
            else -> 0.0
        } * 0.66
    }

    /**
     * Retrieves the experience reward for the Door in the current room.
     *
     * @param player The player whose door XP to retrieve.
     * @param lockpick Whether the lockpick is used for the door.
     * @return The XP reward.
     */
    fun getDoorXp(player: Player, lockpick: Boolean): Double {
        val room = getRoom(player)?.room ?: return 0.0
        var reward = when (room) {
            1 -> 60.0
            2 -> 90.0
            3 -> 150.0
            4 -> 215.0
            5 -> 300.0
            6 -> 450.0
            7 -> 675.0
            8 -> 825.0
            else -> 0.0
        } * 0.66

        if (lockpick) reward /= 2.0

        return reward
    }

    /**
     * Rolls for an artifact for the player.
     *
     * @param player The player to roll for the artifact.
     * @param tier The artifact tier.
     * @return The artifact ID.
     */
    fun rollArtifact(player: Player, tier: Int): Int {
        val room = getRoom(player)!!.room
        val divisor = (room * 2) * (tier * 35)
        val goldRate = divisor / 650.0
        val stoneRate = divisor / 250.0

        val roll = RandomFunction.RANDOM.nextDouble()
        if (goldRate > roll) return PlunderData.artifacts[2].random()
        if (stoneRate > roll) return PlunderData.artifacts[1].random()
        return PlunderData.artifacts[0].random()
    }

    /**
     * Checks whether the entrance switch can be changed.
     */
    fun checkEntranceSwitch() {
        if (System.currentTimeMillis() > PlunderData.nextEntranceSwitch) {
            PlunderData.currentEntrance = PlunderData.pyramidEntranceVarbits.random()
            PlunderData.nextEntranceSwitch = System.currentTimeMillis() + (60000 * 15)
        }
    }

    /**
     * Checks if the entrance door matches the current entrance.
     *
     * @param door The door to check.
     * @return True if the entrance matches, false otherwise.
     */
    fun checkEntrance(door: Node): Boolean = door.asScenery().definition.varbitID == PlunderData.currentEntrance

    fun rollUrnSuccess(
        player: Player,
        charmed: Boolean = false,
    ): Boolean {
        val level = getDynLevel(player, Skills.THIEVING)

        if (getRoom(player) == null) {
            return false
        }

        val room = getRoom(player)!!.room
        return RandomFunction.random(level) > (room * if (charmed) 2 else 4)
    }
}
