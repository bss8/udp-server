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

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author Borislav S. Sabotinov
 * Provides a main entry point for a user to initialize a server of their choice.
 * Can either be standard UDP Server or command execution UDP Server.
 */
public class Main {
    private static String IP_ADDR;
    private static String OS;
    private static String HOST;

    // static init block to get the IP address and OS name of the machine on which client will run
    static {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            IP_ADDR = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        OS = System.getProperty("os.name");
    }

    /**
     * We print instructions to the screen and prompt the user to enter an option code (1-2), regarding
     * what type of server they want to initiate.
     * @param args
     */
    public static void main(String... args) {
        printHelp();
        Scanner scanner = new Scanner(System.in);
        String usrInput = scanner.next();
        if (usrInput.equals("1")) {
            UDPServer.main();
        } else if (usrInput.equals("2")) {
            try {
                UDPServerCmdExec.main();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            printHelp();
            throw new IllegalArgumentException("\nOption code not supported!");
        }
    }

    /**
     * Prints instructions for the user, explaining how to use this program.
     */
    private static void printHelp() {
        System.out.print("Enter 1 to start a standard message server, 2 for command execution server.\n" +
                "java -jar server.jar <OPT_CODE>\n");
    }
}
