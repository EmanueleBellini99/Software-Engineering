package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
class PersonalBoardTest {

    @Test
    void insertCard() throws FileNotFoundException {
        Deckgrid deckgrid = new Deckgrid();
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        assertTrue(personalBoard.insertResources(ResourceType.SHIELDS, 3, 3));
        personalBoard.getStrongbox().addResources(ResourceType.SHIELDS, 1);
        assertFalse(personalBoard.insertCard(deckgrid.getCards().get(16), 0));
        personalBoard.getStrongbox().addResources(ResourceType.COINS, 3);
        assertTrue(personalBoard.insertCard(deckgrid.getCards().get(10), 0));
        assertTrue(personalBoard.insertCard(deckgrid.getCards().get(16), 0));
        assertTrue( personalBoard.insertResources(ResourceType.COINS, 1, 1) );
        assertTrue( personalBoard.insertResources(ResourceType.SHIELDS, 2, 2) );
        assertTrue( personalBoard.insertResources(ResourceType.STONES, 3, 1) );
        assertTrue( personalBoard.insertCard(deckgrid.getCards().get(7), 1) );
    }

    @Test
    void insertResourcesAndSwitchLevelsTest() {
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        assertFalse(personalBoard.insertResources(ResourceType.SHIELDS, 1, 2));
        assertTrue(personalBoard.insertResources(ResourceType.SHIELDS, 1, 1));
        assertFalse(personalBoard.insertResources(ResourceType.COINS, 2, 3));
        assertFalse(personalBoard.insertResources(ResourceType.SHIELDS, 2, 1));
        assertFalse(personalBoard.insertResources(ResourceType.SERVANTS, 3, 4));
        assertTrue(personalBoard.insertResources(ResourceType.COINS, 3, 1));
        assertTrue(personalBoard.insertResources(ResourceType.COINS, 3, 2));
        assertFalse(personalBoard.insertResources(ResourceType.COINS, 3, 3));
        assertTrue(personalBoard.insertResources(ResourceType.STONES, 2, 1));
        assertTrue(personalBoard.switchLevels(1,2));
        assertEquals(ResourceType.STONES,personalBoard.getWarehouseDepot().getLevel(1).getResourceType());
        assertEquals(ResourceType.SHIELDS,personalBoard.getWarehouseDepot().getLevel(2).getResourceType());
        assertEquals(ResourceType.COINS,personalBoard.getWarehouseDepot().getLevel(3).getResourceType());
    }


    @Test
    void activateProductionFromDevCard() throws FileNotFoundException {
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        Deckgrid deckgrid = new Deckgrid();
        assertTrue( personalBoard.insertResources(ResourceType.COINS, 1, 1) );
        assertTrue( personalBoard.insertResources(ResourceType.SHIELDS, 2, 2) );
        assertTrue( personalBoard.insertResources(ResourceType.STONES, 3, 1) );
        assertTrue( personalBoard.insertCard(deckgrid.getCards().get(7), 0) );
        assertTrue(personalBoard.activateProductionFromDevCard(0));
        assertFalse(personalBoard.activateProductionFromDevCard(1));
        personalBoard.getStrongbox().addResources(ResourceType.SHIELDS, 2);
        personalBoard.getStrongbox().addResources(ResourceType.COINS, 1);
        assertTrue( personalBoard.insertCard(deckgrid.getCards().get(0), 1) );
        assertTrue(personalBoard.activateProductionFromDevCard(1));
        assertFalse(personalBoard.activateProductionFromDevCard(1));
        assertFalse(personalBoard.activateProductionFromDevCard(0));
        assertFalse(personalBoard.activateProductionFromDevCard(2));
        assertTrue( personalBoard.insertResources(ResourceType.SHIELDS, 2, 2) );
        personalBoard.getStrongbox().addResources(ResourceType.SHIELDS, 2);
        assertTrue(personalBoard.insertCard(deckgrid.getCards().get(16), 0));
        assertFalse(personalBoard.activateProductionFromDevCard(0));
        assertTrue( personalBoard.insertResources(ResourceType.STONES, 1, 1) );
        assertTrue(personalBoard.activateProductionFromDevCard(0));
    }

