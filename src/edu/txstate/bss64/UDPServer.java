package edu.txstate.bss64;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Basic UDP Server, which will reply to a received message by simply returning it as-is to the client.
 */
public class UDPServer extends Server implements ServerBehavior {
    public static void main(String[] args) {
        try (DatagramSocket aSocket = new DatagramSocket(2587)) {
            System.out.println("Starting up standard UDP Server listening on port 2587.....\n");

            while (true) {
                DatagramPacket request = ServerBehavior.receiveReplyFromClient(aSocket);
                String stringClientData = displayReceivedMessage(request);
                ServerBehavior.sendReplyBackToClient(aSocket, request, stringClientData);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}
