package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.CLTypeAny;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeMap;
import com.casper.sdk.model.clvalue.serde.Target;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

public class CLValueTests {

    @Test
    void allCLTypes_must_be_implemented() throws DynamicInstanceException, NoSuchTypeException {
        for (CLTypeData clTypeData : CLTypeData.values()) {
            // Ensure there are no types with missing implementation
            assertNotNull(clTypeData.getClazz());

            AbstractCLValue<?, ?> clValue = CLTypeData.createCLValueFromCLTypeData(clTypeData);

            // Correct instance type
            assertEquals(clTypeData.getClazz(), clValue.getClass());

            // Check if the correct CLType is set
            assertEquals(clTypeData, clValue.getClType().getClTypeData());
        }
    }

    @Test
    void getTypeBySerializationTag_should_return_correct_CLTypeData() throws NoSuchTypeException {
        for (CLTypeData typeData : CLTypeData.values()) {
            assertEquals(typeData, CLTypeData.getTypeBySerializationTag(typeData.getSerializationTag()));
        }
    }

    @Test
    void createCLTypeFromCLTypeData_should_return_correct_CLType() throws DynamicInstanceException {
        assertTrue(CLTypeData.createCLTypeFromCLTypeData(CLTypeData.ANY) instanceof CLTypeAny);
    }

    @Test
    void createCLValueFromCLTypeName_should_return_correct_CLValue() throws DynamicInstanceException, NoSuchTypeException {
        assertTrue(CLTypeData.createCLValueFromCLTypeName(CLTypeData.ANY.getClTypeName()) instanceof CLValueAny);
    }

    @Test
    void createCLValueFromCLTypeName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.createCLValueFromCLTypeName("NE"));
    }

    @Test
    void getClassByName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.getClassByName("NE"));
    }

    @Test
    void getCLTypeClassByName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.getCLTypeClassByName("NE"));
    }

    @Test
    void clValueAnyByteSerializationWithAndWithoutTypeInfo() throws Exception {

        final CLValueAny clValueAny = new CLValueAny(Hex.decode("d2029649"));

        SerializerBuffer ser = new SerializerBuffer();
        clValueAny.serialize(ser, Target.JSON);
        assertThat(ser.toByteArray(), is(Hex.decode("d2029649")));

        ser = new SerializerBuffer();
        clValueAny.serialize(ser, Target.BYTE);
        assertThat(ser.toByteArray(), is(Hex.decode("04000000d202964915")));
    }

    @Test
    void getTypeByName_from_CLTypeData_should_throw_NoSuchTypeException() {
        assertThrows(NoSuchTypeException.class, () -> CLTypeData.getTypeByName("NE"));
    }


    @Test
    void mapDeserialization() throws Exception {

        final CLValueString key = new CLValueString("ONE");
        final CLValueU32 value = new CLValueU32(2L);
        final Map<CLValueString, CLValueU32> innerMap = new HashMap<>();
        innerMap.put(key, value);
        CLValueMap clValueMap = new CLValueMap(innerMap);

        SerializerBuffer ser = new SerializerBuffer();
        clValueMap.serialize(ser, Target.JSON);
        byte[] bytes = ser.toByteArray();

        byte[] expected = Hex.decode("01000000030000004f4e4502000000");
        assertThat(bytes, is(expected));

        ser = new SerializerBuffer();
        clValueMap.serialize(ser, Target.BYTE);
        bytes = ser.toByteArray();
        expected = Hex.decode("0f00000001000000030000004f4e4502000000110a04");
        assertThat(bytes, is(expected));

        final CLValueMap deserialized = new CLValueMap();
        CLTypeMap clTypeMap = new CLTypeMap();
        clTypeMap.setKeyValueTypes(new CLTypeMap.CLTypeMapEntryType(key.getClType(), value.getClType()));
        deserialized.setClType(clTypeMap);
        deserialized.deserialize(new DeserializerBuffer("01000000030000004f4e4502000000"));
        String hexBytes = deserialized.getBytes();
        assertThat(hexBytes, is("01000000030000004f4e4502000000"));

        CLValueString keyDeser = (CLValueString) deserialized.getValue().keySet().iterator().next();

        assertThat(keyDeser, is(key));
        assertThat(keyDeser.getBytes(), is(key.getBytes()));

        CLValueU32 valueDeser = (CLValueU32) deserialized.getValue().get(key);
        assertThat(valueDeser, is(value));
        assertThat(valueDeser.getBytes(), is(value.getBytes()));
    }
}
