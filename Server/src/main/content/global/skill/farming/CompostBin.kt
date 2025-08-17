package content.global.skill.farming

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds
import java.util.concurrent.TimeUnit

class CompostBin(
    val player: Player,
    val bin: CompostBins,
) {
    private var items = ArrayList<Int>()
    var isSuperCompost = true
    var isTomatoes = true
    var isClosed = false
    var finishedTime = 0L
    var isFinished = false

    fun reset() {
        items.clear()
        isSuperCompost = true
        isTomatoes = true
        isClosed = false
        finishedTime = 0L
        isFinished = false
        updateBit()
    }

    fun isFull(): Boolean = items.size == 15

    fun close() {
        isClosed = true
        sendMessage(player, "You close the compost bin.")
        animate(player, Animations.PUSH_COMPOST_BIN_810)
        playAudio(player, Sounds.COMPOST_CLOSE_2428)
        sendMessage(player, "The contents have begun to rot.", 1)
        finishedTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(RandomFunction.random(35, 50).toLong())
        updateBit()
    }

    fun open() {
        isClosed = false
        animate(player, Animations.PUSH_COMPOST_BIN_810)
        playAudio(player, Sounds.COMPOST_OPEN_2429)
        sendMessage(player, "You open the compost bin.")
        updateBit()
    }

    fun takeItem(): Item? {
        if (items.isEmpty()) return null
        val item = items[0]
        items.remove(item)
        if (items.isEmpty()) {
            isFinished = false
            finishedTime = 0L
            isTomatoes = true
            isSuperCompost = true
            isClosed = false
        }
        playAudio(player, Sounds.FARMING_SCOOP_2443)
        updateBit()
        if (isSuperCompost) {
            rewardXP(player, Skills.FARMING, 8.5)
        } else {
            rewardXP(player, Skills.FARMING, 4.5)
        }
        return Item(item)
    }

    fun isDefaultState(): Boolean = (isFinished == false && finishedTime == 0L && items.size == 0)

    fun isReady(): Boolean = System.currentTimeMillis() > finishedTime && finishedTime != 0L

    fun checkSuperCompostItem(id: Int): Boolean = when (id) {
        Items.WATERMELON_5982,
        Items.PINEAPPLE_2114,
        Items.CALQUAT_FRUIT_5980,
        Items.OAK_ROOTS_6043,
        Items.WILLOW_ROOTS_6045,
        Items.MAPLE_ROOTS_6047,
        Items.YEW_ROOTS_6049,
        Items.MAGIC_ROOTS_6051,
        Items.COCONUT_5974,
        Items.COCONUT_SHELL_5978,
        Items.PAPAYA_FRUIT_5972,
        Items.JANGERBERRIES_247,
        Items.WHITE_BERRIES_239,
        Items.POISON_IVY_BERRIES_6018,
        Items.CLEAN_TOADFLAX_2998,
        Items.CLEAN_AVANTOE_261,
        Items.CLEAN_KWUARM_263,
        Items.CLEAN_CADANTINE_265,
        Items.CLEAN_DWARF_WEED_267,
        Items.CLEAN_TORSTOL_269,
        Items.CLEAN_LANTADYME_2481,
        Items.CLEAN_SNAPDRAGON_3000,
        Items.GRIMY_TOADFLAX_3049,
        Items.GRIMY_KWUARM_213,
        Items.GRIMY_AVANTOE_211,
        Items.GRIMY_TORSTOL_219,
        Items.GRIMY_DWARF_WEED_217,
        Items.GRIMY_LANTADYME_2485,
        Items.GRIMY_SNAPDRAGON_3051,
        Items.GRIMY_CADANTINE_215,
            -> true

        else -> false
    }

    fun addItem(item: Int) {
        if (!isFull()) {
            items.add(item)
            if (!checkSuperCompostItem(item)) {
                isSuperCompost = false
            }
            if (item != Items.TOMATO_1982) isTomatoes = false
        }
        updateBit()
    }

    fun addItem(item: Item) {
        val remaining = 15 - items.size
        val amount = if (item.amount > remaining) {
            remaining
        } else {
            item.amount
        }
        for (i in 0 until amount) {
            playAudio(player, Sounds.FARMING_PUTIN_2441)
            addItem(item.id)
        }
    }

    private fun updateBit() {
        if (items.isNotEmpty()) {
            if (isClosed) {
                setVarbit(player, bin.varbit, 0x40)
            } else if (isFinished) {
                var finalValue = if (items.size == 15) 15 else 14
                if (isTomatoes) {
                    finalValue += 0x80
                } else if (isSuperCompost) {
                    finalValue += 0x20
                }
                setVarbit(player, bin.varbit, finalValue)
            } else {
                var finalValue = items.size
                if (isTomatoes) finalValue += 0x80
                setVarbit(player, bin.varbit, finalValue)
            }
        } else {
            setVarbit(player, bin.varbit, 0)
        }
    }

    fun save(root: JsonObject) {
        val binObject = JsonObject()
        binObject.addProperty("isSuper", this.isSuperCompost)

        val items = JsonArray()
        for (id in this.items) {
            items.add(id)
        }
        binObject.add("items", items)

        binObject.addProperty("finishTime", finishedTime)
        binObject.addProperty("isTomato", isTomatoes)
        binObject.addProperty("isClosed", isClosed)
        binObject.addProperty("isFinished", isFinished)

        root.add("binData", binObject)
    }

    fun parse(_data: JsonObject) {
        val isSuper = if (_data.has("isSuper")) _data.get("isSuper").asBoolean else true

        if (_data.has("items")) {
            val itemsArray = _data.getAsJsonArray("items")
            itemsArray?.forEach {
                addItem(it.asInt)
            }
        }

        if (_data.has("finishTime")) finishedTime = _data.get("finishTime").asLong
        if (_data.has("isTomato")) isTomatoes = _data.get("isTomato").asBoolean
        if (_data.has("isClosed")) isClosed = _data.get("isClosed").asBoolean
        if (_data.has("isFinished")) isFinished = _data.get("isFinished").asBoolean

        updateBit()
    }

    fun finish() {
        items = if (isTomatoes) {
            items.map { Items.ROTTEN_TOMATO_2518 } as ArrayList<Int>
        } else if (isSuperCompost) {
            items.map { Items.SUPERCOMPOST_6034 } as ArrayList<Int>
        } else {
            items.map { Items.COMPOST_6032 } as ArrayList<Int>
        }
        isFinished = true
    }

    fun convert() {
        if (!isSuperCompost) {
            items = items.map { Items.SUPERCOMPOST_6034 } as ArrayList<Int>
            isSuperCompost = true
            updateBit()
        }
    }
}
