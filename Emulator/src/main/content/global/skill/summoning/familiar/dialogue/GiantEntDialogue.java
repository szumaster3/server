package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Giant ent dialogue.
 */
@Initializable
public class GiantEntDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new GiantEntDialogue(player);
    }

    /**
     * Instantiates a new Giant ent dialogue.
     */
    public GiantEntDialogue() {}

    /**
     * Instantiates a new Giant ent dialogue.
     *
     * @param player the player
     */
    public GiantEntDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 8)) {
            case 0:
                npc(FaceAnim.CHILD_NORMAL, "Creeeeeeeeeeeak.....", "(I.....)");
                stage = 0;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Creak..... Creaaaaaaaaak.....", "(Am.....)");
                stage = 0;
                break;
            case 2:
                npc(FaceAnim.CHILD_NORMAL, "Grooooooooan.....", "(Feeling.....)");
                stage = 3;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Groooooooooan.....", "(Sleepy.....)");
                stage = 4;
                break;
            case 4:
                npc(FaceAnim.CHILD_NORMAL, "Grooooooan.....creeeeeeeak", "(Restful.....)");
                stage = 4;
                break;
            case 5:
                npc(FaceAnim.CHILD_NORMAL, "Grrrrooooooooooooooan.....", "(Achey.....)");
                stage = 4;
                break;
            case 6:
                npc(FaceAnim.CHILD_NORMAL, "Creeeeeeeegroooooooan.....", "(Goood.....)");
                stage = 4;
                break;
            case 7:
                npc(FaceAnim.CHILD_NORMAL, "Creeeeeeeeeeeeeaaaaaak.....", "(Tired.....)");
                stage = 4;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.ASKING, "Yes?");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, ".....");
                stage++;
                break;
            case 2:
                sendDialogue("After a while you realise that the ent has finished speaking for the moment.");
                stage = END_DIALOGUE;
                break;

            case 3:
                playerl(FaceAnim.ASKING, "Yes? We almost have a full sentence now - the suspense is killing me!");
                stage = 1;
                break;

            case 4:
                playerl(FaceAnim.ASKING, "I'm not sure if that was worth all the waiting.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.GIANT_ENT_6800, NPCs.GIANT_ENT_6801};
    }
}
