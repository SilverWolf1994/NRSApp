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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UsuarioRegistrar extends AppCompatActivity {

    private Activity thisActivity;
    private ProgressDialog progressDialog;
    private EditText etNombreUsuarioA, etApellidoUsuarioA, etEmailUsuarioA, etPasswordUsuarioA, etConfirmarPasswordUsuarioA;
    private RadioButton rbFemeninoUsuarioA, rbMasculinoUsuarioA;
    private FloatingActionButton fabRegistrarUsuarioA;
    private final String urlControlUsuario = "http://192.168.1.11/NRSApp/control/control_usuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_registrar);
        thisActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etNombreUsuarioA = (EditText) findViewById(R.id.etNombreUsuario);
        etApellidoUsuarioA = (EditText) findViewById(R.id.etApellidoUsuario);
        etEmailUsuarioA = (EditText) findViewById(R.id.etEmailUsuario);
        etPasswordUsuarioA = (EditText) findViewById(R.id.etPasswordUsuario);
        etConfirmarPasswordUsuarioA = (EditText) findViewById(R.id.etConfirmarPasswordUsuario);
        rbFemeninoUsuarioA = (RadioButton) findViewById(R.id.rbFemeninoUsuario);
        rbMasculinoUsuarioA = (RadioButton) findViewById(R.id.rbMasculinoUsuario);
        fabRegistrarUsuarioA = (FloatingActionButton) findViewById(R.id.fabRegistrarUsuario);

        rbFemeninoUsuarioA.setChecked(true);

        fabRegistrarUsuarioA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = etNombreUsuarioA.getText().toString();
                String apellido = etApellidoUsuarioA.getText().toString();
                String genero = seleccionarGenero();
                String email = etEmailUsuarioA.getText().toString();
                String password = etPasswordUsuarioA.getText().toString();
                String confirmarPassword = etConfirmarPasswordUsuarioA.getText().toString();
                if (nombre.equals("") || apellido.equals("") || email.equals("") || password.equals("") || confirmarPassword.equals(""))
                {
                    Snackbar.make(view, "Verifique campos vacios", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else
                {
                    if (confirmarPassword(password, confirmarPassword))
                    {
                        registrarUsuario(email, password, nombre, apellido, genero);
                    }
                    else
                    {
                        Snackbar.make(view, "Verifique las password digitadas", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }
        });
    }

    public void registrarUsuario(String email, String password, String nombre, String apellido, String genero)
    {
        try {
            AsyncHttpClient httpclient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("opcion", 1);
            params.put("email_usuario", email);
            params.put("password_usuario", password);
            params.put("nombre_usuario", nombre);
            params.put("apellido_usuario", apellido);
            params.put("genero_usuario", genero);

            httpclient.post(urlControlUsuario, params, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    progressDialog = new ProgressDialog(thisActivity);
                    progressDialog.setMessage("Registrando Medico \nPor favor espere...");
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
                            Toast.makeText(thisActivity, "REGISTRO EXITOSO", Toast.LENGTH_LONG).show();
                            finish();
                            Intent intentusuariologin = new Intent(thisActivity, UsuarioLogin.class);
                            startActivity(intentusuariologin);
                        } else if (respuesta == 0) {
                            Toast.makeText(thisActivity, response.getString("Mensaje_Error"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException jsonexception) {
                        Toast.makeText(thisActivity, "Error al recibir datos del servidor", Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }catch (Exception exception) {
            Toast.makeText(thisActivity, "Error de conexión", Toast.LENGTH_SHORT).show();
        }
    }

    public String seleccionarGenero()
    {
        String seleccionGenero = "";
        if (rbFemeninoUsuarioA.isChecked())
        {
            seleccionGenero = rbFemeninoUsuarioA.getText().toString();
        }
        else if (rbMasculinoUsuarioA.isChecked())
        {
            seleccionGenero = rbMasculinoUsuarioA.getText().toString();
        }
        return seleccionGenero;
    }

    public boolean confirmarPassword(String password, String confirmarPassword)
    {
        return (password.equals(confirmarPassword));
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

}
