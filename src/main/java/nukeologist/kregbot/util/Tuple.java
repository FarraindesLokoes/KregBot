package nukeologist.kregbot.util;

import java.util.Objects;

public class Tuple<A, B> {
    public A a;
    public B b;

    public static <A, B> Tuple<A, B> of(A a, B b) {
        return new Tuple<>(a, b);
    }

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public Tuple<B, A> inverse() {
        return new Tuple<>(b, a);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != Tuple.class) return false;
        final Tuple o = (Tuple) obj;
        return Objects.equals(a, o.a) && Objects.equals(b, o.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
