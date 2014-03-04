package BotAI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Created by tess on 3/4/14.
 */
public class InputParser {
    private final BufferedReader inputReader;

    public InputParser(){
        this.inputReader = new BufferedReader(new InputStreamReader(System.in));
    }
}
