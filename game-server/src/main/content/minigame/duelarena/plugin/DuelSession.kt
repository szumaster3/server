package content.minigame.duelarena.plugin

import core.api.*
import core.api.curePoison
import core.api.isPoisoned
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.container.ContainerType
import core.game.container.access.InterfaceContainer
import core.game.container.impl.EquipmentContainer
import core.game.global.action.EquipHandler.Companion.unequip
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.login.PlayerParser
import core.game.node.item.Item
import core.game.system.config.ItemConfigParser
import core.game.world.map.Location
import core.plugin.Plugin
import core.tools.RandomFunction
import java.text.DecimalFormat
import java.util.*

class DuelSession(
    val player: Player? = null,
    val other: Player? = null,
    val staked: Boolean,
) : ComponentPlugin() {
    val rules = arrayOfNulls<DuelRule>(DuelRule.values().size)
    var playerContainer: StakeContainer? = null
    var targetContainer: StakeContainer? = null
    var acceptState = 0
    var fightState = 0

    fun end() {
        removeAttribute(player!!, "duel:partner")
        other!!.removeAttribute("duel:partner")
        removeAttribute(player, "duel:staked")
        other.removeAttribute("duel:staked")
        other.removeAttribute("duel:forfeit")
        removeAttribute(player, "duel:forfeit")
        player.interfaceManager.close()
        player.interfaceManager.closeSingleTab()
        player.interfaceManager.restoreTabs()
        other.interfaceManager.close()
        other.interfaceManager.closeSingleTab()
        other.interfaceManager.restoreTabs()
        other.locks.unlockMovement()
        player.locks.unlockMovement()
        player.hintIconManager.clear()
        other.hintIconManager.clear()
        heal(player)
        heal(other)
        for (rule in rules) {
            if (rule != null) {
                player.locks.unlock(rule.name.lowercase(), true, player)
                other.locks.unlock(rule.name.lowercase(), true, other)
            }
        }
    }

    fun heal(p: Player) {
        p.fullRestore()
        if (isPoisoned(p)) {
            curePoison(p)
        }
        p.getSkills().restore()
    }

    fun openRules() {
        setAttribute(player!!, "duel:partner", other)
        other!!.setAttribute("duel:partner", player)
        setAttribute(player, "duel:staked", staked)
        setAttribute(other, "duel:staked", staked)
        removeAttribute(player, "duel:accepted")
        removeAttribute(other, "duel:accepted")
        openRules(player, other)
        openRules(other, player)
    }

    private fun openRules(
        player: Player,
        opponent: Player,
    ) {
        setVarp(player, 286, 0)
        if (staked) {
            val container = StakeContainer(player, this)
            if (player === this.player) {
                playerContainer = container
            } else {
                targetContainer = container
            }
            player.interfaceManager.open(STAKED_INTER)
            player.packetDispatch.sendString(opponent.username, 631, 25)
            player.packetDispatch.sendString(Integer.toString(opponent.properties.currentCombatLevel), 631, 27)
            player.packetDispatch.sendString("", 631, 28)
            container.open()
        } else {
            player.interfaceManager.open(FRIENDLY_INTER)
            player.packetDispatch.sendString(opponent.username, 637, 16)
            player.packetDispatch.sendString(Integer.toString(opponent.properties.currentCombatLevel), 637, 18)
            player.packetDispatch.sendString("", 637, 45)
        }
        setVarp(player, 286, 0)
    }

    fun leave(
        p: Player,
        type: Int,
    ) {
        if (fightState == 2) {
            return
        }
        val o = getOpposite(p)
        o.impactHandler.disabledTicks = 6
        o.teleport(RandomFunction.getRandomElement<Location>(DuelArea.RESPAWN_LOCATIONS))
        var victory = type == 0 || type == 2 || type == 1 && p.impactHandler.playerImpactLog.containsKey(o.details.uid)
        fightState = 2
        p.removeExtension(DuelSession::class.java)
        end()
        if (victory) {
            if (type == 0) {
                o.sendMessage("Well done! " + p.username + " resigned!")
            } else if (type == 2) {
                o.sendMessage("Well done! You have defeated " + p.username + "!")
            }
            victory(o)
        } else if (type == 1) {
            getContainer(player!!)!!.release()
            getContainer(o)!!.release()
            o.removeExtension(DuelSession::class.java)
            o.dialogueInterpreter.sendDialogue("Your opponent timed out, there was no winner!")
        }
        DuelArenaActivity.insertEntry(o, p)
    }

    fun victory(player: Player) {
        val component: Component =
            if (staked) STAKE_VICTORY else FRIENDLY_VICTORY
        player.interfaceManager.open(component)
        player.packetDispatch.sendString(getOpposite(player).username, component.id, if (staked) 23 else 22)
        player.packetDispatch.sendString(
            Integer.toString(getOpposite(player).properties.currentCombatLevel),
            component.id,
            if (staked) 22 else 21,
        )
        if (staked) {
            getContainer(player)!!.release()
            val targetContainer = getOppositeContainer(player)
            if (targetContainer!!.itemCount() > 0) {
                InterfaceContainer.generateItems(player, targetContainer.toArray(), arrayOf("Examine"), 634, 33)
            }
            val c = core.game.container.Container(28, ContainerType.ALWAYS_STACK)
            c.addAll(targetContainer)
            var log = "defeated => " + getOpposite(player).name + " and receieved {"
            for (i in c.toArray()) {
                if (i == null) {
                    continue
                }
                log += i.name + " x " + i.amount + ","
            }
            if (log[log.length - 1] == ',') {
                log = log.substring(0, log.length - 1)
            }
            log += "}"
        } else {
            player.removeExtension(DuelSession::class.java)
        }
    }

    private fun accept() {
        val session = player!!.getExtension<DuelSession>(DuelSession::class.java) ?: return
        when (session.acceptState) {
            2 -> {
                if (!session.checkRules(session.player!!) || !session.checkRules(session.other!!)) {
                    resetAccept()
                    return
                }
                session.player.lock(1)
                session.other.lock(1)
                session.acceptState = 3
                session.player.removeAttribute("duel:accepted")
                session.other.removeAttribute("duel:accepted")
                session.other.interfaceManager.open(if (session.staked) STAKED_RULE_INTER else FRIENDLY_RULE_INTER)
                session.player.interfaceManager.open(if (session.staked) STAKED_RULE_INTER else FRIENDLY_RULE_INTER)
                session.acceptState = 4
                session.player.interfaceManager.closeSingleTab()
                session.other.interfaceManager.closeSingleTab()
                session.player.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 7, 11, 12)
                session.other.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 7, 11, 12)
                val before = StringBuilder()
                val during = StringBuilder()
                if (hasEquipmentRules()) {
                    before.append("Some worn items will be taken off.<br>")
                }
                if (hasRule(DuelRule.NO_WEAPON) || hasRule(DuelRule.NO_SHIELD)) {
                    during.append("You can't use 2H weapons such as bows.<br>")
                }
                if (hasRule(DuelRule.NO_DRINKS) || hasRule(DuelRule.NO_FOOD)) {
                    before.append("Boosted stats will be restored.<br>")
                }
                if (hasRule(DuelRule.NO_PRAYER)) {
                    before.append("Existing prayers will be stopped.<br>")
                }
                if (before.length == 0) {
                    before.append("Nothing will be changed.<br>")
                }
                if (during.length == 0) {
                    during.append("You will fight using normal combat.<br>")
                }
                for (rule in rules) {
                    if (rule != null && rule.equipmentSlot == -1) {
                        during.append(rule.info + "<br>")
                    }
                }
                val interfaceId = if (session.staked) 626 else 639
                val childs =
                    if (staked) {
                        intArrayOf(
                            28,
                            29,
                            30,
                            31,
                            34,
                            35,
                            36,
                            37,
                            38,
                            39,
                            40,
                            41,
                            42,
                            43,
                            44,
                            45,
                        )
                    } else {
                        intArrayOf(16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32)
                    }
                clearChilds(session.player, interfaceId, *childs)
                clearChilds(session.other, interfaceId, *childs)
                var tokens: Array<String?> =
                    before
                        .toString()
                        .split("<br>".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                run {
                    var i = 0
                    while (i < tokens.size) {
                        session.player.packetDispatch.sendString(
                            tokens[i],
                            interfaceId,
                            if (this.staked) 28 + i else 16 + i,
                        )
                        session.other.packetDispatch.sendString(
                            tokens[i],
                            interfaceId,
                            if (this.staked) 28 + i else 16 + i,
                        )
                        i++
                    }
                }
                tokens =
                    during
                        .toString()
                        .split("<br>".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                var i = 0
                while (i < tokens.size) {
                    session.player.packetDispatch.sendString(tokens[i], interfaceId, if (staked) 34 + i else 22 + i)
                    session.other.packetDispatch.sendString(tokens[i], interfaceId, if (staked) 34 + i else 22 + i)
                    i++
                }
                if (staked) {
                    session.player.packetDispatch.sendString(
                        if (session.getContainer(player)!!.isEmpty) {
                            "Absolutely nothing!"
                        } else {
                            getDisplayMessage(
                                session
                                    .getContainer(
                                        player,
                                    )!!
                                    .toArray(),
                            )
                        },
                        626,
                        25,
                    )
                    session.player.packetDispatch.sendString(
                        if (session.getOppositeContainer(player)!!.isEmpty) {
                            "Absolutely nothing!"
                        } else {
                            getDisplayMessage(
                                session
                                    .getOppositeContainer(
                                        player,
                                    )!!
                                    .toArray(),
                            )
                        },
                        626,
                        26,
                    )
                    session.other.packetDispatch.sendString(
                        if (session.getOppositeContainer(player)!!.isEmpty) {
                            "Absolutely nothing!"
                        } else {
                            getDisplayMessage(
                                session
                                    .getOppositeContainer(
                                        player,
                                    )!!
                                    .toArray(),
                            )
                        },
                        626,
                        25,
                    )
                    session.other.packetDispatch.sendString(
                        if (session.getOppositeContainer(other!!)!!.isEmpty) {
                            "Absolutely nothing!"
                        } else {
                            getDisplayMessage(
                                session
                                    .getOppositeContainer(
                                        other,
                                    )!!
                                    .toArray(),
                            )
                        },
                        626,
                        26,
                    )
                }
                session.updateToolTip(player, "")
                session.updateToolTip(getOpposite(player), "")
            }

            6 -> {
                session.player!!.lock(2)
                session.other!!.lock(2)
                session.player.sendMessage("Accepted stake and duel options.")
                session.other.sendMessage("Accepted stake and duel options.")
                session.acceptState = 7
                DuelArenaActivity.getDuelArea(hasRule(DuelRule.OBSTACLES)).duel(this)
                session.applyRules(session.player)
                session.applyRules(session.other)
                session.acceptState = 8
            }
        }
    }

    fun applyRules(player: Player) {
        for (rule in rules) {
            if (rule != null) {
                if (rule.equipmentSlot != -1 && player.equipment[rule.equipmentSlot] != null) {
                    unequip(player, rule.equipmentSlot, player.equipment.getId(rule.equipmentSlot))
                }
                player.locks.lock(rule.name.lowercase(Locale.getDefault()), 100000)
            }
        }
        if (hasRule(DuelRule.NO_SHIELD)) {
            val i = player.equipment[EquipmentContainer.SLOT_WEAPON]
            if (i != null && i.definition.getConfiguration(ItemConfigParser.TWO_HANDED, false)) {
                unequip(player, EquipmentContainer.SLOT_WEAPON, i.id)
            }
        }
        if (hasRule(DuelRule.NO_DRINKS) || hasRule(DuelRule.NO_FOOD)) {
            player.getSkills().restore()
        }
        if (hasRule(DuelRule.NO_PRAYER)) {
            player.prayer.reset()
        }
    }

    private fun toggleRule(
        p: Player,
        index: Int,
    ) {
        val session = player!!.getExtension<DuelSession>(DuelSession::class.java) ?: return
        session.resetAccept()
        if (rules[index] != null) {
            rules[index] = null
        } else {
            if (index < 3) {
                var count = 0
                for (i in 0..2) {
                    if (rules[i] != null) {
                        count++
                    }
                }
                if (count == 2) {
                    session.updateToolTip(
                        player,
                        "You can't have No Ranged, No Melee AND No Magic, how would you fight?",
                    )
                    p.packetDispatch.sendMessage(
                        "You can't have No Ranged, No Melee AND No Magic, how would you fight?",
                    )
                    return
                }
            } else if (index == 8 && rules[9] != null || index == 9 && rules[8] != null) {
                rules[9] = null
                rules[8] = rules[9]
                p.packetDispatch.sendMessage(
                    if (index ==
                        8
                    ) {
                        "You can't have obstacles if you want No Movement."
                    } else {
                        "You can't have No Movement in an area with obstacles."
                    },
                )
            }
            if (index == 15 || index == 16) {
                player.sendMessage("Beware: You won't be able to use two-handed weapons such as bows.")
            }
            rules[index] = DuelRule.values()[index]
        }
        var value = 0
        for (i in rules.indices) {
            if (rules[i] != null) {
                value = value or (1 shl rules[i]!!.configIndex)
            }
        }
        setVarp(player, 286, value)
        setVarp(other!!, 286, value)
    }

    private fun checkRules(player: Player): Boolean {
        val c = core.game.container.Container(60)
        for (rule in rules) {
            if (rule != null && rule.equipmentSlot != -1 && player.equipment[rule.equipmentSlot] != null) {
                c.add(player.equipment[rule.equipmentSlot])
            }
        }
        if (hasRule(DuelRule.FUN_WEAPONS) && !hasFunWeapon(player)) {
            updateToolTip(player, "Fun Weapons is selected but you don't have a 'fun weapon'.")
            updateToolTip(
                getOpposite(player),
                "Fun Weapons is selected but your opponent does not have a 'fun weapon'.",
            )
            return false
        }
        if (staked) {
            c.addAll(getContainer(player))
            c.addAll(getOppositeContainer(player))
        }
        if (!player.inventory.hasSpaceFor(c)) {
            updateToolTip(player, "You do not have enough space for the items removed and/or the stake.")
            updateToolTip(
                getOpposite(player),
                "Your opponent does not have enough space for the items removed and/or the stake.",
            )
            return false
        }
        return true
    }

    override fun handle(
        player: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        var button = button
        val session = player.getExtension<DuelSession>(DuelSession::class.java) ?: return false
        when (component.id) {
            626 ->
                if (button == 53) {
                    requestAccept(player, component)
                }

            639 ->
                if (button == 35) {
                    requestAccept(player, component)
                }

            631 -> {
                if (button == 107) {
                    decline(player)
                } else if (button == 102) {
                    requestAccept(player, component)
                }
                val c = session.getContainer(player)
                if (button == 103) {
                    var amount = 0
                    when (opcode) {
                        155 -> amount = 1
                        196 -> amount = 5
                        124 -> amount = 10
                        199 -> amount = c!!.getAmount(c[slot])
                        234 ->
                            sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                                c!!.withdraw(slot, value as Int)
                            }
                    }
                    c!!.withdraw(slot, amount)
                } else if (button == 104) {
                }
                if (button == 107) {
                    player.interfaceManager.close()
                }
                if (button >= 57 && button <= 67) {
                    var index = 12 + (57 - button) * -1
                    if (button == 60) {
                        index = DuelRule.values().size - 1
                    } else if (button == 61) {
                        index = 15
                    } else if (button == 62) {
                        index = DuelRule.NO_BODY.ordinal
                    } else if (button == 63) {
                        index = DuelRule.NO_SHIELD.ordinal
                    } else if (button == 64) {
                        index = DuelRule.NO_LEGS.ordinal
                    } else if (button == 67) {
                        index = DuelRule.NO_GLOVES.ordinal
                    } else if (button == 66) {
                        index = DuelRule.NO_BOOTS.ordinal
                    }
                    session.toggleRule(player, index)
                }
                if (button >= 29 && button <= 53) {
                    if (button > 49) {
                        button--
                    }
                    var index = (button - 29) / 2
                    if (index == 11) {
                        index = 10
                    } else if (index == 10) {
                        index = 11
                    }
                    session.toggleRule(player, index)
                }
                return false
            }

            637 -> {
                if (button == 86) {
                    player.interfaceManager.close()
                } else if (button == 83 && session.acceptState < 2) {
                    requestAccept(player, component)
                }
                if (button >= 46 && button <= 56) {
                    var index = 0
                    when (button) {
                        46 -> index = DuelRule.NO_HATS.ordinal
                        47 -> index = DuelRule.NO_CAPES.ordinal
                        48 -> index = DuelRule.NO_AMULET.ordinal
                        49 -> index = DuelRule.NO_ARROWS.ordinal
                        50 -> index = DuelRule.NO_WEAPON.ordinal
                        51 -> index = DuelRule.NO_BODY.ordinal
                        52 -> index = DuelRule.NO_SHIELD.ordinal
                        53 -> index = DuelRule.NO_LEGS.ordinal
                        54 -> index = DuelRule.NO_RINGS.ordinal
                        55 -> index = DuelRule.NO_BOOTS.ordinal
                        56 -> index = DuelRule.NO_GLOVES.ordinal
                    }
                    session.toggleRule(player, index)
                }
                if (button >= 19 && button <= 42) {
                    session.toggleRule(player, (button - 19) / 2)
                }
                return false
            }

            336 -> {
                val c1 = if (player === session.player) session.playerContainer else session.targetContainer
                when (opcode) {
                    155 -> c1!!.offer(slot, 1)
                    196 -> c1!!.offer(slot, 5)
                    124 -> c1!!.offer(slot, 10)
                    199 -> c1!!.offer(slot, player.inventory.getAmount(player.inventory[slot].id))
                    234 ->
                        sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                            c1!!.offer(slot, value as Int)
                        }

                    9 -> player.packetDispatch.sendMessage(player.inventory[slot].definition.examine)
                }
            }
        }
        return true
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(631, this)
        ComponentDefinition.put(637, this)
        ComponentDefinition.put(626, this)
        ComponentDefinition.put(639, this)
        StakeContainer.OVERLAY.plugin = this
        return this
    }

    fun requestAccept(
        player: Player,
        component: Component?,
    ) {
        val session = player.getExtension<DuelSession>(DuelSession::class.java) ?: return

        if (session.acceptState == (if (session.acceptState < 3) 1 else 5) &&
            session
                .getOpposite(player)
                .getAttribute("duel:accepted", false)
        ) {
            session.acceptState = if (session.acceptState == 1) 2 else 6
            session.accept()
            return
        }
        if (session.acceptState == 0 || session.acceptState == 4) {
            session.acceptState = if (session.acceptState == 0) 1 else 5
            setAttribute(player, "duel:accepted", true)
            session.updateToolTip(player, "Waiting for other player...")
            session.updateToolTip(session.getOpposite(player), "Other player accepted.")
        }
    }

    fun resetAccept() {
        acceptState = if (acceptState < 3) 0 else 4
        removeAttribute(player!!, "duel:accepted")
        removeAttribute(other!!, "duel:accepted")
    }

    fun clearChilds(
        player: Player,
        interfaceId: Int,
        vararg childs: Int,
    ) {
        for (i in childs) {
            player.packetDispatch.sendString("", interfaceId, i)
        }
    }

    private fun updateToolTip(
        player: Player,
        message: String,
    ) {
        val session = player.getExtension<DuelSession>(DuelSession::class.java) ?: return
        var interfaceId = 631
        var child = 28
        if (!staked) {
            interfaceId =
                if (acceptState < 4) FRIENDLY_INTER.id else FRIENDLY_RULE_INTER.id
            child = if (acceptState < 4) 45 else 33
        } else if (acceptState > 3) {
            interfaceId = STAKED_RULE_INTER.id
            child = 45
        }
        player.packetDispatch.sendString(message, interfaceId, child)
    }

    private fun getDisplayMessage(items: Array<Item?>): String {
        var message = "Absolutely nothing!"
        if (items.size > 0) {
            message = ""
            for (i in items.indices) {
                if (items[i] == null) {
                    continue
                }
                message = message + "<col=FF9040>" + items[i]!!.name
                if (items[i]!!.amount > 1) {
                    message = "$message<col=FFFFFF> x "
                    message = message + "<col=FFFF00>" + getFormattedNumber(items[i]!!.amount) + "<br>"
                } else {
                    message = "$message<br>"
                }
            }
        }
        return message
    }

    fun hasFunWeapon(player: Player): Boolean {
        val c = core.game.container.Container(60)
        c.addAll(player.inventory)
        c.addAll(player.equipment)
        for (item in c.toArray()) {
            if (item == null) {
                continue
            }
            if (item.definition.getConfiguration("fun_weapon", false)) {
                return true
            }
        }
        return false
    }

    fun isRestrictedEquipment(item: Item?): Boolean {
        if (item == null) {
            return false
        }
        val slot = item.definition.getConfiguration(ItemConfigParser.EQUIP_SLOT, -1)
        if (slot == -1) {
            return false
        }
        val twoHanded = item.definition.getConfiguration(ItemConfigParser.TWO_HANDED, false)
        if (slot == EquipmentContainer.SLOT_WEAPON && twoHanded && hasRule(DuelRule.NO_SHIELD)) {
            return true
        }
        for (rule in rules) {
            if (rule == null) {
                continue
            }
            if (rule.equipmentSlot == slot) {
                return true
            }
        }
        return false
    }

    fun hasEquipmentRules(): Boolean {
        for (rule in rules) {
            if (rule != null && rule.ordinal >= DuelRule.NO_HATS.ordinal) {
                return true
            }
        }
        return false
    }

    fun hasRule(r: DuelRule): Boolean = rules[r.ordinal] != null

    fun getOppositeContainer(player: Player): StakeContainer? =
        if (player ===
            this.player
        ) {
            targetContainer
        } else {
            playerContainer
        }

    fun getContainer(player: Player): StakeContainer? = if (player === this.player) playerContainer else targetContainer

    private fun getFormattedNumber(amount: Int): String = DecimalFormat("#,###,##0").format(amount.toLong()).toString()

    val ruleSize: Int
        get() {
            var count = 0
            for (rule in rules) {
                if (rule != null) {
                    count++
                }
            }
            return count
        }

    fun getOpposite(player: Player): Player = if (this.player === player) other!! else this.player!!

    fun getOther(player: Player): Player = if (player === this.player) other!! else player

    companion object {
        private val FRIENDLY_INTER =
            Component(637).setUncloseEvent { player, c ->
                decline(player)
                true
            }
        private val STAKED_INTER =
            Component(631).setUncloseEvent { player, c ->
                decline(player)
                true
            }
        private val FRIENDLY_RULE_INTER =
            Component(639).setUncloseEvent { player, c ->
                decline(player)
                true
            }
        private val STAKED_RULE_INTER =
            Component(626).setUncloseEvent { player, c ->
                decline(player)
                true
            }
        private val FRIENDLY_VICTORY = Component(633)
        private val STAKE_VICTORY =
            Component(634).setUncloseEvent { player, c ->
                reward(player)
                true
            }

        fun decline(player: Player) {
            val session = player.getExtension<DuelSession>(DuelSession::class.java) ?: return
            if (session.acceptState == 3 || session.acceptState == 7) {
                return
            }
            if (session.staked) {
                session.playerContainer!!.release()
                session.targetContainer!!.release()
                session.resetAccept()
            }
            session.player!!.removeExtension(DuelSession::class.java)
            session.other!!.removeExtension(DuelSession::class.java)
            session.end()
            if (player === session.other) {
                session.player.packetDispatch.sendMessage(
                    "Other player declined " + (if (session.staked) "stake and " else "") + "duel options.",
                )
            } else {
                session.other.packetDispatch.sendMessage(
                    "Other player declined " + (if (session.staked) "stake and " else "") + "duel options.",
                )
            }
        }

        fun reward(player: Player) {
            val session = player.getExtension<DuelSession>(DuelSession::class.java)
            if (session == null || session.fightState == 4) {
                return
            }
            val targetContainer = session.getOppositeContainer(player)
            if (!player.inventory.hasSpaceFor(targetContainer)) {
                player.bank.addAll(targetContainer)
                player.sendMessage("An error occured & the stake transfered to your bank.")
            } else {
                session.fightState = 4
                player.inventory.addAll(targetContainer)
            }
            player.removeExtension(DuelSession::class.java)
            PlayerParser.save(player)
        }
    }
}
