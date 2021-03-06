package it.polimi.ingsw.Controller.RequestManagers;

import com.google.gson.Gson;
import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Messages.BuyDevelopmentCardMessage;
import it.polimi.ingsw.Controller.Messages.Message;
import it.polimi.ingsw.Controller.Messages.NotifyDevelopmentCardInsertedOKMessage;
import it.polimi.ingsw.Controller.NotifiableHandler;
import it.polimi.ingsw.DevelopmentCard;
import it.polimi.ingsw.ExtraDeposit;

/**
 * This manager handles a buy development card request
 */
public class BuyDevelopmentCardManager implements Manageable{
    private final Controller controller;

    public BuyDevelopmentCardManager(Controller controller) {
        this.controller = controller;
    }

    @Override
    public String manageRequest(String jsonContent) {
        if(!controller.getGame().isGameStarted()){
            Gson gson = new Gson();
            Message notification = new Message();
            notification.setMessageType("GameNotStartedNotification");
            return gson.toJson(notification);
        }
        Gson gson = new Gson();
        BuyDevelopmentCardMessage buyDevelopmentCardMessage = gson.fromJson(jsonContent, BuyDevelopmentCardMessage.class);
        if (controller.getGame().getCurrentPlayer().getNickname().equals(buyDevelopmentCardMessage.getSenderNickname())) {
            if(controller.getGame().buyDevelopmentCard(buyDevelopmentCardMessage.getLevel(),buyDevelopmentCardMessage.getColour(),buyDevelopmentCardMessage.getSlot(),buyDevelopmentCardMessage.getPayUsingExtraDeposit()[0],buyDevelopmentCardMessage.getPayUsingExtraDeposit()[1])){
                Message mex = new Message();
                mex.setMessageType("BuyDevelopmentCardSuccessNotification");
                NotifyDevelopmentCardInsertedOKMessage notification = new NotifyDevelopmentCardInsertedOKMessage();
                notification.setMessageType("DevelopmentCardBought");
                notification.setInsertedDevelopmentCard(controller.getGame().getCurrentPlayer().getInsertedDevCards());
                notification.setDeckgridConfiguration(controller.getGame().getDeckgrid());
                notification.setNickname(controller.getGame().getCurrentPlayer().getNickname());
                notification.setDevelopmentCardsConfiguration(controller.getGame().getCurrentPlayer().getPersonalBoard().getDevelopmentCard());
                notification.setWarehouseConfiguration(controller.getGame().getCurrentPlayer().getPersonalBoard().getWarehouseDepot());
                notification.setStrongboxConfiguration(controller.getGame().getCurrentPlayer().getPersonalBoard().getStrongbox().getResourcesContained());
                ExtraDeposit[] extraDeposits = new ExtraDeposit[2];
                extraDeposits[0] = controller.getGame().getCurrentPlayer().getPersonalBoard().getExtraDeposit1();
                extraDeposits[1] = controller.getGame().getCurrentPlayer().getPersonalBoard().getExtraDeposit2();
                notification.setExtraDepositsConfiguration(extraDeposits);
                for (NotifiableHandler clientHandler : controller.getConnectedClients()) {
                    clientHandler.notifyInterface(gson.toJson(notification));
                }
                return gson.toJson(mex);
            } else {
                Message mex = new Message();
                mex.setMessageType("BuyDevelopmentCardFailureNotification");
                return gson.toJson(mex);
            }
        }
        Message mex = new Message();
        mex.setMessageType("NotYourTurnNotification");
        return gson.toJson(mex);
    }
}
