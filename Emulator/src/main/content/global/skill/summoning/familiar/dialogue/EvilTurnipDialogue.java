package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Evil turnip dialogue.
 */
@Initializable
public class EvilTurnipDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new EvilTurnipDialogue(player);
    }

    /**
     * Instantiates a new Evil turnip dialogue.
     */
    public EvilTurnipDialogue() {}

    /**
     * Instantiates a new Evil turnip dialogue.
     *
     * @param player the player
     */
    public EvilTurnipDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                playerl(FaceAnim.HALF_ASKING, "So, how are you feeling?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.OLD_NORMAL, "Hur hur hur...");
                stage = 3;
                break;
            case 2:
                npcl(FaceAnim.OLD_NORMAL, "When we gonna fighting things, boss?");
                stage = 4;
                break;
            case 3:
                npcl(FaceAnim.OLD_NORMAL, "I are turnip hear me roar! I too deadly to ignore.");
                stage = 6;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npcl(FaceAnim.OLD_NORMAL, "My roots feel hurty. I thinking it be someone I eated.");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.ASKING, "You mean some THING you ate?");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.OLD_NORMAL, "Hur hur hur. Yah, sure, why not.");
                stage = END_DIALOGUE;
                break;

            case 3:
                playerl(FaceAnim.FRIENDLY, "Well, as sinister as it's chuckling is, at least it's happy. That's a good thing, right?");
                stage = END_DIALOGUE;
                break;

            case 4:
                playerl(FaceAnim.FRIENDLY, "Soon enough.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.OLD_NORMAL, "Hur hur hur. I gets the fighting.");
                stage = END_DIALOGUE;
                break;

            case 6:
                playerl(FaceAnim.FRIENDLY, "I'm glad it's on my side... and not behind me.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.EVIL_TURNIP_6833, NPCs.EVIL_TURNIP_6834};
    }
}
