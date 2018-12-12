package group23.pacman.controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By Tony on 12/12/2018
 */
public final class JoystickManager implements EventHandler<KeyEvent>{

    /**
     * The main instance
     */
    public static final JoystickManager shared = new JoystickManager();

    private JoystickManager(){ }

    // Key bindings, KeyCode -> Collection of Joysticks -> Inner Binding
    private Map<KeyCode,Map<Integer,Key>> key_mappings = new HashMap<>();

    // Event listeners (subscribers)
    private Map<String,JoystickListener> subscribers = new HashMap<>();

    @Override
    public void handle(KeyEvent event) {

        // get all joysticks that are listening to this key
        Map<Integer,Key> keyMap = key_mappings.get(event.getCode());

        if(keyMap != null){
            // for each joystick, invoke the subscribers
            keyMap.forEach((integer, key)
                    -> subscribers.values().forEach(joystickListenerWeakReference
                        -> joystickListenerWeakReference.onJoystickTriggered(integer,key)));
        }
    }

    /**
     * Register a new joystick controller.
     * @param up The up key binding.
     * @param down The down key binding.
     * @param left The left key binding.
     * @param right The right key binding.
     * @param primary The primary key binding.
     * @param secondary The secondary key binding.
     *
     * @return A joystick identifier.
     */
    public int register(KeyCode up, KeyCode down, KeyCode left, KeyCode right, KeyCode primary, KeyCode secondary){
        // create instance
        Joystick joystick = new Joystick(up, down, left,right,primary,secondary);

        KeyCode[] allcodes = joystick.getKeys();
        Map<KeyCode,Key> innerMapping = joystick.getKeyBindings();

        for (KeyCode code: allcodes) {
            Map<Integer,Key> keyMap = key_mappings.get(code);
            if(keyMap == null) {
                keyMap = new HashMap<>();
            }
            Key k = innerMapping.get(code);
            keyMap.put(joystick.id,k);

            key_mappings.put(code,keyMap);
        }

        return joystick.id;
    }

    /**
     * Use this method to register an event handler.
     * @param id The id.
     * @param listener The listener
     */
    public void subscribe(String id, JoystickListener listener) {
        subscribers.put(id,listener);
    }

    /**
     * Use this method to stop listening to events.
     * @param id the id that was used to register.
     */
    public void unsubscribe(String id) {
        subscribers.remove(id);
    }


    public interface JoystickListener {
        void onJoystickTriggered(int joystickId,Key selectedKey);
    }

    public enum Key{
        UP,DOWN,LEFT,RIGHT, ONE, TWO
    }

    private static class Joystick {
        private static int id_counter = 1;

        private int id;
        private KeyCode up;
        private KeyCode down;
        private KeyCode left;
        private KeyCode right;
        private KeyCode primary;
        private KeyCode secondary;

        Joystick(KeyCode up, KeyCode down, KeyCode left, KeyCode right, KeyCode primary, KeyCode secondary) {
            this.up = up;
            this.down = down;
            this.left = left;
            this.right = right;
            this.primary = primary;
            this.secondary = secondary;
            this.id = id_counter++;
        }

        Map<KeyCode,Key> getKeyBindings(){
            Map<KeyCode,Key> m = new HashMap<>();
            m.put(up,Key.UP);
            m.put(down,Key.DOWN);
            m.put(left,Key.LEFT);
            m.put(right,Key.RIGHT);
            m.put(secondary,Key.TWO);
            m.put(primary,Key.ONE);
            return m;
        }

        KeyCode[] getKeys() {
            return new KeyCode[]{
                    up,
                    down,
                    left,
                    right,
                    primary,
                    secondary
            };
        }
    }
}