    @Test
    void chekResourcesIntoWarehouse(){
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        assertTrue( personalBoard.insertResources(ResourceType.COINS, 1, 1) );
        assertTrue( personalBoard.checkResourcesIntoWarehouse(ResourceType.COINS, 1));
        assertTrue( personalBoard.insertResources(ResourceType.SHIELDS, 2, 1) );
        assertTrue( personalBoard.checkResourcesIntoWarehouse(ResourceType.SHIELDS, 1));
        assertTrue( personalBoard.insertResources(ResourceType.SERVANTS, 3, 1) );
        assertTrue( personalBoard.checkResourcesIntoWarehouse(ResourceType.SERVANTS, 1));
        assertTrue( personalBoard.checkResourcesIntoWarehouse(ResourceType.COINS, 1));
        assertFalse( personalBoard.checkResourcesIntoWarehouse(ResourceType.COINS, 2));
        assertFalse( personalBoard.checkResourcesIntoWarehouse(ResourceType.STONES, 1));
    }

    @Test
    void checkResourcesIntoStrongbox(){
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        personalBoard.getStrongbox().addResources(ResourceType.COINS, 1);
        assertTrue( personalBoard.checkResourcesIntoStrongbox(ResourceType.COINS, 1));
        personalBoard.getStrongbox().addResources(ResourceType.SHIELDS, 1);
        assertTrue( personalBoard.checkResourcesIntoStrongbox(ResourceType.SHIELDS, 1));
        personalBoard.getStrongbox().addResources(ResourceType.SERVANTS, 1);
        assertTrue( personalBoard.checkResourcesIntoStrongbox(ResourceType.SERVANTS, 1));
        assertTrue( personalBoard.checkResourcesIntoStrongbox(ResourceType.SERVANTS, 1));
        assertTrue( personalBoard.checkResourcesIntoStrongbox(ResourceType.SERVANTS, 1));
        assertTrue( personalBoard.checkResourcesIntoStrongbox(ResourceType.SERVANTS, 1));
        assertTrue( personalBoard.checkResourcesIntoStrongbox(ResourceType.COINS, 1));
        assertFalse( personalBoard.checkResourcesIntoStrongbox(ResourceType.COINS, 2));
        assertFalse( personalBoard.checkResourcesIntoStrongbox(ResourceType.STONES, 1));
    }


    @Test
    void takeResourcesFromStrongbox(){
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        personalBoard.getStrongbox().addResources(ResourceType.COINS, 1);
        assertTrue( personalBoard.takeResourcesFromStrongbox(ResourceType.COINS, 1));
        personalBoard.getStrongbox().addResources(ResourceType.SHIELDS, 1);
        assertTrue( personalBoard.takeResourcesFromStrongbox(ResourceType.SHIELDS, 1));
        personalBoard.getStrongbox().addResources(ResourceType.SERVANTS, 1);
        assertTrue( personalBoard.takeResourcesFromStrongbox(ResourceType.SERVANTS, 1));
        personalBoard.getStrongbox().addResources(ResourceType.COINS, 1);
        assertTrue( personalBoard.takeResourcesFromStrongbox(ResourceType.COINS, 1));
        assertFalse( personalBoard.takeResourcesFromStrongbox(ResourceType.COINS, 2));
        assertFalse( personalBoard.takeResourcesFromStrongbox(ResourceType.STONES, 1));
    }

    @Test
    void takeResourcesFromWarehouse(){
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        personalBoard.insertResources(ResourceType.COINS, 1, 1);
        assertTrue( personalBoard.takeResourcesFromWarehouse(ResourceType.COINS, 1));
        personalBoard.insertResources(ResourceType.SHIELDS, 1, 1);
        assertTrue( personalBoard.takeResourcesFromWarehouse(ResourceType.SHIELDS, 1));
        personalBoard.insertResources(ResourceType.SERVANTS, 1, 1);
        assertTrue( personalBoard.takeResourcesFromWarehouse(ResourceType.SERVANTS, 1));
        personalBoard.insertResources(ResourceType.COINS, 1, 1);
        assertTrue( personalBoard.takeResourcesFromWarehouse(ResourceType.COINS, 1));
        assertFalse( personalBoard.takeResourcesFromWarehouse(ResourceType.COINS, 2));
        assertFalse( personalBoard.takeResourcesFromWarehouse(ResourceType.STONES, 1));
    }

