package content.region.misthalin.draynor_village.quest.swept.dialogue;

import content.data.GameAttributes;
import content.region.misthalin.draynor_village.quests.swept.SweptAway;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.dialogue.Topic;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.getAttribute;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * Represents the Lottie dialogue.
 *
 * <p>Relations:
 * <ul>
 *   <li>{@link SweptAway Swept Away quest}</li>
 * </ul>
 */
@Initializable
public class LottieDialogue extends Dialogue {

    /**
     * Instantiates a new Lottie dialogue.
     */
    public LottieDialogue() {

    }

    /**
     * Instantiates a new Lottie dialogue.
     *
     * @param player the player
     */
    public LottieDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc(FaceAnim.NEUTRAL, "So, what do you want, then?");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                if(getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LOTTIE, false)) {

                }
                showTopics(
                        new Topic("Who are you and what are you doing in Betty's basement?", 20, false),
                        new Topic("I need to retrieve Betty's wand.", 1, true),
                        new Topic("What is this place?", 25, false),
                        new Topic("No need to be rude!", 29, false)
                );
                break;
            case 1:
                player("Betty sent me down here to fetch her wand, but she", "said there might be a small complication?");
                stage++;
                break;
            case 2:
                npc("I'll say there are complications! Betty has a penchant", "for intricacy.");
                stage++;
                break;
            case 3:
                npc("You're not going to be able to get that wand until you", "get that chest open.");
                stage++;
                break;
            case 4:
                npc("That sounds simple enough; what's so complicated about", "opening a little chest?");
                stage++;
                break;
            case 5:
                npc("Well, the chest in which Betty stores her wand is", "magically sealed. I won't confuse you with the details", "right now, but  here's the deal:");
                stage++;
                break;
            case 6:
                npc("That chest is simply not going to open until order has", "been restored to Betty's menagerie.");
                stage++;
                break;
            case 7:
                player("What do you mean? What menagerie?");
                stage++;
                break;
            case 8:
                npc("Well, if you have a look around, you'll see that there", "are six separate chambers, each of which is designed to", "house a little critter.");
                stage++;
                break;
            case 9:
                npc("Each creature has an enclosure that has been made", "especially for it, but, at the moment, several of them are", "out of place.");
                stage++;
                break;
            case 10:
                npc("So, what you're saying is that we need to put each", "creature in its proper enclosure in order to open the", "chest?");
                stage++;
                break;
            case 11:
                npc("Exactly. Now, I should warn you - these are very", "sensitive creatures and they really don't like each other.");
                stage++;
                break;
            case 12:
                player("What does that have to do with anything?");
                stage++;
                break;
            case 13:
                npc("Two things. First, you can't carry more than one", "creature at a time.");
                stage++;
                break;
            case 14:
                npc("Second, you can't carry a creature through the door of", "a chamber which contains another creature.");
                stage++;
                break;
            case 15:
                npc("You'll have to move the creatures one by one. You can", "use the holding pen in this room to help shift them all", "into the right position.");
                stage++;
                break;
            case 16:
                npc("One more thing I should mention: you shouldn't have", "to move the bat and the snail at all - they're already", "exactly where they need to be.");
                stage++;
                break;
            case 17:
                npc("Now, if you have any questions or want me to put the", "creatures back to the arrangement they're in now, just", "let me know.");
                stage++;
                break;
            case 18:
                npc("Here is a magic slate. It always shows an overview of", "exactly who is where. You can use it for reference, if", "you like. It's not necessary, but I find it helpful.");
                player.getInventory().add(new Item(Items.MAGIC_SLATE_14069, 1));
                stage++;
                break;
            case 19:
                player("Okay, thanks.");
                stage = END_DIALOGUE;
                break;
            case 20:
                npcl(FaceAnim.NEUTRAL, "My name is Lottie and it should be rather obvious what I'm doing here.");
                stage++;
                break;
            case 21:
                options("Are you the cleaning lady?", "Are you a wise and powerful witch, here to show Betty the ropes?");
                stage++;
                break;
            case 22:
                switch (buttonId) {
                    case 1:
                        npcl(FaceAnim.NEUTRAL, "I most certain am not! I'm learning the ways of witchcraft from Betty.");
                        stage++;
                        break;
                    case 2:
                        npcl(FaceAnim.NEUTRAL, "Oh, is my witchly prowess that obvious? How flattering.");
                        stage = 24;
                        break;
                }
            case 23:
                npcl(FaceAnim.NEUTRAL, "If you knew anything at all, you'd be able to tell from my witchly deportment, and aura of mysticism and enchantment that radiates from my every pore.");
                stage = 0;
                break;
            case 24:
                npcl(FaceAnim.NEUTRAL, "In actuality, it is I who is the apprentice, but I'm sure it is only a matter of time before I'm a fully licensed witch.");
                stage = 0;
                break;
            case 25:
                npcl(FaceAnim.NEUTRAL, "Well, if you'd use your eyes, you could tell it is – I know this is going to be difficult to believe – A basement.");
                stage++;
                break;
            case 26:
                playerl(FaceAnim.THINKING, "Oh, very helpful. I can see it's a basement, but what are these cages doing down here?");
                stage++;
                break;
            case 27:
                npcl(FaceAnim.NEUTRAL, "I see you don't know your witchcraft very well. This is Betty's menagerie. Many witches keep some creatures on hand for their spells and incantations.");
                stage++;
                break;
            case 28:
                npcl(FaceAnim.NEUTRAL, "Not to mention the company.");
                stage++;
                break;
            case 29:
                npcl(FaceAnim.NEUTRAL, "Sorry, it's just that I'm very busy.");
                stage++;
                break;
            case 30:
                npcl(FaceAnim.NEUTRAL, "I've lots of studying to do and all of these creatures to look after. It's not easy to keep track of everything and make casual conversation as well.");
                stage++;
                break;
        }
        return true;
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new LottieDialogue(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.LOTTIE_8206};
    }

}
