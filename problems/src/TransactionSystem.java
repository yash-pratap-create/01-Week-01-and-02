import java.util.*;
class TransactionSystem {
    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time;
        Transaction(int id, int amount, String merchant, String account, long time) { this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }
    List<Transaction> transactions = new ArrayList<>();
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }
    public List<int[]> findTwoSum(int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<int[]> res = new ArrayList<>();
        for (Transaction t : transactions) {
            int comp = target - t.amount;
            if (map.containsKey(comp)) {
                res.add(new int[]{map.get(comp).id, t.id});
            }
            map.put(t.amount, t);
        }
        return res;
    }
    public List<int[]> findTwoSumWithinHour(int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<int[]> res = new ArrayList<>();
        for (Transaction t : transactions) {
            int comp = target - t.amount;
            if (map.containsKey(comp)) {
                Transaction prev = map.get(comp);
                if (Math.abs(t.time - prev.time) <= 3600000) {
                    res.add(new int[]{prev.id, t.id});
                }
            }
            map.put(t.amount, t);
        }
        return res;
    }
    public List<List<Integer>> findKSum(int k, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(0, k, target, new ArrayList<>(), result);
        return result;
    }
    private void backtrack(int start, int k, int target, List<Integer> path, List<List<Integer>> res) { if (k == 0 && target == 0) {
        res.add(new ArrayList<>(path));
        return;
    }
        if (k == 0) return;
        for (int i = start; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            path.add(t.id);
            backtrack(i + 1, k - 1, target - t.amount, path, res);
            path.remove(path.size() - 1);
        }
    }
    public void detectDuplicates() {
        Map<String, Set<String>> map = new HashMap<>();
        for (Transaction t : transactions) {
            String key = t.amount + "|" + t.merchant;
            map.putIfAbsent(key, new HashSet<>());
            map.get(key).add(t.account);
        }
        for (String key : map.keySet()) {
            if (map.get(key).size() > 1) {
                String[] parts = key.split("\\|");
                System.out.println("Duplicate -> amount:" + parts[0] + " merchant:" + parts[1] + " accounts:" + map.get(key));
            }
        }
    }
    public static void main(String[] args) {
        TransactionSystem system = new TransactionSystem();
        system.addTransaction(new Transaction(1, 500, "Store A", "acc1", 1000000)); system.addTransaction(new Transaction(2, 300, "Store B", "acc2", 1300000)); system.addTransaction(new Transaction(3, 200, "Store C", "acc3", 1600000)); system.addTransaction(new Transaction(4, 500, "Store A", "acc2", 2000000));
        List<int[]> pairs = system.findTwoSum(500);
        for (int[] p : pairs) {
            System.out.println("TwoSum: " + p[0] + " , " + p[1]);
        }
        List<int[]> pairsHour = system.findTwoSumWithinHour(500);
        for (int[] p : pairsHour) {
            System.out.println("TwoSum1Hour: " + p[0] + " , " + p[1]);
        }
        List<List<Integer>> ksum = system.findKSum(3, 1000);
        for (List<Integer> list : ksum) {
            System.out.println("KSum: " + list);
        }
        system.detectDuplicates();
    }
}
