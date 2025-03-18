package core.game.node.entity.combat.spell

import core.game.node.item.Item
import org.rs.consts.Items

enum class Runes(
    val id: Int,
) {
    AIR_RUNE(Items.AIR_RUNE_556),
    WATER_RUNE(Items.WATER_RUNE_555),
    EARTH_RUNE(Items.EARTH_RUNE_557),
    FIRE_RUNE(Items.FIRE_RUNE_554),
    MIND_RUNE(Items.MIND_RUNE_558),
    NATURE_RUNE(Items.NATURE_RUNE_561),
    CHAOS_RUNE(Items.CHAOS_RUNE_562),
    DEATH_RUNE(Items.DEATH_RUNE_560),
    COSMIC_RUNE(Items.COSMIC_RUNE_564),
    BLOOD_RUNE(Items.BLOOD_RUNE_565),
    SOUL_RUNE(Items.SOUL_RUNE_566),
    ASTRAL_RUNE(Items.ASTRAL_RUNE_9075),
    LAW_RUNE(Items.LAW_RUNE_563),
    BODY_RUNE(Items.BODY_RUNE_559),
    ARMADYL_RUNE(Items.ARMADYL_RUNE_14700),
    SARADOMIN_STAFF(Items.SARADOMIN_STAFF_2415),
    GUTHIX_STAFF(Items.GUTHIX_STAFF_2416),
    ZAMORAK_STAFF(Items.ZAMORAK_STAFF_2417),
    ZURIELS_STAFF(Items.ZURIELS_STAFF_13867),
    ;

    fun transform(): Item = Item(id)

    fun getItem(amount: Int): Item = Item(id, amount)

    companion object {
        fun forId(id: Int): Runes? {
            for (rune in values()) {
                if (rune.id == id) {
                    return rune
                }
            }
            return null
        }

        fun isInfinite(
            rune: Runes?,
            weapon: Item?,
            vararg type: SpellType,
        ): Boolean {
            if (weapon == null || rune == null) {
                return false
            }
            if (type != null) {
                if (weapon.id == 2415 && rune == SARADOMIN_STAFF && type.size == 1) {
                    if (type[0] === SpellType.GOD_STRIKE) {
                        return true
                    }
                }
                if (weapon.id == 2415 && rune == SARADOMIN_STAFF && type.size == 1) {
                    if (type[0] === SpellType.GOD_STRIKE) {
                        return true
                    }
                }
                if (weapon.id == 2416 && rune == GUTHIX_STAFF && type.size == 1) {
                    if (type[0] === SpellType.GOD_STRIKE) {
                        return true
                    }
                }
                if (weapon.id == 13867 && rune == ZURIELS_STAFF && type.size == 1) {
                    if (type[0] === SpellType.BARRAGE ||
                        type[0] === SpellType.BLITZ ||
                        type[0] === SpellType.RUSH ||
                        type[0] === SpellType.BURST
                    ) {
                        return true
                    }
                }
                if (weapon.id == 2417 && rune == ZAMORAK_STAFF && type.size == 1) {
                    if (type[0] === SpellType.GOD_STRIKE) {
                        return true
                    }
                }
            }
            if (rune == AIR_RUNE) {
                if (weapon.id == Items.STAFF_OF_AIR_1381 || weapon.id == Items.AIR_BATTLESTAFF_1397) {
                    return true
                }
            } else if (rune == WATER_RUNE) {
                if (weapon.id == Items.STAFF_OF_WATER_1383 || weapon.id == Items.WATER_BATTLESTAFF_1395) {
                    return true
                }
            } else if (rune == EARTH_RUNE) {
                if (weapon.id == Items.STAFF_OF_EARTH_1385 || weapon.id == Items.EARTH_BATTLESTAFF_1399) {
                    return true
                }
            } else if (rune == FIRE_RUNE) {
                if (weapon.id == Items.STAFF_OF_FIRE_1387 || weapon.id == Items.FIRE_BATTLESTAFF_1393) {
                    return true
                }
            }
            return false
        }
    }
}
