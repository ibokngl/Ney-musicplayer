package com.example.ibokan.neymusicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ibokan on 28.12.2016.
 */

public class SplashScreen extends Activity {

    TextView txtDate;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        /*txtDate= (TextView) findViewById(R.id.txtDate);
        myAsycTask myA = new myAsycTask();
        myA.execute();*/

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                    Intent intent = new Intent(getApplicationContext(),Pesrevler.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

    }


/*class  myAsycTask extends AsyncTask<String ,Void,String > {
    @Override
    protected String doInBackground(String... strings) {
        return getIdContent();
    }
    @Override
    protected void onPostExecute(String s) {
        txtDate.setText(s);
        super.onPostExecute(s);
    }
}

    private  String getIdContent(){

        String line ="";
        String data ="";

        try {
            URL url = new URL("http://kevsereskicuma.com/webservice/date.php");
            HttpURLConnection con  = (HttpURLConnection)url.openConnection();

            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(false);

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream response = con.getInputStream();
                InputStreamReader reader = new InputStreamReader(response);
                BufferedReader br =new BufferedReader(reader);
                while ((line=br.readLine()) !=null){
                    data +=line;
                }
            }

        }catch (Exception e){}

        return  data ;
    }*/

}