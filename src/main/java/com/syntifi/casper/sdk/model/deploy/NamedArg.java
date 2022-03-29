package com.syntifi.casper.sdk.model.deploy;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.*;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.interfaces.EncodableValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Named arguments to a contract
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class NamedArg<P extends AbstractCLType> implements EncodableValue {

    /**
     * The first value in the array is the type of the arg
     */
    private String type;

    /**
     * The second value in the array is a CLValue type
     */
    private AbstractCLValue<?, P> clValue;

    @Override
    public void encode(CLValueEncoder clve, boolean encodeType)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        clve.writeString(type);
        if (clValue instanceof CLValueI32 || clValue instanceof CLValueU32) {
            clve.writeInt(32 / 8);
        }
        if (clValue instanceof CLValueI64 || clValue instanceof CLValueU64) {
            clve.writeInt(64 / 8);
        }
        if (clValue instanceof CLValueU128 || clValue instanceof CLValueU256 ||
                clValue instanceof CLValueU512 || clValue instanceof CLValuePublicKey){
            CLValueEncoder localEncoder = new CLValueEncoder();
            clValue.encode(localEncoder, false);
            int size = localEncoder.toByteArray().length;
            clve.writeInt(size); //removing the CLValue type byte at the end
        }
        if (clValue instanceof CLValueOption) {
            CLValueEncoder localEncoder = new CLValueEncoder();
            clValue.encode(localEncoder, false);
            int size = localEncoder.toByteArray().length;
            clve.writeInt(size); //removing the CLValue type byte at the end
        }
        clValue.encode(clve, encodeType);
    }
}
