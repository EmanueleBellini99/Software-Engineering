package it.polimi.ingsw.View;

import it.polimi.ingsw.View.GUIControllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class GUI extends Application {
    private static Scene scene;
    private static Stage primaryStage;
    private static String clientNickname;
    private static LoginController loginController;
    private static LobbyController lobbyController;
    private static FirstTurnController firstTurnController;
    private static MainSceneController mainSceneController;
    private static MarketSceneController marketSceneController;
    private static DeckgridSceneController deckgridSceneController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = new Scene(loadFXML("login_scene"));
        primaryStage.setTitle("Masters of Renaissance");

      /*  Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double width = resolution.getWidth();
        double height = resolution.getHeight();
        if( height!=1920 && width!= 1080 ) {
            double w = width / 1920;  // your window width
            double h = height / 1080;  // your window height
            Scale scale = new Scale(w-0.15, h-0.15, 1000, 600);
            scene.getRoot().getTransforms().add(scale);
        }*/

        //primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
        setPrimaryStage(primaryStage);
    }



    public static Parent loadFXML(String fxml) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(Client.class.getClassLoader().getResource(fxml + ".fxml")));

    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setLoginController(LoginController loginController) {
        GUI.loginController = loginController;
    }

    public static LoginController getLoginController() {
        return loginController;
    }

    public static LobbyController getLobbyController() {
        return lobbyController;
    }

    public static void setLobbyController(LobbyController lobbyController) {
        GUI.lobbyController = lobbyController;
    }

    public static void setFirstTurnController(FirstTurnController firstTurnController) { GUI.firstTurnController = firstTurnController; }

    public static FirstTurnController getFirstTurnController() { return firstTurnController; }

    public static String getClientNickname() {
        return clientNickname;
    }

    public static void setClientNickname(String clientNickname) {
        GUI.clientNickname = clientNickname;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        GUI.primaryStage = primaryStage;
    }

    public static MainSceneController getMainSceneController() {
        return mainSceneController;
    }

    public static void setMainSceneController(MainSceneController mainSceneController) { GUI.mainSceneController = mainSceneController; }

    public static MarketSceneController getMarketSceneController() { return marketSceneController; }

    public static void setMarketSceneController(MarketSceneController marketSceneController) { GUI.marketSceneController = marketSceneController; }

    public static DeckgridSceneController getDeckgridSceneController() { return deckgridSceneController; }

    public static void setDeckgridSceneController(DeckgridSceneController deckgridSceneController) { GUI.deckgridSceneController = deckgridSceneController; }
}
