package it.polimi.ingsw.Controller.RequestManagers;

import com.google.gson.Gson;
import it.polimi.ingsw.Controller.ClientHandler;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Messages.EndTurnNotificationMessage;
import it.polimi.ingsw.Controller.Messages.EndTurnRequestMessage;
import it.polimi.ingsw.Controller.Messages.Message;
import it.polimi.ingsw.Controller.Messages.VaticanReportMessage;
import it.polimi.ingsw.Controller.NotifiableHandler;
import it.polimi.ingsw.Player;
import it.polimi.ingsw.ResourceType;

/**
 * This manager handles an end turn request
 */
public class EndTurnManager implements Manageable{
    private final Controller controller;

    public EndTurnManager(Controller controller) {
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
        Message message = new Message();
        boolean ans1;
        boolean ans2=false;
        boolean endGame=false;
        String nickname=null;
        String winnerNickname=null;
        int[] faithCardsBefore = controller.getVaticanReport().clone();
        int tempResourcesDiscarded=0;
        EndTurnRequestMessage endTurnRequestMessage = gson.fromJson(jsonContent, EndTurnRequestMessage.class);
            ans1= endTurnRequestMessage.getSenderNickname().equals(controller.getGame().getCurrentPlayer().getNickname());
            if(ans1){
                for(ResourceType res: controller.getGame().getMarketBoard().getTemporaryResources().keySet()){
                tempResourcesDiscarded+=controller.getGame().getMarketBoard().getTemporaryResources().get(res);
                }
                ans2= controller.getGame().endTurn();
                if(ans2){
                    nickname=controller.getGame().getCurrentPlayer().getNickname();
                    endGame=controller.getGame().isEndGame();
                    if(controller.getGame().getWinnerPlayer()!=null)
                        winnerNickname=controller.getGame().getWinnerPlayer().getNickname();
                }
            }
        if(ans1){
            if(ans2){
                EndTurnNotificationMessage mex = new EndTurnNotificationMessage();
                mex.setActualCurrentPlayer(nickname);
                mex.setNumResourcesDiscarded(tempResourcesDiscarded);
                mex.setWinnerPlayerNickname(winnerNickname);
                mex.setWinnerPlayerNumber(controller.getGame().getWinnerPlayerNumber());
                if(controller.getGame().getWinnerPlayerNumber()==5)
                    mex.setWinnerPoints(controller.getGame().getPlayers().get(0).getVictoryPoints());
                else if(controller.getGame().getWinnerPlayer()!=null)
                    mex.setWinnerPoints(controller.getGame().getWinnerPlayer().getVictoryPoints());
                mex.setGameEnding(endGame);
                mex.setMessageType("EndTurnNotificationMessage");
                mex.setTemporaryResources(controller.getGame().getMarketBoard().getTemporaryResources());
                if(controller.getGame().getPlayers().size()==1){
                    mex.setBlackFaithPoints(controller.getGame().getLorenzo().getBlackFaithPoints());
                }
                String notificationForAll = gson.toJson(mex);
                for(NotifiableHandler c : controller.getConnectedClients()){
                        c.notifyInterface(notificationForAll);
                }
                int[] faithCardsAfter = controller.getVaticanReport();
                int whichReport = 0;
                boolean vaticanReportOccurred = false;
                for (int i = 0; i < 3; i++) {
                    if (faithCardsBefore[i] != faithCardsAfter[i]) {
                        switch (i) {
                            case 0:
                                whichReport = 1;
                                vaticanReportOccurred = true;
                                break;
                            case 1:
                                whichReport = 2;
                                vaticanReportOccurred = true;
                                break;
                            case 2:
                                whichReport = 3;
                                vaticanReportOccurred = true;
                                break;
                        }
                    }
                }
                VaticanReportMessage vaticanReportMessage = new VaticanReportMessage();
                vaticanReportMessage.setMessageType("VaticanReportMessage");
                vaticanReportMessage.setOccurred(vaticanReportOccurred);
                vaticanReportMessage.setWhichOne(whichReport);
                for (NotifiableHandler clientHandler : controller.getConnectedClients()) {
                    for(Player p: controller.getGame().getPlayers()) {
                        vaticanReportMessage.setNickname(p.getNickname());
                        vaticanReportMessage.setNewFaithPoints(p.getFaithPoints());
                        clientHandler.notifyInterface(gson.toJson(vaticanReportMessage));
                    }
                }
                message.setMessageType("EndTurnOkNotification");
                return gson.toJson(message);
            }
            message.setMessageType("NotRightToEndTurnNotification");
            return gson.toJson(message);
        }
        message.setMessageType("NotYourTurnNotification");
        return gson.toJson(message);
    }
}
