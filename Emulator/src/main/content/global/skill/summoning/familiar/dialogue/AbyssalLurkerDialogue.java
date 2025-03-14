package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Abyssal lurker dialogue.
 */
@Initializable
public class AbyssalLurkerDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new AbyssalLurkerDialogue(player);
    }

    /**
     * Instantiates a new Abyssal lurker dialogue.
     */
    public AbyssalLurkerDialogue() {}

    /**
     * Instantiates a new Abyssal lurker dialogue.
     *
     * @param player the player
     */
    public AbyssalLurkerDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Djrej gf'ig sgshe...");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "To poshi v'kaa!");
                stage = 1;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "G-harrve shelmie?");
                stage = 2;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Jehifk i'ekfh skjd.");
                stage = 3;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "What? Are we in danger, or something?");
                stage = END_DIALOGUE;
                break;
            case 1:
                playerl(FaceAnim.HALF_ASKING, "What? Is that even a language?");
                stage = END_DIALOGUE;
                break;
            case 2:
                playerl(FaceAnim.HALF_ASKING, "What? Do you want something?");
                stage = END_DIALOGUE;
                break;
            case 3:
                playerl(FaceAnim.HALF_ASKING, "What? Is there somebody down an old well, or something?");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.ABYSSAL_LURKER_6820, NPCs.ABYSSAL_LURKER_6821};
    }
}
