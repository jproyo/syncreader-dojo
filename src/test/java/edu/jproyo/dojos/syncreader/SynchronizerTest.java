package edu.jproyo.dojos.syncreader;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.jproyo.dojos.syncreader.model.Label;

public class SynchronizerTest {

	@Test
	public void testSync_successfull() {
		Synchronizer sync = Synchronizer.create()
			.filePath1(Thread.currentThread().getContextClassLoader().getResource("successfull/file1.txt").getPath())
			.filePath2(Thread.currentThread().getContextClassLoader().getResource("successfull/file2.txt").getPath())
			.killSyncAfterSeconds(30)
			.build();
		List<Label> labels = sync.sync();
		assertNotNull(labels);
		assertEquals(labels.get(0), new Label("1", "monday"));
		assertEquals(labels.get(1), new Label("2", "tuesday"));
		assertEquals(labels.get(2), new Label("3", "wedenesday"));
		assertEquals(labels.get(3), new Label("4", "thursday"));
	}
	
	@Test
	public void testSync_no_normal_form_file1() {
		Synchronizer sync = Synchronizer.create()
			.filePath1(Thread.currentThread().getContextClassLoader().getResource("no_normal_form_file1/file1.txt").getPath())
			.filePath2(Thread.currentThread().getContextClassLoader().getResource("no_normal_form_file1/file2.txt").getPath())
			.killSyncAfterSeconds(60)
			.build();
		List<Label> labels = sync.sync();
		assertNotNull(labels);
		assertEquals(labels.get(0), new Label("1", "monday"));
		assertEquals(labels.get(1), new Label("2", "tuesday"));
		assertEquals(labels.get(2), new Label("3", "wedenesday"));
		assertEquals(labels.get(3), new Label("4", "thursday"));
		assertEquals(labels.get(4), new Label("5", "friday"));
		assertEquals(labels.get(5), new Label("6", "saturday"));
		assertEquals(labels.get(6), new Label("7", "sunday"));
		assertEquals(labels.get(7), new Label("8", "NA"));
		assertEquals(labels.get(8), new Label("9", "NA"));
		assertEquals(labels.get(9), new Label("10", "NA"));
		assertEquals(labels.get(10), new Label("11", "NA"));
	}

	@Test
	public void testSync_no_normal_form_file2() {
		Synchronizer sync = Synchronizer.create()
			.filePath1(Thread.currentThread().getContextClassLoader().getResource("no_normal_form_file2/file1.txt").getPath())
			.filePath2(Thread.currentThread().getContextClassLoader().getResource("no_normal_form_file2/file2.txt").getPath())
			.killSyncAfterSeconds(60)
			.build();
		List<Label> labels = sync.sync();
		assertNotNull(labels);
		assertEquals(labels.get(0), new Label("1", "monday"));
		assertEquals(labels.get(1), new Label("2", "tuesday"));
		assertEquals(labels.get(2), new Label("3", "wedenesday"));
		assertEquals(labels.get(3), new Label("NA", "thursday"));
		assertEquals(labels.get(4), new Label("NA", "friday"));
		assertEquals(labels.get(5), new Label("NA", "saturday"));
		assertEquals(labels.get(6), new Label("NA", "sunday"));
	}
	
	@Test
	public void testSync_time_exceeded_for_processing() {
		Synchronizer sync = Synchronizer.create()
			.filePath1(Thread.currentThread().getContextClassLoader().getResource("no_normal_form_file2/file1.txt").getPath())
			.filePath2(Thread.currentThread().getContextClassLoader().getResource("no_normal_form_file2/file2.txt").getPath())
			.killSyncAfterSeconds(1)
			.build();
		try {
			sync.sync();
			fail();
		} catch (Exception e) {
		}
	}

}
