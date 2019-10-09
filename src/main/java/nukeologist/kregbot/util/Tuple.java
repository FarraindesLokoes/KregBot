package nukeologist.kregbot.util;

import java.util.Objects;

public class Tuple<A, B> {
    public A a;
    public B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != Tuple.class) return false;
        return Objects.equals(a, b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
