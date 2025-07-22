package content.minigame.gnomeball

/*class GnomeBallSession : LogoutListener, MapArea {
    var players: ArrayList<Player> = ArrayList()

    override fun logout(player: Player) {
        val session = player.getAttribute<GnomeBallSession>("gb-session", null) ?: return
        teleport(player, location(2382, 3488, 0))
        session.players.remove(player)
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        val colA1 = ZoneBorders(2383, 3488, 2383, 3488)
        val colA2 = ZoneBorders(2384, 3487, 2384, 3489)
        val colA3 = ZoneBorders(2385, 3485, 2385, 3491)
        val colA4 = ZoneBorders(2386, 3484, 2386, 3492)
        val colA5 = ZoneBorders(2387, 3483, 2387, 3493)
        val colA6 = ZoneBorders(2388, 3482, 2388, 3494)
        val rect1 = ZoneBorders(2389, 3481, 2392, 3495)
        val colB1 = ZoneBorders(2393, 3482, 2393, 3494)
        val colB2 = ZoneBorders(2394, 3493, 2394, 3493)
        val rect2 = ZoneBorders(2395, 3484, 2397, 3492)
        val colC1 = ZoneBorders(2398, 3493, 2398, 3493)
        val colC2 = ZoneBorders(2399, 3492, 2399, 3494)
        val rect3 = ZoneBorders(2400, 3481, 2403, 3495)
        return arrayOf(colA1, colA2, colA3, colA4, colA5, colA6, rect1, colB1, colB2, rect2, colC1, colC2, rect3)
    }

    fun start(list: ArrayList<Player>) {
        for (player in list) {
            setAttribute(player, "gb-session", this)
        }
    }
}
*/
