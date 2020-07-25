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
 */
public class UDPServer  {
    public static void main(String[] args) {
        try (DatagramSocket aSocket = new DatagramSocket(ServerBehavior.PORT)) {
            System.out.println("Starting up standard UDP Server listening on port 2587.....\n");

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
