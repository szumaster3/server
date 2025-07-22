package content.region.kandarin.camelot.dialogue

import content.data.GameAttributes
import content.data.RespawnPoint
import content.data.setRespawnLocation
import content.region.kandarin.camelot.quest.arthur.dialogue.MerlinDialogueFile
import core.ServerConstants
import core.api.*
import core.api.getQuestStage
import core.api.isQuestComplete
import core.api.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Merlin dialogue.
 */
@Initializable
class MerlinDialogue(player: Player? = null) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Merlin Crystal: Freeing Merlin: Finishing.
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            openDialogue(player, MerlinDialogueFile(false))
        } else {
            when (stage) {
                // Knight Waves training ground: Upon completion of Knight Waves.
                0 -> if (player.location.z == 2) {
                    if (!getAttribute(player!!, GameAttributes.KW_COMPLETE, false)) {
                        npc(
                            FaceAnim.HAPPY, "Well done, young adventurer. You truly are a worthy", "knight."
                        ).also { stage = 100 }
                    } else {
                        end()
                        sendMessage(player!!, "Nothing interesting happens.")
                    }
                } else {
                    // Holy Grail: Post-quest dialogue.
                    if (isQuestComplete(player!!, Quests.HOLY_GRAIL)) {
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Congratulations, brave knight, on aiding Camelot in so many ways! If we ever require help again, I will make sure to call upon you!",
                        )
                        stage = 20
                    }
                    // Holy Grail: Returning to Camelot.
                    else if (getQuestStage(
                            player!!, Quests.HOLY_GRAIL
                        ) >= 50 && player!!.hasItem(Item(Items.HOLY_GRAIL_19, 1))
                    ) {
                        npcl(
                            FaceAnim.NEUTRAL,
                            "My magic powers tell me that you have discovered the Grail! Take it to Arthur immediately!",
                        )
                        stage = END_DIALOGUE
                    }
                    // Knight Waves training ground: after completion of Knight Waves.
                    else if (getAttribute(player!!, GameAttributes.KW_COMPLETE, false) && player.location.z != 2) {
                        npc(
                            FaceAnim.FRIENDLY, "Well done, young adventurer. You truly are a worthy knight."
                        ).also { stage = 200 }
                    }
                    // Holy Grail: Speaking to Merlin.
                    else {
                        playerl(
                            FaceAnim.NEUTRAL,
                            "Hello. King Arthur has sent me on a quest for the Holy Grail. He thought you could offer some assistance.",
                        ).also {
                            stage++
                        }
                    }
                }

                1 -> npcl(FaceAnim.NEUTRAL, "Ah yes... the Holy Grail...").also {
                    if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 10) {
                        setQuestStage(player!!, Quests.HOLY_GRAIL, 20)
                    }
                    stage++
                }

                2 -> npcl(
                    FaceAnim.NEUTRAL,
                    "That is a powerful artefact indeed. Returning it here would help Camelot a lot.",
                ).also {
                    stage++
                }

                3 -> npcl(
                    FaceAnim.NEUTRAL,
                    "Due to its nature the Holy Grail is likely to reside in a holy place.",
                ).also { stage++ }

                4 -> playerl(FaceAnim.NEUTRAL, "Any suggestions?").also { stage++ }
                5 -> npcl(
                    FaceAnim.NEUTRAL,
                    "I believe there is a holy island somewhere not far away... I'm not entirely sure... I spent too long inside that crystal! Anyway, go and talk to someone over there.",
                ).also {
                    stage++
                }

                6 -> npcl(FaceAnim.NEUTRAL, "I suppose you could also try speaking to Sir Galahad?").also { stage++ }
                7 -> npcl(
                    FaceAnim.NEUTRAL,
                    "He returned from the quest many years after everyone else. He seems to know something about it, but he can only speak about those experiences cryptically.",
                ).also {
                    stage++
                }

                8 -> showTopics(
                    Topic(FaceAnim.NEUTRAL, "Thank you for the advice.", END_DIALOGUE),
                    Topic(FaceAnim.NEUTRAL, "Where can I find Sir Galahad?", 15),
                )

                15 -> npcl(
                    FaceAnim.NEUTRAL,
                    "Galahad now lives a life of religious contemplation. He lives somewhere west of McGrubor's Wood I think.",
                ).also {
                    stage = END_DIALOGUE
                }

                20 -> playerl(FaceAnim.NEUTRAL, "Thanks!").also { stage = END_DIALOGUE }

                100 -> {
                    end()
                    sendDialogueLines(
                        player!!,
                        "You receive access to the prayers Chivalry and Piety and 20,000",
                        "XP in your Hitpoints, Strength, Attack, and Defence skills. You may",
                        "also speak to Merlin to set your respawn point to Camelot.",
                    ).also {
                        rewardXP(player!!, Skills.HITPOINTS, 20.000)
                        rewardXP(player!!, Skills.ATTACK, 20.000)
                        rewardXP(player!!, Skills.STRENGTH, 20.000)
                        rewardXP(player!!, Skills.DEFENCE, 20.000)
                        setAttribute(player!!, GameAttributes.KW_COMPLETE, true)
                    }
                }
                // Changing the Respawn point.
                200 -> if (player.properties.spawnLocation == ServerConstants.HOME_LOCATION) {
                    playerl(
                        FaceAnim.HALF_ASKING, "I was wondering, can I change my respawn point to Camelot?"
                    ).also { stage++ }
                } else {
                    playerl(FaceAnim.HALF_ASKING, "Can I change my respawn point back to Lumbridge?").also {
                        stage = 202
                    }
                }

                201 -> {
                    npcl(FaceAnim.NEUTRAL, "You have chosen...sensibly")
                    sendMessage(player, "Merlin set your respawn point to Camelot.")
                    player.setRespawnLocation(RespawnPoint.CAMELOT)
                    stage = END_DIALOGUE
                }

                202 -> {
                    npcl(FaceAnim.NEUTRAL, "You have chosen...sensibly")
                    sendMessage(player, "Merlin set your respawn point back to Lumbridge.")
                    player.setRespawnLocation(RespawnPoint.LUMBRIDGE)
                    stage = END_DIALOGUE
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = MerlinDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.MERLIN_213)
}
