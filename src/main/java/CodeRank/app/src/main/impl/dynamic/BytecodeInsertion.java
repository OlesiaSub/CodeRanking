package CodeRank.app.src.main.impl.dynamic;

import java.util.HashMap;

public class BytecodeInsertion {

    private static class Pair {
        public Object fst;
        public Object snd;

        public Pair(Object fstt, Object sndd) {
            fst = fstt;
            snd = sndd;
        }
    }

    public HashMap<Pair, Integer> storage = new HashMap<>();

    public void toInsert(Object source, Object target, int methodId) {
        Pair pair = new Pair(source, target);
        if (!storage.containsKey(new Pair(source, target))) {
            storage.put(pair, 0);
        } else {
            storage.put(pair, storage.get(pair) + 1);
        }
    }

    public void printt() {
        System.out.println("came here yeeeeeeeeeees");
    }
}
