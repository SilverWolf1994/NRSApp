package sngapp.android.unisinu.com.nrsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class UsuarioFormato extends AppCompatActivity {

    private Activity thisActivity;
    private ProgressDialog progressDialog;
    private int codigoUsuario;
    private String nombreUsuario, rbResp1, rbResp2, rbResp3, rbResp4;
    private TextView tvNombreAuxiliarValoracionA, tvHoraFechaValoracionA;
    private EditText etCedulaPacienteValoracionA;
    private RadioButton rbSiResp1A, rbNoResp1A, rbSiResp2A, rbNoResp2A, rbSiResp3A, rbNoResp3A, rbSiResp4A, rbNoResp4A;
    private Button bRegistrarPacienteValoracionA, bPacientesRiesgoValoracionA;
    private FloatingActionButton fabGuardarValoracionA;
    private final String urlControlPaciente = "http://192.168.1.11/NRSApp/control/control_paciente.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_formato);
        thisActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvNombreAuxiliarValoracionA = (TextView) findViewById(R.id.tvNombreAuxiliarValoracion);
        tvHoraFechaValoracionA = (TextView) findViewById(R.id.tvHoraFechaValoracion);
        etCedulaPacienteValoracionA = (EditText) findViewById(R.id.etCedulaPacienteValoracion);
        rbSiResp1A = (RadioButton) findViewById(R.id.rbSiResp1);
        rbNoResp1A = (RadioButton) findViewById(R.id.rbNoResp1);
        rbSiResp2A = (RadioButton) findViewById(R.id.rbSiResp2);
        rbNoResp2A = (RadioButton) findViewById(R.id.rbNoResp2);
        rbSiResp3A = (RadioButton) findViewById(R.id.rbSiResp3);
        rbNoResp3A = (RadioButton) findViewById(R.id.rbNoResp3);
        rbSiResp4A = (RadioButton) findViewById(R.id.rbSiResp4);
        rbNoResp4A = (RadioButton) findViewById(R.id.rbNoResp4);
        bPacientesRiesgoValoracionA = (Button) findViewById(R.id.bPacientesRiesgoValoracion);
        bRegistrarPacienteValoracionA = (Button) findViewById(R.id.bRegistrarPacienteValoracion);
        fabGuardarValoracionA = (FloatingActionButton) findViewById(R.id.fabGuardarValoracion);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            codigoUsuario = bundle.getInt("USUARIO_CODIGO");
            nombreUsuario = bundle.getString("USUARIO_NOMBRE");
        }

        tvNombreAuxiliarValoracionA.setText(nombreUsuario);
        tvHoraFechaValoracionA.setText(obtenerFecha() + " | " + obtenerHora());

        fabGuardarValoracionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!obtenerRespuestas())
                {
                    Toast.makeText(thisActivity, "Seleccione todas las respuestas", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (etCedulaPacienteValoracionA.getText().toString().equals(""))
                    {
                        Toast.makeText(thisActivity, "Digite la cedula del paciente a valorar", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String cedulaPaciente = etCedulaPacienteValoracionA.getText().toString();
                        registrarValoracion(obtenerFecha(), obtenerHora(), rbResp1, rbResp2, rbResp3, rbResp4, cedulaPaciente);
                    }
                }
            }
        });

        bPacientesRiesgoValoracionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentpacienteriesgo = new Intent(thisActivity, PacientesRiesgo.class);
                startActivity(intentpacienteriesgo);
            }
        });

        bRegistrarPacienteValoracionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("USUARIO_CODIGO", codigoUsuario);
                Intent intentpacienteregistrar = new Intent(thisActivity, PacienteRegistrar.class);
                intentpacienteregistrar.putExtras(bundle);
                startActivity(intentpacienteregistrar);
            }
        });
    }

    public void registrarValoracion(String fecha, String hora, String resp1, String resp2, String resp3, String resp4, String cedula_paciente)
    {
        try {
            AsyncHttpClient httpclient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("opcion", 2);
            params.put("fecha_valoracion", fecha);
            params.put("hora_valoracion", hora);
            params.put("resp1_valoracion", resp1);
            params.put("resp2_valoracion", resp2);
            params.put("resp3_valoracion", resp3);
            params.put("resp4_valoracion", resp4);
            params.put("cedula_paciente", cedula_paciente);

            httpclient.post(urlControlPaciente, params, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    progressDialog = new ProgressDialog(thisActivity);
                    progressDialog.setMessage("Registrando Valoración \nPor favor espere...");
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
                            Snackbar.make(findViewById(R.id.clUsuarioFormato), "Registro de valoración guardado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

    public boolean obtenerRespuestas()
    {
        boolean respuesta;
        if ((!rbSiResp1A.isChecked() && !rbNoResp1A.isChecked()) || (!rbSiResp2A.isChecked() && !rbNoResp2A.isChecked()) || (!rbSiResp3A.isChecked() && !rbNoResp3A.isChecked()) || (!rbSiResp4A.isChecked() && !rbNoResp4A.isChecked()))
        {
            respuesta = false;
        }
        else
        {
            if (rbSiResp1A.isChecked())
            {
                rbResp1 = rbSiResp1A.getText().toString();
            }
            else if (rbNoResp1A.isChecked())
            {
                rbResp1 = rbNoResp1A.getText().toString();
            }
            if (rbSiResp2A.isChecked())
            {
                rbResp2 = rbSiResp2A.getText().toString();
            }
            else if (rbNoResp2A.isChecked())
            {
                rbResp2 = rbNoResp2A.getText().toString();
            }
            if (rbSiResp3A.isChecked())
            {
                rbResp3 = rbSiResp3A.getText().toString();
            }
            else if (rbNoResp3A.isChecked())
            {
                rbResp3 = rbNoResp3A.getText().toString();
            }
            if (rbSiResp4A.isChecked())
            {
                rbResp4 = rbSiResp4A.getText().toString();
            }
            else if (rbNoResp4A.isChecked())
            {
                rbResp4 = rbNoResp4A.getText().toString();
            }

            respuesta = true;
        }
        return respuesta;
    }

    public String obtenerFecha()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = simpleDateFormat.format(calendar.getTime());
        return fecha;
    }

    public String obtenerHora()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        String hora = simpleDateFormat.format(calendar.getTime());
        return hora;
    }

    public void limpiarCampos()
    {
        etCedulaPacienteValoracionA.setText("");
        tvHoraFechaValoracionA.setText(obtenerFecha() + " | " + obtenerHora());
        rbSiResp1A.setChecked(false);
        rbNoResp1A.setChecked(false);
        rbSiResp2A.setChecked(false);
        rbNoResp2A.setChecked(false);
        rbSiResp3A.setChecked(false);
        rbNoResp3A.setChecked(false);
        rbSiResp4A.setChecked(false);
        rbNoResp4A.setChecked(false);
        etCedulaPacienteValoracionA.requestFocus();
    }

    public void createSimpleDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);

        builder.setTitle("Cerrar NRS")
                .setMessage("¿Desea cerrar la aplicacion?")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        builder.create();
        builder.show();
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

    @Override
    public void onBackPressed()
    {
        createSimpleDialog();
    }

}
