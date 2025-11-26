package core.game.node.entity.player.link.request.assist

import core.api.*
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestModule
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.Components
import java.util.*

/**
 * Represents the Assist session that manages the assist functionality between two players.
 * It handles experience sharing and applies restrictions based on a 24-hour period.
 *
 * Related Components:
 *  - [Request Assist interface][content.global.plugins.interfaces.RequestAssistInterface]
 */
class AssistSessionPulse : Pulse, RequestModule {
    /**
     * The player requesting assistance.
     */
    var player: Player? = null
        private set

    /**
     * The player being assisted.
     */
    var partner: Player? = null

    private val component = Component(Components.REQ_ASSIST_301).setUncloseEvent(closeEvent)

    /**
     * List of booleans representing the enabled/disabled skills for assistance.
     */
    val skills: BooleanArray = BooleanArray(9)

    /**
     * Flag indicating if the assistance session is restricted due to reaching maximum experience.
     */
    var isRestricted: Boolean = false
        private set

    /**
     * List of experience values for each skill.
     */
    var exp: DoubleArray = DoubleArray(9)

    private var kill = false

    private var time: Long = 0

    /**
     * Instantiates a new Assist session with given player and partner.
     *
     * @param player  the player requesting assistance.
     * @param partner the player being assisted.
     */
    constructor(player: Player?, partner: Player?) {
        this.player = player
        this.partner = partner
    }

    /**
     * Instantiates a new Assist session.
     */
    constructor()

    /**
     * Opens the assist session for the given players, checking restrictions first.
     *
     * @param player the player requesting assistance.
     * @param target the player being assisted.
     */
    override fun open(player: Player?, target: Player?) {
        if (player!!.ironmanManager.checkRestriction() || target!!.ironmanManager.checkRestriction()) {
            return
        }
        player.face(target)
        target.face(player)
        extend(player, target)
        getExtension(player).open()
    }

    /**
     * Opens the assist session.
     */
    fun open() {
        partner!!.addExtension(AssistSessionPulse::class.java, this)
        player!!.lock()
        player!!.interfaceManager.open(component)
        load()
        sendMessage(player!!, "Sending assistance response.")
        sendMessage(player!!, "You are assisting " + partner!!.username + ".")
        sendMessage(partner!!, "You are being assisted by " + player!!.username + ".")
        sendString(
            player!!,
            "Assist System XP Display - You are assisting " + partner!!.username + "",
            Components.REQ_ASSIST_301,
            101
        )
        sendString(player!!, "", Components.REQ_ASSIST_301, 10)
        visualize(player!!, ANIMATION, GRAPHICS)
        animate(partner!!, ANIMATION)
        playAudio(partner!!, 4010)
        playAudio(player!!, 4010)
        Pulser.submit(this)
        refresh()
    }

    /**
     * The close event for assist session.
     */
    private val closeEvent: CloseEvent
        get() = CloseEvent { player, _ ->
            save()
            player.removeExtension(AssistSessionPulse::class.java)
            partner!!.removeExtension(AssistSessionPulse::class.java)
            player.unlock()
            restoreTabs(player)
            sendMessage(player, "You have stopped assisting " + partner!!.username + ".")
            sendMessage(partner!!, player.username + " has stopped assisting you.")
            kill = true
            return@CloseEvent true
        }

    /**
     * Adds experience to a specific skill during the assist session.
     *
     * @param skill      the skill id.
     * @param experience the amount of experience to add.
     */
    fun addExperience(skill: Int, experience: Double) {
        if (isRestricted || totalExperience >= 30000) {
            isRestricted = true
            return
        }
        val skillIndex = getSkillIndex(skill)
        if (exp[skillIndex] + experience >= 30000) {
            exp[skillIndex] = 30000.0
            isRestricted = true
            return
        }
        exp[skillIndex] += experience
        if (exp[skillIndex] >= 30000) {
            exp[skillIndex] = 30000.0
        }
        refresh()
    }

    /**
     * Refreshes the assist session with the latest experience data.
     */
    fun refresh() {
        var value = 0
        var totalXp = 0.0
        for (i in 0..8) {
            if (exp[i] >= 30000) {
                exp[i] = 30000.0
            }
            sendString(player!!, "" + exp[i].toInt(), Components.REQ_ASSIST_301, CHILD_VALUES[i])
            if (skills[i]) {
                value = value or (1 shl CONFIG_VALUES[i].toInt())
            }
            totalXp += exp[i]
        }

        isRestricted = if (totalExperience >= 30000) true else false

        var message = ""
        if (isRestricted) {
            message =
                "You've earned the maximum XP from the Assist System within a 24-hour period. You can assist again in $timeLeft."
        }
        sendString(player!!, message, Components.REQ_ASSIST_301, 10)
        setVarp(player!!, 1087, value)
        setVarp(player!!, 1088, totalXp.toInt() * 10)
    }

    /**
     * Toggles the assist for a specific skill.
     *
     * @param id the skill id.
     */
    fun toggleButton(id: Byte) {
        skills[id.toInt()] = !skills[id.toInt()]
    }

    /**
     * Checks if the assist is toggled for a specific skill.
     *
     * @param skill the skill id.
     * @return `true` if the assist is toggled for the skill, `false` otherwise.
     */
    fun isToggled(skill: Int): Boolean {
        return skills[getSkillIndex(skill)]
    }

