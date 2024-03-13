package com.casper.sdk.model.clvalue;

import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeTuple3;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Casper Tuple3 CLValue implementation
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CLValueTuple3 extends
        AbstractCLValueWithChildren<Triplet<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>>, CLTypeTuple3> {
    @JsonProperty("cl_type")
    private CLTypeTuple3 clType = new CLTypeTuple3();

    @JsonSetter("cl_type")
    public void setClType(final CLTypeTuple3 clType) {
        this.clType = clType;
        childTypesSet();
    }

    public CLValueTuple3(final Triplet<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setChildTypes(value);
        this.setValue(value);
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {
        final SerializerBuffer serVal = new SerializerBuffer();
        setChildTypes(this.getValue());
        getValue().getValue0().serialize(serVal);
        getValue().getValue1().serialize(serVal);
        getValue().getValue2().serialize(serVal);
        final byte[] bytes = serVal.toByteArray();
        ser.writeByteArray(bytes);
        this.setBytes(Hex.toHexString(bytes));
    }

    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        final CLTypeData childTypeData1 = clType.getChildClTypeData(0);
        final CLTypeData childTypeData2 = clType.getChildClTypeData(1);
        final CLTypeData childTypeData3 = clType.getChildClTypeData(2);

        final AbstractCLValue<?, ?> child1 = CLTypeData.createCLValueFromCLTypeData(childTypeData1);
        if (child1.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child1.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildTypes().get(0)).getChildTypes());
        }
        child1.deserializeCustom(deser);

        final AbstractCLValue<?, ?> child2 = CLTypeData.createCLValueFromCLTypeData(childTypeData2);
        if (child2.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child2.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildTypes().get(1)).getChildTypes());
        }
        child2.deserializeCustom(deser);

        final AbstractCLValue<?, ?> child3 = CLTypeData.createCLValueFromCLTypeData(childTypeData3);
        if (child3.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child3.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildTypes().get(2)).getChildTypes());
        }
        child3.deserializeCustom(deser);

        setValue(new Triplet<>(child1, child2, child3));
    }

    @Override
    protected void setChildTypes(final Triplet<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) {
        if (value.getValue0() != null && value.getValue1() != null && value.getValue2() != null) {
            clType.setChildTypes(Arrays.asList(value.getValue0().getClType(), value.getValue1().getClType(),
                    value.getValue2().getClType()));
        } else {
            clType.setChildTypes(new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().toString() : null;
    }
}
