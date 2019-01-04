package group23.pacman.controller;

import group23.pacman.model.Question;
import group23.pacman.system.SysData;
import group23.pacman.view.question.QuestionCell;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    @FXML
    private Button create;

    public PersistCallBack callBack = (id, object, delete) -> {
        if(id == -1) {
            // new question
            listView.getItems().add(object);
        } else {
            if (delete) {
                // remove question at index (id)
                listView.getItems().remove(id);
            }else {
                // update question at index (id)
                listView.getItems().remove(id);
                listView.getItems().add(id,object);
            }
        }

        collect(listView.getItems());
    };

    public QuestionsViewController() {
        super("/group23/pacman/view/QuestionsViewController.fxml");

        leftPane.setStyle("-fx-background-color: white;");
        rightPane.setStyle("-fx-background-color: white;");
        listView.setCellFactory(param -> new QuestionCell());

        // add selection listener
        listView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if(newValue!=null)
                updateView(index,newValue);
        });

        loadData();

        create.setOnAction(event -> updateView(-1,null));
    }

    /**
     * method that is sued to load questions form file.
     */
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
    void updateView(int index,Question question){
        // create question controller
        SingleQuestionViewController questionViewController =
                new SingleQuestionViewController(index,question,callBack);

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

    /**
     * method is called when it's time to save questions to disk.
     * @param questions
     */
    void collect(List<Question> questions) {
        // store questions in disk
        //TODO complete method here
    }

    @FunctionalInterface
    interface PersistCallBack {
        void onPersist(int id,Question object,boolean delete);
    }
}
