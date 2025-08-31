package content.region.kandarin.camelot.quest.arthur.cutscene

import core.api.*
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations

/**
 * Handles hiding in the crate.
 */
class CrateCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(2778, 3401, 0))
        loadRegion(11161)
        teleport(player, base.transform(26, 47, 0))
        player.setDirection(Direction.WEST)
        player.animate(Animation(Animations.CROUCH_HIDE_4552))
        player.lock()
    }

    override fun onLogout(player: Player) {
        setExit(Location.create(2802, 3442, 0))
        super.onLogout(player)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0  -> dialogueUpdate(false, "You wait.")
            1  -> dialogueUpdate(false, "And wait...")
            2  -> dialogueUpdate(false, "And wait...")
            3  -> dialogueUpdate(false, "You hear voices outside the crate.", "${core.tools.DARK_BLUE}Is this your crate Arnhein?</col>")
            4  -> dialogueUpdate(false, "${core.tools.DARK_RED}Yeah I think so. Pack it aboard as soon as you can.</col>", "${core.tools.DARK_RED}I'm on a tight schedule for deliveries!</col>")
            5  -> dialogueUpdate(false, "You feel the crate being lifted.", "${core.tools.DARK_BLUE}Oof. Wow, this is pretty heavy!</col>", "${core.tools.DARK_BLUE}I never knew candles weighed so much!</col>")
            6  -> dialogueUpdate(false, "${core.tools.DARK_RED}Quit your whining, and stow it in the hold.</col>")
            7  -> dialogueUpdate(false, "You feel the crate being put down inside the ship.")
            8  -> dialogueUpdate(false, "You wait...")
            9  -> dialogueUpdate(false, "And wait...")
            10 -> dialogueUpdate(false, "${core.tools.DARK_RED}Casting off!</col>")
            11 -> dialogueUpdate(false, "You feel the ship start to move.")
            12 -> dialogueUpdate(false, "Feels like you're now out at sea.")
            13 -> dialogueUpdate(false, "The ship comes to a stop.")
            14 -> dialogueUpdate(false, "${core.tools.DARK_RED}Unload Mordred's deliveries onto the jetty.</col>", "${core.tools.DARK_BLUE}Aye-aye cap'n!</col>")
            15 -> dialogueUpdate(false, "You feel the crate being lifted.")
            16 -> dialogueUpdate(false, "You can hear someone mumbling outside the crate.", "", "${core.tools.DARK_BLUE}...stupid Arhein... making me... candles...", "${core.tools.DARK_BLUE}never weigh THIS much....hurts....union about this!...")
            17 -> dialogueUpdate(false, "${core.tools.DARK_BLUE}...if....MY ship be different!...", "${core.tools.DARK_BLUE}stupid! Arhein...")
            18 -> dialogueUpdate(false, "You feel the crate being put down.")
            19 -> {
                setTitle(player, 2)
                sendDialogueOptions(player, "Would you like to get back out of the crate?", "Yes.", "No.")
                addDialogueAction(player) { _, selected ->
                    when(selected) {
                        2 -> dialogueUpdate(false, "You climb out of the crate.").also { updateStage(23) }
                        else -> dialogueUpdate(false, "You decide to stay in the crate.").also { updateStage(20) }
                    }
                }
            }
            20 -> dialogueUpdate(false, "You wait...")
            21 -> dialogueUpdate(false, "And wait...")
            22 -> dialogueUpdate(false, "And wait...").also { updateStage(18) }
            23 -> end()
        }
    }
}