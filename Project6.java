//******************************************************************************
//
//  Developer:     Michael Franklin
//
//  Project #:     Project 6
//
//  File Name:     Project6.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      04/03/2022
//
//  Instructor:    Fred Kumi 
//
//  Description:   Program source for Project 6.
//
//
//******************************************************************************

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Scanner;

public class Project6 {

	// constants
	private final boolean debug = false;
	private final String outputFilename = "Project6-Output.txt";
	private final String[] goodResponses = {
		"Excellent!",
		"Very good!",
		"Nice work!",
		"Way to go!",
		"Keep up the good work!"
	};
	private final String[] badResponses = {
		"That is incorrect!",
		"No. Please try again!",
		"Wrong, Try once more!",
		"No. Don’t give up!",
		"Incorrect. Keep trying!"
	};
	private final String[] operatorStrings = {
			"*",
			"%",
			"+",
			"-",
		};
	private final String[] levelNames = {
		"Basic",
		"Intermediate",
		"Advanced"
	};
	
	// vars
	private boolean quit;
	private Scanner inputStream;
	private int score;
	private int level;
	private int[] levelScores = {-1,-1,-1};
	private String expression;
	private int answer;
	private SecureRandom rand;
	private ExpressionSolver solver;
	private Logger logger;
	
	// ***************************************************************
	//
	// Method: main
	//
	// Description: The main method of the program
	//
	// Parameters: String array
	//
	// Returns: N/A
	//
	// **************************************************************
	public static void main(String argvs[])
	{
		// create class object
		Project6 prog = new Project6();
		
		// setup program vars
		try
		{
			prog.Setup();
		}
		catch(Exception e)
		{
			// error setting up random generator
			if (e instanceof NoSuchAlgorithmException)
			{
				
			}
			else if (e instanceof NoSuchProviderException)
			{

			}
			// print error
			System.err.println("Error During Setup:");
			e.printStackTrace();
			
			// then quit
			System.exit(0);
		}
		
		// welcome message
		prog.developerInfo();
		prog.DisplayWelcome();
		
		// testing chunk
		/*prog.level = 0;
		for (int i = 0; i < 10; i++)
		{
			prog.GenerateQuestion();
			logger.Log();
		}
		prog.quit = true;*/
		
		// main loop
		if (!prog.quit)
		{
			// prompt an initial difficulty menu
			prog.PromptInitialDiffMenu();
		}
		while(!prog.quit)
		{
			// question loop
			do {
				// generate a question and its answer
				prog.GenerateQuestion();
				
				// get input and keep trying for correct answer
				prog.PromptQuestion();
				
			}while (prog.score < 5);
			
			// prompt user with quit menu
			prog.PromptQuitMenu();
		}
		
		// display results
		prog.DisplayResults();
	}

	
	// ***************************************************************
	//
	// Method: Setup
	//
	// Description: Sets up the program's various vars and streams
	//
	// Parameters: None
	//
	// Returns: N/A
	//
	// **************************************************************
	public void Setup() throws NoSuchAlgorithmException, NoSuchProviderException 
	{
		quit = false;
		inputStream = new Scanner(System.in);
		solver = new ExpressionSolver();
		score = 0;
		level = 0;
		expression = "";
		answer = 1;
		rand = SecureRandom.getInstance("SHA1PRNG", "SUN");
		logger = new Logger(outputFilename);
	}
	
	
	// ***************************************************************
	//
	// Method: DisplayWelcome
	//
	// Description: displays a welcome message to the user
	//
	// Parameters: None
	//
	// Returns: N/A
	//
	// **************************************************************
	public void DisplayWelcome()
	{
		logger.Log("Welecome to LIFT:\nLearning Is Fun - Training.\n");
	}
	
	
	// ***************************************************************
	//
	// Method: GenerateQuestion
	//
	// Description: Generates a random expression and its answer based on 
	//				level
	//
	// Parameters: None
	//
	// Returns: N/A
	//
	// **************************************************************
	public void GenerateQuestion()
	{
		// clear expression
		expression = "";
		
		// generate first digit
		expression += "" + (rand.nextInt(9)+1);
		
		// append operands and digits based on current level
		for (int i = 0; i <= level; i++)
		{
			// append operator
			expression += " " +  operatorStrings[rand.nextInt(operatorStrings.length)];
			
			// append digit
			expression += " " + (rand.nextInt(9)+1);
		}

		// calculate answer
		answer = solver.Calculate(expression,debug);
	}
	
	
	// ***************************************************************
	//
	// Method: PromptQuestion
	//
	// Description: Prompts generated question to the user, gets input, and 
	//				checks answer. Only returns once the answer input is 
	//				correct.
	//
	// Parameters: None
	//
	// Returns: N/A
	//
	// **************************************************************
	public void PromptQuestion()
	{
		boolean correct = false;
		
		// while the question has not been answered correctly
		while (!correct)
		{
			// display the question
			logger.Log(score + 1 + ". Solve:\n\n" + expression + "\n");
			
			// get user input
			int input = GetUserInput();
			
			// check answer
			correct = input == answer;
			
			// display message based on correctness
			if (correct)
			{
				// display correct response
				logger.Log(GetGoodResponse());
				
				// increment score
				score++;
			}
			else
			{
				// display incorrect response
				logger.Log(GetBadResponse());
			}
			
			// add a new line for better readablity
			logger.Log();
		}
	}
	
	
	// ***************************************************************
	//
	// Method: GetUserInput
	//
	// Description: Gets and validates user input.
	//
	// Parameters: None
	//
	// Returns: Integer: the validated input from the user
	//
	// **************************************************************
	public int GetUserInput()
	{
		int response = -1;
		boolean valid = false;
		
		while (!valid)
		{
			// get user input
			String input = "";
			while (input.equals(""))
			{
				input = inputStream.nextLine();
			}
			
			// validate input
			try
			{
				response = Integer.parseInt(input);
				
				// parse worked, valid input
				valid = true;
			}
			catch(NumberFormatException e)
			{
				// not int input, discard input				
			}
		}
		
		// log input to file
		logger.WriteToFile("" + response + "\n");
		
		// return valid input
		return response;
	}
	
	
	// ***************************************************************
	//
	// Method: GetGoodResponse
	//
	// Description: Gets a random correct answer response
	//
	// Parameters: None
	//
	// Returns: String: Response chosen
	//
	// **************************************************************
	public String GetGoodResponse()
	{
		return goodResponses[rand.nextInt(goodResponses.length)];
	}
	
	
	// ***************************************************************
	//
	// Method: GetBadResponse
	//
	// Description: Gets a random incorrect answer response
	//
	// Parameters: None
	//
	// Returns: String: Response chosen
	//
	// **************************************************************
	public String GetBadResponse()
	{
		return badResponses[rand.nextInt(badResponses.length)];
	}
		
	
	// ***************************************************************
	//
	// Method: PromptQuitMenu
	//
	// Description: displays a quit menu to the user, giving options for 
	//				advance level, continue, and quit. Also handles user 
	//				input and responses in menu.
	//
	// Parameters: None
	//
	// Returns: N/A
	//
	// **************************************************************
	public void PromptQuitMenu()
	{
		int cap = (level < 2) ? ((level == 0) ? 4 : 3) : 2;
		int input = 0;
		while (input < 1 || input > cap)
		{
			// display quit menu
			logger.Log("You are doing well.");
			if (level < 2)
				logger.Log("Ready for the next level?\n");
			else
				logger.Log();
			
			logger.Log("1. Contiue at " + levelNames[level] + " Level");
			
			if (level == 0)
				logger.Log("2. Advance to " + levelNames[level + 1] + " Level\n3. Advance to " + levelNames[level + 2] + " Level\n4. Quit\n");
			else if (level == 1)
				logger.Log("2. Advance to " + levelNames[level + 1] + " Level\n3. Quit\n");
			else
				logger.Log("2. Quit");
			
			// get user response
			input = GetUserInput();
			
			if (input < 1 || input > cap)
				logger.Log("Please input a valid menu option.");
		}
		
		// handle user response
		if (level == 0)
		{
			// advance menu
			switch(input)
			{
			case 1:
				// continue
				// do nothing, program will continue at current level
				break;
			case 2:
				// advance to next level
				AdvanceLevel(level+1);
				break;
			case 3:
				// advance to last level
				AdvanceLevel(level+2);
				break;
			case 4:
				// quit
				// log current score
				LogScore();
				quit = true;
				break;
			}
		}
		else if (level == 1)
		{
			// advance menu
			switch(input)
			{
			case 1:
				// continue
				// do nothing, program will continue at current level
				break;
			case 2:
				// advance
				AdvanceLevel(level+1);
				break;
			case 3:
				// quit
				// log current score
				LogScore();
				quit = true;
				break;
			}
			
		}
		else
		{
			// continue menu
			switch(input)
			{
			case 1:
				// continue
				// do nothing, program will continue at current level
				break;
			case 2:
				// quit
				// log current score
				LogScore();
				quit = true;
				break;
			}
		}
	}
		
	
	// ***************************************************************
	//
	// Method: PromptInitialDiffMenu
	//
	// Description: displays an initial difficulty menu to the user, 
	//				giving options for basic, intermediate, and advanced 
	//				difficulty levels. Also handles user input and 
	//				responses in menu.
	//
	// Parameters: None
	//
	// Returns: N/A
	//
	// **************************************************************
	public void PromptInitialDiffMenu()
	{
		int input = 0;
		while (input < 1 || input > 3)
		{
			// display difficulty menu
			logger.Log("Please Select a Difficulty Level:");
			logger.Log();
			
			logger.Log("1. Basic – 2 Operands and 1 Operator\n"
					+ "2. Intermediate – 3 Operands and 2 Operators\n"
					+ "3. Advanced – 4 Operands and 3 Operators");
			logger.Log();
			
			// get user response
			input = GetUserInput();
			
			if (input < 1 || input > 3)
				logger.Log("Please input a valid menu option.");
		}
		
		// set difficulty level based on input
		level = input - 1;
	}
	
	
	// ***************************************************************
	//
	// Method: LogScore
	//
	// Description: logs the current level score
	//
	// Parameters: None
	//
	// Returns: N/A
	//
	// **************************************************************
	public void LogScore()
	{
		// log score
		levelScores[level] = score;
	}
	
	
	// ***************************************************************
	//
	// Method: AdvanceLevel
	//
	// Description: advances user to the passed level of difficulty. Logs 
	//				current difficulty score and resets running score for 
	//				the next level
	//
	// Parameters: Integer: level to advance to
	//
	// Returns: N/A
	//
	// **************************************************************
	public void AdvanceLevel(int lvl)
	{
		// log score
		LogScore();
		
		// advance to the next level
		this.level = lvl;
		
		// reset current score
		score = 0;
		
		// print out level welcome message
		logger.Log("Welecome to the " + levelNames[level] + " Level.\n");
	}
	
	
	// ***************************************************************
	//
	// Method: DisplayResults
	//
	// Description: displays the final scores for each level to the user
	//
	// Parameters: None
	//
	// Returns: N/A
	//
	// **************************************************************
	public void DisplayResults()
	{
		// output results
		logger.Log("\nFinal Results:");
		
		for (int i = 0; i <= level; i ++)
		{
			if (levelScores[i] != -1)
				logger.Log(levelNames[i] + " Level Score: " + levelScores[i]);
		}
		
		logger.Log("\nThank you for using LIFT\n");
		
		// close streams
		inputStream.close();
		logger.CloseFile();
	}
	
	
	//***************************************************************
	//
	//  Method:       developerInfo (Non Static)
	// 
	//  Description:  The developer information method of the program
	//
	//  Parameters:   None
	//
	//  Returns:      N/A 
	//
	//**************************************************************
	public void developerInfo()
	{
		logger.Log("Name:    Michael Franklin");
		logger.Log("Course:  COSC 4301 Modern Programming");
		logger.Log("Project: Six\n");
	}
	
}
