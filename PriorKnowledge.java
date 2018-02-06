package Classify;

import java.io.File;


public class PriorKnowledge extends main
{
	// These two arrays hold the number of each category in the training dataset and the prior value for each category.
	public int[] countCategory = new int[numberOfCategory];
	public double[] prior = new double[numberOfCategory];
	
	public void calculatePrior(File dataFile)
	{		
		for(int i=0; i<numberOfCategory; i++)
			countCategory[i]=0;
		
		LoadCategoryData obj = new LoadCategoryData();		
		obj.loadTrainingCategory(dataFile);
		
		for(int i=0; i<numberOfTrainingDocs; i++)
		{
			countCategory[obj.trainingCategoryTable[i]-1] += 1;
		}
		
		for(int i=0; i<numberOfCategory; i++)
		{
			prior[i] = (double) countCategory[i]/numberOfTrainingDocs;
		}
		
	}
	public void countCategoryTest()
	{
		for(int i=0; i<numberOfCategory; i++)
			countCategory[i]=0;
		
		LoadCategoryData obj = new LoadCategoryData();		
		obj.loadTestingCategory(testCategory);
		
		for(int i=0; i<numberOfTestingDocs; i++)
		{
			countCategory[obj.testingCategoryTable[i]-1] += 1;
		}		
	}
	
	public void countCategoryTrain()
	{
		for(int i=0; i<numberOfCategory; i++)
			countCategory[i]=0;
		
		LoadCategoryData obj = new LoadCategoryData();		
		obj.loadTrainingCategory(trainCategory);
		
		for(int i=0; i<numberOfTrainingDocs; i++)
		{
			countCategory[obj.trainingCategoryTable[i]-1] += 1;
		}
	}
}
