//******************************************************************************
//
//  Developer:     Michael Franklin
//
//  Project #:     Project 6
//
//  File Name:     Logger.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      04/03/2022
//
//  Instructor:    Fred Kumi 
//
//  Description:   writes data to a file. Also supports simultaneous console //				   and file output
//
//
//******************************************************************************

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	
	private FileWriter fileStream;
	
	public Logger(String filename)
	{
		// open file at filename
		OpenFileForWriting(filename);
	}
	
	
	//***************************************************************
	//
	//  Method:       OpenFileForWriting
	// 
	//  Description:  opens a file at passed filename for writing
	//
	//  Parameters:   String: filename to output file
	//
	//  Returns:      boolean: true if open was successful, false
	//				  otherwise
	//
	//**************************************************************
	private boolean OpenFileForWriting(String filename)
	{
		boolean success = false;

		File file = new File(filename);
		try
		{
			this.fileStream = new FileWriter(file, true);
			success = true;
		}
		catch (Exception e)
		{
			if (e instanceof IOException)
			{
				System.err.println("File IO Error while opening output file.");
			}
			else
			{
				System.err.println("Unhandled Error while opening output file.");
			}
			e.printStackTrace();
			System.exit(0);
		}
		finally
		{
			if (!success)
			{
				// there was an error close the file
				CloseFile();
				System.exit(0);
			}
		}
		
		return success;
	}
	
	
	//***************************************************************
	//
	//  Method:       Log
	// 
	//  Description:  writes data to the open file and to the console
	//
	//  Parameters:   String: data to write to file and console
	//
	//  Returns:      boolean: true if write was successful, false
	//				  otherwise
	//
	//**************************************************************
	public boolean Log()
	{
		return Log("");
	}
	public boolean Log(String data)
	{
		boolean success = false;
		
		// print to console
		System.out.println(data);
		
		// write to file
		success = WriteToFile(data + "\n");
		
		return success;
	}

	
	//***************************************************************
	//
	//  Method:       WriteToFile
	// 
	//  Description:  writes data to the open file
	//
	//  Parameters:   String: data to write to file
	//
	//  Returns:      boolean: true if write was successful, false
	//				  otherwise
	//
	//**************************************************************
	public boolean WriteToFile(String data)
	{
		boolean success = false;
		
		// if there is a file open
		if (fileStream != null)
		{
			try
			{
				fileStream.write(data);
				fileStream.flush();
				success = true;
			}
			catch (Exception e)
			{
				if (e instanceof IOException)
				{
					System.err.println("File IO Error while writing to output file.");
				}
				else
				{
					System.err.println("Unhandled Error while writing to output file.");
				}
				e.printStackTrace();
			}
			finally
			{
				if (!success)
				{
					// there was an error close the file
					CloseFile();
					System.exit(0);
				}
			}
		}
		
		return success;
	}

	
	//***************************************************************
	//
	//  Method:       CloseFile
	// 
	//  Description:  closes the open output file
	//
	//  Parameters:   None
	//
	//  Returns:      N/A
	//
	//**************************************************************
	public void CloseFile()
	{
		try
		{
			fileStream.close();
		}
		catch (Exception e)
		{
			if (e instanceof IOException)
			{
				System.err.println("File IO Error while closing output file.");
			}
			else
			{
				System.err.println("Unhandled Error while closing output file.");
			}
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
}
