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
    private long length = 64;
    /**
     * 时间戳长度
     */
    private long timestampLength = 41;
    /**
     * 机器位最大长度 datacenterId(5) + workId(5)
     */
    private long datacenterIdLength = 5;
    private long workerIdLength = 5;
    /**
     * 序列位长度
     */
    private long sequenceLength = 12;
    /**
     * 机器Id左移位数
     */
    private long workIdLShift = sequenceLength;
    /**
     * 数据中心Id左移位数
     */
    private long datacenterIdLShift = sequenceLength + workerIdLength;
    /**
     * 时间戳左偏移位数
     */
    private long timestampLShift = sequenceLength + workerIdLength + datacenterIdLength;

    private long maxDatacenterId = ~(-1L << datacenterIdLShift);
    private long maxWorkId = ~(-1L << workIdLShift);
    private long maxSequence = ~(-1L << sequenceLength);

    private long datacenterId, workId, sequence;

    private final long startTimestamp = 1711364268130L;

    /**
     * 为了在启动时重置
     */
    private long lastTimestamp = -1L;

    public SnowFlake(long datacenterId, long workId, long sequence) {
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId范围为[0~" + maxDatacenterId + "]");
        }
        if (workId > maxWorkId || workId < 0) {
            throw new IllegalArgumentException("workId范围为[0~" + maxWorkId + "]");
        }
        if (sequence > maxSequence || sequence < 0) {
            throw new IllegalArgumentException("sequence范围为[0~" + maxSequence + "]");
        }
        this.datacenterId = datacenterId;
        this.workId = workId;
        this.sequence = sequence;
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        return generateNextId();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return INSERT_ONLY;
    }

    /**
     * 生成下一个key
     * @return
     */
    public synchronized long generateNextId() {
        long currentTimestamp = currentTimestamp(lastTimestamp);

        // 并发下可能是同一毫秒，添加序列号sequence
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                // sequence超过最大值，按位‘与’后为0，当前时间戳序列已满，下一个时间戳
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                currentTimestamp = currentTimestamp(lastTimestamp);
            }
        }else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - startTimestamp) << timestampLShift) |
                (datacenterId << datacenterIdLShift) |
                (workId << workIdLShift) |
                sequence;
    }

    private long currentTimestamp(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("新的时间戳在旧时间戳之前");
        }
        return timestamp;
    }

}
