package Classify;

import java.io.File;



public class main 
{
	public static int numberOfCategory = 20;
	public static int numberOfTrainingTuples = 1467345;
	public static int numberOfTestingTuples = 967874;
	public static int numberOfTrainingDocs = 11269;
	public static int numberOfTestingDocs = 7505;
	public static int numberOfWords = 61189;
	
	static File categoryFile = new File("20newsgroups/map.csv");
	static File TrainData = new File("20newsgroups/train_data.csv");
	static File trainCategory = new File("20newsgroups/train_label.csv");
	static File TestData = new File("20newsgroups/test_data.csv");
	static File testCategory = new File("20newsgroups/test_label.csv");

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub				
		
		//========================================================================================================================		
		
		//Load the Categories file.				
		LoadNewsLabels obj = new LoadNewsLabels();		
		obj.categoryList(categoryFile);

		//========================================================================================================================		
		
		//Calculate class prior P(wj)
		PriorKnowledge obj1 = new PriorKnowledge();
		obj1.calculatePrior(trainCategory);
		System.out.println("\n\n1.Class priors: \n--------------");
		for(int i=0; i<numberOfCategory; i++)
			System.out.println("P(" + obj.category[i] + ") = " + obj1.prior[i]);
		
		//========================================================================================================================
		
		TextClassifiers obj3 = new TextClassifiers();
		obj3.Bayesianestimator();
		obj3.MaximumLikelihood();		
	}
}