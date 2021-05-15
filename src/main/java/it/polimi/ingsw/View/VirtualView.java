package it.polimi.ingsw.View;

import it.polimi.ingsw.View.Printers.*;

import java.util.ArrayList;

public class VirtualView {
    private MarketBoardPrinter marketBoardPrinter = new MarketBoardPrinter();
    private DeckGridPrinter deckGridPrinter = new DeckGridPrinter();
    private ArrayList<PersonalBoardPrinter> personalBoards = new ArrayList<>();
    private ArrayList<LeaderCardsPrinter> leaderCardsPrinters = new ArrayList<>();
    private int blackFaithPoints;

    public void print(Printable p){
        p.print();
    }


    public MarketBoardPrinter getMarketBoardPrinter() {
        return marketBoardPrinter;
    }

    public DeckGridPrinter getDeckGridPrinter() {
        return deckGridPrinter;
    }

    public ArrayList<PersonalBoardPrinter> getPersonalBoards() {
        return personalBoards;
    }

    public ArrayList<LeaderCardsPrinter> getLeaderCardsPrinters() {
        return leaderCardsPrinters;
    }

    public int getBlackFaithPoints() {
        return blackFaithPoints;
    }

    public void setBlackFaithPoints(int blackFaithPoints) {
        this.blackFaithPoints = blackFaithPoints;
    }

    public void printAll(){
        for(int i=0;i<10;i++){
            System.out.print("\n");
        }
        //printo tutto quando cambia la CLI in seguito ad un'azione di un player
    }
}
