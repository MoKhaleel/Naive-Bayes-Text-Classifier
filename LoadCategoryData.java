package Classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class LoadCategoryData extends main
{
	// These two lists holds the fiels test_lables and train_lables
	public int[] trainingCategoryTable = new int[numberOfTrainingDocs];
	public int[] testingCategoryTable = new int[numberOfTestingDocs];
		
		
	public void loadTrainingCategory (File dataFile)
	{
		trainingCategoryTable = loadFilesToJava (dataFile, numberOfTrainingDocs);
	}
		
	public void loadTestingCategory (File dataFile)
	{
		testingCategoryTable = loadFilesToJava (dataFile, numberOfTestingDocs);
	}
		
	public int[] loadFilesToJava (File dataFile, int numberOfDoc)
	{
		int[] A = new int[numberOfDoc];
		try
        {	
            // read from the file using FileReader
            FileInputStream in = new FileInputStream ( dataFile );
            InputStreamReader str = new InputStreamReader ( in, "UTF-8" );
            BufferedReader br = new BufferedReader ( str );
            // initialize index
            StringBuffer word = new StringBuffer ( );
            Character character;
            // one line of text
            String oneLine = "";
            // THIS VECTOR CONTAIN ONE WORDS
            Vector wordsVector = new Vector( );
            // read in the text a line at a time
            while ( ( oneLine = br.readLine ( ) ) != null )
            {
                // add spaces at the end of the line
                oneLine = oneLine + "  ";
                // tokenize each line
                for ( int i=0; i<oneLine.length ( ); i++ )
                {
                    // if the character is not a space, append it to a word
                	character = new Character(oneLine.charAt(i));
                    if( ! ( (character.isWhitespace(oneLine.charAt(i))) || (character == ',') ) )
                    {
                        word.append ( oneLine.charAt ( i ) );
                    }
                    // otherwise, add it to the vector "wordsVector"...
                    else
                    {
                        if ( word.length ( ) != 0 )
                        {
                            wordsVector.addElement ( word.toString ( ) );
                            word.setLength ( 0 );
                        }
                    }
                }
            }
            // Now we have the vector "wordsVector" that contain all the words, "attributes",
            // of our schema table, then we should copy it in to the one dimintional array...
            for (int i=0; i<numberOfDoc;i++)
            {
            	A[i] = Integer.parseInt(wordsVector.get(i).toString());
            }
            // close the FileReader
            br.close ( );
            in.close ( );
        }
        catch ( IOException e )
        {
            // insert an error message box here
            System.out.println ( "Could not open file " );
        }
		
		return A;
    }
}