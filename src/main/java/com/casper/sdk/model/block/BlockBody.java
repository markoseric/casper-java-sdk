package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ian@meywood.com
 */
@Getter
@Setter
public abstract class BlockBody {

    /** @see PublicKey */
    @JsonProperty("proposer")
    private PublicKey proposer;

    /** The body's hash. */
    @JsonProperty("hash")
    private Digest hash;
}

