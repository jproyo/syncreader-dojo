package edu.jproyo.dojos.syncreader;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.jproyo.dojos.syncreader.model.Label;

public class SynchronizerTest {

	@Test
	public void testSync_successfull() {
		Synchronizer sync = Synchronizer.create()
			.filePath1(Thread.currentThread().getContextClassLoader().getResource("file1.txt").getPath())
			.filePath2(Thread.currentThread().getContextClassLoader().getResource("file2.txt").getPath())
			.build();
		List<Label> labels = sync.sync();
		assertNotNull(labels);
		assertEquals(labels.get(0), new Label("1", "monday"));
		assertEquals(labels.get(1), new Label("2", "tuesday"));
		assertEquals(labels.get(2), new Label("3", "wedenesday"));
		assertEquals(labels.get(3), new Label("4", "thursday"));
	}

}
