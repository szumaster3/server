package content.minigame.gnomecook.handlers

import org.rs.consts.NPCs

enum class GnomeCookingTask(
    val level: GnomeCookingTipper.LEVEL,
    val npc_id: Int,
    val tip: String,
) {
    CPT_ERRDO(GnomeCookingTipper.LEVEL.EASY, NPCs.CAPTAIN_ERRDO_3811, "at the top level of the Grand Tree."),
    DALILAH(GnomeCookingTipper.LEVEL.EASY, NPCs.DALILA_4588, "sitting in the Gnome Restaurant."),
    GULLUCK(GnomeCookingTipper.LEVEL.EASY, NPCs.GULLUCK_602, "on the third level of the Grand Tree."),
    ROMETTI(GnomeCookingTipper.LEVEL.EASY, NPCs.ROMETTI_601, "on the second level of the Grand Tree."),
    NARNODE(GnomeCookingTipper.LEVEL.EASY, NPCs.KING_NARNODE_SHAREEN_670, "at the base of the Grand Tree."),
    MEEGLE(GnomeCookingTipper.LEVEL.EASY, NPCs.MEEGLE_4597, "in the terrorbird enclosure."),
    PERRDUR(GnomeCookingTipper.LEVEL.EASY, NPCs.PERRDUR_4587, "sitting in the Gnome Restaurant."),
    SARBLE(GnomeCookingTipper.LEVEL.EASY, NPCs.SARBLE_4599, "in the swamp west of the Grand Tree."),
    GIMLEWAP(GnomeCookingTipper.LEVEL.HARD, NPCs.AMBASSADOR_GIMBLEWAP_4580, "upstairs in Ardougne castle."),
    BLEEMADGE(GnomeCookingTipper.LEVEL.HARD, NPCs.CAPTAIN_BLEEMADGE_3810, "at the top of White Wolf Mountain."),
    DALBUR(GnomeCookingTipper.LEVEL.HARD, NPCs.CAPTAIN_DALBUR_3809, "by the gnome glider in Al Kharid"),
    BOLREN(GnomeCookingTipper.LEVEL.HARD, NPCs.KING_BOLREN_469, "next to the Spirit Tree in Tree Gnome Village"),
    SCHEPBUR(GnomeCookingTipper.LEVEL.HARD, NPCs.LIEUTENANT_SCHEPBUR_3817, "in the battlefield of Khazar, south of the river."),
    IMBLEWYN(GnomeCookingTipper.LEVEL.HARD, NPCs.PROFESSOR_IMBLEWYN_4586, "on the ground floor of the Magic Guild."),
    ONGLEWIP(GnomeCookingTipper.LEVEL.HARD, NPCs.PROFESSOR_ONGLEWIP_4585, "in the Wizard's Tower south of Draynor."),
    // https://runescape.wiki/w/Captain_Ninto
    // https://runescape.wiki/w/Captain_Daerkin
}
