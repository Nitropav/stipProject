package kursClient.db;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DBConfigurator {

    public DBProperties getProperties() {
        String json = "";
        try {
            StringBuilder stringBuffer = new StringBuilder();
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/dbProperties.json")));
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {

                stringBuffer.append(line).append("\n");
            }

            json = stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.fromJson(json, DBProperties.class);
    }
}