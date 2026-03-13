import java.util.*;
class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;
    public DNSEntry(String domain, String ipAddress, int ttlSeconds) { this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000L); }
    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}
class DNSCache {
    private int capacity;
    private HashMap<String, DNSEntry> cache;
    private LinkedHashMap<String, DNSEntry> lru;
    private int hits;
    private int misses;
    public DNSCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        lru = new LinkedHashMap<>(capacity, 0.75f, true);
    }
    public String resolve(String domain) {
        DNSEntry entry = cache.get(domain);
        if (entry != null && !entry.isExpired()) {
            hits++;
            lru.get(domain);
            System.out.println("Cache HIT → " + entry.ipAddress);
            return entry.ipAddress;
        }
        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
            lru.remove(domain);
            System.out.println("Cache EXPIRED → Query upstream"); } else {
            System.out.println("Cache MISS → Query upstream");
        }
        misses++;
        String ip = queryUpstreamDNS(domain);
        DNSEntry newEntry = new DNSEntry(domain, ip, 300);
        if (cache.size() >= capacity) {
            String oldest = lru.keySet().iterator().next();
            cache.remove(oldest);
            lru.remove(oldest);
        }
        cache.put(domain, newEntry);
        lru.put(domain, newEntry);
        System.out.println(domain + " → " + ip + " (TTL: 300s)");
        return ip;
    }
    private String queryUpstreamDNS(String domain) {
        Random r = new Random();
        return "172.217.14." + (200 + r.nextInt(20));
    }
    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : ((double) hits / total) * 100;
        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%"); System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
    }
}
public class DNSCacheSystem {
    public static void main(String[] args) throws InterruptedException {
        DNSCache cache = new DNSCache(5);
        cache.resolve("google.com");
        cache.resolve("google.com");
        Thread.sleep(2000);
        cache.resolve("google.com");
        cache.resolve("amazon.com");
        cache.resolve("openai.com");
        cache.getCacheStats();
    }
}
