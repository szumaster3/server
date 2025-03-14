package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Hydra dialogue.
 */
@Initializable
public class HydraDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new HydraDialogue(player);
    }

    /**
     * Instantiates a new Hydra dialogue.
     */
    public HydraDialogue() {}

    /**
     * Instantiates a new Hydra dialogue.
     *
     * @param player the player
     */
    public HydraDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Raaaspraaasp? (Isn't it hard to get things done with just one head?)");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Raaaasp raaaasp! (Man, I feel good!)");
                stage = 4;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Rassssp rasssssp! (You know, two heads are better than one!)");
                stage = 10;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Raaaaaaasp. (Siiiigh.)");
                stage = 12;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Not really!");
                stage++;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Raaasp raaaaap raaaasp?", "(Well I suppose you work with what you got, right?)");
                stage++;
                break;
            case 2:
                npc(FaceAnim.CHILD_NORMAL, "Raaaaaasp raaaasp raaaasp.", "(At least he doesn't have someone whittering in their ear all the time.)");
                stage++;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Raaaaaaasp!", "(Quiet, you!)");
                stage = END_DIALOGUE;
                break;
            case 4:
                npc(FaceAnim.CHILD_NORMAL, "Raaasp ssssss raaaasp.", "(That's easy for you to say.)");
                stage++;
                break;
            case 5:
                playerl(FaceAnim.HALF_ASKING, "What's up?");
                stage++;
                break;
            case 6:
                npc(FaceAnim.CHILD_NORMAL, "Raaa....", "(well...)");
                stage++;
                break;
            case 7:
                npc(FaceAnim.CHILD_NORMAL, "Raaaaasp sss rassssp.", "(Don't pay any attention, they are just feeling whiny.)");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.HALF_ASKING, "But they're you, aren't they?");
                stage++;
                break;
            case 9:
                npc(FaceAnim.CHILD_NORMAL, "Raaaasp raasp rasssp!", "(Don't remind me!)");
                stage = END_DIALOGUE;
                break;
            case 10:
                npc(FaceAnim.CHILD_NORMAL, "Raaaasp rassssp sssssp....", "(Unless you're the one doing all the heavy thinking....)");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "I think I'll stick to one for now, thanks.");
                stage = END_DIALOGUE;
                break;
            case 12:
                npc(FaceAnim.CHILD_NORMAL, "Raasp raasp raaaaasp?", "(What's up this time?)");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.HALF_ASKING, "Can I help?");
                stage++;
                break;
            case 14:
                npc(FaceAnim.CHILD_NORMAL, "Rasssp ssssssp? raaaaasp raaaasp.", "(Do you mind? This is a private conversation.)");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "Well, excu-u-use me.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.HYDRA_6811, NPCs.HYDRA_6812};
    }
}
