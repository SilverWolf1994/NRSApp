package sngapp.android.unisinu.com.nrsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pbCargandoA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pbCargandoA = (ProgressBar) findViewById(R.id.pbCargando);
        pbCargandoA.setMax(4000);

        Thread thread = new Thread(){
            public void run(){
                try {
                    int cargando = 0, segundo = 1000;
                    while(cargando < 4000)
                    {
                        sleep(segundo);
                        cargando += segundo;
                        pbCargandoA.setProgress(cargando);
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intentusuariologin = new Intent(MainActivity.this, UsuarioLogin.class);
                    startActivity(intentusuariologin);
                }
            }
        };
        thread.start();
    }
}
