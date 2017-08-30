package com.telstock.tmanager.cemex.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.squareup.picasso.Picasso;
import com.telstock.tmanager.cemex.R;
import com.telstock.tmanager.cemex.interfaces.NavigationOnClick;
import com.telstock.tmanager.cemex.model.NavigationFilas;

import java.util.ArrayList;

import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;

/**
 * Created by czamora on 8/9/16.
 */
public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    private Context context;
    //private static NavigationOnClick interfaceOnClick;

    private ArrayList<NavigationFilas> arrayNavigationFilas = new ArrayList<>();
    private String nv_header_nombre;
    private String nv_header_imagen;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    /** CONSTRUCTOR DE LA CLASE **/
    public NavigationAdapter(Context context, //NavigationOnClick interfaceOnClick,
                             ArrayList<NavigationFilas> arrayNavigationFilas,
                             String nv_header_nombre, String nv_header_imagen) {
        this.context = context;
        //this.interfaceOnClick = interfaceOnClick;
        this.arrayNavigationFilas = arrayNavigationFilas;
        this.nv_header_nombre = nv_header_nombre;
        this.nv_header_imagen = nv_header_imagen;
    }

    /** MÉTODO QUE DEVUELVE LA VISTA A MOSTRAR, DEPENDIENDO SI
        ES CABECERA O ÍTEM **/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        if (viewType == TYPE_HEADER) {
            v = LayoutInflater.from(parent.getContext())////////////////////////
                    .inflate(R.layout.nav_header_main, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.nav_custom_row_main, parent, false);
        }

        return new ViewHolder(v, viewType);
    }

    /** MÉTODO QUE MANEJA LOS ELEMENTOS DE CADA COLUMNA (EL
        CONTENIDO QUE TENDRÁN) **/
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder.holderId == 0) { //Elementos de la cabecera.
            holder.txt_nombre_usuario.setText(nv_header_nombre);

            Picasso.with(context)
                    .load(Url.URL_WEBSERVICE + Url.getArchivoProspecto + nv_header_imagen)
                    .placeholder(R.drawable.list_avatar)
                    .error(R.drawable.list_avatar)
                    .into(holder.img_imagen_usuario);
            /*try {
                //Se convierte la imagen en Base64 a array de bytes.
                byte[] imageByteArray = Base64.decode(nv_header_imagen, Base64.DEFAULT);
                //Se coloca la imagen en bytes dentro del ImageView.
                Glide.with(context)
                        .load(imageByteArray)
                        .asBitmap()
                        .placeholder(R.drawable.list_avatar)
                        .error(R.drawable.list_avatar)
                        .fitCenter()
                        .into(holder.img_imagen_usuario);
            } catch (Exception e) {
                Log.e("NavigationAdapterERROR", e.toString());
            }*/

        } else { //Elementos de las filas normales.

            //Recuperamos la posición -1, para tomar sólo la longitud del arraylist.
            final NavigationFilas navigationFilas = arrayNavigationFilas.get(position - 1);

            //Se colocan los tags para identificar los elementos de cada fila.
            holder.linear_layout_lista.setTag(R.id.linear_layout_lista, navigationFilas);

            holder.img_imagen_item.setImageResource(navigationFilas.getImagenFila());
            holder.txt_nombre_item.setText(navigationFilas.getNombreFila());
            //En caso de que el ítem de notificación sea 0, entonces no se muestra el círculo
            //de notificación.
            if (navigationFilas.getNotificacionFila() != 0) {
                holder.txt_notificacion_item.setText(Integer.toString(navigationFilas.getNotificacionFila()));
            } else {
                holder.txt_notificacion_item.setVisibility(View.INVISIBLE);
            }
        }

    }

    /** MÉTODO QUE INDICA EL TIPO DE VISTA DEL RECYCLERVIEW **/
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    /** MÉTODO QUE DEFINE SI LA VISTA ES CABECERA O ÍTEM **/
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    /** MÉTODO QUE DEVUELVE EL TAMAÑO TOTAL DEL RECYCLERVIEW, SE PONE
        +1 PARA CONSIDERAR LA CABECERA **/
    @Override
    public int getItemCount() {
        return arrayNavigationFilas.size() + 1;
    }

    /** CLASE QUE ENLAZA LOS OBJETOS CON LOS ELEMENTOS UI **/
    public static class ViewHolder extends RecyclerView.ViewHolder {
            //implements View.OnClickListener {

        private int holderId;

        private TextView txt_nombre_usuario;
        private ImageView img_imagen_usuario;

        private LinearLayout linear_layout_lista;
        private ImageView img_imagen_item;
        private TextView txt_nombre_item;
        private TextView txt_notificacion_item;

        public ViewHolder(View v, int viewType) {
            super(v);

            if (viewType == TYPE_HEADER) { //Muestra la cabecera del RecyclerView.
                holderId = 0;
                txt_nombre_usuario = (TextView) v.findViewById(R.id.txt_nombre_usuario);
                img_imagen_usuario = (ImageView) v.findViewById(R.id.img_imagen_usuario);
            } else { //Muestra la fila customizada del RecyclerView.
                holderId = 1;
                linear_layout_lista = (LinearLayout) v.findViewById(R.id.linear_layout_lista);
                img_imagen_item = (ImageView) v.findViewById(R.id.img_imagen_item);
                txt_nombre_item = (TextView) v.findViewById(R.id.txt_nombre_item);
                txt_notificacion_item = (TextView) v.findViewById(R.id.txt_notificacion_item);

                //linear_layout_lista.setOnClickListener(this);
            }
        }

        /*@Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.linear_layout_lista:
                    NavigationFilas navigationFilas = (NavigationFilas) v.getTag(R.id.linear_layout_lista);
                    interfaceOnClick.onClick(v, navigationFilas);
                    break;
            }
        }*/
    }
}