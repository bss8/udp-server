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
 * https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html#static
 * @author Borislav S. Sabotinov
 */
public interface ServerBehavior {
    //index 2 = direct caller, can be self but abstract may not be instantiated
    int DIRECT_CALLER_IDX = 2;
    int PORT = 2587;

    /**
     * Prints a message to standard output.
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
