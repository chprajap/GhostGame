/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWordi;
    private Random rand = new Random();

    public TrieNode() {
        children = new HashMap<>();
        isWordi = false;
    }

    public void add(String s) {
        HashMap<Character, TrieNode> temp = children;
        for (int i = 0; i < s.length(); i++){
            if (!temp.containsKey(s.charAt(i))){
                temp.put(s.charAt(i), new TrieNode());
            }
            if (i == s.length() - 1){
                temp.get(s.charAt(i)).isWordi = true;
            }
            temp = temp.get(s.charAt(i)).children;
        }
    }

    public boolean isWord(String s) {
        TrieNode temp = searchNode(s);
        if (temp == null)
            return false;
        else
            return temp.isWordi;

    }

    private TrieNode searchNode(String s) {
        TrieNode temp = this;
        for (int i = 0; i < s.length(); i++){
            if (!temp.children.containsKey(s.charAt(i))){
                return null;
            }
            temp = temp.children.get(s.charAt(i));
        }
        return temp;


    }

    public String getAnyWordStartingWith(String s) {

        TrieNode temp =searchNode(s);
        if (temp==null) {
            return null;
        } else {
                for (Character c : temp.children.keySet()) {
                    temp = temp.children.get(c);
                    s += c;
                    break;
                 }
            }
        return s;
     }

    public String getGoodWordStartingWith(String s) {
        TrieNode temp = searchNode(s);
        int flag=0;
        String alphabets="abcdefghijklmnopqrstuvwxyz";
        ArrayList<Character> wordending = new ArrayList<Character>();
        wordending.clear();
        if(s==null || s==""){
            int y = rand.nextInt(alphabets.length());
            s+=alphabets.charAt(y);
            return s;
        }
        else {
            if (temp == null) {
                return null;
            } else {
                for (Character c : temp.children.keySet()) {
                    temp = temp.children.get(c);

                    try {
                        if (temp.isWordi && temp != null) {
                            wordending.add(c);
                        } else {
                            s += c;
                            flag = 1;
                            break;
                        }
                        if (flag == 0) {
                            for (int i = 0; i < alphabets.length(); i++) {
                                int w = rand.nextInt(alphabets.length());
                                if (!(wordending.contains(alphabets.charAt(w)))) {
                                    s += alphabets.charAt(w);
                                    break;
                                }
                            }

                        }
                    } catch (Exception e) {
                        //Toast.makeText(this, "Exception caught restart the game", Toast.LENGTH_LONG).show();
                    }
                }
                return s;
            }
        }
    }
}
