package Classify;

public class TextClassifiers extends main
{
	// this array calculate nk: number of times word wk occurs in all documents in class wj.
	public int[][] nk = new int[numberOfWords][numberOfCategory];
	public double[][] PMLE = new double[numberOfWords][numberOfCategory];
	public double[][] BE = new double[numberOfWords][numberOfCategory];
	public double[][] classTrainPMLE = new double[numberOfTrainingDocs][numberOfCategory];
	public double[][] classTrainBE = new double[numberOfTrainingDocs][numberOfCategory];
	public int[] finalTrainClassPMLE = new int[numberOfTrainingDocs];
	public int[] finalTrainClassBE = new int[numberOfTrainingDocs];
	public double[][] classTestPMLE = new double[numberOfTestingDocs][numberOfCategory];
	public double[][] classTestBE = new double[numberOfTestingDocs][numberOfCategory];
	public int[] finalTestClassPMLE = new int[numberOfTestingDocs];
	public int[] finalTestClassBE = new int[numberOfTestingDocs];
	public int[][] confusionMatrix = new int[numberOfCategory][numberOfCategory]; 
	
	public void MaximumLikelihood()
	{
		calculateNk();
		calculatePMLE();
		classifyTrainPMLE();
		calculateResultsTrainPMLE();
		classifyTestPMLE();
		calculateResultsTestPMLE();
	}
	
	public void Bayesianestimator()
	{
		calculateNk();
		calculateBE();		
		classifyTrainBE();
		calculateResultsTrainBE();
		classifyTestBE();
		calculateResultsTestBE();
	}

	public void calculateNk()
	{
		LoadCategoryData obj = new LoadCategoryData();		
		obj.loadTrainingCategory(trainCategory);
		
		LoadFiles obj1 = new LoadFiles();
		obj1.loatTrainingFile(TrainData);
		
		for(int i=0; i<numberOfWords; i++)
			for(int j=0; j<numberOfCategory; j++)
				nk[i][j]=0;
		
		for (int i=0; i<numberOfTrainingTuples; i++)
		{
			nk[obj1.trainingArray[i][1]-1][obj.trainingCategoryTable[obj1.trainingArray[i][0]-1]-1] += obj1.trainingArray[i][2];
		}		
	}
	
	public void calculatePMLE()
	{	
		NumberOfWordsInCategory obj1 = new NumberOfWordsInCategory();		
		obj1.countWords();
		
		for(int i=0; i<numberOfWords; i++)
			for(int j=0; j<numberOfCategory; j++)
			{
				PMLE[i][j] = (double) nk[i][j] / obj1.noOFWordsPerCategory[j];
				//Take the natural logarithm (ln) of the values

					PMLE[i][j] = java.lang.Math.log(PMLE[i][j]);
			}		
	}
	
	public void classifyTrainPMLE()
	{
		PriorKnowledge obj = new PriorKnowledge();
		obj.calculatePrior(trainCategory);
		
		LoadFiles obj2 = new LoadFiles();
		obj2.loatTrainingFile(TrainData);
		
		for(int i=0; i<numberOfTrainingDocs; i++)
			for(int j=0; j<numberOfCategory; j++)
			{
				classTrainPMLE[i][j] = java.lang.Math.log((double) obj.prior[j]);
			}
		
		for(int i=0; i<numberOfTrainingTuples; i++)
			for(int j=0; j<numberOfCategory; j++)
			{
				classTrainPMLE[obj2.trainingArray[i][0]-1][j] += (double) obj2.trainingArray[i][2] * PMLE[obj2.trainingArray[i][1]-1][j]; 
			}		
				
		double temp; int index;
		for(int i=0; i<numberOfTrainingDocs; i++)
		{
			temp = classTrainPMLE[i][0];
			index = 0;
			for(int j=1; j<numberOfCategory; j++)
			{
				if (classTrainPMLE[i][j] > temp)
				{
					temp = classTrainPMLE[i][j];
					index = j;
				}
			}
			finalTrainClassPMLE[i] = index + 1;
		}		
	}
	
