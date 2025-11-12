package content.region.island.braindeath.dialogue

import core.api.openOverlay
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the 50% Luke dialogue on Braindeath Island.
 */
@Initializable
class FiftyPercentLukeDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_ASKING, "Are you all right?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val ladLass = if (player!!.isMale) "lad" else "lass"
        val isCharos = player!!.equipment.containsAtLeastOneItem(Items.RING_OF_CHAROSA_6465)
        when(stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "Who goes there?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "Arr! A landlubber!").also { stage++ }
            2 -> npcl(FaceAnim.OLD_DEFAULT, "Begone afore I take my cutlass to ye! I've been charged with guardin' this gate and no noodle-armed landlubbers will make it past me alive!").also { stage++ }
            3 -> playerl(FaceAnim.NEUTRAL, "That's not a cutlass.").also { stage++ }
            4 -> playerl(FaceAnim.NEUTRAL, "I think it's a twig.").also { stage++ }
            5 -> npcl(FaceAnim.OLD_DEFAULT, "Ye cheeky begger! I was wavin' my finger at ye!").also { stage++ }
            6 -> playerl(FaceAnim.NEUTRAL, "All right...").also { stage++ }
            7 -> npcl(FaceAnim.OLD_DEFAULT, "Arrr... just 'cos me body happens to be 50% wood does not mean I'm heartless.").also { stage++ }
            8 -> npcl(FaceAnim.OLD_DEFAULT, "I got a bag of 'em here. Wanna see?").also { stage++ }
            9 -> playerl(FaceAnim.NEUTRAL, "I think I'll pass.").also { stage++ }
            10 -> showTopics(
                Topic("What happened to you?", if(!isCharos)  11 else 19),
                Topic("How flammable are you?", 51),
                Topic("So what is going on here anyway?", 58)
            )
            // What happened to you?
            11 -> npcl(FaceAnim.OLD_DEFAULT, "That be a tale so heart-wrenchin' that it has so far wrenched the hearts from over a dozen sturdier men than thee!").also { stage++ }
            12 -> npcl(FaceAnim.OLD_DEFAULT, "A tale of such woe that none but the stoniest hearts can hear it without sheddin' a single, poignant tear...").also { stage++ }
            13 -> npcl(FaceAnim.OLD_DEFAULT, "And I'll never tell ye, not even if ye wore an item, such as a ring, with powers of mind control!").also { stage++ }
            14 -> playerl(FaceAnim.NEUTRAL, "What if I said please?").also { stage++ }
            15 -> npcl(FaceAnim.OLD_DEFAULT, "Hmmmm...well alright...").also { stage++ }
            16 -> npcl(FaceAnim.OLD_DEFAULT, "Wait, no!").also { stage++ }
            17 -> npcl(FaceAnim.OLD_DEFAULT, "Get goin' landlubber before I take my splintery foot to yet behind!").also { stage++ }
            18 -> end()
            // What happened to you? (Dialogue with Ring of Charos in equipment).
            19 -> npcl(FaceAnim.OLD_DEFAULT, "Well ye seem like a $ladLass that can handle such a tale...so I'll tell ye...").also { stage++ }
            20 -> npcl(FaceAnim.OLD_DEFAULT, "Well, it all starts with this albatross...").also { stage++ }
            21 -> npcl(FaceAnim.OLD_DEFAULT, "Wait, never mind, I'll skip forward a bit.").also { stage++ }
            22 -> playerl(FaceAnim.HALF_ASKING, "Are you sure?").also { stage++ }
            23 -> npcl(FaceAnim.OLD_DEFAULT, "I have to $ladLass, Cap'n Donnie will flay what little is left of me if I told ye.").also { stage++ }
            24 -> npcl(FaceAnim.OLD_DEFAULT, "But anyway, I got recruited to the zombie pirates along with the rest of the crew in an unspecified incident involvin this albatross.").also { stage++ }
            25 -> npcl(FaceAnim.OLD_DEFAULT, "We was sailin' along happily, and I was partakin' of a little 'rum' in the crows nest.").also { stage++ }
            26 -> npcl(FaceAnim.OLD_DEFAULT, "Well, we hit either a really rough wave or some rocks.").also { stage++ }
            27 -> npcl(FaceAnim.OLD_DEFAULT, "Twas kind of hard for me to tell which, as I was well out of it by then!").also { stage++ }
            28 -> npcl(FaceAnim.OLD_DEFAULT, "Regardless, I toppled from the crows nest into the water.").also { stage++ }
            29 -> playerl(FaceAnim.NEUTRAL, "Is that how you got so badly injured?").also { stage++ }
            30 -> npcl(FaceAnim.OLD_DEFAULT, "No $ladLass!").also { stage++ }
            31 -> npcl(FaceAnim.OLD_DEFAULT, "What happened next was that I discovered a new, previously uncharted reef of hard, spiky coral.").also { stage++ }
            32 -> npcl(FaceAnim.OLD_DEFAULT, "I made a mental note of its location, and to this day it is still marked on our fleet's charts as Lukes Reef.").also { stage++ }
            33 -> npcl(FaceAnim.OLD_DEFAULT, "I managed to grab a hold of our ship, the Inebriated, as it passed overhead.").also { stage++ }
            34 -> npcl(FaceAnim.OLD_DEFAULT, "And then I discovered another, taller, spikier reef of even sharper and more painful coral.").also { stage++ }
            35 -> npcl(FaceAnim.OLD_DEFAULT, "To this day it is still marked on our fleet's charts as The Other 50% Reef.").also { stage++ }
            36 -> playerl(FaceAnim.NEUTRAL, "Owwwwwwwww...").also { stage++ }
            37 -> npcl(FaceAnim.OLD_DEFAULT, "It gets worse...").also { stage++ }
            38 -> npcl(FaceAnim.OLD_DEFAULT, "When they hauled what was left of me on deck, they dropped me onto the floor while they decided what to do with me.").also { stage++ }
            39 -> npcl(FaceAnim.OLD_DEFAULT, "Bear in mind this would be on a ghost ship, the planks of which sweat a thick mixture of stagnant water...and pure salt crystals.").also { stage++ }
            40 -> playerl(FaceAnim.NEUTRAL, "Oh...my...god...").also { stage++ }
            41 -> npcl(FaceAnim.OLD_DEFAULT, "But on the good side, all my thrashin' and pained squealin' settled the matter in the Captain's mind, and he had the shipwright carve me half a body out of his Witchwood Planks.").also { stage++ }
            42 -> playerl(FaceAnim.NEUTRAL, "Witchwood? What's that?").also { stage++ }
            43 -> npcl(FaceAnim.OLD_DEFAULT, "Tis a special, magical wood from a now extinct tree.").also { stage++ }
            44 -> npcl(FaceAnim.OLD_DEFAULT, "Once they nailed it all in place the stuff moves like it is part of me body.").also { stage++ }
            45 -> npcl(FaceAnim.OLD_DEFAULT, "The stuff will also grow back if it breaks, which is dead handy!").also { stage++ }
            46 -> playerl(FaceAnim.NEUTRAL, "Wow, that stuff must be very valuable!").also { stage++ }
            47 -> npcl(FaceAnim.OLD_DEFAULT, "Arr! That it be!").also { stage++ }
            48 -> npcl(FaceAnim.OLD_DEFAULT, "So, that be the tale of how I managed to lose precisely 50% of my body.").also { stage++ }
            49 -> playerl(FaceAnim.NEUTRAL, "There there.").also { stage++ }
            50 -> end()
            // How flammable are you?
            51 -> npcl(FaceAnim.HALF_ASKING, "What kind of question is that?").also { stage++ }
            52 -> playerl(FaceAnim.FRIENDLY,"I'll soon have you out of the way! Burn!").also {
                player.animate(Animation(Animations.HUMAN_LIGHT_FIRE_WITH_TINDERBOX_733))
                stage++
            }
            53 -> npcl(FaceAnim.OLD_DEFAULT, "Don't make me angry! Ye'll not like me when I'm angry!").also {
                openOverlay(player, Components.FADE_TO_BLACK_115)
                stage++
            }
            54 -> sendDialogue(player, "One 500 hit combo later...").also {
                openOverlay(player, Components.FADE_FROM_BLACK_170)
                stage++
            }
            55 -> npcl(FaceAnim.OLD_DEFAULT, "Let that be a lesson to ye!").also { stage++ }
            56 -> playerl(FaceAnim.NEUTRAL, "My world is an ocean of paaaain!").also { stage++ }
            57 -> end()
            // So what is going on here anyway?
            58 -> npcl(FaceAnim.OLD_DEFAULT, "Ye expect me to talk?").also { stage++ }
            59 -> playerl(FaceAnim.NEUTRAL, "No Mr. Luke, I expect you to die!").also { stage++ }
            60 -> npcl(FaceAnim.OLD_DEFAULT, "Hah! I'm one step ahead of ye!").also { stage++ }
            61 -> playerl(FaceAnim.NEUTRAL, "Egad, outsmarted by the man with the wooden brain.").also { stage++ }
            62 -> playerl(FaceAnim.NEUTRAL, "But seriously, what is going on here?").also { stage++ }
            63 -> npcl(FaceAnim.OLD_DEFAULT, "I can't tell ye $ladLass.").also { stage++ }
            64 -> npcl(FaceAnim.OLD_DEFAULT, "The Cap'n would have me whittled down to toothpicks if I did.").also { stage++ }
            65 -> playerl(FaceAnim.NEUTRAL, "Well if you can't tell me, perhaps you could show me through the medium of Interpretive Dance?").also { stage++ }
            66 -> npcl(FaceAnim.OLD_DEFAULT, "No. Just...no.").also { stage++ }
            67 -> playerl(FaceAnim.NEUTRAL, "Mime?").also { stage++ }
            68 -> npcl(FaceAnim.OLD_DEFAULT, "Look, $ladLass I'm not tellin' ye a thing! So clear out while ye still can!").also { stage++ }
            69 -> end()
        }

        return true
    }

    override fun newInstance(player: Player?): Dialogue = FiftyPercentLukeDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.LUKE_50PERCENT_2828)
}