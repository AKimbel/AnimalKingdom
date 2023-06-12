/*
Aaron Kimbel
ICS4U0-C
Final Project
Animal Kingdom: Card Arena
Controls Networking connections for the server
*/

package src.networking;

import src.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Random;

public class ServerNetworkHandler extends Thread{

    private ServerSocket serverSocket;
    private Socket connectedSocket;
    private final int port;
    private DataOutputStream dOut;

    public ServerNetworkHandler(int port) {
        super();
        this.port = port;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("waiting");
            connectedSocket = serverSocket.accept();
            System.out.println("CONNECTED!");
            dOut = new DataOutputStream(connectedSocket.getOutputStream());
            boolean shouldStart = new Random().nextBoolean();
            PanelManager.start(shouldStart,false);
            dOut.writeBoolean(!shouldStart);
            dOut.flush();


        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(0);
        }
    }

    public void closeSocket() {
        try {
            if(!connectedSocket.isClosed()) connectedSocket.close();
            if(!serverSocket.isClosed()) connectedSocket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void sendCards() {
        for(PlayerCardPanel card : PanelManager.player.PlayerPlayedCards) {
            try {
                if (card != null) {
                    dOut.writeUTF(card.getCard().getName());
                } else {
                    dOut.writeUTF("null");
                }
                dOut.flush();
            } catch (IOException ignored) {}
        }
    }

    public Card[] receiveCards() {
        Card[] cards = new Card[5];
        try {
            DataInputStream dIn = new DataInputStream(connectedSocket.getInputStream());
            for(int x = 0; x < cards.length; x++) {
                String in = dIn.readUTF();
                if(!in.equals("null")) {
                    cards[x] = CardDeck.findCard(in);
                    CardDeck.removeCard(in);
                } else {
                    cards[x] = null;
                }
            }
        } catch (IOException ignored) {}
        return cards;
    }
}
