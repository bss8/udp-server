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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UDPServerCmdExec implements ServerBehavior {
    private static String OS;

    public static void main(String... args) throws UnknownHostException {
        OS = System.getProperty("os.name");
        System.out.println("Starting up command execution UDP Server at " + InetAddress.getLocalHost() +
                " running on " + OS + " listening on port 2587.....\n");
        try (DatagramSocket aSocket = new DatagramSocket(PORT)) {
            while (true) {
                DatagramPacket request = ServerBehavior.receiveReplyFromClient(aSocket);
                String stringClientData = ServerBehavior.displayReceivedMessage(request);
                String[] decomposedCommand = stringClientData.split("\\s");
                boolean isValidCmd;
                if (OS.contains("Windows")) {
                    isValidCmd = validateWindowsCmd(decomposedCommand[0]);
                } else if (OS.contains("Linux")) {
                    isValidCmd = validateLinuxCmd(decomposedCommand[0]);
                } else {
                    throw new UnsupportedOperationException("OS type is not supported!");
                }

                if (isValidCmd) {
                    System.out.println("Command validated....");
                    stringClientData = runCmd(stringClientData);
                    ServerBehavior.sendReplyBackToClient(aSocket, request, stringClientData);
                } else {
                    System.out.println("Server refused to execute, command is not valid or not supported on this OS!");
                    ServerBehavior.sendReplyBackToClient(aSocket, request, "This server is running on an OS which does not support this command!");
                }
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    /**
     * @param cmd
     * @return
     */
    private static boolean validateLinuxCmd(String cmd) {
        return readCmdFile(cmd, "resources/linux_commands.txt");
    }

    /**
     * @param cmd
     * @return
     */
    private static boolean validateWindowsCmd(String cmd) {
        return readCmdFile(cmd, "resources/windows_commands.txt");
    }

    /**
     * try-with-resources block makes use of superclass Reader's implementation of the Closeable interface
     * FileReader extends InputStreamReader, which in turn extends Reader
     * BufferedReader extends Reader directly
     *
     * @param cmd
     * @param allowedCmdFile
     * @return
     */
    private static boolean readCmdFile(String cmd, String allowedCmdFile) {
        boolean isValidCmd = false;
        try (FileReader fileReader = new FileReader(allowedCmdFile); // Throws FileNotFoundException
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {  // throws IOException, which is higher than FileNotFound
                if (line.equals(cmd)) {
                    isValidCmd = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return isValidCmd;
    }

    /**
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

