package it.polimi.ingsw.View.MessageBuilders;

import com.google.gson.Gson;
import it.polimi.ingsw.Controller.Messages.SwitchLevelMessage;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Used to build a switch level request
 */
public class SwitchLevelMessageBuilder extends MessageBuilder {
    private final String nickname;
    public SwitchLevelMessageBuilder(String nickname) {
        this.nickname = nickname;
    }

    @Override
        public String buildMessage() {
        int[] levelToSwitch={0,0};
        int firstLevel, secondLevel;
        Gson gson = new Gson();
        Scanner input = new Scanner(System.in);
        SwitchLevelMessage message = new SwitchLevelMessage();
        message.setMessageType("SwitchLevels");
        message.setSenderNickname(nickname);
        do {
            try {
                System.out.println("Write the first level you want to switch: [\"1\",\"2\",\"3\"]");
                firstLevel = input.nextInt();
            }
            catch (InputMismatchException e){
                System.out.println("Please write a number");
                firstLevel=4;
                input.nextLine();
            }
        } while (firstLevel != 1 && firstLevel != 2 && firstLevel != 3);
        levelToSwitch[0]=firstLevel;

        do {
            try {
                System.out.println("Write the second level you want to switch: [\"1\",\"2\",\"3\"]");
                secondLevel = input.nextInt();
            }
            catch (InputMismatchException e){
                System.out.println("Please write a number");
                secondLevel=4;
                input.nextLine();
            }
        } while (secondLevel != 1 && secondLevel != 2 && secondLevel != 3);
        levelToSwitch[1]=secondLevel;

        message.setLevelsToSwitch(levelToSwitch);
        return gson.toJson(message);
    }
}