    @Test
    void missingResourcesIntoWarehouse(){
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        assertTrue(personalBoard.insertResources(ResourceType.SHIELDS, 3, 3));
        personalBoard.getStrongbox().addResources(ResourceType.SHIELDS, 1);
        assertEquals( 1, personalBoard.missingResourcesIntoWarehouse(ResourceType.SHIELDS, 4));
        assertEquals( 4, personalBoard.missingResourcesIntoWarehouse(ResourceType.SHIELDS, 4));
        assertTrue( personalBoard.insertResources(ResourceType.COINS, 1, 1) );
        assertEquals( 0, personalBoard.missingResourcesIntoWarehouse(ResourceType.COINS, 1));
        personalBoard.insertResources(ResourceType.SHIELDS, 1, 1);
        assertEquals( 0, personalBoard.missingResourcesIntoWarehouse(ResourceType.SHIELDS, 1));
        personalBoard.insertResources(ResourceType.SERVANTS, 1, 1);
        assertEquals( 0, personalBoard.missingResourcesIntoWarehouse(ResourceType.SERVANTS, 1));
        personalBoard.insertResources(ResourceType.COINS, 1, 1);
        assertEquals( 0, personalBoard.missingResourcesIntoWarehouse(ResourceType.COINS, 1));
        assertEquals( 2, personalBoard.missingResourcesIntoWarehouse(ResourceType.COINS, 2));
        assertEquals(1,  personalBoard.missingResourcesIntoWarehouse(ResourceType.STONES, 1));
        personalBoard.insertResources(ResourceType.STONES, 3, 3);
        assertEquals( 0, personalBoard.missingResourcesIntoWarehouse(ResourceType.STONES, 2));
        assertEquals( 9, personalBoard.missingResourcesIntoWarehouse(ResourceType.STONES, 10));
    }

    @Test
    void missingResourcesIntoWarehouseWithoutRemove(){
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        assertTrue(personalBoard.insertResources(ResourceType.SHIELDS, 3, 3));
        personalBoard.getStrongbox().addResources(ResourceType.SHIELDS, 1);
        assertEquals( 1, personalBoard.missingResourcesIntoWarehouseWithoutRemove(ResourceType.SHIELDS, 4));
        assertEquals( 1, personalBoard.missingResourcesIntoWarehouseWithoutRemove(ResourceType.SHIELDS, 4));
    }

