package core.game.dialogue;

import core.game.component.Component;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;
import core.plugin.PluginManifest;
import core.plugin.PluginType;
import core.tools.Log;
import shared.consts.Components;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static core.api.ContentAPIKt.log;
import static core.game.dialogue.DialogueUtilsKt.splitLines;
import static core.tools.DialogueHelperKt.DIALOGUE_INITIAL_OPTIONS_HANDLE;
import static core.tools.DialogueHelperKt.START_DIALOGUE;

/**
 * Handles player dialogues.
 * <p>
 * Registered as a {@link Plugin} of type {@link PluginType#DIALOGUE}.
 */
@PluginManifest(type = PluginType.DIALOGUE)
public abstract class Dialogue implements Plugin<Player> {

    protected static final String RED = "<col=8A0808>";
    protected static final String BLUE = "<col=08088A>";

    protected Player player;
    protected DialogueInterpreter interpreter;
    public DialogueFile file;

    protected ArrayList<String> optionNames = new ArrayList<String>(10);
    protected ArrayList<DialogueFile> optionFiles = new ArrayList<DialogueFile>(10);

    protected final int TWO_OPTIONS = Components.MULTI2_228;
    protected final int THREE_OPTIONS = Components.MULTI3_230;
    protected final int FOUR_OPTIONS = Components.MULTI4_232;
    protected final int FIVE_OPTIONS = Components.MULTI5_234;

    protected NPC npc;
    public int stage;
    protected boolean finished;

    /**
     * Default constructor.
     */
    public Dialogue() {
    }

    /**
     * Constructs a Dialogue for the given player.
     *
     * @param player the player this dialogue is for
     */
    public Dialogue(Player player) {
        this.player = player;
        if (player != null) {
            this.interpreter = player.getDialogueInterpreter();
        }
    }

    /**
     * Registers this dialogue with the interpreter for all associated dialogue ids.
     */
    public void init() {
        for (int id : getIds()) {
            DialogueInterpreter.add(id, this);
        }
    }

    /**
     * Closes the dialogue, resets interfaces and marks as finished.
     *
     * @return true when closed
     */
    public boolean close() {
        player.getInterfaceManager().closeChatbox();
        player.getInterfaceManager().openChatbox(Components.CHATDEFAULT_137);
        if (file != null) file.end();
        finished = true;
        return true;
    }

    /**
     * Sends a normal dialogue line from the given entity with optional expression.
     *
     * @param entity     speaker (player or NPC)
     * @param expression facial animation
     * @param messages   dialogue lines
     */
    public void sendNormalDialogue(Entity entity, FaceAnim expression, String... messages) {
        interpreter.sendDialogues(entity, expression, messages);
    }

    /**
     * Increments the dialogue stage.
     */
    public void increment() {
        stage++;
    }

    /**
     * Returns current stage and increments it.
     *
     * @return current stage before increment
     */
    public int getAndIncrement() {
        return stage++;
    }

    /**
     * Ends the dialogue by closing the interpreter.
     */
    public void end() {
        if (interpreter != null) {
            interpreter.close();
        }
    }

