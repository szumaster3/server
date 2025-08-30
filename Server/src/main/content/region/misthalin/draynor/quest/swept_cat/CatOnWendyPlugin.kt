package content.region.misthalin.draynor.quest.swept_cat

import content.global.skill.summoning.pet.Pets
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.*

val purpleCats = intArrayOf(
    Items.PET_KITTEN_14089,
    Items.PET_CAT_14090,
    Items.LAZY_CAT_14091,
    Items.OVERGROWN_CAT_14092,
    Items.WILY_CAT_14093,
)

class CatOnWendyPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles using cats on Wendy NPC (Purple cats mini-quest).
         */

        onUseAnyWith(IntType.NPC, NPCs.WENDY_8201) { player, item, _ ->
            val petData = Pets.forId(item.id)
            if (petData == null) {
                sendMessage(player, "I can't do anything with that.")
                return@onUseAnyWith false
            }

            if (player.familiarManager.hasPet()) {
                sendMessage(player, "You need to pick up your pet first to do that.")
                return@onUseAnyWith false
            }

            val isPurpleCat = item.id in purpleCats

            if (!isQuestComplete(player, Quests.SWEPT_AWAY)) {
                sendMessage(player, "You can't use your cat on Wendy yet.")
                return@onUseAnyWith false
            }

            if (isPurpleCat) {
                openDialogue(player, WendyPurpleCatDialogueExtension())
            } else {
                openDialogue(player, WendyRegularCatDialogueExtension())
            }

            return@onUseAnyWith true
        }
    }
}

class WendyRegularCatDialogueExtension : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.WENDY_8201)

        when (stage) {
            0 -> player("Could you make this cat purple for me?").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "I can, but I should warn you: I can make cats purple, but I don't know how to change them back. It's a permanent change! Is that okay?").also { stage++ }
            2 -> options("Yes, please make my cat permanently purple.", "No, I think I'll hold off for the time being.").also { stage++ }
            3 -> when (buttonID) {
                1 -> player("Yes, please make my cat permanently purple.").also { stage++ }
                2 -> player(FaceAnim.NEUTRAL, "No, I think I'll hold off for the time being.").also { stage = 6 }
            }
            4 -> {
                val familiar = player?.familiarManager?.familiar
                val inv = player?.inventory?.toArray() ?: arrayOfNulls<Item>(0)
                val petID = inv.firstOrNull { it != null && Pets.forId(it.id) != null }

                if (familiar == null && petID == null) {
                    stage = 7
                    return
                }

                val petItemID = familiar?.id ?: petID?.id
                val petData = Pets.forId(petItemID!!) ?: run {
                    stage = 7
                    return
                }

                val purpleCatID = when (petItemID) {
                    petData.babyItemId -> Pets.PURPLE_CAT.babyItemId
                    petData.grownItemId -> Pets.PURPLE_CAT.grownItemId
                    petData.overgrownItemId -> Pets.PURPLE_CAT.overgrownItemId
                    // petData.lazyItemId -> Pets.CAT_6.lazyItemId
                    // petData.wilyItemId -> Pets.CAT_6.wilyItemId
                    else -> run {
                        end()
                        return
                    }
                }

                player?.familiarManager?.morphPet(Item(purpleCatID), true, familiar?.location ?: player?.location)

                val newFamiliar = player?.familiarManager?.familiar
                if (newFamiliar != null) {
                    player?.face(newFamiliar)
                    player?.animate(Animation(Animations.MULTI_BEND_OVER_827))
                    sendGraphics(Graphics.PUFF_OF_GREY_1276, newFamiliar.location)
                    newFamiliar.sendChat("Miaow!")
                }

                npcl(FaceAnim.HAPPY, "There you go! Oh, such a beautiful colour.")
                stage = 5
            }
            5 -> player("Thank you.").also { stage = 7 }
            6 -> npcl(FaceAnim.FRIENDLY, "Okay. If you change your mind, let me know.").also { stage++ }
            7 -> end()
        }
    }
}

class WendyPurpleCatDialogueExtension : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.WENDY_8201)
        when (stage) {
            0 -> sendDialogue(player!!, "You show your purple cat to Wendy.").also { stage++ }
            1 -> npc("Oh, what a lovely cat you have there. Hello, kitty!").also { stage++ }
            2 -> options("Can you change this cat back to its normal colour?", "Thank you.").also { stage++ }
            3 -> when (buttonID) {
                1 -> player("Can you change this cat back to its normal colour?").also { stage++ }
                2 -> player("Thank you.").also { stage = 6 }
            }
            4 -> npcl(FaceAnim.STRUGGLE, "Um, did I forget to mention...once the spell is cast, it's cast for good. I can't undo the purple.").also { stage++ }
            5 -> player(FaceAnim.HALF_CRYING, "Ah, right. Hmm.").also { stage = 6 }
            6 -> end()
        }
    }
}
