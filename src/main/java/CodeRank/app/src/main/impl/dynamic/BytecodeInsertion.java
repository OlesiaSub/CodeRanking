package CodeRank.app.src.main.impl.dynamic;

import java.util.HashMap;
import java.util.Objects;

public class BytecodeInsertion {

    private static class InvocationData {
        public String source;
        public String target;
        public String invocationType;

        public InvocationData(String source, String target, String invocationType) {
            this.source = source;
            this.target = target;
            this.invocationType = invocationType;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof InvocationData)) {
                return false;
            }
            InvocationData data = (InvocationData) o;
            return this.invocationType.equals(data.invocationType) &&
                    this.target.equals(data.target) && this.source.equals(data.source);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target, invocationType);
        }
    }

    public static HashMap<InvocationData, Integer> storage = new HashMap<>();

    public static void toInsert(String source, String target, String invocationType) {
        InvocationData invocationData = new InvocationData(source, target, invocationType);
        if (!storage.containsKey(invocationData)) {
            storage.put(invocationData, 0);
        } else {
            storage.put(invocationData, storage.get(invocationData) + 1);
        }
        System.out.println(storage.get(invocationData));
    }
}
