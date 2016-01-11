package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;
import java.lang.String;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static int wordLength = DEFAULT_WORD_LENGTH;

    private static HashSet<String> wordSet = new HashSet<>();
    //private static ArrayList<String> wordList = new ArrayList<>();
    private static HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private static HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordSet.add(word);

            //wordList.add(word);

            String sorted = sortWord(word);

            if(lettersToWord.containsKey(sorted)) {
                lettersToWord.get(sorted).add(word);
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                lettersToWord.put(sorted, list);
            }

            if(sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                sizeToWords.put(word.length(), list);
            }
        }

        CleanUp();
    }

    public static void CleanUp(){
        for (String s : wordSet){
            String sortedword = sortWord(s);

            if (lettersToWord.containsKey(sortedword) && lettersToWord.get(sortedword).size() < MIN_NUM_ANAGRAMS){
                for (String str : lettersToWord.get(sortedword)) {
                    sizeToWords.get(str.length()).remove(str);
                }
                lettersToWord.remove(lettersToWord.get(sortedword));
            }
        }

    }

    public static String sortWord(String input){
        char[] chars = input.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);

        return sorted;
    }

    public boolean isGoodWord(String word, String base) {
        if(!wordSet.contains(word)){
            System.out.println("Word is not in the List");
            return false;
        }

        // does word contain a substring base?
        if (word.contains(base)){
            System.out.println("Word contains the base word");
            return false;
        }

        return true;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();

        for (char c = 'a'; c <= 'z'; c++){
            String test = sortWord(word + c);

            if (lettersToWord.containsKey(test)){
                ArrayList<String> anagrams = lettersToWord.get(test);

                for (String s : anagrams){
                    if (isGoodWord(s, word)) {
                        result.add(s);
                    }
                }
            }
            // else do nothing, no anagram was found
        }

        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> list = sizeToWords.get(wordLength);

        while (list.size() < 1){
            wordLength++;
            list = sizeToWords.get(wordLength);
        }

        int i = Math.abs(random.nextInt() % list.size());
        String answer = new String();
        int counter = 0;

        for ( ; ; i++, counter++) {
            String word = list.get(i);
            String sortedword = sortWord(word);
            ArrayList<String> sortedwordList = lettersToWord.get(sortedword);
            if (sortedwordList.size() >= MIN_NUM_ANAGRAMS) {
                answer = word;
                break;
            }

            if (i == list.size() - 1) {
                i = 0;
            }

            if (counter == list.size()){
                if (wordLength < MAX_WORD_LENGTH){
                    wordLength++;
                }
                list = sizeToWords.get(wordLength);
                i = Math.abs(random.nextInt() % list.size());
                counter = 0;
            }
        }

        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }

        return answer;
    }
}
