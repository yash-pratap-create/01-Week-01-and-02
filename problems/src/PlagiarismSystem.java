import java.util.*;
class PlagiarismDetector {
    private HashMap<String, Set<String>> ngramIndex; private int n;
    public PlagiarismDetector(int n) {
        this.n = n;
        ngramIndex = new HashMap<>();
    }
    public List<String> generateNGrams(String text) {
        List<String> ngrams = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");
        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(words[i + j]).append(" ");
            }
            ngrams.add(sb.toString().trim());
        }
        return ngrams;
    }
    public void addDocument(String docId, String text) { List<String> ngrams = generateNGrams(text);
        for (String gram : ngrams) {
            ngramIndex.putIfAbsent(gram, new HashSet<>()); ngramIndex.get(gram).add(docId);
        }
    }
    public void analyzeDocument(String docId, String text) {
        List<String> ngrams = generateNGrams(text);
        HashMap<String, Integer> matchCount = new HashMap<>(); for (String gram : ngrams) {
            if (ngramIndex.containsKey(gram)) {
                for (String doc : ngramIndex.get(gram)) {
                    if (!doc.equals(docId)) {
                        matchCount.put(doc,
                                matchCount.getOrDefault(doc, 0) + 1);
                    }
                }
            }
        }
        System.out.println("Extracted " + ngrams.size() + " n-grams");
        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {
            int matches = entry.getValue();
            double similarity =
                    ((double) matches / ngrams.size()) * 100;
            System.out.println("Found " + matches +
                    " matching n-grams with \"" +
                    entry.getKey() + "\"");
            System.out.printf("Similarity: %.2f%% ", similarity);
            if (similarity > 50) {
                System.out.println("(PLAGIARISM DETECTED)");
            } else if (similarity > 10) {
                System.out.println("(suspicious)");
            } else {
                System.out.println();
            }
        }
    }
}
public class PlagiarismSystem {
    public static void main(String[] args) {
        PlagiarismDetector detector = new PlagiarismDetector(5);
        String essay1 = "data structures and algorithms are important for computer science students learning programming concepts";
        String essay2 = "data structures and algorithms are important for computer science students studying programming principles";
        String essay3 = "machine learning and artificial intelligence are transforming modern technology and research";
        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        System.out.println("analyzeDocument(\"essay_123.txt\")");
        detector.analyzeDocument("essay_123.txt", essay3);
    }
}
