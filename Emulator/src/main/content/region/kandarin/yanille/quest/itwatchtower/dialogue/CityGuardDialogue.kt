package content.region.kandarin.yanille.quest.itwatchtower.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.world.map.RegionManager
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the City Guard dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.quest.itwatchtower.Watchtower]
 */
class CityGuardDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.CITY_GUARD_862)
        when(stage) {
            0 -> {
                if(getQuestStage(player!!, Quests.WATCHTOWER) >= 20) {
                    npc(FaceAnim.OLD_DEFAULT, "What is it?")
                    stage = 13
                    return
                }

                if(getAttribute(player!!, GameAttributes.WATCHTOWER_RIDDLE, false) && inInventory(player!!, Items.DEATH_RUNE_560)) {
                    removeItem(player!!, Item(Items.DEATH_RUNE_560, 1))
                    player(FaceAnim.HAPPY, "I worked it out!")
                    stage = 11
                } else {
                    npc(FaceAnim.OLD_ANGRY1, "Grrrr, what business you got here?")
                    stage = 1
                }
            }
            1 -> player("I am on an errand.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_CALM_TALK2, "So what you want with me?").also { stage++ }
            3 -> showTopics(
                Topic("I am an ogre killer come to destroy you!", 4),
                Topic("I seek passage into the skavid caves.", 5)
            )
            4 -> {
                end()
                npc!!.attack(player)
            }
            5 -> npc(FaceAnim.OLD_NORMAL, "Is that so... You humour me small t'ing, answer this", "riddle and I will help you...").also { stage++ }
            6 -> npc(FaceAnim.OLD_DEFAULT, "I want you to bring me an item: I will give you all the", "letters of this item, you work out what it is...").also { stage++ }
            7 -> npc(FaceAnim.OLD_DEFAULT,"My first is in days, but not in years, My second is in", "evil, and also in tears, My third is in all, but not in", "none, My fourth is in hot, but not in sun.").also { stage++ }
            8 -> npc(FaceAnim.OLD_DEFAULT, "My fifth is in heaven, and also in hate, My sixth is in", "fearing, but not in fate, My seventh is in plush, but not", "in place.").also { stage++ }
            9 -> npc(FaceAnim.OLD_DEFAULT, "My eighth is in nine, but not in eight, My last is in", "earth, and also in great.").also { stage++ }
            10 -> npc(FaceAnim.OLD_DEFAULT, "My whole is an object, that magic will make, It brings", "wrack and ruin to all in its wake... Now how long, I", "wonder, will this riddle take?").also {
                setAttribute(player!!, GameAttributes.WATCHTOWER_RIDDLE, true)
                stage = END_DIALOGUE
            }
            11 -> npc(FaceAnim.OLD_DEFAULT,"Well, well. The imp has done it! Thanks for the rune;", "this is what you be needing...").also { stage++ }
            12 -> {
                end()
                addItemOrDrop(player!!, Items.SKAVID_MAP_2376, 1)
                setQuestStage(player!!, Quests.WATCHTOWER, 20)
                sendMessage(player!!, "The guard gives you a map.")
            }

            13 -> options("Do you have any other riddles for me?", "I have lost the map you gave me.").also { stage++ }
            14 -> when(buttonID) {
                1 -> player("Do you have any other riddles for me?").also { stage++ }
                2 -> player("I have lost the map you gave me.").also { stage = 22 }
            }
            15 -> npcl(FaceAnim.OLD_DEFAULT, "Yes, what looks good on a plate with salad?").also { stage++ }
            16 -> options("I don't know...", "A nice pizza?").also { stage++ }
            17 -> when(buttonID) {
                1 -> player("I don't know...").also { stage++ }
                2 -> player("A nice pizza?").also { stage = 20 }
            }
            18 -> npcl(FaceAnim.OLD_DEFAULT, "You!!!").also { stage++ }
            19 -> npcl(FaceAnim.OLD_DEFAULT, "Now go and bother me no more...").also { stage = END_DIALOGUE }

            20 -> npcl(FaceAnim.OLD_ANGRY1, "Grr.. think you are a comedian eh?").also { stage++ }
            21 -> npcl(FaceAnim.OLD_DEFAULT, "Get lost!").also { stage = END_DIALOGUE }
            22 -> if(inInventory(player!!, Items.SKAVID_MAP_2376)) {
                npcl(FaceAnim.OLD_DEFAULT, "Are you blind ? what is that you are carrying?").also { stage++ }
            } else {
                npcl(FaceAnim.OLD_DEFAULT, "What's the point? Take this copy and bother me no more!").also {
                    addItem(player!!, Items.SKAVID_MAP_2376, 1)
                    stage = END_DIALOGUE
                }
            }
            23 -> player(FaceAnim.HALF_THINKING, "Oh, that map....").also { stage = END_DIALOGUE }
        }
    }
}