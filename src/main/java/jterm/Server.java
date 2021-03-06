/*
* JTerm - a cross-platform terminal
* Copyright (C) 2017 Sergix, NCSGeek
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package main.java.jterm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

	private Socket socket;
	public static boolean run = true;
	public static int port = 0;
	private static String line;
	
	Server(Socket newSocket) {
		socket = newSocket;
		
	}
	
	public void run() {
		
		while (run) {
			try {
				InputStream input = socket.getInputStream();
				BufferedReader bufferedSocketInput 	= new BufferedReader(new InputStreamReader(input));
				
				line = bufferedSocketInput.readLine();
				if (line.isEmpty()) {
					break;
					
				}
				System.out.println("\n" + line);
				
				bufferedSocketInput.close();
				
			}
			catch (IOException ioe)
			{
				System.out.println(ioe);
				break;
				
			}
			
		}
		
		return;
		
	}
	
	public static void Start(ArrayList<String> options) {
		
		String portInput = "80";
		
		for (String option: options)
		{
			if(option.equals("-h"))
			{
				System.out.println("Command syntax:\n\tserver [-h] port\n\nStarts a TCP server socket that accepts ");
				return;
				
			}
			else
			{
				portInput = option;
				
			}
		}
		
		int i = 0;

        while( i < portInput.length())  
        {
            port *= 10;
            port += portInput.charAt(i++) - '0';
            
        }
		
		try {
			ServerSocket server = new ServerSocket(port);
			
			new Thread(new Runnable() {
				
				public void run() {
					
					while (true)
					{
						System.out.print("> ");
						BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in), 1);
						try {
							String input = consoleInput.readLine();
							switch (input) {
								case "help":
									System.out.println("Server currently opened on port " + port);
									break;
								
								case "quit":
									run = false;
									return;
							
							}
							
						} catch (IOException ioe) {
							System.out.println("Input Stream closed.");
							break;
							
						}
						
					}
					
				}
				
			}).start();
			
			while (run) {
				Socket socket = server.accept();
				Server newRequest = new Server(socket);
				new Thread(newRequest).start();
				
			}
			
			server.close();
			
		}
		catch (IOException ioe)
		{
			System.out.println("ERROR: Server closed");
			
		}
		
	}
	
}
