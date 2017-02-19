package sngapp.android.unisinu.com.nrsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UsuarioLogin extends AppCompatActivity {

    private Activity thisActivity;
    private ProgressDialog progressDialog;
    private EditText etCodigoLoginA, etPasswordLoginA;
    private Button bRegistrarMedicoA;
    private FloatingActionButton fabLoginA;
    private final String urlControlUsuario = "http://192.168.1.11/NRSApp/control/control_usuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_login);
        thisActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etCodigoLoginA = (EditText) findViewById(R.id.etCodigoLogin);
        etPasswordLoginA = (EditText) findViewById(R.id.etPasswordLogin);
        bRegistrarMedicoA = (Button) findViewById(R.id.bRegistrarMedico);
        fabLoginA = (FloatingActionButton) findViewById(R.id.fabLogin);

        bRegistrarMedicoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentregistrarusuario = new Intent(thisActivity, UsuarioRegistrar.class);
                startActivity(intentregistrarusuario);
            }
        });

        fabLoginA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = etCodigoLoginA.getText().toString();
                String password = etPasswordLoginA.getText().toString();
                login(usuario, password);
            }
        });
    }

    public void login(String usuario, String password)
    {
        AsyncHttpClient httpclient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("opcion", 0);
        params.put("codigo_usuario", usuario);
        params.put("password_usuario", password);

        try
        {
            httpclient.post(urlControlUsuario, params, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    progressDialog = new ProgressDialog(thisActivity);
                    progressDialog.setMessage("Iniciando Sesión \nPor favor espere...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        progressDialog.dismiss();

                        int respuesta = response.getInt("Success");
                        if (respuesta == 1) {
                            int codigoUsuarioBD = response.getInt("Codigo_Usuario");
                            String nombreUsuarioBD = response.getString("Nombre_Usuario");

                            Bundle bundle = new Bundle();
                            bundle.putInt("USUARIO_CODIGO", codigoUsuarioBD);
                            bundle.putString("USUARIO_NOMBRE", nombreUsuarioBD);
                            Intent intentlistadoproyectos = new Intent(thisActivity, UsuarioFormato.class);
                            intentlistadoproyectos.putExtras(bundle);
                            startActivity(intentlistadoproyectos);

                        } else if (respuesta == 0) {
                            Toast.makeText(thisActivity, response.getString("Mensaje_Error"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException jsonexception) {
                        Toast.makeText(thisActivity, "Error al recibir datos del servidor", Toast.LENGTH_SHORT).show();
                    }

                }

            });
        }
        catch (Exception exception)
        {
            Toast.makeText(thisActivity, "Error de conexión", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_informacion:
                notificar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void notificar()
    {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(thisActivity);

        builder.setTitle("Información")
                .setCancelable(false)
                .setMessage("Aplicación desarrollada por: \n" + "Brandon David Chavez Noguera \n" + "Email: developerandroid.app94@gmail.com")
                .setPositiveButton("ACEPTAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        builder.create();
        builder.show();
    }

}
