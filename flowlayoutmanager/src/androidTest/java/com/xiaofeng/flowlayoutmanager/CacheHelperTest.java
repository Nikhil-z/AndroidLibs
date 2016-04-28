package com.xiaofeng.flowlayoutmanager;

import android.graphics.Point;
import android.test.InstrumentationTestCase;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by xhan on 4/27/16.
 */
public class CacheHelperTest extends InstrumentationTestCase {
	@Test
	public void testLineCountsNoLimit() throws Exception {
		FlowLayoutOptions layoutOptions = new FlowLayoutOptions();
		layoutOptions.itemsPerLine = FlowLayoutOptions.ITEM_PER_LINE_NO_LIMIT;
		int contentAreaWidth = 10;
		CacheHelper cacheHelper = new CacheHelper(layoutOptions, contentAreaWidth);
		Point[] items = new Point[] {
				new Point(5, 1), new Point(2, 1)
				, new Point(4, 1), new Point(4, 1), new Point(2, 1)
				, new Point(9, 1)
				, new Point(2, 1), new Point(3, 2)};
		cacheHelper.addToCache(0, items);
		int[] lineCounts = cacheHelper.getLineCounts();
		assertThat("lineCounts should have 4 items", lineCounts.length, is(4));
		assertThat("line 0 have 2 items", lineCounts[0], is(2));
		assertThat("line 1 have 3 items", lineCounts[1], is(3));
		assertThat("line 2 have 1 items", lineCounts[2], is(1));
		assertThat("line 3 have 2 items", lineCounts[3], is(2));
	}

	@Test
	public void testLineCountsWithLimit() throws Exception {
		FlowLayoutOptions layoutOptions = new FlowLayoutOptions();
		layoutOptions.itemsPerLine = 2;
		int contentAreaWidth = 10;
		CacheHelper cacheHelper = new CacheHelper(layoutOptions, contentAreaWidth);
		Point[] items = new Point[] {
				new Point(5, 1), new Point(2, 1)
				, new Point(4, 1), new Point(4, 1)
				, new Point(2, 1)
				, new Point(9, 1)
				, new Point(2, 1), new Point(3, 2)};
		cacheHelper.addToCache(0, items);
		int[] lineCounts = cacheHelper.getLineCounts();
		assertThat("lineCounts should have 5 items", lineCounts.length, is(5));
		assertThat("line 0 have 2 items", lineCounts[0], is(2));
		assertThat("line 1 have 2 items", lineCounts[1], is(2));
		assertThat("line 2 have 1 items", lineCounts[2], is(1));
		assertThat("line 3 have 1 items", lineCounts[3], is(1));
		assertThat("line 4 have 2 items", lineCounts[4], is(2));
	}

	public void testItemLineIndex() {
		FlowLayoutOptions layoutOptions = new FlowLayoutOptions();
		layoutOptions.itemsPerLine = 2;
		int contentAreaWidth = 10;
		CacheHelper cacheHelper = new CacheHelper(layoutOptions, contentAreaWidth);
		Point[] items = new Point[] {
				new Point(5, 1), new Point(2, 1)
				, new Point(4, 1), new Point(4, 1)
				, new Point(2, 1)
				, new Point(9, 1)
				, new Point(2, 1), new Point(3, 2)};
		cacheHelper.addToCache(0, items);

		assertThat("1st item have line index 0", cacheHelper.itemLineIndex(0), is(0));
		assertThat("3rd item have line index 1", cacheHelper.itemLineIndex(2), is(1));
		assertThat("7th item have line index 4", cacheHelper.itemLineIndex(6), is(4));
		assertThat("8th item have line index 4", cacheHelper.itemLineIndex(7), is(4));
		assertThat("9th item have line index -1", cacheHelper.itemLineIndex(8), is(CacheHelper.NOT_FOUND));
	}

	public void testHavePreviousLineCached() {
		FlowLayoutOptions layoutOptions = new FlowLayoutOptions();
		layoutOptions.itemsPerLine = 2;
		int contentAreaWidth = 10;
		CacheHelper cacheHelper = new CacheHelper(layoutOptions, contentAreaWidth);
		Point[] items = new Point[] {
				new Point(5, 1), new Point(2, 1)
				, new Point(4, 1), new Point(4, 1)
				, new Point(2, 1)
				, new Point(9, 1)
				, new Point(2, 1), new Point(3, 2)};
		cacheHelper.addToCache(0, items);

		assertThat("1st item do not have previous line cached", cacheHelper.havePreviousLineCached(0), is(false));
		assertThat("3rd item have previous line cached", cacheHelper.havePreviousLineCached(2), is(true));
		assertThat("8th item have previous line cached", cacheHelper.havePreviousLineCached(7), is(true));
		assertThat("9th item do not have previous line cached", cacheHelper.havePreviousLineCached(9), is(false));
	}

