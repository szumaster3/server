package content.global.activity.jobs.dialogue

import content.global.activity.jobs.JobManager
import content.global.activity.jobs.JobType
import content.global.activity.jobs.impl.BoneBuryingJobs
import content.global.activity.jobs.impl.CombatJobs
import content.global.activity.jobs.impl.ProductionJobs
import core.api.addItem
import core.api.hasAnItem
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items

class TutorDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val playerJobManager = JobManager.getInstance(player!!)
        when (stage) {
            0 ->
                playerl(FaceAnim.ASKING, "Do you have any jobs for me?").also {
                    stage =
                        if (hasAnItem(player!!, Items.TASK_LIST_13464).exists()) {
                            if (JobManager.getInstance(player!!).hasJob()) 100 else 10
                        } else {
                            1
                        }
                }

            1 ->
                npcl(
                    "Well, first of all, I can see that you don't have a task list. Would you like one?",
                ).also { stage++ }
            2 -> options("Yes, please.", "No, thanks.").also { stage++ }
            3 ->
                when (buttonID) {
                    1 ->
                        when (addItem(player!!, Items.TASK_LIST_13464)) {
                            true -> npcl("Here you go.").also { stage = if (playerJobManager.hasJob()) 100 else 10 }
                            false -> playerl("I don't have enough space in my inventory.").also { stage = END_DIALOGUE }
                        }

                    2 -> playerl("No, thanks.").also { stage = END_DIALOGUE }
                }

            10 ->
                npcl("Let me see if I've got any work for you.").also {
                    if (playerJobManager.hasReachedJobLimit()) {
                        stage = 200
                    } else {
                        playerJobManager.generate(npc!!)
                        stage = if (playerJobManager.hasJob()) 11 else 30
                    }
                }

            11 -> npcl(playerJobManager.getAssignmentMessage()).also { stage = 12 }
            12 -> playerl("Okay, off I go!").also { stage = 13 }
            13 -> npcl("There, I've updated your task list with your new job. Good luck!").also { stage = END_DIALOGUE }
            30 ->
                npcl("I'm sorry, I don't currently have any jobs that you're qualified for.").also {
                    stage = END_DIALOGUE
                }

            100 -> {
                val job = playerJobManager.job ?: return
                val employerName = NPC(job.employer.npcId).name
                val amount = playerJobManager.jobAmount
                val originalAmount = playerJobManager.jobOriginalAmount

                when (job.type) {
                    JobType.PRODUCTION -> {
                        job as ProductionJobs
                        val itemName = Item(job.itemId).name.lowercase()
                        val pluralized = if (originalAmount > 1) itemName + "s" else itemName
                        npcl("You're still working for $employerName. You need to produce $amount more $pluralized.")
                    }

                    JobType.BONE_BURYING -> {
                        job as BoneBuryingJobs
                        val boneName = Item(job.boneIds.first()).name.lowercase()
                        val pluralized = if (originalAmount > 1) boneName + "s" else boneName
                        npcl(
                            "You're still working for $employerName. You need to bury $amount more $pluralized in ${job.buryArea.title}.",
                        )
                    }

                    JobType.COMBAT -> {
                        job as CombatJobs
                        val monsterName = NPC(job.monsterIds.first()).name.lowercase()
                        val pluralized = if (originalAmount > 1) monsterName + "s" else monsterName
                        npcl("You're still working for $employerName. You need to defeat $amount more $pluralized.")
                    }
                }
                stage = 101
            }

            101 -> options("Oh, I better finish their job first.", "I'd like a new job instead.").also { stage++ }
            102 ->
                when (buttonID) {
                    1 -> playerl("Oh, I better finish their job first.").also { stage = END_DIALOGUE }
                    2 -> playerl("I'd like a new job instead.").also { stage = 10 }
                }

            200 -> npcl("You've hit your limit for the day. Come back tomorrow!").also { stage = END_DIALOGUE }
        }
    }
}
