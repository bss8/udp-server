package edu.txstate.bss64;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public interface ServerBehavior {
    static DatagramPacket receiveReplyFromClient(DatagramSocket aSocket) throws IOException {
        // moved declaration and initialization inside while loop to clear buffer
        // to properly display only the current message received from a client.
        // This ensures no new message data is mixed with an old message.
        //byte[] buffer = new byte[1000];
        byte[] buffer = new byte[1000];
        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
        //request = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(request);
        return request;
    }

    static void sendReplyBackToClient(DatagramSocket aSocket, DatagramPacket request, String stringClientData) throws IOException {
        byte[] clientData = stringClientData.getBytes();
        DatagramPacket reply = new DatagramPacket(clientData, clientData.length, request.getAddress(), request.getPort());
        aSocket.send(reply);
    }
}
