package mx.com.tarjetasdelnoreste.realmdb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import mx.com.tarjetasdelnoreste.realmdb.model.User;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Crea la configuración de Realm.
        realmConfig = new RealmConfiguration.Builder(this).build();

        //Abre Realm para la UI thread.
        realm = Realm.getInstance(realmConfig);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadArray();
                loadAll();
            }
        });
    }

    public void basicCRUD() {

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                //Se añade una persona.
                User user = realm.createObject(User.class);
                user.setName("César");
                user.setApellidoMaterno("Zamora");
                user.setApellidoMaterno("Gutiérrez");
                user.setEdad(23);
            }
        });

        User user = realm.where(User.class).findFirst();
        Log.d("Usuario", user.getName());
    }

    public void loadArray() {

        ArrayList<User> arrayList = new ArrayList<>();
        arrayList.add(new User("Max", "Bautista", "Salinas", 24));
        arrayList.add(new User("Michel", "Torres", "Alonso", 25));
        arrayList.add(new User("Carlos", "Orta", "Vera", 26));

        realm.beginTransaction(); //Indica que iniciará una transacción.
        realm.delete(User.class); //Borra lo que hay en la tabla.
        realm.copyToRealm(arrayList); //Copia el array dentro de Realm.
        realm.commitTransaction(); //Finaliza la transacción.

        List<User> listaUsers = realm.where(User.class).findAll();
        Log.d("as", "as");
    }

    public void loadAll() {
        List<User> listaUsers = realm.where(User.class).findAll();
        for (int i = 0; i < listaUsers.size(); i++) {
            Log.d("USUARIO: ", listaUsers.get(i).getName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
