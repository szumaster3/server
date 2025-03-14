package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.hasHandsFree;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Ravenous locust dialogue.
 */
@Initializable
public class RavenousLocustDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new RavenousLocustDialogue(player);
    }

    /**
     * Instantiates a new Ravenous locust dialogue.
     */
    public RavenousLocustDialogue() {}

    /**
     * Instantiates a new Ravenous locust dialogue.
     *
     * @param player the player
     */
    public RavenousLocustDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (hasHandsFree(player)) {
            npcl(FaceAnim.CHILD_NORMAL, "Clatter click chitter click? (Wouldn't you learn focus better if you used chopsticks?)");
            stage = 0;
            return true;
        }
        switch ((int) (Math.random() * 3)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Chitterchitter chirrup clatter. (Today, grasshopper, I will teach you to walk on rice paper.)");
                stage = 5;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Clatter chirrup chirp chirrup clatter clatter. (A wise man once said; 'Feed your mantis and it will be happy'.)");
                stage = 9;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Clatter chirrupchirp- (Today, grasshopper, we will-)");
                stage = 11;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Yes, almost every day.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Chirrupchirrup chirrup. ('Almost' is not good enough.)");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Well, I'm trying as hard as I can.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Chirrup chitter chitter chirrup? (How do you expect to achieve enlightenment at this rate, grasshopper?)");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Spontaneously.");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.HALF_ASKING, "What if I can't find any?");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Clatter chitter click chitter... (Then we will wander about and punch monsters in the head...)");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.HALF_ASKING, "I could do in an enlightened way if you want?");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "Chirrupchitter! (That will do!)");
                stage = END_DIALOGUE;
                break;
            case 9:
                playerl(FaceAnim.HALF_ASKING, "Is there any point to that saying?");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "Clatter chirrupchirrup chirp. (I find that a happy mantis is its own point.)");
                stage = END_DIALOGUE;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "You know, I'd rather you call me something other than grasshopper.");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "Clitterchirp? (Is there a reason for this?)");
                stage++;
                break;
            case 13:
                playerl(FaceAnim.FRIENDLY, "You drool when you say it.");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "Clickclatter! Chirrup chirpchirp click chitter... (I do not! Why would I drool when I call you a juicy...)");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "...clickclick chitter clickchitter click... (...succulent, nourishing, crunchy...)");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.CHILD_NORMAL, "*Drooool*");
                stage++;
                break;
            case 17:
                playerl(FaceAnim.FRIENDLY, "You're doing it again!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.RAVENOUS_LOCUST_7372, NPCs.RAVENOUS_LOCUST_7373 };
    }
}
