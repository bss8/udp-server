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
 */
public class Main {
    private static String IP_ADDR;
    private static String OS;
    private static String HOST;

    static {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            IP_ADDR = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        OS = System.getProperty("os.name");
    }

    public static void main(String... args) {
        printHelp();
        Scanner scanner = new Scanner(System.in);
        String usrInput = scanner.next();
        if (usrInput.equals("1")) {
            UDPServer.main();
        } else if (usrInput.equals("2")) {

        } else {
            printHelp();
            throw new IllegalArgumentException("\nOption code not supported!");
        }
    }

    private static void printHelp() {
        System.out.print("Enter 1 to start a standard message server, 2 for command execution server.\n" +
                "java -jar server.jar <OPT_CODE>\n");
    }
}
