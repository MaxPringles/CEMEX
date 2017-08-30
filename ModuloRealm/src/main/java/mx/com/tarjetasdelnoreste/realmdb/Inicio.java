package mx.com.tarjetasdelnoreste.realmdb;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import mx.com.tarjetasdelnoreste.realmdb.modules.AllModules;

/**
 * Created by USRMICRO10 on 20/09/2016.
 */
public class Inicio extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this) // Beware this is the app context
                .name("CEMEX.realm")                    // So always use a unique name
                .modules(new AllModules())           // Always use explicit modules in library projects
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
