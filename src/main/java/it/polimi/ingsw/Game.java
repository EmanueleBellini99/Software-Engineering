package it.polimi.ingsw;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Main class: Users actions are managed by Game class.
 */
public class Game {

    /**
     * Reference to market board, unique in the game and shared by all players.
     */
    private final MarketBoard marketBoard = new MarketBoard(this);
    /**
     * Contains all the Game's players.
     */
    private final ArrayList<Player> players = new ArrayList<>();
    /**
     * It represents the player who is playing in this turn.
     */
    private Player currentPlayer;

    public Deckgrid getDeckgrid() {
        return deckgrid;
    }

    /**
     * Represents the 12 decks of development cards.
     */
    private final Deckgrid deckgrid = new Deckgrid();

    public ActionCardStack getActionCardStack() {
        return actionCardStack;
    }

    /**
     * Represents the deck of Solo Action tokens, used in single player mode.
     */
    private ActionCardStack actionCardStack;

    public LorenzoSingleton getLorenzo() {
        return lorenzo;
    }

    /**
     * Represents Lorenzo il Magnifico: the virtual opponent in single player mode.
     */
    private LorenzoSingleton lorenzo;
    /**
     * True if game has already started.
     */
    private boolean gameStarted = false;
    /**
     * Represents through the player number(first player, second player.. ordered by tourn order) which of all
     * players is the winner of the game.
     */
    private int WinnerPlayerNumber;
    /**
     * The Player who wins the game.
     */
    private Player winnerPlayer;
    /**
     * It is true if a player reaches the end of faith track or buy the 7'th development cards.
     */
    private boolean isEndGame = false;
    /**
     * True if the player has already done one of the necessary actions to end the turn.
     */
    private boolean actionCardDone = false;
    /**
     * Represent the initial leader card's deck that contains all the leader cards of the game.
     */
    private final LeaderCardDeck leaderCardDeck = new LeaderCardDeck();

    /** Represent the score of the single player */
    private int singlePlayerScore;

