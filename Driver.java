/*
    rokirc-srv: A remote application that listens to an IRC server and generates Markov chains.
    Copyright (C) 2018  Joshua Trahan

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

import com.robut.rokrcsrv.IRCManager;
import com.robut.rokrcsrv.IRCManagerException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;

public class Driver {
    public static void main(String[] args){
        if (args.length > 0) {
            String server = args[0];
            int port = Integer.parseInt(args[1]);
            String[] channels = new String[0];

            String userName;
            String auth;

            if (args.length > 2) {
                channels = Arrays.copyOfRange(args, 2, args.length);
            }

            try (BufferedReader credFile = new BufferedReader(new FileReader("resources/creds.txt"))) {
                userName = credFile.readLine();
                auth = credFile.readLine();
                credFile.close();
            } catch (Exception e) {
                System.err.printf("Exception reading credentials: %s%n", e);
                return;
            }

            testIrcManager(server, port, userName, auth, Arrays.asList(channels));
        }
    }

    public static void testIrcManager(String server, int port, String nick, String auth, Collection<String> channels){
        IRCManager ircManager = new IRCManager("resources/dbs/");
        ircManager.connectToIrcServer(server, port, nick, auth, channels);

        for (String chan : channels) {
            try {
                System.out.printf("%s: %s%n", chan, ircManager.generateMarkovString(server, chan));
            } catch (IRCManagerException e) {
                System.err.printf("Error generating message: %s%n", e);
            }
        }
    }
}
