package org.lizhao.base.utils.uniquekey;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.util.EnumSet;

import static org.hibernate.generator.EventTypeSets.INSERT_ONLY;

/**
 * description  雪花算法
 *  0b 最高位0 + 时间戳（41bit）+ 机器id(10bit) + 序列号（12bit） 共64bit 8个字节 long类型长度
 *
 * @author LIZHAO
 * @version V1.0
 * @date 2022/10/1 15:21
 */
@Getter
@Setter
public class SnowFlake implements BeforeExecutionGenerator {

    /**
     * 生成的唯一键的长度(bit)
     */
    private volatile short length = 64;
    /**
     * 时间戳长度
     */
    private volatile short timestampLength = 41;
    /**
     * 机器位最大长度
     */
    private volatile short workerIdLength = 10;
    /**
     * 序列位长度
     */
    private volatile short sequenceLength = 12;


    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        return null;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return INSERT_ONLY;
    }
}
