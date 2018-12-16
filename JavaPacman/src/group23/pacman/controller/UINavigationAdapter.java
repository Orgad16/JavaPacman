package group23.pacman.controller;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created By Tony on 12/12/2018
 *
 * This class is used to organize UI Controls in a data structure such that it makes it easy to navigate through it using simple controls like arrow keys.
 * The way it works is simply by adding a new row to the instance object, then using the current/move functions provided.
 *
 * The default object is always (0,0) or the first item on the first row. (left to right)
 *
 * Let's say we have a simple UI with 5 buttons that looks like this on screen:
 * |-------------|
 * | [A] [B] [C] |
 * |    [D] [E]  |
 * |-------------|
 *
 * We can clearly see that A,B,C are on the same row, and D,E are on the same row.
 * In such case we use the addRow function like so:
 *
 * ```
 * UINavigationAdapter<Button> navAdapter = new UINavigationAdapter<>();
 *
 * navAdapter.addRow(A,B,C);
 *
 * navAdapter.addRow(D,E);
 * ```
 *
 * By doing so we have defined two rows. By using ```current()``` we receive object A.
 * by calling move_left() we'll receive object C, and if we called move_down() we'll get object D.
 *
 * Do note that every time you call a move function the x,y positions will be updated.
 *
 */
public class UINavigationAdapter<T> {
    private Vector<Vector<T>> dataGrid = new Vector<>(10);
    private int x;
    private int y;

    /**
     * @return the current row.
     */
    public int getX() {
        return x;
    }

    /**
     * @return the current column.
     */
    public int getY() {
        return y;
    }

    /**
     * Adds a new row to the navigation adapter.
     *
     * @param firstElement The first element in the row.
     * @param otherElements any other elements
     */
    public void addRow(T firstElement, T... otherElements){
        Vector<T> items = new Vector<>(otherElements.length + 1);
        items.add(firstElement);
        items.addAll(Arrays.asList(otherElements));
        dataGrid.add(items);
    }

    /**
     * @return returns the current object being pointed to.
     */
    public T current(){
        return dataGrid.get(x).get(y);
    }

    /**
     * @return moves one row down and returns the object pointed to.
     */
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

    /**
     * @return moves one row up and returns the object pointed to.
     */
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

    /**
     * @return moves one column to the left and returns the object pointed to.
     */
    public T move_left(){
        y -= 1;
        if (y == -1){
            y = dataGrid.get(x).size() - 1;
        }

        return current();
    }

    /**
     * @return moves one column to the right and returns the object pointed to.
     */
    public T move_right(){
        y += 1;
        if (y == dataGrid.get(x).size()){
            y = 0;
        }
        return current();
    }
}
