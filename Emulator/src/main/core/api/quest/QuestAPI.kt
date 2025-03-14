package core.api.quest

import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.node.entity.player.link.quest.QuestReq
import core.game.node.entity.player.link.quest.QuestRequirements

/**
 * Retrieves the total number of quest points a player has earned.
 *
 * @param player The player whose quest points are being retrieved.
 * @return The total quest points the player has earned.
 */
fun getQuestPoints(player: Player): Int {
    return player.questRepository.points
}

/**
 * Retrieves the current stage of a specific quest for a player.
 *
 * @param player The player whose quest stage is being retrieved.
 * @param quest The name of the quest.
 * @return The stage of the quest, or 0 if the quest is not started.
 */
fun getQuestStage(
    player: Player,
    quest: String,
): Int {
    return player.questRepository.getStage(quest)
}

/**
 * Sets the stage of a specific quest for a player.
 *
 * @param player The player whose quest stage is being set.
 * @param quest The name of the quest.
 * @param stage The new stage to set for the quest.
 */
fun setQuestStage(
    player: Player,
    quest: String,
    stage: Int,
) {
    player.questRepository.setStage(QuestRepository.getQuests()[quest]!!, stage)
    player.questRepository.syncronizeTab(player)
}

/**
 * Updates the quest tab to reflect the latest quest information for a player.
 *
 * @param player The player whose quest tab is being updated.
 */
fun updateQuestTab(player: Player) {
    player.questRepository.syncronizeTab(player)
}

/**
 * Checks if a quest is in progress for a player within a specified stage range.
 *
 * @param player The player whose quest status is being checked.
 * @param quest The name of the quest.
 * @param startStage The starting stage of the quest to check.
 * @param endStage The ending stage of the quest to check.
 * @return True if the player's quest is within the specified range, otherwise false.
 */
fun isQuestInProgress(
    player: Player,
    quest: String,
    startStage: Int,
    endStage: Int,
): Boolean {
    return player.questRepository.getStage(quest) in startStage..endStage
}

/**
 * Checks if a quest is complete for a player (stage 100).
 *
 * @param player The player whose quest completion status is being checked.
 * @param quest The name of the quest.
 * @return True if the quest is complete, otherwise false.
 */
fun isQuestComplete(
    player: Player,
    quest: String,
): Boolean {
    return player.questRepository.getStage(quest) == 100
}

/**
 * Retrieves the quest object for a player.
 *
 * @param player The player whose quest is being retrieved.
 * @param quest The name of the quest.
 * @return The Quest object representing the quest for the player.
 */
fun getQuest(
    player: Player,
    quest: String,
): Quest {
    return player.questRepository.getQuest(quest)
}

/**
 * Starts a quest for a player if they meet the necessary requirements.
 *
 * @param player The player who is starting the quest.
 * @param quest The name of the quest to start.
 * @return True if the quest was successfully started, false if the player doesn't meet the requirements.
 */
fun startQuest(
    player: Player,
    quest: String,
): Boolean {
    val quest = player.questRepository.getQuest(quest)
    val canStart = quest.hasRequirements(player)
    if (!canStart) return false
    quest.start(player)
    return true
}

/**
 * Finishes a quest for a player, marking it as complete.
 *
 * @param player The player who is finishing the quest.
 * @param quest The name of the quest to finish.
 */
fun finishQuest(
    player: Player,
    quest: String,
) {
    player.questRepository.getQuest(quest).finish(player)
}

/**
 * Checks if a player has completed a specific quest before allowing them to proceed with another quest.
 * Sends a message to the player if they haven't completed the required quest.
 *
 * @param player The player whose quest completion status is being checked.
 * @param questName The name of the required quest.
 * @param message The message to send to the player if they have not completed the required quest.
 * @return True if the player has completed the required quest, otherwise false.
 */
fun requireQuest(
    player: Player,
    questName: String,
    message: String,
): Boolean {
    if (!isQuestComplete(player, questName)) {
        sendMessage(player, "You must have completed the $questName quest. $message")
        return false
    }
    return true
}

/**
 * Checks if a player has met the requirements to start a specific quest.
 *
 * @param player The player whose quest requirements are being checked.
 * @param quest The name of the quest.
 * @param message If true, a message will be sent to the player if they do not meet the requirements.
 * @return True if the player meets the requirements for the quest, otherwise false.
 */
@JvmOverloads
fun hasRequirement(
    player: Player,
    quest: String,
    message: Boolean = true,
): Boolean {
    val questReq = QuestRequirements.values().filter { it.questName.equals(quest, true) }.firstOrNull() ?: return false
    return core.api.hasRequirement(player, QuestReq(questReq), message)
}

private class QuestAPI
