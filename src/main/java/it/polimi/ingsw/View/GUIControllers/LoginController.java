package it.polimi.ingsw.View.GUIControllers;

import com.google.gson.Gson;

import it.polimi.ingsw.Controller.Messages.AddPlayerMessage;
import it.polimi.ingsw.View.GUI;
import it.polimi.ingsw.View.PrinterSingleton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField nicknameField;
    @FXML
    private Label loginFailed;
    @FXML
    private Label loginFailedReconnection;
    @FXML
    private Label gameStartedError;
    @FXML
    private Label playerNumberExceeded;

    public void login(ActionEvent actionEvent) {
        Gson gson = new Gson();
        AddPlayerMessage addPlayerMessage = new AddPlayerMessage();
        addPlayerMessage.setSenderNickname(nicknameField.getText());
        addPlayerMessage.setMessageType("AddPlayer");
        PrinterSingleton.getPrinterSingleton().sendMessage(gson.toJson(addPlayerMessage));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GUI.setLoginController(this);
    }

    public void setLoginError(){
        playerNumberExceeded.setVisible(false);
        gameStartedError.setVisible(false);
        loginFailedReconnection.setVisible(false);
        loginFailed.setVisible(true);
    }
    public void setGameStartedError(){
        playerNumberExceeded.setVisible(false);
        loginFailed.setVisible(false);
        gameStartedError.setVisible(true);
        loginFailedReconnection.setVisible(true);
    }
    public void setPlayerNumberExceeded(){
        loginFailed.setVisible(false);
        gameStartedError.setVisible(false);
        loginFailedReconnection.setVisible(false);
        playerNumberExceeded.setVisible(true);
    }
}
