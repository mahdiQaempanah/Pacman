package sample.Model.Tools;

import javafx.util.Pair;
import sample.Model.BoardMapBarriers;
import sample.Model.IntegerCoordinates;

import java.util.ArrayList;

public class SmallUsefullTool {
    static public Pair<IntegerCoordinates, IntegerCoordinates> findObjectInCollection(Pair<IntegerCoordinates, IntegerCoordinates> searchFor, ArrayList<Pair<IntegerCoordinates, IntegerCoordinates>> objects) {
        for (Pair<IntegerCoordinates, IntegerCoordinates> object : objects) {
            if (searchFor.getValue().equal(object.getValue()) && searchFor.getKey().equal(object.getKey()))
                return object;
        }
        return null;
    }

    static public boolean badCell(IntegerCoordinates cell) {
        return cell.getY() < 0 || cell.getX() < 0 ||
                cell.getY() >= BoardMapBarriers.boardSize.getY() || cell.getX() >= BoardMapBarriers.boardSize.getX();
    }
}
