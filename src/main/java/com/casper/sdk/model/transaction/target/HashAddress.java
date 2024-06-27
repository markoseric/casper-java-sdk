package com.casper.sdk.model.transaction.target;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * An alias for Keys hash variant.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HashAddress {
    @JsonValue
    private boolean[] hashAddress;
}
