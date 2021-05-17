package it.polimi.ingsw.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
            String hostName = "127.0.0.1";
        System.out.println("\u2551");
            int portNumber = 1234;
            //COMMENT FOR FIRST DEBUG
            //String type=args[0];
            String type = "CLI";
            try (
                    Socket socket = new Socket(hostName, portNumber);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                socket.setSoTimeout(15000);
                String userInput;
                //System.out.println(in.readLine());
                ClientPong clientPong = new ClientPong(out);
                Thread t1 = new Thread(clientPong);
                t1.start();
                ServerReader serverReader = new ServerReader(in);
                Thread t = new Thread(serverReader);
                t.start();
                //UNCOMMENT FOR FIRST DEBUG
                /*while (true) {
                    if((userInput = stdIn.readLine()) != null) {
                        out.println(userInput);
                    }
                }
                /*
                */

                if (type.equals("CLI")) {
                    CLI cli = new CLI();
                    serverReader.setCli(cli);
                    Thread thread = new Thread(cli);
                    thread.start();
                    while (true) {
                        if ((userInput = cli.getUserInput()) != null) {
                            if(userInput.equals("Invalid command."))
                                System.out.println("Invalid command. Use help to see all valid commands.");
                            else if(!userInput.equals("Show"))
                                out.println(userInput);
                            cli.setUserInput(null);
                        }
                    }
                } else if (type.equals("GUI")) {
                    //TODO GUI
                }
                else{
                    System.err.println("Wrong config");
                    System.exit(1);
                }
            // UNCOMMENT FOR FIRST DEBUG*/
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + hostName);
                System.exit(1);
            } catch(SocketTimeoutException e){
                System.out.println("Il server non manda più ping");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " +
                        hostName);
                System.exit(1);
            }
    }
}

