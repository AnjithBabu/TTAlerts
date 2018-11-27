package utils;

import android.app.Activity;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pojos.Settings;

public class AppDataReadWriter {

    String filename = "settings.tta";
    String fileContents = "empty";
    FileOutputStream outputStream;

    public boolean writeSettingsToAppData(Activity act, Settings settObj) throws Exception{
        outputStream = act.openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(fileContents.getBytes());
        outputStream.close();

        return true;
    }

    public Settings readSettingsFromAppData(Activity act) throws IOException{

        FileInputStream fin = null;

        try {
            fin = act.openFileInput(filename);
            int c;
            String data="";
            while( (c = fin.read()) != -1){
                data = data + Character.toString((char)c);
            }


        }catch (FileNotFoundException fne){
            return new Settings();
        }finally {
            fin.close();
        }

        return null;
    }
}
