package it.polimi.ingsw.View.CLINotifiers;

import com.google.gson.Gson;
import it.polimi.ingsw.Controller.Messages.ChosenLeaderCardsMessage;
import it.polimi.ingsw.View.ModelPrinter;
import it.polimi.ingsw.View.Printers.LeaderCardsPrinter;

/**
 * Notifies the CLI of a leader card selection.
 */
public class ChosenLeaderCards extends CLINotifier {

    String nickname;

    public ChosenLeaderCards(ModelPrinter modelPrinter){
        super(modelPrinter);
    }

    @Override
    public void notifyCLI(String notification) {
        Gson gson = new Gson();
        ChosenLeaderCardsMessage data = gson.fromJson(notification, ChosenLeaderCardsMessage.class);

        nickname = data.getSenderNickname();
        int[] num = new int[2];
        for(LeaderCardsPrinter l : modelPrinter.getLeaderCardsPrinters() ){
            if( l.getOwnerNickname().equals(nickname) ){
                num[0]=data.getFirstChosenLeaderCardNumber();
                num[1]=data.getSecondChosenLeaderCardNumber();
                l.setChosenLeaderCards(num);
            }
        }
    }
}
