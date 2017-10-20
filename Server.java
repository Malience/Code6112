import java.net.*;
import java.io.*;          //Required imports.
/**
 * This Server Class file provides the Clients with a connection through an IP and Port.
 * Also the server houses the Game Board for connect-four, dispatching it to the Clients
 * upon request and updating it when needed. The Server also ensures the game is running
 * and when it will end.
 *
 * @author (Christopher Carroll)
 * @version (V 1.0)
 */
public class Server
{
    static ServerSocket server;                 //Server socket that designated this file as Host on a port/ip.
    static Socket client1;                      //Allows acceptance of a first Client when requested.
    static Socket client2;                      //Allows acceptance of a second Client when requested.

    static DataOutputStream client1dataWriter;  //Allows sending data to first connected Client when instantiated.
    static DataOutputStream client2dataWriter;  //Allows sending data to second connected Client when instantiated.
    static DataInputStream client1dataReader;   //Allows reading data from first connected Client when instantiated.
    static DataInputStream client2dataReader;   //Allows reading data from second connected Client when instantiated.

    public static int port = 11360;             //The Port number the Server uses to make Client connections.


    public static String updateBoard(String[][] SG) //A method that updates the Game Board to the most recent change.
    {
        String reRender =
                " _ _ _ _ _ _ _\n|"+SG[5][0]+"|"+SG[5][1]+"|"+SG[5][2]+"|"+SG[5][3]+"|"+SG[5][4]+"|"+SG[5][5]+"|"+SG[5][6]+"|\n" +
                " _ _ _ _ _ _ _\n|"+SG[4][0]+"|"+SG[4][1]+"|"+SG[4][2]+"|"+SG[4][3]+"|"+SG[4][4]+"|"+SG[4][5]+"|"+SG[4][6]+"|\n" +
                " _ _ _ _ _ _ _\n|"+SG[3][0]+"|"+SG[3][1]+"|"+SG[3][2]+"|"+SG[3][3]+"|"+SG[3][4]+"|"+SG[3][5]+"|"+SG[3][6]+"|\n" +
                " _ _ _ _ _ _ _\n|"+SG[2][0]+"|"+SG[2][1]+"|"+SG[2][2]+"|"+SG[2][3]+"|"+SG[2][4]+"|"+SG[2][5]+"|"+SG[2][6]+"|\n" +
                " _ _ _ _ _ _ _\n|"+SG[1][0]+"|"+SG[1][1]+"|"+SG[1][2]+"|"+SG[1][3]+"|"+SG[1][4]+"|"+SG[1][5]+"|"+SG[1][6]+"|\n" +
                " _ _ _ _ _ _ _\n|"+SG[0][0]+"|"+SG[0][1]+"|"+SG[0][2]+"|"+SG[0][3]+"|"+SG[0][4]+"|"+SG[0][5]+"|"+SG[0][6]+"|\n";;
        return reRender;
    }

    public static int checkBoard(String[][] SG, int userNum) //A method that finds the lowest place in the Client specified
    {                                                        //column to place a symbol. Also checks if the column is full.
        int iterator = 0;
        while(!SG[iterator][userNum].equals(" "))
        {
            iterator++;

            if(iterator >= 6)
            {
                return iterator;
            }
        }
        return iterator;
    }

