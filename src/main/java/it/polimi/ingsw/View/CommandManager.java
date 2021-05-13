package it.polimi.ingsw.View;


import it.polimi.ingsw.View.MessageBuilders.*;

public class CommandManager {
    private VirtualView virtualView;
    private String nickname;

    public CommandManager(VirtualView virtualView, String nickname) {
        this.virtualView = virtualView;
        this.nickname = nickname;
    }

    public String manage(String userInput){
        MessageBuilder toBuild;
        switch (userInput) {
   //Help to show all commands
            case "help":

//Logging in
            case "login":

//Activating production
            case "ActivateProduction":

//Activating leader card(s)
            case "ActivateLeaderCard":

//Activating leader ability
            case "ActivateLeaderAbility":

//Activating action card
            case "ActionCardActivation":

//Activating SwitchLevels
            case "SwitchLevels":
                toBuild = new SwitchLevelMessageBuilder(nickname);
                return toBuild.buildMessage();
//Activating TakeResourceFromMarket
            case "TakeResourcesFromMarket":
                toBuild = new TakeResourceFromMarketMessageBuilder(nickname);
                return toBuild.buildMessage();
//Activating InsertResourceIntoWarehouse
            case "InsertResourcesIntoWarehouse":
                toBuild = new InsertResourcesIntoWarehouseMessageBuilder(nickname);
                return toBuild.buildMessage();
//Activating BuyDevelopmentCard
            case "BuyDevelopmentCard":
                toBuild = new BuyDevelopmentCardMessageBuilder(nickname);
                return toBuild.buildMessage();
//Starting single player mode
            case "StartSinglePlayer":
                toBuild = new StartSinglePlayerMessageBuilder();
                return toBuild.buildMessage();
//Starting a multi player game
            case "StartMultiPlayer":
                toBuild = new StartMultiPlayerMessageBuilder();
                return toBuild.buildMessage();
//Activating an initial resource distribution for the second or the third player
            case "DistributionSecondThird":
                toBuild = new DistributionSecondThirdMessageBuilder(nickname);
                return toBuild.buildMessage();
//Activating an initial resource distribution for the fourth player
            case "DistributionFourth":
                toBuild = new DistributionFourthMessageBuilder(nickname);
                return toBuild.buildMessage();
//Selecting the leader cards from the 4 choosable cards
            case "LeaderCardSelection":
                toBuild = new LeaderCardSelectionMessageBuilder(nickname);
                return toBuild.buildMessage();
// Ending turn
            case "EndTurnRequest":
                toBuild = new EndTurnRequestMessageBuilder(nickname);
                return toBuild.buildMessage();

        }
        return "Invalid command";
    }

}
