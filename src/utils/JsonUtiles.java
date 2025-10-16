package utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtiles {

    /**
     * Lee un archivo JSON y devuelve su contenido como String.
     * Si el archivo no existe, devuelve "[]".
     */
    public static String leer(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (IOException e) {
            return "[]";  // si no existe, devolvemos array vacío para evitar errores al parsear
        }
    }

    /**
     * Graba un JSONArray en un archivo, en formato legible (pretty print)
     */
    public static void grabar(JSONArray array, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(array.toString(4));  // 4 es la indentación
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

