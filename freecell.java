import java.util.*;

class OrderedStack
{
	private Stack <String> pile;
	private int score = 0;
	private int nextScore = 1;
	
	public OrderedStack()
	{
		pile = new Stack <> ();
	}
	
	public int getSize()
	{
		return pile.size();
	}
	
	public int getScore()
	{
		return score;
	}
	
	public boolean isEmpty()
	{
		return pile.isEmpty();
	}
	
	public void push(String card)
	{
		score += nextScore;
		nextScore++;
		pile.push(card);
	}
	
	public String peek()
	{
		return pile.peek();
	}
	
	public Stack<String> displayPile()
	{
		return pile;
	}
}

class ColumnClass
{
	private ArrayList <String> column;
	
	public ColumnClass()
	{
		column = new ArrayList <> ();
	}
	
	public int getSize()
	{
		return column.size();
	}
	
	public String getElement(int i)
	{
		return column.get(i);
	}
	
	public int getIndex(String card)
	{
		for (int i = 0; i < column.size(); i++)
		{
			if (column.get(i).equals(card))
				return i;
		}
		return -1;
	}
	
	public boolean contains(String card)
	{
		return column.contains(card);
	}
	
	public boolean isEmpty()
	{
		return column.isEmpty();
	}
	
	public void add(String card)
	{
		column.add(card);
	}
	
	public void add(int i,String card)
	{
		column.add(i,card);
	}
	
	public void remove(String card)
	{
		column.remove(card);
	}
	
	public void clear()
	{
		column.clear();
	}
	
	public ArrayList <String> displayColumn()
	{
		return column;
	}
}

/**
* <h1> Freecell Game </h1>
* <p> This is a class that contains a Freecell game which is a card game in command prompt.
* <p> The goal of this game is to transfer all the cards from the columns to the piles according to the ranks and suits.
* <p> User needs to enter the command based on this format: <b> source, selected card, destination </b>.
* <p> User can also enter 'r' to restart the game, 'x' to exit the game and just enter one column number to rotate the column.
* 
* @author Lim Yixen,Wong Jit Chow
*/
public class freecell
{
	/**
	* This constant stores the four suits of the cards.
	*/
	public static final String SUITS = "cdhs";
	/**
	* This constant stores the ranks of the cards on ascending order.
	*/
	public static final String RANKS = "A23456789XJQK";
	/**
	* This constant stores the total number of piles will be shown in the board.
	*/
	public static final int PILENUM = 4;
	/**
	* This constant stores the total number of columns will be shown in the board.
	*/
	public static final int COLUMNNUM = 9;
	/**
	* This constant stores the highest mark the user can get in this game.
	*/
	public static final int FULLSCORE = 364;
	/**
	* This constant stores the total number of cards in this game.
	*/
	public static final int TOTALCARD = 52;
	/**
	* This is used to get the user input.
	*/
	public static Scanner input = new Scanner(System.in);
	
	//Array of the piles and columns
	private static OrderedStack [] pile = new OrderedStack [PILENUM];
	private static ColumnClass [] column = new ColumnClass [COLUMNNUM];
	
	/**
	 * This is a clear screen function.
	 */
	public static void cls()
	{
		try
		{	
			new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		}
		catch(Exception E)
		{
			System.out.println(E);
		}
	}
	
	/**
	 * This function displays the board of the Freecell game.
	 * <p> The board consists of four stacks of piles and nine stacks of columns.
	 */
	public static void display()
	{
		int sum = 0;
		for (int i = 0; i < PILENUM; i++) //Get the sum of the score of four piles
			sum += pile[i].getScore();
		
		//Display the board of the game
		System.out.println("Score: " + sum + "\n");
		
		for (int i = 0; i < PILENUM; i++)
			System.out.println("Pile " + SUITS.charAt(i) + ": " + pile[i].displayPile());
		
		for (int i = 0; i < COLUMNNUM; i++)
			System.out.println("Column " + (i+1) + ": " + column[i].displayColumn());
	}
	
