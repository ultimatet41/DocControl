package windows;

import data.AbstrDoc;
import javafx.stage.Stage;

public abstract class AbstrWindow {
    abstract public void updateData();
    abstract public void setData(AbstrDoc abstrDoc);
    abstract public void setStage(Stage thisWindowStage);
    abstract public void setAbstrWindow(AbstrWindow abstrWindow);
}
