package content.region.asgarnia.falador.quest.rd.plugin.tests

import content.region.asgarnia.falador.quest.rd.RDUtils
import content.region.asgarnia.falador.quest.rd.RecruitmentDrive
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.plugin.Initializable
import core.tools.secondsToTicks
import shared.consts.NPCs

@Initializable
class SirTinleyDialogue(player: Player? = null) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SirTinleyDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SirTinleyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_TINLEY_2286)
    }
}

class SirTinleyDialogueFile(private val dialogueNum: Int = 0) : DialogueBuilderFile(), MapArea {

    companion object {
        /**
         * Attribute to prevent the player from moving during the test.
         */
        const val attributeDoNotMove = "quest:recruitmentdrive-donotmove"
    }

    override fun create(b: DialogueBuilder) {

        // Start dialogue if the player hasn't moved and hasn't failed yet
        b.onPredicate { player ->
            dialogueNum == 0 &&
                    !getAttribute(player, attributeDoNotMove, false) &&
                    !getAttribute(player, RecruitmentDrive.stageFail, false)
        }
            .npc("Ah, welcome @name.", "I have but one clue for you to pass this room's puzzle:", "'Patience'.")
            .endWith { _, player ->
                setAttribute(player, attributeDoNotMove, true)
                queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                    when (stage) {
                        0 -> return@queueScript delayScript(player, secondsToTicks(9))
                        1 -> {
                            if (!getAttribute(player, RecruitmentDrive.stageFail, false)) {
                                removeAttribute(player, attributeDoNotMove)
                                setAttribute(player, RecruitmentDrive.stagePass, true)
                                npc(FaceAnim.HAPPY, "Excellent work, @name.", "Please step through the portal to meet your next", "challenge.")
                            } else {
                                removeAttribute(player, attributeDoNotMove)
                                npc(FaceAnim.SAD, "You did not complete the challenge in time.")
                            }
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }

        // Dialogue if the player already solved the puzzle
        b.onPredicate { player ->
            dialogueNum == 0 &&
                    !getAttribute(player, attributeDoNotMove, false) &&
                    getAttribute(player, RecruitmentDrive.stageFail, false)
        }
            .npc(FaceAnim.FRIENDLY, "Excellent work, @name.", "Please step through the portal to meet your next", "challenge.")
            .end()

        // Dialogue if the player fails
        b.onPredicate { player ->
            dialogueNum == 0 &&
                    getAttribute(player, attributeDoNotMove, false) ||
                    dialogueNum == 2
        }
            .betweenStage { _, player, _, _ ->
                setAttribute(player, RecruitmentDrive.stageFail, true)
            }
            .npc(FaceAnim.SAD, "No... I am very sorry.", "Apparently you are not up to the challenge.", "I will return you where you came from, better luck in the", "future.")
            .endWith { _, player ->
                removeAttribute(player, attributeDoNotMove)
                removeAttribute(player, RecruitmentDrive.stageFail)
                RDUtils.failSequence(player)
            }

        // Dialogue for the next stage
        b.onPredicate { _ -> dialogueNum == 1 }
            .npc("Ah, @name, you have arrived.", "Speak to me to begin your task.")
            .endWith { _, player -> removeAttribute(player, attributeDoNotMove) }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2474, 4959, 2478, 4957))
    }

    /**
     * Called whenever an entity moves in this map area.
     */
    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity !is Player) return

        val doNotMove = getAttribute(entity, attributeDoNotMove, false)
        val stagePass = getAttribute(entity, RecruitmentDrive.stagePass, false)

        if (stagePass) {
            removeAttribute(entity, attributeDoNotMove)
            return
        }

        if (doNotMove) {
            val borders = defineAreaBorders()[0]

            if (!inBorders(entity, borders)) {
                setAttribute(entity, RecruitmentDrive.stageFail, true)
                removeAttribute(entity, attributeDoNotMove)
                openDialogue(entity, SirTinleyDialogueFile(2), NPC(NPCs.SIR_TINLEY_2286))
            }
        }
    }
}
