package content.global.plugins.interfaces.player_kit

import content.region.island.tutorial.plugin.CharacterDesign
import core.api.*
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.appearance.Gender
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds

class PlayerKitInterfaceListener : InterfaceListener {

    override fun defineInterfaceListeners() {
        onOpen(PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID) { player, component ->
            openHairdresserShop(player, component.id)
            return@onOpen true
        }
        onOpen(PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID) { player, component ->
            openHairdresserShop(player, component.id)
            return@onOpen true
        }
        on(PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID) { player, component, _, button, _, _ ->
            handleHairdresserButtons(player, component.id, button)
            return@on true
        }
        on(PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID) { player, component, _, button, _, _ ->
            handleHairdresserButtons(player, component.id, button)
            return@on true
        }
        onOpen(PlayerKit.MAKEOVER_MAGE_INTERFACE_ID) { player, component ->
            openMakeoverShop(player, component)
            return@onOpen true
        }
        on(PlayerKit.MAKEOVER_MAGE_INTERFACE_ID) { player, _, _, button, _, _ ->
            when (button) {
                in PlayerKit.SKIN_COLOR_BUTTON_COMPONENT_IDS -> { updateSkin(player, button) }
                113, 101 -> setAttribute(player, PlayerKit.PLAYER_KIT_GENDER_SAVE_ATTIBUTE, Gender.MALE.ordinal)
                114, 103 -> setAttribute(player, PlayerKit.PLAYER_KIT_GENDER_SAVE_ATTIBUTE, Gender.FEMALE.ordinal)
                PlayerKit.MAKEOVER_TEXT_COMPONENT_ID -> makeoverPay(player)
            }
            return@on true
        }
        onOpen(PlayerKit.THESSALIA_MALE_INTERFACE_ID) { player, _ ->
            openClothesShop(player, male = true)
            return@onOpen true
        }
        onOpen(PlayerKit.THESSALIA_FEMALE_INTERFACE_ID) { player, _ ->
            openClothesShop(player, male = false)
            return@onOpen true
        }
        on(PlayerKit.THESSALIA_MALE_INTERFACE_ID) { player, _, _, button, _, _ ->
            handleClothesButtons(player, button, true)
            return@on true
        }
        on(PlayerKit.THESSALIA_FEMALE_INTERFACE_ID) { player, _, _, button, _, _ ->
            handleClothesButtons(player, button, false)
            return@on true
        }
        onClose(PlayerKit.THESSALIA_MALE_INTERFACE_ID) { player, _ ->
            return@onClose closeClothesShop(player)
        }
        onClose(PlayerKit.THESSALIA_FEMALE_INTERFACE_ID) { player, _ ->
            return@onClose closeClothesShop(player)
        }
        onOpen(PlayerKit.YRSA_SHOE_STORE_INTERFACE_ID) { player, _ ->
            openShoeInterface(player)
            return@onOpen true
        }
        on(PlayerKit.YRSA_SHOE_STORE_INTERFACE_ID) { player, _, _, button, _, _ ->
            if (button == 14) {
                shoePay(player)
                return@on true
            }
            if (button in PlayerKit.YRSA_SELECT_BUTTONS_COMPONENT_IDS) {
                updateFeet(player, button)
                return@on true
            }

            return@on true
        }
        onOpen(PlayerKit.REINALD_BRACELETS_INTERFACE_ID) { player, component ->
            setAttribute(player, PlayerKit.PLAYER_KIT_WRIST_SAVE_ATTRIBUTE, player.appearance.wrists.look)
            player.toggleWardrobe(true)
            component.setUncloseEvent { p, _ ->
                closeBraceShop(p)
                true
            }
            return@onOpen true
        }
        on(PlayerKit.REINALD_BRACELETS_INTERFACE_ID) { player, _, _, buttonId, _, _ ->
            PlayerKit.WRISTS_MODELS[buttonId]?.let { modelId -> openBraceShop(player, modelId) }
                ?: run { if (buttonId == 117) braceletPay(player) }
            return@on true
        }
    }