    @Test
    void finishProduction() throws FileNotFoundException {
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        Deckgrid deckgrid = new Deckgrid();
        assertTrue( personalBoard.insertResources(ResourceType.COINS, 1, 1) );
        assertTrue( personalBoard.insertResources(ResourceType.SHIELDS, 2, 2) );
        assertTrue( personalBoard.insertResources(ResourceType.STONES, 3, 1) );
        assertTrue( personalBoard.insertCard(deckgrid.getCards().get(7), 0) );
        assertTrue(personalBoard.activateProductionFromDevCard(0)); //earned 1 COINS
        assertFalse(personalBoard.activateProductionFromDevCard(1));
        personalBoard.getStrongbox().addResources(ResourceType.SHIELDS, 2);
        personalBoard.getStrongbox().addResources(ResourceType.COINS, 1);
        assertTrue( personalBoard.insertCard(deckgrid.getCards().get(0), 1) );
        assertTrue(personalBoard.activateProductionFromDevCard(1)); //earned 1 FAITHPOINTS
        assertFalse(personalBoard.activateProductionFromDevCard(1));
        assertFalse(personalBoard.activateProductionFromDevCard(0));
        assertFalse(personalBoard.activateProductionFromDevCard(2));
        assertTrue( personalBoard.insertResources(ResourceType.SHIELDS, 2, 2) );
        personalBoard.getStrongbox().addResources(ResourceType.SHIELDS, 2);
        assertTrue(personalBoard.insertCard(deckgrid.getCards().get(16), 0));
        assertFalse(personalBoard.activateProductionFromDevCard(0));
        assertTrue( personalBoard.insertResources(ResourceType.STONES, 1, 1) );
        assertTrue(personalBoard.activateProductionFromDevCard(0));//earned 2 FAITHPOINTS
        personalBoard.getStrongbox().addResources(ResourceType.COINS, 2);
        personalBoard.getStrongbox().addResources(ResourceType.SHIELDS, 2);
        personalBoard.getStrongbox().addResources(ResourceType.SERVANTS, 2);
        assertTrue( personalBoard.insertCard(deckgrid.getCards().get(5), 2) );
        assertTrue(personalBoard.activateProductionFromDevCard(2));//earned 1 SHIELDS
        personalBoard.getStrongbox().removeResources(ResourceType.SHIELDS, personalBoard.getStrongbox().getNumOf(ResourceType.SHIELDS));
        personalBoard.getStrongbox().removeResources(ResourceType.COINS,personalBoard.getStrongbox().getNumOf(ResourceType.COINS));
        personalBoard.getStrongbox().removeResources(ResourceType.STONES,personalBoard.getStrongbox().getNumOf(ResourceType.STONES));
        personalBoard.getStrongbox().removeResources(ResourceType.SERVANTS,personalBoard.getStrongbox().getNumOf(ResourceType.SERVANTS));
        personalBoard.fromStrongboxTempToStrongbox();
        assertEquals(1, personalBoard.getStrongbox().getNumOf(ResourceType.COINS) );
        assertEquals(1, personalBoard.getStrongbox().getNumOf(ResourceType.SHIELDS) );
        assertEquals(0, personalBoard.getStrongbox().getNumOf(ResourceType.SERVANTS) );
        assertEquals(0, personalBoard.getStrongbox().getNumOf(ResourceType.STONES) );
        personalBoard.getStrongbox().removeResources(ResourceType.SHIELDS, personalBoard.getStrongbox().getNumOf(ResourceType.SHIELDS));
        personalBoard.getStrongbox().removeResources(ResourceType.COINS,personalBoard.getStrongbox().getNumOf(ResourceType.COINS));
        personalBoard.getStrongbox().removeResources(ResourceType.STONES,personalBoard.getStrongbox().getNumOf(ResourceType.STONES));
        personalBoard.getStrongbox().removeResources(ResourceType.SERVANTS,personalBoard.getStrongbox().getNumOf(ResourceType.SERVANTS));
        personalBoard.getStrongbox().addResources(ResourceType.COINS, 1);
        assertTrue(personalBoard.activateProductionFromDevCard(2));//earned 1 SHIELDS
        personalBoard.fromStrongboxTempToStrongbox();
        assertEquals(0, personalBoard.getStrongbox().getNumOf(ResourceType.COINS) );
        assertEquals(1, personalBoard.getStrongbox().getNumOf(ResourceType.SHIELDS) );
        assertEquals(0, personalBoard.getStrongbox().getNumOf(ResourceType.SERVANTS) );
        assertEquals(0, personalBoard.getStrongbox().getNumOf(ResourceType.STONES) );
    }

    @Test
    void activateProductionFromPersonalBoard(){
        Player player = new Player();
        PersonalBoard personalBoard = new PersonalBoard(player);
        assertTrue( personalBoard.insertResources(ResourceType.COINS, 1, 1) );
        assertTrue( personalBoard.insertResources(ResourceType.SHIELDS, 2, 2) );
        assertTrue( personalBoard.activateProductionFromPersonalBoard(ResourceType.COINS, ResourceType.SHIELDS, ResourceType.STONES) );
        personalBoard.fromStrongboxTempToStrongbox();
        assertEquals(0, personalBoard.getStrongbox().getNumOf(ResourceType.COINS) );
        assertEquals(0, personalBoard.getStrongbox().getNumOf(ResourceType.SHIELDS) );
        assertEquals(0, personalBoard.getStrongbox().getNumOf(ResourceType.SERVANTS) );
        assertEquals(1, personalBoard.getStrongbox().getNumOf(ResourceType.STONES) );
        assertTrue( personalBoard.insertResources(ResourceType.STONES, 3, 2) );
        assertTrue( personalBoard.activateProductionFromPersonalBoard(ResourceType.STONES, ResourceType.STONES, ResourceType.COINS) );
        personalBoard.fromStrongboxTempToStrongbox();
        assertEquals(1, personalBoard.getStrongbox().getNumOf(ResourceType.COINS) );
        assertEquals(0, personalBoard.getStrongbox().getNumOf(ResourceType.SHIELDS) );
        assertEquals(0, personalBoard.getStrongbox().getNumOf(ResourceType.SERVANTS) );
        assertEquals(1, personalBoard.getStrongbox().getNumOf(ResourceType.STONES) );


    }
}