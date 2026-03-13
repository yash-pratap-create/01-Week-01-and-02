import java.util.*;
class AutocompleteSystem {
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>(); PriorityQueue<Query> top = new PriorityQueue<>((a, b) -> { if (a.freq == b.freq) return b.text.compareTo(a.text); return a.freq - b.freq;
        });
    }
    static class Query {
        String text;
        int freq;
        Query(String t, int f) {
            text = t;
            freq = f;
        }
    }
    private TrieNode root = new TrieNode();
    private Map<String, Integer> freqMap = new HashMap<>();
    public AutocompleteSystem(String[] queries, int[] freqs) { for (int i = 0; i < queries.length; i++) {
        addQuery(queries[i], freqs[i]);
    }
    }
    private void addQuery(String query, int freq) {
        freqMap.put(query, freqMap.getOrDefault(query, 0) + freq); TrieNode node = root;
        Query q = new Query(query, freqMap.get(query));
        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
            updateTop(node, q);
        }
    }
    private void updateTop(TrieNode node, Query q) { node.top.removeIf(x -> x.text.equals(q.text));
        node.top.offer(q);
        if (node.top.size() > 10) node.top.poll();
    }
    public List<String> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) return new ArrayList<>();
            node = node.children.get(c);
        }
        List<Query> list = new ArrayList<>(node.top);
        list.sort((a, b) -> {
            if (b.freq == a.freq) return a.text.compareTo(b.text);
            return b.freq - a.freq;
        });
        List<String> res = new ArrayList<>();
        for (Query q : list) res.add(q.text + " (" + q.freq + ")");
        return res;
    }
    public void updateFrequency(String query) {
        addQuery(query, 1);
    }
    public static void main(String[] args) {
        String[] queries = {"java tutorial", "javascript", "java download"};
        int[] freqs = {1234567, 987654, 456789};
        AutocompleteSystem ac = new AutocompleteSystem(queries, freqs);
        System.out.println(ac.search("jav"));
        ac.updateFrequency("java 21 features");
        ac.updateFrequency("java 21 features");
        ac.updateFrequency("java 21 features");
        System.out.println(ac.search("java"));
    }
}
