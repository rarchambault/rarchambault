package test.java.unitTests;

import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
    public static void writeHeader(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("testName,numObjects,durationMs,avgCpuPercent,jvmMemoryDelta,peakJvmUsedMemory,systemMemoryDelta\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writePerformanceResults(String filePath,
                                               String testName,
                                               int numObjects,
                                               long durationMs,
                                               double avgCpuPercent,
                                               long jvmMemoryDelta,
                                               long peakJvmUsedMemory,
                                               long systemMemoryDelta) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(String.format("%s,%d,%d,%.2f,%d,%d,%d\n",
                    testName,
                    numObjects,
                    durationMs,
                    avgCpuPercent,
                    jvmMemoryDelta,
                    peakJvmUsedMemory,
                    systemMemoryDelta
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}