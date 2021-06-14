package rank.pain.impl.dynamic;

import java.io.Serializable;
import java.util.Objects;

public class InvocationData implements Serializable {
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
