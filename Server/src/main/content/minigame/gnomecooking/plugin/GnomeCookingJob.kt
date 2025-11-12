package content.minigame.gnomecooking.plugin

import shared.consts.NPCs

enum class GnomeCookingJob(val level: GnomeTipper.LEVEL, val npc_id: Int, val tip: String) {
    ERRDO(GnomeTipper.LEVEL.EASY, NPCs.CAPTAIN_ERRDO_3811, "at the top level of the Grand Tree."),
    DALILAH(GnomeTipper.LEVEL.EASY, NPCs.DALILA_4588, "sitting in the Gnome Restaurant."),
    GULLUCK(GnomeTipper.LEVEL.EASY, NPCs.GULLUCK_602, "on the third level of the Grand Tree."),
    ROMETTI(GnomeTipper.LEVEL.EASY, NPCs.ROMETTI_601, "on the second level of the Grand Tree."),
    NARNODE(GnomeTipper.LEVEL.EASY, NPCs.KING_NARNODE_SHAREEN_670, "at the base of the Grand Tree."),
    MEEGLE(GnomeTipper.LEVEL.EASY, NPCs.MEEGLE_4597, "in the terrorbird enclosure."),
    PERRDUR(GnomeTipper.LEVEL.EASY, NPCs.PERRDUR_4587, "sitting in the Gnome Restaurant."),
    SARBLE(GnomeTipper.LEVEL.EASY, NPCs.SARBLE_4599, "in the swamp west of the Grand Tree."),
    GIMLEWAP(GnomeTipper.LEVEL.HARD, NPCs.AMBASSADOR_GIMBLEWAP_4580, "upstairs in Ardougne castle."),
    BLEEMADGE(GnomeTipper.LEVEL.HARD, NPCs.CAPTAIN_BLEEMADGE_3810, "at the top of White Wolf Mountain."),
    DALBUR(GnomeTipper.LEVEL.HARD, NPCs.CAPTAIN_DALBUR_3809, "by the gnome glider in Al Kharid"),
    BOLREN(GnomeTipper.LEVEL.HARD, NPCs.KING_BOLREN_469, "next to the Spirit Tree in Tree Gnome Village"),
    SCHEPBUR(GnomeTipper.LEVEL.HARD, NPCs.LIEUTENANT_SCHEPBUR_3817, "in the battlefield of Khazar, south of the river."),
    IMBLEWYN(GnomeTipper.LEVEL.HARD, NPCs.PROFESSOR_IMBLEWYN_4586, "on the ground floor of the Magic Guild."),
    ONGLEWIP(GnomeTipper.LEVEL.HARD, NPCs.PROFESSOR_ONGLEWIP_4585, "in the Wizard's Tower south of Draynor.")
}