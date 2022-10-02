package org.lizhao.database.jpa;

import org.hibernate.HibernateException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.lizhao.base.utils.uniquekey.SnowFlake;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * description jpa id生成规则实现类
 *
 * @author LIZHAO
 * @version V1.0
 * @date 2022/10/1 16:10
 */
public class IdentifierGeneratorImpl implements IdentifierGenerator {

    public final static Map<String, Supplier<Serializable>> GENERATORS = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        GENERATORS.put("org.lizhao.base.utils.uniquekey.SnowFlake", SnowFlake::generate);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        GenericGenerator genericGenerator = object.getClass().getDeclaredAnnotation(GenericGenerator.class);

        String strategy = genericGenerator.strategy();
        Supplier<Serializable> generator = GENERATORS.get(strategy);
        if (generator != null) {
            return generator.get();
        }

        return null;
    }
}

