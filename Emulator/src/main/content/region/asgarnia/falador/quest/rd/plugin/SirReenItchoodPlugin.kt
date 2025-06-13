package content.region.asgarnia.falador.quest.rd.plugin

import content.region.asgarnia.falador.quest.rd.RecruitmentDrive
import content.region.asgarnia.falador.quest.rd.cutscene.FailCutscene
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import org.rs.consts.Components
import org.rs.consts.NPCs

class SirReenItchoodPlugin(
    private val dialogueNum: Int = 0,
) : DialogueBuilderFile() {
    companion object {
        const val ATTRIBUTE_CLUE = "rd:cluenumber"
    }

    override fun create(b: DialogueBuilder) {
        b
            .onPredicate { player ->
                dialogueNum in 0..1 &&
                    !getAttribute(
                        player,
                        RecruitmentDrive.stageFail,
                        false,
                    )
            }.betweenStage { _, player, _, _ ->
                if (getAttribute(player, ATTRIBUTE_CLUE, 6) == 6) {
                    setAttribute(player, ATTRIBUTE_CLUE, (0..5).random())
                }
            }.npc(
                "Greetings friend, and welcome here,",
                "you'll find my puzzle not so clear.",
                "Hidden amongst my words, it's true,",
                "the password for the door as a clue.",
            ).options()
            .let { optionBuilder ->

                optionBuilder
                    .option_playerl("Can I have the clue for the door?")
                    .branch { player -> getAttribute(player, ATTRIBUTE_CLUE, 0) }
                    .let { branch ->
                        branch
                            .onValue(0)
                            .npc(
                                "Better than me, you'll not find",
                                "In rhyming and in puzzles.",
                                "This clue so clear will tax your mind",
                                "Entirely as it confuzzles!",
                            ).end()
                        branch
                            .onValue(1)
                            .npc(
                                "Feel the aching of your mind",
                                "In puzzlement, confused.",
                                "See the clue hidden behind",
                                "His words, as you perused.",
                            ).end()
                        branch
                            .onValue(2)
                            .npc(
                                "Look closely at the words i speak;",
                                "And study closely every part.",
                                "See for yourself the word you seek",
                                "Trapped for you if you're smart.",
                            ).end()
                        branch
                            .onValue(3)
                            .npc(
                                "More than words, i have not for you",
                                "Except the things i say today.",
                                "Aware are you, this is a clue?",
                                "Take note of what i say!",
                            ).end()
                        branch
                            .onValue(4)
                            .npc(
                                "Rare it is that you will see",
                                "A puzzle such as this!",
                                "In many ways it tickles me",
                                "Now, watching you hit and miss!",
                            ).end()
                        branch
                            .onValue(5)
                            .npc(
                                "This riddle of mine may confuse,",
                                "I am quite sure of that.",
                                "Mayhap you should closely peruse",
                                "Every word i have spat?",
                            ).end()
                    }
                return@let optionBuilder
                    .option("Can I have a different clue?")
                    .player("I don't get that riddle...", "Can I have a different one?")
                    .branch { player -> getAttribute(player, ATTRIBUTE_CLUE, 0) }
                    .let { branch ->

                        branch
                            .onValue(0)
                            .npc(
                                "Before you hurry through that door",
                                "Inspect the words i spoke.",
                                "There is a simple hidden flaw",
                                "Ere you think my rhyme a joke.",
                            ).end()
                        branch
                            .onValue(1)
                            .npc(
                                "First my clue you did not see,",
                                "I really wish you had.",
                                "Such puzzling wordplay devilry",
                                "Has left you kind of mad!",
                            ).end()
                        branch
                            .onValue(2)
                            .npc(
                                "Last time my puzzle did not help",
                                "Apparently, so you've bidden.",
                                "Study my speech carefully, whelp",
                                "To find the answer, hidden.",
                            ).end()
                        branch
                            .onValue(3)
                            .npc(
                                "Many types have passed through here",
                                "Even such as you amongst their sort.",
                                "And in the end, the puzzles clear;",
                                "TThe hidden word you saught.",
                            ).end()
                        branch
                            .onValue(4)
                            .npc(
                                "Repetition, once again",
                                "Against good sense it goes.",
                                "In my words, the answers plain",
                                "Now that you see rhyme flows.",
                            ).end()
                        branch
                            .onValue(5)
                            .npc(
                                "Twice it is now, i have stated",
                                "In a rhyme, what is the pass.",
                                "Maybe my words obfuscated",
                                "Entirely beyond your class.",
                            ).end()
                    }
            }
        b
            .onPredicate { player ->
                dialogueNum == 2 || getAttribute(player, RecruitmentDrive.stageFail, false)
            }.betweenStage { _, player, _, _ ->
                setAttribute(player, RecruitmentDrive.stageFail, true)
            }.npc(
                FaceAnim.SAD,
                "It's sad to say,",
                "this test beat you.",
                "I'll send you to Tiffy,",
                "what to do?",
            ).endWith { _, player ->
                lock(player, 10)
                removeAttribute(player, ATTRIBUTE_CLUE)
                setAttribute(player, RecruitmentDrive.stagePass, false)
                setAttribute(player, RecruitmentDrive.stageFail, false)
                runTask(player, 3) {
                    FailCutscene(player).start()
                    return@runTask
                }
            }
    }
}

class CombinationLockInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.RD_COMBOLOCK_285) { player, _ ->
            initializeLocks(player)
            return@onOpen true
        }

        onClose(Components.RD_COMBOLOCK_285) { player, _ ->
            clearLocks(player)
            return@onClose true
        }

        on(Components.RD_COMBOLOCK_285) { player, _, _, buttonID, _, _ ->
            handleButtonPress(player, buttonID)
            return@on true
        }
    }

    private fun initializeLocks(player: Player) {
        lockArray.forEach { lock ->
            setAttribute(player, lock, INITIAL_VALUE)
        }
    }

    private fun clearLocks(player: Player) {
        lockArray.forEach { lock ->
            removeAttribute(player, lock)
        }
    }

    private fun handleButtonPress(
        player: Player,
        buttonID: Int,
    ) {
        if (buttonID in 10..17) {
            adjustLockValue(player, buttonID)
        } else if (buttonID == 18) {
            validateAnswer(player)
        }
    }

    private fun adjustLockValue(
        player: Player,
        buttonID: Int,
    ) {
        val position = (buttonID - 10) / 2
        val direction = (buttonID - 10) % 2
        var newValue = getAttribute(player, lockArray[position], INITIAL_VALUE) + if (direction == 0) -1 else 1

        newValue =
            when {
                newValue < LOWER_BOUND -> UPPER_BOUND
                newValue > UPPER_BOUND -> LOWER_BOUND
                else -> newValue
            }

        setAttribute(player, lockArray[position], newValue)
        sendString(player, newValue.toChar().toString(), Components.RD_COMBOLOCK_285, position + 6)
    }

    private fun validateAnswer(player: Player) {
        val answer = lockArray.joinToString("") { getAttribute(player, it, INITIAL_VALUE).toChar().toString() }
        closeInterface(player)
        if (answers[getAttribute(player, SirReenItchoodPlugin.ATTRIBUTE_CLUE, 0)] == answer) {
            if (!getAttribute(player, RecruitmentDrive.stageFail, false)) {
                setAttribute(player, RecruitmentDrive.stagePass, true)
            }
            playJingle(player, 159)
            sendNPCDialogue(
                player,
                NPCs.SIR_REN_ITCHOOD_2287,
                "Your wit is sharp, your brains quite clear; You solved my puzzle with no fear. At puzzles I rank you quite the best, now enter the portal for your next test.",
            )
        } else {
            setAttribute(player, RecruitmentDrive.stageFail, true)
            openDialogue(player, SirReenItchoodPlugin(2), NPC(NPCs.SIR_REN_ITCHOOD_2287))
        }
    }

    companion object {
        private const val INITIAL_VALUE = 65
        private const val LOWER_BOUND = 65
        private const val UPPER_BOUND = 90
        private const val LOCK_COUNT = 4
        private val lockArray = arrayOf("rd:lock1", "rd:lock2", "rd:lock3", "rd:lock4")
        private val answers = arrayOf("BITE", "FISH", "LAST", "MEAT", "RAIN", "TIME")
    }
}
