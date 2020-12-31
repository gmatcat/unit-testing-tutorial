package com.techyourchance.unittestingfundamentals.exercise2;

import com.techyourchance.unittestingfundamentals.exercise1.NegativeNumberValidator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringDuplicatorTest {

    StringDuplicator SUT;

    @Before
    public void setup() {
        SUT = new StringDuplicator();
    }

    @Test
    public void test1() {
        String result = SUT.duplicate("asd");
        Assert.assertThat(result, is("asdasd"));
    }

    @Test
    public void duplicate_emptyString_emptyStringReturned() throws Exception {
        String result = SUT.duplicate("");
        assertThat(result, is(""));
    }


    @Test
    public void test2() {
        String result = SUT.duplicate("pwoe");
        Assert.assertThat(result, is("pwoepwoe"));
    }


}