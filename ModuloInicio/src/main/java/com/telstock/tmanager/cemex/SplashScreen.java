package com.telstock.tmanager.cemex;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.telstock.tmanager.cemex.funciones.InformacionTelefonica;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    private Context context;

    @Nullable
    @BindView(R.id.tv_version)
    TextView tvVersion;

    private InformacionTelefonica informacionTelefonica;

    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = this;

        ButterKnife.bind(this);

        informacionTelefonica = new InformacionTelefonica(context);

        tvVersion.setText(informacionTelefonica.obtenerVersionAplicacion(context));

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                Intent mainIntent = new Intent().setClass(SplashScreen.this,
                        LoginActivity.class);
                startActivity(mainIntent);

                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
