import java.util.*;

/**
 * Created by tess on 4/9/14.
 */
public class PatternMatcher {
    private LinkedList<String> closestWords = null;

    private int[][] matrix;

    private int closestDistance = -1;

    private String[] oldword = {};

    public PatternMatcher(){

    }

    public String getBestMatch(String line, Set<String> PatternList){
        String bestMatch = "";
        int wordsInCommon = 0;
        String[] wordList = line.split(" ");
        for(String pattern: PatternList ){

            int wordsCount = 0;
            String[] patternList = pattern.split(" ");
            for(int i = 0; i < wordList.length; i++){
                for(int j = 0; j< patternList.length; j++){
                    if(wordList[i].equals(patternList[j])){
                        wordsCount++;
                        break;
                    }
                }
            }
            if(wordsCount> wordsInCommon){
                bestMatch = pattern;
                wordsInCommon = wordsCount;
            }
        }
        return bestMatch;
    }

    int partDist(String[] w1, String[] w2, int w1len, int w2len) {
        //System.out.println("partdistance");
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

    int Distance(String[] w1, String[] w2) {
        //System.out.println("Distance");
        String[] tmp= oldword;
        if(w2.length > oldword.length){
          // System.out.println("w2 is longer");
            for(int i= 0; i<oldword.length;i++){
                //System.out.println(w2[i]);
                tmp[i] = w2[i];
                //System.out.println(tmp[i]);

            }
        }
        //System.out.println("after for");
        if((w2.length > oldword.length) && (tmp.equals(oldword))){
            //System.out.println("if");
            oldword = w2;

            return partDist(w1, w2, w1.length, w2.length);
        }
        else{
            //System.out.println("else");
            for(int[] row: matrix){
                Arrays.fill(row, 0);
            }
            oldword = w2;
            return partDist(w1, w2, w1.length, w2.length);
        }
    }

    public String ClosestWords(String w, Set<String> wordList) {
        String[] word1 = w.split(" ");
        matrix = new int[w.length()+1][30];
        for (String s : wordList) {
            //System.out.println(s);
            String[] word2 = s.split(" ");
            if((Math.abs(word1.length-word2.length) <= closestDistance) || (closestDistance == -1)){
                int dist = Distance(word1, word2);
                //System.out.println("d(" + w + "," + s + ")=" + dist);
                if (dist < closestDistance || closestDistance == -1) {
                    closestDistance = dist;
                    closestWords = new LinkedList<String>();
                    closestWords.add(s);
                }
                else if (dist == closestDistance)
                    closestWords.add(s);
            }
        }
        //System.out.println("finished");
        Random r = new Random(closestWords.size());
        closestDistance = -1;
        //System.out.println(closestWords.get(0));
        if(closestWords.isEmpty()){
            System.out.println("return null");
            return null;
        }
        //System.out.println(closestWords.size());
        String retur = closestWords.get(r.nextInt(closestWords.size()));
        closestWords.clear();
        return retur;
    }

    int getMinDistance() {
        return closestDistance;
    }

    private LinkedList<String> getClosestWords() {
        return closestWords;
    }
}
