package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;
import com.techyourchance.unittestingfundamentals.exercise2.StringDuplicator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setup() {
        SUT = new IntervalsAdjacencyDetector();
    }

    @Test
    public void isAdjacent_doesOverLap_returnFalse  () {
        Interval start = new Interval(1,2);
        Interval end = new Interval(3,4);
        boolean isAdjacent = SUT.isAdjacent(start,end);
        Assert.assertThat(isAdjacent, is(false));
    }

    @Test
    public void isAdjacent_doesNotOverLap_returnTrue() {
        Interval start = new Interval(1,2);
        Interval end = new Interval(2,3);
        boolean isAdjacent = SUT.isAdjacent(start,end);
        Assert.assertThat(isAdjacent, is(true));
    }

    @Test
    public void isAdjacent_sameStartInsideStartInterval_returnTrue() {
        Interval start = new Interval(1,5);
        Interval end = new Interval(1,2);
        boolean isAdjacent = SUT.isAdjacent(start,end);
        Assert.assertThat(isAdjacent, is(false));
    }

    @Test
    public void isAdjacent_sameStartInsideEndInterval_returnTrue() {
        Interval start = new Interval(1,5);
        Interval end = new Interval(0,6);
        boolean isAdjacent = SUT.isAdjacent(start,end);
        Assert.assertThat(isAdjacent, is(false));
    }

    @Test
    public void isAdjacent_isSame_returnTrue() {
        Interval start = new Interval(1,2);
        Interval end = new Interval(1,2);
        boolean isAdjacent = SUT.isAdjacent(start,end);
        Assert.assertThat(isAdjacent, is(true));
    }

}