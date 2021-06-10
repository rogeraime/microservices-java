package com.adesso.digitalwash.tests;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import org.junit.Test;

public class MiscellaneousTests {
	@Test
	public void nextTuesday() throws Exception {
		TemporalAdjuster tNextTues = TemporalAdjusters.next(DayOfWeek.TUESDAY);
		LocalDate nextTue = LocalDate.now(ZoneId.of("UTC")).with(tNextTues);
		LocalDate tues2 = nextTue.with(tNextTues);
		assertTrue(nextTue.getDayOfWeek() == DayOfWeek.TUESDAY);
		assertTrue(tues2.getDayOfWeek() == DayOfWeek.TUESDAY);
	}
}
