package content.minigame.allfiredup.plugin

import core.api.*
import core.api.getQuestStage
import core.api.isQuestComplete
import core.api.setQuestStage
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.tools.DARK_BLUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests

class AFUBeaconPlugin : InteractionListener {
    private val VALID_LOGS = intArrayOf(Items.LOGS_1511, Items.OAK_LOGS_1521, Items.WILLOW_LOGS_1519, Items.MAPLE_LOGS_1517, Items.YEW_LOGS_1515, Items.MAGIC_LOGS_1513)
    private val FILL_ANIM = Animation(Animations.MOVE_FORWARD_TO_CLIMB_UP_LADDER_9136)
    private val LIGHT_ANIM = Animation(Animations.TWIST_7307)
    override fun defineListeners() {
        on(IntType.SCENERY, "add-logs", "light") { player, node ->
            val beacon = AFUBeacon.forLocation(node.location)
            val questComplete = player.questRepository.isComplete(Quests.ALL_FIRED_UP)
            val questStage = player.questRepository.getStage(Quests.ALL_FIRED_UP)

            if ((beacon != AFUBeacon.RIVER_SALVE && beacon != AFUBeacon.RAG_AND_BONE && !questComplete) ||
                (beacon == AFUBeacon.RIVER_SALVE && questStage < 20 && !questComplete) ||
                (beacon == AFUBeacon.RAG_AND_BONE && questStage < 50 && !questComplete)
            ) {
                sendPlayerDialogue(player, "I probably shouldn't mess with this.", FaceAnim.THINKING)
                return@on true
            }
            player.debug(beacon.getState(player).name)
            when (beacon.getState(player)) {
                BeaconState.EMPTY -> fillBeacon(player, beacon, questComplete)
                BeaconState.DYING -> restoreBeacon(player, beacon, questComplete)
                BeaconState.FILLED -> lightBeacon(player, beacon, questComplete)
                BeaconState.LIT, BeaconState.WARNING -> {
                    player.debug("INVALID BEACON STATE")
                }
            }
            return@on true
        }
    }

