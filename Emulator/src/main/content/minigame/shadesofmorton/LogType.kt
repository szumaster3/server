package content.minigame.shadesofmorton

import org.rs.consts.NPCs

enum class LogType(
    val doses: Int,
    val shadesID: List<Int>,
    val requiredLevel: Int,
    val experience: Double,
) {
    REGULAR(
        doses = 2,
        shadesID = listOf(NPCs.LOAR_SHADE_1241, NPCs.PHRIN_SHADE_1244),
        requiredLevel = 5,
        experience = 50.0,
    ),
    OAK(
        doses = 2,
        shadesID = listOf(NPCs.LOAR_SHADE_1241, NPCs.PHRIN_SHADE_1244),
        requiredLevel = 20,
        experience = 70.0,
    ),
    WILLOW(
        doses = 3,
        shadesID = listOf(NPCs.LOAR_SHADE_1241, NPCs.PHRIN_SHADE_1244, NPCs.RIYL_SHADE_1246),
        requiredLevel = 35,
        experience = 100.0,
    ),
    TEAK(
        doses = 3,
        shadesID = listOf(NPCs.LOAR_SHADE_1241, NPCs.PHRIN_SHADE_1244, NPCs.RIYL_SHADE_1246),
        requiredLevel = 40,
        experience = 120.0,
    ),
    ARCTIC_PINE(
        doses = 3,
        shadesID = listOf(NPCs.LOAR_SHADE_1241, NPCs.PHRIN_SHADE_1244, NPCs.RIYL_SHADE_1246),
        requiredLevel = 47,
        experience = 158.0,
    ),
    MAPLE(
        doses = 3,
        shadesID = listOf(NPCs.LOAR_SHADE_1241, NPCs.PHRIN_SHADE_1244, NPCs.RIYL_SHADE_1246),
        requiredLevel = 50,
        experience = 175.0,
    ),
    MAHOGANY(
        doses = 3,
        shadesID = listOf(NPCs.LOAR_SHADE_1241, NPCs.PHRIN_SHADE_1244, NPCs.RIYL_SHADE_1246),
        requiredLevel = 55,
        experience = 210.0,
    ),
    EUCALYPTUS(
        doses = 4,
        shadesID = listOf(NPCs.LOAR_SHADE_1241, NPCs.PHRIN_SHADE_1244, NPCs.RIYL_SHADE_1246, NPCs.ASYN_SHADE_1248),
        requiredLevel = 63,
        experience = 246.0,
    ),
    YEW(
        doses = 4,
        shadesID = listOf(NPCs.LOAR_SHADE_1241, NPCs.PHRIN_SHADE_1244, NPCs.RIYL_SHADE_1246, NPCs.ASYN_SHADE_1248),
        requiredLevel = 65,
        experience = 255.0,
    ),
    MAGIC(
        doses = 4,
        shadesID =
            listOf(
                NPCs.LOAR_SHADE_1241,
                NPCs.PHRIN_SHADE_1244,
                NPCs.RIYL_SHADE_1246,
                NPCs.ASYN_SHADE_1248,
                NPCs.FIYR_SHADE_1250,
            ),
        requiredLevel = 80,
        experience = 404.5,
    ),
}
