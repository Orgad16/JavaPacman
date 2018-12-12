package group23.pacman.controller;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created By Tony on 12/12/2018
 */
public class UINavigationAdapter<T> {
    private Vector<Vector<T>> dataGrid = new Vector<>(10);
    private int x;
    private int y;

    public void addRow(T... data){
        Vector<T> items = new Vector<>(Arrays.asList(data));
        dataGrid.add(items);
    }

    public T current(){
        return dataGrid.get(x).get(y);
    }

    public T move_down(){
        x += 1;

        // normalize x
        if (x == dataGrid.size()) {
            x = 0;
        }

        // normalize y if needed
        if (dataGrid.get(x).size() <= y) {
            y = dataGrid.get(x).size() - 1;
        }

        return current();
    }

    public T move_up(){
        x -= 1;

        // normalize x
        if (x == -1) {
            x = dataGrid.size() - 1;
        }

        // normalize y if needed
        if (dataGrid.get(x).size() <= y) {
            y = dataGrid.get(x).size() - 1;
        }

        return current();
    }

    public T move_left(){
        y -= 1;
        if (y == -1){
            y = dataGrid.get(x).size() - 1;
        }

        return current();
    }

    public T move_right(){
        y += 1;
        if (y == dataGrid.get(x).size()){
            y = 0;
        }
        return current();
    }
}