	public void calculateResultsTrainPMLE()
	{
		LoadCategoryData obj = new LoadCategoryData();
		obj.loadTrainingCategory(trainCategory);
		
		LoadNewsLabels obj1 = new LoadNewsLabels();
		obj1.categoryList(categoryFile);
		
		PriorKnowledge obj2 = new PriorKnowledge();
		obj2.countCategoryTrain();
		
		System.out.println("\n\n3 Results based on Maximum Likelihood estimator: ");

		
		// Calculate the overall accuracy
		double Accuracy = 0, count = 0;
		for(int i=0; i<numberOfTrainingDocs; i++)
		{
			if (finalTrainClassPMLE[i] == obj.trainingCategoryTable[i])
				count++;
		}	

		Accuracy = count / numberOfTrainingDocs;		
		System.out.println("\n3.1).Training Data on Maximum Likelihood: \n\nOverall Accuracy = " + Accuracy + "\n");		

		// Calculate the accuracy per category
		int cat = 1, i=0; 
		while(i<numberOfTrainingDocs)
		{
			Accuracy = 0; count = 0;;
			while (i<numberOfTrainingDocs && obj.trainingCategoryTable[i] == cat)
			{
				if (finalTrainClassPMLE[i] == obj.trainingCategoryTable[i])
					count++;
				i++;
			}
			Accuracy = count / obj2.countCategory[cat-1];		
			System.out.println("Accuracy of Category " + cat + ". " + obj1.category[cat-1] + " = " + Accuracy);
			cat++;
		}
		
		calculateConfusionMatrix(obj.trainingCategoryTable,finalTrainClassPMLE,numberOfTrainingDocs);

	}
	
	public void classifyTestPMLE()
	{
		PriorKnowledge obj = new PriorKnowledge();
		obj.calculatePrior(trainCategory);
		
		LoadFiles obj2 = new LoadFiles();
		obj2.loatTestingFile(TestData);
		
		for(int i=0; i<numberOfTestingDocs; i++)
			for(int j=0; j<numberOfCategory; j++)
			{
				classTestPMLE[i][j] = java.lang.Math.log((double) obj.prior[j]);
			}
		
		for(int i=0; i<numberOfTestingTuples; i++)
			for(int j=0; j<numberOfCategory; j++)
			{
				classTestPMLE[obj2.testingArray[i][0]-1][j] += (double) obj2.testingArray[i][2] * PMLE[obj2.testingArray[i][1]-1][j]; 
			}		
				
		double temp; int index;
		for(int i=0; i<numberOfTestingDocs; i++)
		{
			temp = classTestPMLE[i][0];
			index = 0;
			for(int j=1; j<numberOfCategory; j++)
			{
				if (classTestPMLE[i][j] > temp)
				{
					temp = classTestPMLE[i][j];
					index = j;
				}
			}
			finalTestClassPMLE[i] = index + 1;
		}		
	}
	
	public void calculateResultsTestPMLE()
	{
		LoadCategoryData obj = new LoadCategoryData();
		obj.loadTestingCategory(testCategory);
		
		LoadNewsLabels obj1 = new LoadNewsLabels();
		obj1.categoryList(categoryFile);
		
		PriorKnowledge obj2 = new PriorKnowledge();
		obj2.countCategoryTest();
		
		// Calculate the overall accuracy
		double TestingAccuracy = 0, count = 0;
		for(int i=0; i<numberOfTestingDocs; i++)
		{
			if (finalTestClassPMLE[i] == obj.testingCategoryTable[i])
				count++;
		}	

		TestingAccuracy = count / numberOfTestingDocs;		
		System.out.println("\n3.2).Testing Data on Maximum Likelihood: \n\nOverall Accuracy = " + TestingAccuracy + "\n");		

		// Calculate the accuracy per category
		int cat = 1, i=0; 
		while(i<numberOfTestingDocs)
		{
			TestingAccuracy = 0; count = 0;
			while (i<numberOfTestingDocs && obj.testingCategoryTable[i] == cat)
			{
				if (finalTestClassPMLE[i] == obj.testingCategoryTable[i])
					count++;
				i++;
			}
			TestingAccuracy = count / obj2.countCategory[cat-1];		
			System.out.println("Accuracy of Category " + cat + ". " + obj1.category[cat-1] + " = " + TestingAccuracy);
			cat++;
		}
		
		calculateConfusionMatrix(obj.testingCategoryTable,finalTestClassPMLE,numberOfTestingDocs);
	}
	
