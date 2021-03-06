package it.polimi.ingsw.View.GUINotifiers;

import it.polimi.ingsw.View.GUI;
import javafx.application.Platform;

/**
 * Notifies the GUI of the failure of the insertion of a resource
 */
public class InsertedFailGUINotifier extends GUINotifier{
    @Override
    public void notifyGui(String notification) {
        Platform.runLater(()->{
            GUI.getMarketSceneController().getNotificationLabel().setVisible(true);
            GUI.getMarketSceneController().getNotificationLabel().setText("Insertion incompatible with the warehouse configuration.");
        });
    }
}
