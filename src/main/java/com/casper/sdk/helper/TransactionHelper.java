package com.casper.sdk.helper;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.transaction.*;
import com.casper.sdk.model.transaction.entrypoint.TransactionEntryPoint;
import com.casper.sdk.model.transaction.pricing.PricingMode;
import com.casper.sdk.model.transaction.scheduling.TransactionScheduling;
import com.casper.sdk.model.transaction.target.TransactionTarget;
import com.syntifi.crypto.key.hash.Blake2b;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;

import java.util.Date;
import java.util.List;

/**
 * Utility class to help with the building of transactions.
 *
 * @author ian@meywood.com
 */
public class TransactionHelper {

    public static TransactionV1 buildTransaction(final InitiatorAddr<?> initiatorAddr,
                                                 final Ttl ttl,
                                                 final String chainName,
                                                 final PricingMode pricingMode,
                                                 final TransactionV1Body body) throws NoSuchTypeException, ValueSerializationException {

        final SerializerBuffer serializerBuffer = new SerializerBuffer();
        body.serialize(serializerBuffer, Target.BYTE);

        final Digest bodyHash = Digest.digestFromBytes(Blake2b.digest(serializerBuffer.toByteArray(), 32));

        return TransactionV1.builder()
                .header(buildTransactionHeader(initiatorAddr, new Date(), ttl, chainName, pricingMode, bodyHash))
                .body(body)
                .build();
    }

    private static TransactionV1Body buildTransactionBody(final List<NamedArg<?>> args,
                                                          final TransactionTarget target,
                                                          final TransactionEntryPoint entryPoint,
                                                          final TransactionCategory category,
                                                          final TransactionScheduling scheduling) {
        return TransactionV1Body.builder()
                .args(args)
                .target(target)
                .entryPoint(entryPoint)
                .transactionCategory(category)
                .scheduling(scheduling)
                .build();
    }

    private static TransactionV1Header buildTransactionHeader(@SuppressWarnings("rawtypes") final InitiatorAddr initiatorAddr,
                                                              final Date timestamp,
                                                              final Ttl ttl,
                                                              final String chainName,
                                                              final PricingMode pricingMode,
                                                              final Digest bodyHash) {
        return TransactionV1Header.builder()
                .initiatorAddr(initiatorAddr)
                .timestamp(timestamp)
                .ttl(ttl)
                .chainName(chainName)
                .pricingMode(pricingMode)
                .bodyHash(bodyHash)
                .build();
    }

}
