package com.casper.sdk.service.impl.event;

import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventType;
import lombok.*;

import java.util.Objects;
import java.util.Optional;

/**
 * The abstract base class implementation for all events
 *
 * @param <T> the type of the event data
 * @author ian@meywood.com
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(of = {"source", "id"})
abstract class AbstractEvent<T> implements Event<T> {

    /** The type of event RAW or POJO */
    private final EventType eventType;
    /** The source node of the event */
    private final String source;
    /** The ID of the event */
    private final Long id;
    /** The event data */
    private final T data;

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }
}