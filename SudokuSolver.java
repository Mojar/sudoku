import java.util.*;
import java.io.*;

class choiceList
{
	int[] choices; //The array of all possible choices for this square
	int listSize;  //The amount of elements in the array
	int additionIndex;  //The index to traverse when adding choices
	int currentIndex;  //The index to traverse when removing choices
	boolean isEnd;  //True if we are at the end of the array
	int N;
	int positionX;
	int positionY;

   public choiceList(int gridSize, int xCoord, int yCoord){
	   currentIndex = 0;
	   additionIndex = 0;
	   isEnd = false;
	   N = gridSize;
      positionX = xCoord;
      positionY = yCoord;
	}
	
	void createList(int size){
	   int[] list = new int[size];
	   listSize = size;
	   this.choices = list;
	   if (this.listSize > 1){
		   isEnd = false;
		   return;
		}
		isEnd = true;
	}

	void addChoice(int value){
		   choices[additionIndex] = value;
		   //Could simply call incrementIndex() but we're going for optimized speed here
			additionIndex += 1;
         if(listSize == 1){
			   isEnd = true;
			}
	}

	void incrementIndex(){
      if (!isEnd){
         currentIndex += 1;
         if(currentIndex == listSize - 1){
			   isEnd = true;
			}
		}
	}

	boolean isEnd(){
	   return isEnd;
	}

	int getX(){
      return positionX;
	}
	
	int getY(){
      return positionY;
	}

   int getCurrentIndex(){
      return currentIndex;
	}

	int getCurrentChoice(){
	   return this.choices[currentIndex];
	}
	
	int remainingChoices(){
      return (listSize - 1) - currentIndex;
	}
	
	void resetIndex(){
		currentIndex = 0;
      if(listSize != 1){
		   isEnd = false;
		}
	}
}


class Sudoku
{
    /* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For 
     * a standard Sudoku puzzle, SIZE is 3 and N is 9. */
    int SIZE, N;

    /* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
     * not yet been revealed are stored as 0. */
    int Grid[][];

	 //Return True if a given value is somewhere in the same row or column as the given coordinates
	 public boolean isMatching_RowColumn(int xCoords, int yCoords, int myValue){
       int i;
		 //Traverse Column
		 for(i = 0; i < N; i++){
		    if(Grid[i][yCoords] == myValue){
             return true;
			 }
		 }
		 //Traverse Row
		 for(i = 0; i < N; i++){
		    if(Grid[xCoords][i] == myValue){
             return true;
			 }
		 }
		 return false;
	 }
	 
	 //Return True if a given value is somewhere in the same block as the given coordinates
	 public boolean isMatching_Block(int xCoords, int yCoords, int value){
       int minX = xCoords;
       int minY = yCoords;
       int i = 0;

		 //Find left edge of block
		 while((xCoords - i) % SIZE != 0){
		 	 i++;
		    minX = (xCoords - i);
		 }

       //Find upper edge of block
       i = 0;
		 while((yCoords - i) % SIZE != 0){
		 	 i++;
		    minY = (yCoords - i);
		 }
		 //Check if a match exists within the current block
		 for(i = 0; i < SIZE; i++){
		    for(int j = 0; j < SIZE; j++){
			    if(Grid[(minX + i)][(minY + j)] == value){
                return true;
				 }
			 }
		 }
		 return false;
	 }

