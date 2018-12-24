/*
Given a set of words (without duplicates), find all the wordsquares.

Input:
["area","lead","wall","lady","ball"]

Output:
[
  [ "wall",
    "area",
    "lead",
    "lady"
  ],
  [ "ball",
    "area",
    "lead",
    "lady"
  ]
]
*/

// Use Trie to speed up DFS with backtracking.
class WordSquareFinder {

    private class TrieNode{
        String val=null;
        Map<Character,TrieNode> children = new HashMap<>();
    }

    private static int N;
    TrieNode root;
    List<List<String>> res;

    public List<List<String>> wordSquares(String[] words) {
        res = new ArrayList<>();
        N = words[0].length();

        if(N==1){
            for(String word: words){
                List<String> temp = new ArrayList<>();
                temp.add(word);
                res.add(temp);
            }
            return res;
        }

        root = new TrieNode();
        for(String word: words){
            insert(word);
        }

        for(String word: words){
            List<String> square = new ArrayList<>();
            square.add(word);
            DFS(word,1,square);
        }

        return res;
    }

    private void DFS(String word, int index, List<String> square){
        if(index==N){
            res.add(square);
            return;
        }

        StringBuilder prefix = new StringBuilder();
        for(int i=0; i<index; i++){
            char letter = square.get(i).charAt(index);
            prefix.append(letter);
        }

        List<String> candidates = matchPrefix(prefix.toString());

        for(String candidate: candidates){
            List<String> mySquare = new ArrayList<>();
            mySquare.addAll(square);
            mySquare.add(candidate);
            DFS(candidate, index+1, mySquare);
        }
    }

    private List<String> matchPrefix(String pre){
        List<String> res = new ArrayList<>();
        TrieNode cur = root;
        for(int i=0; i<pre.length(); i++){
            char letter = pre.charAt(i);
            if(cur.children.containsKey(letter)) cur = cur.children.get(letter);
            else return res;
        }
        addAllChildren(cur,res);
        return res;
    }

    private void addAllChildren(TrieNode start, List<String> res){
        Queue<TrieNode> q = new LinkedList<>();
        q.offer(start);
        while(!q.isEmpty()){
            TrieNode cur = q.poll();
            if(cur.val!=null){
                res.add(cur.val);
                continue;
            }
            else q.addAll(cur.children.values());
        }
    }

    private void insert(String word){
        TrieNode cur = root;
        for(int i=0; i<word.length(); i++){
            char letter = word.charAt(i);
            if(cur.children.containsKey(letter)) cur = cur.children.get(letter);
            else{
                cur.children.put(letter,new TrieNode());
                cur = cur.children.get(letter);
            }
        }
        cur.val = word;
    }
}
