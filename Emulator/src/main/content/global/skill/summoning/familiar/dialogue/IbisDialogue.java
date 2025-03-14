package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Ibis dialogue.
 */
@Initializable
public class IbisDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new IbisDialogue(player);
    }

    /**
     * Instantiates a new Ibis dialogue.
     */
    public IbisDialogue() {}

    /**
     * Instantiates a new Ibis dialogue.
     *
     * @param player the player
     */
    public IbisDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 5)) {
            case 0:
                npcl(FaceAnim.OLD_DEFAULT, "I'm the best fisherman ever!");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.OLD_DEFAULT, "I like to fish!");
                stage = 3;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "I want to go fiiiish.");
                stage = 4;
                break;
            case 3:
                npcl(FaceAnim.OLD_DEFAULT, "Hey, where are we?");
                stage = 5;
                break;
            case 4:
                npcl(FaceAnim.OLD_DEFAULT, "Can I look after those sharks for you?");
                stage = 8;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "Where is your skillcape to prove it, then?");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.OLD_DEFAULT, "At home...");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "I'll bet it is.");
                stage = END_DIALOGUE;
                break;
            case 3:
                playerl(FaceAnim.HAPPY, "I know!");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "We can't be fishing all the time you know.");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.HALF_ASKING, "What do you mean?");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.OLD_DEFAULT, "I just noticed we weren't fishing.");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "Well, we can't fish all the time.");
                stage = END_DIALOGUE;
                break;
            case 8:
                playerl(FaceAnim.HALF_ASKING, "I don't know. Would you eat them?");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.OLD_DEFAULT, "Yes! Ooops...");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "I think I'll hang onto them myself for now.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.IBIS_6991};
    }
}
