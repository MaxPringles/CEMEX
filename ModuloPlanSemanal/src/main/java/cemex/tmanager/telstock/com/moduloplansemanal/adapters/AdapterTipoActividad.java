package cemex.tmanager.telstock.com.moduloplansemanal.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import cemex.tmanager.telstock.com.moduloplansemanal.R;
import cemex.tmanager.telstock.com.moduloplansemanal.interfaces.OnClickProspectos;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;

/**
 * Created by usr_micro13 on 28/02/2017.
 */

public class AdapterTipoActividad extends RecyclerView.Adapter<AdapterTipoActividad.ViewHolder> {

    private Context context;
    private List<CatalogoActividadesPGVDB> catalogoActividadesPGVDBList;
    private static OnClickProspectos itemListener;

    public AdapterTipoActividad(Context context, List<CatalogoActividadesPGVDB> catalogoActividadesPGVDBList,
                                OnClickProspectos itemListener) {

        this.context = context;
        this.catalogoActividadesPGVDBList = catalogoActividadesPGVDBList;
        this.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_tipo_actividad,parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CatalogoActividadesPGVDB catalogoActividadesPGVDB = catalogoActividadesPGVDBList.get(position);

        holder.radioButton.setTag(R.id.radio_button, position);
        holder.radioButton.setText(catalogoActividadesPGVDB.getDescripcion());

        if (catalogoActividadesPGVDB.isChecked()) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return catalogoActividadesPGVDBList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private Context context;

        private RadioButton radioButton;

        public ViewHolder(View v, Context context) {
            super(v);

            this.context = context;

            radioButton = (RadioButton) v.findViewById(R.id.radio_button);

            radioButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.radio_button) {
                int position = (int) v.getTag(R.id.radio_button);
                itemListener.onClickProspectos(v, position);
            }
        }
    }
}
