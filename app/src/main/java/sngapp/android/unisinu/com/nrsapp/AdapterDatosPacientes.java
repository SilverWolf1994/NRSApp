package sngapp.android.unisinu.com.nrsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by ACER on 22/04/2016.
 */
public class AdapterDatosPacientes extends ArrayAdapter<ArrayDatosPacientes> {

    private ViewHolder viewHolder;
    private LayoutInflater layoutInflater;

    public AdapterDatosPacientes(Context context, List<ArrayDatosPacientes> objects)
    {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.list_item_pacientes, null);
            viewHolder = new ViewHolder();

            viewHolder.setTVCedulaItemPacienteAdap((TextView) convertView.findViewById(R.id.tvCedulaItemListadoPacientes));
            viewHolder.setTVNombreItemPacienteAdap((TextView) convertView.findViewById(R.id.tvNombreItemListadoPacientes));
            viewHolder.setTVApellidoItemPacienteAdap((TextView) convertView.findViewById(R.id.tvApellidoItemListadoPacientes));
            viewHolder.setTVEdadItemPacienteAdap((TextView) convertView.findViewById(R.id.tvEdadItemListadoPacientes));
            viewHolder.setTVCamaItemPacienteAdap((TextView) convertView.findViewById(R.id.tvCamaItemListadoPacientes));

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ArrayDatosPacientes arrayDatosPacientes = getItem(position);
        viewHolder.getTVCedulaItemPacienteAdap().setText(arrayDatosPacientes.getCedulaPaciente());
        viewHolder.getTVNombreItemPacienteAdap().setText(arrayDatosPacientes.getNombrePaciente());
        viewHolder.getTVApellidoItemPacienteAdap().setText(arrayDatosPacientes.getApellidoPaciente());
        viewHolder.getTVEdadItemPacienteAdap().setText(String.valueOf(arrayDatosPacientes.getEdadPaciente()));
        viewHolder.getTVCamaItemPacienteAdap().setText(String.valueOf(arrayDatosPacientes.getCamaPaciente()));

        return convertView;
    }

    private class ViewHolder
    {

        public TextView tvCedulaItemPacienteAdap;
        public TextView tvNombreItemPacienteAdap;
        public TextView tvApellidoItemPacienteAdap;
        public TextView tvEdadItemPacienteAdap;
        public TextView tvCamaItemPacienteAdap;

        public void setTVCedulaItemPacienteAdap(TextView tvCedulaItemPacienteAdap)
        {
            this.tvCedulaItemPacienteAdap = tvCedulaItemPacienteAdap;
        }

        public void setTVNombreItemPacienteAdap(TextView tvNombreItemPacienteAdap)
        {
            this.tvNombreItemPacienteAdap = tvNombreItemPacienteAdap;
        }

        public void setTVApellidoItemPacienteAdap(TextView tvApellidoItemPacienteAdap)
        {
            this.tvApellidoItemPacienteAdap = tvApellidoItemPacienteAdap;
        }

        public void setTVEdadItemPacienteAdap(TextView tvEdadItemPacienteAdap)
        {
            this.tvEdadItemPacienteAdap = tvEdadItemPacienteAdap;
        }

        public void setTVCamaItemPacienteAdap(TextView tvCamaItemPacienteAdap)
        {
            this.tvCamaItemPacienteAdap = tvCamaItemPacienteAdap;
        }

        public TextView getTVCedulaItemPacienteAdap()
        {
            return tvCedulaItemPacienteAdap;
        }

        public TextView getTVNombreItemPacienteAdap()
        {
            return tvNombreItemPacienteAdap;
        }

        public TextView getTVApellidoItemPacienteAdap()
        {
            return tvApellidoItemPacienteAdap;
        }

        public TextView getTVEdadItemPacienteAdap()
        {
            return tvEdadItemPacienteAdap;
        }

        public TextView getTVCamaItemPacienteAdap()
        {
            return tvCamaItemPacienteAdap;
        }

    }

}
