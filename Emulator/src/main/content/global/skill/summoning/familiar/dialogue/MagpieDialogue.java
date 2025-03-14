package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Magpie dialogue.
 */
@Initializable
public class MagpieDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new MagpieDialogue(player);
    }

    /**
     * Instantiates a new Magpie dialogue.
     */
    public MagpieDialogue() {}

    /**
     * Instantiates a new Magpie dialogue.
     *
     * @param player the player
     */
    public MagpieDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "There's nowt gannin on here...");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Howway, let's gaan see what's happenin' in toon.");
                stage = 2;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Are we gaan oot soon? I'm up fer a good walk me.");
                stage = 3;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Ye' been plowdin' i' the claarts aall day.");
                stage = 4;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "Err...sure? Maybe?");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.HALF_ASKING, "It seems upset, but what is it saying?");
                stage = END_DIALOGUE;
                break;
            case 2:
                playerl(FaceAnim.HALF_ASKING, "What? I can't understand what you're saying.");
                stage = END_DIALOGUE;
                break;
            case 3:
                playerl(FaceAnim.HALF_ASKING, "That...that was just noise. What does that mean?");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.HALF_ASKING, "What? That made no sense.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.MAGPIE_6824};
    }
}
