package edu.txstate.bss64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UDPServerCmdExec extends Server implements ServerBehavior {
    public static void main(String[] args) {
        System.out.println("Starting up command execution UDP Server listening on port 2587.....\n");

        try (DatagramSocket aSocket = new DatagramSocket(2587)) {
            while (true) {
                DatagramPacket request = ServerBehavior.receiveReplyFromClient(aSocket);

                String stringClientData = displayReceivedMessage(request);
                stringClientData = runCmd(stringClientData);

                ServerBehavior.sendReplyBackToClient(aSocket, request, stringClientData);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    private static String runCmd(String cmd) {
        StringBuilder output = null;
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", cmd.trim());
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

//            int exitVal = process.waitFor();
//            if (exitVal == 0) {
//                System.out.println("Success!");
//                System.out.println(output);
//                //System.exit(0);
//            } else {
//                //abnormal...
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}

