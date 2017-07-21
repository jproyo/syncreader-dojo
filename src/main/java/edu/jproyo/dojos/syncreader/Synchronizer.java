/**
 * Copyright (C) 2015 mxHero Inc (mxhero@mxhero.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.jproyo.dojos.syncreader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.jproyo.dojos.syncreader.formatter.Formatter;
import edu.jproyo.dojos.syncreader.model.Label;
import edu.jproyo.dojos.syncreader.model.PrefixedLabel;
import edu.jproyo.dojos.syncreader.processor.FileProcessor;

/**
 * The Class Synchronizer.
 */
public class Synchronizer {
	
	/** The file path 1. */
	private String filePath1;
	
	/** The file path 2. */
	private String filePath2;
	
	/** The kill sync after seconds. */
	private long killSyncAfterSeconds = 30l;

	/** The formatter. */
	private Formatter formatter;
	
	/**
	 * Sync.
	 *
	 * @return the list
	 */
	public List<Label> sync(){
		List<Label> list = new ArrayList<>();
		ExecutorService pool = Executors.newFixedThreadPool(2);
		try{
			Semaphore semaphoreFile1 = new Semaphore(1);
			Semaphore semaphoreFile2 = new Semaphore(0);
			AtomicBoolean proceedUntilEnd = new AtomicBoolean(false);
			LinkedList<PrefixedLabel> messages = new LinkedList<>();
			
			FileProcessor fileTask1 = FileProcessor.create()
												.label(PrefixedLabel.FILE_1_LABEL)
												.semaphoreThis(semaphoreFile1)
												.semaphoreOther(semaphoreFile2)
												.messages(messages)
												.filePath(filePath1)
												.waitTimeInMillis(1000)
												.proceedUntilEnd(proceedUntilEnd)
												.build();
			
			FileProcessor fileTask2 = FileProcessor.create()
												.label(PrefixedLabel.FILE_2_LABEL)
												.semaphoreThis(semaphoreFile2)
												.semaphoreOther(semaphoreFile1)
												.messages(messages)
												.filePath(filePath2)
												.waitTimeInMillis(2000)
												.proceedUntilEnd(proceedUntilEnd)
												.build();
			
			list = CompletableFuture.allOf(CompletableFuture.runAsync(fileTask1, pool), CompletableFuture.runAsync(fileTask2, pool))
				.thenApply(fn -> formatter().formatRawList(messages)).get(killSyncAfterSeconds, TimeUnit.SECONDS);
			
		} catch (Exception e) {
			throw new RuntimeException("Error synchronizer", e);
		} finally {
			pool.shutdown();
			try {
				if(!pool.awaitTermination(1,  TimeUnit.SECONDS)){
					pool.shutdownNow();
				}
			} catch (InterruptedException e) {
			}
		}
		return list;
	}
	
	/**
	 * Formatter.
	 *
	 * @return the formatter
	 */
	private Formatter formatter() {
		if(formatter == null) formatter = new Formatter();
		return formatter;
	}

	/**
	 * Creates the.
	 *
	 * @return the builder
	 */
	public static Builder create(){
		return new Builder();
	}
	
	/**
	 * The Class Builder.
	 */
	public static class Builder{
		
		/** The target. */
		private Synchronizer target = new Synchronizer();
		
		/**
		 * File path 1.
		 *
		 * @param filePath the file path
		 * @return the builder
		 */
		public Builder filePath1(String filePath){
			this.target.filePath1 = filePath;
			return this;
		}

		/**
		 * File path 2.
		 *
		 * @param filePath the file path
		 * @return the builder
		 */
		public Builder filePath2(String filePath){
			this.target.filePath2 = filePath;
			return this;
		}
		
		/**
		 * Kill sync after seconds.
		 *
		 * @param seconds the seconds
		 * @return the builder
		 */
		public Builder killSyncAfterSeconds(long seconds){
			this.target.killSyncAfterSeconds = seconds;
			return this;
		}
		
		/**
		 * Builds the.
		 *
		 * @return the synchronizer
		 */
		public Synchronizer build(){
			return target;
		}

	}

}
