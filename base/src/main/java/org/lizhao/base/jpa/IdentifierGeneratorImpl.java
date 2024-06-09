package org.lizhao.base.jpa;

import jakarta.annotation.PostConstruct;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.id.IdentifierGenerator;
import org.lizhao.base.utils.uniquekey.SnowFlake;

import java.util.LinkedList;
import java.util.List;

/**
 * description jpa id生成规则实现类
 *
 * @author LIZHAO
 * @version V1.0
 * @date 2022/10/1 16:10
 */
public class IdentifierGeneratorImpl implements IdentifierGenerator {

    public final static List<BeforeExecutionGenerator> GENERATORS = new LinkedList<>();

    @PostConstruct
    public void postConstruct() {
        GENERATORS.add(new SnowFlake(1L, 1L, 1L));
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

//        GenericGenerator genericGenerator = object.getClass().getDeclaredAnnotation(GenericGenerator.class);
//        Class<? extends Generator> strategy = genericGenerator.type();
        BeforeExecutionGenerator generator = GENERATORS.get(0);
        if (generator != null) {
            return String.valueOf(generator.generate(session, object, null, null));
        }

        return null;
    }
}

