package com.telstock.tmanager.cemex.modulocitas.bus;

/**
 * Created by czamora on 10/15/16.
 */
import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public class BusProvider {

    //Se hace una instancia de Otto.
    private static final Bus _instance = new MainThreadBus();

    //Se devuelve la instancia creada.
    public static Bus getInstance() {
        return _instance;
    }

    private BusProvider() {
    }

    //Se maneja el hilo que mandará los mensajes a los suscriptores.
    private static class MainThreadBus extends Bus {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void post(final Object event) {
            //Si ya se está en el hilo principal, entonces hacer simplemente el post.
            if (Looper.myLooper() == Looper.getMainLooper()) {
                super.post(event);
            }
            else //De otro modo, envolver el post en el mainThreadHandler
            {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        post(event);
                    }
                });
            }
        }
    }
}