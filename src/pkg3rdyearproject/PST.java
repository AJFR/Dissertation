/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3rdyearproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 *
 * @author freakin
 */
public class PST {
    final int MIN_PERCENT_CHANGE = 10;
    int inputLength = 0;
    String input;
    Node root;
    ArrayList<String> states;
    public ArrayList<Node> nodeList;
    
    public PST(String input){
        inputLength = input.length() - input.replaceAll(" ", "").length() + 1;
        this.input = input.replaceAll(" ", "");
        //Generates all of the states
        states = genStates();
        //Creates the root node e
        root = new Node("e");
        //Generates the probabilities of e
        root.addMapProbs(genAllProb(root.state));
        nodeList = new ArrayList<>();
        //Add e to the nodeList
        nodeList.add(root);
        buildPST();
    }
        
    private void buildPST(){
        final int depth = 4;
        //cnt allows us to loop through the node list whilst using a while loop
        int cnt = 0;
        //Creates an empty node called pnt_node
        Node pnt_node;
        //Creates an empty ArrayList for the next set of nodes
        ArrayList<Node> nextSet;
        //While there are still nodes to look at it keeps looping
        while(nodeList.size()>cnt){
            //Set parent node to the next node in the list starting with root
            pnt_node = nodeList.get(cnt);
            if(pnt_node.state.length()>2*depth){
                //Stop when the length, depth, is gerter than 4
                break;
            }
            //Creat next set of states
            nextSet = getNextSetOfStates(pnt_node);
            //Set all of the probability HashMaps for each child node
            for(Node n : nextSet){
                n.addMapProbs(genAllProb(n.getState()));
                //System.out.println("Node:" + n.getState());
            }
            if(cnt>=1){
                //If cnt is greater than one it is not the first time through
                //Threshold is how we decided on the node if the entropy is greater than 0.0035 then the node has a great enough surprise to be useful
                double threshold = 0.0035;
                //Loops through all of the potential child nodes of pnt
                for(Node n : nextSet){
                    if(entropyCalc(n)> threshold){
                        //If the node passes the entopy calcualation then it is add as a child to pnt and added to the nodeList
                        pnt_node.addChildren(n);
                        nodeList.add(n);
                    }
                }
            }
            else if(cnt<1){
                //If its the first time through we add all children.
                for(Node n : nextSet){
                    if(n.getParent().getProbs(n.getState())!=0.0){
                        pnt_node.addChildren(n);
                        nodeList.add(n);
                    }
                    
                }
            }
            cnt++;
            }
        printNodeList();
    }
    
    public void retrain(String alt_input){
        System.out.println("Retraing On Input: "+ alt_input);
        alt_input = alt_input.replaceAll(" ", "");
        final int depth = 4;
        //cnt allows us to loop through the node list whilst using a while loop
        int cnt = 0;
        //Creates an empty node called pnt_node
        Node pnt_node;
        //Creates an empty ArrayList for the next set of nodes
        ArrayList<Node> nextSet;
        //While there are still nodes to look at it keeps looping
        while(nodeList.size()>cnt){
            //Set parent node to the next node in the list starting with root
            pnt_node = nodeList.get(cnt);
            if(pnt_node.state.length()>2*depth){
                break;
            }
            //Creat next set of states
            nextSet = getNextSetOfStates(pnt_node);
            //Set all of the probability HashMaps for each child node
            for(Node n : nextSet){
                n.addMapProbs(genAllProb(n.getState(),alt_input));
                //System.out.println("Node:" + n.getState());
            }
            if(cnt>=1){
                //If cnt is greater than one it is not the first time through
                //Threshold is how we decided on the node if the entropy is greater than 0.0035 then the node has a great enough surprise to be useful
                double threshold = 0.0035;
                //Loops through all of the potential child nodes of pnt
                for(Node n : nextSet){
                    //If the node passes the entopy calcualation then it is add as a child to pnt and added to the nodeList
                    if(entropyCalc(n)> threshold){
                        boolean merged = false;
                        //If we already have the node in nodeList we merge the probs of both
                        for(Node z : nodeList.subList(1,nodeList.size()-1)){
                            if(z.getState().equals(n.getState())){
                                z.addMapProbs(mergeProbs(z,n));
                                merged = true;
                            }
                        }
                        //If not merged adds them to nodeList
                        if(!merged){
                                pnt_node.addChildren(n);
                                nodeList.add(n);
                            }
                    }
                }
            }
            else if(cnt<1){
                //If its the first time through we add all children since they are most likely already added to nodeList we merge them.
                for(Node n : nextSet){
                    if(n.getState().equals("A3"))
                    for(Node z : nodeList.subList(1,nodeList.size()-1)){
                        if(z.getState().equals(n.getState())){
                                z.addMapProbs(mergeProbs(z,n));
                            }
                    }
                }
            }
            cnt++;
            }
        printNodeList(); 
    }
    
