package com.syntifi.casper.sdk.demo.quarkus.api;

import java.net.MalformedURLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.syntifi.casper.sdk.model.block.CasperBlockData;
import com.syntifi.casper.sdk.service.CasperService;

@Path("/block")
public class BlockResource {
    private static String testIP = "144.91.79.58";
    private static int testPort = 7777;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CasperBlockData getLastBlock() throws MalformedURLException {
        CasperService service = CasperService.usingPeer(testIP, testPort);

        return service.getBlock();
    }
}