package com.object0r.toortools.helpers;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class BenchmarkHelper
{
    HashMap<String, BenchmarkEntry> benchmarks = new HashMap<String, BenchmarkEntry>();

    private class BenchmarkEntry
    {
        Date start;
        Date stop;

        public Date getStart()
        {
            return start;
        }

        public void setStart(Date start)
        {
            this.start = start;
        }

        public Date getStop()
        {
            return stop;
        }

        public void setStop(Date stop)
        {
            this.stop = stop;
        }

        public BenchmarkEntry()
        {
            setStart(new Date());
        }
    }

    public void markStart(String benchmarkName)
    {
        BenchmarkEntry benchmarkEntry = new BenchmarkEntry();
        benchmarks.put(benchmarkName, benchmarkEntry);
    }

    public void markStop(String benchmarkName) throws Exception
    {
        BenchmarkEntry benchmarkEntry = findBenchmark(benchmarkName);
        if (benchmarkEntry == null)
        {
            throw new Exception("Benchmark with name: " + benchmarkName + " doesn't exit.");
        }
        benchmarkEntry.setStop(new Date());
    }

    private BenchmarkEntry findBenchmark(String benchmarkName)
    {
        return benchmarks.get(benchmarkName);
    }

    /**
     * Returns the benchmark time difference between start and stop, in ms.
     *
     * @param benchmarkName
     * @return
     * @throws Exception
     */
    public long getBenchmarkTimeInMs(String benchmarkName) throws Exception
    {
        BenchmarkEntry benchmarkEntry = findBenchmark(benchmarkName);
        if (benchmarkEntry == null)
        {
            throw new Exception("Benchmark with name: " + benchmarkName + " doesn't exit.");
        }
        if (benchmarkEntry.getStart() == null)
        {
            throw new Exception("Benchmark exists but doesn't not have a start time.");
        }
        if (benchmarkEntry.getStop() == null)
        {
            throw new Exception("Benchmark exists but doesn't not have a stop time.");
        }
        return DateHelper.getDateDiff(benchmarkEntry.getStart(), benchmarkEntry.getStop(), TimeUnit.MILLISECONDS);
    }

    /**
     * Returns the benchmark duration, in human readable text.
     *
     * @param benchmarkName
     * @return
     * @throws Exception
     */
    public String getBenchmarkTimeHumanReadable(String benchmarkName) throws Exception
    {
        long diff = getBenchmarkTimeInMs(benchmarkName);
        return DateHelper.getMsDurationInHumanReadable(diff);
    }

    /**
     * Returns the benchmark duration in specified TimeUnit.
     *
     * @param benchmarkName
     * @param timeUnit      TimeUnit to return the
     * @return
     * @throws Exception
     */
    public long getBenchmarkTime(String benchmarkName, TimeUnit timeUnit) throws Exception
    {
        BenchmarkEntry benchmarkEntry = findBenchmark(benchmarkName);
        if (benchmarkEntry == null)
        {
            throw new Exception("Benchmark with name: " + benchmarkName + " doesn't exit.");
        }
        if (benchmarkEntry.getStart() == null)
        {
            throw new Exception("Benchmark exists but doesn not have a start time.");
        }
        if (benchmarkEntry.getStop() == null)
        {
            throw new Exception("Benchmark exists but doesn not have a stop time.");
        }
        return DateHelper.getDateDiff(benchmarkEntry.getStart(), benchmarkEntry.getStop(), TimeUnit.MILLISECONDS);
    }
}
