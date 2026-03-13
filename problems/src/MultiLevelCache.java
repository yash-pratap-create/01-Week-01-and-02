import java.util.*;
class MultiLevelCache {
    static class Video {
        String id;
        String data;
        int access;
        Video(String id, String data) {
            this.id = id;
            this.data = data;
            this.access = 1;
        }
    }
    private final int L1_CAP = 10000;
    private final int L2_CAP = 100000;
    private LinkedHashMap<String, Video> L1 = new LinkedHashMap<>(16, 0.75f, true) { protected boolean removeEldestEntry(Map.Entry<String, Video> e) { return size() > L1_CAP;
    }
    };
    private LinkedHashMap<String, Video> L2 = new LinkedHashMap<>(16, 0.75f, true) { protected boolean removeEldestEntry(Map.Entry<String, Video> e) { return size() > L2_CAP;
    }
    };
    private Map<String, Video> L3 = new HashMap<>();
    private int l1Hit = 0, l2Hit = 0, l3Hit = 0, miss = 0;
    public MultiLevelCache() {
        for (int i = 1; i <= 200000; i++) {
            L3.put("video_" + i, new Video("video_" + i, "data_" + i));
        }
    }
    public String getVideo(String id) {
        if (L1.containsKey(id)) {
            l1Hit++;
            Video v = L1.get(id);
            v.access++;
            return "L1 HIT → " + v.data;
        }
        if (L2.containsKey(id)) {
            l2Hit++;
            Video v = L2.get(id);
            v.access++;
            promoteToL1(v);
            return "L2 HIT → promoted to L1";
        }
        if (L3.containsKey(id)) {
            l3Hit++;
            Video v = L3.get(id);
            v.access++;
            L2.put(id, v);
            return "L3 HIT → added to L2";
        }
        miss++;
        return "VIDEO NOT FOUND";
    }
    private void promoteToL1(Video v) {
        L1.put(v.id, v);
    }
    public void invalidate(String id) {
        L1.remove(id);
        L2.remove(id);
        L3.remove(id);
    }
    public void updateVideo(String id, String data) {
        Video v = new Video(id, data);
        L3.put(id, v);
        L2.put(id, v);
        L1.put(id, v);
    }
    public void getStatistics() {
        int total = l1Hit + l2Hit + l3Hit + miss;
        double l1Rate = total == 0 ? 0 : (l1Hit * 100.0 / total);
        double l2Rate = total == 0 ? 0 : (l2Hit * 100.0 / total);
        double l3Rate = total == 0 ? 0 : (l3Hit * 100.0 / total);
        double overall = ((l1Hit + l2Hit + l3Hit) * 100.0) / total;
        System.out.println("L1 Hit Rate: " + l1Rate + "% Avg Time: 0.5ms"); System.out.println("L2 Hit Rate: " + l2Rate + "% Avg Time: 5ms"); System.out.println("L3 Hit Rate: " + l3Rate + "% Avg Time: 150ms");
        System.out.println("Overall Hit Rate: " + overall + "%"); }
    public static void main(String[] args) {
        MultiLevelCache cache = new MultiLevelCache();
        System.out.println(cache.getVideo("video_123")); System.out.println(cache.getVideo("video_123")); System.out.println(cache.getVideo("video_999"));
        cache.getStatistics();
    }
}
