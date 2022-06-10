package de.sonallux.example.graphql.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class EventEmitter<T> {
    private FluxSink<T> fluxSink = null;
    private Flux<T> cachedFlux = null;

    public Flux<T> getEvents() {
        if (fluxSink != null && !fluxSink.isCancelled()) {
            return cachedFlux;
        }
        return cachedFlux = Flux.<T>create(sink -> {
            fluxSink = sink;
        }).share();
    }

    public void emitEvent(T event) {
        if (fluxSink == null) {
            return;
        }
        if (fluxSink.isCancelled()) {
            fluxSink = null;
            cachedFlux = null;
            return;
        }
        fluxSink.next(event);
    }
}
