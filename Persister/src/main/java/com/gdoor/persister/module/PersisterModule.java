package com.gdoor.persister.module;

import com.gdoor.persister.elastic.PersisterImpl;
import com.google.inject.AbstractModule;

public class PersisterModule  extends AbstractModule {
    @Override
    protected void configure() {
        bind(Persister.class).to(PersisterImpl.class);
    }
}
