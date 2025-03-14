package core.game.dialogue

import core.game.component.Component
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.START_DIALOGUE

abstract class DialogueFile {
    var player: Player? = null
    var npc: NPC? = null
    var interpreter: DialogueInterpreter? = null
    open var stage = START_DIALOGUE
    var dialogue: Dialogue? = null

    abstract fun handle(
        componentID: Int,
        buttonID: Int,
    )

    open fun load(
        player: Player,
        npc: NPC?,
        interpreter: DialogueInterpreter,
    ): DialogueFile {
        this.player = player
        this.npc = npc
        this.interpreter = interpreter
        interpreter.activeTopics.clear()

        return this
    }

    open fun player(vararg msg: String?): Component? {
        return interpreter!!.sendDialogues(player, null, *msg)
    }

    open fun player(
        expr: FaceAnim?,
        vararg msg: String?,
    ): Component? {
        return interpreter!!.sendDialogues(player, expr, *msg)
    }

    open fun playerl(
        expr: FaceAnim?,
        msg: String?,
    ): Component? {
        return player(expr, *splitLines(msg!!))
    }

    open fun playerl(msg: String?): Component? {
        return player(*splitLines(msg!!))
    }

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

    open fun npc(
        id: Int,
        vararg msg: String?,
    ): Component? {
        return interpreter!!.sendDialogues(id, FaceAnim.FRIENDLY, *msg)
    }

    open fun npc(
        expr: FaceAnim?,
        vararg msg: String?,
    ): Component? {
        return if (npc == null) {
            interpreter!!.sendDialogues(0, expr, *msg)
        } else {
            interpreter!!.sendDialogues(npc, expr, *msg)
        }
    }

    open fun npc(
        id: Int,
        expr: FaceAnim?,
        vararg msg: String?,
    ): Component? {
        return interpreter!!.sendDialogues(id, expr, *msg)
    }

    open fun npc(
        id: Int,
        title: String,
        expr: FaceAnim?,
        vararg msg: String?,
    ): Component? {
        val chatBoxComponent = interpreter!!.sendDialogues(id, expr, *msg)
        player!!.packetDispatch.sendString(title, chatBoxComponent.id, 3)
        return chatBoxComponent
    }

    open fun npcl(
        expr: FaceAnim?,
        msg: String?,
    ): Component? {
        return npc(expr, *splitLines(msg!!))
    }

    open fun npcl(
        id: Int,
        expr: FaceAnim?,
        msg: String?,
    ): Component? {
        return npc(id, expr, *splitLines(msg!!))
    }

    open fun npcl(
        id: Int,
        title: String,
        expr: FaceAnim?,
        msg: String?,
    ): Component? {
        return npc(id, title, expr, *splitLines(msg!!))
    }

    open fun npcl(msg: String?): Component? {
        return npc(*splitLines(msg!!))
    }

    open fun end() {
        interpreter?.close()
    }

    open fun sendNormalDialogue(
        id: Entity?,
        expr: FaceAnim?,
        vararg msg: String?,
    ) {
        interpreter!!.sendDialogues(id, expr, *msg)
    }

    open fun options(
        vararg options: String?,
        title: String = "Select an Option",
    ) {
        interpreter!!.sendOptions(title, *options)
    }

    fun endFile() {
        interpreter!!.dialogue.file = null
    }

    fun returnAtStage(stage: Int) {
        dialogue!!.file = null
        dialogue!!.stage = stage
    }

    fun abandonFile() {
        interpreter!!.dialogue.file = null
        player("Nevermind.")
    }

    open fun getCurrentStage(): Int {
        return stage
    }

    fun Int.substage(stage: Int): Int {
        return this + stage
    }

    fun dialogue(vararg messages: String) {
        player?.dialogueInterpreter?.sendDialogue(*messages)
    }

    fun showTopics(
        vararg topics: Topic<*>,
        title: String = "Select an Option:",
    ): Boolean {
        val validTopics = ArrayList<String>()
        topics.filter { if (it is IfTopic) it.showCondition else true }.forEach { topic ->
            interpreter!!.activeTopics.add(topic)
            validTopics.add(topic.text)
        }
        if (validTopics.size == 0) {
            return true
        } else if (validTopics.size == 1) {
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
        } else {
            options(*validTopics.toTypedArray(), title = title)
            return false
        }
    }
}
