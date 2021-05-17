package it.polimi.ingsw.Controller.RequestManagers;

import com.google.gson.Gson;
import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Messages.Message;
import it.polimi.ingsw.Controller.Messages.MultiplayerCreationMessage;
import it.polimi.ingsw.Player;

import java.util.ArrayList;

public class StartMultiPlayerManager implements Manageable {
    private final Controller controller;

    public StartMultiPlayerManager(Controller controller) {
        this.controller = controller;
    }

    @Override
    public String manageRequest(String jsonContent) {
        boolean ans;
        Gson gson = new Gson();
        Message message = new Message();
        ArrayList<Player> players;
        ans=controller.getGame().startMultiplayer();
        if (ans) {
            players=controller.getGame().getPlayers();
            for (ClientHandler clientHandler : controller.getConnectedClients()) {
                    for (Player player : players) {
                            MultiplayerCreationMessage multiplayerCreationMessage = new MultiplayerCreationMessage();
                            multiplayerCreationMessage.setMessageType("MultiPlayerCreationMessage");
                            multiplayerCreationMessage.setPlayerNumber(player.getPlayerNumber());
                            multiplayerCreationMessage.setNickname(player.getNickname());
                            multiplayerCreationMessage.setChoosableLeaderCardsNumbers(player.getChoosableLeadercardsNumber());
                            multiplayerCreationMessage.setDeckgridConfiguration(controller.getGame().getDeckgrid());
                            multiplayerCreationMessage.setMarbleGridConfiguration(controller.getGame().getMarketBoard().getMarketboardColours());
                            multiplayerCreationMessage.setMarbleOut(controller.getGame().getMarketBoard().getMarbleOut().getColour());
                            String s = gson.toJson(multiplayerCreationMessage);
                            clientHandler.notifyInterface(s);
                    }
                }
            message.setMessageType("MultiPlayerCreationOkNotification");
        } else {
            message.setMessageType("MultiPlayerCreationErrorNotification");
        }
        return gson.toJson(message);
    }
}
