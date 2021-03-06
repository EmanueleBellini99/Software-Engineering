package it.polimi.ingsw.View;

import it.polimi.ingsw.View.Printers.*;

import java.util.ArrayList;

/**
 * This class represents the part of the model that all clients have locally.
 * It is used by the cli and the gui for drawing.
 * The various methods are mainly used by the CLI since the GUI mainly takes the data
 */
public class ModelPrinter {
    private MarketBoardPrinter marketBoardPrinter = new MarketBoardPrinter();
    private DeckGridPrinter deckGridPrinter = new DeckGridPrinter();
    private ArrayList<PersonalBoardPrinter> personalBoards = new ArrayList<>();
    private ArrayList<LeaderCardsPrinter> leaderCardsPrinters = new ArrayList<>();
    private int blackFaithPoints = -1;
    private boolean multiplayerGameStarted = false;
    private String currentPlayerNickname;
    private boolean discardedFirstLeader = false;
    private String lastActionToken;


    /**
     * @param p indicates the printer
     * @param whatIHaveToPrint represent the object or data to print
     */
    public void print(Printable p, String whatIHaveToPrint) {
        if (whatIHaveToPrint.equals("blackFaithPoints")) {
            printLorenzoTrack();
        } else {
            p.print(whatIHaveToPrint);
        }
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

    public void printAll() {
        for (int i = 0; i < 10; i++) {
            System.out.print("\n");
        }
        //printo tutto quando cambia la CLI in seguito ad un'azione di un player
    }

    public boolean isMultiplayerGameStarted() {
        return multiplayerGameStarted;
    }

    public void setMultiplayerGameStarted(boolean multiplayerGameStarted) {
        this.multiplayerGameStarted = multiplayerGameStarted;
        System.out.println("Game started in multiplayer mode!");
    }

    private void printLorenzoTrack() {
        System.out.println("Lorenzo's faith track:");
        int i;
        for (i = 0; i <= 166; i++) {
            if (i == 0)
                System.out.print(Colours.RESET + "\u2554");
            else if (i == 166)
                System.out.print(Colours.ANSI_YELLOW.escape() + "\u2557");
            else if ((i >= 1 && i <= 30) || (i >= 55 && i <= 74) || (i >= 110 && i <= 123))
                System.out.print(Colours.RESET + "\u2550");
            else
                System.out.print(Colours.ANSI_YELLOW.escape() + "\u2550");
        }
        System.out.print(Colours.RESET + "\n");
        for (i = 0; i <= 4; i++) {
            if (i == blackFaithPoints)
                System.out.print(Colours.ANSI_RED.escape() + "\u2551  " + "\u271F   ");
            else
                System.out.print(Colours.RESET + "\u2551  " + i + "  ");
        }
        for (; i <= 8; i++) {
            if (i == blackFaithPoints)
                System.out.print(Colours.ANSI_RED.escape() + "\u2551  " + "\u271F   ");
            else
                System.out.print(Colours.ANSI_YELLOW.escape() + "\u2551  " + i + "  ");
        }
        for (; i <= 11; i++) {
            if (i == blackFaithPoints)
                System.out.print(Colours.ANSI_RED.escape() + "\u2551  " + "\u271F   ");
            else
                System.out.print(Colours.RESET + "\u2551  " + i + "  ");
        }
        for (; i <= 16; i++) {
            if (i == blackFaithPoints)
                System.out.print(Colours.ANSI_RED.escape() + "\u2551  " + "\u271F   ");
            else
                System.out.print(Colours.ANSI_YELLOW.escape() + "\u2551  " + i + "  ");
        }
        for (; i <= 18; i++) {
            if (i == blackFaithPoints)
                System.out.print(Colours.ANSI_RED.escape() + "\u2551  " + "\u271F   ");
            else
                System.out.print(Colours.RESET + "\u2551  " + i + "  ");
        }
        for (; i <= 24; i++) {
            if (i == blackFaithPoints)
                System.out.print(Colours.ANSI_RED.escape() + "\u2551  " + "\u271F   ");
            else
                System.out.print(Colours.ANSI_YELLOW.escape() + "\u2551  " + i + "  ");
        }
        System.out.print(Colours.ANSI_YELLOW.escape() + "\u2551" + Colours.RESET + "\n");
        for (i = 0; i <= 166; i++) {
            if (i == 0)
                System.out.print(Colours.RESET + "\u255A");
            else if (i == 166)
                System.out.print(Colours.ANSI_YELLOW.escape() + "\u255D");
            else if ((i >= 1 && i <= 30) || (i >= 55 && i <= 74) || (i >= 110 && i <= 123))
                System.out.print(Colours.RESET + "\u2550");
            else
                System.out.print(Colours.ANSI_YELLOW.escape() + "\u2550");
        }
        System.out.print(Colours.RESET + "\n");
        System.out.println(Colours.ANSI_CYAN.escape() + "VP:                   1                 2                 4                   6                    9                    12                   16                   20");
        System.out.print(Colours.RESET + "\n");
    }

    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }

    public void setCurrentPlayerNickname(String currentPlayerNickname) {
        this.currentPlayerNickname = currentPlayerNickname;
    }

    public boolean hasDiscardedFirst() {
        return discardedFirstLeader;
    }

    public void setHasDiscardedFirstLeader(boolean discardedFirstLeader) {
        this.discardedFirstLeader = discardedFirstLeader;
    }

    public String getLastActionToken() {
        return lastActionToken;
    }

    public void setLastActionToken(String lastActionToken) {
        this.lastActionToken = lastActionToken;
    }
}
