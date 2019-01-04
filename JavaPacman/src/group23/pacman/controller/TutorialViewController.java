package group23.pacman.controller;

import group23.pacman.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class TutorialViewController extends RootController implements JoystickManager.JoystickListener{

    /**
     * The image view which displays the slides
     */
    @FXML private ImageView imageView;

    /**
     * A label to show the current slide
     */
    @FXML private Label slideNumLabel;

    /**
     * Previous button.
     */
    @FXML private ToggleButton prev;

    /**
     * Next button
     */
    @FXML private ToggleButton next;

    /**
     * Back button.
     */
    @FXML private ToggleButton back;

    /**
     * Array which holds the slides
     * TODO: update slides here.
     */
    private Image[] slides = {
            new Image("assets/Pacman/Whip/left-whip1.png"),
            new Image("assets/Pacman/Whip/down-whip1.png"),
            new Image("assets/Pacman/Whip/up-whip1.png"),
            new Image("assets/Pacman/Whip/right-whip1.png")
    };

    /**
     * The current slide.
     */
    private int currentSlide = 0;

    /**
     * The navigation adapter.
     */
    private UINavigationAdapter<ToggleButton> adapter = new UINavigationAdapter<>();


    public TutorialViewController() {
        super("/group23/pacman/view/TutorialViewController.fxml");

        // setup adapter
        adapter.addRow(back,prev,next);
        adapter.current().setSelected(true);

        // update slide
        updateSlide();
    }

    @Override
    public void didBecomeActive() {
        // register controller to joystick manager
        JoystickManager
                .shared
                .subscribe(identifier(), this);
    }

    @Override
    public void didEnterBackground() {
        JoystickManager.shared.unsubscribe(identifier());
    }

    @Override
    public void onJoystickTriggered(int joystickId, JoystickManager.Key selectedKey) {
        if(joystickId == 1) {
            switch (selectedKey){
                case LEFT:
                    // move left
                    adapter.current().setSelected(false);
                    adapter.move_left().setSelected(true);
                    break;
                case RIGHT:
                    // move right
                    adapter.current().setSelected(false);
                    adapter.move_right().setSelected(true);
                    break;
                case ONE:
                    // select
                    activate(adapter.current());
                    break;
                case TWO:
                    // go back
                    MainApp.getInstance().popViewController(true);
                    break;
            }
        }
    }

    /**
     * handles the action from the current button.
     * @param current The current button selected.
     */
    private void activate(ToggleButton current) {
        if (current == prev) {
            // prev
            currentSlide--;
            if (currentSlide < 0)
                currentSlide = slides.length - 1;

            updateSlide();
        }

        if (current == next) {
            // next
            currentSlide++;
            if (currentSlide >= slides.length)
                currentSlide = 0;

            updateSlide();
        }

        if (current == back) {
            MainApp.getInstance().popViewController(true);
        }
    }

    /**
     * Updates the slide.
     */
    private void updateSlide(){
        Image slide = slides[currentSlide];
        imageView.setImage(slide);

        slideNumLabel.setText((currentSlide + 1) + "/" + slides.length );
    }
}
