//******************************************************************************
//
//  Developer:     Michael Franklin
//
//  Project #:     Project 6
//
//  File Name:     ExpressionNode.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      04/03/2022
//
//  Instructor:    Fred Kumi 
//
//  Description:   Holds the Expression Node structure for the expression 
//				   tree used by Expression Solver
//
//
//******************************************************************************

public class ExpressionNode
{
    String data;
    ExpressionNode left, right;
 
    ExpressionNode(String data)
    {
        this.data = data;
        this.left = null;
        this.right = null;
    }
 
    ExpressionNode(String data, ExpressionNode left, ExpressionNode right)
    {
        this.data = data;
        this.left = left;
        this.right = right;
    }
}
