package Server;

import Client.Requests.*;
import Server.Responses.ErrorResponse;
import Server.Responses.MessageListResponse;
import Server.Responses.SuccessResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    static class ClientHandler extends Thread {
        // Shared
        private static List<Message> board = new ArrayList<Message>();
        private static ChannelList channels = new ChannelList();
        private static Clock clock = new Clock();


        // Independent to client
        private String userName;
        private int alreadyReadMessages;
        private String userCurrentChannel;
        private Socket clientSocket;
        private PrintWriter serverPrinter;
        private BufferedReader clientReader;

        public ClientHandler(Socket socket) throws IOException {
            clientSocket = socket;
            serverPrinter = new PrintWriter(clientSocket.getOutputStream(), true);
            clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            alreadyReadMessages = 0;
            userName = null;
            userCurrentChannel = null;
        }

        public void subscribe (String channelName, boolean subOrUnsub) {
            // Setting responses
            String returnChannel = "None";
            String message = "unsubscribed from ";
            userCurrentChannel = null;
            if (subOrUnsub) {
                returnChannel = channelName;
                message = "subscribed too ";
                userCurrentChannel = channelName;
            }

            // Subscribe or UnSubscribe
            if (channels.checkIfExists(channelName)) {
                serverPrinter.println(new SuccessResponse(returnChannel));
                serverPrinter.println(new SuccessResponse("You have "+message+channelName));
            } else {
                serverPrinter.println(new ErrorResponse("error").toJSON());
                serverPrinter.println(new ErrorResponse("No channel " + channelName + " exists").toJSON());
            }
        }
        public void serverRequestSwitch(String requestType, Object userRequestJson, long ts) {
            // Options for each request type
            switch (requestType) {
                case "LoginRequest":
                    if (userName != null)
                        break;
                    else {
                        userName = LoginRequest.fromJSON(userRequestJson).getName();
                        serverPrinter.println(new SuccessResponse("Successful Login"));
                    }
                    break;

                case "OpenRequest":
                    if (userName == null) break;
                    if (channels.checkIfExists(userName)) {
                        serverPrinter.println(new SuccessResponse("Channel "+userName+" already exists"));
                        break;
                    } else if (!channels.checkIfExists(userName)) {
                        // Create a channel in the users name, then subscribe the user
                        Channel newUserChannel = new Channel(userName);
                        channels.addChannel(newUserChannel);
                        serverPrinter.println(new SuccessResponse("Channel "+userName+" created successfully"));
                        break;
                    }
                    serverPrinter.println(new ErrorResponse("An unknown error has occured, please try again"));
                    break;

                case "PublishRequest":
                    if (userName == null) break;
                    String requestedChannel = PublishRequest.fromJSON(userRequestJson).getChannel();
                    if (userCurrentChannel == null || !userCurrentChannel.equals(requestedChannel)) {
                        serverPrinter.println(new ErrorResponse("Must be subscribed to a channel to post a message"));
                        break;
                    }

                    // Check if message is too long
                    Message newMessage = PublishRequest.fromJSON(userRequestJson).getMessage();
                    if (newMessage.getBody().length() > 50) {
                        serverPrinter.println(new ErrorResponse("Message body is too large"));
                        break;
                    }

                    // synchronized access to the shared message board
                    synchronized (ClientHandler.class) {
                        Channel current = channels.getChannel(userCurrentChannel);
                        // Override with server timestamp
                        current.addMessage(new Message(newMessage.getBody(), newMessage.getAuthor(), ts));
                    }
                    serverPrinter.println(new SuccessResponse("Message published to channel "+userCurrentChannel));
                    break;

                case "SubscribeRequest":
                    // Handles Subscribing and UnSubscribing
                    if (userName == null) break;
                    if (SubscribeRequest.fromJSON(userRequestJson).getSubOrUnsub())
                        subscribe(SubscribeRequest.fromJSON(userRequestJson).getChannelName(), true);
                    else
                        subscribe(SubscribeRequest.fromJSON(userRequestJson).getChannelName(), false);
                    break;

                case "GetRequest":
                    if (userName == null || userCurrentChannel == null) {
                        serverPrinter.println(new ErrorResponse("Please enter a channel that exists"));
                        break;
                    }
                    long smallest = GetRequest.fromJSON(userRequestJson).getAfterTime();
                    List<Message> currentMessages;

                    // Has a read message count which increments as the user reads messages
                    if (smallest < 0) {
                        // synchronized access to the shared message board
                        synchronized (ClientHandler.class) {
                            Channel current = channels.getChannel(userCurrentChannel);
                            currentMessages = current.getChannelMessages().subList(alreadyReadMessages, current.getChannelMessages().size());
                            alreadyReadMessages = current.getChannelMessages().size();
                        }
                    } else {
                        // User specifies a time stamp for messages to start at
                        // synchronized access to the shared message board
                        synchronized (ClientHandler.class) {
                            Channel current = channels.getChannel(userCurrentChannel);
                            currentMessages = current.getChannelMessages(smallest);
                            alreadyReadMessages = current.getChannelMessages().size();
                        }
                    }

                    serverPrinter.println(new MessageListResponse(currentMessages));
                    break;

                case "QuitRequest":
                    if (userName == null) break;
                    serverPrinter.println(new SuccessResponse("Goodbye "+userName+", see you soon :)"));
                    return;

                default:
                    // error: illegal request
                    serverPrinter.println(new ErrorResponse("Illegal request").toJSON());
                    break;
            }
        }

        public void run() {
            try {
                channels.read();
                System.out.println("*** A new connection has been made, waiting for login");
                String clientInputRequest;
                while ((clientInputRequest = clientReader.readLine()) != null) {

                    // Creating a time stamp that increments each loop
                    long ts = clock.tick();

                    // Display request server side, and which user if logged in
                    if (userName != null)
                        System.out.println(userName + ": " + clientInputRequest);
                    else
                        System.out.println(clientInputRequest);

                    // Check the user request type
                    Object userRequestJson = JSONValue.parse(clientInputRequest);
                    String requestType = (String) ((JSONObject) userRequestJson).get("_class");

                    // Respond to user requests
                    serverRequestSwitch(requestType, userRequestJson, ts);
                }
                channels.save();
                System.out.println("*** A connection with "+userName+" has been terminated");
                clientReader.close();
                serverPrinter.close();
            } catch (IOException e) {
                System.out.println("Exception while connected");
                System.out.println(e.getMessage());
            }
        }
    }


    public static void main(String[] args) {

        int portNumber = 12345;

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("Exception listening for connection on port " + portNumber);
            System.out.println(e.getMessage());
        }
    }
}
