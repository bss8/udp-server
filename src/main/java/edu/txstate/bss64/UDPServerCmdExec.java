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

import java.io.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;


/**
 * @author Borislav S. Sabotinov
 * Command execution UDP server. Implements ServerBehavior, which defines standard server behavior such as receiving
 * messages from a client and sending a reply.
 */
public class UDPServerCmdExec implements ServerBehavior {
    /**
     * @param args not used
     * @throws UnknownHostException
     */
    public static void main(String... args) throws UnknownHostException {
        System.out.println("Starting up command execution UDP Server at " + InetAddress.getLocalHost() +
                " running on " + OS + " listening on port " + PORT + ".....\n");
        try (DatagramSocket aSocket = new DatagramSocket(PORT)) {
            while (true) {
                DatagramPacket request = ServerBehavior.receiveReplyFromClient(aSocket);
                String stringClientData = ServerBehavior.displayReceivedMessage(request);
                String[] decomposedCommand = stringClientData.contains(" ") ?
                        stringClientData.split("\\s") :
                        new String[]{stringClientData};  // separate cmd by space delimiter

                boolean isValidCmd = validateCommand(decomposedCommand[0]);

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
     * Validate the command received from the client using the OS specific resource files.
     * resources/linux_commands.txt and resources/windows_commands.txt files contain lists of
     * available commands that can be performed by the respective OS. If the user's command is in the list,
     * server can attempt to execute it, otherwise not.
     *
     * @param firstCommandComponent we only need the first part of the client provided command, not the argument,
     *                              to determine if the OS supports it.
     * @return true if command is available on the server's OS, false otherwise.
     */
    private static boolean validateCommand(String firstCommandComponent) {
        boolean isValidCmd;
        if (OS.contains("Windows")) {
            isValidCmd = determineIfCmdIsValid(firstCommandComponent, "windows_commands.txt");
        } else if (OS.contains("Linux")) {
            isValidCmd = determineIfCmdIsValid(firstCommandComponent, "linux_commands.txt");
        } else {
            throw new UnsupportedOperationException("OS type is not supported!");
        }
        return isValidCmd;
    }

    /**
     * If the server is running on a Linux OS, attempt to validate the command using the Linux list
     * getClass().getResource() searches relative to the .class file, while
     * getClass().getClassLoader().getResource() searches relative to the classpath root.
     *
     * @param cmd first part of the client provided command
     * @return true if cmd is valid, false otherwise
     */
    private static boolean determineIfCmdIsValid(String cmd, String resourceFileForOS) {
        ClassLoader classLoader = UDPServerCmdExec.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceFileForOS);

        return readCmdFile(cmd, inputStream);
    }

    /**
     * try-with-resources block makes use of superclass Reader's implementation of the Closeable interface
     * FileReader extends InputStreamReader, which in turn extends Reader
     * BufferedReader extends Reader directly
     * But we cannot use a file directly: "If you need to read file content in JARs, you can not use File class directly."
     * https://stackoverflow.com/questions/4755806/file-not-found-exception-in-jar
     *
     * @param cmd     first part of the client provided command
     * @param iStream either Linux or Windows resource file for validating the command, as an input stream
     * @return true if command is valid, false otherwise
     */
    private static boolean readCmdFile(String cmd, InputStream iStream) {
        boolean isValidCmd = false;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(iStream))) {

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
     * Helper function that will attempt to execute the command (either Linux or Windows).
     *
     * @param cmd command to execute
     * @return String output returned from executing the command
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
} // end class UDPServerCmdExec
