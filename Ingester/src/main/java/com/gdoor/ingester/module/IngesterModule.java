package com.gdoor.ingester.module;

import com.gdoor.ingester.elastic.IngesterImpl;
import com.google.inject.AbstractModule;

public class IngesterModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Ingester.class).to(IngesterImpl.class);
    }
}
