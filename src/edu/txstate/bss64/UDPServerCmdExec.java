package edu.txstate.bss64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UDPServerCmdExec {
    public static void main(String[] args) {
        try (DatagramSocket aSocket = new DatagramSocket(6789)) {
            byte[] buffer = new byte[1000];
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);

                byte[] clientData = request.getData();
                String stringClientData = new String(clientData, StandardCharsets.UTF_8);
                stringClientData = runCmd(stringClientData);

                clientData = stringClientData.getBytes();
                DatagramPacket reply = new DatagramPacket(clientData, clientData.length, request.getAddress(), request.getPort());
                aSocket.send(reply);
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

