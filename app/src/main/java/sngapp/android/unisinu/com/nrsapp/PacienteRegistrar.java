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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PacienteRegistrar extends AppCompatActivity {

    private Activity thisActivity;
    private ProgressDialog progressDialog;
    private int codigoUsuario;
    private EditText etCedulaPacienteA, etNombrePacienteA, etApellidoPacienteA, etEdadPacienteA, etCamaPacienteA;
    private FloatingActionButton fabRegistrarPacienteA;
    private final String urlControlPaciente = "http://192.168.1.11/NRSApp/control/control_paciente.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_registrar);
        thisActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etCedulaPacienteA = (EditText) findViewById(R.id.etCedulaPaciente);
        etNombrePacienteA = (EditText) findViewById(R.id.etNombrePaciente);
        etApellidoPacienteA = (EditText) findViewById(R.id.etApellidoPaciente);
        etEdadPacienteA = (EditText) findViewById(R.id.etEdadPaciente);
        etCamaPacienteA = (EditText) findViewById(R.id.etCamaPaciente);
        fabRegistrarPacienteA = (FloatingActionButton) findViewById(R.id.fabRegistrarPaciente);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            codigoUsuario = bundle.getInt("USUARIO_CODIGO");
        }

        fabRegistrarPacienteA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCedulaPacienteA.getText().toString().equals("") || etNombrePacienteA.getText().toString().equals("") || etApellidoPacienteA.getText().toString().equals("") || etEdadPacienteA.getText().toString().equals("") || etCamaPacienteA.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Error: Compruebe campos vacios", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String cedula = etCedulaPacienteA.getText().toString();
                    String nombre = etNombrePacienteA.getText().toString();
                    String apellido = etApellidoPacienteA.getText().toString();
                    int edad = Integer.parseInt(etEdadPacienteA.getText().toString());
                    int cama = Integer.parseInt(etCamaPacienteA.getText().toString());
                    registrarPaciente(cedula, nombre, apellido, edad, cama, codigoUsuario);
                }
            }
        });
    }

    public void registrarPaciente(String cedula, String nombre, String apellido, int edad, int cama, int auxmedico)
    {
        try {
            AsyncHttpClient httpclient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("opcion", 1);
            params.put("cedula_paciente", cedula);
            params.put("nombre_paciente", nombre);
            params.put("apellido_paciente", apellido);
            params.put("cama_paciente", cama);
            params.put("edad_paciente", edad);
            params.put("codigo_medico", auxmedico);

            httpclient.post(urlControlPaciente, params, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    progressDialog = new ProgressDialog(thisActivity);
                    progressDialog.setMessage("Registrando Paciente \nPor favor espere...");
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
                            String cedulaPacienteBD = response.getString("Cedula_Paciente");
                            Toast.makeText(thisActivity, "REGISTRO EXITOSO \nPaciente con la cedula: " + cedulaPacienteBD, Toast.LENGTH_LONG).show();
                            limpiarCampos();
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

    public void limpiarCampos()
    {
        etCedulaPacienteA.setText("");
        etNombrePacienteA.setText("");
        etApellidoPacienteA.setText("");
        etEdadPacienteA.setText("");
        etCamaPacienteA.setText("");
        etCedulaPacienteA.requestFocus();
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
