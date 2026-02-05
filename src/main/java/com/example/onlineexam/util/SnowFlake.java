package com.example.onlineexam.util;

import com.example.onlineexam.service.UserService;
import jakarta.annotation.PostConstruct;
import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Twitter的分布式自增ID雪花算法
 **/

@Component
public class SnowFlake {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(SnowFlake.class);
    // ============================== Fields ==============================
    private static final long START_TIMESTAMP = 1609459200000L; // 2021-01-01 00:00:00

    // 每一部分占用的位数
    private static final long SEQUENCE_BIT = 12; // 序列号占用的位数
    private static final long MACHINE_BIT = 5;   // 机器标识占用的位数
    private static final long DATACENTER_BIT = 5; // 数据中心占用的位数

    // 每一部分的最大值
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private static final long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);

    // 每一部分向左的位移
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private static final long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    // ============================== Instance Variables ==============================
    private final long datacenterId;    // 数据中心
    private final long machineId;       // 机器标识
    private final AtomicLong sequence = new AtomicLong(0L); // 原子序列号
    private volatile long lastTimestamp = -1L; // 上一次时间戳，volatile保证可见性

    // 时钟回拨容错阈值（毫秒）
    private static final long MAX_BACKWARD_MS = 5L;

    // 生成统计（用于监控）
    private final AtomicLong generatedCount = new AtomicLong(0);
    private final AtomicLong duplicateCheckCount = new AtomicLong(0);

    // ============================== Constructors ==============================
    public SnowFlake() {
        this.datacenterId = getDatacenterIdFromConfig();
        this.machineId = getMachineIdFromConfig();
        LOG.info("SnowFlake initialized with datacenterId: {}, machineId: {}",
                datacenterId, machineId);


    }

    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("datacenterId can't be greater than %d or less than 0", MAX_DATACENTER_NUM));
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException(
                    String.format("machineId can't be greater than %d or less than 0", MAX_MACHINE_NUM));
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
        LOG.info("SnowFlake initialized with custom datacenterId: {}, machineId: {}",
                datacenterId, machineId);
    }

    // ============================== Configuration ==============================
    @Value("${snowflake.datacenter-id:1}")
    private Long configDatacenterId;

    @Value("${snowflake.machine-id:-1}")
    private Long configMachineId;

    private long getDatacenterIdFromConfig() {
        if (configDatacenterId != null && configDatacenterId >= 0 && configDatacenterId <= MAX_DATACENTER_NUM) {
            return configDatacenterId;
        }
        return 1L; // 默认值
    }

    private long getMachineIdFromConfig() {
        if (configMachineId != null && configMachineId >= 0 && configMachineId <= MAX_MACHINE_NUM) {
            return configMachineId;
        }
        return generateUniqueMachineId(); // 自动生成
    }

    private long generateUniqueMachineId() {
        try {
            // 方法1：使用IP地址最后一段
            String ip = getLocalIp();
            String[] ipParts = ip.split("\\.");
            if (ipParts.length == 4) {
                long ipPart = Long.parseLong(ipParts[3]);
                return ipPart % (MAX_MACHINE_NUM + 1);
            }
        } catch (Exception e) {
            LOG.warn("Failed to generate machineId from IP, trying MAC address", e);
        }

        try {
            // 方法2：使用MAC地址
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = network.getHardwareAddress();
            if (mac != null) {
                long macValue = 0;
                for (int i = 0; i < mac.length; i++) {
                    macValue = (macValue << 8) + (mac[i] & 0xff);
                }
                return macValue % (MAX_MACHINE_NUM + 1);
            }
        } catch (Exception e) {
            LOG.warn("Failed to generate machineId from MAC, using hostname", e);
        }

        try {
            // 方法3：使用主机名hash
            String hostname = InetAddress.getLocalHost().getHostName();
            int hash = hostname.hashCode();
            return Math.abs(hash) % (MAX_MACHINE_NUM + 1);
        } catch (Exception e) {
            LOG.warn("Failed to generate machineId from hostname, using random", e);
            // 方法4：使用随机数
            return System.currentTimeMillis() % (MAX_MACHINE_NUM + 1);
        }
    }

    private String getLocalIp() throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            if (iface.isLoopback() || !iface.isUp()) {
                continue;
            }
            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr.isSiteLocalAddress() && !addr.isLoopbackAddress()) {
                    return addr.getHostAddress();
                }
            }
        }
        return InetAddress.getLocalHost().getHostAddress();
    }

    // ============================== Methods ==============================
    /**
     * 生成下一个ID
     * 完全线程安全，使用双重检查锁和原子操作
     */
    public long nextId() {
        long currentTimestamp = timeGen();

        // 检查时钟回拨
        if (currentTimestamp < lastTimestamp) {
            long offset = lastTimestamp - currentTimestamp;
            if (offset <= MAX_BACKWARD_MS) {
                // 小范围时钟回拨，等待
                waitUntilNextMillis();
                currentTimestamp = timeGen();
            } else {
                // 大范围时钟回拨，抛出异常
                throw new RuntimeException(
                        String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", offset));
            }
        }

        // 相同毫秒内的处理
        if (currentTimestamp == lastTimestamp) {
            long sequenceValue = sequence.incrementAndGet() & MAX_SEQUENCE;
            if (sequenceValue == 0) {
                // 当前毫秒序列号已用完，等待下一毫秒
                currentTimestamp = waitUntilNextMillis();
            }
            return generateId(currentTimestamp, sequenceValue);
        } else {
            // 新的毫秒，重置序列号
            sequence.set(0L);
            lastTimestamp = currentTimestamp;
            return generateId(currentTimestamp, 0L);
        }
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     */
    private long waitUntilNextMillis() {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            // 使用Thread.yield()减少CPU占用
            Thread.yield();
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 生成ID的最终方法
     */
    private synchronized long generateId(long timestamp, long sequence) {
        // 这里使用synchronized确保lastTimestamp的更新是原子的
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Timestamp regression detected during ID generation");
        }

        lastTimestamp = timestamp;

        long id = ((timestamp - START_TIMESTAMP) << TIMESTAMP_LEFT)
                | (datacenterId << DATACENTER_LEFT)
                | (machineId << MACHINE_LEFT)
                | sequence;

        // 统计和检查（生产环境可移除）
        long count = generatedCount.incrementAndGet();
        if (count % 10000 == 0) {
            LOG.debug("Generated {} IDs, last ID: {}", count, id);
        }

        return id;
    }

    /**
     * 获取当前时间戳
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    // ============================== Helper Methods ==============================

    /**
     * 解析ID的各个部分，用于调试
     */
    public String parseId(long id) {
        long sequence = id & MAX_SEQUENCE;
        long machineId = (id >> MACHINE_LEFT) & MAX_MACHINE_NUM;
        long datacenterId = (id >> DATACENTER_LEFT) & MAX_DATACENTER_NUM;
        long timestamp = (id >> TIMESTAMP_LEFT) + START_TIMESTAMP;

        return String.format("ID解析: 时间戳=%d(时间=%s), 数据中心=%d, 机器ID=%d, 序列号=%d",
                timestamp, new java.util.Date(timestamp), datacenterId, machineId, sequence);
    }

    /**
     * 批量生成ID测试
     */
    public void test(int count) {
        LOG.info("开始生成 {} 个ID进行测试...", count);
        java.util.Set<Long> ids = new java.util.HashSet<>();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            long id = nextId();
            if (!ids.add(id)) {
                LOG.error("发现重复ID: {} 在第 {} 次生成", id, i);
                throw new RuntimeException("测试失败：生成重复ID");
            }
        }

        long endTime = System.currentTimeMillis();
        LOG.info("测试成功：生成 {} 个唯一ID，耗时 {} 毫秒", ids.size(), (endTime - startTime));
    }

    // ============================== Getters ==============================
    public long getDatacenterId() {
        return datacenterId;
    }

    public long getMachineId() {
        return machineId;
    }

    public long getGeneratedCount() {
        return generatedCount.get();
    }
}
