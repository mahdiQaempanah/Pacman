package sample.Model.Tools;

import javafx.scene.input.KeyCode;
import sample.Model.GameObjects.MoveType;

public class GraphicTool {
    public static MoveType translateKeyCodeToMoveType(KeyCode keyCode) {
        MoveType ans = new MoveType();
        if (keyCode == KeyCode.UP)
            ans.setType(MoveType.DOWN);

        if (keyCode == KeyCode.DOWN)
            ans.setType(MoveType.UP);

        if (keyCode == KeyCode.LEFT)
            ans.setType(MoveType.LEFT);

        if (keyCode == KeyCode.RIGHT)
            ans.setType(MoveType.RIGHT);

        if (ans.getType() == null)
            return null;
        return ans;
    }
}
