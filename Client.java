import java.net.*;
import java.io.*;         //Required imports.
import java.util.*;
/**
 * This Client Class is run twice to simulate two online players in a connect-four game.
 * The Clients connect to the Server with the specified IP and Port, and are give symbols
 * that represent their game pieces. The Clients take turns placing their pieces by submitting
 * the Column to place their piece into, which go to the lower-most empty slot in that column.
 *
 * @author (Christopher Carroll)
 * @version (V 1.0)
 */
public class Client
{
    static Socket clientSocket;          //This socket allows the client to connect with the server when instantiated.

    static DataOutputStream dataWriter;  //Allows sending data to the Server when instantiated.
    static DataInputStream dataReader;   //Allows reading data from the Server when instantiated.

    public static void main(String[] args)
    {
        Server serverLoc = new Server();

        try
        {
            clientSocket = new Socket("localhost", serverLoc.port); //Client connects to server at localhost and a port number.
            Scanner input = new Scanner(System.in);

            dataWriter = new DataOutputStream(clientSocket.getOutputStream()); //Instantiating the DataOutputStream.
            dataReader = new DataInputStream(clientSocket.getInputStream());   //Instantiating the DataInputStream.

            System.out.println(dataReader.readUTF()); //Telling the Client, the connection to the server has been successful.

            String SymbolID = dataReader.readUTF();   //Telling the Client which symbol X's or O's it will use.
            System.out.println("Your assigned Player Input Symbol: " + SymbolID);

            boolean clientRunTime = true;
            String userVote = null;

            while(clientRunTime == true)    //The main runtime loop of the Client.
            {                               //In this loop, data is validated is some aspects, sent to the server where
                int userColumn = 0;         //it undergoes more data validation to check if a Column is valid and open.
                boolean rowCheck = false;   //If the data is not completely satisfactory, the attempt is rejected, and
                System.out.println(dataReader.readUTF()); //the user is prompted for a new input.
                System.out.println(dataReader.readUTF());

                System.out.println("(Please enter a number between 1 and 7)"); //Client-sided data validation, this checks
                while(userColumn < 1 || userColumn > 7)                        //if the user input is both an integer, and
                {                                                              //if the integer is in the range of 1-7.
                    while(!input.hasNextInt())
                    {
                        System.out.println("Error. The input you chose was not an integer.");  //If the input is not an integer.
                        input.next();
                    }

                    userColumn = input.nextInt();

                    if(userColumn > 0 && userColumn < 8)         //Ensures the input is within the correct range.
                    {
                        dataWriter.writeInt(userColumn);
                        rowCheck = dataReader.readBoolean(); //Sends the input to the Server class to check if the column
                                                             //has room for the input.
                        if(rowCheck == false)                //If the column is full, the user is prompted for another
                        {                                    //input, where the input must be re-validated before sent.
                            System.out.println("This is an invalid move, please choose another Column.");
                        }

                        while(rowCheck == false)             //Input re-validation is the case that the column is full.
                        {
                            System.out.println("(Try another number in 1-7)");

                            while(!input.hasNextInt())
                            {
                                System.out.println("Error. The input you chose was not an integer.");
                                input.next();
                            }

                            userColumn = input.nextInt();

                            if(userColumn > 0 && userColumn < 8)
                            {
                                dataWriter.writeInt(userColumn);       //The re-validated data is sent.
                                rowCheck = dataReader.readBoolean();
                            }
                            else
                            {
                                System.out.println("Error. The integer is not in the range of 1-7.");
                            }
                        }
                        System.out.println("Successfully sent (" + userColumn + ") to the Server."); //Confirmation for acceptable input.
                    }
                    else
                    {
                        System.out.println("Error. The integer is not in the range of 1-7.");
                    }
                }


                System.out.println("\nPress the y key to vote to end the game, enter anything else to continue."); //Player vote to end game.
                userVote = input.next();
                System.out.println("Vote Sent, if both clients agree in the same cycle the game will be aborted. Turn Complete.");

                if(userVote.equals("y") || userVote.equals("Y"))  //If both of the Clients agree the game should end, the appropriate
                {                                                 //answer is told to the Server.
                    dataWriter.writeBoolean(true);
                }
                else
                {
                    dataWriter.writeBoolean(false);
                }

                boolean playerVote = dataReader.readBoolean();   //After confirmation from the Server that the game will
                                                                 //end, the Server shuts down, and prompts the Clients
                if(playerVote == true)                           //to follow suit.
                {
                    clientRunTime = false;
                }


                int boardFull;
                boardFull = dataReader.readInt();               //If the Server detects that there are no further places
                                                                //to put Client symbols, the game ends, and the Clients
                if(boardFull == 1)                              //are informed by the Server that the game should end.
                {
                    System.out.println("\nServer: Game Board is full, ending game.");
                    clientRunTime = false;
                }
            }

            System.out.println("\nGame Over!");

            clientSocket.close();                //Socket, data reader, and data writer are closed down.
            dataReader.close();
            dataWriter.close();
            System.out.println("Successfully Disconnected from Server.");
        }
        catch(IOException exception_IO)        //Catches any case of client/server disconnection, or bad port/ip.
        {
            System.out.println("A Server error has occurred.");
        }
    }
}