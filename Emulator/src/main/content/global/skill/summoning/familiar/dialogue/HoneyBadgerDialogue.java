package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Honey badger dialogue.
 */
@Initializable
public class HoneyBadgerDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new HoneyBadgerDialogue(player);
    }

    /**
     * Instantiates a new Honey badger dialogue.
     */
    public HoneyBadgerDialogue() {}

    /**
     * Instantiates a new Honey badger dialogue.
     *
     * @param player the player
     */
    public HoneyBadgerDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 5)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "An outpouring of sanity-straining abuse*");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "An outpouring of spittal-flecked insults.*");
                stage = 0;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "A lambasting of visibly illustrated obscenities.*");
                stage = 0;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "A tirade of biologically questionable threats*");
                stage = 0;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "A stream of eye-watering crudities*");
                stage = 0;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        if (stage == 0) {
            playerl(FaceAnim.FRIENDLY, "Why do I talk to you again?");
            stage = END_DIALOGUE;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.HONEY_BADGER_6845, NPCs.HONEY_BADGER_6846};
    }
}
