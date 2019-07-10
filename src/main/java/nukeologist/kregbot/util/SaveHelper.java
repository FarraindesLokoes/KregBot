package nukeologist.kregbot.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nukeologist.kregbot.KregBot;

import java.io.*;

/**
 * Class to help with saving data
 */
public class SaveHelper<T> {

    private final Class<T> type;

    public SaveHelper(Class<T> type) {
        this.type = type;
    }

    public boolean saveJson(T object, String fileName) {
        try (Writer writer = new FileWriter(fileName + ".json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(object, writer);
        } catch (IOException e) {
            KregBot.LOG.info("failed to save json with filename {}.json", fileName);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public T fromJson(String fileName) {
        T obj;
        try (Reader reader = new FileReader(fileName + ".json")) {
            Gson gson = new GsonBuilder().create();
            obj = gson.fromJson(reader, type);
        }catch(IOException e) {
            KregBot.LOG.info("failed to retrieve json with filename {}.json", fileName);
            e.printStackTrace();
            return null;
        }
        return obj;
    }
}
