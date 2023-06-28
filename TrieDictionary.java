import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TrieDictionary 
{
    private class TrieNode 
    {
        TrieNode[] children; //trie has children (26 each)
        boolean isWord;

        TrieNode() 
        {
            children = new TrieNode[26]; //only taking input of lowercase English alphabets (important)
            isWord = false;
        }
    }

    TrieNode root;

    TrieDictionary() 
    {
        root = new TrieNode(); //intial assign
    }

    private void insertWord(String word) 
    {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) 
        {
            char c = word.charAt(i);
            int index = c - 'a'; //to get the index where the letter should be inserted(ex: is char is b, its ascii value is 98 and a is 97. index will be 1, at 1 it will be inserted)
            if (index < 0 || index >= 26) 
            {
                // Skip characters that are not lowercase English alphabets
                continue;
            }
            if (node.children[index] == null) 
            {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isWord = true;
    }

    String getWord(TrieNode node, StringBuilder sb) 
    {
        if (node.isWord) 
        {
            return sb.toString();
        }

        for (int i = 0; i < 26; i++) 
        {
            if (node.children[i] != null) 
            {
                sb.append((char) ('a' + i));
                String word = getWord(node.children[i], sb);
                if (!word.isEmpty()) 
                {
                    return word;
                }
                sb.deleteCharAt(sb.length() - 1);
            }
        }

        return "";
    }

    public static void main(String[] args) 
    {
        TrieDictionary dictionary = new TrieDictionary();

        try 
        {
            BufferedReader reader = new BufferedReader(new FileReader("words_pos.csv"));

            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] words = line.split(","); // CSV file spliting (0,1,2) columns are present
                if (words.length > 1) {
                    String word = words[1].toLowerCase(); //the word is in the second column and converting it to lowercase
                    dictionary.insertWord(word);
                }
            }

            reader.close();
        } 
        catch (IOException e) 
        {
            System.out.println("Error reading file: " + e.getMessage());
        }

        StringBuilder sb = new StringBuilder();
        String word = dictionary.getWord(dictionary.root, sb); //first word is aa
        System.out.println("Word from the dictionary: " + word);
    }
}