    private fun openHairdresserShop(player: Player, iface: Int) {
        val childModel = if (iface == PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID) 17 else 62
        val childHead = if (iface == PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID) 146 else 61

        setAttribute(player, PlayerKit.PLAYER_KIT_HAIR_SAVE_ATTRIBUTE, player.appearance.hair.look)
        setAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SAVE_ATTRIBUTE, player.appearance.beard.look)
        setAttribute(player, PlayerKit.PLAYER_KIT_HAIR_COLOR_SAVE_ATTRIBUTE, player.appearance.hair.color)
        setAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, false)

        sendPlayerOnInterface(player, iface, childModel)
        sendPlayerOnInterface(player, iface, childHead)
        sendAnimationOnInterface(player, FaceAnim.HAPPY.animationId, iface, childHead)
        player.toggleWardrobe(true)

        Component(iface)?.setUncloseEvent { p, _ ->
            closeHairdresserShop(p)
            true
        }

        refreshAppearance(player)
    }

    private fun handleHairdresserButtons(player: Player, iface: Int, button: Int) {
        when (button) {
            199 -> setAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, false)
            200 -> setAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, true)
            196, 274 -> hairdresserPay(player)
            else -> {
                if (iface == PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID)
                    updateHairMale(player, button) else updateHairFemale(player, button)
            }
        }
    }

    private fun closeHairdresserShop(player: Player) {
        player.toggleWardrobe(false)
        val paid = getAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, false)
        if (!paid) {
            val originalHair = getAttribute(player, PlayerKit.PLAYER_KIT_HAIR_SAVE_ATTRIBUTE, 0)
            val originalBeard = getAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SAVE_ATTRIBUTE, -1)
            val originalColor = getAttribute(player, PlayerKit.PLAYER_KIT_HAIR_COLOR_SAVE_ATTRIBUTE, 0)
            updateHairLook(player, originalHair)
            updateHairColor(player, originalColor)

            if (originalBeard != -1) {
                updateBeardLook(player, originalBeard)
            }
            refreshAppearance(player)
        }
        removeAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE)
    }

    private fun openBraceShop(player: Player, modelId: Int) {
        val appearanceIndex = calculateBraceletIndex(modelId, player)
        sendModelOnInterface(player, Components.REINALD_SMITHING_EMPORIUM_593, PlayerKit.BRACELET_PREVIEW_COMPONENT_ID, modelId, 1)
        setComponentVisibility(player, Components.REINALD_SMITHING_EMPORIUM_593, PlayerKit.BRACELET_PREVIEW_COMPONENT_ID, modelId == 0)
        updateWristsLook(player, appearanceIndex)
        player.debug("Using wrist appearance id =[$appearanceIndex]")
        refreshAppearance(player)
        sendPlayerOnInterface(player, Components.REINALD_SMITHING_EMPORIUM_593, 60)
    }

    private fun closeBraceShop(player: Player) {
        val original = getAttribute(player, PlayerKit.PLAYER_KIT_WRIST_SAVE_ATTRIBUTE, defaultBraceletAppearance(player))
        if (!getAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, false)) {
            updateWristsLook(player, original)
            refreshAppearance(player)
        }
        removeAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE)
        player.toggleWardrobe(false)
    }

    private fun calculateBraceletIndex(id: Int, p: Player): Int {
        var base =
            when (id) {
                27704 -> 117
                27708 -> 118
                27697 -> 119
                27700 -> 120
                27699 -> 123
                27709 -> 124
                27707 -> 121
                27705 -> 122
                27706 -> 125
                27702 -> 126
                27703 -> if (p.isMale) 33 else 67
                27698 -> if (p.isMale) 84 else 127
                0 -> if (p.isMale) 34 else 68
                else -> 0
            }
        if (!p.isMale && id !in listOf(27703, 27698, 0)) base += 42
        return base
    }

    private fun defaultBraceletAppearance(player: Player) = if (player.isMale) 34 else 68

    private fun openMakeoverShop(player: Player, component: Component) {
        sendNpcOnInterface(player, 1, component.id, PlayerKit.MAKEOVER_MODEL_MALE_COMPONENT_ID)
        sendNpcOnInterface(player, 5, component.id, PlayerKit.MAKEOVER_MODEL_FEMALE_COMPONENT_ID)
        sendAnimationOnInterface(player, FaceAnim.NEUTRAL.animationId, component.id, PlayerKit.MAKEOVER_MODEL_MALE_COMPONENT_ID)
        sendAnimationOnInterface(player, FaceAnim.NEUTRAL.animationId, component.id, PlayerKit.MAKEOVER_MODEL_FEMALE_COMPONENT_ID)

        if (inInventory(player, Items.MAKEOVER_VOUCHER_5606)) {
            sendString(player, "USE MAKEOVER VOUCHER", component.id, PlayerKit.MAKEOVER_TEXT_COMPONENT_ID)
        }

        val currentSkinColor = player.appearance.skin.color
        setAttribute(player, PlayerKit.PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, currentSkinColor)
        setVarp(player, 262, currentSkinColor)

        player.toggleWardrobe(true)
        component.setUncloseEvent { p, _ ->
            p.toggleWardrobe(false)

            if (getAttribute(p, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, false)) {
                val newColor = getAttribute(player, PlayerKit.PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, -1)
                val newGender = getAttribute(player, PlayerKit.PLAYER_KIT_GENDER_SAVE_ATTIBUTE, -1)

                if (newGender > -1) {
                    mapAppearance(p, Gender.values()[newGender])
                }
                if (newColor > -1) {
                    updateSkinColor(player, newColor)
                }
                refreshAppearance(p)
                removeAttribute(p, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE)
            }

            removeAttribute(p, PlayerKit.PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE)
            removeAttribute(p, PlayerKit.PLAYER_KIT_GENDER_SAVE_ATTIBUTE)
            true
        }
    }

    private fun updateSkin(player: Player, button: Int) {
        val newIndex =
            when (button) {
                in 93..99 -> button - 92
                100 -> 8
                else -> return
            }
        val newSkin = button - PlayerKit.SKIN_COLOR_BUTTON_COMPONENT_IDS.first
        setAttribute(player, PlayerKit.PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, newSkin)
        setVarp(player, 262, newIndex)
        updateSkinColor(player, newSkin)
        refreshAppearance(player)
    }

    private fun mapAppearance(player: Player, newGender: Gender) {
        val appearance = player.appearance
        val oldGender = appearance.gender
        if (oldGender == newGender) return

        val oldCache = appearance.appearanceCache.map { it.look to it.color }
        appearance.setGender(newGender)

        val src = if (oldGender == Gender.MALE) CharacterDesign.MALE_LOOK_IDS else CharacterDesign.FEMALE_LOOK_IDS
        val dst = if (newGender == Gender.MALE) CharacterDesign.MALE_LOOK_IDS else CharacterDesign.FEMALE_LOOK_IDS

        val newCache = appearance.appearanceCache
        for (i in newCache.indices) {
            val (look, col) = oldCache.getOrNull(i) ?: continue
            val s = src.getOrNull(i)
            val d = dst.getOrNull(i)
            if (s == null || d == null || s.isEmpty() || d.isEmpty()) continue
            val idx = s.indexOf(look)
            val mapped = if (idx != -1 && idx < d.size) d[idx] else d.first()
            newCache[i].changeLook(mapped)
            newCache[i].changeColor(col)
        }
        appearance.sync()
    }

    private fun openShoeInterface(player: Player) {
        val original = player.appearance.feet.color
        setAttribute(player, PlayerKit.PLAYER_KIT_FEET_SAVE_ATTRIBUTE, original)
        playGlobalAudio(player.location, Sounds.WARDROBE_OPEN_96, 1)
        player.toggleWardrobe(true)
        for (i in PlayerKit.YRSA_FEET_MODEL_IDS.indices) {
            sendItemOnInterface(
                player,
                PlayerKit.YRSA_SHOE_STORE_INTERFACE_ID,
                PlayerKit.YRSA_SELECT_BUTTONS_COMPONENT_IDS[i],
                PlayerKit.YRSA_FEET_MODEL_IDS[i]
            )
        }
        val text = if (!player.houseManager.isInHouse(player)) "CONFIRM (500 GOLD)" else "CONFIRM (FREE)"
        sendString(player, text, PlayerKit.YRSA_SHOE_STORE_INTERFACE_ID, 14)
        Component(PlayerKit.YRSA_SHOE_STORE_INTERFACE_ID)?.setUncloseEvent { p, _ ->
            closeShoeInterface(p)
            true
        }
        refreshAppearance(player)
    }

    private fun closeShoeInterface(player: Player) {
        player.toggleWardrobe(false)
        playGlobalAudio(player.location, Sounds.WARDROBE_CLOSE_95, 1)

        if (!getAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, false)) {
            val original = getAttribute(player, PlayerKit.PLAYER_KIT_FEET_SAVE_ATTRIBUTE, player.appearance.feet.color)
            updateFeetColor(player, original)
            refreshAppearance(player)
        }

        removeAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE)
        removeAttribute(player, PlayerKit.PLAYER_KIT_FEET_SAVE_ATTRIBUTE)
        refreshAppearance(player)
    }

    private fun updateFeet(player: Player, button: Int) {
        val subtract = 15
        val idx = button - subtract
        setVarp(player, 261, button - 14)
        updateFeetColor(player, PlayerKit.YRSA_COLOR_BUTTONS_COMPONENT_IDS[idx])
        refreshAppearance(player)
    }

    inner class EndDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> npc(NPCs.YRSA_1301, FaceAnim.FRIENDLY, "I think they suit you.").also { stage++ }
                1 -> player(FaceAnim.HAPPY, "Thanks!").also { stage++ }
                2 -> end()
            }
        }
    }

    private fun updateHairFemale(player: Player, button: Int) {
        if (button in PlayerKit.femaleColorButtonRange) {
            updateHairColor(player, button, PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID)
            return
        }
        if (button in PlayerKit.femaleStyleButtonRange) {
            updateHair(player, button, PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID)
        }
    }

    private fun updateHairMale(player: Player, button: Int) {
        val beardMode = getAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, false)

        if (beardMode && button !in PlayerKit.maleColorButtonRange) {
            updateBeard(player, button)
            return
        }
        if (button in PlayerKit.maleColorButtonRange) {
            updateHairColor(player, button, PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID)
            return
        }
        if (button in PlayerKit.maleStyleButtonRange) {
            updateHair(player, button, PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID)
        }
    }

    private fun updateBeard(player: Player, button: Int) {
        var offset = 105
        when (button) {
            123 -> offset += 2
            126 -> offset += 4
            129 -> offset += 6
        }
        val index = PlayerKit.MALE_FACIAL[button - offset]
        player.appearance.beard.changeLook(index)
        refreshAppearance(player)
    }

    private fun updateHair(player: Player, button: Int, iface: Int) {
        val base = if (iface == PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID) 65 else 148
        var subtractor = base
        val array = if (iface == PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID) PlayerKit.MALE_HAIR else PlayerKit.FEMALE_HAIR
        if (button == 89 || button == 90) subtractor += 2
        updateHairLook(player, array[button - subtractor])
        refreshAppearance(player)
    }

    private fun updateHairColor(player: Player, button: Int, iface: Int) {
        val offset = if (iface == PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID) 229 else 73
        updateHairColor(player, PlayerKit.HAIR_COLORS[button - offset])
        refreshAppearance(player)
    }

    private fun openClothesShop(player: Player, male: Boolean) {
        player.toggleWardrobe(true)

        setAttribute(player, PlayerKit.PLAYER_KIT_TORSO_SAVE_ATTRIBUTE, player.appearance.torso.look)
        setAttribute(player, PlayerKit.PLAYER_KIT_TORSO_COLOR_SAVE_ATTRIBUTE, player.appearance.torso.color)

        setAttribute(player, PlayerKit.PLAYER_KIT_ARMS_SAVE_ATTRIBUTE, player.appearance.arms.look)
        setAttribute(player, PlayerKit.PLAYER_KIT_ARMS_COLOR_SAVE_ATTRIBUTE, player.appearance.arms.color)

        setAttribute(player, PlayerKit.PLAYER_KIT_LEGS_SAVE_ATTRIBUTE, player.appearance.legs.look)
        setAttribute(player, PlayerKit.PLAYER_KIT_LEGS_COLOR_SAVE_ATTRIBUTE, player.appearance.legs.color)

        val componentId = PlayerKit.CLOTHES_DISPLAY_COMPONENT_ID
        if (male) sendPlayerOnInterface(player, PlayerKit.THESSALIA_MALE_INTERFACE_ID, componentId)
        else sendPlayerOnInterface(player, PlayerKit.THESSALIA_FEMALE_INTERFACE_ID, componentId)
    }

    private fun handleClothesButtons(player: Player, button: Int, male: Boolean) {
        // pay buttons.
        if (button == 181 || button == 180 || button == 297) {
            clothesPay(player)
            return
        }
        // type select.
        when (button) {
            if (male) 182 else 183 -> setAttribute(player, PlayerKit.PLAYER_KIT_TYPE_ATTRIBUTE, PlayerKit.ColorType.TORSO)
            if (male) 183 else 184 -> setAttribute(player, PlayerKit.PLAYER_KIT_TYPE_ATTRIBUTE, PlayerKit.ColorType.ARMS)
            if (male) 184 else 185 -> setAttribute(player, PlayerKit.PLAYER_KIT_TYPE_ATTRIBUTE, PlayerKit.ColorType.LEGS)
        }
        val type = getAttribute(player, PlayerKit.PLAYER_KIT_TYPE_ATTRIBUTE, PlayerKit.ColorType.TORSO)
        if (male) {
            if (button in PlayerKit.MALE_ARMS_BUTTONS_COMPONENT_IDS) updateArms(player, button, true)
            if (button in PlayerKit.MALE_TORSO_BUTTONS_COMPONENT_IDS) updateTorso(player, button, true)
            if (button in PlayerKit.MALE_LEGS_BUTTONS_COMPONENT_IDS) updateLegs(player, button, true)
            if (button in PlayerKit.MALE_COLOR_BUTTONS_COMPONENT_IDS) updateColor(player, button, true, type)
        } else {
            if (button in PlayerKit.FEMALE_ARMS_BUTTONS_COMPONENT_IDS) updateArms(player, button, false)
            if (button in PlayerKit.FEMALE_TORSO_BUTTONS_COMPONENT_IDS) updateTorso(player, button, false)
            if (button in PlayerKit.FEMALE_LEGS_BUTTONS_COMPONENT_IDS) updateLegs(player, button, false)
            if (button in PlayerKit.FEMALE_COLOR_BUTTONS_COMPONENT_IDS) updateColor(player, button, false, type)
        }
    }

    private fun closeClothesShop(player: Player): Boolean {
        player.toggleWardrobe(false)
        removeAttribute(player, PlayerKit.PLAYER_KIT_TYPE_ATTRIBUTE)
        playJingle(player, 266)

        if (!getAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, false)) {
            updateTorsoLook(player,  getAttribute(player, PlayerKit.PLAYER_KIT_TORSO_SAVE_ATTRIBUTE, 0))
            updateTorsoColor(player, getAttribute(player, PlayerKit.PLAYER_KIT_TORSO_COLOR_SAVE_ATTRIBUTE, 0))
            updateArmsLook(player,   getAttribute(player, PlayerKit.PLAYER_KIT_ARMS_SAVE_ATTRIBUTE, 0))
            updateArmsColor(player,  getAttribute(player, PlayerKit.PLAYER_KIT_ARMS_COLOR_SAVE_ATTRIBUTE, 0))
            updateLegsLook(player,   getAttribute(player, PlayerKit.PLAYER_KIT_LEGS_SAVE_ATTRIBUTE, 0))
            updateLegsColor(player,  getAttribute(player, PlayerKit.PLAYER_KIT_LEGS_COLOR_SAVE_ATTRIBUTE, 0))
            refreshAppearance(player)
        }

        player.removeAttribute(PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE)
        return true
    }

    private fun updateTorso(p: Player, button: Int, male: Boolean) {
        val arr = if (male) PlayerKit.MALE_TORSO_IDS else PlayerKit.FEMALE_TORSO_IDS
        val subtract = if (male) PlayerKit.MALE_TORSO_BUTTONS_COMPONENT_IDS.first else PlayerKit.FEMALE_TORSO_BUTTONS_COMPONENT_IDS.first
        updateTorsoLook(p, arr[button - subtract])
        refreshAppearance(p)
    }

    private fun updateLegs(p: Player, button: Int, male: Boolean) {
        val arr = if (male) PlayerKit.MALE_LEG_IDS else PlayerKit.FEMALE_LEG_IDS
        val subtract = if (male) PlayerKit.MALE_LEGS_BUTTONS_COMPONENT_IDS.first else PlayerKit.FEMALE_LEGS_BUTTONS_COMPONENT_IDS.first
        updateLegsLook(p, arr[button - subtract])
        refreshAppearance(p)
    }

    private fun updateArms(p: Player, button: Int, male: Boolean) {
        val arr = if (male) PlayerKit.MALE_SLEEVE_IDS else PlayerKit.FEMALE_ARMS_IDS
        val subtract = if (male) PlayerKit.MALE_ARMS_BUTTONS_COMPONENT_IDS.first else PlayerKit.FEMALE_ARMS_BUTTONS_COMPONENT_IDS.first
        updateArmsLook(p, arr[button - subtract])
        refreshAppearance(p)
    }

    private fun updateColor(p: Player, button: Int, male: Boolean, type: PlayerKit.ColorType) {
        val subtract = if (male) PlayerKit.MALE_COLOR_BUTTONS_COMPONENT_IDS.first else PlayerKit.FEMALE_COLOR_BUTTONS_COMPONENT_IDS.first
        val index = button - subtract
        when (type) {
            PlayerKit.ColorType.TORSO -> updateTorsoLook(p, PlayerKit.TORSO_BUTTON_COLOR_COMPONENT_IDS[index])
            PlayerKit.ColorType.ARMS -> updateTorsoColor(p, PlayerKit.TORSO_BUTTON_COLOR_COMPONENT_IDS[index])
            PlayerKit.ColorType.LEGS -> updateLegsColor(p, PlayerKit.LEGS_BUTTON_COLOR_COMPONENT_IDS[index])
        }
        refreshAppearance(p)
    }

    private fun hairdresserPay(player: Player) {
        if (!player.houseManager.isInHouse(player)) {
            if (!removeItem(player, PlayerKit.HAIR_CHANGE_PRICE)) {
                sendDialogue(player, "You can not afford that.")
                return
            }
        }
        setAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)
    }

    private fun braceletPay(player: Player) {
        if (!removeItem(player, PlayerKit.WRISTS_CHANGE_PRICE)) {
            sendDialogue(player, "You cannot afford that.")
            return
        }
        setAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)
    }

    private fun clothesPay(player: Player) {
        if (!player.houseManager.isInHouse(player)) {
            if (!removeItem(player, PlayerKit.CLOTHES_PRICE)) {
                sendDialogue(player, "You cannot afford that.")
                return
            }
        }
        setAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)
    }

    private fun shoePay(player: Player) {
        val newColor = getAttribute(player, PlayerKit.PLAYER_KIT_FEET_SAVE_ATTRIBUTE, player.appearance.feet.color)

        if (newColor == player.appearance.feet.color) {
            closeInterface(player)
            return
        }
        if (!player.houseManager.isInHouse(player)) {
            if (!removeItem(player, PlayerKit.FEET_CHANGE_PRICE)) {
                sendDialogue(player, "You cannot afford that.")
                return
            }
        }

        setAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)

        setVarp(player, 261, 0)
        openDialogue(player, EndDialogue())
    }

    private fun makeoverPay(player: Player) {
        val oldGender = player.appearance.gender
        val newColor = getAttribute(player, PlayerKit.PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, player.appearance.skin.color)
        val newGender = Gender.values()[player.getAttribute(PlayerKit.PLAYER_KIT_GENDER_SAVE_ATTIBUTE, oldGender.ordinal)]

        if (newColor == player.appearance.skin.color && newGender == oldGender) {
            closeInterface(player)
            return
        }

        val currency =
            if (inInventory(player, Items.MAKEOVER_VOUCHER_5606)) {
                PlayerKit.MAKEOVER_VOUCHER
            } else {
                PlayerKit.MAKEOVER_PRICE
            }

        if (!removeItem(player, currency)) {
            sendDialogue(player, "You cannot afford that.")
            return
        }

        setAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)

        val npc = findNPC(NPCs.MAKE_OVER_MAGE_2676)
        if (npc != null && oldGender != newGender) {
            runTask(player, 2) {
                when {
                    oldGender == Gender.MALE && newGender == Gender.FEMALE -> {
                        sendChat(npc, "Ooh!")
                        npc.transform(NPCs.MAKE_OVER_MAGE_2676)
                    }
                    oldGender == Gender.FEMALE && newGender == Gender.MALE -> {
                        sendChat(npc, "Aha!")
                        npc.transform(NPCs.MAKE_OVER_MAGE_599)
                    }
                }
            }
            queueScript(player, 5, QueueStrength.SOFT) {
                npc.transform(NPCs.MAKE_OVER_MAGE_599)
                return@queueScript stopExecuting(player)
            }
        }
    }
}
