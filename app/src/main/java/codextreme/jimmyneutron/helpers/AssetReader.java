package codextreme.jimmyneutron.helpers;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class AssetReader {
    public static String read(Context c, String filename) {
        AssetManager assetManager = c.getAssets();
        InputStream input;
        String text = "";

        try {
            input = assetManager.open(filename);

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            text = new String(buffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
