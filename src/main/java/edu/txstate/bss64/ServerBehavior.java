/*
 * Copyright (c) 2020. Borislav S. Sabotinov
 * https://github.com/bss8
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package edu.txstate.bss64;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

/**
 * This interface defines a number of static helper functions. Intent is to improve code readability and reduce
 * duplication.
 * This way we stay closer to the single-responsibility model of function declaration.
 * https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html#static
 *
 * @author Borislav S. Sabotinov
 */
public interface ServerBehavior {
    //index 2 = direct caller, can be self but abstract may not be instantiated
    int DIRECT_CALLER_IDX = 2;
    int PORT = 2587;

    /**
     * Helper function which prints a message received form a client to standard output.
     * It returns a String representation of the client message
     * @param request message received from a UDP Client
     */
    static String displayReceivedMessage(DatagramPacket request) {
        byte[] clientData = request.getData();
        String stringClientData = new String(clientData, StandardCharsets.UTF_8);
        // determine calling class to provide a more specific message
        String callerClassName = Thread.currentThread().getStackTrace()[DIRECT_CALLER_IDX].getClassName();
        if (callerClassName.contains("CmdExec")) {
            System.out.print("\nrequest to execute cmd from ");
        } else {
            System.out.print("\nmessage received from ");
        }
        //print the rest of the message - client address and received message
        System.out.println(request.getAddress().getHostAddress() + ": " + stringClientData);

        return stringClientData;
    }

    /**
     * Defines how a server should receive a message from a client.
     *
     * @param aSocket a DataGram socket. Each server is responsible for creating their own instance, preferrably
     *                in a try-with-resources block to take advantage of the Closeable interface which DatagramSocket
     *                implements.
     * @return
     * @throws IOException
     */
    static DatagramPacket receiveReplyFromClient(DatagramSocket aSocket) throws IOException {
        // Reset buffer each time we receive a reply, so we can clear previous buffer to properly display only the
        // current message received from a client. This ensures no new message data is mixed with an old message.
        byte[] buffer = new byte[1000];
        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(request);
        return request;
    }

    /**
     * Defines how a server should reply back to the client.
     * @param aSocket Each server is responsible for creating their own instance, preferrably
     *                in a try-with-resources block to take advantage of the Closeable interface which DatagramSocket
     *                implements.
     * @param request The request which was received from the client
     * @param stringClientData String representation of the client data received
     * @throws IOException
     */
    static void sendReplyBackToClient(DatagramSocket aSocket, DatagramPacket request, String stringClientData) throws IOException {
        byte[] clientData = stringClientData.getBytes();
        DatagramPacket reply = new DatagramPacket(clientData, clientData.length, request.getAddress(), request.getPort());
        aSocket.send(reply);
    }
}
