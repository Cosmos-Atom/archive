import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TrieDictionary3 {
    Scanner sc=new Scanner(System.in);
    private static final int MAX_SUGGESTIONS = 5;

    private class TrieNode {
        TrieNode[] children;
        boolean isWord;

        TrieNode() {
            children = new TrieNode[26];
            isWord = false;
        }
    }

    TrieNode root;

    TrieDictionary3() {
        root = new TrieNode();
    }

    private void insertWord(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';
            if (index < 0 || index >= 26) {
                continue;
            }
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isWord = true;
    }

    //spellcheck function
    public void spellcheck()
    {
        System.out.println("Enter a word to check spelling");
        String tocheck=sc.next();
        int count=0;
        TrieNode node = root;
        for (int i = 0; i < tocheck.length(); i++) 
        {
            char c = tocheck.charAt(i);
            for(int j=0;j<26;j++)
            {
                if ((c-'a')==j && node.children[j]!=null) 
                {
                    count++;
                    node = node.children[j];
                    break;
                }
                else{
                    continue;
                }
            }
        }
        if(count==tocheck.length() && node.isWord)// && (node.children[(tocheck.length())-1])==null)
        {
            System.out.println("The word is a valid word");
        }
        else{
            System.out.println("The word is not a valid word");
        }
    }

    private void autoComplete(TrieNode node, StringBuilder prefix, List<String> suggestions) {
        if (suggestions.size() >= MAX_SUGGESTIONS) {
            return;
        }

        if (node.isWord) {
            suggestions.add(prefix.toString());
        }

        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                prefix.append((char) ('a' + i));
                autoComplete(node.children[i], prefix, suggestions);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    public List<String> autoComplete(String prefix) {
        TrieNode node = root;
        StringBuilder sb = new StringBuilder(prefix.toLowerCase());
        List<String> suggestions = new ArrayList<>();

        // Traverse the Trie until the end of the prefix
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            int index = c - 'a';
            if (index < 0 || index >= 26 || node.children[index] == null) {
                return suggestions;
            }
            node = node.children[index];
        }

        // Perform autocomplete from the node at the end of the prefix
        autoComplete(node, sb, suggestions);

        // Sort the suggestions alphabetically
        Collections.sort(suggestions);

        return suggestions;
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        TrieDictionary3 dictionary = new TrieDictionary3();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("words_pos.csv"));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(",");
                if (words.length > 1) {
                    String word = words[1].toLowerCase();
                    dictionary.insertWord(word);
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        

        while(true)
        {
            System.out.println("Menu driven:");
            System.out.println("1.SpellCheck");
            System.out.println("2.Autocomplete");
            System.out.println("3.Exit");
            System.out.println("Enter your choice:");
            int ch=sc.nextInt();
            switch(ch){
                case 1:
                    {
                        dictionary.spellcheck();
                        break;
                    }
                case 2:
                    {
                        Scanner scanner = new Scanner(System.in);
                        System.out.print("Enter the prefix: ");
                        String prefix = scanner.nextLine();

                        // Perform autocomplete and get suggestions
                        List<String> suggestions = dictionary.autoComplete(prefix);

                        if (suggestions.isEmpty()) {
                            System.out.println("No suggestions found for prefix: " + prefix);
                        } 
                        else {
                            System.out.println("Top " + MAX_SUGGESTIONS + " suggestions for prefix \"" + prefix + "\":");
                            for (int i = 0; i < Math.min(suggestions.size(), MAX_SUGGESTIONS); i++) {
                                System.out.println(suggestions.get(i));
                            }
                        }
                        break;
                    }
                    case 3:
                    {
                        System.out.println("THANK YOU");
                        return;
                    }
                
            }
    }
}
}