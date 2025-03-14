package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type War tortoise dialogue.
 */
@Initializable
public class WarTortoiseDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new WarTortoiseDialogue(player);
    }

    /**
     * Instantiates a new War tortoise dialogue.
     */
    public WarTortoiseDialogue() {}

    /**
     * Instantiates a new War tortoise dialogue.
     *
     * @param player the player
     */
    public WarTortoiseDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        int randomChoice = (int) (Math.random() * 4);
        switch (randomChoice) {
            case 0:
                npc(FaceAnim.OLD_NORMAL, "*The tortoise waggles its head about.*", "What are we doing in this dump?");
                stage = 0;
                break;
            case 1:
                npc(FaceAnim.OLD_NORMAL, "Hold up a minute, there.");
                stage = 4;
                break;
            case 2:
                npc(FaceAnim.OLD_NORMAL, "*The tortoise bobs its head around energetically.*", "Oh, so now you're paying attention to", "me, are you?");
                stage = 10;
                break;
            case 3:
                npc(FaceAnim.OLD_NORMAL, "*The tortoise exudes an air of reproach.*", "Are you going to keep rushing", "around all day?");
                stage = 16;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Well, I was just going to take care of a few things.");
                stage++;
                break;
            case 1:
                npc(FaceAnim.OLD_NORMAL, "*The tortoise shakes its head.*", "I don't believe it. Stuck here with this young whippersnapper", "running around having fun.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "You know, I'm sure you would enjoy it if you gave it a chance.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.OLD_NORMAL, "Oh, you would say that, wouldn't you?");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "What do you want?");
                stage++;
                break;
            case 5:
                npc(FaceAnim.OLD_NORMAL, "*The tortoise bobs its head sadly.*", "For you to slow down!");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Well, I've stopped now.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.OLD_NORMAL, "Yes, but you'll soon start up again, won't you?");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Probably.");
                stage++;
                break;
            case 9:
                npc(FaceAnim.OLD_NORMAL, "*The tortoise waggles its head despondently.*", "I don't believe it....");
                stage = END_DIALOGUE;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "I pay you plenty of attention!");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.OLD_NORMAL, "Only when you want me to carry those heavy things of yours.");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "I don't ask you to carry anything heavy.");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.OLD_NORMAL, "What about those lead ingots?");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.HALF_ASKING, "What lead ingots?");
                stage++;
                break;
            case 15:
                npc(FaceAnim.OLD_NORMAL, "*The tortoise droops its head.*", "Well, that's what it felt like....", "*grumble grumble*");
                stage = END_DIALOGUE;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "Only for as long as I have the energy to.");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.OLD_NORMAL, "Oh. I'm glad that my not being able to keep up with you brings you such great amusement.");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "I didn't mean it like that.");
                stage++;
                break;
            case 19:
                npc(FaceAnim.OLD_NORMAL, "*The tortoise waggles its head disapprovingly.*", "Well, when you are QUITE finished laughing at my expense,", "how about you pick up a rock larger than your body", "and go crawling about with it?");
                stage++;
                break;
            case 20:
                npcl(FaceAnim.OLD_NORMAL, "We'll see how energetic you are after an hour or two of that.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.WAR_TORTOISE_6815, NPCs.WAR_TORTOISE_6816 };
    }
}
