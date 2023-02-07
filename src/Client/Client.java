package Client;

import Client.Requests.LoginRequest;
import Server.Clock;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import Client.Requests.*;
import Server.Message;
import Server.Responses.MessageListResponse;
import Server.Responses.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    public static String userName = null;
    public static String currentChannel = "None";
    private static Clock clock = new Clock();


    public static void userLogin(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Welcome to the Messaging Application\n"
                + "Please provide login name:");
        userName = in.readLine();
        out.println(new LoginRequest(userName));
    }

    public static void printMenu() {
        System.out.println("\nHello " + userName + ",\n"
                + "what would you like to do today?\n\n"
                + "1 : Creates a channel under your name\n"
                + "2 : Write a message to current channel\n"
                + "3 : Subscribe to a channel\n"
                + "4 : Unsubscribe from a channel\n"
                + "5 : Retrieve all messages in current channel\n"
                + "6 : Reprint the menu\n"
                + "9 : Log out");
    }

    public static void subscribe(
            boolean subOrUnsub,
            BufferedReader serverReader,
            PrintWriter serverWriter,
            BufferedReader clientReader
    ) throws IOException {

        String message = "unsubscribe from:";
        if (subOrUnsub)
            message = "subscribe to:";

        System.out.println("Please enter a channel name to "+message);
        serverWriter.println(new SubscribeRequest(clientReader.readLine(), subOrUnsub));
        String serverMessage = serverResponse(serverReader);
        if (!serverMessage.equals("error"))
            currentChannel = serverMessage;
    }

    public static String serverResponse(BufferedReader serverReader) throws IOException {
        // Read server response; terminate if null (i.e. server quit)
        String serverResponse;
        if ((serverResponse = serverReader.readLine()) == null)
            return null;

        // Parse JSON response, then try to deserialize JSON
        Object serverResponseJson = JSONValue.parse(serverResponse);
        String responseType = (String) ((JSONObject) serverResponseJson).get("_class");
        String responseString = null;

        switch (responseType) {
            case "SuccessResponse":
                responseString = SuccessResponse.fromJSON(serverResponseJson).getSuccess();
                break;

            case "MessageListResponse":
                if (MessageListResponse.fromJSON(serverResponseJson) != null) {
                    for (Message messages : MessageListResponse.fromJSON(serverResponseJson).getMessages()) {
                        System.out.println(messages);
                    }
                    responseString = "Displayed up to date messages";
                } else {
                    responseString = "No messages to display";
                }
                break;

            case "ErrorResponse":
                responseString = ErrorResponse.fromJSON(serverResponseJson).getError();
                break;

            default:
                // Not any known response
                responseString = "PANIC: " + serverResponse + " parsed as " + serverResponseJson;
                break;
        }

        return responseString;
    }

    public static void main(String[] args) throws IOException {

        String hostName = "localhost";
        int portNumber = 12345;

        try (
                Socket serverSocket = new Socket(hostName, portNumber);
                PrintWriter serverWriter = new PrintWriter(serverSocket.getOutputStream(), true);
                BufferedReader serverReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            // User login
            if (userName == null) {
                userLogin(serverWriter, clientReader);
                System.out.println("Server: "+serverResponse(serverReader)
                        + "\n---------------------------------------------------");
            }
            // Display menu and user input
            printMenu();
            while (userName != null) {
                // Creating a time stamp that increments each loop
                long ts = clock.tick();
                // User selects an option, which generates the according request
                System.out.println("\n---------------------------------------------------"
                        + "\nYour current Channel: " + currentChannel
                        + "\nPlease choose an option:");
                int userOption = 0;
                try {
                    userOption = new Scanner(System.in).nextInt();
                    switch (userOption) {
                        case 1:
                            // Open
                            serverWriter.println(new OpenRequest(userName));
                            System.out.println("Server: "+serverResponse(serverReader));
                            // Subscribe to own channel
                            serverWriter.println(new SubscribeRequest(userName, true));
                            currentChannel = serverResponse(serverReader);
                            break;
                        case 2:
                            // Publish
                            System.out.println("Please enter a message");
                            serverWriter.println(new PublishRequest(currentChannel, clientReader.readLine(), userName, ts));
                            break;
                        case 3:
                            // Subscribe
                            subscribe(true, serverReader, serverWriter, clientReader);
                            break;
                        case 4:
                            // UnSubscribe
                            subscribe(false, serverReader, serverWriter, clientReader);
                            break;
                        case 5:
                            // Get
                            System.out.println("Would you like to only get messages after a specific timestamp?\n"
                                    + "1) Yes\n"
                                    + "2) No\n");
                            userOption = new Scanner(System.in).nextInt();
                            if (userOption == 2)
                                serverWriter.println(new GetRequest());
                            else if (userOption == 1) {
                                System.out.println("Please insert a number:");
                                serverWriter.println(new GetRequest(new Scanner(System.in).nextLong()));
                            }
                            break;
                        case 6:
                            // Reprint menu
                            printMenu();
                            break;
                        case 9:
                            // Quit
                            serverWriter.println(new QuitRequest());
                            System.out.println("Server: "+serverResponse(serverReader));
                            System.out.println("Logging out...");
                            return;
                        default:
                            System.out.println("Oops, That was not an option, try again");
                            continue;
                    }
                } catch (NoSuchElementException e) {
                    System.out.println(e.getMessage());
                }

                // Display to client server message
                System.out.println("Server: "+serverResponse(serverReader));
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't connect to the server " + hostName);
            System.exit(1);
        }
    }
}
