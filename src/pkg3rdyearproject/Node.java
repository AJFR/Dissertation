/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3rdyearproject;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author freakin
 */
public class Node {
    //MAX_CHILD will only be used for a binary alphabet
    //static final int MAX_CHILD = 2;
    String state;
    HashMap prob_of_edge;
    ArrayList<Node> children;
    Node parent;
    HashMap PFA_edges;
    
    public Node(String state){
        children = new ArrayList();
        this.state = state;
        prob_of_edge = new HashMap();
        PFA_edges = new HashMap();
    }
    
    public void addProbs(Double prob, String state){
        prob_of_edge.put(state, prob);
    }
    
    public void addPFAEdge(String state, Node node){
        PFA_edges.put(state, node);
    }
    
    public Node getPFAEdge(String state){
        return (Node) PFA_edges.get(state);
    }
    
    public Double getProbs(String state){
        return (Double) prob_of_edge.get(state);
    }
    
    public String getState(){
        return state;
    }
    
    public ArrayList getChildren(){
        return children;
    }
    
    public void addChildren(Node child){
        children.add(child);
    }
    
    public void setParent(Node pnt){
        parent = pnt;
    }
    
    public Node getParent(){
        return parent;
    }
    
    public void removeChild(Node child){
        children.remove(child);
    }
    
    public void addMapProbs(HashMap probs){
        //Aassigns the HashMap probs to prob_of_edge
        prob_of_edge = probs;
    }
    
    public HashMap getMap(){
        return prob_of_edge;
    }
    
    public Node getChild(String state){
        boolean fnd = false;
        Node child = null;
        int cnt = 0;
        while(!fnd){
            if(children.get(cnt).getState().equals(state)){
                child = children.get(cnt);
                fnd = true;
            }
            cnt++;
        }
        return child;
    }
}
