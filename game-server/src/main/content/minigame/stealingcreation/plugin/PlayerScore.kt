package content.minigame.stealingcreation.plugin

/**
 * Represents the score of a player in the Stealing Creation mini-game.
 *
 * @param name the name of the player
 * @param team the team the player belongs to
 */
data class PlayerScore(
    val name: String,
    val team: Boolean,
) {
    private var gather = 0
    private var process = 0
    private var depositing = 0
    private var withdraw = 0
    private var damage = 0
    private var kills = 0
    private var deaths = 0

    /**
     * Updates the gathering score by the specified delta.
     * @param delta the change in gathering score
     */
    fun updateGathering(delta: Int) {
        gather += delta
    }

    /**
     * Updates the processing score by the specified delta.
     * @param delta the change in processing score
     */
    fun updateProcessing(delta: Int) {
        process += delta
    }

    /**
     * Updates the depositing score by the specified delta.
     * @param delta the change in depositing score
     */
    fun updateDepositing(delta: Int) {
        depositing += delta
    }

    /**
     * Updates the withdrawing score by the specified delta.
     * @param delta the change in withdrawing score
     */
    fun updateWithdrawing(delta: Int) {
        withdraw += delta
    }

    /**
     * Updates the damaging score by the specified delta.
     * @param delta the change in damaging score
     */
    fun updateDamaging(delta: Int) {
        damage += delta
    }

    /**
     * Updates the killed score by the specified delta.
     * @param delta the change in killed score
     */
    fun updateKilled(delta: Int) {
        kills += delta
    }

    /**
     * Updates the died score by the specified delta.
     * @param delta the change in died score
     */
    fun updateDied(delta: Int) {
        deaths += delta
    }

    /**
     * Calculates the total score, excluding kill and death calculations.
     * @param winBoost whether to apply a win boost to the total score
     * @return the total score
     */
    fun total(winBoost: Boolean): Int {
        var total = gather + process + ((depositing - withdraw) * 2) + damage
        if (winBoost) {
            total = (total * 1.1).toInt()
        }
        return total
    }

    companion object {
        /**
         * Calculates the total experience for a team.
         * @param playerScores the list of scores
         * @param team the team to calculate the experience for
         * @param winner whether the team is the winner
         * @return the total experience
         */
        fun totalXP(
            playerScores: List<PlayerScore>,
            team: Boolean,
            winner: Boolean,
        ): Int =
            playerScores
                .filter { it.team == team }
                .sumOf { it.total(winner) }

        /**
         * Finds the player with the highest total score.
         * @param playerScores the list of scores
         * @param winnerTeam the winner team (1 or 2)
         * @return the player with the highest total score
         */
        fun highestTotal(
            playerScores: List<PlayerScore>,
            winnerTeam: Int,
        ): PlayerScore? = playerScores.maxByOrNull { it.total(it.team == (winnerTeam == 1)) }

        /**
         * Finds the player with the lowest total score.
         * @param playerScores the list of scores
         * @param winnerTeam the winner team (1 or 2)
         * @return the player with the lowest total score
         */
        fun lowestTotal(
            playerScores: List<PlayerScore>,
            winnerTeam: Int,
        ): PlayerScore? = playerScores.minByOrNull { it.total(it.team == (winnerTeam == 1)) }

        /**
         * Finds the player with the most kills.
         * @param playerScores the list of scores
         * @return the player with the most kills
         */
        fun mostKills(playerScores: List<PlayerScore>): PlayerScore? = playerScores.maxByOrNull { it.kills }

        /**
         * Finds the player with the most deaths.
         * @param playerScores the list of scores
         * @return the player with the most deaths
         */
        fun mostDeaths(playerScores: List<PlayerScore>): PlayerScore? = playerScores.maxByOrNull { it.deaths }

        /**
         * Finds the player who gathered the most resources.
         * @param playerScores the list of scores
         * @return the player with the most gathered resources
         */
        fun mostGathered(playerScores: List<PlayerScore>): PlayerScore? = playerScores.maxByOrNull { it.gather }

        /**
         * Finds the player who processed the most resources.
         * @param playerScores the list of scores
         * @return the player with the most processed resources
         */
        fun mostProcessed(playerScores: List<PlayerScore>): PlayerScore? = playerScores.maxByOrNull { it.process }

        /**
         * Finds the player who deposited the most resources.
         * @param playerScores the list of scores
         * @return the player with the most deposited resources
         */
        fun mostDeposited(playerScores: List<PlayerScore>): PlayerScore? =
            playerScores.maxByOrNull {
                (
                    it.depositing -
                        it.withdraw
                ) *
                    2
            }

        /**
         * Finds the player who dealt the most damage.
         * @param playerScores the list of scores
         * @return the player with the most damage dealt
         */
        fun mostDamaged(playerScores: List<PlayerScore>): PlayerScore? = playerScores.maxByOrNull { it.damage }
    }
}
