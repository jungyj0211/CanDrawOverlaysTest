package com.example.yongjin.candrawoverlaystest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context;
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(this)){

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(intent);

            }
        }


        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(Settings.canDrawOverlays(getApplicationContext())){

                        context = getApplicationContext();
                        context.startService(new Intent(context,WindowService.class));
                        Toast.makeText(getApplicationContext(),"다른 앱 위에 그리기 허용", Toast.LENGTH_SHORT).show();
                        return;

                    }else
                        Toast.makeText(getApplicationContext(),"다른 앱 위에 그리기 허용 안됨",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Test");
                builder.setMessage("Test")
                        .setCancelable(true)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                context = getApplicationContext();
                                context.stopService(new Intent(context, WindowService.class));
                                finish();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

                builder.create();
                builder.show();




            }
        });

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}