    /** True if all clients are disconnected */
    private boolean allAFK = false;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Game getGame() {
        return this;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setEndGame(boolean endGame) {
        isEndGame = endGame;
    }

    public MarketBoard getMarketBoard() {
        return marketBoard;
    }

    public Game() throws FileNotFoundException {
    }

    /**
     * Add a player in the game, if game has less then 4 players.
     * @param nickname is the player's nickname in the game
     * @return true if has correctly added a player in the game
     */
    public boolean addPlayer( String nickname ){
        if( nickname.length()<1 || nickname.length()>20 )
            return false;
        if( players.size() >= 4 || gameStarted ) {
            return false;
        }
        for(Player p: players){
            if(p.getNickname().equals(nickname))
                return false;
        }
        Player p = new Player();
        p.setNickname(nickname);
        p.setGame(this);
        players.add(p);
        return true;
    }

    /**
     * Starts the single player mode, only if there is 1 player in game.
     * @return true if game correctly starts in single player mode.
     */
    public boolean startSinglePlayer(){
        if( players.size()!=1 || gameStarted ) {
            return false;
        }
        leaderCardDeck.randomDistribute(players.get(0));
        this.actionCardStack = new ActionCardStack(deckgrid);
        this.lorenzo = LorenzoSingleton.getLorenzo();
        currentPlayer=players.get(0);
        currentPlayer.setCanEndTurn(true);
        players.get(0).setPlayerNumber(1);
        lorenzo.setGame(this);
        gameStarted = true;
        players.get(0).setInitialDistribution(true);
        actionCardDone = true;
        return true;
    }


    /**
     * Starts the multiplayer mode, only if there at least 2 players in the game(maximum 4 players).
     * @return true if the game correctly starts in multiplayer mode.
     */
    public boolean startMultiplayer(){
        if( players.size()<=1 || players.size()>4 || gameStarted ) {
            return false;
        }
        Collections.shuffle(players);
        players.get(0).setInkwell(true);
        if(!players.get(0).isAFK())
            currentPlayer = players.get(0);
        else{
            for(Player p: players) {
                if (!p.isAFK()) {
                    currentPlayer = p;
                    break;
                }
            }
        }
        for( int i = 0; i<players.size(); i++ ){
            players.get(i).setPlayerNumber(i+1);
            leaderCardDeck.randomDistribute(players.get(i));
            if( i==0 ){
                players.get(i).setInitialDistribution(true);
                players.get(i).setCanEndTurn(true);
            }
            if( i==2){
                players.get(i).addFaithPointsAndCallAudience(1);
            }
            else if( i==3 ){
                players.get(i).addFaithPointsAndCallAudience(1);
            }
        }
        for(Player p: players){
            if(p.isAFK()){
                p.chooseLeaderCards(0,1);
                p.setChosenLeaderCards(true);
                p.setInitialDistribution(true);
                if(p.getPlayerNumber()==2||p.getPlayerNumber()==3)
                    p.insertResourcesIntoWarehouse(ResourceType.STONES,1,1);
                else if(p.getPlayerNumber()==4)
                    p.insertResourcesIntoWarehouse(ResourceType.STONES,2,2);
            }
        }
        gameStarted = true;
        return true;
    }


    /**
     * private method, finds next player on base of AFK's players.
     * @param number is the current player's number.
     * @return the new current player.
     */
    private Player findNextPlayer(int number){
        for( Player p: players ){
            if( p.getPlayerNumber() == number ){
                for( int i = number; i<players.size(); i++ ){
                    if( !players.get(i).isAFK() ){
                        return players.get(i);
                    }
                }
                for( int i=0; i<number; i++ ){
                    if( !players.get(i).isAFK() ){
                        return players.get(i);
                    }
                }
            }
        }
        return currentPlayer;
    }

    /**
     * Makes turn ends for the current player and starts the turn for the next player.
     * @return true if the player can end the turn.
     */
    public boolean endTurn(){
        allAFK = true;
        for( Player p: players ){
            if (!p.isAFK()) {
                allAFK = false;
                break;
            }
        }
        if( allAFK ){
            currentPlayer.getPersonalBoard().setRequirementForLeaderProduction1(null);
            currentPlayer.getPersonalBoard().setRequirementForLeaderProduction2(null);
            return true;
        }
        if( currentPlayer.notDoneInitialDistribution() || !currentPlayer.canEndTurn() || !currentPlayer.hasChosenLeaderCards()){
            return false;
        }
        int number = currentPlayer.getPlayerNumber();
        if( currentPlayer.isAFK() ){
            currentPlayer.getPersonalBoard().setRequirementForLeaderProduction1(null);
            currentPlayer.getPersonalBoard().setRequirementForLeaderProduction2(null);
            currentPlayer = findNextPlayer(number);
            currentPlayer.setCanEndTurn(false);
            marketBoard.setWhiteMarblesSelected(0);
            if( !marketBoard.getTemporaryResources().isEmpty() )
                discardRemainingResources();
            return true;
        }
        if(currentPlayer.getNumTransformationAbility()>0&&marketBoard.getWhiteMarblesSelected()>0) return false;
        marketBoard.setWhiteMarblesSelected(0);
        if(players.size()==1&&!actionCardDone) return false;
        actionCardDone=false;
        if( !marketBoard.getTemporaryResources().isEmpty() ){
            discardRemainingResources();
        }
        if(players.size()==1&&lorenzo!=null&&lorenzo.isLorenzoWinner())
            isEndGame=true;
        if( currentPlayer.getPlayerNumber()==players.size() ){
            if( isEndGame )
                endGame();
        }
        currentPlayer.getPersonalBoard().setRequirementForLeaderProduction1(null);
        currentPlayer.getPersonalBoard().setRequirementForLeaderProduction2(null);
        currentPlayer = findNextPlayer(number);
        currentPlayer.setCanEndTurn(false);
        return true;
    }


    /**
     * Does the initial distribution of one resource for the second and third players.
     * @param resourceType is the resource type chosen by the player.
     * @return true if it is called on the second or third player.
     */
    public boolean distributionResourceSecondThird( ResourceType resourceType ){
        if( (currentPlayer.getPlayerNumber() == 2 || currentPlayer.getPlayerNumber() == 3)
            && currentPlayer.notDoneInitialDistribution() && resourceType!=ResourceType.FAITHPOINTS) {
            currentPlayer.insertResourcesIntoWarehouse(resourceType, 1, 1);
            currentPlayer.setInitialDistribution(true);
            currentPlayer.setCanEndTurn(true);
            return true;
        }
        return false;
    }

    /** Does the initial distribution of two resources for the fourth player.
     * @param resourceType1 is the first resource type chosen by the player.
     * @param resourceType2 is the second resource type chosen by the player.
     * @return true if it is called on the fourth player.
     */
    public boolean distributionResourceFourthPlayer( ResourceType resourceType1, ResourceType resourceType2 ){
        if( currentPlayer.getPlayerNumber() == 4 && currentPlayer.notDoneInitialDistribution()
        && resourceType1!=ResourceType.FAITHPOINTS && resourceType2!=ResourceType.FAITHPOINTS) {
            if( resourceType1.equals(resourceType2) ) {
                currentPlayer.insertResourcesIntoWarehouse(resourceType1, 2, 2);
            }
            else {
                currentPlayer.insertResourcesIntoWarehouse(resourceType1, 1, 1);
                currentPlayer.insertResourcesIntoWarehouse(resourceType2, 2, 1);
            }
            currentPlayer.setInitialDistribution(true);
            currentPlayer.setCanEndTurn(true);
            return true;
        }
        return false;
    }

    /**
     * Founds the winner player on base of victory points of all players and sets the winner player.
     * @return true when has found the winner player.
     */
    public boolean endGame(){
        int maxPoints = 0;
        int WinnerPlayerNumber = 1;

        for( int i=0; i<players.size(); i++ ){
            players.get(i).calculateVictoryPoints();
            if(players.size()==1){
                if(lorenzo.isLorenzoWinner()){
                    this.WinnerPlayerNumber=5; //HA VINTO LORENZO
                    this.winnerPlayer=null;
                }
                else{
                    this.WinnerPlayerNumber=1;
                    this.winnerPlayer=currentPlayer;
                    singlePlayerScore=currentPlayer.getVictoryPoints();
                }
            }
            if( players.get(i).getVictoryPoints() == maxPoints ) {
                if(players.get(WinnerPlayerNumber-1).getTotNumOfRes() < players.get(i).getTotNumOfRes()) {
                    WinnerPlayerNumber = players.get(i).getPlayerNumber();
                    this.WinnerPlayerNumber=WinnerPlayerNumber;
                    winnerPlayer=players.get(WinnerPlayerNumber-1);
                }
            }
            if ( players.get(i).getVictoryPoints() > maxPoints ) {
                maxPoints = players.get(i).getVictoryPoints();
                WinnerPlayerNumber = players.get(i).getPlayerNumber();
                this.WinnerPlayerNumber=WinnerPlayerNumber;
                winnerPlayer=players.get(WinnerPlayerNumber-1);
            }
        }
        return true;
    }

    /**
     * Allows the player to switch resources through warehouse's levels, if it is permitted by the rules.
     * @param maxSlotsFirst represents the first level to switch with the second.
     * @param maxSlotsSecond represents the second level to switch with the first.
     * @return true if the level switch is legal.
     */
    public boolean switchLevels(int maxSlotsFirst, int maxSlotsSecond){
        return currentPlayer.switchLevels(maxSlotsFirst, maxSlotsSecond);
    }

    /**
     * Allows the player to buy a development card from the deckgrid and insert that card into one of his personal board's slots.
     * @param level is the level of the card wanted by the player.
     * @param colour is the colour of the card wanted by the player.
     * @param slot is the personal board's slot where te player wants to insert the card
     * @return true if the player can buy that card and if the player can insert that card into the chosen slot
     */
    public boolean buyDevelopmentCard( int level, Colour colour, int slot , int payUsingExtraDep1, int payUsingExtraDep2 ) {
        if (currentPlayer.notDoneInitialDistribution() || currentPlayer.canEndTurn() || !currentPlayer.hasChosenLeaderCards())
            return false;
        if (payUsingExtraDep1 < 0 || payUsingExtraDep1 > 2 || (payUsingExtraDep1 > 0 && currentPlayer.getPersonalBoard().getExtraDeposit1() == null))
            return false;
        if (payUsingExtraDep2 < 0 || payUsingExtraDep2 > 2 || (payUsingExtraDep2 > 0 && currentPlayer.getPersonalBoard().getExtraDeposit2() == null))
            return false;

        if (payUsingExtraDep1 != 0 || payUsingExtraDep2 != 0) {
            currentPlayer.getPersonalBoard().setPayUsingExtraDep1(payUsingExtraDep1);
            currentPlayer.getPersonalBoard().setPayUsingExtraDep2(payUsingExtraDep2);
        }

        boolean canBuy;
        if (deckgrid.readCard(level, colour) == null)
            return false;
        canBuy = (currentPlayer.insertCard(deckgrid.readCard(level, colour), slot));
        if (canBuy) {
            deckgrid.removeCard(level, colour);
            currentPlayer.setCanEndTurn(true);
            return true;
        }
        return false;
    }


    /**
     * Allows the player to activate production from development cards, base production power, leader cards. The player
     * can choose all the possible productions or only some of those.
     * @param whichDevCardSlot says from which development cards the player wants to activate the production.
     * @param fromPersonalBoard says if the player wants to activate production from personal board.
     * @param whichLeaderCard says from which leader cards the player wants to activate the production.
     * @param resourceType1 is the first resource type of resource the player wants to pay to activate the base production power.
     *                      It is null if the player doesn't want to use the base production power.
     * @param resourceType2 is the second resource type of resource the player wants to pay to activate the base production power.
     *      *                      It is null if the player doesn't want to use the base production power.
     * @param obtainedResource is the resource type of resource received by base production power.
     *                         It is null if the player doesn't want to use the base production power.
     * @param resourceObtainedFromLeader is the resource type of resources received by leader card's production power.
     *      *                         It is null if the player doesn't want to use any leader card's production power or
     *                                   if the player hasn't got any leader card with a production ability
     * @return true if the productions can be done.
     */
    public boolean activateProduction(boolean[] whichDevCardSlot, boolean fromPersonalBoard, boolean[] whichLeaderCard,
                                      ResourceType resourceType1, ResourceType resourceType2, ResourceType obtainedResource,
                                      ResourceType[] resourceObtainedFromLeader, int payUsingExtraDep1, int payUsingExtraDep2 ){

        if( currentPlayer.notDoneInitialDistribution() || currentPlayer.canEndTurn() || !currentPlayer.hasChosenLeaderCards()  )
            return false;
        if (payUsingExtraDep1 < 0 || payUsingExtraDep1 > 2 || (payUsingExtraDep1 > 0 && currentPlayer.getPersonalBoard().getExtraDeposit1() == null))
            return false;
        if (payUsingExtraDep2 < 0 || payUsingExtraDep2 > 2 || (payUsingExtraDep2 > 0 && currentPlayer.getPersonalBoard().getExtraDeposit2() == null))
            return false;

        if (payUsingExtraDep1 != 0 || payUsingExtraDep2 != 0) {
            currentPlayer.getPersonalBoard().setPayUsingExtraDep1(payUsingExtraDep1);
            currentPlayer.getPersonalBoard().setPayUsingExtraDep2(payUsingExtraDep2);
        }

        //DEV CARDS
        for( int i=0; i<3; i++ ){
            if( whichDevCardSlot[i] ){
                currentPlayer.getPersonalBoard().activateProductionFromDevCard(i);
            }
        }

        //BASE PRODUCTION
        if( fromPersonalBoard && resourceType1!=null && resourceType2!=null &&
                obtainedResource!=null && obtainedResource!=ResourceType.FAITHPOINTS ){
            currentPlayer.getPersonalBoard().activateProductionFromPersonalBoard(resourceType1, resourceType2, obtainedResource);
        }


        //LEADER CARDS
        for( int j=0; j<2; j++ ){
            if( whichLeaderCard[j] && currentPlayer.getChoosedLeaderCards().get(j).isActive() && resourceObtainedFromLeader[j]!=null ){
                currentPlayer.getPersonalBoard().activateProductionFromLeaderCard(resourceObtainedFromLeader[j]);
            }
        }
        if(currentPlayer.getPersonalBoard().isTempStrongboxEmpty()) {
            return false;
        }
        currentPlayer.getPersonalBoard().fromStrongboxTempToStrongbox();
        currentPlayer.setCanEndTurn(true);
        currentPlayer.getPersonalBoard().setPayUsingExtraDep1(0);
        currentPlayer.getPersonalBoard().setPayUsingExtraDep2(0);
        return true;
    }

    /**
     * Allows the player to select a row or a column in the resource market to take the resources contained in that row or column.
     * @param row index of selection for row.
     * @param column index of selection for column.
     * @return true if player selects a correct row or column.
     */
    public boolean takeResourcesFromMarket( int row, int column ){
        if( currentPlayer.notDoneInitialDistribution() || currentPlayer.canEndTurn() || !currentPlayer.hasChosenLeaderCards())
            return false;
        if( marketBoard.getResourcesFromMarket(row, column) ) {
            currentPlayer.setCanEndTurn(true);
            return true;
        }
        return false;
    }

    /**
     * Allows the player to insert into his warehouse the resources he wants to insert, once he toke those resources from market.
     * @param resourceType is the resource type of resource that the player wants to insert
     * @param quantityToAdd is the quantity of resource that the player wants to insert.
     * @return true if the player can effectively insert than number of that type of resource.
     */
    public boolean insertResourcesIntoWarehouse( ResourceType resourceType, int quantityToAdd, boolean intoExtraDeposit ){
        if(marketBoard.getTemporaryResources().get(resourceType)==null) return false;
        int maxAddable = marketBoard.getTemporaryResources().get(resourceType);
        if( quantityToAdd > maxAddable || quantityToAdd <= 0 )
            return false;
        if (intoExtraDeposit) {
            if (currentPlayer.getPersonalBoard().addToExtraDeposit1(resourceType, quantityToAdd)) {
                marketBoard.getTemporaryResources().put(resourceType, maxAddable - quantityToAdd);
                return true;
            } else if (currentPlayer.getPersonalBoard().addToExtraDeposit2(resourceType, quantityToAdd)) {
                marketBoard.getTemporaryResources().put(resourceType, maxAddable - quantityToAdd);
                return true;
            }
            return false;
        }
        if( currentPlayer.getPersonalBoard().insertResources(resourceType, 1, quantityToAdd) ) {
            marketBoard.getTemporaryResources().put(resourceType, maxAddable - quantityToAdd);
            return true;
        }
        else if( currentPlayer.getPersonalBoard().insertResources(resourceType, 2, quantityToAdd) ){
            marketBoard.getTemporaryResources().put(resourceType, maxAddable - quantityToAdd);
            return true;
        }
        else if( currentPlayer.getPersonalBoard().insertResources(resourceType, 3, quantityToAdd) ){
            marketBoard.getTemporaryResources().put(resourceType, maxAddable - quantityToAdd);
            return true;
        }
        return false;
    }


    /**
     * After the insertion of resources into warehouse, the player has to discard all the resources taken from market
     * that he has not inserted into his warehouse. For every discarded resource, the other players earn one faith point.
     */
    private void discardRemainingResources(){
        Map<ResourceType, Integer> map;
        boolean doneAudience = false;
        map = marketBoard.getTemporaryResources();
        for( ResourceType r: map.keySet() ){
            int pointsToAdd = map.get(r);
            if(players.size()==1){
                lorenzo.addFaithPoints(pointsToAdd);
            }
            else {
                for (Player player : players) {
                    if (player.getPlayerNumber() != currentPlayer.getPlayerNumber()) {
                        player.addFaithPointsWithoutCallingAudience(pointsToAdd);
                    }
                }
                for (Player player : players) {
                    if (player.getPlayerNumber() != currentPlayer.getPlayerNumber()) {
                        player.endGameFaithPoints();
                        if (!doneAudience) {
                            doneAudience = player.callAudience();
                        }
                    }
                }
            }
        }
        map.clear();
    }


    /**
     * When a Faith Marker reaches (or goes beyond) a Pope space, a Vatican Report occurs. All the players in that Pope space
     * earn 2 victory points.
     * @return false and it does nothing if firsAudience() method was already called.
     */
    public boolean firstAudience(){
        for(Player player: players){
            if( player.getFaithCards()[0] == 0 )
                return false;
            if (player.getFaithPoints() >= 5) {
                player.addVictoryPoints(2);
            }
            player.setFaithCardsZero(0);
        }
        return true;
    }

    /**
     * When a Faith Marker reaches (or goes beyond) a Pope space, a Vatican Report occurs. All the players in that Pope space
     * earn 3 victory points.
     * @return false and it does nothing if secondAudience() method was already called.
     */
    public boolean secondAudience(){
        for(Player player: players){
            if( player.getFaithCards()[1] == 0 )
                return false;
            if (player.getFaithPoints() >= 12) {
                player.addVictoryPoints(3);
            }
            player.setFaithCardsZero(1);
        }
        return true;
    }

    /**
     * When a Faith Marker reaches (or goes beyond) a Pope space, a Vatican Report occurs. All the players in that Pope space
     * earn 3 victory points.
     * @return false and it does nothing if thirdAudience() method was already called.
     */
    public boolean thirdAudience() {
        for (Player player : players) {
            if (player.getFaithCards()[2] == 0)
                return false;
            if (player.getFaithPoints() >= 19) {
                player.addVictoryPoints(4);
            }
            player.setFaithCardsZero(2);
        }
        return true;
    }

    public Player getWinnerPlayer() {
        return winnerPlayer;
    }

    public int getWinnerPlayerNumber() {
        return WinnerPlayerNumber;
    }

    /**
     * In single player mode, it takes the first action token from action card deck and activates the effect.
     * @return true if it can be done.
     */
    public boolean activateActionCard(){
        if(players.size()!=1 || !currentPlayer.canEndTurn() || actionCardDone) return false;
        actionCardStack.activateCard();
        actionCardDone=true;
        return true;
    }

    /**
     * Makes the player choose 2 leader cards from the 4 leader cards he got when game starts.
     * @param pos1 Position of the first leader card.
     * @param pos2 Position of the second leader card.
     * @return false if the current player has already chosen the two leader cards
     */
    public boolean chooseLeaderCards(int pos1, int pos2){
        if(currentPlayer.hasChosenLeaderCards())
            return false;
        if(currentPlayer.chooseLeaderCards(pos1, pos2)){
            currentPlayer.setChosenLeaderCards(true);
            return true;
        }
        return false;
    }

    public boolean discardLeaderCard(int pos){
        return currentPlayer.discardLeaderCard(pos);
    }

    /**
     * Allows the player to activate the leader card's special ability.
     * @param pos The position of the leaderCard to activate.
     * @return false if the player has already done his move in this turn.
     */
    public boolean activateLeaderCard(int pos){
        return currentPlayer.activateLeaderCard(pos);
    }

    public boolean activateLeaderAbility(int whichLeaderCard){
        return currentPlayer.activateLeaderAbility(whichLeaderCard);
    }

    /**
     * @return True if an end game condition has been reached.
     */
    public boolean isEndGame() {
        return isEndGame;
    }

    /**
     * @return If the game has started.
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isAllAFK() {
        return allAFK;
    }

    public void setAllAFK(boolean allAFK) {
        this.allAFK = allAFK;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}