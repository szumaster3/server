package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Pack yak dialogue.
 */
@Initializable
public class PackYakDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new PackYakDialogue(player);
    }

    /**
     * Instantiates a new Pack yak dialogue.
     */
    public PackYakDialogue() {}

    /**
     * Instantiates a new Pack yak dialogue.
     *
     * @param player the player
     */
    public PackYakDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 3)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Barroobaroooo baaaaaaaaarooo...");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Baroo barrrooooo barooobaaaarrroooo...");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Barooo, barrrooooooooo...");
                stage = 10;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "(When I came of age, the herd pushed me out. It was always this way for an adult whose father still lived.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "I would have to wander across the Fremennik lands until I found a new herd to join, and I had to hope that the men whose paths I crossed wouldn't");
                stage++;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "decide that I might make a delicious meal. The trolls, too, were a concern, though they were easily avoided and yak-skin is thick and tough.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "So I set out on my journey, only 8 years old for a human, but already a man in yak society.)");
                stage++;
                break;
            case 4:
                npcl(FaceAnim.CHILD_NORMAL, "Barrrrooooo. (I'd forgotten how depressing my life is...)");
                stage = END_DIALOGUE;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "(When I was an adolescent, wandering the fields and slopes of my homeland, I knew that I was destined for greatness.");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Though I was merely a yak, and though all yaks dream of far-off lands and grand adventures, I knew my horns to be sharper than most and my tongue more agile;");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "I was better able to cast my lowing voice across the chasms that separated me from the other yaks, and my voice became a song to the yaks that remained in herds.");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "I, unlike all the others of my kind, was not interested in rejoining the society - in my exile I had come to love solitude and the friendly sounds between the spaces");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "of silence that the mountains whispered.)");
                stage = END_DIALOGUE;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "(I was born on a miserable night in Bennath. My mother, bless her soul, died in labour and was eaten by the Fremennik");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "who tended our herd. My father was never about, as he spent much of his time showing the adult females how much he could carry, for how long and");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "how quickly he could traverse a mountain pass. He was a foolish yak and was laughed at by the herd, though he had no inkling of that.");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "With no obvious father to raise me, I was left very much to my own devices and relied on the generosity of others to make my way through infancy and childhood.");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "I was forced to be cunning, wise and, sometimes, ruthless.)");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "Baroooooo. (I don't know what it is to be ruth, though. It is a silly word)");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] { NPCs.PACK_YAK_6873, NPCs.PACK_YAK_6874 };
    }
}
