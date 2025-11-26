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
import core.tools.Log
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

/**
 * Handles the player kit interfaces
 * @author Emperor, Vexia, Ceikry
 */
class PlayerKitInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        // Start appearance changer.
        onOpen(PlayerKit.START_APPEARANCE_INTERFACE_ID) { player, _ ->
            if (player.interfaceManager.isResizable) openOverlay(player, Components.BLACK_OVERLAY_333)
            return@onOpen true
        }
        on(PlayerKit.START_APPEARANCE_INTERFACE_ID) { player, _, _, buttonID, _, _ ->
            CharacterDesign.handleButtons(player, buttonID)
            return@on true
        }
        // Hairdresser.
        listOf(PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID, PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID).forEach { iface ->
            onOpen(iface) { player, c ->
                openHairdresserShop(player, c.id)
                return@onOpen true
            }
            on(iface) { player, _, _, button, _, _ ->
                handleHairdresserButtons(player, iface, button)
                return@on true
            }
            onClose(iface) { p, _ ->
                closeHairdresserShop(p)
                return@onClose true
            }
        }
        // Makeover (gender + skin).
        onOpen(PlayerKit.MAKEOVER_MAGE_INTERFACE_ID) { player, component ->
            openMakeoverShop(player, component)
            return@onOpen true
        }
        on(PlayerKit.MAKEOVER_MAGE_INTERFACE_ID) { player, _, _, button, _, _ ->
            when (button) {
                in PlayerKit.SKIN_COLOR_BUTTON_COMPONENT_IDS -> updateSkin(player, button)
                113,
                101 -> updateGender(player, true)
                114,
                103 -> updateGender(player, false)
                PlayerKit.MAKEOVER_CONFIRM_COMPONENT_ID -> makeoverPay(player)
            }
            return@on true
        }
        // Clothes.
        listOf(PlayerKit.THESSALIA_MALE_INTERFACE_ID to true, PlayerKit.THESSALIA_FEMALE_INTERFACE_ID to false).forEach {
                (iface, male) ->
            onOpen(iface) { player, _ ->
                openClothesShop(player, male)
                return@onOpen true
            }
            on(iface) { player, c, _, button, _, _ ->
                handleClothesButtons(player, c.id, button, male)
                return@on true
            }
            onClose(iface) { player, _ -> closeClothesShop(player) }
        }
        // Shoe store.
        onOpen(PlayerKit.YRSA_SHOE_STORE_INTERFACE_ID) { player, c ->
            c.setUncloseEvent { p, _ ->
                closeShoeShop(p)
                true
            }
            openShoeShop(player)
            return@onOpen true
        }
        on(PlayerKit.YRSA_SHOE_STORE_INTERFACE_ID) { player, _, _, button, _, _ ->
            when (button) {
                14 -> shoePay(player)
                in PlayerKit.YRSA_SELECT_BUTTONS_COMPONENT_IDS -> updateFeet(player, button)
            }
            return@on true
        }
        // Rainald arm guards.
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
            PlayerKit.WRISTS_MODELS[buttonId]?.let { modelId -> updateArmguards(player, modelId) }
                ?: run { if (buttonId == 117) pay(player, PlayerKit.REINALD_BRACELETS_INTERFACE_ID) }
            return@on true
        }
    }

    /**
     * Opens the hairdresser interface.
     */
    private fun openHairdresserShop(player: Player, iface: Int) {
        player.toggleWardrobe(true)
        val female = iface == PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID
        val childModel = if (female) 17 else 62
        val childHead = if (female) 146 else 61

        setAttribute(player, PlayerKit.PLAYER_KIT_HAIR_SAVE_ATTRIBUTE, player.appearance.hair.look)
        setAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SAVE_ATTRIBUTE, player.appearance.beard.look)
        setAttribute(player, PlayerKit.PLAYER_KIT_HAIR_COLOR_SAVE_ATTRIBUTE, player.appearance.hair.color)
        setAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, false)

        sendPlayerOnInterface(player, iface, childModel)
        sendPlayerOnInterface(player, iface, childHead)
        sendAnimationOnInterface(player, FaceAnim.HAPPY.animationId, iface, childHead)
        Component(iface).setUncloseEvent { p, _ ->
            closeHairdresserShop(p)
            true
        }
    }

    /**
     * Handles button interactions for hairdresser.
     */
    private fun handleHairdresserButtons(player: Player, comp: Int, button: Int) {
        when (button) {
            199 -> setAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, false)
            200 -> setAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, true)
            196,
            274,
            100,
            68 -> pay(player, comp)
            else ->
                if (comp == PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID) updateHairMale(player, button)
                else updateHairFemale(player, button)
        }
    }
    /**
     * Closes the hairdresser shop.
     */
    private fun closeHairdresserShop(player: Player) {
        player.toggleWardrobe(false)
        playJingle(player, 266)
        val paid = getAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, false)
        if (!paid) {
            updateHairLook(player, getAttribute(player, PlayerKit.PLAYER_KIT_HAIR_SAVE_ATTRIBUTE, 0))
            updateHairColor(player, getAttribute(player, PlayerKit.PLAYER_KIT_HAIR_COLOR_SAVE_ATTRIBUTE, 0))
            val beard = getAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SAVE_ATTRIBUTE, -1)
            if (beard != -1) updateBeardLook(player, beard)
            refreshAppearance(player)
        }
        removeAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE)
    }

    /**
     * Closes the bracelet shop.
     */
    private fun closeBraceShop(player: Player) {
        val original = getAttribute(player, PlayerKit.PLAYER_KIT_WRIST_SAVE_ATTRIBUTE, defaultBraceletAppearance(player))
        if (!getAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, false)) {
            updateWristsLook(player, original)
            refreshAppearance(player)
        }
        removeAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE)
        player.toggleWardrobe(false)
        playJingle(player, 266)
    }

    /**
     * Converts a bracelet model id into the appearance index for the players gender.
     *
     * @param id The bracelet model.
     * @param p The player.
     * @return The appearance index.
     */
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

    /**
     * Return the default wrists based on players gender.
     */
    private fun defaultBraceletAppearance(player: Player) = if (player.isMale) 34 else 68

    /**
     * Opens the makeover mage interface.
     */
    private fun openMakeoverShop(player: Player, component: Component) {
        sendNpcOnInterface(player, 1, component.id, PlayerKit.MAKEOVER_MODEL_MALE_COMPONENT_ID)
        sendNpcOnInterface(player, 5, component.id, PlayerKit.MAKEOVER_MODEL_FEMALE_COMPONENT_ID)
        sendAnimationOnInterface(
            player,
            FaceAnim.NEUTRAL.animationId,
            component.id,
            PlayerKit.MAKEOVER_MODEL_MALE_COMPONENT_ID
        )
        sendAnimationOnInterface(
            player,
            FaceAnim.NEUTRAL.animationId,
            component.id,
            PlayerKit.MAKEOVER_MODEL_FEMALE_COMPONENT_ID
        )
        if (inInventory(player, Items.MAKEOVER_VOUCHER_5606)) {
            sendString(player, "USE MAKEOVER VOUCHER", component.id, PlayerKit.MAKEOVER_CONFIRM_COMPONENT_ID)
        }
        setAttribute(player, PlayerKit.PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, player.appearance.skin.color)
        setVarp(player, 262, player.appearance.skin.color)
        player.toggleWardrobe(true)
        component.setUncloseEvent { p, _ ->
            p.toggleWardrobe(false)

            val paid = getAttribute(p, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, false)

            if (paid) {
                val newColor = getAttribute(player, PlayerKit.PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, -1)
                val newGender = getAttribute(player, PlayerKit.PLAYER_KIT_GENDER_SAVE_ATTIBUTE, -1)
                if (newGender > -1) mapAppearance(p, Gender.values()[newGender])
                if (newColor > -1) updateSkinColor(player, newColor)
                refreshAppearance(p)
                removeAttribute(p, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE)
            }
            removeAttribute(p, PlayerKit.PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE)
            removeAttribute(p, PlayerKit.PLAYER_KIT_GENDER_SAVE_ATTIBUTE)
            true
        }
    }

    /**
     * Opens clothes shop interface.
     */
    private fun openClothesShop(player: Player, male: Boolean) {
        setAttribute(player, PlayerKit.PLAYER_KIT_TORSO_SAVE_ATTRIBUTE, player.appearance.torso.look)
        setAttribute(player, PlayerKit.PLAYER_KIT_TORSO_COLOR_SAVE_ATTRIBUTE, player.appearance.torso.color)
        setAttribute(player, PlayerKit.PLAYER_KIT_ARMS_SAVE_ATTRIBUTE, player.appearance.arms.look)
        setAttribute(player, PlayerKit.PLAYER_KIT_ARMS_COLOR_SAVE_ATTRIBUTE, player.appearance.arms.color)
        setAttribute(player, PlayerKit.PLAYER_KIT_LEGS_SAVE_ATTRIBUTE, player.appearance.legs.look)
        setAttribute(player, PlayerKit.PLAYER_KIT_LEGS_COLOR_SAVE_ATTRIBUTE, player.appearance.legs.color)
        player.toggleWardrobe(true)
        val componentId = PlayerKit.CLOTHES_DISPLAY_COMPONENT_ID
        val iface = if (male) PlayerKit.THESSALIA_MALE_INTERFACE_ID else PlayerKit.THESSALIA_FEMALE_INTERFACE_ID
        sendPlayerOnInterface(player, iface, componentId)
    }

    /**
     * Handles button interactions for clothes shop.
     */
    private fun handleClothesButtons(player: Player, comp: Int, button: Int, male: Boolean) {
        if (button in listOf(180, 181, 297)) {
            pay(player, comp)
            return
        }
        val part =
            when (comp to button) {
                PlayerKit.THESSALIA_MALE_INTERFACE_ID to 182,
                PlayerKit.THESSALIA_FEMALE_INTERFACE_ID to 183 -> BodyPart.TORSO
                PlayerKit.THESSALIA_MALE_INTERFACE_ID to 183,
                PlayerKit.THESSALIA_FEMALE_INTERFACE_ID to 184 -> BodyPart.ARMS
                PlayerKit.THESSALIA_MALE_INTERFACE_ID to 184,
                PlayerKit.THESSALIA_FEMALE_INTERFACE_ID to 185 -> BodyPart.LEGS
                else -> null
            }
        part?.let { setAttribute(player, PlayerKit.PLAYER_KIT_TYPE_ATTRIBUTE, it) }
        when (button) {
            in (if (male) PlayerKit.maleTorsoButtonRange else PlayerKit.femaleTorsoButtonRange) ->
                updateAppearance(player, button, male, BodyPart.TORSO)
            in (if (male) PlayerKit.maleArmsButtonRange else PlayerKit.femaleArmsButtonRange) ->
                updateAppearance(player, button, male, BodyPart.ARMS)
            in (if (male) PlayerKit.maleLegsButtonRange else PlayerKit.femaleLegsButtonRange) ->
                updateAppearance(player, button, male, BodyPart.LEGS)
            in (if (male) PlayerKit.maleClothesColorButtonRange else PlayerKit.femaleClothesColorButtonRange) -> {
                val type = getAttribute(player, PlayerKit.PLAYER_KIT_TYPE_ATTRIBUTE, BodyPart.TORSO)
                updateColor(player, button, male, type)
            }
        }
    }

    /**
     * Closes clothes shop.
     */
    private fun closeClothesShop(player: Player): Boolean {
        player.toggleWardrobe(false)
        removeAttribute(player, PlayerKit.PLAYER_KIT_TYPE_ATTRIBUTE)
        playJingle(player, 266)

        if (!getAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, false)) {
            updateTorsoLook(player, getAttribute(player, PlayerKit.PLAYER_KIT_TORSO_SAVE_ATTRIBUTE, 0))
            updateTorsoColor(player, getAttribute(player, PlayerKit.PLAYER_KIT_TORSO_COLOR_SAVE_ATTRIBUTE, 0))
            updateArmsLook(player, getAttribute(player, PlayerKit.PLAYER_KIT_ARMS_SAVE_ATTRIBUTE, 0))
            updateArmsColor(player, getAttribute(player, PlayerKit.PLAYER_KIT_ARMS_COLOR_SAVE_ATTRIBUTE, 0))
            updateLegsLook(player, getAttribute(player, PlayerKit.PLAYER_KIT_LEGS_SAVE_ATTRIBUTE, 0))
            updateLegsColor(player, getAttribute(player, PlayerKit.PLAYER_KIT_LEGS_COLOR_SAVE_ATTRIBUTE, 0))
            refreshAppearance(player)
            runTask(player, 2) { sendNPCDialogue(player, NPCs.THESSALIA_548, "A marvellous choice. You look splendid!") }
        }

        removeAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE)
        return true
    }

    /**
     * Opens Yrsa shoe store.
     */
    private fun openShoeShop(player: Player) {
        val original = player.appearance.feet.color
        player.toggleWardrobe(true)
        setAttribute(player, PlayerKit.PLAYER_KIT_FEET_SAVE_ATTRIBUTE, original)
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
        refreshAppearance(player)
    }

    /**
     * Closes Yrsa shoe store.
     */
    private fun closeShoeShop(player: Player) {
        player.toggleWardrobe(false)
        playJingle(player, 266)
        if (!getAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, false)) {
            val original = getAttribute(player, PlayerKit.PLAYER_KIT_FEET_SAVE_ATTRIBUTE, player.appearance.feet.color)
            updateFeetColor(player, original)
            refreshAppearance(player)
        }

        removeAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE)
        removeAttribute(player, PlayerKit.PLAYER_KIT_FEET_SAVE_ATTRIBUTE)
        refreshAppearance(player)
    }

    private enum class BodyPart {
        TORSO,
        ARMS,
        LEGS
    }

    /**
     * Updates the appearance.
     *
     * @param player The player.
     * @param button The button.
     * @param male Whether the interface version is male.
     * @param part The body part being updated.
     */
    private fun updateAppearance(player: Player, button: Int, male: Boolean, part: BodyPart) {
        val (range, array) =
            when (part to male) {
                BodyPart.TORSO to true -> PlayerKit.maleTorsoButtonRange to PlayerKit.maleTorsoIDs
                BodyPart.ARMS to true -> PlayerKit.maleArmsButtonRange to PlayerKit.maleSleeveIDs
                BodyPart.LEGS to true -> PlayerKit.maleLegsButtonRange to PlayerKit.maleLegIDs
                BodyPart.TORSO to false -> PlayerKit.femaleTorsoButtonRange to PlayerKit.femaleTopIDs
                BodyPart.ARMS to false -> PlayerKit.femaleArmsButtonRange to PlayerKit.femaleArmIDs
                BodyPart.LEGS to false -> PlayerKit.femaleLegsButtonRange to PlayerKit.femaleLegIDs
                else -> return
            }
        val index = button - range.first
        if (index !in array.indices) return

        when (part) {
            BodyPart.TORSO -> player.appearance.torso.changeLook(array[index])
            BodyPart.ARMS -> player.appearance.arms.changeLook(array[index])
            BodyPart.LEGS -> player.appearance.legs.changeLook(array[index])
        }
        refreshAppearance(player)
    }

    /**
     * Updates wrist appearance preview.
     *
     * @param player The player previewing bracelets.
     * @param modelId The bracelet.
     */
    private fun updateArmguards(player: Player, modelId: Int) {
        val appearanceIndex = calculateBraceletIndex(modelId, player)
        sendModelOnInterface(
            player,
            Components.REINALD_SMITHING_EMPORIUM_593,
            PlayerKit.BRACELET_PREVIEW_COMPONENT_ID,
            modelId,
            1
        )
        setComponentVisibility(
            player,
            Components.REINALD_SMITHING_EMPORIUM_593,
            PlayerKit.BRACELET_PREVIEW_COMPONENT_ID,
            modelId == 0
        )
        updateWristsLook(player, appearanceIndex)
        player.debug("Using wrist appearance id =[$appearanceIndex]")
        refreshAppearance(player)
        sendPlayerOnInterface(player, Components.REINALD_SMITHING_EMPORIUM_593, 60)
    }

    /**
     * Updates the color of the selected body part.
     *
     * @param player The player.
     * @param button The button id.
     * @param male Whether the interface version is male.
     * @param type The body part whose color is being updated.
     */
    private fun updateColor(player: Player, button: Int, male: Boolean, type: BodyPart) {
        val subtract =
            if (male) PlayerKit.maleClothesColorButtonRange.first else PlayerKit.femaleClothesColorButtonRange.first
        val index = button - subtract
        val colorArray =
            when (type) {
                BodyPart.ARMS,
                BodyPart.TORSO -> PlayerKit.torsoColors
                BodyPart.LEGS -> PlayerKit.legColors
            }
        if (index in colorArray.indices) {
            when (type) {
                BodyPart.ARMS,
                BodyPart.TORSO -> player.appearance.torso.changeColor(colorArray[index])
                BodyPart.LEGS -> player.appearance.legs.changeColor(colorArray[index])
            }
            refreshAppearance(player)
        } else player.debug("Invalid color: button=$button, index=$index, male=$male, type=$type")
    }

    /**
     * Updates hair or hair-color selection for the female hairdresser interface.
     *
     * @param player The player.
     * @param button The button id.
     */
    private fun updateHairFemale(player: Player, button: Int) {
        when (button) {
            in PlayerKit.femaleColorButtonRange ->
                updateHairColor(player, button, PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID)
            in PlayerKit.femaleStyleButtonRange -> updateHair(player, button, PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID)
        }
    }

    /**
     * Updates hair, beard, or hair-color for the male hairdresser interface.
     *
     * @param player The player.
     * @param button The button id.
     */
    private fun updateHairMale(player: Player, button: Int) {
        val beardMode = getAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SETTINGS_ATTRIBUTE, false)
        when {
            beardMode && button !in PlayerKit.maleColorButtonRange -> updateBeard(player, button)
            button in PlayerKit.maleColorButtonRange ->
                updateHairColor(player, button, PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID)
            button in PlayerKit.maleStyleButtonRange ->
                updateHair(player, button, PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID)
        }
    }

    /**
     * Updates beard style.
     *
     * @param player The player.
     * @param button The button id.
     */
    private fun updateBeard(player: Player, button: Int) {
        var offset = 105
        when (button) {
            123 -> offset += 2
            126 -> offset += 4
            129 -> offset += 6
        }
        val index = PlayerKit.MALE_FACIAL[button - offset]
        updateBeardLook(player, index)
        refreshAppearance(player)
    }

    /**
     * Updates hairstyle look.
     *
     * @param player The player.
     * @param button The button id.
     * @param iface The interface version (male/female).
     */
    private fun updateHair(player: Player, button: Int, iface: Int) {
        val base = if (iface == PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID) 65 else 148
        val array = if (iface == PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID) PlayerKit.MALE_HAIR else PlayerKit.FEMALE_HAIR
        val subtractor = if (button in listOf(89, 90)) base + 2 else base
        updateHairLook(player, array[button - subtractor])
        refreshAppearance(player)
    }

    /**
     * Updates hair color.
     *
     * @param player The player.
     * @param button The button id.
     * @param iface The interface version (male/female).
     */
    private fun updateHairColor(player: Player, button: Int, iface: Int) {
        val offset = if (iface == PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID) 229 else 73
        updateHairColor(player, PlayerKit.HAIR_COLORS[button - offset])
        refreshAppearance(player)
    }

    /**
     * Updates the player feet color.
     *
     * @param player The player.
     * @param button The button id.
     */
    private fun updateFeet(player: Player, button: Int) {
        val idx = button - 15
        setVarp(player, 261, button - 14)
        updateFeetColor(player, PlayerKit.YRSA_COLOR_BUTTONS_COMPONENT_IDS[idx])
        refreshAppearance(player)
    }

    /**
     * Saves the selected gender.
     *
     * @param player The player.
     * @param male True for male.
     */
    private fun updateGender(player: Player, male: Boolean) {
        setAttribute(
            player,
            PlayerKit.PLAYER_KIT_GENDER_SAVE_ATTIBUTE,
            if (male) Gender.MALE.ordinal else Gender.FEMALE.ordinal
        )
    }

    /**
     * Updates the skin color.
     *
     * @param player The player.
     * @param button The button id.
     */
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
        refreshAppearance(player)
    }

    /**
     * Saves previous appearance for makeover mage.
     *
     * @param player The player.
     * @param newGender The gender.
     */
    private fun mapAppearance(player: Player, newGender: Gender) {
        val appearance = player.appearance
        val oldGender = appearance.gender
        if (oldGender == newGender) return

        val oldCache = appearance.appearanceCache.map { it.look to it.color }
        appearance.setGender(newGender)

        val src = if (oldGender == Gender.MALE) PlayerKit.MALE_LOOK_IDS else PlayerKit.FEMALE_LOOK_IDS
        val dst = if (newGender == Gender.MALE) PlayerKit.MALE_LOOK_IDS else PlayerKit.FEMALE_LOOK_IDS

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

    /**
     * Processes payment for hair, arm guards, and clothes update.
     *
     * @param player The player.
     * @param shop The interface id.
     */
    private fun pay(player: Player, shop: Int) {
        val changed =
            when (shop) {
                PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID,
                PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID -> {
                    val hair = getAttribute(player, PlayerKit.PLAYER_KIT_HAIR_SAVE_ATTRIBUTE, -1)
                    val hairColor = getAttribute(player, PlayerKit.PLAYER_KIT_HAIR_COLOR_SAVE_ATTRIBUTE, -1)
                    val beard = getAttribute(player, PlayerKit.PLAYER_KIT_BEARD_SAVE_ATTRIBUTE, -1)
                    hair != player.appearance.hair.look ||
                            hairColor != player.appearance.hair.color ||
                            beard != player.appearance.beard.look
                }
                PlayerKit.REINALD_BRACELETS_INTERFACE_ID -> {
                    val original =
                        getAttribute(player, PlayerKit.PLAYER_KIT_WRIST_SAVE_ATTRIBUTE, defaultBraceletAppearance(player))
                    original != player.appearance.wrists.look
                }
                PlayerKit.THESSALIA_FEMALE_INTERFACE_ID,
                PlayerKit.THESSALIA_MALE_INTERFACE_ID -> {
                    val torso = getAttribute(player, PlayerKit.PLAYER_KIT_TORSO_SAVE_ATTRIBUTE, -1)
                    val arms = getAttribute(player, PlayerKit.PLAYER_KIT_ARMS_SAVE_ATTRIBUTE, -1)
                    val legs = getAttribute(player, PlayerKit.PLAYER_KIT_LEGS_SAVE_ATTRIBUTE, -1)
                    torso != player.appearance.torso.look ||
                            arms != player.appearance.arms.look ||
                            legs != player.appearance.legs.look
                }
                else -> {
                    log(this.javaClass, Log.WARN, "Invalid shop =[$shop].")
                    return
                }
            }

        if (!changed) {
            sendMessage(player, "You must select an option first.")
            return
        }

        val price =
            when (shop) {
                PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID,
                PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID -> PlayerKit.HAIR_CHANGE_PRICE
                PlayerKit.REINALD_BRACELETS_INTERFACE_ID -> PlayerKit.WRISTS_CHANGE_PRICE
                PlayerKit.THESSALIA_FEMALE_INTERFACE_ID,
                PlayerKit.THESSALIA_MALE_INTERFACE_ID -> PlayerKit.CLOTHES_PRICE
                else -> 0
            }

        val inHouse = player.houseManager.isInHouse(player)
        if (!inHouse && !removeItem(player, price)) {
            val message =
                when (shop) {
                    PlayerKit.THESSALIA_FEMALE_INTERFACE_ID,
                    PlayerKit.THESSALIA_MALE_INTERFACE_ID -> "You need 1,000 gold coins to change your clothes."
                    PlayerKit.REINALD_BRACELETS_INTERFACE_ID -> "You need 500 gold coins to change your armguards."
                    PlayerKit.HAIRDRESSER_FEMALE_INTERFACE_ID,
                    PlayerKit.HAIRDRESSER_MALE_INTERFACE_ID -> "You need 2,000 gold coins to change your hairstyle."
                    else -> "You cannot afford this."
                }
            sendMessage(player, message)
            return
        }

        setAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)
    }

    /**
     * Processes payment for feet update.
     *
     * @param player The player.
     */
    private fun shoePay(player: Player) {
        val newColor = getAttribute(player, PlayerKit.PLAYER_KIT_FEET_SAVE_ATTRIBUTE, player.appearance.feet.color)

        if (newColor == player.appearance.feet.color) {
            closeInterface(player)
            return
        }
        if (!player.houseManager.isInHouse(player)) {
            if (!removeItem(player, PlayerKit.FEET_CHANGE_PRICE)) {
                sendMessage(player, "You need 500 gold coins to change your shoes.")
                return
            }
        }

        setAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)
        openDialogue(player, EndDialogue())
    }

    /**
     * Processes payment for gender and skin update.
     *
     * @param player The player.
     */
    private fun makeoverPay(player: Player) {
        val oldGender = player.appearance.gender
        val oldSkin = player.appearance.skin.color

        val newSkin = getAttribute(player, PlayerKit.PLAYER_KIT_SKIN_COLOR_SAVE_ATTRIBUTE, oldSkin)
        val newGender = Gender.values()[player.getAttribute(PlayerKit.PLAYER_KIT_GENDER_SAVE_ATTIBUTE, oldGender.ordinal)]

        if (newSkin == oldSkin && newGender == oldGender) {
            sendMessage(player, "You must select an option first.")
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
            sendMessage(player, "You cannot afford this.")
            closeInterface(player)
            return
        }

        setAttribute(player, PlayerKit.PLAYER_KIT_PAID_ATTRIBUTE, true)
        closeInterface(player)

        val npc = findNPC(NPCs.MAKE_OVER_MAGE_2676)
        if (npc != null && oldGender != newGender) {

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

            queueScript(player, 5, QueueStrength.SOFT) {
                npc.reTransform()
                return@queueScript stopExecuting(player)
            }
        }
    }

    /**
     * Shoe store dialogue after purchase.
     */
    inner class EndDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> npc(NPCs.YRSA_1301, FaceAnim.FRIENDLY, "I think they suit you.").also { stage++ }
                1 -> player(FaceAnim.HAPPY, "Thanks!").also { stage++ }
                2 -> end()
            }
        }
    }
}
