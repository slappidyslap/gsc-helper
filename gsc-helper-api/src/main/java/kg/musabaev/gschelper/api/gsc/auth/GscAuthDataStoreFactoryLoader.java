package kg.musabaev.gschelper.api.gsc.auth;

import com.google.api.client.util.store.DataStoreFactory;

public interface GscAuthDataStoreFactoryLoader {

    DataStoreFactory load();
}
