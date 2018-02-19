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

class MazeApp
{
	public static final int path = 10;
	public static void main(String[] args) throws Exception
	{
		LoadMaze loadmaze = LoadMaze.readContents();
		@SuppressWarnings("static-access")
		HMM hmm = new HMM(loadmaze,loadmaze.y_axis,path);
		hmm.filtering();
		hmm.fbSmoothingAlgo();
		hmm.viterbiAlgo();
	} 
	 
}