    public static void main(String[] args)
    {
        try
        {
            System.out.println("Server Initiated."); //Server has started.
            server = new ServerSocket(port);         //Server is now hosting the game on Localhost and the above Port number.

            String [][] SG = new String[6][7];       //The Game Board is represented by a 2-D array. It is initialized as a
                                                     //blank board with the same dimensions as a game of connect-four.
            for(int row = 0; row < SG.length; row++)
            {
                for(int col = 0; col < SG[row].length; col++)
                {
                    SG[row][col] = " ";
                }
            }

            String renderBoard =      //The visual Game Board is created using text and the 2-D array represents playable spaces.
                            " _ _ _ _ _ _ _\n|"+SG[5][0]+"|"+SG[5][1]+"|"+SG[5][2]+"|"+SG[5][3]+"|"+SG[5][4]+"|"+SG[5][5]+"|"+SG[5][6]+"|\n" +
                            " _ _ _ _ _ _ _\n|"+SG[4][0]+"|"+SG[4][1]+"|"+SG[4][2]+"|"+SG[4][3]+"|"+SG[4][4]+"|"+SG[4][5]+"|"+SG[4][6]+"|\n" +
                            " _ _ _ _ _ _ _\n|"+SG[3][0]+"|"+SG[3][1]+"|"+SG[3][2]+"|"+SG[3][3]+"|"+SG[3][4]+"|"+SG[3][5]+"|"+SG[3][6]+"|\n" +
                            " _ _ _ _ _ _ _\n|"+SG[2][0]+"|"+SG[2][1]+"|"+SG[2][2]+"|"+SG[2][3]+"|"+SG[2][4]+"|"+SG[2][5]+"|"+SG[2][6]+"|\n" +
                            " _ _ _ _ _ _ _\n|"+SG[1][0]+"|"+SG[1][1]+"|"+SG[1][2]+"|"+SG[1][3]+"|"+SG[1][4]+"|"+SG[1][5]+"|"+SG[1][6]+"|\n" +
                            " _ _ _ _ _ _ _\n|"+SG[0][0]+"|"+SG[0][1]+"|"+SG[0][2]+"|"+SG[0][3]+"|"+SG[0][4]+"|"+SG[0][5]+"|"+SG[0][6]+"|\n";


            client1 = server.accept();
            client1dataWriter = new DataOutputStream(client1.getOutputStream());  //Client 1 connects and is registered as Player 1.
            client1dataReader = new DataInputStream(client1.getInputStream());
            client1dataWriter.writeUTF("Server: You have Connected successfully as Player 1.");
            System.out.println("Player 1 Connected...");

            client2 = server.accept();
            client2dataWriter = new DataOutputStream(client2.getOutputStream());  //Client 2 connects and is registered as Player 2.
            client2dataReader = new DataInputStream(client2.getInputStream());
            client2dataWriter.writeUTF("Server: You have Connected successfully as Player 2.");
            System.out.println("Player 2 Connected...");

            String player1 = "X";   //Player 1 is designated the fierce X game piece, may the flames guide thee.
            String player2 = "O";   //Player 2 is designated the humble O game piece, may the force be with you...Always.

            client1dataWriter.writeUTF(player1); //Player 1 is informed of their symbol.
            client2dataWriter.writeUTF(player2); //Player 2 is informed of their symbol.


            boolean serverRunTime = true;
            int columnChanger = 0;
            int row = 0;
            boolean P1Vote = false, P2Vote = false;

            while(serverRunTime == true)  //The main runtime loop of the Server.
            {
                boolean P1Check = false, P2Check = false; //Player 1 is given directions.
                client1dataWriter.writeUTF("\nServer(->P1): Here is the current state of the game board:\n" + renderBoard);
                client1dataWriter.writeUTF("Server(->P1): It is your turn, Enter the Column number to place your symbol.");

                while(P1Check == false)                              //A loop that validates that a Column is appropriate.
                {
                    columnChanger = client1dataReader.readInt() - 1; //Client 1 sends their column choice.
                    row = checkBoard(SG, columnChanger);             //Lowest row is determined for that column.

                    if(row >= 0 && row < 6)
                    {
                        client1dataWriter.writeBoolean(true);        //The Client(1) is informed of the valid column choice.
                        P1Check = true;
                    }
                    else
                    {
                        client1dataWriter.writeBoolean(false);       //The Client(1) is informed of the invalid column choice.
                    }
                }

                SG[row][columnChanger] = "X";               //Game Board is marked with the symbol in the location.
                renderBoard = updateBoard(SG);              //Game Board is updated for the next player.
                System.out.println("Player 1 Chooses Column: " + (columnChanger + 1));
                P1Vote = client1dataReader.readBoolean();   //Server is notified of the Client's(1) decision to continue.

                                                            //Player 2 is given directions.
                client2dataWriter.writeUTF("\nServer(->P2): Here is the current state of the game board:\n" + renderBoard);
                client2dataWriter.writeUTF("Server(->P2): It is your turn, Enter the Column number to place your symbol.");

                while(P2Check == false)                              //A loop that validates that a Column is appropriate.
                {
                    columnChanger = client2dataReader.readInt() - 1; //Client 2 sends their column choice.
                    row = checkBoard(SG, columnChanger);             //Lowest row is determined for that column.

                    if(row >= 0 && row < 6)
                    {
                        client2dataWriter.writeBoolean(true);      //The Client(2) is informed of the valid column choice.
                        P2Check = true;
                    }
                    else
                    {
                        client2dataWriter.writeBoolean(false);     //The Client(2) is informed of the invalid column choice.
                    }
                }

                SG[row][columnChanger] = "O";               //Game Board is marked with the symbol in the location.
                renderBoard = updateBoard(SG);              //Game Board is updated for the next turn cycle.
                System.out.println("Player 2 Chooses Column: " + (columnChanger + 1));
                P2Vote = client2dataReader.readBoolean();   //Server is notified of the Client's(2) decision to continue.

                if(P1Vote == true && P2Vote == true)    //If both Clients vote to stop the game, runtime will end after
                {                                       //this iteration.
                    serverRunTime = false;
                    System.out.println("\nPlayer Vote Successful.");
                    client1dataWriter.writeBoolean(true);
                    client2dataWriter.writeBoolean(true); //Clients are told to stop runtime.
                }
                else
                {
                    System.out.println("Player Vote Unsuccessful.");
                    client1dataWriter.writeBoolean(false);
                    client2dataWriter.writeBoolean(false); //Clients are told that the vote failed.
                }


                int boardFull = 0;
                for(int x = 0; x < 7; x++)    //Logic to detect if the Game Board is completely full. If so, runtime ends
                {                             //after the cycle.
                    if(!SG[5][x].equals(" "))
                    {
                        boardFull++;
                    }
                    if(boardFull == 7)
                    {
                        System.out.println("The Server has detected the game board is full, Ending Game.");
                        serverRunTime = false;
                        client1dataWriter.writeInt(1);
                        client2dataWriter.writeInt(1);
                    }
                    if(boardFull < 7 && x == 6)
                    {
                        System.out.println("Game board is in good standing.");
                        client1dataWriter.writeInt(0);
                        client2dataWriter.writeInt(0);
                    }
                }
            }

            client1.close();        //Client and Server connections are closed.
            client2.close();
            server.close();

            client1dataWriter.close(); //Data Reader, and Data Writer are closed down.
            client2dataWriter.close();
            client1dataReader.close();
            client2dataReader.close();
            System.out.println("Server Successfully Shut Down.");
        }
        catch(IOException exception_IO)        //Catches any case of client/server disconnection, or bad port/ip.
        {
            System.out.println("A Server error has occurred.");
        }
    }
}