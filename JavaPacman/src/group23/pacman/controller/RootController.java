package group23.pacman.controller;

import ui.UIViewController;

/**
 * Created By Tony on 12/12/2018
 */
public abstract class RootController extends UIViewController {
    /**
     * Create a UIViewController instance from any fxml file.
     *
     * @param path The path for the fxml.
     */
    public RootController(String path) {
        super(path);
    }

    /**
     * Called when the controller is brought to the main view.
     */
    public void didBecomeActive(){ }

    /**
     * Called when the controller is in the background.
     */
    public void didEnterBackground() { }
}
