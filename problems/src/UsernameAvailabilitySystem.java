import java.util.*;
class UsernameChecker {
    private HashMap<String, Integer> userDatabase;
    private HashMap<String, Integer> attemptFrequency;
    public UsernameChecker() {
        userDatabase = new HashMap<>();
        attemptFrequency = new HashMap<>();
        userDatabase.put("john_doe", 101);
        userDatabase.put("admin", 1);
        userDatabase.put("alex99", 202);
    }
    public boolean checkAvailability(String username) {
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);
        return !userDatabase.containsKey(username);
    }
    public List<String> suggestAlternatives(String username) { List<String> suggestions = new ArrayList<>();
        suggestions.add(username + "1");
        suggestions.add(username + "2");
        suggestions.add(username.replace("_", "."));
        suggestions.add(username + "123");
        return suggestions;
    }
    public String getMostAttempted() {
        String mostAttempted = "";
        int maxAttempts = 0;
        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }
        return mostAttempted + " (" + maxAttempts + " attempts)"; }
}
public class UsernameAvailabilitySystem {
    public static void main(String[] args) {
        UsernameChecker checker = new UsernameChecker(); System.out.println("john_doe → " +
                checker.checkAvailability("john_doe"));
        System.out.println("jane_smith → " +
                checker.checkAvailability("jane_smith"));
        System.out.println(checker.suggestAlternatives("john_doe"));
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("john_doe");
        System.out.println(checker.getMostAttempted());
    }
}