    private int minusExpoBaseTwo(int order){
        //Double as Math.pow(2,i) returns a double this is casted as an int when returning
        double cnt = 0;
        //This allows us to add the exponatials together whilst decreasing the power each time effectively making 2^(order) + 2^(order-1)....until order equals 1
        for(int i = order;i>0;i--){
            cnt = cnt + Math.pow(2, i);
        }
        //Converts it back to an int
        return (int) cnt;
    }
    
    private String concat(String concat, String orignalState){
        //This method was created because concat of the root nodes state, e which is a symbol for nothing, and 1 ends up as e1 instead of 1
        if(orignalState.equals("e")){
            return concat;
        }
        else{
            return concat.concat(orignalState);
        }
    }
    
    private String concatReverse(String orignalState, String concat){
        //This method was created because the above version returns it the wrong way round for certain operations
        if(orignalState.equals("e")){
            return concat;
        }
        else{
            return orignalState.concat(concat);
        }
    }
    
    private String concatReverse(String orignalState, char concat){
        //This method was created because the above version returns it the wrong way round for certain operations
        String stringChar = "";
        stringChar += concat;
        if(orignalState.equals("e")){
            return stringChar;
        }
        else{
            return orignalState.concat(stringChar);
        }
    }
    
    public static ArrayList genStates(){
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
    
    public ArrayList genStates2(){
        //44 is the size because its the amount of notes * the amount of beat * the amount of dot, 11*4   *2 currently removed dot
        ArrayList states = new ArrayList(44);
        List<String> notes = new ArrayList<String>(Arrays.asList("A","B","C","D","E","F","G","H","I","J","K"));
        List<String> beat = new ArrayList<String>(Arrays.asList("1","2","3","4"));
        //List<String> dot = new ArrayList<String>(Arrays.asList("1","0"));
        for(String s : notes){
            for(String d : beat){
                //for(int d = 0; d<=1; d++){
                    String state = ""; 
                    state += s;
                    state += d;
                    //state += dot.get(d);
                    states.add(state);
                //}
            }
        }
        return states;
    }
    
    private HashMap genAllProb(String state){
        HashMap probs = new HashMap(states.size());
        //For error checking
        double sumOfProbs = 0;
        double rawSum = 0;
        //If the state is "e" sets state to empty
        if(state.equals("e")){
            state = "";
        }
        //Goes through all of the states possible
        for(String s : states){
            //Adds on every state possible on to the back of the state i.e. if state is A2 will check all A2state
            String stateS = state + s;
            //Generates how many times this string is found in the input string
            double probStateS = genProbs(stateS);
            //rawSum for later uses allows for the actual probability to be caluclated
            rawSum += probStateS;
            //Puts the amount of times the state is seen into a prob HashMap this is not the finished HashMap
            probs.put(s, genProbs(stateS));
        }
        for(String s : states){
            //Loops through all of the values in the HashMap again turning them into the correct probabilites
            double prob = (double)probs.get(s);
            probs.put(s, prob/rawSum);
        }
        if(Double.isNaN((Double)probs.get(states.get(0)))){
            //Inheirits root node probs if all probs are NaN (Only happens if a note only appears at the end and no where else)
            probs = root.prob_of_edge;
        }
        return probs;
    }
    
    private HashMap genAllProb(String state, String inputString){
        HashMap probs = new HashMap(states.size());
        //For error checking
        double sumOfProbs = 0;
        double rawSum = 0;
        //If the state is "e" sets state to empty
        if(state.equals("e")){
            state = "";
        }
        //Goes through all of the states possible
        for(String s : states){
            //Adds on every state possible on to the back of the state i.e. if state is A2 will check all A2state
            String stateS = state + s;
            //Generates how many times this string is found in the input string
            double probStateS = genProbs(stateS, inputString);
            //rawSum for later uses allows for the actual probability to be caluclated
            rawSum += probStateS;
            //Puts the amount of times the state is seen into a prob HashMap this is not the finished HashMap
            probs.put(s, genProbs(stateS, inputString));
        }
        for(String s : states){
            //Loops through all of the values in the HashMap again turning them into the correct probabilites
            double prob = (double)probs.get(s);
            probs.put(s, prob/rawSum);
        }
        return probs;
    }
    
    private Double genProbs(String state){
        PatternMatcher p = new PatternMatcher();
        return p.probOfState(input,state); 
    }
    
    private Double genProbs(String state, String alt_input){
        PatternMatcher p = new PatternMatcher();
        return p.probOfState(alt_input,state); 
    }
    
    private double entropyCalc(Node node){
        Node parent = node.getParent();
        return entropyCalc(node.getState(), parent.getState());
    }
    
    public double entropyCalc(String stateSigS,String stateS){
        //Total of the Sumation
        double total= 0;
        //Probability of the stateSigS occuring
        double pSigSP;
        //Probability of stateS occuring
        double pSigS;
        //Given stateS whats the chance of sigP
        double pCond;
        //lg10 of pSigSP/pCond*pSigS
        double lgP;
        //Used for concating pSigSP so that we get pSigS0 and pSigS1
        String sigP;
        for(int cnt = 0;cnt<states.size();cnt++){
            sigP = states.get(cnt);
            pSigSP = getCumlProbs(stateSigS.concat(sigP));
            pSigS = getCumlProbs(stateSigS);
            pCond = condProb(stateS, sigP);
            lgP = Math.log10(pSigSP/(pCond*pSigS));
            if(Double.isInfinite(lgP)){
                lgP = 0;
            }
            if(Double.isNaN(lgP)){
                lgP = 0;
            }
            total = total + pSigSP*lgP;
        }
        
        //System.out.println(total);
        if(Double.isInfinite(total)){
            return 0;
        }
        return total;
    }
    
    public double getCumlProbs(String state){
        String currState = "";
        //We start at the root and using the knowledge of what state prob we want to get we go down the tree times the the prob of the edge each time
        Node node = this.root;
        HashMap childProbs;
        //Total has to start at one as 0*anything is 0
        double total = 1;
        //Length of the state MAYBE NEEDS A -1 I HAVE NO IDEA IT SEEMS TO GENERATE THE CORRECT ENTROPY OTHERWISE
        int len = state.length()/2;
        for(int cnt = 0; cnt<len;cnt++){
            childProbs = genAllProb(currState);
            currState += state.substring((cnt*2), (cnt*2)+2);
            total *= (double) childProbs.get(state.substring(cnt*2, (cnt*2)+2));
        }
        if(Double.isNaN(total)){
            return 0;
        }
        else{
            return total;
        }
    }
    
    private void printNodeList(){
        System.out.println("PST:");
        for(Node n: nodeList){
            if(n.getState().equals("e")){
                System.out.println(n.getState());
            }
            else{
                System.out.println(n.getState()+ ": Parent: "+n.getParent().getState());
            }
            
        }
    }
    
    private Node getChildNode(Node node, String zeroOrOne){
        ArrayList childList = node.getChildren();
        boolean found = false;
        int cnt = 0;
        Node child = null;
        while(!found){
            child = (Node) childList.get(cnt);
            if(child.getState().equals(concatReverse(node.getState(), zeroOrOne))){
                found = true;
            }
            cnt++;
        }
        return child;
    }
    
    private double condProb(String stateGiven, String stateFind){
        //Cond Prob is simple uses the pattern matcher to find out the probability
        HashMap probs = genAllProb(stateGiven);
        return (double) probs.get(stateFind);
    }
    
    public ArrayList<Node> getNodeList(){
        return nodeList;
    }
    
    public ArrayList<Node> getNextSetOfStates(Node parent){
        ArrayList<Node> nextSet = new ArrayList<>();
        String nodeState = parent.getState();
        //"e" is a symbol for empty so we set it to empty
            if(nodeState.equals("e")){
                nodeState = "";
            }
        for(String s : states){
            //Creates all the children of parent with parent.state + s
            Node tmp_node = new Node(nodeState + s);
            tmp_node.setParent(parent);
            nextSet.add(tmp_node);
        }
        
        return nextSet;
    }
    
    private HashMap mergeProbs(Node z, Node n){
        HashMap zMap = z.getMap();
        HashMap nMap = n.getMap();
        //Has to loop through alphabet so we get all probabilites
        for(String s : states){
            double prob1 = (double) zMap.get(s);
            //If either prob1 or prob2 was NaN on ANY node it would overwrite root nodes probabilites for no reason 
            if(Double.isNaN(prob1)){
                prob1 = 0;
            }
            double prob2 = (double) nMap.get(s);
            if(Double.isNaN(prob2)){
                prob2 = 0;
            }
            double mean = (prob1+prob2)/2;
            if(Double.isNaN(mean)){
                mean = 0;
            }
            //The new HashMap is the average of both probs
            zMap.put(s, mean);
        }
        return zMap;
        
    }
    
}

    
