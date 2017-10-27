import java.util.*;
import java.io.*;     //Required Java imports.

/**
 * This class file reads a graph in from a file, and uses the Breadth First Search algorithm
 * to visit all the nodes, mapping the graph.
 * 
 * @author (Christopher Carroll)
 * @version (v1.1 created on 10/26/2015)
 */
public class BreadthSearch
{
    public static void main(String[] args) throws IOException   //Exception for invalid file name.
    {
        String fileName, holder, holder2, holder3, save, nodeStart;       //String variables used throughout program.
        HashMap<String, LinkedList<String>> theList = new HashMap<String, LinkedList<String>>(); //Creation of a Hash Map to store nodes and adjacent nodes.
        Scanner input = new Scanner(System.in);      //Scanner object to read user input.
        StringTokenizer stok;          //String Tokenizers to break up strings read in, so they can be placed in maps and lists.
        int nodeCounter = 0;          //Counts the number of nodes the graph has.

        System.out.println("Enter directory followed by the name of the text file, to begin file streaming: ");
        fileName = input.nextLine();   //Saves location of user's text file as a string, for example: (C:\documents\textfilename.txt).

        Scanner inputFile = new Scanner(new File(fileName));  //File streaming begins from text file at user specified location.

        while(inputFile.hasNext())    //Loops through, reading the file line by line.
        {
            holder = inputFile.next();                             //First line is read from text file and set to a String.
            stok = new StringTokenizer(holder, ",");               //String Tokenizer breaks up the large String into smaller pieces.
            save = stok.nextToken();                               //Saving the first node to a String variable.
            LinkedList<String> listSet = new LinkedList<String>(); //Creation of the linked list for current text line.
            listSet.add(save);                                     //Adds first node to be a key for the LinkedList's HashMap location.
            while(stok.hasMoreTokens())
            {
                listSet.add(stok.nextToken());    //Loops through the rest of the nodes for the line, adding them to the LinkedList.
            }
            theList.put(save, listSet);  //Using the first node value as the key, adds the LinkedList of nodes to the HashMap.
            nodeCounter++;    //Counts the number of nodes in the graph by incrementing when the loop is running.
        }
        inputFile.close();    //File streaming stops.

        System.out.println("\nPlease enter the CAPITAL letter for the starting node.");
        nodeStart = input.nextLine();  //User enters starting node value.

        LinkedList<String> listSetOther = new LinkedList<String>(theList.get(nodeStart));  //Using the key, the adjacency list in the map is saved to another list.

        Queue<String> breadthQueue = new LinkedList<String>();             //Creation of the queue.
        HashMap<String, String> theList2 = new HashMap<String, String>();   //Creation of another HashMap to save the nodes already searched.

        for(int x = 0; x < listSetOther.size(); x++)   //Loop to iterate through the Linked List.
        {
            holder2 = listSetOther.get(x);    //Values of Linked List saved to string variable.
            breadthQueue.add(holder2);        //Values of string variables added to the queue.
            theList2.put(holder2, holder2);   //Saves nodes already visisted to the HashMap using the letter itself as the key.
        }

        System.out.println("\nBreadth First Search Order: ");

        for(int i = 0; i < nodeCounter; i++)   //Loops through all the nodes visiting them using a queue.
        {
            holder3 = breadthQueue.poll();   //Retrieves first node from the queue, then removes it from the queue.
            System.out.print(holder3 + " "); //Prints the corresponding node to the screen.
            LinkedList<String> listSetOther2 = new LinkedList<String>(theList.get(holder3));  //Uses the node as a key, to view surrounding nodes.

            for(int q = 0; q < listSetOther2.size(); q++)  //Loops through the surrounding nodes.
            {
                if(listSetOther2.get(q).equals(theList2.get(listSetOther2.get(q)))) //If statement filters out the repeat nodes.
                {
                    int rogue = 0;
                }
                else
                {
                    breadthQueue.add(listSetOther2.get(q)); //Adds surrounding non-repeated node to the queue.
                    theList2.put(listSetOther2.get(q), listSetOther2.get(q));  //Adds the new node to the HashMap to designate it visited.
                }
            }
        }
    }
}