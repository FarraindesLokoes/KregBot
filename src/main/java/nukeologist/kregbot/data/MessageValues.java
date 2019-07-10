package nukeologist.kregbot.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nukeologist
 */
public class MessageValues {

    private Map<Long, Map<String, Integer>> VALUES = new HashMap<>();

    public void add(long guild, String str, int number) {
        Map<String, Integer> possible = VALUES.get(guild);
        if (possible != null) {
            Integer current = possible.get(str);
            if (current != null) {
                current += number;
                possible.replace(str, current);
            } else {
                possible.put(str, number);
            }
        } else {
            VALUES.put(guild, new HashMap<>());
            possible = VALUES.get(guild);
            Integer current = possible.get(str);
            if (current != null) {
                current += number;
                possible.replace(str, current);
            } else {
                possible.put(str, number);
            }
        }
    }

    public Map<Long, Map<String, Integer>> getMap() {
        return VALUES;
    }

    public Map<String, Integer> getMapOfGuild(long id) {
        return VALUES.get(id);
    }
}
