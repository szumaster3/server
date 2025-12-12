package content.region.asgarnia.goblin_village.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Goblin Village guards dialogue.
 */
class GoblinVillageGuardDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player("You're a long way out from the city.").also { stage++ }
            1 -> npc(FaceAnim.OLD_DEFAULT,"I know. We guards usually stay by banks and shops,", "but I got sent all the way out here to keep an eye on", "the brigands loitering just south of here.").also { stage++ }
            2 -> player("Sounds more exciting than standing", "around guarding banks and shops.").also { stage++ }
            3 -> npc(FaceAnim.OLD_DEFAULT,"It's not too bad. At least I don't get attacked so often", "out here. Guards in the cities get killed all the time.",).also { stage++ }
            4 -> player("Honestly, people these days just don't know how to behave!").also { stage = END_DIALOGUE }
        }
    }
}
