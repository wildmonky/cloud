package org.lizhao.cloud.gateway.converter.nosql.mongo;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.bson.types.Decimal128;

import java.math.BigDecimal;

/**
 * description
 *
 * @author LIZHAO
 * @version V1.0
 * @date 2022/9/17 16:58
 */
public class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {

    @Override
    public BigDecimal convert(Decimal128 value) {
        return null;
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}
