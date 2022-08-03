package com.casper.sdk.jackson.deserializer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.casper.sdk.exception.DeserializationException;
import com.casper.sdk.exception.NoSuchKeyTagException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.casper.sdk.exception.InvalidByteStringException;
import com.casper.sdk.model.clvalue.encdec.StringByteHelper;
import com.casper.sdk.model.key.AbstractSerializedKeyTaggedHex;
import com.casper.sdk.model.key.Tag;

/**
 * Customize the mapping of Casper's Hex String preceded by the crypto
 * algorithm tag such as PublicKey/Signature
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public abstract class AbstractSerializedKeyTaggedHexDeserializer<T extends AbstractSerializedKeyTaggedHex<S>, S extends Tag>
        extends JsonDeserializer<T> {

    /** Missing PublicKey in Events is shown as "00" */
    private static final String NULL_PUBLIC_KEY = "00";

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        if (NULL_PUBLIC_KEY.equals(node.textValue())) {
            return null;
        }

        T object = this.getInstanceOf();

        try {
            byte[] bytes = StringByteHelper.hexStringToByteArray(node.asText());
            this.loadKey(object, bytes);
        } catch (NoSuchAlgorithmException | NoSuchKeyTagException | InvalidByteStringException e) {
            throw new DeserializationException("Problem deserializing Algorithm tagged hex string", e);
        }

        return object;
    }

    protected abstract T getInstanceOf();

    protected abstract void loadKey(T key, byte[] bytes) throws NoSuchAlgorithmException, NoSuchKeyTagException;
}