    /* The solve() method should remove all the unknown characters ('x') in the Grid
     * and replace them with the numbers from 1-9 that satisfy the Sudoku puzzle. */
    public void solve()
    {
        /* INSERT YOUR CODE HERE */
      int i, j, k; //Incrementor variables (declared here for optimization)

      int temp[] = new int[N]; //Used for the creation of the undefined square objects
      int tempIncr;
      Stack<choiceList> listStack = new Stack<choiceList>();
      Stack<choiceList> solutionStack = new Stack<choiceList>();
      Stack<choiceList> sortingStack = new Stack<choiceList>();
		
		//Go through each undefined square and make an object for it
		for(i = 0; i < N; i++){
		   for(j = 0; j < N; j++){
            if(Grid[i][j] == 0){
				   //Create the object (arraySize, xCoord, yCoord)
					choiceList list = new choiceList(N,i,j);

				   //Find all values that don't already exist in a row, column, or block
					tempIncr = 0;
					for(k = 1; k < N + 1; k++){
					   if( isMatching_RowColumn(i,j,k) || isMatching_Block(i,j,k) ){
						   //Go to next iteration
							continue;
						}
						//Add the value as a choice possibility for this spot
                  temp[tempIncr] = k;
                  tempIncr++;
					}
					//Add these values to the list object
					list.createList(tempIncr);
					for(k = 0; k < tempIncr; k++){
						list.addChoice(temp[k]);
					}

					//Add the object to the stack
               listStack.push(list);
				}
			}
		}



		//Clear any locations that clearly have only 1 choice
		boolean changeOccurred = true;  //Used to check if we've recently filled in some solutions

      while(changeOccurred == true){
			changeOccurred = false;

		   //Put the entire listStack into the solutionStack, removing any solved locations
			while(!listStack.empty()){
			   if(listStack.peek().isEnd()){
				   //Update the Grid
					Grid[listStack.peek().getX()][listStack.peek().getY()] = listStack.peek().getCurrentChoice();
				   listStack.pop(); //Would put this on the line above for optimization, but I don't know whether the left-value is evaluated first or the right

				   changeOccurred = true;
					if(listStack.empty()){
                  break;
					}

				   //If there's a match in the next item now that the change has occurred, update that item                  //Potential for error here: Make sure you include !isEnd
				   while( (isMatching_RowColumn(listStack.peek().getX(),listStack.peek().getY(),listStack.peek().getCurrentChoice()) || isMatching_Block(listStack.peek().getX(),listStack.peek().getY(),listStack.peek().getCurrentChoice())) ){
					   listStack.peek().incrementIndex();
					}
				}
				else{
				   solutionStack.push(listStack.pop());
				}
			}
			
		   //Put the entire solutionStack into the listStack, removing any solved locations
			while(!solutionStack.empty()){
			   if(solutionStack.peek().isEnd()){
				   Grid[solutionStack.peek().getX()][solutionStack.peek().getY()] = solutionStack.peek().getCurrentChoice();
				   solutionStack.pop().getCurrentChoice(); //Would put this on the line above for optimization, but I don't know whether the left-value is evaluated first or the right

				   changeOccurred = true;
				   
				   //If there's a match in the next item now that the change has occurred, update that item                  //Potential for error here: Make sure you include !isEnd
				   while( (isMatching_RowColumn(solutionStack.peek().getX(),solutionStack.peek().getY(),solutionStack.peek().getCurrentChoice()) || isMatching_Block(solutionStack.peek().getX(),solutionStack.peek().getY(),solutionStack.peek().getCurrentChoice())) ){
					   solutionStack.peek().incrementIndex();
					}
				}
				else{
				   listStack.push(solutionStack.pop());
				}
			}
		}
		
		//Re-order the listStack so that objects with less choices are at the top (pseudo-heap)
		i = 0;
		while(!listStack.empty() || !sortingStack.empty()){
			//Put the ordered objects into the solutionStack
			while(!listStack.empty()){
			   if(listStack.peek().remainingChoices() == i){
				   solutionStack.push(listStack.pop());
				}
				else{
	            sortingStack.push(listStack.pop());
				}
			}
			i++;
			//Put all of the sortingStack back into the listStack and repeat the above
			while(!sortingStack.empty()){
			   listStack.push(sortingStack.pop());
			}
		}
		//Put the newly ordered SolutionStack back into the listStack
		while(!solutionStack.empty()){
		   listStack.push(solutionStack.pop());
		}

		//Traverse the filled stack and once it is empty, a solution has been found
		while(!listStack.empty()){
         //If there aren't any conflicts, add object from itemList to solutionList
			if( !isMatching_RowColumn(listStack.peek().getX(),listStack.peek().getY(),listStack.peek().getCurrentChoice()) && !isMatching_Block(listStack.peek().getX(),listStack.peek().getY(),listStack.peek().getCurrentChoice()) ){
	         //Move item into solution stack
				solutionStack.push(listStack.pop());

				//Update the Grid
	         Grid[solutionStack.peek().getX()][solutionStack.peek().getY()] = solutionStack.peek().getCurrentChoice();

	         //If the list is empty, we've solved the puzzle
	         if(listStack.empty()){
	            return;
				}
			}else{
			   //Increment the list item until we have a choice that works
				if(listStack.peek().remainingChoices() != 0){
				   listStack.peek().incrementIndex();
				}
				else{
				   //Go back into the solution list to the most recent item that had an alternate choice
					while(!solutionStack.empty()){
					   //Reset that item and its portion of the grid
						listStack.peek().resetIndex();
	               Grid[listStack.peek().getX()][listStack.peek().getY()] = 0;
					   listStack.push(solutionStack.pop());
						if(listStack.peek().remainingChoices() != 0){
						   listStack.peek().incrementIndex();
						   break;
					   }
					}
				}
			}
		}
   }