	public void testHaveNextLineCached() {
		FlowLayoutOptions layoutOptions = new FlowLayoutOptions();
		layoutOptions.itemsPerLine = 2;
		int contentAreaWidth = 10;
		CacheHelper cacheHelper = new CacheHelper(layoutOptions, contentAreaWidth);
		Point[] items = new Point[] {
				new Point(5, 1), new Point(2, 1)
				, new Point(4, 1), new Point(4, 1)
				, new Point(2, 1)
				, new Point(9, 1)
				, new Point(2, 1), new Point(3, 2)};
		cacheHelper.addToCache(0, items);
		assertThat("1st item have next line cached", cacheHelper.haveNextLineCached(0), is(true));
		assertThat("3rd item have next line cached", cacheHelper.haveNextLineCached(2), is(true));
		assertThat("8th item does not have next line cached", cacheHelper.haveNextLineCached(7), is(false));
		assertThat("9th item do not have next line cached", cacheHelper.haveNextLineCached(9), is(false));
	}

	public void testRemoveMiddle() {
		FlowLayoutOptions layoutOptions = new FlowLayoutOptions();
		layoutOptions.itemsPerLine = 2;
		int contentAreaWidth = 10;
		CacheHelper cacheHelper = new CacheHelper(layoutOptions, contentAreaWidth);
		Point[] items = new Point[] {
				new Point(5, 1), new Point(2, 1)
				, new Point(4, 1), new Point(4, 1)
				, new Point(2, 1)
				, new Point(9, 1)
				, new Point(2, 1), new Point(3, 2)};
		cacheHelper.addToCache(0, items);

		cacheHelper.remove(3); // second 4 in second line

		// after remove it should be
		// 5, 2
		// 4, 2
		// 9
		// 2, 3

		int[] lineCounts = cacheHelper.getLineCounts();
		assertThat("line counts should have 4 lines", lineCounts.length, is(4));
		assertThat("line 1 have 2 items", lineCounts[0], is(2));
		assertThat("line 2 have 2 items", lineCounts[1], is(2));
		assertThat("line 3 have 1 items", lineCounts[2], is(1));
		assertThat("line 4 have 1 items", lineCounts[3], is(2));

		cacheHelper.remove(4); // 9 is removed
		// after removal it should be
		// 5, 2
		// 4, 2
		// 2, 3
		lineCounts = cacheHelper.getLineCounts();
		assertThat("line counts should have 3 lines", lineCounts.length, is(3));
		assertThat("line 1 have 2 items", lineCounts[0], is(2));
		assertThat("line 2 have 3 items", lineCounts[1], is(2));
		assertThat("line 3 have 1 items", lineCounts[2], is(2));
	}

	public void testAdd() throws Exception {
		FlowLayoutOptions layoutOptions = new FlowLayoutOptions();
		layoutOptions.itemsPerLine = FlowLayoutOptions.ITEM_PER_LINE_NO_LIMIT;
		int contentAreaWidth = 10;
		CacheHelper cacheHelper = new CacheHelper(layoutOptions, contentAreaWidth);

		Point[] items = new Point[] {
				new Point(5, 1), new Point(2, 1)
				, new Point(4, 1), new Point(4, 1), new Point(2, 1)
				, new Point(2, 1), new Point(3, 2)};
		cacheHelper.addToCache(0, items);

		cacheHelper.addToCache(3, new Point(9, 2));
		// after addition it should be:
		// 5, 2
		// 4
		// 9
		// 4, 2, 2
		// 3

		int[] lineCounts = cacheHelper.getLineCounts();
		assertThat("should have 5 lines", lineCounts.length, is(5));

		assertThat("line 1 have 2 items", lineCounts[0], is(2));
		assertThat("line 2 have 1 items", lineCounts[1], is(1));
		assertThat("line 3 have 1 items", lineCounts[2], is(1));
		assertThat("line 4 have 3 items", lineCounts[3], is(3));
		assertThat("line 5 have 1 items", lineCounts[4], is(1));
	}


}