    /**
     * Gets the index corresponding to the skill id.
     *
     * @param skill the skill id.
     * @return the index of the skill in the assist session.
     */
    fun getSkillIndex(skill: Int): Int {
        when (skill) {
            Skills.RUNECRAFTING -> return 0
            Skills.CRAFTING -> return 1
            Skills.FLETCHING -> return 2
            Skills.CONSTRUCTION -> return 3
            Skills.FARMING -> return 4
            Skills.MAGIC -> return 5
            Skills.SMITHING -> return 6
            Skills.COOKING -> return 7
            Skills.HERBLORE -> return 8
        }
        return -1
    }

    /**
     * Gets the time left before the assist session can be resumed.
     *
     * @return the time left as a string.
     */
    private val timeLeft: String
        get() {
            val date2 = Date(time)
            val date1 = Date(System.currentTimeMillis())
            val calendar1 = Calendar.getInstance()
            val calendar2 = Calendar.getInstance()
            calendar1.time = date1
            calendar2.time = date2
            val milliseconds1 = calendar1.timeInMillis
            val milliseconds2 = calendar2.timeInMillis
            val diff = milliseconds2 - milliseconds1
            val diffSeconds = diff / 1000
            val diffMinutes = diff / (60 * 1000)
            val diffHours = diff / (60 * 60 * 1000)
            if (diffHours > 1) {
                return "$diffHours hours"
            }
            if (diffMinutes > 1) {
                return "$diffMinutes minutes"
            }
            return "$diffSeconds seconds"
        }


    /**
     * Gets the total experience accumulated during the assist session.
     *
     * @return the total experience.
     */
    private val totalExperience: Double
        get() {
            var xp = 0.0
            for (i in 0..8) {
                xp += exp[i]
            }
            return xp
        }

    /**
     * Loads the assist session data for the player.
     */
    fun load() {
        time = player!!.getSavedData().globalData.getAssistTime()
        if (time == 0L) {
            player!!.getSavedData().globalData.setAssistTime(System.currentTimeMillis() + TIME_OUT)
        }
        for (i in 0..8) {
            exp[i] = player!!.getSavedData().globalData.getAssistExperience()[i]
        }
    }

    /**
     * Saves the assist session data for the player.
     */
    fun save() {
        player!!.getSkills().refresh()
        player!!.getSavedData().globalData.setAssistTime(time)
        player!!.getSavedData().globalData.setAssistExperience(exp)
    }

    /**
     * Resets the assist session data for the player.
     */
    fun reset() {
        player!!.getSavedData().globalData.setAssistTime(0L)
        player!!.getSavedData().globalData.setAssistExperience(DoubleArray(9))
        load()
    }

    /**
     * Handles the pulse logic for updating the assist session.
     *
     * @return `true` if the assist session is to be killed, `false` otherwise.
     */
    override fun pulse(): Boolean {
        if (!player!!.location.withinDistance(partner!!.location, 20) || !partner!!.isActive || !player!!.isActive) {
            player!!.interfaceManager.close()
            return true
        }
        if (time < System.currentTimeMillis()) {
            time = System.currentTimeMillis() + TIME_OUT
            for (i in 0..8) {
                exp[i] = 0.0
            }
        }
        refresh()
        return kill
    }

    /**
     * Translates experience from one player to another during the assist session.
     *
     * @param p          the player.
     * @param slot       the skill slot.
     * @param experience the experience to be added.
     * @param mod        the experience modifier.
     * @return `true` if the experience was successfully translated, `false` otherwise.
     */
    fun translateExperience(p: Player, slot: Int, experience: Double, mod: Double): Boolean {
        var experience = experience
        if (player !== p) {
            val index = getSkillIndex(slot)
            if (index != -1) {
                if (!isRestricted) {
                    val level = p.getSkills().getLevel(slot)
                    val pLevel = player!!.getSkills().getLevel(slot)
                    if (pLevel < level) {
                        return false
                    }
                    if (skills[getSkillIndex(slot)]) {
                        if (exp[index] + experience >= 30000) {
                            experience -= (exp[index] + experience - 30000)
                        }
                        player!!.getSkills().addExperience(slot, experience)
                        addExperience(slot, experience * mod)
                        return true
                    }
                }
            }
        }
        return false
    }

    companion object {
        private val ANIMATION: Animation = Animation.create(7299)
        private val GRAPHICS: Graphics = Graphics.create(shared.consts.Graphics.ASSIST_LEVELS_1247)
        private const val TIME_OUT: Long = 86400000 // 60000; //86400000;
        private val CONFIG_VALUES = byteArrayOf(3, 4, 6, 8, 9, 11, 13, 14, 15)
        private val CHILD_VALUES = intArrayOf(46, 48, 50, 52, 54, 56, 58, 60, 62)

        /**
         * Extends the player with a new assist session.
         *
         * @param player   the requesting player.
         * @param partner  the assisting player.
         */
        fun extend(player: Player?, partner: Player?) {
            player!!.addExtension(AssistSessionPulse::class.java, AssistSessionPulse(player, partner))
        }

        /**
         * Gets extension.
         *
         * @param player the player.
         * @return the extension.
         */
        fun getExtension(player: Player?): AssistSessionPulse {
            return player!!.getExtension(AssistSessionPulse::class.java)
        }
    }
}
