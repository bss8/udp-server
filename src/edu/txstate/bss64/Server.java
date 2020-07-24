package edu.txstate.bss64;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public abstract class Server {
    //index 2 = direct caller, can be self but abstract may not be instantiated
    private static final int DIRECT_CALLER_IDX = 2;

    /**
     * Prints a message to standard output.
     * @param request message received from a UDP Client
     */
    public static String displayReceivedMessage(DatagramPacket request) {
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
}
