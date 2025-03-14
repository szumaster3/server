package core.plugin.type;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Managers.
 */
public class Managers {

    private static List<ManagerPlugin> plugins = new ArrayList<>(20);

    /**
     * Register.
     *
     * @param plugin the plugin
     */
    public static void register(ManagerPlugin plugin){
        if(plugin != null){
            plugins.add(plugin);
        }
    }

    /**
     * Tick.
     */
    public static void tick(){
        for(ManagerPlugin p : plugins){
            p.tick();
        }
    }
}