	//=============================================================================================================================
		
	public void calculateBE()
	{		
		NumberOfWordsInCategory obj1 = new NumberOfWordsInCategory();		
		obj1.countWords();
		
		for(int i=0; i<numberOfWords; i++)
			for(int j=0; j<numberOfCategory; j++)
			{
				BE[i][j] = ((double) nk[i][j] + 1) / ((double) obj1.noOFWordsPerCategory[j] + (double) numberOfWords);
				//Take the natural logarithm (ln) of the values
				BE[i][j] = java.lang.Math.log(BE[i][j]);
			}	
	}
	
	public void classifyTrainBE()
	{
		PriorKnowledge obj = new PriorKnowledge();
		obj.calculatePrior(trainCategory);
		
		LoadFiles obj2 = new LoadFiles();
		obj2.loatTrainingFile(TrainData);
		
		for(int i=0; i<numberOfTrainingDocs; i++)
			for(int j=0; j<numberOfCategory; j++)
			{
				classTrainBE[i][j] = java.lang.Math.log((double) obj.prior[j]);
			}
		
		for(int i=0; i<numberOfTrainingTuples; i++)
			for(int j=0; j<numberOfCategory; j++)
			{
				classTrainBE[obj2.trainingArray[i][0]-1][j] += ( (double) obj2.trainingArray[i][2] * (double) BE[obj2.trainingArray[i][1]-1][j] ); 
			}
				
		double temp; int index;
		for(int i=0; i<numberOfTrainingDocs; i++)
		{
			temp = classTrainBE[i][0];
			index = 0;
			for(int j=1; j<numberOfCategory; j++)
			{
				if (classTrainBE[i][j] > temp)
				{
					temp = classTrainBE[i][j];
					index = j;
				}
			}
			finalTrainClassBE[i] = index + 1;
			//System.out.println(finalTrainClassBE[i]);
		}		
	}
	
	public void calculateResultsTrainBE()
	{
		LoadCategoryData obj = new LoadCategoryData();
		obj.loadTrainingCategory(trainCategory);
		
		LoadNewsLabels obj1 = new LoadNewsLabels();
		obj1.categoryList(categoryFile);
		
		PriorKnowledge obj2 = new PriorKnowledge();
		obj2.countCategoryTrain();
		
		System.out.println("\n\n2.Results based on Bayesian estimator: ");
			
		// Calculate the overall accuracy
		double Accuracy = 0, count = 0;
		for(int i=0; i<numberOfTrainingDocs; i++)
		{
			if (finalTrainClassBE[i] == obj.trainingCategoryTable[i])
				count++;
		}	

		Accuracy = count / numberOfTrainingDocs;		
		System.out.println("\n2.1).Training Data on Bayesian estimator: \n\nOverall Accuracy = " + Accuracy + "\n");		

		// Calculate the accuracy per category
		int cat = 1, i=0; 
		while(i<numberOfTrainingDocs)
		{
			Accuracy = 0; count = 0;;
			while (i<numberOfTrainingDocs && obj.trainingCategoryTable[i] == cat)
			{
				if (finalTrainClassBE[i] == obj.trainingCategoryTable[i])
					count++;
				i++;
			}
			Accuracy = count / obj2.countCategory[cat-1];		
			System.out.println("Accuracy of Category " + cat + ". " + obj1.category[cat-1] + " = " + Accuracy);
			cat++;
		}
		
		calculateConfusionMatrix(obj.trainingCategoryTable,finalTrainClassBE,numberOfTrainingDocs);
	}
	
