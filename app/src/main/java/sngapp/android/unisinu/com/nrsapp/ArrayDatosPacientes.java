package sngapp.android.unisinu.com.nrsapp;

/**
 * Created by ACER on 22/04/2016.
 */
public class ArrayDatosPacientes {

    private String cedulaPaciente;
    private String nombrePaciente;
    private String apellidoPaciente;
    private int edadPaciente;
    private int camaPaciente;

    public void setCedulaPaciente(String cedulaPaciente) { this.cedulaPaciente = cedulaPaciente; }

    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public void setApellidoPaciente(String apellidoPaciente) { this.apellidoPaciente = apellidoPaciente; }

    public void setEdadPaciente(int edadPaciente) { this.edadPaciente = edadPaciente; }

    public void setCamaPaciente(int camaPaciente) { this.camaPaciente = camaPaciente; }

    public String getCedulaPaciente() { return cedulaPaciente; }

    public String getNombrePaciente()
    {
        return nombrePaciente;
    }

    public String getApellidoPaciente()
    {
        return apellidoPaciente;
    }

    public int getEdadPaciente() { return edadPaciente; }

    public int getCamaPaciente() { return camaPaciente; }

}
