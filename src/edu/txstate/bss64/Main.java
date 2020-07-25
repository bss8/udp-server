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

/**
 * @author Borislav S. Sabotinov
 */
public class Main {
    public static void main(String...args) {
        if (args.length != 1 || args[0].equals("help")) {
            printHelp();
        } else {
            if (args[0].equals("1")) {

            } else if (args[0].equals("2")) {

            } else {
                printHelp();
                throw new IllegalArgumentException("\nOption code not supported!");
            }
        }
    }

    private static void printHelp() {
        System.out.print("Enter 1 for standard message server, 2 for command execution server:\n" +
                "java -jar server.jar <OPT_CODE>\n");
    }
}
