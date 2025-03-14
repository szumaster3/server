package core.game.node.entity.combat.graves

import org.rs.consts.NPCs
import org.rs.consts.Quests

enum class GraveType(
    val npcId: Int,
    val cost: Int,
    val durationMinutes: Int,
    val isMembers: Boolean,
    val requiredQuest: String = "",
    val text: String,
) {
    MEM_PLAQUE(
        npcId = NPCs.GRAVE_MARKER_6565,
        cost = 0,
        durationMinutes = 2,
        isMembers = false,
        text = "In memory of @name,<br>who died here.",
    ),
    FLAG(
        npcId = NPCs.GRAVE_MARKER_6568,
        cost = 50,
        durationMinutes = 2,
        isMembers = false,
        text = MEM_PLAQUE.text,
    ),
    SMALL_GS(
        npcId = NPCs.GRAVESTONE_6571,
        cost = 500,
        durationMinutes = 2,
        isMembers = false,
        text = "In loving memory of our dear friend @name,<br>who died in this place @mins ago.",
    ),
    ORNATE_GS(
        npcId = NPCs.GRAVESTONE_6574,
        cost = 5000,
        durationMinutes = 3,
        isMembers = false,
        text = SMALL_GS.text,
    ),
    FONT_OF_LIFE(
        npcId = NPCs.GRAVESTONE_6577,
        cost = 50000,
        durationMinutes = 4,
        isMembers = true,
        text = "In your travels,<br>pause awhile to remember @name,<br>who passed away at this spot.",
    ),
    STELE(
        npcId = NPCs.STELE_6580,
        cost = 50000,
        durationMinutes = 4,
        isMembers = true,
        text = FONT_OF_LIFE.text,
    ),
    SARA_SYMBOL(
        npcId = NPCs.SARADOMIN_SYMBOL_6583,
        cost = 50000,
        durationMinutes = 4,
        isMembers = true,
        text = "@name,<br>an enlightened servant of Saradomin,<br>perished in this place.",
    ),
    ZAM_SYMBOL(
        npcId = NPCs.ZAMORAK_SYMBOL_6586,
        cost = 50000,
        durationMinutes = 4,
        isMembers = true,
        text = "@name,<br>a most bloodthirsty follower of Zamorak,<br>perished in this place.",
    ),
    GUTH_SYMBOL(
        npcId = NPCs.GUTHIX_SYMBOL_6589,
        cost = 50000,
        durationMinutes = 4,
        isMembers = true,
        text = "@name,<br>who walked with the Balance of Guthix,<br>perished in this place.",
    ),
    BAND_SYMBOL(
        npcId = NPCs.BANDOS_SYMBOL_6592,
        cost = 50000,
        durationMinutes = 4,
        isMembers = true,
        requiredQuest = Quests.LAND_OF_THE_GOBLINS,
        text = "@name,<br>a vicious warrior dedicated to Bandos,<br>perished in this place.",
    ),
    ARMA_SYMBOL(
        npcId = NPCs.ARMADYL_SYMBOL_6595,
        cost = 50000,
        durationMinutes = 4,
        isMembers = true,
        requiredQuest = Quests.TEMPLE_OF_IKOV,
        text = "@name,<br>a follower of the Law of Armadyl,<br>perished in this place.",
    ),
    ZARO_SYMBOL(
        npcId = NPCs.MEMORIAL_STONE_6598,
        cost = 50000,
        durationMinutes = 4,
        isMembers = true,
        requiredQuest = Quests.DESERT_TREASURE,
        text = "@name,<br>servant of the Unknown Power,<br>perished in this place.",
    ),
    ANGEL_DEATH(
        npcId = NPCs.MEMORIAL_STONE_6601,
        cost = 500000,
        durationMinutes = 5,
        isMembers = true,
        text = "Ye frail mortals who gaze upon this sight,<br>forget not the fate of @name, once mighty, now<br>surrendered to the inescapable grasp of destiny.<br><i>Requires cat in pace.</i>",
    ),
    ;

    companion object {
        val ids =
            values()
                .fold(ArrayList<Int>()) { list, type ->
                    list.add(type.npcId)
                    list.add(type.npcId + 1)
                    list.add(type.npcId + 2)
                    list
                }.toIntArray()
    }
}
