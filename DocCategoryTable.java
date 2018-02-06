package Classify;

import java.io.File;


public class DocCategoryTable extends main 
{	
	public int[][] trainingDocTable = new int[numberOfTrainingDocs][numberOfWords+1];
	public int[][] testingDocTable = new int[numberOfTestingDocs][numberOfWords+1];
	
	public void creatTrainingTable(File dataFile)
	{				
		//Load the Test and Training files.
		LoadFiles obj = new LoadFiles();		
		obj.loatTrainingFile(dataFile);
		
		trainingDocTable = buildTable(numberOfTrainingDocs, obj.numberOfTrainingTuples, obj.trainingArray);	
	}
	
	public void creatTestingTable(File dataFile)
	{
		//Load the Test and Training files.
		LoadFiles obj = new LoadFiles();		
		obj.loatTestingFile(dataFile);
		
		trainingDocTable = buildTable(numberOfTestingDocs, obj.numberOfTestingTuples, obj.testingArray);	
	}
	
	public int[][] buildTable(int numberOfDocuments, int numberOfTuples, int[][] TArray)
	{
		int[][] A = new int[numberOfDocuments][numberOfWords+1];

		//Full the table with zero values
		for(int i=0; i<numberOfDocuments; i++)
			for(int j=0; j<numberOfWords+1; j++)
				A[i][j] = 0;
		
        for (int i=0; i<numberOfTuples;i++)
        {
        		A[TArray[i][1]][TArray[i][2]] = TArray[i][3];
        }
        return A;		
	}
}
