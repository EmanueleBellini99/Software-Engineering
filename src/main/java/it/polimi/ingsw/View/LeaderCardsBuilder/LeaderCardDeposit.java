package it.polimi.ingsw.View.LeaderCardsBuilder;
import it.polimi.ingsw.ResourceType;

public class LeaderCardDeposit extends LeaderCards{
    ResourceType resourceType;
    ResourceType resourceRequired;
    String type;
    int leaderCardNumber;
    public LeaderCardDeposit(ResourceType resourceRequired, ResourceType resourceType, int leaderCardNumber) {
        this.resourceType = resourceType;
        this.resourceRequired = resourceRequired;
        this.type = "Deposit";
        this.leaderCardNumber=leaderCardNumber;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    @Override
    public ResourceType getResourceRequired() {
        return resourceRequired;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getLeaderCardNumber() {
        return leaderCardNumber;
    }
}
