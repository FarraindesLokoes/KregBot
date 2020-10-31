package nukeologist.kregbot.data;

import nukeologist.kregbot.util.Tuple;

import java.util.*;

public class DickInfo {

    private final long USER;
    private Map<String, Integer> dickData;

    public DickInfo(final long OWNER) {
        this.USER = OWNER;
        this.dickData = new HashMap<>();
    }

    public void addToDickData(Tuple<String, Integer> dick) {
        this.dickData.put(dick.a, dick.b);
    }

    public Map<String, Integer> getDickData() {
        return dickData;
    }

    public long getUSER() {
        return USER;
    }


}
