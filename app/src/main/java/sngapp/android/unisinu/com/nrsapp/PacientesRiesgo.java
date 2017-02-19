package sngapp.android.unisinu.com.nrsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class PacientesRiesgo extends AppCompatActivity {

    private ArrayList<ArrayDatosPacientes> arrayDatosPacientes = new ArrayList<>();
    private ArrayDatosPacientes arrayDatosP;
    private Activity thisActivity;
    private ProgressDialog progressDialog;
    private ListView rvListadoPacientesA;
    private final String urlControlPaciente = "http://192.168.1.11/NRSApp/control/control_paciente.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pacientes_riesgo);
        thisActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvListadoPacientesA = (ListView) findViewById(R.id.rvListadoPacientes);
        listarPacientesBD();

        rvListadoPacientesA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Paciente: " + arrayDatosPacientes.get(position).getNombrePaciente() + " en estado de riesgo", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }


    public void listarPacientesBD()
    {
        AsyncHttpClient httpclient = new AsyncHttpClient();;
        RequestParams params = new RequestParams();
        params.put("opcion", 5);

        httpclient.post(urlControlPaciente, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(thisActivity);
                progressDialog.setMessage("Listando Pacientes \nPor favor espere...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject jsonobject;
                    if (response.getJSONArray("Pacientes").length() > 0) {

                        for (int i = 0; i < response.getJSONArray("Pacientes").length(); i++) {
                            arrayDatosP = new ArrayDatosPacientes();
                            jsonobject = response.getJSONArray("Pacientes").getJSONObject(i);
                            arrayDatosP.setCedulaPaciente(jsonobject.getString("Cedula_Paciente"));
                            arrayDatosP.setNombrePaciente(jsonobject.getString("Nombre_Paciente"));
                            arrayDatosP.setApellidoPaciente(jsonobject.getString("Apellido_Paciente"));
                            arrayDatosP.setEdadPaciente(jsonobject.getInt("Edad_Paciente"));
                            arrayDatosP.setCamaPaciente(jsonobject.getInt("Cama_Paciente"));
                            arrayDatosPacientes.add(arrayDatosP);
                        }
                        rvListadoPacientesA.setAdapter(new AdapterDatosPacientes(thisActivity, arrayDatosPacientes));

                    } else {
                        Toast.makeText(thisActivity, "No se encontraron pacientes a listar", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    progressDialog.dismiss();

                } catch (JSONException localJSONException) {
                    Toast.makeText(thisActivity, localJSONException.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                Toast.makeText(thisActivity, "Error al conectar a la BD", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
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
