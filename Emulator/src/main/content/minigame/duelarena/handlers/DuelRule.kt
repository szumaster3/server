package content.minigame.duelarena.handlers

import core.game.container.impl.EquipmentContainer

enum class DuelRule {
    NO_RANGE(
        configIndex = 4,
        info = "You cannot use Ranged attacks.",
    ),
    NO_MELEE(
        configIndex = 5,
        info = "You cannot use Melee attacks.",
    ),
    NO_MAGIC(
        configIndex = 6,
        info = "You cannot use Magic attacks.",
    ),
    FUN_WEAPONS(
        configIndex = 12,
        info = "You can only attack with 'fun' weapons.",
    ),
    NO_FORFEIT(
        configIndex = 0,
        info = "You cannot forfeit the duel.",
    ),
    NO_DRINKS(
        configIndex = 7,
        info = "You cannot use potions.",
    ),
    NO_FOOD(
        configIndex = 8,
        info = "You cannot use food.",
    ),
    NO_PRAYER(
        configIndex = 9,
        info = "You cannot use Prayer.",
    ),
    NO_MOVEMENT(
        configIndex = 1,
        info = "You cannot move.",
    ),
    OBSTACLES(
        configIndex = 10,
        info = "There will be obstacles in the arena.",
    ),
    ENABLE_SUMMONING(
        configIndex = 28,
        info = "Familiars will be allowed in the arena.",
    ),
    NO_SPECIAL_ATTACKS(
        configIndex = 13,
        info = "You cannot use special attacks.",
    ),
    NO_HATS(
        configIndex = 14,
        info = "",
        equipmentSlot = EquipmentContainer.SLOT_HAT,
    ),
    NO_CAPES(
        configIndex = 15,
        info = "",
        equipmentSlot = EquipmentContainer.SLOT_CAPE,
    ),
    NO_AMULET(
        configIndex = 16,
        info = "",
        equipmentSlot = EquipmentContainer.SLOT_AMULET,
    ),
    NO_WEAPON(
        configIndex = 17,
        info = "You can't use 2H weapons such as bows.",
        equipmentSlot = EquipmentContainer.SLOT_WEAPON,
    ),
    NO_SHIELD(
        configIndex = 19,
        info = "You can't use 2H weapons such as bows.",
        equipmentSlot = EquipmentContainer.SLOT_SHIELD,
    ),
    NO_BODY(
        configIndex = 18,
        info = "",
        equipmentSlot = EquipmentContainer.SLOT_CHEST,
    ),
    NO_LEGS(
        configIndex = 21,
        info = "",
        equipmentSlot = EquipmentContainer.SLOT_LEGS,
    ),
    NO_GLOVES(
        configIndex = 23,
        info = "",
        equipmentSlot = EquipmentContainer.SLOT_HANDS,
    ),
    NO_RINGS(
        configIndex = 26,
        info = "",
        equipmentSlot = EquipmentContainer.SLOT_RING,
    ),
    NO_BOOTS(
        configIndex = 24,
        info = "",
        equipmentSlot = EquipmentContainer.SLOT_FEET,
    ),
    NO_ARROWS(
        configIndex = 27,
        info = "",
        equipmentSlot = EquipmentContainer.SLOT_ARROWS,
    ),
    ;

    val configIndex: Int
    val info: String
    var equipmentSlot = -1

    constructor(configIndex: Int, info: String) {
        this.configIndex = configIndex
        this.info = info
    }

    constructor(configIndex: Int, info: String, equipmentSlot: Int) {
        this.configIndex = configIndex
        this.info = info
        this.equipmentSlot = equipmentSlot
    }
}
