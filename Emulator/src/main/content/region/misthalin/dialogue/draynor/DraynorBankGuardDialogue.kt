package content.region.misthalin.dialogue.draynor

import core.api.Container
import core.api.inBank
import core.api.inInventory
import core.api.removeItem
import core.game.activity.ActivityManager
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class DraynorBankGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Yes?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (!player.getSavedData().globalData.isDraynorRecording()) {
                    options(
                        "Can I deposit my stuff here?",
                        "That wall doesn't look very good.",
                        "Sorry, I don't want anything.",
                    ).also { stage++ }
                } else {
                    options("Can I see that recording again, please?", "Sorry, I don't want anything.").also {
                        stage = 70
                    }
                }
            }

            70 ->
                when (buttonId) {
                    1 -> player("Can I see that recording again, please?").also { stage = 71 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Sorry, I don't want anything.").also { stage = 30 }
                }

            71 -> npc("I'd like you to pay me 50 gp first.").also { stage++ }
            72 -> {
                if (!inInventory(player, Items.COINS_995, 50)) {
                    player("I'm not carrying that much.").also { stage = 73 }
                    return true
                } else {
                    options("Ok, here's 50 gp.", "Thanks, maybe another day.").also { stage = 80 }
                }
            }

            73 -> {
                if (inBank(player, Items.COINS_995, 50)) {
                    npc(
                        "As a bank employee, I suppose I could take the money",
                        "directly from your bank account.",
                    ).also {
                        stage =
                            74
                    }
                } else {
                    npc("Come back when you do.").also { stage = 75 }
                }
            }

            74 ->
                options("Ok, you can take 50 gp from my bank account.", "Thanks, maybe another day.").also {
                    stage = 76
                }

            75 -> end()
            76 ->
                when (buttonId) {
                    1 -> player("Ok, you can take 50 gp from my bank account.").also { stage = 79 }
                    2 -> player("Thanks, maybe another day.").also { stage = 77 }
                }

            77 -> npc("Ok.").also { stage = 78 }
            78 -> end()
            79 -> {
                end()
                if (inBank(player, Items.COINS_995, 50) &&
                    removeItem(player, Item(Items.COINS_995, 50), Container.BANK)
                ) {
                    startRecording(player)
                }
            }

            80 ->
                when (buttonId) {
                    1 -> player("Ok, here's 50 gp.").also { stage = 81 }
                    2 -> player("Thanks, maybe another day.").also { stage = 77 }
                }

            81 -> {
                end()
                if (!inInventory(player, Items.COINS_995, 50)) {
                    end()
                    return true
                }
                if (removeItem(player, Item(Items.COINS_995, 50))) {
                    startRecording(player)
                }
            }

            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Hello. Can I deposit my stuff here?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "That wall doesn't look very good.").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Sorry, I don't want anything.").also { stage = 30 }
                }

            30 -> npc(FaceAnim.HALF_GUILTY, "Ok.").also { stage++ }
            31 -> end()
            20 -> npc(FaceAnim.HALF_GUILTY, "No, it doesn't.").also { stage++ }
            21 ->
                options(
                    "Are you going to tell me what happened?",
                    "Alright, I'll stop bothering you now.",
                ).also { stage++ }
            22 ->
                when (buttonId) {
                    1 -> player("Are you going to tell me what happened?").also { stage = 25 }
                    2 -> player("Alright, I'll stop bothering you now.").also { stage = 23 }
                }
            23 -> npc("Good day, sir.").also { stage++ }
            24 -> end()
            25 -> npc("I could do.").also { stage++ }
            26 -> player("Ok, go on!").also { stage++ }
            27 -> npc("Someone smashed the wall when", "they were robbing the bank.").also { stage++ }
            28 -> player("Someone's robbed the bank?").also { stage++ }
            29 -> npc("Yes.").also { stage = 50 }
            50 -> player("But... was anyone hurt?", "Did they get anything valuable?").also { stage++ }
            51 -> npc("Yes, but we were able to get more staff and mend the", "wall easily enough.").also { stage++ }
            52 ->
                npc(
                    "The Bank has already replaced all the stolen items that",
                    "belonged to customers.",
                ).also { stage++ }
            53 -> player("Oh, good... but the bank staff got hurt?").also { stage++ }
            54 -> npc("Yes but the new ones are just as good.").also { stage++ }
            55 -> player("You're not very nice, are you?").also { stage++ }
            56 -> npc("No-one's expecting me to be nice.").also { stage++ }
            57 -> player("Anyway... So, someone's robbed the bank?").also { stage++ }
            58 -> npc("Yes.").also { stage++ }
            59 -> player("Do you know who did it?").also { stage++ }
            60 ->
                npc(
                    "We are fairly sure we know who the robber was. The",
                    "security recording was damaged in the attack, but it still",
                    "shows his face clearly enough.",
                ).also {
                    stage++
                }
            61 -> player("You've got a security recording?").also { stage++ }
            62 -> npc("Yes. Our insurers insisted that we", "install a magical scrying orb.").also { stage++ }
            63 -> player("Can I see the recording?").also { stage++ }
            64 -> npc("I suppose so. But it's quite long.").also { stage++ }
            65 -> options("That's ok, show me the recording.", "Thanks, maybe another day.").also { stage++ }
            66 ->
                when (buttonId) {
                    1 -> player("That's ok, show me the recording.").also { stage = 68 }
                    2 -> player("Thanks, maybe another day.").also { stage = 67 }
                }
            67 -> end()
            68 ->
                npc(
                    "Alright... The bank's magical playback device will feed the",
                    "recorded images into your mind. Just shut your eyes.",
                ).also {
                    stage++
                }
            69 -> startRecording(player)
            10 -> npc(FaceAnim.HALF_GUILTY, "No. I'm a security guard, not a bank clerk.").also { stage++ }
            11 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DraynorBankGuardDialogue(player)
    }

    private fun startRecording(player: Player) {
        end()
        ActivityManager.start(player, "dbr cutscene", false)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BANK_GUARD_2574)
    }
}
