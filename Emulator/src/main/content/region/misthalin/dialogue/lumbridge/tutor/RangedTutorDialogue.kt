package content.region.misthalin.dialogue.lumbridge.tutor

import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class RangedTutorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val BOW = Item(9705)
    private val ARROW = Item(9706)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (args.size == 2) {
            if (player.getSavedData().globalData.getTutorClaim() > System.currentTimeMillis()) {
                npc(
                    "I work with the Ranged Combat tutor to give out",
                    "consumable items that you may need for combat such",
                    "as arrows and runes. However we have had some",
                    "cheeky people try to take both!",
                )
                stage = 200
                return true
            }
            stage = 99
            if (player.inventory.containsItem(BOW)) {
                npc("You have a training bow in your inventory.")
                return true
            }
            if (player.bank.containsItem(BOW)) {
                npc("You have a training bow in your bank.")
                return true
            }
            if (player.equipment.containsItem(BOW)) {
                npc("You're wielding your training bow.")
                return true
            }
            if (player.inventory.containsItem(ARROW)) {
                npc("You have a training arrows in your inventory.")
                return true
            }
            if (player.bank.containsItem(ARROW)) {
                npc("You have a training arrows in your bank.")
                return true
            }
            if (player.equipment.containsItem(ARROW)) {
                npc("You're wielding training arrows.")
                return true
            }
            if (player.inventory.freeSlots() < 2) {
                end()
                player.packetDispatch.sendMessage("Not enough inventory space.")
                return true
            }
            if (player.inventory.add(BOW)) {
                sendItemDialogue(
                    player,
                    BOW,
                    "Nemarti gives you a Training Shortbow. It can only be used with Training Arrows.",
                )
                stage = 230
                player.getSavedData().globalData.setTutorClaim(System.currentTimeMillis() + 1200000)
                return true
            }
            return true
        }
        interpreter.sendOptions(
            "Select an Option",
            "Can you teach me the basics please?",
            "What about fletching?",
            "Goodbye.",
        )
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            230 -> {
                val arrows = Item(ARROW.id, 25)
                if (player.inventory.add(arrows)) {
                    interpreter.sendItemMessage(
                        arrows,
                        "Nemarti gives you 25 training arrows. They can only be used with the Training Shortbow.",
                    )
                    stage = 231
                    player.getSavedData().globalData.setTutorClaim(System.currentTimeMillis() + 1200000)
                    return true
                }
            }

            231 -> end()
            99 -> end()
            1 ->
                when (buttonId) {
                    1 -> {
                        player(FaceAnim.HALF_GUILTY, "Can you teach me the basics please?")
                        stage = 10
                    }

                    2 -> {
                        player(FaceAnim.HALF_GUILTY, "What about fletching?")
                        stage = 20
                    }

                    3 -> {
                        player(FaceAnim.HALF_GUILTY, "Goodbye.")
                        stage = 30
                    }
                }

            10 -> {
                npc("To start with you'll need a bow and arrows.")
                stage = 11
            }

            11 -> {
                npc(
                    "Mikasi, the Magic Combat tutor and I both give you",
                    "items every 30 minutes, however you must choose",
                    "wether you want runes or ranged equipment. To",
                    "claim ranged equipment, right-click on me and choose",
                )
                stage = 12
            }

            12 -> {
                npc("Claim, to claim runes right-click on the Magic Combat", "tutor and select Claim.")
                stage = 13
            }

            13 -> {
                npc(
                    "When you have both bow and arrows, wield them by",
                    "right-clicking on them in your inventory and selecting",
                    "wield.",
                )
                stage = 14
            }

            14 -> {
                npc(
                    "To set the way you shoot, click on the crossed swords",
                    "above your inventory. This will open the combat",
                    "interface where you can pick how you shoot your bow.",
                    "Accurate means that you will shoot less often but be",
                )
                stage = 15
            }

            15 -> {
                npc(
                    "more likely to hit, rapid means you shoot more often",
                    "but might not hit so often and long range means just",
                    "that, it increases your range. I prefer rapid personally,",
                    "experiment and try it out!",
                )
                stage = 16
            }

            16 -> {
                npc(
                    "The Training Shortbow and Training Arrows can only",
                    "be used together. Remember to pick up your arrows,",
                    "re-use them and come back when you need more.",
                )
                stage = 17
            }

            17 -> end()
            20 -> {
                npc(
                    "Ahh the art of making your own bow and arrows. It's",
                    "quite simple really. You'll need an axe to cut some logs",
                    "from trees and a knife. Knives can be found in and",
                    "arround the Lumbridge castle and in the Varrock",
                )
                stage = 21
            }

            21 -> {
                npc(FaceAnim.HALF_GUILTY, "General store upstairs.")
                stage = 22
            }

            22 -> {
                npc(
                    "Use your knife on the logs, this will bring up a list of",
                    "items you can make. Right-click on the item of your",
                    "choice and choose the amount to fletch.",
                )
                stage = 23
            }

            23 -> {
                npc(
                    "For arrows you will need to smith some arrow heads",
                    "and kill some chickens for feathers. Add the feathers",
                    "and heads to the shafts to make arrows you can use.",
                )
                stage = 24
            }

            24 -> {
                npc(
                    "You'll need to find a flax field, there's one south of",
                    "Seer's Village. Gather flax, then spin it on a spinning",
                    "wheel, there's one in Seers' Village too. This makes bow",
                    "strings which you can then use on the unstrung bows",
                )
                stage = 25
            }

            25 -> {
                npc(FaceAnim.HALF_GUILTY, "to make a working bow!")
                stage = 26
            }

            26 -> {
                player(FaceAnim.HALF_GUILTY, "Brilliant. If I forget anything I'll come talk to you", "again.")
                stage = 27
            }

            27 -> {
                options("Can you teach me the basics please?", "What about fletching?", "Goodbye.")
                stage = 1
            }

            30 -> end()
            200 -> {
                npc(
                    "So, every half an hour, you may come back and claim",
                    "either arrows OR runes, but not both. Come back in a",
                    "while for runes, or simply make your own.",
                )
                stage = 201
            }

            201 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RANGED_TUTOR_1861)
    }
}
