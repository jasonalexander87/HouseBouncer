package com.iasonas.housebouncer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivityF extends AppCompatActivity {

    Button start, configure, privacy;
    Intent activityService, configuration;
    String skype;
    int restart, sense;
    boolean hasSkype = false;
    boolean hasConf = false;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainsh800);



        activityService = new Intent(this, Activity21.class);
        configuration = new Intent(this, Configuration.class);

        configure = (Button)findViewById(R.id.buttonConf);
        start = (Button)findViewById(R.id.buttonStart);
        privacy = (Button)findViewById(R.id.buttonPP);

        if((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))

        {
            ActivityCompat.requestPermissions(this,new String[]{

                    Manifest.permission.CAMERA,

            },REQUEST_CAMERA_PERMISSION);

        }

        privacy.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                String url = "https://sites.google.com/view/hbapp";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                finish();


            }

        });

        configure.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                startActivity(configuration);
                finish();


            }

        });


        start.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {


                String ver = getSkypeVer();
                if(ver.equals("YOU DONT HAVE SKYPE INSTALLED")) {

                    hasSkype = false;
                    Toast.makeText(MainActivityF.this, ver, Toast.LENGTH_SHORT).show();

                } else { hasSkype = true; }


                if(hasSkype) {

                    hasConf = getParams();

                    if(!hasConf) {

                        Toast.makeText(MainActivityF.this, "Configure parameters first", Toast.LENGTH_SHORT).show();

                    }
                }

                if(hasConf && hasSkype) {

                    activityService.putExtra("skypeA", skype);
                    activityService.putExtra("sensitivity", sense);
                    activityService.putExtra("restartT", restart);

                    startActivity(activityService);
                    finish();


                }

            }

        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CAMERA_PERMISSION)

        {

            if(grantResults[0] != PackageManager.PERMISSION_GRANTED )

            {

                Toast.makeText(this, "CAMERA PERMISSIONS REQUIRED", Toast.LENGTH_SHORT).show();

                finish();

            }

        }

    }


    public boolean getParams() {

        Context con = getApplicationContext();
        SharedPreferences sharedPrefs = con.getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);

        if(!sharedPrefs.contains("SKYPE")) { return false; }

        skype = sharedPrefs.getString("SKYPE", "SKYPE");
        restart = sharedPrefs.getInt("RESTART",140);
        sense = sharedPrefs.getInt("SENSE",5);

        return true;

    }


    public String getSkypeVer() {

        try {
            PackageInfo pinfo = null;
            pinfo = getPackageManager().getPackageInfo("com.skype.raider", 0);
            String verName = pinfo.versionName;


            return "SKYPE VERSION" + verName;


        }catch(android.content.pm.PackageManager.NameNotFoundException e) { return "YOU DONT HAVE SKYPE INSTALLED"; }


    }

}

