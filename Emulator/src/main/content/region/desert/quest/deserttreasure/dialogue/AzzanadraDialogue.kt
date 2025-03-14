package content.region.desert.quest.deserttreasure.dialogue

import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AzzanadraDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.DESERT_TREASURE) == 10) {
            npcl(FaceAnim.OLD_DEFAULT, "I knew they could not trap me here for long!")
            stage = 1
        } else {
            npcl(FaceAnim.OLD_DEFAULT, "You should come back when the battle is over.")
            stage = END_DIALOGUE
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> {
                npcl(FaceAnim.OLD_DEFAULT, "Well done, soldier, tell me, how goes the battle?")
                stage++
            }
            2 -> {
                playerl(FaceAnim.THINKING, "Battle?")
                stage++
            }
            3 -> {
                npcl(FaceAnim.OLD_DEFAULT, "You do not know of the battle?")
                stage++
            }
            4 -> {
                npcl(FaceAnim.OLD_DEFAULT, "More time must have passed than I had thought...")
                stage++
            }
            5 -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Tell me, what news of great Paddewwa? Do the shining spires of Lassar still stand?",
                )
                stage++
            }
            6 -> {
                player("Uh...", "Sorry, I've never heard of them...")
                stage++
            }
            7 -> {
                npcl(FaceAnim.OLD_SAD, "No!")
                stage++
            }
            8 -> {
                npcl(
                    FaceAnim.OLD_SAD,
                    "My lord... What has become of you? I cannot hear your voice in my mind anymore!",
                )
                stage++
            }
            9 -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "My thanks to you brave warrior for your help in freeing me from this accursed tomb, but it seems I have much to do to make amends.",
                )
                stage++
            }
            10 -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "If the shining cities no longer stand, then it means that we must have failed my lord...",
                )
                stage++
            }
            11 -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "How long have I been trapped here? Master... Have you truly been dispatched from this world?",
                )
                stage++
            }
            12 -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Warrior, for your efforts in freeing me, I offer you the gift of knowledge.",
                )
                stage++
            }
            13 -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I bestow upon you the ancient magicks, taught me by my Lord before his disappearance, may you use them well in battle for our people!",
                )
                stage++
            }
            14 -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "They will replace the knowledge you previously had, but you may switch between them by praying at the altar in this room at any time.",
                )
                stage++
            }
            15 -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I trust that we shall meet again adventurer, I offer you the blessings of myself and my master in all of your endeavours!",
                )
                stage++
            }
            16 -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Now, I must leave you, for there must be some trace of my master's power left somewhere. Feel free to use the portal I shall create to return here easily in the future!",
                )
                stage++
            }
            17 -> {
                /*
                 * Finish quest and apply changes to the player.
                 */
                end()
                if (getQuestStage(player, Quests.DESERT_TREASURE) == 10) {
                    sendMessage(player, "A strange wisdom has filled your mind...")
                    finishQuest(player, Quests.DESERT_TREASURE)
                    player.spellBookManager.setSpellBook(SpellBookManager.SpellBook.ANCIENT)
                    player.spellBookManager.update(player)
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AzzanadraDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SCARABS_1970, NPCs.AZZANADRA_1971)
    }
}
