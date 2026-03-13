import java.util.*;
class Event {
    String url;
    String userId;
    String source;
    public Event(String url, String userId, String source) { this.url = url;
        this.userId = userId;
        this.source = source;
    }
}
class RealTimeAnalytics {
    private HashMap<String, Integer> pageViews; private HashMap<String, Set<String>> uniqueVisitors; private HashMap<String, Integer> trafficSources;
    public RealTimeAnalytics() {
        pageViews = new HashMap<>();
        uniqueVisitors = new HashMap<>();
        trafficSources = new HashMap<>();
    }
    public void processEvent(Event e) {
        pageViews.put(e.url, pageViews.getOrDefault(e.url, 0) + 1);
        uniqueVisitors.putIfAbsent(e.url, new HashSet<>());
        uniqueVisitors.get(e.url).add(e.userId);
        trafficSources.put(e.source, trafficSources.getOrDefault(e.source, 0) + 1); }
    public void getDashboard() {
        List<Map.Entry<String, Integer>> pages =
                new ArrayList<>(pageViews.entrySet());
        pages.sort((a, b) -> b.getValue() - a.getValue());
        System.out.println("Top Pages:");
        int count = 0;
        for (Map.Entry<String, Integer> entry : pages) {
            if (count == 10) break;
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();
            System.out.println((count + 1) + ". " + url +
                    " - " + views + " views (" + unique + " unique)");
            count++;
        }
        int totalSource = 0;
        for (int v : trafficSources.values()) {
            totalSource += v;
        }
        System.out.println("\nTraffic Sources:");
        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {
            double percent = ((double) entry.getValue() / totalSource) * 100;
            System.out.printf("%s: %.0f%%\n",
                    entry.getKey(), percent);
        }
    }
}
public class AnalyticsDashboard {
    public static void main(String[] args) {
        RealTimeAnalytics analytics = new RealTimeAnalytics();
        analytics.processEvent(new Event("/article/breaking-news", "user_123", "Google")); analytics.processEvent(new Event("/article/breaking-news", "user_456", "Facebook")); analytics.processEvent(new Event("/sports/championship", "user_789", "Direct")); analytics.processEvent(new Event("/sports/championship", "user_123", "Google")); analytics.processEvent(new Event("/sports/championship", "user_999", "Google")); analytics.processEvent(new Event("/article/breaking-news", "user_888", "Direct")); analytics.getDashboard();
    }
}
