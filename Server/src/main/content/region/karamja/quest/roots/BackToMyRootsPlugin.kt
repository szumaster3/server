package content.region.karamja.quest.roots

import content.data.GameAttributes
import content.region.karamja.quest.roots.npc.HoracioNPC
import content.region.karamja.quest.roots.npc.WildJadeVineNPC
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.world.map.build.DynamicRegion
import core.game.world.repository.Repository
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import shared.consts.*

class BackToMyRootsPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles taking a hand from smelly packet
         * Outside the RPDT in East Ardougne.
         */

        on(Scenery.SMELLY_PACKAGE_27055, IntType.SCENERY, "open") { player, _ ->
            player.animate(Animation(Animations.HUMAN_WITHDRAW_833))
            val npcId = Repository.findNPC(NPCs.RPDT_EMPLOYEE_843)
            npcId?.sendChat("Oh great, back to work.")
            setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 20, true)
            sendItemDialogue(player, Items.HAND_11763, "You find a hand with a scrap of a wizards robe attached.")
            addItemOrDrop(player, Items.HAND_11763)
            return@on true
        }

        /*
         * Handles spawn the wild jade vine.
         */

        onUseWith(IntType.SCENERY, Items.SEALED_POT_11777, Scenery.HORACIO_S_JADE_VINE_PATCH_27061) { player, used, with ->
            if(removeItem(player, used.asItem())) {
                lock(player, 3)
                openInterface(player, 795)
                sendString(player, "The small cutting slowly grows and matures....", 795, 1)
                runTask(player, 3) {
                    closeInterface(player)
                    setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 55) // n.
                    openDialogue(player, HoracioDialogueFile())
                }
            }
            return@onUseWith true
        }

        /*
         * Handles battle.
         */

        on(Scenery.WILD_JADE_VINE_27062, IntType.SCENERY, "attack") { player, _ ->
            val horacio = HoracioNPC()
            val wildJade = WildJadeVineNPC()
            lock(player, 10)
            openOverlay(player, Components.FADE_TO_BLACK_120)

            region.add(player)

            setAttribute(player, GameAttributes.VINE_FIGHT, player.location)
            registerLogoutListener(player, GameAttributes.VINE_FIGHT) { p ->
                p.location = getAttribute(p, GameAttributes.VINE_FIGHT, player.location)
                removeAttribute(p, GameAttributes.VINE_FIGHT)
            }

            horacio.init()
            wildJade.init()

            runTask(player, 10) {
                closeOverlay(player)
                openOverlay(player, Components.FADE_FROM_BLACK_170)

                setVarbit(player, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 60)

                player.properties.teleportLocation =
                    region.baseLocation.transform(15, 49, 0)
                horacio.properties.teleportLocation =
                    region.baseLocation.transform(12, 49, 0)
                wildJade.properties.teleportLocation =
                    region.baseLocation.transform(15, 51, 0)
            }
            return@on true
        }

    }

    companion object {
        val region: DynamicRegion = DynamicRegion.create(10547)
    }
}

private class HoracioDialogueFile : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.HORACIO_845)
        when(stage) {
            0 -> npcl(FaceAnim.SCARED , "ARG! It's grown out of control! You should have pruned it yesterday... Now we'll need a hatchet and secateurs to deal with it. Can you help fight it?").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "Okay, I'm ready for combat.").also { stage++ }
            2 -> npcl(FaceAnim.SCARED , "It doesn't look like you're prepared properly. You'll need to be wielding a hatchet to fight the vine!").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "Okay, I'll go grab one.").also { stage = END_DIALOGUE }
        }
    }
}