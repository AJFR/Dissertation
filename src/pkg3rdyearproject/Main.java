/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3rdyearproject;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author freakin
 */
public class Main {
    
    static String inputString;
    static String alt_input;
    static String output;
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * 
     * First input is 3 Blind Mice found in the file in the project folder input.txt the second is Wheels on the Bus and it is found in input2.txt
     * Output is printed in the output and a file is created/overwritten in the project called output which will contain that string
     * If you don't wish to retrain comment out 
     * 
     * 
     */
    public static void main(String[] args) throws IOException {
        //Get the first input which is Three Blind Mice
        inputString = getInput(args[0]);
        //Gets the second input, for retraining, which is Wheels on the Bus
        alt_input = getInput(args[1]);
        //Creates the PST model using Three Blind Mice as input
        PST PST_model = new PST(inputString);
        //Retrains PST model on Wheels on the Bus If you don't wish to retrain comment out the line below
        PST_model.retrain(inputString);
        //Creates the PFA model from the 
        PFA PFA_model = new PFA(PST_model.getNodeList());
        //Finds the length of the string by finding the difference in size between the string and the string with no spaces
        int length = inputString.length() - inputString.replaceAll(" ", "").length() + 1;
        //Generator is created for generating the output
        Generator gen = new Generator(length, PFA_model);
        output = gen.generate();
        List<String> lines = Arrays.asList(output);
        Path file = Paths.get("output.txt");
        //Output is written to the file output.txt in the project folder
        Files.write(file, lines, Charset.forName("UTF-8"));
    }
    
    
    
    public static String getInput(String filename) throws IOException{
        String temp = "";
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        while(br.ready()){
            temp += br.readLine();
        }
        
        System.out.println("Inital Input: "+temp);
        return temp;
    }
}
