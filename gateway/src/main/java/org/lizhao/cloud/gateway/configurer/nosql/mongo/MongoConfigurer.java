package org.lizhao.cloud.gateway.configurer.nosql.mongo;

import org.lizhao.cloud.gateway.converter.nosql.mongo.BigDecimalToDecimal128Converter;
import org.lizhao.cloud.gateway.converter.nosql.mongo.Decimal128ToBigDecimalConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author LIZHAO
 * @version V1.0
 * @date 2022/9/17 16:04
 */
@Configuration
public class MongoConfigurer {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }

    /**
     * mongoCustomConversions会由spring进行管理,
     * 按照加入的转换器,在数据库读写时对数据类型进行转换
     * @return
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDatabaseFactory)  {
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver,new MongoMappingContext());
        List<Object> list = new ArrayList<>();
        //自定义的类型转换器
        list.add(new BigDecimalToDecimal128Converter());
        list.add(new Decimal128ToBigDecimalConverter());
        converter.setCustomConversions(new MongoCustomConversions(list));
        return converter;
    }


}
