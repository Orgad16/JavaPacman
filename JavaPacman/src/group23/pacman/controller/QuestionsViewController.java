package group23.pacman.controller;

import group23.pacman.model.Question;
import group23.pacman.system.SysData;
import group23.pacman.view.question.QuestionCell;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ui.UIViewController;

import java.util.List;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class QuestionsViewController extends UIViewController {

    @FXML
    private ListView<Question> listView;

    @FXML
    private AnchorPane rightPane;

    @FXML
    private AnchorPane leftPane;

    @FXML
    private SplitPane splitPane;

    public QuestionsViewController() {
        super("/group23/pacman/view/QuestionsViewController.fxml");

        leftPane.setStyle("-fx-background-color: white;");
        rightPane.setStyle("-fx-background-color: white;");
        listView.setCellFactory(param -> new QuestionCell());

        // add selection listener
        listView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
            if(newValue!=null)
                updateView(newValue);
        });

        loadData();
    }

    private void loadData() {
        List<Question> qestionList = SysData.instance.getQuestionsFromJson();
        listView.getItems().clear();
        listView.getItems().addAll(qestionList);

        listView.getSelectionModel().select(0);

    }

    /**
     * method is called when clicking one of the question items in the list on the left.
     * @param question
     */
    void updateView(Question question){
        // create question controller
        SingleQuestionViewController questionViewController =
                new SingleQuestionViewController(question);

        // display controller
        showView(questionViewController);
    }

    /**
     * display the controller on the right side of the pane.
     *
     * @param controller
     */
    void showView(UIViewController controller){
        rightPane.getChildren().clear();
        if(controller != null){
            Pane view = controller.view;
            AnchorPane.setTopAnchor(view,8.0);
            AnchorPane.setBottomAnchor(view,8.0);
            AnchorPane.setRightAnchor(view,8.0);
            AnchorPane.setLeftAnchor(view,8.0);
            rightPane.getChildren().add(view);
        }

    }
}