	/**
	 * This function random distributes cards on certain columns when the game starts / restarts.
	 */
	public static void create()
	{
		Random rand = new Random();
		Set <String> tempColumn = new LinkedHashSet <> ();
		String card;
		
		//Create piles and columns
		for (int i = 0; i < PILENUM; i++)
			pile[i] = new OrderedStack ();
		
		for (int i = 0; i < COLUMNNUM; i++)
			column[i] = new ColumnClass ();

		while(true)//Add cards into tempColumn
		{
			int suit_index = rand.nextInt(SUITS.length());
			int rank_index = rand.nextInt(RANKS.length());
			
			card = "" + SUITS.charAt(suit_index) + RANKS.charAt(rank_index);
			tempColumn.add(card);
			
			if (tempColumn.size() == TOTALCARD)
				break;
		}
		
		//Make a copy of the tempColumn Set to ArrayList
		ArrayList <String> tempColumn2 = new ArrayList <> (tempColumn);
		int index = 0;
		int c = 0;

		while(c < TOTALCARD)//Distribute the cards into the top eight columns
		{
			card = tempColumn2.get(c);
			column[index].add(card);
			c++;				
			
			if ((index < PILENUM && column[index].getSize() == 7) || (index >= PILENUM && column[index].getSize() == 6))
				index++;
		}
	}
	
	/**
	 * This function displays error message based on the invalid command.
	 *
	 * @param msg The error message generated
	 */
	public static void displayMessage(String msg)
	{
		System.out.print("\nError: " + msg + "\nPlease re-enter command.\nCommand > ");
	}
	
	/**
	 * This function searches for the index of the rank of a selected card <b> (Accroding to the default order of the ranks) </b>.
	 *
	 * @param card The selected card that needs to be searched for its rank's index
	 *
	 * @return The index of the rank (-1 if index not found)
	 */
	public static int indexSearch(String card)
	{
		for (int i = 0; i < RANKS.length(); i++)
		{
			if (RANKS.charAt(i) == (card.charAt(1)))
				return i;
		}
		return -1;
	}
	
	/**
	 * This function checks whether the bunch of cards selected is in order to be moved.
	 * <p> The current card must be one rank bigger than the card to its right
	 *
	 * @param source The source column that contains the selected card
	 * @param card The selected card by user
	 *
	 * @return True if the card(s) start from the selected card is in order (in terms of the ranks), False if not in order
	 */
	public static boolean inOrder(int source,String card)
	{
		int indexOfCard = column[source].getIndex(card);
		String first = "";
		String second = "";
		
		for (int i = indexOfCard; i < column[source].getSize(); i++)
		{
			if (i < (column[source].getSize()-1))
			{
				first = "" + column[source].getElement(i);
				second = "" + column[source].getElement(i+1);
				
				if (indexSearch(first) != (indexSearch(second)+1))
					return false;
			}
		}
		return true;
	}
	
	/**
	 * This function mvoes the selected card(s) to the destination.
	 * <p><b> Actions: </b>
	 * <p> 1. Remove card(s) from the source column
	 * <p> 2. Add card(s) to the destination column / pile
	 *
	 * @param source The source column that contains the selected card
	 * @param card The selected card by user
	 * @param destination The destination column or pile 
	 */
	public static void move(int source,String card,char destination)
	{
		ArrayList <String> temp = new ArrayList <> ();//Holds the selected card(s)
		String current_card = "";
		int lastIndex = column[source].getSize()-1;
		int cardIndex = column[source].getIndex(card);
		
		for (int i = lastIndex; i >= cardIndex; i--)//Load all the cards to be moved into temp
		{
			current_card = column[source].getElement(i);
			temp.add(current_card);
			column[source].remove(current_card);
		}
		
		if (!Character.isDigit(destination))//If the destination is pile
		{
			int destination_pile = SUITS.indexOf(destination);//Find the index of the pile
			for (int i = 0; i < temp.size(); i++) //Push card(s) into destination pile
			{
				current_card = temp.get(i);
				pile[destination_pile].push(current_card);
			}
		}
		else //If the destination is column
		{
			int destination_column = Character.getNumericValue(destination)-1;//Find the index of the column
			for (int i = temp.size()-1; i >= 0 ; i--)//Push card(s) into destination column
			{
				current_card = temp.get(i);
				column[destination_column].add(current_card);
			}
		}
	}
	
