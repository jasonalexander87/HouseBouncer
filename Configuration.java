package com.iasonas.housebouncer;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;


public class Configuration extends AppCompatActivity {

    Button save;
    EditText etskypeA, restartTime;
    SeekBar seekBar;
    Intent mainac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        if(height <= 800) {
            setContentView(R.layout.activity_configurationsh320);
        }else if((height <= 1600) && (height > 800)) {
            setContentView(R.layout.activity_configurationsh800);
        }else {
            setContentView(R.layout.activity_configurationsh1600);

        }


        save = (Button)findViewById(R.id.buttonSave);
        etskypeA = (EditText) findViewById(R.id.editTextSkypeA);
        restartTime = (EditText) findViewById(R.id.editTextRestart);
        seekBar = (SeekBar) findViewById(R.id.seekBarSensitivity);

        mainac = new Intent(this, MainActivityF.class);


        save.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                String skypeAdd = etskypeA.getText().toString();
                String restartT = restartTime.getText().toString();
                int sensitivity= seekBar.getProgress();

                if(inputCheck(skypeAdd, restartT)) {

                    int restartTime = Integer.parseInt(restartT);
                    if(restartTime > 140) { restartTime = 140; }

                    saveToSP(skypeAdd, sensitivity, restartTime);

                    Toast.makeText(Configuration.this, "SAVED", Toast.LENGTH_SHORT).show();

                    startActivity(mainac);
                    finish();


                } else {

                    Toast.makeText(Configuration.this, "Please enter correct values", Toast.LENGTH_SHORT).show();


                }

            }

        });


    }

    @Override
    public void onBackPressed()
    {
        startActivity(mainac);
        finish();
    }

    @Override
    protected void onPause() {

        super.onPause();
        finish();

    }


    public void saveToSP(String skypeAddress, int sense, int restartTime) {

        Context con = getApplicationContext();
        SharedPreferences sharedPrefs = con.getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("SKYPE", skypeAddress);
        editor.putInt("RESTART", restartTime);
        editor.putInt("SENSE", sense);

        editor.apply();

    }

    public boolean inputCheck(String text1, String text2) {

        if(text1 != null && text1.length() !=0 && text2 != null && text2.length() !=0 ) { return true; }


        return false;
    }

}
