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
public class PFA {
    ArrayList<Node> PFA;
    
    public PFA(ArrayList<Node> nodeList){
        PFA = nodeList;   
        buildPFA();
    }
    
    private void buildPFA(){ 
        ArrayList<String> nextStates;
        //We need to loop over every node in PFA atleast once
        for (Node node : PFA) {
            //Generates all children states of node
            nextStates = genNextStates(node.getState());
           //Loops through all nodes in the PFA ArrayList
           for(Node edge : PFA){
               //Loops through all children of node
               for(String s : nextStates){
                    //Checks if the state plus a zero is found in the edge state if it is it is the node it tranistions to
                    if(isStateThere(s, edge.getState())){
                        String state_of_edge = s.substring(s.length()-2);
                        //Adds the edge to the PFAEdge hashMap as the value with the state as the key
                        node.addPFAEdge(state_of_edge, edge);
                    }
               }
           }
           
        }
    }
    
    private String concat(String orignalState, String concat){
        //This method was created because the above version returns it the wrong way round for certain operations
        if(orignalState.equals("e")){
            return concat;
        }
        else{
            return orignalState.concat(concat);
        }
    }
   /* Doesn't work for more complex alphabet no good visual way of doing this
    public void printPFA(){
        System.out.println("PFA:");
        for(Node n: PFA){
            ArrayList transitionStates = genNextStates(n.getState());
                //System.out.println(n.getState()+ ": 0: "+n.getPFAEdge("0").getState()+" 1:"+n.getPFAEdge("1").getState());
        }
    }
    */
    private boolean isStateThere(String state_to_be_found, String state_searched){
        boolean found = false;
        
        int cnt = 0;
        int len = state_to_be_found.length();
        //Cnt must be less than or equal to the length of the string so that the state can be set each time.
        while(cnt<=len){
            //First sets state to the full string i.e. if state_to_be_found is xxxx sets it to xxxx then _xxx then __xx then ___x since it is trying to find the transition state. This allows it to both target itself and if no other matches are found it matches for 1 or 0
            String state = state_to_be_found.substring(cnt);
            //Uses the String matches function can change to PatternMatcher but must use disjointed search (check your other PatterMatcher)
            if(state_searched.matches(state)){
                //If found returns true
                return true;
            }
            //Since my langauage is tuple based we want to increase by two each time
            cnt+=2;
        }
        return found;
    }
    
    public ArrayList genNextStates(String state){
        if(state.equals("e")){
            state = "";
        }
        String tmp_state = state;
        //44 is the size because its the amount of notes * the amount of beat * the amount of dot, 11*4   *2 currently removed dot
        ArrayList states = new ArrayList(44);
        List<String> notes = new ArrayList<String>(Arrays.asList("A","B","C","D","E","F","G","H","I","J","K"));
        List<String> beat = new ArrayList<String>(Arrays.asList("1","2","3","4"));
        //List<String> dot = new ArrayList<String>(Arrays.asList("1","0"));
        for(int n = 0; n<=10; n++){
            for(int b = 0; b<=3; b++){
                //for(int d = 0; d<=1; d++){
                    tmp_state += notes.get(n);
                    tmp_state += beat.get(b);
                    //state += dot.get(d);
                    states.add(tmp_state);
                    tmp_state = state;
                //}
            }
        }
        return states;
    }
    
    public ArrayList<Node> getPFA(){
        return PFA;
    }
}
