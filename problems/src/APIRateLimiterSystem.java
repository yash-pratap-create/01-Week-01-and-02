import java.util.*;
class TokenBucket {
    int tokens;
    int maxTokens;
    long lastRefillTime;
    int refillRate;
    public TokenBucket(int maxTokens, int refillRate) { this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis(); }
    public synchronized boolean allowRequest() {
        refillTokens();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }
    private void refillTokens() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;
        int tokensToAdd = (int) (elapsed / refillRate);
        if (tokensToAdd > 0) {
            tokens = Math.min(maxTokens, tokens + tokensToAdd); lastRefillTime = now;
        }
    }
    public int getTokens() {
        refillTokens();
        return tokens;
    }
    public long getResetTime() {
        return lastRefillTime + (maxTokens * refillRate);
    }
}
class RateLimiter {
    private HashMap<String, TokenBucket> clients;
    private int limit;
    private int refillRate;
    public RateLimiter(int limit, int refillRate) {
        clients = new HashMap<>();
        this.limit = limit;
        this.refillRate = refillRate;
    }
    public void checkRateLimit(String clientId) {
        clients.putIfAbsent(clientId, new TokenBucket(limit, refillRate));
        TokenBucket bucket = clients.get(clientId);
        if (bucket.allowRequest()) {
            System.out.println("Allowed (" + bucket.getTokens() + " requests remaining)"); } else {
            long retryAfter = (bucket.getResetTime() - System.currentTimeMillis()) / 1000;
            System.out.println("Denied (0 requests remaining, retry after " + retryAfter + "s)"); }
    }
    public void getRateLimitStatus(String clientId) {
        TokenBucket bucket = clients.get(clientId);
        if (bucket == null) {
            System.out.println("No record for client");
            return;
        }
        int used = limit - bucket.getTokens();
        long reset = bucket.getResetTime() / 1000;
        System.out.println("{used: " + used + ", limit: " + limit + ", reset: " + reset + "}"); }
}
public class APIRateLimiterSystem {
    public static void main(String[] args) {
        RateLimiter limiter = new RateLimiter(1000, 3600);
        limiter.checkRateLimit("abc123");
        limiter.checkRateLimit("abc123");
        limiter.checkRateLimit("abc123");
        limiter.getRateLimitStatus("abc123");
    }
}

