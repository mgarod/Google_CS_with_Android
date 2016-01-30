package com.google.engedu.ghost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        if(s.length() == 0){
            isWord = true;
        }
        else if ( children.containsKey(Character.toString(s.charAt(0))) ){
            children.get(Character.toString(s.charAt(0))).add(s.substring(1));
        }
        else{
            TrieNode n = new TrieNode();
            children.put(Character.toString(s.charAt(0)), n);
            n.add(s.substring(1));
        }
    }

    public boolean isWord(String s) {
        if (s.length() == 0){
            return isWord;
        }

        String c = Character.toString(s.charAt(0));
        if ( children.containsKey(c) ){
            TrieNode n = children.get(c);
            return n.isWord(s.substring(1));
        } else {
            return false;
        }
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode cursor = this;

        for (int i = 0; i < s.length(); ++i){
            String c = Character.toString(s.charAt(i));

            if (cursor.children.containsKey(c))
                cursor = cursor.children.get(c);
            else
                return null;
        }

        Random r = new Random();
        while(!cursor.isWord){
            final Object[] objects = cursor.children.keySet().toArray();
            String[] stringArray = Arrays.copyOf(objects, objects.length, String[].class);
            final int index = r.nextInt(stringArray.length);
            String c = stringArray[index];

            System.out.println(c);

            s = s + c;

            System.out.println(s);

            cursor = cursor.children.get(c);
        }

        return s;
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode cursor = this;

        for (int i = 0; i < s.length(); ++i){
            String c = Character.toString(s.charAt(i));

            if (cursor.children.containsKey(c))
                cursor = cursor.children.get(c);
            else
                return null;
        }

        Random r = new Random();
        while(!cursor.isWord){
            final Object[] objects = cursor.children.keySet().toArray();
            String[] stringArray = Arrays.copyOf(objects, objects.length, String[].class);
            int index = r.nextInt(stringArray.length);
            String c = stringArray[index];

            int counter = stringArray.length;
            while (cursor.children.get(c).isWord && counter > 0){
                index = (index + 1) % stringArray.length;
                c = stringArray[index];
                counter--;
                System.out.println("infinite loop?");
            }

            // If no good letter was found (all children lead to words), c will be the
            // last letter observed, and as good as any to use to bluff the human
            s = s + c;

            cursor = cursor.children.get(c);
        }

        return s;
    }
}
