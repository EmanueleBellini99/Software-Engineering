package it.polimi.ingsw.MarbleTypes;

import it.polimi.ingsw.MarketBoard;
import it.polimi.ingsw.Selectable;

/**
 * A type of marble that gives
 * FaithPoints to the player
 * who selects it.
 */
public class FaithMarbleType implements Selectable {

    /**
     * Public method overrode from Selectable.
     * Called by the marble, gives 1 FaithPoint to the
     * player selecting this marble.
     */
    @Override
    public void onSelection(MarketBoard marketBoard) {
        marketBoard.getGame().getCurrentPlayer().addFaithPointsAndCallAudience(1);
    }
}