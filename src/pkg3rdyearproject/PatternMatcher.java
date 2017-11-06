/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3rdyearproject;
import java.util.regex.*;

/**
 *
 * @author freakin
 */
public class PatternMatcher {
//DOES THIS NEED TO BE A SEPERATE CLASS??
    public PatternMatcher(){
        
    }
    
    public Double probOfState(String input, String state){
        //Creates the pattern from the state
        Pattern pattern = Pattern.compile(state);
        //Creates the matcher from the pattern and the input string
        Matcher matcher = pattern.matcher(input);
        //cnt increases when a valid match is found
        Double cnt = 0.0;
        //from allows for it to be disjointed otherwise it skips over valuable states
        int from = 0;
        //matcher.find(from) is true whilst there are still matches to be made
        while(matcher.find(from)){
            cnt++;
            //This makes from equal to the first character matched plus one
            from = matcher.start()+2;
        }
        return cnt;
    }
}
