package content.region.misthalin.quest.priest.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import org.rs.consts.Quests

@Initializable
class PriestDoorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var door: Scenery? = null

    override fun open(vararg args: Any): Boolean {
        door = args[0] as Scenery
        if (getQuestStage(player, Quests.PRIEST_IN_PERIL) == 10) {
            sendDialogue(
                "You knock at the door...You hear a voice from inside.",
                "${BLUE}Who are you, and what do you want?",
            )
            stage = 0
        }
        if (getQuestStage(player, Quests.PRIEST_IN_PERIL) == 12) {
            sendDialogue(
                "You knock at the door...You hear a voice from inside.",
                "${BLUE}You again?",
                "${BLUE}What do you want now?",
            )
            stage = 11
        }
        if (getQuestStage(player, Quests.PRIEST_IN_PERIL) >= 13) {
            sendDialogue("You knock at the door...The door swings open as you knock.")
            stage = 20
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                player(FaceAnim.HALF_GUILTY, "Ummmm.....")
                stage = 1
            }

            1 -> {
                player(FaceAnim.HALF_GUILTY, "Roald sent me to check on Drezel.")
                stage = 2
            }

            2 -> {
                interpreter.sendDialogue(
                    "$BLUE(Psst... Hey... Who's Roald? Who's Drezel?)$RED (Uh... isn't Drezel that",
                    "${RED}dude upstairs? Oh, wait, Roald's the King of Varrock right?)$BLUE (He is???",
                    "${BLUE}Aw man... Hey, you deal with this okay?) He's just coming! Wait a",
                    "${BLUE}second!$RED Hello, my name is Drevil.$BLUE (Drezel!)$RED I mean Drezel.",
                )
                stage = 3
            }

            3 -> {
                player(
                    FaceAnim.HALF_GUILTY,
                    "Well, as I say, the King sent me to make sure",
                    "everything's okay with you.",
                )
                stage = 4
            }

            4 -> {
                interpreter.sendDialogue("${RED}And, uh, what would you do if everything wasn't okay with me?")
                stage = 5
            }

            5 -> {
                player(FaceAnim.HALF_GUILTY, "I'm not sure. Ask you what help you need I suppose.")
                stage = 6
            }

            6 -> {
                interpreter.sendDialogue(
                    "${RED}Ah, good, well, I don't think...$BLUE (Psst... hey... the dog!)$RED OH! Yes, of",
                    "course! Will you do me a favour adventurer?",
                )
                stage = 7
            }

            7 -> {
                player(FaceAnim.HALF_GUILTY, "Sure. I'm a helpful person!")
                stage = 8
            }

            8 -> {
                interpreter.sendDialogue(
                    "${RED}HAHAHAHA! Really? Thanks buddy! You see that mausoleum out",
                    "${RED}there? There's a horrible big dog underneath it that I'd like you to",
                    "${RED}kill for me! It's been really bugging me! Barking all the time and",
                    "${RED}stuff! Please kill it for me buddy!",
                )
                stage = 9
            }

            9 -> {
                player(FaceAnim.HALF_GUILTY, "Okey-dokey, one dead dog coming up.")
                stage = 10
            }

            10 -> {
                end()
                setQuestStage(player, Quests.PRIEST_IN_PERIL, 11)
            }

            11 -> {
                player(FaceAnim.HALF_GUILTY, "I killed that dog for you.")
                stage = 12
            }

            12 -> {
                interpreter.sendDialogue(
                    "${BLUE}HAHAHAHAHA! Really? Hey, that's great!$RED Yeah thanks a lot buddy!",
                    "${RED}HAHAHAHAHAHA",
                )
                stage = 13
            }

            13 -> {
                player(FaceAnim.HALF_ASKING, "What's so funny?")
                stage = 14
            }

            14 -> {
                interpreter.sendDialogue(
                    "${BLUE}HAHAHAHA nothing buddy! We're just so grateful to you!",
                    "${BLUE}HAHAHA$RED Yeah, maybe you should go tell the King what a great job",
                    "${RED}you did buddy! HAHAHA",
                )
                stage = 15
            }

            15 -> end()
            20 -> {
                end()
                DoorActionHandler.handleDoor(player, door!!)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue = PriestDoorDialogue(player)

    override fun getIds(): IntArray = intArrayOf(54584)
}
