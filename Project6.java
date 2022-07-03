//***************************************************************
//
//  Developer:         Cory Munselle
//
//  Program #:         6
//
//  File Name:         Project6.java
//
//  Course:            COSC 4301 - Modern Programming
//
//  Due Date:          04/03/22
//
//  Instructor:        Prof. Fred Kumi
//
//  Description:
//     <An explanation of what the program is designed to do>
//
//***************************************************************

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Project6
{
    private final Scanner input;
    private Formatter output;
    private final SecureRandom randomNumbers;

    private final ArrayList<Integer> expressionNums;
    private final ArrayList<String> expressionOperators;

    private boolean quit;
    private boolean changeDifficulty;

    private String question;
    private String quitChoice;

    private int difficultyChoice;
    private int prevDifficulty;
    private int count;
    private int totalCount;
    private int answer;
    private int result;


    //***************************************************************
    //
    //  Method:       main
    // 
    //  Description:  The main method of the program
    //
    //  Parameters:   String array
    //
    //  Returns:      N/A 
    //
    //**************************************************************
	public static void main(String[] args)
    {
        developerInfo();

        Project6 myObject = new Project6();

        myObject.openFile();

        myObject.questionDriver();
    }

    //***************************************************************
    //
    //  Method:       Constructor
    //
    //  Description:  Initializes most used variables
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public Project6()
    {
        input = new Scanner(System.in);
        randomNumbers = new SecureRandom();
        expressionNums = new ArrayList<>();
        expressionOperators = new ArrayList<>();
        quitChoice = "";
        difficultyChoice = 0;
        prevDifficulty = -1;
        answer = 0;
        result = 0;
        totalCount = 0;
        count = 0;
        changeDifficulty = true;
        quit = false;
    }

    //***************************************************************
    //
    //  Method:       checkAnswer
    //
    //  Description:  Checks if the result supplied by the user is correct
    //
    //  Parameters:   None
    //
    //  Returns:      boolean
    //
    //**************************************************************
    public boolean checkAnswer()
    {
        return answer == result;
    }

    //***************************************************************
    //
    //  Method:       openFile
    //
    //  Description:  Tries to open the output file
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void openFile()
    {
        try
        {
            output = new Formatter("./Project6-output.txt");
            output.format("%s%n%s%n%s%n", "Name:    Cory Munselle", "Course:  COSC 4301 Modern Programming", "Program: Six \n");
        }
        catch (IOException ioException)
        {
            System.err.println("Error opening file. Terminating.");
            System.exit(1);
        }
    }

    //***************************************************************
    //
    //  Method:       difficultyMenu
    //
    //  Description:  Displays the difficulty options for the user
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void difficultyMenu() {
        final String[] difficulties = {"1) Basic - Two operands and one operator",
                                       "2) Intermediate - Three operands and two operators",
                                       "3) Advanced - Four operands and three operators"};

        System.out.println("Please select a difficulty: ");
        output.format("%s%n", "Please select a difficulty: ");
        for (int i = difficultyChoice; i <= difficulties.length-1; i++) {
            System.out.println(difficulties[i]);
            output.format("%s%n", difficulties[i]);
        }
        output.format("\n");
        // only change difficulty if all the checks are complete
        while (changeDifficulty) {
            // prevents invalid inputs
            if (input.hasNextInt()) {
                try {
                    difficultyChoice = input.nextInt();
                // shouldn't ever reach this because of the previous if statement, but just in case.
                } catch (IllegalArgumentException | InputMismatchException e) {
                    System.err.println("Not an integer! Please select number from the difficulties shown above.");
                    output.format("%s%n", "Not an integer! Please select number from the difficulties shown above.");
                    input.next();
                }
                // invalid selection (not one of the three options)
                if (difficultyChoice >= 4 || difficultyChoice <= 0) {
                    output.format("%s%n", difficultyChoice);
                    System.out.println("Invalid selection. Please select a number from the difficulties shown above.");
                    output.format("%s%n", "Not an integer! Please select number from the difficulties shown above.");
                }
                // tried to select a difficulty lower than the previously selected one
                else if (difficultyChoice <= prevDifficulty) {
                    output.format("%s%n", difficultyChoice);
                    System.out.println("Cannot choose a difficulty lower than the current one! \nPlease choose from the difficulties shown above. ");
                    output.format("%s%n", "Cannot choose a difficulty lower than the current one! \nPlease choose from the difficulties shown above. ");
                }
                // correct selection
                else {
                    output.format("%s%n", difficultyChoice);
                    changeDifficulty = false;
                    prevDifficulty = difficultyChoice;
                }
            }
            // refreshes input so the next value can be entered
            else {
                String str = input.next();
                output.format("%s%n", str);
                System.out.println("Not an integer! Please select a number from the difficulties shown above.");
                output.format("%s%n", "Not an integer! Please select a number from the difficulties shown above.");

            }
        }
        // move to the question prompt
        questionPrompt();
    }

    //***************************************************************
    //
    //  Method:       questionPrompt
    //
    //  Description:  Generates a question and prompts the user to answer it
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void questionPrompt()
    {
        question = genExpression();

        answer = calcAnswer();

        System.out.println("What is " + question + "?");
        output.format("%s %s %s%n","What is", question, "?");

        getAnswer();
    }

    //***************************************************************
    //
    //  Method:       getAnswer
    //
    //  Description:  Gets answer from the user and checks if it is correct
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void getAnswer()
    {
        boolean notANumber = true;
        while (notANumber) {
            if (input.hasNextInt()) {
                result = input.nextInt();
                notANumber = false;
            }
            else {
                String str = input.next();
                output.format("%s%n", str);
                System.out.println("Not an integer! Please provide an integer value.");
                output.format("%s%n", "Not an integer! Please provide an integer value.");
            }
        }
        output.format("%d%n", result);
        if (checkAnswer())
        {
            count++;
            totalCount++;
        }
        displayMessage();
    }

    //***************************************************************
    //
    //  Method:       displayMessage
    //
    //  Description:  Prints out random correct/incorrect message
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void displayMessage()
    {
        final String[] correctMsgs = {"Good job!", "Excellent work!", "Correct answer!", "You got it right!", "Right on!"};
        final String[] wrongMsgs = {"Not quite. Keep trying!", "Incorrect. Give it another try!", "Wrong answer. Don't give up!", "You'll get it next time!", "It's not over yet. Try again!"};

        int message = randomNumbers.nextInt(5);

        if (checkAnswer())
        {
            System.out.println(correctMsgs[message] + "\n");
            output.format(correctMsgs[message] + "\n\n");
        }
        else
        {
            System.out.println(wrongMsgs[message] + "\n");
            output.format(wrongMsgs[message] + "\n\n");
        }
    }

    //***************************************************************
    //
    //  Method:       questionDriver
    //
    //  Description:  Facilitates question prompts and message prompting
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void questionDriver()
    {
        while (!quit)
        {
            if (checkAnswer())
            {
                // only change difficulty if the person selects change difficulty
                if (changeDifficulty) {
                    difficultyMenu();
                }
                // generate another question
                else {
                    questionPrompt();
                }
            }
            // if they answered incorrectly, just ask the same question again
            else
            {
                System.out.println("What is " + question + "?");
                output.format("%s %s %s%n","What is", question, "?");
                getAnswer();
            }
            quitMessage();
        }
    }

    //***************************************************************
    //
    //  Method:       quitMessage
    //
    //  Description:  Prints quit message and exits the program
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void quitMessage()
    {
        // only called if at least five questions were answered correctly, including the most recent one
        if (count >= 5 && checkAnswer())
        {
            System.out.print("Good job on answering " + totalCount + " questions correctly! " +
                             "\nWhat would you like to do?" +
                             "\nq)uit" +
                             "\nc)ontinue with same difficulty" +
                             "\nn)ew difficulty\n" +
                             "\nNote: Any input besides those three letters will default to quitting the program.\n");
            output.format("Good job on answering " + totalCount + " questions correctly! " +
                          "\nWhat would you like to do?" +
                          "\nq)uit" +
                          "\nc)ontinue with same difficulty" +
                          "\nn)ew difficulty\n" +
                          "\nNote: Any input besides those three letters will default to quitting the program.\n");
            quitChoice = input.next();
            output.format(quitChoice + "\n");
            // quit
            if (quitChoice.equalsIgnoreCase("q"))
            {
                System.out.println("\nThank you for using this program. Goodbye!");
                output.format("\nThank you for using this program. Goodbye!");
                quit = true;
                output.close();
            }
            // Not really necessary, but I didn't want an empty if statement so I just put a message in there
            else if (quitChoice.equalsIgnoreCase("c")) {
                System.out.println("Continuing with the same difficulty...");
            }
            // change the difficulty, but only if the difficulty isn't max
            else if (quitChoice.equalsIgnoreCase("n") && difficultyChoice < 3){
                count = 0;
                changeDifficulty = true;
            }
            // assume since the difficulty is max that they want to keep answering questions
            else if (quitChoice.equalsIgnoreCase("n") && difficultyChoice >= 3) {
                System.out.println("Max difficulty already chosen. Continuing with the same difficulty...");
            }
            // if the user inputs something besides the other prompts just quit.
            else {
                System.out.println("\nThank you for using this program. Goodbye!");
                output.format("\nThank you for using this program. Goodbye!");
                quit = true;
                output.close();
            }
        }
    }

    //***************************************************************
    //
    //  Method:       genExpression
    //
    //  Description:  Generates a random expression based on difficulty
    //
    //  Parameters:   None
    //
    //  Returns:      String expression.toString()
    //
    //**************************************************************
    public String genExpression() {
        StringBuilder expression = new StringBuilder();

        // reset both arraylists to make sure they're empty
        expressionNums.clear();
        expressionOperators.clear();

        // generate expression based on difficulty, using difficulty as condition
        for (int i = 0; i < difficultyChoice; i++) {
            // generate a random number and use it for the operator selection
            int selection = randomNumbers.nextInt(4);
            expressionNums.add(randomNumbers.nextInt(9)+1);
            expressionOperators.add(randOperator(selection));
            expression.append(expressionNums.get(i))
                      .append(" ")
                      .append(expressionOperators.get(i))
                      .append(" ");
        }
        // add the last one at the end
        expressionNums.add(randomNumbers.nextInt(9)+1);
        expression.append(expressionNums.get(difficultyChoice));

        // return expression as a string
        return expression.toString();
    }

    //***************************************************************
    //
    //  Method:       randOperator
    //
    //  Description:  returns a random operator based on the number passed in
    //
    //  Parameters:   int selection
    //
    //  Returns:      String operator
    //
    //**************************************************************
    public String randOperator(int selection) {
        String operator = "";
        switch (selection) {
            case 0:
               operator = "*";
               break;
            case 1:
                operator = "%";
                break;
            case 2:
                operator = "+";
                break;
            case 3:
                operator = "-";
                break;
        }
        return operator;
    }

    //***************************************************************
    //
    //  Method:       calcAnswer
    //
    //  Description:  Calculates the answer to the expression generated
    //
    //  Parameters:   None
    //
    //  Returns:      int answer
    //
    //**************************************************************
    public int calcAnswer() {

        // loop through all the expression operators until there are no more * or % left
        while (expressionOperators.toString().contains("*") ||  expressionOperators.toString().contains("%")) {

            // cleans the toString representation of the arraylist for locating * or %
            String operatorString = cleanString(expressionOperators.toString());

            int loc = operatorString.indexOf("*");

            // if there are no *, get the index of %
            if (loc == -1 || (operatorString.contains("%") && operatorString.indexOf("%") < loc)) {
                loc = operatorString.indexOf("%");
            }

            // In order:
            // set the value in the number arraylist to the new answer generated from calcExpression
            // remove the number to the right of the operator (loc + 1)
            // trim the numbers array to the new size
            // remove the operator at the current location
            // trim the operators array to the new size
            expressionNums.set(loc, calcExpression(expressionNums.get(loc), expressionNums.get(loc + 1), expressionOperators.get(loc)));
            expressionNums.remove(loc + 1);
            expressionNums.trimToSize();
            expressionOperators.remove(loc);
            expressionOperators.trimToSize();
        }

        // do the same thing as above with + and -
        while (expressionOperators.toString().contains("+") || expressionOperators.toString().contains("-")) {

            String operatorString = cleanString(expressionOperators.toString());

            int loc = operatorString.indexOf("+");

            if (loc == -1 || (operatorString.contains("-") && operatorString.indexOf("-") < loc)) {
                loc = operatorString.indexOf("-");
            }

            expressionNums.set(loc, calcExpression(expressionNums.get(loc), expressionNums.get(loc + 1), expressionOperators.get(loc)));
            expressionNums.remove(loc + 1);
            expressionNums.trimToSize();
            expressionOperators.remove(loc);
            expressionOperators.trimToSize();
        }
        // return the last value in the numbers arraylist
        return expressionNums.get(0);
    }

    //***************************************************************
    //
    //  Method:       calcExpression
    //
    //  Description:  Solves the expression passed in
    //
    //  Parameters:   int value1, int value2, String operator
    //
    //  Returns:      int answer
    //
    //**************************************************************
    public int calcExpression(int val1, int val2, String operator) {
        int answer = 0;
        switch (operator) {
            case "*":
                answer = val1 * val2;
                break;
            case "%":
                answer = val1 % val2;
                break;
            case "+":
                answer = val1 + val2;
                break;
            case "-":
                answer = val1 - val2;
                break;
        }
        return answer;
    }

    //***************************************************************
    //
    //  Method:       cleanString
    //
    //  Description:  Helper function to remove all unnecessary characters
    //
    //  Parameters:   String str
    //
    //  Returns:      String str
    //
    //**************************************************************
    public String cleanString(String str) {
        str = str.replace(",", "");
        str = str.replace("[", "");
        str = str.replace("]", "");
        str = str.replace(" ", "");

        return str;
    }
	
	//***************************************************************
    //
    //  Method:       developerInfo
    // 
    //  Description:  The developer information method of the program
    //
    //  Parameters:   None
    //
    //  Returns:      N/A 
    //
    //**************************************************************
    public static void developerInfo()
    {
       System.out.println("Name:    Cory Munselle");
       System.out.println("Course:  COSC 4301 Modern Programming");
       System.out.println("Program: Six \n");

    } // End of developerInfo
}
