/*
 * Author: Mahesh Devalla
 * Artificial Intelligence
 * Assignment: HMM and first-order logic assignment
 * 
 * References:
 * http://tutorials.jenkov.com/java-internationalization/decimalformat.html
 * http://web.cse.ohio-state.edu/~dwang/teaching/cis730/Belief-net.pdf
 * http://www.cs.tut.fi/~elomaa/teach/AI-2011-9.pdf
 * http://www.cs.mcgill.ca/~dprecup/courses/Prob/Lectures/prob-lecture01.pdf
 * http://web.stanford.edu/~danlass/NASSLLI-coursenotes-combined.pdf
 * https://en.wikipedia.org/wiki/Probabilistic_logic
 * http://groups.engin.umd.umich.edu/CIS/course.des/cis479/lectures/uncert.html
 * http://www.cs.dartmouth.edu/~devin/cs76/06_hmm-logic/assignment_markov.html
 * http://mlg.eng.cam.ac.uk/zoubin/papers/ijprai.pdf
 * http://aries.ektf.hu/~gkusper/ArtificialIntelligence_LectureNotes.v.1.0.4.pdf
 * http://www.sdsc.edu/~tbailey/teaching/cse151/lectures/chap07a.html
 * https://www.ismll.uni-hildesheim.de/lehre/ai-08s/skript/ai-2up-05-fol.pdf
 * https://www.techfak.uni-bielefeld.de/ags/wbski/lehre/digiSA/WS0506/MDKI/Vorlesung/vl07_FOL.pdf
 * https://en.wikipedia.org/wiki/First-order_logic#Automated_theorem_proving_and_formal_methods
 * http://www.let.rug.nl/bos/lot2013/BB1.pdf
 * http://pages.cs.wisc.edu/~dyer/cs540/notes/fopc.html
 * http://www.cs.cmu.edu/~cdm/pdf/AutomLogic1-6up.pdf
 * 
 */
package hmm;

import java.io.*; 
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;


class LoadMaze
{
	final static Charset cs = StandardCharsets.UTF_8;   
	public static int[] NORTH = {0, 1};
	public static int[] EAST = {1, 0};
	public static int[] SOUTH = {0, -1};
	public static int[] WEST = {-1, 0};
    public static int[] STAY = {0, 0};	
    public static int move[][] = { NORTH, EAST, SOUTH, WEST, STAY }; 
    private static Path p;
    public static char colors[] = {'r','g','b','y'};
	public static char ch[][];
	public Random rand = new Random(0);
	public static int y_axis,x_axis;
	LoadMaze()
	{	
	}
/*
 * Reference taken from
 * https://docs.oracle.com/javase/tutorial/essential/io/fileio.html
 * http://www.java2s.com/Code/Java/File-Input-Output/UseNIOtoreadatextfile.htm
 * 
 */
	public  static LoadMaze readContents() throws Exception
	{
		LoadMaze loadmaze = new LoadMaze();
		try
		{
			p= Paths.get("maze.txt");
			List<String> content= readContent(p,cs);
			List<String> data = Files.readAllLines(Paths.get("Maze.txt"));
			int temp1 = 0;
			y_axis = data.size();
			loadmaze.ch = new char[y_axis][];
			for (String strlength : data) 
			{
				x_axis = strlength.length();

				loadmaze.ch[y_axis - temp1 - 1] = new char[strlength.length()];
				for(int i = 0;i<strlength.length();i++)
				{
					loadmaze.ch[y_axis - temp1 - 1][i] = strlength.charAt(i);
				}
				temp1++;
			}
		}
		catch(Exception e)
		{
			System.out.println("Cannot load the contents");
			e.printStackTrace();
		} 
		return loadmaze;
	}
	
	public static List<String> readContent(Path p,Charset cs) throws Exception{
		List<String> listOfStrings=null;
		 
		try (BufferedReader br = newBufferedReader(p,cs)) 
	        {
			listOfStrings = new ArrayList<>();
	            for (;;) 
	            {
	                String str = br.readLine();
	                	if (str == null)
	                    break;
	                	listOfStrings.add(str);
	            }
	            return listOfStrings;
	    } catch (IOException e) {
			e.printStackTrace();
		}
		return listOfStrings;
	}
	private static BufferedReader newBufferedReader(Path p2, Charset encoding2) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader br= new BufferedReader(new FileReader("Maze.txt"));
		return br;
		 
	}
	public boolean isValid(int xcord , int ycord)
	{
		return (xcord >= 0 && xcord < x_axis && ycord<y_axis && ycord >=0)?(ch[ycord][xcord]!='#'):false;
		
		
	}

	public char getValue(int xcord, int ycord)
	{
		//Returning in reverse order to get the position
		return ch[ycord][xcord];
	}

	public static int xyState(int xcord, int ycord)
	{
	//	System.out.println(xcord+" "+ycord);
		return ycord*y_axis +xcord;
	}
	
	//Convert state to XY
	public static int[] stateXY(int state)
	{
		int[] arr = new int[2];
		arr[0] = state % y_axis;
		arr[1] = state / y_axis;
		return arr;
	}
	
	public char[] getPath(int[] path) 
	{
        int[] arr;
        char[] valPath = new char[path.length];
        char probValue,trueValue;
        int value;
        for (int i = 0; i < path.length; i++) 
        {
          arr = stateXY(path[i]);
          trueValue = ch[arr[1]][arr[0]];

          value = rand.nextInt(100);
        //Providing the probability here.
          if (value < 88) 
          {  
        	  probValue = trueValue;
          } 
          else 
          {
            do 
            {            
            	probValue = colors[rand.nextInt(4)];
            } 
            while (probValue == trueValue);
          }

          valPath[i] = probValue;
        }
        return valPath;
      }
	
	public char[] getCorrectPath(int[] path) 
	{
        int[] position;
        char[] tempch = new char[path.length];

        for (int i = 0; i < path.length; i++) 
        {
        	position = stateXY(path[i]);
        	tempch[i] = ch[position[1]][position[0]];
        }
        return tempch;
      }
	
	 public int[] getPath(int size) 
	 {
         int[] position = new int[size];
         int tempxcord, tempycord;
         int nextxcord, nextycord;
          int[] nextMove;

        //Choosing a path randomly from the set.
         do 
         {
        	 tempxcord = rand.nextInt(x_axis);
        	 tempycord = rand.nextInt(y_axis);
         } 
         while (! isValid(tempxcord, tempycord));

         position[0] = xyState(tempxcord, tempycord);

         for (int i = 1; i < size; i++) {
        	 //Trying to get the next move.
        	 do 
        	 {
        	nextMove = move[rand.nextInt(5)];
             nextxcord = tempxcord + nextMove[0];
             nextycord = tempycord + nextMove[1];
           } 
        	 while (! isValid(nextxcord, nextycord));

        	 position[i] = xyState(nextxcord, nextycord);
        	 tempxcord = nextxcord;
        	 tempycord = nextycord;

         }

         return position;
       }
}