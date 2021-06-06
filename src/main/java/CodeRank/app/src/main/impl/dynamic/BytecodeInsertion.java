package CodeRank.app.src.main.impl.dynamic;

import java.io.ObjectStreamClass;
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
            InvocationData data2 = (InvocationData) o;
            return this.invocationType.equals(data2.invocationType) &&
                    this.target.equals(data2.target) && this.source.equals(data2.source);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target, invocationType);
        }
    }

    public BytecodeInsertion() {

    }

    public static HashMap<InvocationData, Integer> storage = new HashMap<>();

    public static void toInsert(String source, String target, String invocationType) {
//        System.out.println("ENTRY INSERTION");
        InvocationData invocationData = new InvocationData(source, target, invocationType);
        if (!storage.containsKey(invocationData)) {
            storage.put(invocationData, 0);
        } else {
            storage.put(invocationData, storage.get(invocationData) + 1);
        }
//        System.out.println(storage.get(invocationData));
//        System.out.println("EXIT INSERTION");
    }
}
