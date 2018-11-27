package utils;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.abdevs.project.ttcalerts.MainActivity;

public class AppStorageHandler extends AsyncTask<Void, Void, String> {

    private MainActivity mainAct;
    private String error;
    private  boolean status;
    private int option;

    public AppStorageHandler(MainActivity mainAct, int option) {
        this.mainAct = mainAct;
        this.error = null;
        this.option = option;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {

            AppDataReadWriter appStorageHandler = new AppDataReadWriter();
            //status = appStorageHandler.readSettingsFromAppData(mainAct);

            if(status){
                error = "Settings updated !!!";
            }else {
                error = "Sorry, cannot add settings, " +
                        "please close tta app completely and try again";
            }

        }catch (Exception e){
            error = "Sorry, cannot add settings, " +
                    "please close tta app completely and try again";
            Log.d("TTAAlerts_Error", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        switch (option){
            case (AppConstants.CREATE_SETTGS):

                break;
        }

        Toast.makeText(mainAct,error,Toast.LENGTH_LONG);
    }


}
