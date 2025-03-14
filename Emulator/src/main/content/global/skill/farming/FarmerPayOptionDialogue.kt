package content.global.skill.farming

import core.api.inInventory
import core.api.note
import core.api.removeItem
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items

class FarmerPayOptionDialogue(
    val patch: Patch,
    val quickPay: Boolean = false,
) : DialogueFile() {
    var item: Item? = null

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val faceAnim =
            if (npc!!.id in intArrayOf(1037, 2343)) FaceAnim.OLD_NORMAL else FaceAnim.HALF_GUILTY
        when (stage) {
            START_DIALOGUE -> {
                if (patch.patch.type == PatchType.TREE_PATCH && patch.plantable != null && patch.isGrown()) {
                    showTopics(
                        Topic("Yes, get rid of the tree.", 300, true),
                        Topic("No thanks.", END_DIALOGUE, true),
                        title = "Pay 200 gp to have the tree chopped down?",
                    )
                } else if (patch.protectionPaid) {
                    npc(
                        faceAnim,
                        "I don't know what you're talking about - I'm already",
                        "looking after that patch for you.",
                    ).also { stage = 100 }
                } else if (patch.isDead) {
                    npc(
                        faceAnim,
                        "That patch is dead - it's too late for me to do",
                        "anything about it now.",
                    ).also { stage = END_DIALOGUE }
                } else if (patch.isDiseased) {
                    npc(
                        faceAnim,
                        "That patch is diseased - I can't look after it",
                        "until it has been cured.",
                    ).also { stage = END_DIALOGUE }
                } else if (patch.isWeedy() || patch.isEmptyAndWeeded()) {
                    npc(
                        faceAnim,
                        "You don't have anything planted in that patch. Plant",
                        "something and I might agree to look after it for you.",
                    ).also { stage = END_DIALOGUE }
                } else if (patch.isGrown()) {
                    npc(
                        faceAnim,
                        "That patch is already fully grown!",
                        "I don't know what you want me to do with it!",
                    ).also { stage = END_DIALOGUE }
                } else {
                    item = patch.plantable?.protectionItem
                    val protectionText =
                        when (item?.id) {
                            Items.COMPOST_6032 -> if (item?.amount == 1) "bucket of compost" else "buckets of compost"
                            Items.POTATOES10_5438 -> if (item?.amount == 1) "sack of potatoes" else "sacks of potatoes"
                            Items.ONIONS10_5458 -> if (item?.amount == 1) "sack of onions" else "sacks of onions"
                            Items.CABBAGES10_5478 -> if (item?.amount == 1) "sack of cabbages" else "sacks of cabbages"
                            Items.JUTE_FIBRE_5931 -> "jute fibres"
                            Items.APPLES5_5386 -> if (item?.amount == 1) "basket of apples" else "baskets of apples"
                            Items.MARIGOLDS_6010 -> "harvest of marigold"
                            Items.TOMATOES5_5968 ->
                                if (item?.amount ==
                                    1
                                ) {
                                    "basket of tomatoes"
                                } else {
                                    "baskets of tomatoes"
                                }
                            Items.ORANGES5_5396 -> if (item?.amount == 1) "basket of oranges" else "baskets of oranges"
                            Items.COCONUT_5974 -> "coconuts"
                            Items.CACTUS_SPINE_6016 -> "cactus spines"
                            Items.STRAWBERRIES5_5406 ->
                                if (item?.amount ==
                                    1
                                ) {
                                    "basket of strawberries"
                                } else {
                                    "baskets of strawberries"
                                }
                            Items.BANANAS5_5416 -> if (item?.amount == 1) "basket of bananas" else "baskets of bananas"
                            else -> item?.name?.lowercase()
                        }
                    if (item == null) {
                        if (patch.plantable!!.harvestItem == Items.POISON_IVY_BERRIES_6018) {
                            npc(faceAnim, "There is no need for me to look after that poison ivy.")
                            stage = 400
                        } else {
                            npc(faceAnim, "Sorry, I won't protect that.").also { stage = END_DIALOGUE }
                        }
                    } else if (quickPay &&
                        !(
                            inInventory(player!!, item!!.id, item!!.amount) ||
                                inInventory(
                                    player!!,
                                    note(item!!).id,
                                    note(item!!).amount,
                                )
                        )
                    ) {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        npc(faceAnim, "I want $amount $protectionText for that.")
                        stage = 200
                    } else if (quickPay) {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        showTopics(
                            Topic("Yes", 20, true),
                            Topic("No", END_DIALOGUE, true),
                            title = "Pay $amount $protectionText?",
                        )
                    } else {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        npc(faceAnim, "If you like, but I want $amount $protectionText for that.")
                        stage++
                    }
                }
            }

            1 -> {
                if (!(
                        inInventory(player!!, item!!.id, item!!.amount) ||
                            inInventory(
                                player!!,
                                note(item!!).id,
                                note(item!!).amount,
                            )
                    )
                ) {
                    player("I'm afraid I don't have any of those at the moment.").also { stage = 10 }
                } else {
                    showTopics(
                        Topic(FaceAnim.NEUTRAL, "Okay, it's a deal.", 20),
                        Topic(FaceAnim.NEUTRAL, "No, that's too much.", 10),
                    )
                }
            }

            10 -> npc(faceAnim, "Well, I'm not wasting my time for free.").also { stage = END_DIALOGUE }

            20 -> {
                if (removeItem(player!!, item) || removeItem(player!!, note(item!!))) {
                    patch.protectionPaid = true
                    npc(
                        faceAnim,
                        "That'll do nicely, ${if (player!!.isMale) "sir" else "madam"}. Leave it with me - I'll make sure",
                        "those crops grow for you.",
                    ).also { stage = END_DIALOGUE }
                } else {
                    npc(faceAnim, "This shouldn't be happening. Please report this.").also {
                        stage = END_DIALOGUE
                    }
                }
            }

            100 -> player("Oh sorry, I forgot.").also { stage = END_DIALOGUE }

            200 -> player(FaceAnim.NEUTRAL, "Thanks, maybe another time.").also { stage = END_DIALOGUE }

            300 -> {
                if (removeItem(player!!, Item(Items.COINS_995, 200))) {
                    patch.clear()
                    dialogue("The gardener obligingly removes your tree.").also { stage = END_DIALOGUE }
                } else {
                    dialogue("You need 200 gp to pay for that.").also { stage = END_DIALOGUE }
                }
            }

            400 -> player("Really?").also { stage++ }
            401 ->
                npc(
                    "Yes, poison ivy is pretty hardy stuff, and most animals",
                    "will avoid eating it.",
                ).also { stage++ }

            402 -> npc("Hence, there is no reason to worry about it.").also { stage++ }
            403 -> player("Great.").also { stage = END_DIALOGUE }
        }
    }
}
