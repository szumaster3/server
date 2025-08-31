package core.game.dialogue

import core.game.component.Component
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.START_DIALOGUE

/**
 * Represents an abstract dialogue file that handles player and NPC dialogues.
 */
abstract class DialogueFile {
    var player: Player? = null
    var npc: NPC? = null
    var interpreter: DialogueInterpreter? = null
    open var stage = START_DIALOGUE
    var dialogue: Dialogue? = null

    /**
     * Handles dialogue interactions based on component and button ID.
     */
    abstract fun handle(
        componentID: Int,
        buttonID: Int,
    )

    /**
     * Loads the dialogue file with the provided player, NPC, and interpreter.
     * @return The loaded dialogue file instance.
     */
    open fun load(player: Player, npc: NPC?, interpreter: DialogueInterpreter): DialogueFile {
        this.player = player
        this.npc = npc
        this.interpreter = interpreter
        interpreter.activeTopics.clear()

        return this
    }

    /**
     * Sends player dialogue with optional facial animation.
     */
    open fun player(vararg msg: String?): Component? = interpreter!!.sendDialogues(player, null, *msg)

    open fun player(expr: FaceAnim?, vararg msg: String?): Component? = interpreter!!.sendDialogues(player, expr, *msg)

    /**
     * Sends player dialogue with optional facial animation.
     */
    open fun playerl(expr: FaceAnim?, msg: String?): Component? = player(expr, *splitLines(msg!!))

    open fun playerl(msg: String?): Component? = player(*splitLines(msg!!))

    /**
     * Sends npc dialogues.
     */
    open fun npc(vararg msg: String?): Component? {
        if (npc != null) {
            return interpreter!!.sendDialogues(
                npc,
                if (npc!!.id > 8591) FaceAnim.OLD_NORMAL else FaceAnim.FRIENDLY,
                *msg,
            )
        }
        return null
    }

    open fun npc(id: Int, vararg msg: String?): Component? = interpreter!!.sendDialogues(id, FaceAnim.FRIENDLY, *msg)

    open fun npc(expr: FaceAnim?, vararg msg: String?): Component? = if (npc == null) {
        interpreter!!.sendDialogues(0, expr, *msg)
    } else {
        interpreter!!.sendDialogues(npc, expr, *msg)
    }

    open fun npc(id: Int, expr: FaceAnim?, vararg msg: String?): Component? =
        interpreter!!.sendDialogues(id, expr, *msg)

    open fun npc(id: Int, title: String, expr: FaceAnim?, vararg msg: String?): Component? {
        val chatBoxComponent = interpreter!!.sendDialogues(id, expr, *msg)
        player!!.packetDispatch.sendString(title, chatBoxComponent.id, 3)
        return chatBoxComponent
    }

    /**
     * Sends NPC dialogue with line splitting.
     */
    open fun npcl(expr: FaceAnim?, msg: String?): Component? = npc(expr, *splitLines(msg!!))

    open fun npcl(id: Int, expr: FaceAnim?, msg: String?): Component? = npc(id, expr, *splitLines(msg!!))

    open fun npcl(id: Int, title: String, expr: FaceAnim?, msg: String?): Component? =
        npc(id, title, expr, *splitLines(msg!!))

    open fun npcl(msg: String?): Component? = npc(*splitLines(msg!!))

    /**
     * Ends the current dialogue session.
     */
    open fun end() {
        interpreter?.close()
    }

    /**
     * Sends a normal dialogue message.
     */
    open fun sendNormalDialogue(id: Entity?, expr: FaceAnim?, vararg msg: String?) {
        interpreter!!.sendDialogues(id, expr, *msg)
    }

    /**
     * Displays dialogue options.
     */
    open fun options(vararg options: String?, title: String = "Select an Option") {
        interpreter!!.sendOptions(title, *options)
    }

    /**
     * Ends the dialogue file session.
     */
    fun endFile() {
        interpreter!!.dialogue.file = null
    }

    /**
     * Returns to a specified dialogue stage.
     */
    fun returnAtStage(stage: Int) {
        dialogue!!.file = null
        dialogue!!.stage = stage
    }

    /**
     * Abandons the dialogue file, resetting dialogue state.
     */
    fun abandonFile() {
        interpreter!!.dialogue.file = null
        player("Nevermind.")
    }

    /**
     * Retrieves the current dialogue stage.
     */
    open fun getCurrentStage(): Int = stage

    /**
     * Creates a substage offset.
     */
    fun Int.substage(stage: Int): Int = this + stage

    /**
     * Sends a player dialogue message.
     */
    fun dialogue(vararg messages: String) {
        player?.dialogueInterpreter?.sendDialogue(*messages)
    }

    /**
     * Displays selectable dialogue topics.
     * @return true if no topics are available, otherwise false.
     */
    fun showTopics(vararg topics: Topic<*>, title: String = "Select an Option:"): Boolean {
        val validTopics = ArrayList<String>()
        topics.filter { if (it is IfTopic) it.showCondition else true }.forEach { topic ->
            interpreter!!.activeTopics.add(topic)
            validTopics.add(topic.text)
        }
        when (validTopics.size) {
            0 -> {
                return true
            }

            1 -> {
                val topic = topics[0]
                if (topic.toStage is DialogueFile) {
                    val topicFile = topic.toStage
                    interpreter!!.dialogue.loadFile(topicFile)
                } else if (topic.toStage is Int) {
                    stage = topic.toStage
                }
                player(topic.text)
                interpreter!!.activeTopics.clear()
                return false
            }

            else -> {
                options(*validTopics.toTypedArray(), title = title)
                return false
            }
        }
    }
}
