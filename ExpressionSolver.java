import java.util.Stack;

//******************************************************************************
//
//  Developer:     Michael Franklin
//
//  Project #:     Project 6
//
//  File Name:     ExpressionSolver.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      04/03/2022
//
//  Instructor:    Fred Kumi 
//
//  Description:   Expression Solver class, uses expression tress to solve 
//				   most expressions.
//				   Supports % * + - operators, non-negative integers only
//
//
//******************************************************************************

public class ExpressionSolver {

	private ExpressionNode tree;
	private boolean debug;
	
	// ***************************************************************
	//
	// Method: IsOperator
	//
	// Description: checks if the passed string is an operator or not
	//
	// Parameters: String: string to check
	//
	// Returns: Boolean: true if passed string is an operator, false if not
	//
	// **************************************************************
	private boolean IsOperator(String operator) {
        return (operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("%"));
    }
	
	
	// ***************************************************************
	//
	// Method: GetPostFixExpression / PostOrder
	//
	// Description: gets the postfix notation of the expression stored in 
	//				the expression tree
	//
	// Parameters: None
	//
	// Returns: String: postfix notation of the expression stored in the 
	//					expression tree
	//
	// **************************************************************
	public String GetPostFixExpression()
	{
        // strip the first space
		return PostOrder(tree).substring(1);
	}
	private String PostOrder(ExpressionNode root)
    {
		String postFix = "";
        if (root != null) {
        	postFix += PostOrder(root.left);
        	postFix += PostOrder(root.right);
    		postFix += " " + root.data;
        }

        return postFix;
    }
 
	
	// ***************************************************************
	//
	// Method: GetExpression / InOrder
	//
	// Description: gets the expression stored in the expression tree
	//
	// Parameters: None
	//
	// Returns: String: expression stored in the expression tree
	//
	// **************************************************************
	public String GetExpression()
	{ 
		// strip the first space
		return InOrder(tree).substring(1);
	}
    private String InOrder(ExpressionNode root)
    {
    	String inOrder = "";
        if (root != null) 
        {     
            inOrder += InOrder(root.left);
            inOrder += " " + root.data;
            inOrder += InOrder(root.right);
        }
        
        return inOrder;
    }
	
    
    // ***************************************************************
  	//
  	// Method: HasOperators
  	//
  	// Description: checks if passed string has operators
  	//
  	// Parameters: String: string to check
  	//
  	// Returns: Boolean: true if operators are found, false if not
  	//
  	// **************************************************************
    private Boolean HasOperators(String data)
	{
		 boolean found = false;
		 
		 int index = 0;
		 while (index < data.length() && !found)
		 {
			 char ch = data.charAt(index);
			 if (IsOperator("" + ch))
		 {
			 found = true;
		 }
		 else
		 {
			 // not a supported operator, keep looking
				 index++;
			 }
		 }
		 
		 return found;
	}
     
     
    // ***************************************************************
  	//
  	// Method: ToPostFix
  	//
  	// Description: converts a passed expression to its postfix notation
  	//
  	// Parameters: String: expression to convert to postfix notation
  	//
  	// Returns: String: the postfix notation of the expression
  	//
  	// **************************************************************
    private String ToPostFix(String expression)
    {
    	 String postfix = "";
    	 
    	 // check if this expression has any operators left
    	 if (HasOperators(expression))
    	 {
    		 // get the location of the last operator to solve for
    		 // note the backwards iteration to put higher operators
    		 // lower in the expression tree
        	 int operatorIndex = -1;
        	 int index = expression.length()-1;
        	 boolean tier1 = true;
        	 while (operatorIndex == -1)
        	 {
        		 char ch = expression.charAt(index);
        		 // check for tier 1 operators
        		 if (tier1 && (ch == '+' || ch == '-'))
        		 {
        			 operatorIndex = index;
        		 }
        		 // check for tier 2 operators if no tier 1 were found
        		 else if (!tier1 && (ch == '%' || ch == '*'))
        		 {
        			 operatorIndex = index;
        		 }
        		 else
        		 {
        			 // not a supported operator, keep looking
        			 index--;
            		 if (index < 0)
            		 {
            			 // we have found no tier 1 operators
            			 tier1 = false;
            			 
            			 // reset index
            			 index = expression.length()-1;
            		 }
        		 }
        	 }
        	 
        	 // split expression at the found operator
        	 String before = expression.substring(0,operatorIndex-1);
        	 String after = expression.substring(operatorIndex+1);
        	 String operator = expression.substring(operatorIndex, operatorIndex+1);
        	 
        	 // recurse
        	 postfix = ToPostFix(before) + ToPostFix(after) + " " + operator;
    	 }
    	 else
    	 {
    		 // return the expression as is no need to recurse any more
    		 postfix = expression;
    	 }
    	
    	 return postfix;
    }
     
     
    // ***************************************************************
 	//
 	// Method: Construct
 	//
 	// Description: constructs an expression tree given the postfix 
    //				notation of the expression
 	//
 	// Parameters: String: the postfix notation of the expression to store
 	//
 	// Returns: ExpressionNode: the root of the constructed expression tree
 	//
 	// **************************************************************
    private ExpressionNode Construct(String postfix)
    {
    	// create an empty stack to store tree pointers
    	Stack<ExpressionNode> s = new Stack<>();
    	
        if (postfix != null && postfix.length() != 0) {          
            // traverse the postfix expression
            String[] tokens = postfix.split(" ");
            for (String token: tokens)
            {
                // if the current token is an operator
                if (IsOperator(token))
                {
                    // pop two nodes `x` and `y` from the stack
                	ExpressionNode x = s.pop();
                	ExpressionNode y = s.pop();
     
                    // construct a new binary tree whose root is the 
                	// operator and whose left and right children point to 
                	// `y` and `x`, respectively
                	ExpressionNode node = new ExpressionNode(token, y, x);
     
                    // push the current node into the stack
                    s.add(node);
                }
                // if the current token is an operand, create a new binary 
                // tree node whose root is the operand and push it into the 
                // stack
                else {
                    s.add(new ExpressionNode(token));
                }
            }
        }
 
        // return the root of the expression tree
        return s.peek();
    }
    
    
    // ***************************************************************
   	//
   	// Method: Evaluate
   	//
   	// Description: calculates the answer to the passed operator and 
    //				operands
   	//
   	// Parameters: Integer: the first operand
    //			   Integer: the second operand
    //			   String: the operator
   	//
   	// Returns: Integer: the calculated answer
   	//
   	// **************************************************************
    private int Evaluate(int first, int second, String operator)
    {
    	int answer = 0;
    	
    	if (operator.equals("%"))
    		answer = first % second;
    	else if (operator.equals("*"))
    		answer = first * second;
    	else if (operator.equals("+"))
    		answer = first + second;
    	else if (operator.equals("-"))
    		answer = first - second;
    	if (debug)
	    	System.out.println(first + " " + operator + " " + second + " = " + answer + "\n");
    	return answer;
    }
    
    
    // ***************************************************************
	//
	// Method: Calculate
	//
	// Description: calculates the answer to the passed expression
	//
	// Parameters: String: the expression to calculate
    //			   Boolean (Optional): whether to show debug info
	//
	// Returns: Integer: the calculated answer to the expression
	//
	// **************************************************************
    public int Calculate(String expression)
    {
    	return Calculate(expression,false);
    }
    public int Calculate(String expression, boolean debug)
	{
		int answer = 0;
		this.debug = debug;
		
		// print the postfix notation of the expression
		String postfix = ToPostFix(expression);
		if (this.debug)
		{
			System.out.println("Generated Expresion: "+ expression);
			System.out.println("PostFix notation: " + postfix);
		}
		// construct expression tree
		tree = Construct(postfix);
		
		// calculate answer
		answer = Calculate(tree);
		if (this.debug)
		{
			System.out.println("Answer: " + answer);
		}
		
		return answer;
	}
	private int Calculate(ExpressionNode node)
	{
		int answer = 0;
		if (node != null)
		{
			// check if this is a operator
			if (IsOperator(node.data))
			{
				
				// start solving the expression post order
				int x,y;
				// calculate the highest nodes first
				if (node.right.data.equals("%") || node.right.data.equals("*"))
				{
					x = Calculate(node.right);
					y = Calculate(node.left);
				}
				else
				{
					x = Calculate(node.left);
					y = Calculate(node.right);
				}
				
				// solve the current operator
				answer = Evaluate(x, y, node.data);
				
			}
			else
			{
				// is an operand
				// return the operand
				answer = Integer.parseInt(node.data);
			}
		}
		
		return answer;
	}
}
