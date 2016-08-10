import java.util.*;

/**
 * Created by tess on 4/9/14.
 */
public class PatternMatcher {
    private LinkedList<String> closestWords = null;
    private int[][] matrix;
    private int closestDistance = -1;
    private String[] oldword = {};
    private final HashMap<String, ArrayList<String>> patternsAndTemplates;
    private HashMap<String, HashMap<String, ArrayList<String>>> first;
    private HashMap<String, HashMap<String,ArrayList<String>>> significant;
    private HashMap<String, Integer> wordCounts;
    private int numOfWords = 0;
    private Boolean isSignificant;

    public PatternMatcher(HashMap<String, ArrayList<String>> patternsAndTemplates, Boolean isSignificant){
        this.isSignificant = isSignificant;
        this.patternsAndTemplates = patternsAndTemplates;
        first = new HashMap<String, HashMap<String, ArrayList<String>>>();
        significant = new HashMap<String, HashMap<String, ArrayList<String>>>();
        wordCounts = new HashMap<String, Integer>();
        createWordSignificance();
        if(isSignificant){
            createIndexBySignificant();
        }else {
            createIndexByFirst();
        }
    }

    public String GetClosestMatch(String userInput){
        if(isSignificant){
            if(significant.containsKey(getMostSignificant(userInput))){
                if(getMostSignificant(userInput)==null){
                    return "I don't understand";
                }
                //System.out.println(getMostSignificant(userInput));
                String bestMatch = ClosestWords(userInput, significant.get(getMostSignificant(userInput)).keySet());

                //System.out.println("best match: " +bestMatch);
                if(bestMatch.equals("")||bestMatch == null){
                    return "I don't understand";
                }else{
                    Random r = new Random();
                    return significant.get(getMostSignificant(userInput)).get(bestMatch).get(
                            r.nextInt(significant.get(getMostSignificant(userInput)).get(bestMatch).size()));
                }
            }else{
                return "I don't understand";
            }
        }
        else{
            if(first.containsKey(getFirstWord(userInput))){
                String bestMatch = ClosestWords(userInput, first.get(getFirstWord(userInput)).keySet());

                //System.out.println("best match: " +bestMatch);

                if(bestMatch.equals("")||bestMatch == null){
                    return "I don't understand";
                }else{
                    Random r = new Random();
                    return first.get(getFirstWord(userInput)).get(bestMatch)
                            .get(r.nextInt(first.get(getFirstWord(userInput)).get(bestMatch).size()));
                }
            }
            return "I don't understand";
        }
    }

    private int partDist(String[] w1, String[] w2, int w1len, int w2len) {
        if (w1len == 0)
            return w2len;
        if (w2len == 0)
            return w1len;
        int res;
        if(matrix[w1len-1][w2len-1] != 0){
            res = matrix[w1len-1][w2len-1];
        }
        else{
            res = partDist(w1, w2, w1len - 1, w2len - 1);
            matrix[w1len-1][w2len-1] = res;
        }
        res = res + (w1[w1len - 1].equals(w2[w2len - 1]) ? 0 : 1);
        int addLetter;
        if(matrix[w1len-1][w2len] != 0){
            addLetter = matrix[w1len-1][w2len]+1;
        }
        else{
            addLetter = partDist(w1, w2, w1len - 1, w2len) + 1;
            matrix[w1len-1][w2len] = addLetter - 1;
        }
        if (addLetter < res){
            res = addLetter;
        }
        int deleteLetter;
        if(matrix[w1len][w2len-1] != 0){
            deleteLetter = matrix[w1len][w2len-1]+1;
        }
        else{
            deleteLetter = partDist(w1, w2, w1len, w2len-1) + 1;
            matrix[w1len][w2len-1] = deleteLetter - 1;
        }
        if (deleteLetter < res){
            res = deleteLetter;
        }
        return res;
    }

    private int Distance(String[] w1, String[] w2) {
        String[] tmp= oldword;
        if(w2.length > oldword.length){
            for(int i= 0; i<oldword.length;i++){
                tmp[i] = w2[i];

            }
        }
        if((w2.length > oldword.length) && (tmp.equals(oldword))){
            oldword = w2;

            return partDist(w1, w2, w1.length, w2.length);
        }
        else{
            for(int[] row: matrix){
                Arrays.fill(row, 0);
            }
            oldword = w2;
            return partDist(w1, w2, w1.length, w2.length);
        }
    }

    private String ClosestWords(String w, Set<String> wordList) {
        String[] word1 = w.split(" ");
        matrix = new int[w.length()+1][30];
        for (String s : wordList) {
            
            String[] word2 = s.split(" ");
            if(((Math.abs(word1.length-word2.length) <= closestDistance) || (closestDistance == -1))&& Math.abs(word1.length-word2.length)<20){
                
                int dist = Distance(word1, word2);
                
                if (dist < closestDistance || closestDistance == -1) {
                    closestDistance = dist;
                    closestWords = new LinkedList<String>();
                    closestWords.add(s);
                }
                else if (dist == closestDistance)
                    closestWords.add(s);
            }
        }
        
        Random r = new Random(closestWords.size());
        closestDistance = -1;
        
        if(closestWords.isEmpty()){
            System.out.println("return null");
            return null;
        }
        
        String retur = closestWords.get(r.nextInt(closestWords.size()));
        closestWords.clear();
        return retur;
    }

    private void createWordSignificance(){
        Set<String> set = patternsAndTemplates.keySet();
        for(String s: set){
            
            String[] words = s.split(" ");
            for(int i = 0; i< words.length; i++){
                String word = words[i];
                if(wordCounts.containsKey(word)){
                    int j = wordCounts.get(word);
                    j++;
                    wordCounts.remove(word);
                    wordCounts.put(word, j);
                    numOfWords++;
                }else{
                    wordCounts.put(word, 1);
                    numOfWords++;
                }
            }
        }
    }

    private void createIndexBySignificant(){

        Set<String> set = patternsAndTemplates.keySet();
        for(String sentence: set){
            String[] significantWords = sentence.split(" ");
            for(String word: significantWords){
                if(significant.containsKey(word)){
                    significant.get(word).put(sentence, patternsAndTemplates.get(sentence));
                }
                else{
                    HashMap<String, ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
                    temp.put(sentence, patternsAndTemplates.get(sentence));
                    significant.put(word, temp);
                }
            }
        }
    }


    private void createIndexByFirst(){

        Set<String> set = patternsAndTemplates.keySet();
        for(String sentence: set){
            String firstWord = getFirstWord(sentence);
            
            if(first.containsKey(firstWord)){
                first.get(firstWord).put(sentence, patternsAndTemplates.get(sentence));
            }
            else{
                HashMap<String, ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
                temp.put(sentence, patternsAndTemplates.get(sentence));
                first.put(firstWord, temp);
            }
        }
    }

    private String getFirstWord(String sentence){
        String firstword = "";
        String[] words = sentence.split(" ");
        firstword = words[0];
        
        return firstword;
    }




    private String getMostSignificant(String sentence){
        String mostSignificant = "";
        int max= 0;
        String[] words = sentence.split(" ");
        for(int i = 0; i<words.length; i++){
            if(wordCounts.containsKey(words[i])){
               
                if(numOfWords/wordCounts.get(words[i]) > max && (!words[i].equals(" "))){
                    max = numOfWords/wordCounts.get(words[i]);
                    mostSignificant = words[i];
                }
            }
        }

        return mostSignificant;
    }

}
