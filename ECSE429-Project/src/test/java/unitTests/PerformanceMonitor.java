package test.java.unitTests;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PerformanceMonitor {

    public static PerformanceResult monitorDuring(Runnable task) {
        OperatingSystemMXBean osBean =
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // System memory before task
        long systemMemBefore = osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize();

        // JVM memory before
        long initialJvmUsedMemory = getJvmUsedMemory();

        List<Double> cpuSamples = new ArrayList<>();
        List<Long> jvmUsedMemorySamples = new ArrayList<>();
        AtomicBoolean running = new AtomicBoolean(true);

        Thread sampler = new Thread(() -> {
            while (running.get()) {
                double cpuLoad = osBean.getSystemCpuLoad();
                if (cpuLoad >= 0.0) {
                    cpuSamples.add(cpuLoad);
                }

                jvmUsedMemorySamples.add(getJvmUsedMemory());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        });

        sampler.start();
        long startTime = System.currentTimeMillis();

        // Run the task
        task.run();

        long endTime = System.currentTimeMillis();
        running.set(false);

        try {
            sampler.join();
        } catch (InterruptedException ignored) {}

        // JVM memory after
        long finalJvmUsedMemory = getJvmUsedMemory();

        // System memory after task
        long systemMemAfter = osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize();

        double avgCpu = cpuSamples.stream().mapToDouble(Double::doubleValue).average().orElse(0.0) * 100;
        long peakJvmUsedMemory = jvmUsedMemorySamples.stream().mapToLong(Long::longValue).max().orElse(0L);

        return new PerformanceResult(
                endTime - startTime,
                avgCpu,
                initialJvmUsedMemory,
                finalJvmUsedMemory,
                peakJvmUsedMemory,
                systemMemBefore,
                systemMemAfter
        );
    }

    private static long getJvmUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static class PerformanceResult {
        public final long durationMs;
        public final double averageCpuPercent;

        public final long jvmUsedMemoryBefore;
        public final long jvmUsedMemoryAfter;
        public final long peakJvmUsedMemory;

        public final long systemMemoryUsedBefore;
        public final long systemMemoryUsedAfter;

        public PerformanceResult(
                long durationMs,
                double averageCpuPercent,
                long jvmUsedMemoryBefore,
                long jvmUsedMemoryAfter,
                long peakJvmUsedMemory,
                long systemMemoryUsedBefore,
                long systemMemoryUsedAfter
        ) {
            this.durationMs = durationMs;
            this.averageCpuPercent = averageCpuPercent;
            this.jvmUsedMemoryBefore = jvmUsedMemoryBefore;
            this.jvmUsedMemoryAfter = jvmUsedMemoryAfter;
            this.peakJvmUsedMemory = peakJvmUsedMemory;
            this.systemMemoryUsedBefore = systemMemoryUsedBefore;
            this.systemMemoryUsedAfter = systemMemoryUsedAfter;
        }

        public long getJvmMemoryDelta() {
            return jvmUsedMemoryAfter - jvmUsedMemoryBefore;
        }

        public long getSystemMemoryDelta() {
            return systemMemoryUsedAfter - systemMemoryUsedBefore;
        }
    }
}