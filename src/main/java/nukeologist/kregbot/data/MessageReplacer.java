package nukeologist.kregbot.data;

import nukeologist.kregbot.listeners.ReplacerListener;

import java.util.HashMap;
import java.util.Map;

public class MessageReplacer {
    private Map<Long, Map<String, String>> VALUES = new HashMap<>();

    public void add(long guild, String original, String replacer) {
        Map<String, String> possible = VALUES.get(guild);
        if (possible != null) {
            if (possible.containsKey(original)) {
                possible.replace(original, replacer);
            } else {
                possible.put(original, replacer);
            }
        } else {
            VALUES.put(guild, new HashMap<>());
            possible = VALUES.get(guild);
            if (possible.containsKey(original)) {
                possible.replace(original, replacer);
            } else {
                possible.put(original, replacer);
            }
        }
        ReplacerListener.SAVER.saveJson(this, "replacers");
    }

    public boolean remove(long guild, String original) {
        Map<String, String> possible = VALUES.get(guild);

        if (possible != null) {
            if (possible.containsKey(original)) {
                possible.remove(original);
                return true;
            }
        }
        return false;
    }

    public Map<String, String> getFromGuild(long guild){
        return VALUES.get(guild);
    }

    public Map<Long, Map<String, String>> getMap() {
        return VALUES;
    }

    public Map<String, String> getMapOfGuild(long id) {
        return VALUES.get(id);
    }
}
