package content.global.skill.summoning.familiar.dialogue.spirit;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit jelly dialogue.
 */
@Initializable
public class SpiritJellyDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritJellyDialogue(player);
    }

    /**
     * Instantiates a new Spirit jelly dialogue.
     */
    public SpiritJellyDialogue() {}

    /**
     * Instantiates a new Spirit jelly dialogue.
     *
     * @param player the player
     */
    public SpiritJellyDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.OLD_NORMAL, "Play play play play!");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.OLD_NORMAL, "It's playtime now!");
                stage = 4;
                break;
            case 2:
                npcl(FaceAnim.OLD_NORMAL, "Can we go over there now, pleasepleasepleasepleeeeeease?");
                stage = 8;
                break;
            case 3:
                npcl(FaceAnim.OLD_NORMAL, "What game are we playing now?");
                stage = 14;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "The only game I have time to play is the 'Staying Very Still' game.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.OLD_NORMAL, "But that game is soooooo booooooring...");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "How about we use the extra house rule, that makes it the 'Staying Very Still and Very Quiet' game.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.OLD_NORMAL, "Happy happy! I love new games!");
                stage = END_DIALOGUE;
                break;

            case 4:
                playerl(FaceAnim.FRIENDLY, "Okay, how about we play the 'Staying Very Still' game.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.OLD_NORMAL, "But that game is booooooring...");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "If you win then you can pick the next game, how about that?");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.OLD_NORMAL, "Happy happy!");
                stage = END_DIALOGUE;
                break;

            case 8:
                playerl(FaceAnim.FRIENDLY, "Go over where?");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.OLD_NORMAL, "I dunno, someplace fun, pleasepleaseplease!");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "Okay, but first, let's play the 'Sitting Very Still' game.");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.OLD_NORMAL, "But that game is booooooring...");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "Well, if you win we can go somewhere else, okay?");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.OLD_NORMAL, "Happy happy!");
                stage = END_DIALOGUE;
                break;

            case 14:
                playerl(FaceAnim.FRIENDLY, "It's called the 'Staying Very Still' game.");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.OLD_NORMAL, "This game is booooooring...");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "Hey, all that moping doesn't look very still to me.");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.OLD_NORMAL, "I never win at this game...");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "You know what? I think I'll not count it this one time");
                stage++;
                break;
            case 19:
                npcl(FaceAnim.OLD_NORMAL, "Happy happy! You're the best friend ever!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] {NPCs.SPIRIT_JELLY_6992, NPCs.SPIRIT_JELLY_6993};
    }
}
