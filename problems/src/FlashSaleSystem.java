import java.util.*;
class FlashSaleInventory {
    private HashMap<String, Integer> stock;
    private HashMap<String, LinkedHashMap<Integer, Integer>> waitingList; public FlashSaleInventory() {
        stock = new HashMap<>();
        waitingList = new HashMap<>();
        stock.put("IPHONE15_256GB", 100);
        waitingList.put("IPHONE15_256GB", new LinkedHashMap<>());
    }
    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }
    public synchronized void purchaseItem(String productId, int userId) {
        int available = stock.getOrDefault(productId, 0);
        if (available > 0) {
            stock.put(productId, available - 1);
            System.out.println("Success, " + (available - 1) + " units remaining");
        } else {
            LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);
            if (queue == null) {
                queue = new LinkedHashMap<>();
                waitingList.put(productId, queue);
            }
            queue.put(userId, queue.size() + 1);
            System.out.println("Added to waiting list, position #" + queue.size()); }
    }
}
public class FlashSaleSystem{
    public static void main(String[] args) {
        FlashSaleInventory inventory = new FlashSaleInventory();
        System.out.println("Stock: " + inventory.checkStock("IPHONE15_256GB") + " units available");
        inventory.purchaseItem("IPHONE15_256GB", 12345);
        inventory.purchaseItem("IPHONE15_256GB", 67890);
        for (int i = 1; i <= 100; i++) {
            inventory.purchaseItem("IPHONE15_256GB", 20000 + i);
        }
        inventory.purchaseItem("IPHONE15_256GB", 99999);
    }
}