	public void classifyTestBE()
	{
		PriorKnowledge obj = new PriorKnowledge();
		obj.calculatePrior(trainCategory);
		
		LoadFiles obj2 = new LoadFiles();
		obj2.loatTestingFile(TestData);
		
		for(int i=0; i<numberOfTestingDocs; i++)
			for(int j=0; j<numberOfCategory; j++)
			{
				classTestBE[i][j] = java.lang.Math.log((double) obj.prior[j]);
			}
		
		for(int i=0; i<numberOfTestingTuples; i++)
			for(int j=0; j<numberOfCategory; j++)
			{
				classTestBE[obj2.testingArray[i][0]-1][j] += (double) obj2.testingArray[i][2] * BE[obj2.testingArray[i][1]-1][j]; 
			}		
				
		double temp; int index;
		for(int i=0; i<numberOfTestingDocs; i++)
		{
			temp = classTestBE[i][0];
			index = 0;
			for(int j=1; j<numberOfCategory; j++)
			{
				if (classTestBE[i][j] > temp)
				{
					temp = classTestBE[i][j];
					index = j;
				}
			}
			finalTestClassBE[i] = index + 1;
		}		
	}
	
	public void calculateResultsTestBE()
	{
		LoadCategoryData obj = new LoadCategoryData();
		obj.loadTestingCategory(testCategory);
		
		LoadNewsLabels obj1 = new LoadNewsLabels();
		obj1.categoryList(categoryFile);
		
		PriorKnowledge obj2 = new PriorKnowledge();
		obj2.countCategoryTest();
		
		// Calculate the overall accuracy
		double TestingAccuracy = 0, count = 0;
		for(int i=0; i<numberOfTestingDocs; i++)
		{
			if (finalTestClassBE[i] == obj.testingCategoryTable[i])
				count++;
		}	

		TestingAccuracy = count / numberOfTestingDocs;		
		System.out.println("\n2.2).Testing Data on Bayesian estimator: \n\nOverall Accuracy = " + TestingAccuracy + "\n");		

		// Calculate the accuracy per category
		int cat = 1, i=0; 
		while(i<numberOfTestingDocs)
		{
			TestingAccuracy = 0; count = 0;;
			while (i<numberOfTestingDocs && obj.testingCategoryTable[i] == cat)
			{
				if (finalTestClassBE[i] == obj.testingCategoryTable[i])
					count++;
				i++;
			}
			TestingAccuracy = count / obj2.countCategory[cat-1];		
			System.out.println("Accuracy of Category " + cat + ". " + obj1.category[cat-1] + " = " + TestingAccuracy);
			cat++;
		}
		
		calculateConfusionMatrix(obj.testingCategoryTable,finalTestClassBE,numberOfTestingDocs);
	}
	
	//==========================================================================================================================
	public void calculateConfusionMatrix (int[] real, int[] classified, int size)
	{
		// Initalize the Confusion Matrix
		for (int i=0; i<numberOfCategory; i++)
			for (int j=0; j<numberOfCategory; j++)
				confusionMatrix[i][j] = 0;
		
		// calculate the Confusion Matrix
		for (int i=0; i<size; i++)
			confusionMatrix[real[i]-1][classified[i]-1] += 1 ;

		
		// Print the Confusion Matrix
		System.out.println("\nThe Confusion Matrix: \nReal Categories (rows) \\ Classification Results (columns)");
		System.out.print("     ");
		for(int i=1; i<=20; i++)
			if (i<10)
				System.out.print(" "+ i + "  |");
			else
				System.out.print(" "+ i + " |");
		System.out.println("\n---------------------------------------------------------------------------------------------------------");

		for (int i=0; i<numberOfCategory; i++)
		{
			if (i<9)
				System.out.print(" "+ (i+1) + "  | ");
			else
				System.out.print(" "+ (i+1) + " | ");
			for (int j=0; j<numberOfCategory; j++)
			{
				System.out.printf("%3d", confusionMatrix[i][j]); 
				if (j<numberOfCategory-1)
					System.out.print("  ");
			}
			System.out.print(" |");
			System.out.println("\n---------------------------------------------------------------------------------------------------------|");		}
	}
}