    private fun fillBeacon(player: Player, beacon: AFUBeacon, questComplete: Boolean, ) {
        when (beacon) {
            AFUBeacon.MONASTERY -> {
                if (getStatLevel(player, Skills.PRAYER) < 31) {
                    player.dialogueInterpreter.sendDialogues(core.game.node.entity.npc
                            .NPC(beacon.keeper)
                            .getShownNPC(player), FaceAnim.ANGRY, "You must join the monastery to light this beacon!",)
                    return
                }
            }

            AFUBeacon.GWD -> {
                if (getVarbit(player, beacon.varbit) < 1) {
                    sendDialogue(player, "You must repair the windbreak before you can light this beacon.")
                    return
                }
            }

            AFUBeacon.GOBLIN_VILLAGE -> {
                if (!isQuestComplete(player, Quests.THE_LOST_TRIBE)) {
                    player.dialogueInterpreter.sendDialogues(core.game.node.entity.npc
                            .NPC(beacon.keeper)
                            .getShownNPC(player), FaceAnim.THINKING, "We no trust you outsider. You no light our beacon.", "(Complete Lost Tribe to use this beacon.)",)
                    return
                }
            }

            else -> {}
        }

        if (getStatLevel(player, Skills.FIREMAKING) < beacon.fmLevel) {
            sendDialogue(player, "You need ${beacon.fmLevel} Firemaking to light this beacon.")
            return
        }

        val logs = getLogs(player, 20)
        if (logs.id != 0 && player.inventory.remove(logs)) {
            player.lock()

            var session: AFUSession? = null
            if (questComplete) {
                session = player.getAttribute("afu-session", null)
                if (session == null) {
                    session = AFUSession(player)
                    session.init()
                }
            }

            GameWorld.Pulser.submit(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> player.animator.animate(FILL_ANIM)
                            1 -> {
                                beacon.fillWithLogs(player)
                                if (questComplete) {
                                    session?.setLogs(beacon.ordinal, logs)
                                }
                            }

                            2 -> {
                                player.unlock()
                                resetAnimator(player)
                                return true
                            }
                        }
                        return false
                    }
                },
            )
        } else {
            sendDialogue(player, "You need some logs to do this.")
        }
    }

    private fun lightBeacon(player: Player, beacon: AFUBeacon, questComplete: Boolean, ) {
        var session: AFUSession? = null
        if (questComplete) {
            session = player.getAttribute("afu-session", null)
            if (session == null) return
        }

        if (inInventory(player, Items.TINDERBOX_590, 1)) {
            player.lock()
            GameWorld.Pulser.submit(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> player.animator.animate(LIGHT_ANIM)
                            1 -> {
                                beacon.light(player)
                                if (questComplete) {
                                    session?.startTimer(beacon.ordinal)
                                    if (session?.getLitBeacons() == 6 && !player.hasFireRing()) {
                                        sendDoubleItemDialogue(player, -1, Items.RING_OF_FIRE_13659, DARK_BLUE +
                                                    "Congratulations! You have kept six beacons alight simultaneously!</col><br></br>Why don't you talk to King Roald in Varrock? He has<br></br>a special rewawrd for you.",)
                                        sendMessage(player, "Congratulations! You've lit six beacons simultaneously!")
                                        sendMessage(player, "Why don't you talk to King Roald in Varrock? He has a special reward for you.")
                                        setAttribute(player, "/save:afu-mini:ring", true)
                                    }
                                    if (session?.getLitBeacons() == 10 && !player.hasFlameGloves()) {
                                        sendDoubleItemDialogue(player, -1, Items.FLAME_GLOVES_13660, DARK_BLUE +
                                                    "Congratulations! You have kept ten beacons alight simultaneously!</col><br></br>Why don't you talk to King Roald in Varrock? He has<br></br>a special rewawrd for you.",)
                                        sendMessage(player, "Congratulations! You've lit ten beacons simultaneously!")
                                        sendMessage(player, "Why don't you talk to King Roald in Varrock? He has a special reward for you.")
                                        setAttribute(player, "/save:afu-mini:gloves", true)
                                    }
                                    if (session?.getLitBeacons() == 14 && !player.hasInfernoAdze()) {
                                        sendDoubleItemDialogue(player, -1, Items.INFERNO_ADZE_13661, DARK_BLUE + "Congratulations! You have kept all fourteen beacons alight simultaneously!</col><br></br>Why don't you talk to King Roald in Varrock? He has<br></br>a special rewawrd for you.",)
                                        sendMessage(player, "Congratulations! You've lit all fourteen beacons simultaneously!")
                                        sendMessage(player, "Why don't you talk to King Roald in Varrock? He has a special reward for you.")
                                        setAttribute(player, "/save:afu-mini:adze", true)
                                    }
                                    var experience = beacon.experience
                                    experience += session?.getBonusExperience() ?: 0.0
                                    rewardXP(player, Skills.FIREMAKING, experience)
                                } else {
                                    setQuestStage(
                                        player,
                                        Quests.ALL_FIRED_UP,
                                        getQuestStage(player, Quests.ALL_FIRED_UP) + 10,
                                    )
                                }
                            }

                            2 -> player.unlock().also { return true }
                        }
                        return false
                    }
                },
            )
        } else {
            sendDialogue(player, "You need a tinderbox to light this.")
        }
    }

    private fun restoreBeacon(player: Player, beacon: AFUBeacon, questComplete: Boolean, ) {
        var session: AFUSession? = null
        if (questComplete) {
            session = player.getAttribute("afu-session", null)
            if (session == null) return
        }

        val logs = getLogs(player, 5)
        if (logs.id != 0 && player.inventory.remove(logs)) {
            player.lock()
            GameWorld.Pulser.submit(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> player.animator.animate(FILL_ANIM)
                            1 -> beacon.light(player).also {
                                if (questComplete) {
                                    session?.refreshTimer(beacon, logs.id)
                                } else {
                                    setQuestStage(player, Quests.ALL_FIRED_UP, 80)
                                }
                            }
                            2 -> player.unlock().also { return true }
                        }
                        return false
                    }
                },
            )
        } else {
            sendDialogue(player, "You need some logs to do this.")
        }
    }

    private fun getLogs(player: Player, amount: Int, ): Item {
        var logId = 0
        for (log in VALID_LOGS) {
            if (player.inventory.getAmount(log) >= amount) {
                logId = log
                break
            }
        }
        return Item(logId, amount)
    }

    fun Player.hasFireRing(): Boolean =
        inventory.containsItem(Item(Items.RING_OF_FIRE_13659)) ||
            bank.containsItem(Item(Items.RING_OF_FIRE_13659)) ||
            equipment.containsItem(Item(Items.RING_OF_FIRE_13659))

    fun Player.hasFlameGloves(): Boolean =
        inventory.containsItem(Item(Items.FLAME_GLOVES_13660)) ||
            bank.containsItem(Item(Items.FLAME_GLOVES_13660)) ||
            equipment.containsItem(Item(Items.FLAME_GLOVES_13660))

    fun Player.hasInfernoAdze(): Boolean =
        inventory.containsItem(Item(Items.INFERNO_ADZE_13661)) ||
            bank.containsItem(Item(Items.INFERNO_ADZE_13661)) ||
            equipment.containsItem(Item(Items.INFERNO_ADZE_13661))
}
