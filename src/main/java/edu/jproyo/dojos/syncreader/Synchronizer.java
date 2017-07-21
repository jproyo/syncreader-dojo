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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.jproyo.dojos.syncreader.model.Label;
import edu.jproyo.dojos.syncreader.processor.FileProcessor;

/**
 * The Class Synchronizer.
 */
public class Synchronizer {
	
	/** The file path 1. */
	private String filePath1;
	
	/** The file path 2. */
	private String filePath2;
	
	/**
	 * Sync.
	 * @return 
	 */
	public List<Label> sync(){
		List<Label> list = new ArrayList<>();
		ExecutorService pool = Executors.newFixedThreadPool(2);
		try{
			Semaphore semaphoreFile1 = new Semaphore(1);
			Semaphore semaphoreFile2 = new Semaphore(0);
			LinkedList<String> messages = new LinkedList<>();
			long currentTimeMillis = System.currentTimeMillis();
			Future<?> submitFile1 = pool.submit(new FileProcessor(semaphoreFile1, semaphoreFile2, messages, filePath1, 1000));
			Future<?> submitFile2 = pool.submit(new FileProcessor(semaphoreFile2, semaphoreFile1, messages, filePath2, 2000));
			
			while(!submitFile1.isDone() && !submitFile2.isDone()){
				if(System.currentTimeMillis()-currentTimeMillis > 20000){
					throw new TimeoutException("Too much time waiting for processing");
				}
			}
			
			for (int i = 0; i < messages.size(); i+=2) {
				list.add(new Label(messages.get(i), messages.get(i+1)));
			}
			
		} catch (Exception e) {
		} finally {
			pool.shutdown();
			try {
				if(!pool.awaitTermination(10,  TimeUnit.SECONDS)){
					pool.shutdownNow();
				}
			} catch (InterruptedException e) {
			}
		}
		return list;
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
		 * Builds the.
		 *
		 * @return the synchronizer
		 */
		public Synchronizer build(){
			return target;
		}

	}

}
