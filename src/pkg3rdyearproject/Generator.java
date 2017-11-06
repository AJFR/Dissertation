/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3rdyearproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author freakin
 */
public class Generator {
    //This is the PFA Model
    ArrayList<Node> pfa;
    int inputLength;
    Node currentNode;
    //Musical Alphabet
    ArrayList<String> states;
    
    public Generator(int inputLength, PFA pfa){
        this.inputLength = inputLength;
        this.pfa = pfa.getPFA();
        //Set currentNode to the root node as the output always starts empty, root nodes state is "e"
        currentNode = this.pfa.get(0);
        //Generates the alphabet
        states = genStates();
    }
    
    public String generate(){
        String output = "";
        for(int cnt = 0; cnt<inputLength; cnt++){
            //transition() gets the next state to add to the output
            output += transition();
            
        }
        System.out.println("Output:");
        System.out.println(output);
        return output;
    }
    
    
    private String transition(){
        //Creates a random int between 1 and 100 inclusive
        int rndm = ThreadLocalRandom.current().nextInt(1, 100);
        double sumProb = 0;
        for(String s : states){
            //Adds the probability timesed by 100 to get it into the correct scale to sumProb
            sumProb += currentNode.getProbs(s) * 100;
            //If sumProb is less than rndm it has not reached the correct state
            if(sumProb>=rndm){
                //Assigns currentNode to the node we from the edge
                currentNode = currentNode.getPFAEdge(s);
                //Returns the state
                return s;
            }
        }
        return "";
    }
    
    private ArrayList genStates(){
        //44 is the size because its the amount of notes * the amount of beat * the amount of dot, 11*4   *2 currently removed dot
        ArrayList states = new ArrayList(44);
        List<String> notes = new ArrayList<String>(Arrays.asList("A","B","C","D","E","F","G","H","I","J","K"));
        List<String> beat = new ArrayList<String>(Arrays.asList("1","2","3","4"));
        //List<String> dot = new ArrayList<String>(Arrays.asList("1","0"));
        for(int n = 0; n<=10; n++){
            for(int b = 0; b<=3; b++){
                //for(int d = 0; d<=1; d++){
                    String state = ""; 
                    state += notes.get(n);
                    state += beat.get(b);
                    //state += dot.get(d);
                    states.add(state);
                //}
            }
        }
        return states;
    }
}
