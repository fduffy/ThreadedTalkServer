
//A network server that listens to port 2009.
//It sends the time of the day to its client

import java.io.*;
import java.util.*;
import java.net.*;

public class TalkServer
{
	public static void main(String[] args)
	{
		int port = 2009;

		NetworkServer_1 nwServer = new NetworkServer_1(port);

		nwServer.listen();
	}
}

class NetworkServer_1 implements Runnable
{
	protected int port;
	protected Socket clientConnection = null;

  /** Build a server on specified port. It will continue to accept connections
      until an explicit exit command is sent.
  */
	public NetworkServer_1(int port)
	{
		this.port = port;
	}
	public NetworkServer_1(int port, Socket newConnection)
	{
		this.port = port;
		this.clientConnection = newConnection;
	}
	/** method that executes for every incoming connection. Multi threaded operation.
	 */
	public void run() {
		try {
			handleConnection(clientConnection);
		} catch (IOException ioe){ System.out.println("IOException: " + ioe); }
	}

  /** Monitor a port for connections. Each time one is established, pass resulting Socket to
      handleConnection.
  */
	public void listen()
	{

		try
		{
			 ServerSocket listener = new ServerSocket(port);
			 Socket server;
                         System.out.println(" TalkServer is up and running\n");
			 while(true)
			 {
				 server = listener.accept(); //accept connection request from a client
				 Runnable connectionHandler = new NetworkServer_1(port, server);
				 new Thread(connectionHandler).start();
			 }

		}
		catch (IOException ioe){ System.out.println("IOException: " + ioe); }

	}
  /**
      This is the method that provides the behavior to the server
  */

    protected void handleConnection(Socket server) throws IOException
	{
	  String s="";
          String[] greeting = {"My head hurts", "And I feel sick", " Well, you speak too much",
			       "Well, I never.","Well, you don't say.",
                               "Everybody hates me.","Try and calm down",
                               "Why won't you help me?", "I'm all alone.",
                               "I can't afford it.","Mustn't grumble though, eh?",
				"Never mind, eh?", "I see.","What a shame.",
				"My bill for $00 will be in the mail today.", "You devil!"
	                       };
	  //to get input from the client
          BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));

	  //Out to the client -- Enable auto-flush
	  PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);


	  System.out.println("got connection from " +
		              server.getInetAddress().getHostName() + "\n");

	  out.println(" TalkServer for CS-430 by Prof. Sattar" +
	               "To close connection type, END");

          out.println("You can talk to me. What's your name?");

          //get client name
	  String clientName = in.readLine();

	  out.println("SERVER> Hello " + clientName + ", This is your electronic shrink here.");

          //Now talkboolean done = false;

	  while (true)
	  {       // get input from the client
		  if((s = in.readLine()).length() != 0)
		  {
			  if (s.equals("END")) 
                          {
                            System.out.println("TalkServer is shutting down. BYE!");
                            break;
                           }

			  System.out.println("Echoing: " + s);
                          out.println( "SERVER > " +
				       greeting[((int)(greeting.length*Math.random())) % greeting.length]);
		  }
	  }

	  
	  server.close();
	  System.exit(0);
	}
}
