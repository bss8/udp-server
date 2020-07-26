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

import java.net.*;
import java.io.*;

/**
 * Basic UDP Server, which will reply to a received message by simply returning it as-is to the client.
 * It is the user's responsibility to specify how this client should be used - single message or iterative mode.
 * This class implements interface ServerBehavior to improve readability and reduce duplication when UDPServerCmdExec
 * is considered.
 */
public class UDPServer implements ServerBehavior {
    public static void main(String... args) {
        try (DatagramSocket aSocket = new DatagramSocket(PORT)) {
            System.out.println("Starting up standard UDP Server listening on port " + PORT + ".....\n");
            while (true) {
                DatagramPacket request = ServerBehavior.receiveReplyFromClient(aSocket);
                String stringClientData = ServerBehavior.displayReceivedMessage(request);
                ServerBehavior.sendReplyBackToClient(aSocket, request, stringClientData);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}
