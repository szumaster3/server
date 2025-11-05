package content.region.kandarin.yanille.quest.handsand2

import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import shared.consts.*

/**
 * Represents the cutscene in Returning clarence mini-quest.
 */
class FuneralCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(10288)
    }
    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(5)
            }
            1 -> {
                addNPC(NPCs.ZAVISTIC_RARVE_2059, 24, 19, Direction.NORTH)
                addNPC(NPCs.WIZARD_13, 23, 20, Direction.EAST)
                addNPC(NPCs.PROFESSOR_IMBLEWYN_4586, 23, 21, Direction.SOUTH_EAST)
                addNPC(NPCs.ROBE_STORE_OWNER_1658, 25, 21, Direction.SOUTH_WEST)
                teleport(player, 25, 20)
                moveCamera(18, 12, 1000)
                rotateCamera(24, 7, 1000)
                player.face(getNPC(NPCs.ZAVISTIC_RARVE_2059))
                fadeFromBlack()
                timedUpdate(3)
            }
            2 -> {
                moveCamera(18, 12, 800)
                rotateCamera(24, 20, 800, 3)
                timedUpdate(3)
            }
            3 -> dialogueUpdate(NPCs.ZAVISTIC_RARVE_2059, FaceAnim.NEUTRAL, "Welcome, friends. Today we honour a wizard scholar and send him to study in the hall of the Great Wizards after his work was cut short on Gielinor by the greed of a most evil man.")
            4 -> dialogueUpdate(NPCs.ZAVISTIC_RARVE_2059, FaceAnim.NEUTRAL, "Clarence was my apprentice. He had not yet earned his 6th level of magic, but his intelligence served him well and, in time, he would no doubt have become one of the greatest wizards in Gielinor.")
            5 -> dialogueUpdate(NPCs.ROBE_STORE_OWNER_1658, FaceAnim.NEUTRAL, "* whispers * He was actually a bit of a new boy around here, from the Wizards' Tower at Draynor, but he was a decent sort and a member of a society within the Guild called the New Order Occult Bookists, a small")
            6 -> dialogueUpdate(NPCs.ROBE_STORE_OWNER_1658, FaceAnim.NEUTRAL, "group who study magic from the old tomes.")
            7 -> dialogueUpdate(NPCs.ZAVISTIC_RARVE_2059, FaceAnim.SAD, "So, it is with sadness in our hearts that we bid farewell to Clarence today. May he rest in peace within the grounds of the Guild he loved.")
            8 -> {
                visualize(getNPC(NPCs.ZAVISTIC_RARVE_2059)!!, Animations.CAST_SPELL_707, Graphics.EARTH_BLAST_CAST_138)
                setVarbit(player, Vars.VARBIT_MINIQUEST_RETURNING_CLARENCE_PROGRESS_4054, 1, true)
                animate(getNPC(NPCs.ROBE_STORE_OWNER_1658)!!, Animations.BOW_858)
                sendChat(getNPC(NPCs.ROBE_STORE_OWNER_1658)!!, "Hear hear!")
                sendChat(getNPC(NPCs.PROFESSOR_IMBLEWYN_4586)!!, "Rest in pieces!")
                timedUpdate(10)
            }
            9 -> dialogueUpdate(NPCs.WIZARD_13, FaceAnim.SAD, "* whispers * I say, old chap, it's a pity you couldn't find his other foot. Now he has only one foot in the grave.")
            10 -> playerDialogueUpdate(FaceAnim.SAD, "Umm... Sorry, I couldn't foot the bill.")
            11 -> {
                getNPC(NPCs.ZAVISTIC_RARVE_2059)!!.face(player)
                player.face(getNPC(NPCs.ZAVISTIC_RARVE_2059)!!)
                dialogueUpdate(NPCs.ZAVISTIC_RARVE_2059, FaceAnim.SAD, "Thank you for coming to honour Clarence today.")
            }
            12 -> dialogueUpdate(NPCs.ZAVISTIC_RARVE_2059, FaceAnim.FRIENDLY, "And particular thanks to ${player.username}, who solved the case and will now be richly rewarded.")
            13 -> {
                visualize(getNPC(NPCs.ZAVISTIC_RARVE_2059)!!, Animations.CAST_SPELL_1161, Graphics.BOUNTY_LOCATE_CAST_142)
                timedUpdate(3)
            }
            14 -> {
                sendGraphics(Graphics.STUN_PROJECTILE_174, player.location)
                timedUpdate(6)
            }
            15 -> {
                sendItemDialogue(player, Items.FIRE_RUNE_6428, "You have been awarded 10,000 experience in Magic!")
                timedUpdate(6)
            }
            16 -> end {}
        }
    }
}