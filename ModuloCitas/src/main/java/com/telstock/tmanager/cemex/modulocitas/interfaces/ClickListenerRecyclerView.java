package com.telstock.tmanager.cemex.modulocitas.interfaces;

import android.view.View;

/**
 * Created by czamora on 11/29/16.
 */

public interface ClickListenerRecyclerView {

    //MÉTODOS DE MANEJADOR CON GESTUREDETECTOR.
    void onClick(View view, int position);
    void onLongClick(View view, int position);

}
