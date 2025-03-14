package content.global.skill.summoning.familiar.dialogue.spirit;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit dagannoth dialogue.
 */
@Initializable
public class SpiritDagannothDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritDagannothDialogue(player);
    }

    /**
     * Instantiates a new Spirit dagannoth dialogue.
     */
    public SpiritDagannothDialogue() {}

    /**
     * Instantiates a new Spirit dagannoth dialogue.
     *
     * @param player the player
     */
    public SpiritDagannothDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        int randomIndex = (int) (Math.random() * 4);
        switch (randomIndex) {
            case 0:
                npc(FaceAnim.CHILD_NORMAL,
                    "Grooooooowl graaaaawl raaaawl?",
                    "(Are you ready to surrender to the power of",
                    "the Deep Waters?)");
                stage = 0;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL,
                    "Groooooowl. Hsssssssssssssss!",
                    "(The Deeps will swallow the lands. None will",
                    "stand before us!)");
                stage = 5;
                break;
            case 2:
                npc(FaceAnim.CHILD_NORMAL,
                    "Hssssss graaaawl grooooowl, growwwwwwwwwl!",
                    "(Oh how the bleak gulfs hunger for the",
                    "Day of Rising.)");
                stage = 8;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Raaaawl!", "(Submit!)");
                stage = 11;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Err, not really.");
                stage++;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Rooooowl?", "(How about now?)");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "No, sorry.");
                stage++;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Rooooowl?", "(How about now?)");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "No, sorry. You might want to try again a little later.");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "What if we build boats?");
                stage++;
                break;
            case 6:
                npc(FaceAnim.CHILD_NORMAL,
                    "Hsssssssss groooooowl?",
                    "Hssssshsss grrooooooowl?",
                    "(What are boats? The tasty wooden containers full of meat?)");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "I suppose they could be described as such, yes.");
                stage = END_DIALOGUE;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "My brain hurts when I listen to you talk...");
                stage++;
                break;
            case 9:
                npc(FaceAnim.CHILD_NORMAL,
                    "Raaaaawl groooowl grrrrawl!",
                    "(That's the truth biting into your clouded mind!)");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "Could you try using a little less truth please?");
                stage = END_DIALOGUE;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "Submit to what?");
                stage++;
                break;
            case 12:
                npc(FaceAnim.CHILD_NORMAL,
                    "Hssssssssss rawwwwwl graaaawl!",
                    "(To the inevitable defeat of all life on the Surface!)");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY,
                    "I think I'll wait a little longer before I just keep over and submit, thanks.");
                stage++;
                break;
            case 14:
                npc(FaceAnim.CHILD_NORMAL,
                    "Hsssss, grooooowl, raaaaawl.",
                    "(Well, it's your choice, but those that submit first will be eaten first.)");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "I'll pass on that one, thanks.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.SPIRIT_DAGANNOTH_6804, NPCs.SPIRIT_DAGANNOTH_6805};
    }
}
