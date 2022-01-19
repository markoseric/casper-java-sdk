package com.syntifi.casper.sdk.model.deploy;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Root class for a Casper deploy request
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class DeployData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * the {@link Deploy}
     */
    @JsonProperty("deploy")
    private Deploy deploy;

    /**
     * a list of {@link JsonExecutionResult}
     */
    @JsonProperty("execution_results")
    private List<JsonExecutionResult> executionResults;
}
