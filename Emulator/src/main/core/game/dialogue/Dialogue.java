package core.game.dialogue;

import core.game.component.Component;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;
import core.plugin.PluginManifest;
import core.plugin.PluginType;
import core.tools.Log;
import org.rs.consts.Components;

import java.util.ArrayList;

import static core.api.ContentAPIKt.log;
import static core.game.dialogue.DialogueUtilsKt.splitLines;
import static core.tools.DialogueHelperKt.DIALOGUE_INITIAL_OPTIONS_HANDLE;
import static core.tools.DialogueHelperKt.START_DIALOGUE;

/**
 * The type Dialogue.
 */
@PluginManifest(type = PluginType.DIALOGUE)
public abstract class Dialogue implements Plugin<Player> {
    /**
     * The constant RED.
     */
    protected static final String RED = "<col=8A0808>";
    /**
     * The constant BLUE.
     */
    protected static final String BLUE = "<col=08088A>";
    /**
     * The Player.
     */
    protected Player player;
    /**
     * The Interpreter.
     */
    protected DialogueInterpreter interpreter;
    /**
     * The File.
     */
    public DialogueFile file;
    /**
     * The Option names.
     */
    protected ArrayList<String> optionNames = new ArrayList<>(10);
    /**
     * The Option files.
     */
    protected ArrayList<DialogueFile> optionFiles = new ArrayList<>(10);

    /**
     * The Two options.
     */
    protected final int TWO_OPTIONS = Components.MULTI2_228;
    /**
     * The Three options.
     */
    protected final int THREE_OPTIONS = Components.MULTI3_230;
    /**
     * The Four options.
     */
    protected final int FOUR_OPTIONS = Components.MULTI4_232;
    /**
     * The Five options.
     */
    protected final int FIVE_OPTIONS = Components.MULTI5_234;

    /**
     * The Npc.
     */
    protected NPC npc;
    /**
     * The Stage.
     */
    public int stage;
    /**
     * The Finished.
     */
    protected boolean finished;

    /**
     * Instantiates a new Dialogue.
     */
    public Dialogue() {
        //
    }

    /**
     * Instantiates a new Dialogue.
     *
     * @param player the player
     */
    public Dialogue(Player player) {
        this.player = player;
        if (player != null) {
            this.interpreter = player.getDialogueInterpreter();
        }
    }

    /**
     * Init.
     */
    public void init() {
        for (int id : getIds()) {
            DialogueInterpreter.add(id, this);
        }
    }

    /**
     * Close boolean.
     *
     * @return the boolean
     */
    public boolean close() {
        player.getInterfaceManager().closeChatbox();
        player.getInterfaceManager().openChatbox(Components.CHATDEFAULT_137);
        if (file != null) file.end();
        finished = true;
        return true;
    }

    /**
     * Send normal dialogue.
     *
     * @param entity     the entity
     * @param expression the expression
     * @param messages   the messages
     */
    public void sendNormalDialogue(Entity entity, FaceAnim expression, String... messages) {
        interpreter.sendDialogues(entity, expression, messages);
    }

    /**
     * Increment.
     */
    public void increment() {
        stage++;
    }

    /**
     * Gets and increment.
     *
     * @return the and increment
     */
    public int getAndIncrement() {
        return stage++;
    }

    /**
     * End.
     */
    public void end() {
        if (interpreter != null) {
            interpreter.close();
        }
    }

    /**
     * Finish.
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
            return (Dialogue) Class.forName(this.getClass().getCanonicalName())
                .getDeclaredConstructor(Player.class)
                .newInstance(player);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Open boolean.
     *
     * @param args the args
     * @return the boolean
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
     * Handle boolean.
     *
     * @param interfaceId the interface id
     * @param buttonId    the button id
     * @return the boolean
     */
    public abstract boolean handle(int interfaceId, int buttonId);

    /**
     * Get ids int [ ].
     *
     * @return the int [ ]
     */
    public abstract int[] getIds();

    /**
     * Npc component.
     *
     * @param messages the messages
     * @return the component
     */
    public Component npc(final String... messages) {
        return npc == null
            ? interpreter.sendDialogues(getIds()[0], getIds()[0] > 8591 ? FaceAnim.OLD_NORMAL : FaceAnim.FRIENDLY, messages)
            : interpreter.sendDialogues(npc, npc.getId() > 8591 ? FaceAnim.OLD_NORMAL : FaceAnim.FRIENDLY, messages);
    }

    /**
     * Npc component.
     *
     * @param id       the id
     * @param messages the messages
     * @return the component
     */
    public Component npc(int id, final String... messages) {
        return interpreter.sendDialogues(id, FaceAnim.FRIENDLY, messages);
    }

    /**
     * Send dialogue component.
     *
     * @param messages the messages
     * @return the component
     */
    public Component sendDialogue(String... messages) {
        return interpreter.sendDialogue(messages);
    }

    /**
     * Npc component.
     *
     * @param expression the expression
     * @param messages   the messages
     * @return the component
     */
    public Component npc(FaceAnim expression, final String... messages) {
        return npc == null
            ? interpreter.sendDialogues(getIds()[0], expression, messages)
            : interpreter.sendDialogues(npc, expression, messages);
    }

    /**
     * Player component.
     *
     * @param messages the messages
     * @return the component
     */
    public Component player(final String... messages) {
        return interpreter.sendDialogues(player, null, messages);
    }

    /**
     * Player component.
     *
     * @param expression the expression
     * @param messages   the messages
     * @return the component
     */
    public Component player(FaceAnim expression, final String... messages) {
        return interpreter.sendDialogues(player, expression, messages);
    }

    /**
     * Options.
     *
     * @param options the options
     */
    public void options(final String... options) {
        interpreter.sendOptions("Select an Option", options);
    }

    /**
     * Is finished boolean.
     *
     * @return the boolean
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets stage.
     *
     * @param i the
     */
    public void setStage(int i) {
        this.stage = i;
    }

    /**
     * Next.
     */
    public void next() {
        this.stage++;
    }

    /**
     * Load file.
     *
     * @param file the file
     */
    public void loadFile(DialogueFile file) {
        if (file != null) {
            this.file = file.load(player, npc, interpreter);
            this.file.setDialogue(this);
            stage = START_DIALOGUE;
        }
    }

    /**
     * Add option.
     *
     * @param name the name
     * @param file the file
     */
    public void addOption(String name, DialogueFile file) {
        optionNames.add("Talk about " + name);
        optionFiles.add(file);
    }

    /**
     * Send choices boolean.
     *
     * @return the boolean
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
     * Npcl component.
     *
     * @param expr the expr
     * @param msg  the msg
     * @return the component
     */
    public Component npcl(FaceAnim expr, String msg) {
        return npc(expr, splitLines(msg, 54));
    }

    /**
     * Playerl component.
     *
     * @param expr the expr
     * @param msg  the msg
     * @return the component
     */
    public Component playerl(FaceAnim expr, String msg) {
        return player(expr, splitLines(msg, 54));
    }

    /**
     * Show topics boolean.
     *
     * @param topics the topics
     * @return the boolean
     */
    public boolean showTopics(Topic<?>... topics) {
        ArrayList<String> validTopics = new ArrayList<>();
        for (Topic<?> topic : topics) {
            if (topic instanceof IfTopic && !((IfTopic<?>) topic).getShowCondition()) continue;
            interpreter.activeTopics.add(topic);
            validTopics.add(topic.getText());
        }

        if (validTopics.isEmpty()) {
            return true;
        } else if (validTopics.size() == 1) {
            Topic<?> topic = interpreter.activeTopics.get(0);
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