	/**
	 * This function checks whether the condition of destination pile / column is suitable to move cards.
	 * <p><b> Suitable conditions (For destination pile): </b>
	 * <p> 1. All the selected card(s) are same suit
	 * <p> 2. The suit of the destination pile is same as the suit of the card(s)
	 * <p> 3. Last element of destination pile / column are in correct order to move the selected card(s)
	 * <p> 4. First card in an empty pile must be 'A'
	 * <p><b> Suitable conditions (For destination column): </b>
	 * <p> 1. First of the selected card(s) must be one rank lower than the last element of destination column
	 * 
	 * @param source The source column that contains the selected card
	 * @param card The selected card by user
	 * @param destination The destination column or pile 
	 *
	 * @return True if the condition of destination is suitable to move card(s), False if vice versa
	 */
	public static boolean destination_ready(int source,String card,char destination)
	{
		int cardIndex = column[source].getIndex(card);
		String current_card = column[source].getElement(cardIndex);
		int sourceColumn_lastIndex = column[source].getSize()-1;
		String sourceColumn_lastElement = column[source].getElement(sourceColumn_lastIndex);
		char current_suit = current_card.charAt(0);
		
		if (!Character.isDigit(destination))//If the destination is pile
		{
			int destination_pile = SUITS.indexOf(destination);
			
			for (int i = cardIndex + 1; i < column[source].getSize(); i++)//Check if all selected cards are same suit
			{
				current_card = "" + column[source].getElement(i);
				if (!(current_card.charAt(0) == current_suit))
				{
					displayMessage("Selected card(s) not same suit");
					return false;
				}
			}
			
			if (!(current_suit == (SUITS.charAt(destination_pile))))//Check if the suit of the destination pile is same as the suit of the cards
			{
				displayMessage("Card(s) not same suit as the destination pile");
				return false;
			}
			
			if (!pile[destination_pile].isEmpty())//If pile contains any cards
			{
				String pile_lastElement = pile[destination_pile].peek();//Get the element on the top of the pile stack
				
				//Check if last element of pile and column are in correct order to be moved
				if (RANKS.indexOf(pile_lastElement.charAt(1)) != (RANKS.indexOf(sourceColumn_lastElement.charAt(1))-1))
				{
					displayMessage("Selected card(s) need to be placed in pile in order");
					return false;
				}
			}
			else if (current_card.charAt(1) != 'A')//If pile is empty
			{
				displayMessage("First card in the pile must be 'A'");
				return false;
			}
		}
		else //If the destination is column
		{
			int destination_column = Character.getNumericValue(destination)-1;
			
			if (!column[destination_column].isEmpty())
			{
				int destColumn_lastIndex = column[destination_column].getSize()-1;
				String destColumn_lastElement = column[destination_column].getElement(destColumn_lastIndex);
				
				//Check if the first selected card in source column is one rank lower than the last element of destination column
				if (RANKS.indexOf(current_card.charAt(1)) != (RANKS.indexOf(destColumn_lastElement.charAt(1))-1))
				{
					displayMessage("Selected card(s) can only be placed on one rank that bigger than it");
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * This function rotates a column which move the last card to the front of the column.
	 * 
	 * @param columnChar The column that needs to be rotated by user
	 */
	public static void rotate(char columnChar)
	{
		int columnNum = Character.getNumericValue(columnChar)-1;
		int lastIndex = column[columnNum].getSize()-1;
		
		String card = column[columnNum].getElement(lastIndex);//Get the last card from the selected column
		column[columnNum].remove(card);//Remove the card from the column
		column[columnNum].add(0,card); //Add into the head of the column
	}
	
	/**
	 * This is the main function that runs automatically.
	 * <p> Input will be received here and input validation will happen here
	 * 
	 * @param args command-line arguments
	 */
	public static void main (String [] args)
	{		
		char command = 'r';
		int source = 0;
		String selected_card = "";
		char destination = 'B';
		
		cls();
		create();
		display();
		System.out.print("Command > ");
		String received_command = input.nextLine();
		
		while (true)
		{
			if (received_command.length() == 1)//If only one char is entered
			{
				command = received_command.charAt(0);
				
				if (Character.toLowerCase(command) == 'r')//Check to restart the game
				{
					cls();
					create();
					display();
					System.out.print("Command > ");
					received_command = input.nextLine();
				}
				else if (Character.toLowerCase(command) == 'x')//Check to exit the game
					break;
				else if (Character.isDigit(command))//Check whether it is digit that might refer to column
				{
					if (!(Character.getNumericValue(command) >= 1 && Character.getNumericValue(command) <= COLUMNNUM))//Check if the rotate column is 0
					{
						displayMessage("Invalid rotation column");
						received_command = input.nextLine();
					}
					else
					{
						rotate(command);
						cls();
						display();
						System.out.print("Command > ");
						received_command = input.nextLine();
					}
				}
				else
				{
					displayMessage("Invalid command");
					received_command = input.nextLine();
				}
			}
			else if (received_command.length() == 6)//If the length is matched with the correct moving command
			{
				if (received_command.charAt(1) == ' ' && received_command.charAt(4) == ' ')//If the command is separated by space according to the correct moving command
				{
					received_command = received_command.replace(" " , "");//Remove the space
					if (received_command.length() == 4)//If the correct moving command is entered
					{
						source = received_command.charAt(0);
						if (Character.isDigit(source))
						{
							source = Character.getNumericValue(source)-1;//Get the index of the source column
							
							if (!(source >= 0 && source <= COLUMNNUM-1))//Check the validity of the source column
							{
								displayMessage("Invalid source column");
								received_command = input.nextLine();
							}
							else //If the source column is valid
							{
								//Prepare the card to the default format
								selected_card = "" + Character.toLowerCase(received_command.charAt(1)) + Character.toUpperCase(received_command.charAt(2));
								
								if (SUITS.indexOf(selected_card.charAt(0)) == -1 || RANKS.indexOf(selected_card.charAt(1)) == -1)//Check the validity of the card
								{
									displayMessage("Invalid card");
									received_command = input.nextLine();
								}							
								else if (!(column[source].contains(selected_card)))//Check whether the card is on the source column
								{
									displayMessage("Selected card is not found on the source column");
									received_command = input.nextLine();
								}
								else
								{
									destination = received_command.charAt(3);
									
									if (destination == (char)(source+1 + '0'))//Check whether the source and destination is the same
									{
										displayMessage("Source and destination are the same");
										received_command = input.nextLine();
									}
									else if ((Character.isDigit(destination) && Character.getNumericValue(destination) >= 1 && Character.getNumericValue(destination) <= COLUMNNUM) || SUITS.indexOf(destination) != -1)//Check for validity of the destination column
									{
										if (inOrder(source,selected_card))//Check whether the card(s) is in order
										{
											if (destination_ready(source,selected_card,destination))//Check the condition in destination is ready to receive card(s)
											{
												move(source,selected_card,destination);
												
												int sum = 0;
												for (int i = 0; i < PILENUM; i++)//Get the sum of score of all piles
													sum += pile[i].getScore();
												
												cls();
												display();
												
												if (sum < FULLSCORE)//If the game is not finished yet
												{
													System.out.print("Command > ");
													received_command = input.nextLine();
												}
												else //If the game is finished
												{
													System.out.println("You win the game!\nScore: " + sum);
													break;
												}
											}
											else
												received_command = input.nextLine();
										}
										else
										{
											displayMessage("Selected card(s) must at the right most of a column or in order");
											received_command = input.nextLine();
										}
									}
									else
									{
										displayMessage("Invalid destination column or pile");
										received_command = input.nextLine();
									}
								}
							}
						}
						else
						{
							displayMessage("Invalid source column");
							received_command = input.nextLine();
						}
					}
					else
					{
						displayMessage("Invalid command");
						received_command = input.nextLine();
					}
				}
				else
				{
					displayMessage("Invalid command");
					received_command = input.nextLine();
				}
			}
			else
			{
				displayMessage("Invalid command");
				received_command = input.nextLine();
			}
		}
	}
}