    /**
     * Marks the dialogue as finished by setting stage to -1.
     */
    public void finish() {
        setStage(-1);
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Dialogue newInstance(Player player) {
        try {
            Class<?> classReference = Class.forName(this.getClass().getCanonicalName());
            return (Dialogue) classReference.getDeclaredConstructor(Player.class).newInstance(player);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException |
                 InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Opens the dialogue, optionally initializing NPC.
     *
     * @param args optional arguments; first arg may be NPC
     * @return true if opened successfully
     */
    public boolean open(Object... args) {
        player.getDialogueInterpreter().activeTopics.clear();
        if (args.length > 0 && args[0] instanceof NPC) {
            npc = (NPC) args[0];
        }
        if (npc == null) {
            log(this.getClass(), Log.WARN, args[0].getClass().getSimpleName() + " is not assigning an NPC.");
        }
        player.getDialogueInterpreter().handle(0, 0);
        return true;
    }

    /**
     * Handles dialogue interface button clicks.
     *
     * @param interfaceId interface id clicked
     * @param buttonId    button id clicked
     * @return true if handled
     */
    public abstract boolean handle(int interfaceId, int buttonId);

    /**
     * Returns the npc ids this dialogue handles.
     *
     * @return array of npc ids
     */
    public abstract int[] getIds();

    /**
     * Sends NPC dialogue with default expression.
     *
     * @param messages dialogue lines
     * @return dialogue component
     */
    public Component npc(final String... messages) {
        if (npc == null) {
            return interpreter.sendDialogues(getIds()[0], getIds()[0] > 8591 ? FaceAnim.OLD_NORMAL : FaceAnim.FRIENDLY, messages);
        }
        return interpreter.sendDialogues(npc, npc.getId() > 8591 ? FaceAnim.OLD_NORMAL : FaceAnim.FRIENDLY, messages);
    }

    /**
     * Sends NPC dialogue with given NPC id.
     *
     * @param id       NPC id
     * @param messages dialogue lines
     * @return dialogue component
     */
    public Component npc(int id, final String... messages) {
        return interpreter.sendDialogues(id, FaceAnim.FRIENDLY, messages);
    }

    /**
     * Sends generic dialogue lines.
     *
     * @param messages dialogue lines
     * @return dialogue component
     */
    public Component sendDialogue(String... messages) {
        return interpreter.sendDialogue(messages);
    }

    /**
     * Sends NPC dialogue with specified expression.
     *
     * @param expression facial animation
     * @param messages   dialogue lines
     * @return dialogue component
     */
    public Component npc(FaceAnim expression, final String... messages) {
        if (npc == null) {
            return interpreter.sendDialogues(getIds()[0], expression, messages);
        }
        return interpreter.sendDialogues(npc, expression, messages);
    }

    /**
     * Sends player dialogue lines without expression.
     *
     * @param messages dialogue lines
     * @return dialogue component
     */
    public Component player(final String... messages) {
        return interpreter.sendDialogues(player, null, messages);
    }

    /**
     * Sends player dialogue lines with expression.
     *
     * @param expression facial animation
     * @param messages   dialogue lines
     * @return dialogue component
     */
    public Component player(FaceAnim expression, final String... messages) {
        return interpreter.sendDialogues(player, expression, messages);
    }

    /**
     * Sends dialogue options to the player.
     *
     * @param options option texts
     */
    public void options(final String... options) {
        interpreter.sendOptions("Select an Option", options);
    }

    /**
     * Checks if dialogue has finished.
     *
     * @return true if finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Returns the player associated with this dialogue.
     *
     * @return player instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the current dialogue stage.
     *
     * @param i new stage value
     */
    public void setStage(int i) {
        this.stage = i;
    }

    /**
     * Advances to the next dialogue stage.
     */
    public void next() {
        this.stage += 1;
    }

    /**
     * Loads a dialogue file to start a new dialogue section.
     *
     * @param file dialogue file to load
     */
    public void loadFile(DialogueFile file) {
        if (file == null) return;
        this.file = file.load(player, npc, interpreter);
        this.file.setDialogue(this);
        stage = START_DIALOGUE;
    }

    /**
     * Adds an option with its associated dialogue file.
     *
     * @param name option name
     * @param file dialogue file to load on selection
     */
    public void addOption(String name, DialogueFile file) {
        optionNames.add("Talk about " + name);
        optionFiles.add(file);
    }

    /**
     * Sends dialogue choices or loads dialogue directly depending on option count.
     *
     * @return true if options were sent, false if dialogue was loaded
     */
    public boolean sendChoices() {
        if (optionNames.size() == 1) {
            loadFile(optionFiles.get(0));
            return false;
        } else if (optionNames.isEmpty()) {
            stage = START_DIALOGUE;
            return false;
        } else {
            options(optionNames.toArray(new String[0]));
            stage = DIALOGUE_INITIAL_OPTIONS_HANDLE;
            return true;
        }
    }

    /**
     * Sends NPC dialogue with expression, splitting long lines.
     *
     * @param expr facial animation
     * @param msg  dialogue text
     * @return dialogue component
     */
    public Component npcl(FaceAnim expr, String msg) {
        return npc(expr, splitLines(msg, 54));
    }

    /**
     * Sends player dialogue with expression, splitting long lines.
     *
     * @param expr facial animation
     * @param msg  dialogue text
     * @return dialogue component
     */
    public Component playerl(FaceAnim expr, String msg) {
        return player(expr, splitLines(msg, 54));
    }

    /**
     * Displays selectable topics in dialogue.
     *
     * @param topics dialogue topics to show
     * @return true if no valid topics were shown, false otherwise
     */
    public boolean showTopics(Topic<?>... topics) {
        ArrayList<String> validTopics = new ArrayList<>();
        for (Topic<?> topic : topics) {
            if (topic instanceof IfTopic && !((IfTopic<?>) topic).getShowCondition()) continue;
            interpreter.activeTopics.add(topic);
            validTopics.add(topic.getText());
        }
        if (validTopics.size() == 0) {
            return true;
        } else if (validTopics.size() == 1) {
            Topic topic = interpreter.activeTopics.get(0);
            if (topic.getToStage() instanceof DialogueFile) {
                DialogueFile topicFile = (DialogueFile) topic.getToStage();
                interpreter.getDialogue().loadFile(topicFile);
            } else if (topic.getToStage() instanceof Integer) {
                stage = (Integer) topic.getToStage();
            }
            player(topic.getText());
            interpreter.activeTopics.clear();
            return false;
        } else {
            options(validTopics.toArray(new String[0]));
            return false;
        }
    }
}
