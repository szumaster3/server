package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Talon beast dialogue.
 */
@Initializable
public class TalonBeastDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new TalonBeastDialogue(player);
    }

    /**
     * Instantiates a new Talon beast dialogue.
     */
    public TalonBeastDialogue() {}

    /**
     * Instantiates a new Talon beast dialogue.
     *
     * @param player the player
     */
    public TalonBeastDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        int randomChoice = (int) (Math.random() * 4);
        switch (randomChoice) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Is this all you apes do all day, then?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "This place smells odd...");
                stage = 4;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Hey!");
                stage = 7;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "C'mon! Lets go fight stuff!");
                stage = 11;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Well, we do a lot of other things, too.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "That's dull. Lets go find something and bite it.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "I wouldn't want to spoil my dinner.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "So, I have to watch you trudge about again? Talk about boring.");
                stage = END_DIALOGUE;
                break;

            case 4:
                playerl(FaceAnim.HALF_ASKING, "Odd?");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "Yes, not enough is rotting...");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "For which I am extremely grateful.");
                stage = END_DIALOGUE;
                break;

            case 7:
                playerl(FaceAnim.FRIENDLY, "Aaaargh!");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "Why d'you always do that?");
                stage++;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "I don't think I'll ever get used to having a huge, ravenous feline sneaking around behind me all the time.");
                stage++;
                break;

            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "That's okay, I doubt I'll get used to following an edible, furless monkey prancing in front of me all the time either.");
                stage = END_DIALOGUE;
                break;

            case 11:
                playerl(FaceAnim.ASKING, "What sort of stuff?");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "I dunno? Giants, monsters, vaguely-defined philosophical concepts. You know: stuff.");
                stage++;
                break;

            case 13:
                playerl(FaceAnim.ASKING, "How are we supposed to fight a philosophical concept?");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "With subtle arguments and pointy sticks!");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "Well, I can see you're going to go far in debates.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.TALON_BEAST_7347, NPCs.TALON_BEAST_7348 };
    }
}