    /*****************************************************************************/
    /* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE FUNCTIONS BELOW THIS LINE. */
    /* Everything below has been provided for reading the puzzle and displaying the result. */
    /*****************************************************************************/
 
    /* Default constructor.  This will initialize all positions to the default 0
     * value.  Use the read() function to load the Sudoku puzzle from a file or
     * the standard input. */
    public Sudoku( int size )
    {
        SIZE = size;
        N = size*size;

        Grid = new int[N][N];
        for( int i = 0; i < N; i++ )
            for( int j = 0; j < N; j++ )
                Grid[i][j] = 0;
    }


    /* readInteger is a helper function for the reading of the input file.  It reads
     * words until it finds one that represents an integer. For convenience, it will also
     * recognize the string "x" as equivalent to "0". */
    static int readInteger( InputStream in ) throws Exception
    {
        int result = 0;
        boolean success = false;

        while( !success ) {
            String word = readWord( in );

            try {
                result = Integer.parseInt( word );
                success = true;
            } catch( Exception e ) {
                // Convert 'x' words into 0's
                if( word.compareTo("x") == 0 ) {
                    result = 0;
                    success = true;
                }
                // Ignore all other words that are not integers
            }
        }

        return result;
    }


    /* readWord is a helper function that reads a word separated by white space. */
    static String readWord( InputStream in ) throws Exception
    {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
        String whiteSpace = " \t\n";

        // Ignore any leading white space
        while( whiteSpace.indexOf(currentChar) > -1 ) {
            currentChar = in.read();
        }

        // Read all characters until you reach white space
        while( whiteSpace.indexOf(currentChar) == -1 ) {
            result.append( (char) currentChar );
            currentChar = in.read();
        }
        return result.toString();
    }


    /* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
     * grid is filled in one row at at time, from left to right.  All non-valid
     * characters are ignored by this function and may be used in the Sudoku file
     * to increase its legibility. */
    public void read( InputStream in ) throws Exception
    {
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                Grid[i][j] = readInteger( in );
            }
        }
    }


    /* Helper function for the printing of Sudoku puzzle.  This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth( String text, int width )
    {
        for( int i = 0; i < width - text.length(); i++ )
            System.out.print( " " );
        System.out.print( text );
    }


    /* The print() function outputs the Sudoku grid to the standard output, using
     * a bit of extra formatting to make the result clearly readable. */
    public void print()
    {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes 
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuffer line = new StringBuffer();
        for( int lineInit = 0; lineInit < lineLength; lineInit++ )
            line.append('-');

        // Go through the Grid, printing out its values separated by spaces
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                printFixedWidth( String.valueOf( Grid[i][j] ), digits );
                // Print the vertical lines between boxes 
                if( (j < N-1) && ((j+1) % SIZE == 0) )
                    System.out.print( " |" );
                System.out.print( " " );
            }
            System.out.println();

            // Print the horizontal line between boxes
            if( (i < N-1) && ((i+1) % SIZE == 0) )
                System.out.println( line.toString() );
        }
    }


    /* The main function reads in a Sudoku puzzle from the standard input, 
     * unless a file name is provided as a run-time argument, in which case the
     * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
     * outputs the completed puzzle to the standard output. */
    public static void main( String args[] ) throws Exception
    {
        InputStream in;
        if( args.length > 0 ) 
            in = new FileInputStream( args[0] );
        else
            in = System.in;

        // The first number in all Sudoku files must represent the size of the puzzle.  See
        // the example files for the file format.
        int puzzleSize = readInteger( in );
        if( puzzleSize > 100 || puzzleSize < 1 ) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }

        Sudoku s = new Sudoku( puzzleSize );

        // read the rest of the Sudoku puzzle
        s.read( in );

// get the time before starting the solve
long startTime = System.currentTimeMillis();
        // Solve the puzzle.  We don't currently check to verify that the puzzle can be
        // successfully completed.  You may add that check if you want to, but it is not
        // necessary.
        s.solve();
// get the time after the solve
long endTime = System.currentTimeMillis();
System.out.println("Running time: "+ (endTime-startTime) + " milliseconds");

        // Print out the (hopefully completed!) puzzle
        s.print();
    }
}

