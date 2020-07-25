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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServerCmdExec implements ServerBehavior {
    private static String OS;

    public static void main(String[] args) {

        OS = System.getProperty("os.name");
        try (DatagramSocket aSocket = new DatagramSocket(2587)) {
            while (true) {
                System.out.println("Starting up command execution UDP Server on " + InetAddress.getLocalHost() + " listening on port 2587.....\n");
                DatagramPacket request = ServerBehavior.receiveReplyFromClient(aSocket);

                String stringClientData = ServerBehavior.displayReceivedMessage(request);
                stringClientData = runCmd(stringClientData);

                ServerBehavior.sendReplyBackToClient(aSocket, request, stringClientData);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    /**
     *
     * @param cmd command to execute
     * @return
     */
    private static String runCmd(String cmd) {
        StringBuilder output = null;
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (OS.contains("Linux")) {
            // Run a shell command
            processBuilder.command("bash", "-c", cmd.trim());
        } else if (OS.contains("Windows")) {
            // Run a batch command
            processBuilder.command("cmd.exe", "/c", cmd.trim());
        } else {
            throw new UnsupportedOperationException("This terminal and OS are not supported!");
        }

        System.out.println(cmd);
        try {
            Process process = processBuilder.start();
            output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                System.out.println(output);
            } else {
                //abnormal...
                System.err.println("Command Execution Failed!");
                System.out.println(output);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output != null ? output.toString() : null;
    }
}

