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

import java.util.*;
import java.text.*;

public class HMM {
	private LoadMaze loadmaze;
	DecimalFormat dformat = new DecimalFormat("#0.000");
	private int length, area;

	/*
	 * Making all the variables in particular format
	 * Reference from:
	 * http://tutorials.jenkov.com/java-internationalization/decimalformat.html
	 */
	private int[] path;
	private char[] color,value;
	private double[][] red,blue,yellow,green;
	/*
	 * Variables for viterbi algorithm and forward,backward algorithm.
	 * Reference from http://www.cs.dartmouth.edu/~devin/cs76/06_hmm-logic/assignment_markov.html
	 */
	private double[] forward,backward;
	private double[][] tModel,tTranspose;
	private double[][] viterbi;
	private int[][] prev_viterbi;

	public LoadMaze getLoadmaze() {
		return loadmaze;
	}

	public void setLoadmaze(LoadMaze loadmaze) {
		this.loadmaze = loadmaze;
	}

	public DecimalFormat getDformat() {
		return dformat;
	} 

	public void setDformat(DecimalFormat dformat) {
		this.dformat = dformat;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int[] getPath() {
		return path;
	}

	public void setPath(int[] path) {
		this.path = path;
	}

	public char[] getColor() {
		return color;
	}

	public void setColor(char[] color) {
		this.color = color;
	}

	public char[] getValue() {
		return value;
	}

	public void setValue(char[] value) {
		this.value = value;
	}

	public double[][] getRed() {
		return red;
	}

	public void setRed(double[][] red) {
		this.red = red;
	}

	public double[][] getBlue() {
		return blue;
	}

	public void setBlue(double[][] blue) {
		this.blue = blue;
	}

	public double[][] getYellow() {
		return yellow;
	}

	public void setYellow(double[][] yellow) {
		this.yellow = yellow;
	}

	public double[][] getGreen() {
		return green;
	}

	public void setGreen(double[][] green) {
		this.green = green;
	}

	public double[] getForward() {
		return forward;
	}

	public void setForward(double[] forward) {
		this.forward = forward;
	}

	public double[] getBackward() {
		return backward;
	}

	public void setBackward(double[] backward) {
		this.backward = backward;
	}

	public double[][] gettModel() {
		return tModel;
	}

	public void settModel(double[][] tModel) {
		this.tModel = tModel;
	}

	public double[][] gettTranspose() {
		return tTranspose;
	}

	public void settTranspose(double[][] tTranspose) {
		this.tTranspose = tTranspose;
	}

	public double[][] getViterbi() {
		return viterbi;
	}

	public void setViterbi(double[][] viterbi) {
		this.viterbi = viterbi;
	}

	public int[][] getPrev_viterbi() {
		return prev_viterbi;
	}

	public void setPrev_viterbi(int[][] prev_viterbi) {
		this.prev_viterbi = prev_viterbi;
	}

	public HMM(LoadMaze loadmaze, int length, int pathsize) {
		
		int[] position;
		this.loadmaze = loadmaze;
		this.length = length;
		this.area = length * length;
		this.path = loadmaze.getPath(pathsize);
		this.color = loadmaze.getCorrectPath(path);
		this.value = loadmaze.getPath(path);
		this.viterbi = new double[pathsize][area];
		this.prev_viterbi = new int[pathsize][area];
		printStars();
		for (int i = 0; i < path.length; i++) {
			position = LoadMaze.stateXY(path[i]);
			System.out.println("Current Step is:" + (i + 1) + " {" + position[0] + ", " + position[1] + "}\t Value is: " + value[i] + "\t and color is: "+ color[i] );
		}
		init();
	}

	/*
	 * using matrices to changes from matrix to states.
	 */
	public void init() {
		double prob,nextprob;
		char color;
		int[] position; 
		double[][] maze;
		int currmove,nextcord;
		color = 'r';
		maze = new double[this.getArea()][this.getArea()]; 
		for (int r = 0; r < getArea(); r++) 
		{
			position = LoadMaze.stateXY(r);
			maze[r][r]=(loadmaze.getValue(position[0], position[1]) == color)?0.88:0.04;
		}
		red = maze;

		color = 'b';
		maze = new double[getArea()][getArea()];
		for (int b = 0; b < getArea(); b++)
		{
			position = LoadMaze.stateXY(b);
			maze[b][b]=(loadmaze.getValue(position[0], position[1]) == color)?0.88:0.04;
		}
		blue = maze;

		color = 'g';
		maze = new double[getArea()][getArea()];
		for (int g = 0; g < getArea(); g++) 
		{
			position = LoadMaze.stateXY(g);
			maze[g][g]=(loadmaze.getValue(position[0], position[1]) == color)?0.88:0.04;
		}
		green = maze;

		color = 'y';
		maze = new double[getArea()][getArea()];
		for (int y = 0; y < getArea(); y++)
		{
			position = LoadMaze.stateXY(y);
			maze[y][y]=(loadmaze.getValue(position[0], position[1]) == color)?0.88:0.04;
		}
		yellow = maze;
		printStars();
		System.out.println("Red Sensor:");
		System.out.println();
	//	System.out.println("************************************************************************");
		for (int ycord = 0; ycord < getArea(); ycord++) {
			System.out.print(" ");
			for (int xcord = 0; xcord< getArea(); xcord++) {
				System.out.print(dformat.format(red[ycord][xcord]) + ", ");
			}
			System.out.print("\n");
		}		
		printStars();		
		System.out.println();
		System.out.println("Blue Sensor:");
		for (int ycord = 0; ycord < getArea(); ycord++) {
			System.out.print(" ");
			for (int xcord = 0; xcord < getArea(); xcord++) {
				System.out.print(dformat.format(blue[ycord][xcord]) + ", ");
			}
			System.out.print("\n");
		}
		printStars();
		
		System.out.println("Green Sensor:");
		for (int ycord = 0; ycord < getArea(); ycord++) {
			System.out.print(" ");
			for (int xcord = 0; xcord < getArea(); xcord++) {
				System.out.print(dformat.format(green[ycord][xcord]) + ", ");
			}
			System.out.print("\n");
		}
		printStars();
		System.out.println("Yellow Sensor:");
		for (int ycord = 0; ycord < getArea(); ycord++) {
			System.out.print(" ");
			for (int xcord = 0; xcord < getArea(); xcord++) {
				System.out.print(dformat.format(yellow[ycord][xcord]) + ", ");
			}
			System.out.print("\n");
		}
		System.out.println("\n");

		tModel = new double[area][area];
		for (int mazecord = 0; mazecord < getArea(); mazecord++){
			currmove = 0;
			nextprob = 0.0;
			position = LoadMaze.stateXY(mazecord);
			for (int[] tempmove : LoadMaze.move)
				if (loadmaze.isValid(position[0] + tempmove[0], position[1] + tempmove[1]))
					currmove += 1;
			nextprob=(currmove == 0)?0.0:(1.0/currmove);

			for (int[] tempmove : LoadMaze.move) 
			{
				if (loadmaze.isValid(position[0] + tempmove[0], position[1] + tempmove[1])) {
					nextcord = LoadMaze.xyState(position[0] + tempmove[0], position[1] + tempmove[1]);
					tModel[mazecord][nextcord] = nextprob;
				}
			}
		}
		printStars();
		System.out.println("Transition Table: ");
		for (int ycord = 0; ycord < area; ycord++) {
			System.out.print(" ");
			for (int xcord = 0; xcord< area; xcord++) {
				System.out.print(dformat.format(tModel[ycord][xcord]) + ", ");
			}
			System.out.print("\n");
		}
		System.out.println("\n");

		tTranspose = new double[area][area];
		for (int y = 0; y < area; y++)
			for (int x = 0; x < area; x++)
				tTranspose[y][x] = tModel[x][y];
		printStars();
		System.out.println("Transpose of Transition Table: ");
		for (int ycord = 0; ycord < area; ycord++) {
			System.out.print(" ");
			for (int xcord = 0; xcord < area; xcord++) {
				System.out.print(dformat.format(tTranspose[ycord][xcord]) + ", ");
			}
			System.out.print("\n");
		}
		System.out.println("\n");

		backward = new double[area];
		Arrays.fill(backward, 1.0);
		printStars();
		System.out.println("\nInitial Condition for Backward:");
		int size = length;
		for (int i = 0; i < backward.length; i++) {
			if (size == length) {
				System.out.print("\n  ");
				size = 0;
			}

			System.out.print(dformat.format(backward[i]) + ", ");
			size++;
		}
		System.out.println("");
		printStars();
		//probability for forwarding 
		prob = 1.0 / area;
		forward = new double[area];
		Arrays.fill(forward, prob);
		/*
		 * Print the first condition for forwarding.
		 */	
		System.out.println("\nInitial Condition for Forward:");
		int size2 = length;
		for (int i = 0; i < forward.length; i++) {
			if (size2 == length) {
				System.out.print("\n  ");
				size2 = 0;
			}

			System.out.print(dformat.format(forward[i]) + ", ");
			size2++;
		}
		System.out.println("");
		printStars();

	}

	/*
	 * Method for forward filtering to choose the correct sensor and then normalizing.
	 * Reference from
	 * http://stat.columbia.edu/~liam/research/pubs/fast-low-rank-kalman.pdf 
	 */
	public void filtering() {
		double[][] sen = null;
		double[] forwardfilter;
		forwardfilter = forward;

		for (int color = 0; color < path.length; color++) {
			if(value[color]=='r')
			{
				sen=red;
			}
			else if(value[color]=='b')
			{
				sen = blue;
			}
			else if(value[color]=='g')
			{
				sen = green;
			}
			else if(value[color]=='y')
			{
				sen = yellow;
			}
			double[] content = calc(sen, calc(tTranspose, getForward()));
			double[] normalizedMatrix = new double[content.length];
			double sum = 0;

			for (double val : content)
				sum += val;

			for (int i = 0; i < content.length; i++)
				normalizedMatrix[i] = content[i] / sum;

			forwardfilter = normalizedMatrix;

			/*
			 * Calculating the viterbi optimal path
			 * Reference taken from
			 * https://en.wikipedia.org/wiki/Viterbi_algorithm
			 * https://onlinecourses.science.psu.edu/stat857/node/203
			 */
			if (color == 0)
				viterbi[0] = forwardfilter;
			//Distribution
			System.out.println("\nFiltered Distribution at the step " + (color + 1) + ": ");
			System.out.println("Current Location is:" + path[color] + "");
			int len = length;
			for (int i = 0; i < forwardfilter.length; i++) {
				if (len == length) {
					System.out.print("\n  ");
					len = 0;
				}

				System.out.print(dformat.format(forwardfilter[i]) + ", ");
				len++;
			}
			System.out.println("");
			printStars();

		}
	}

	/*
	 * Bonus forward backward algorithm
	 * Reference taken from 
	 * https://www.youtube.com/watch?v=7zDARfKVm7s
	 * http://www.cse.unt.edu/~tarau/teaching/NLP/HMM.pdf
	 */
	public void fbSmoothingAlgo() {
		double[] forw,back,dis;
		double[][] sen = null;
		double[][] forwvalues = new double[path.length][area];
		forw = getForward();
		back = getBackward();
		for (int color = 0; color < path.length; color++) {
			if(value[color]=='r')
			{
				sen=red;
			}
			else if(value[color]=='b')
			{
				sen = blue;
			}
			else if(value[color]=='g')
			{
				sen = green;
			}
			else if(value[color]=='y')
			{
				sen = yellow;
			}
			
			double[] message = calc(sen, calc(tTranspose, forw));
			double[] normMatrix = new double[message.length];
			double sum = 0;

			for (double val : message)
				sum += val;

			for (int i = 0; i < message.length; i++)
				normMatrix[i] = message[i] / sum;
			forw = normMatrix;
			forwvalues[color] = forw;
		}
		for (int color = path.length - 1; color >= 0; color--) {
			double[] message = multiply(forwvalues[color], back);
			double[] normMatrix = new double[message.length];
			double sum = 0;

			for (double val : message)
				sum += val;

			for (int i = 0; i < message.length; i++)
				normMatrix[i] = message[i] / sum;
			dis = normMatrix;
			System.out.println("\nSmoothed Distribution at step " + (color + 1) + ": ");
			System.out.println("Current Location is : " + path[color]);
			int len = length;
			for (int i = 0; i < dis.length; i++) {
				if (len == length) {
					System.out.print("\n  ");
					len = 0;
				}
				System.out.print(dformat.format(dis[i]) + ", ");
				len++;
			}
			System.out.println();
			printStars();
			if(value[color]=='r')
			{
				sen=red;
			}
			else if(value[color]=='b')
			{
				sen = blue;
			}
			else if(value[color]=='g')
			{
				sen = green;
			}
			else if(value[color]=='y')
			{
				sen = yellow;
			}
			back = calc(tModel, calc(sen, back));
		}
	}

	public static void printStars() {
		for (int star = 0; star < 120; star++) {
			System.out.print("*");
		}
		System.out.println("");
		for (int star = 0; star < 120; star++) {
			System.out.print("*");

		}
System.out.println("");
	
}
	/*
	 * Bonus 
	 * Method to implement Viterbi algorithm
	 * Reference taken from https://www.youtube.com/watch?v=sCO2riwPUTA
	 * http://research.cs.tamu.edu/prism/lectures/pr/pr_l23.pdf
	 */
	public void viterbiAlgo() {
		double[][] sen = null;
		int max, prevVal;
		LinkedList<Integer> finalPath = new LinkedList<>();
		double maxVal,tempVal;
		int currVal, finalVal;
		Arrays.fill(prev_viterbi[0], -1);
		for (int color = 1; color < path.length; color++) {
			double[] viterbiVal = new double[area];

			for (currVal = 0; currVal < area; currVal++) {
				max = -1;
				maxVal = 0.0;
				for (finalVal = 0; finalVal < area; finalVal++) {
					tempVal = viterbi[color - 1][finalVal] * tModel[finalVal][currVal];
					if (tempVal > maxVal) {
						maxVal = tempVal;
						max = finalVal;
					}
				}
				viterbiVal[currVal] = maxVal;
				prev_viterbi[color][currVal] = max;
			}

			if(value[color]=='r')
			{
				sen=red;
			}
			else if(value[color]=='b')
			{
				sen = blue;
			}
			else if(value[color]=='g')
			{
				sen = green;
			}
			else if(value[color]=='y')
			{
				sen = yellow;
			}
			
			
			double[] message = calc(sen, viterbiVal);
			double[] normalized = new double[message.length];
			double sum = 0;

			for (double val : message)
				sum += val;

			for (int i = 0; i < message.length; i++)
				normalized[i] = message[i] / sum;

			viterbi[color] = normalized;
		}

		max = -1;
		maxVal = -1.0;
		for (int i = 0; i < area; i++) {
			tempVal = viterbi[path.length - 1][i];
			if (tempVal > maxVal) {
				maxVal = tempVal;
				max = i;
			}
		}
		currVal = max;
		finalPath.addFirst(currVal);
		for (int color = path.length - 1; color > 0; color--) {
			prevVal = prev_viterbi[color][currVal];
			finalPath.addFirst(prevVal);
			currVal = prevVal;
		}

		System.out.println("Final path with probability is:" + maxVal);
		System.out.println(finalPath);
	}
	
	/*
	 * Normal method to calculate multiplication of matrix.
	 */
	public static double[] calc(double[][] matrix, double[] message) {
		double[] result = new double[message.length];
		double rowSum = 0;

		for (int y = 0; y < message.length; y++) {
			rowSum = 0;
			for (int x = 0; x < message.length; x++) {
				rowSum += (matrix[y][x] * message[x]);
			}
			result[y] = rowSum;
		}
		return result;
	}
	
	public static double[] multiply(double[] m1, double[] m2) {
		double[] res = new double[m1.length];
		for (int i = 0; i < m1.length; i++)
			res[i] = m1[i] * m2[i];

		return res;
	}


}
