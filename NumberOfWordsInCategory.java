package Classify;



public class NumberOfWordsInCategory extends main
{
	public int[] noOFWordsPerCategory = new int[numberOfCategory];
	
	public void countWords()
	{
		for(int i=0; i<numberOfCategory; i++)
			noOFWordsPerCategory[i]=0;
		
		LoadCategoryData obj = new LoadCategoryData();		
		obj.loadTrainingCategory(trainCategory);
		
		LoadFiles obj1 = new LoadFiles();
		obj1.loatTrainingFile(TrainData);
		
		for(int i=0; i<numberOfTrainingTuples; i++)
			noOFWordsPerCategory[obj.trainingCategoryTable[obj1.trainingArray[i][0]-1]-1] += obj1.trainingArray[i][2]; 	
	}
}