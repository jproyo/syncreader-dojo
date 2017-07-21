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
package edu.jproyo.dojos.syncreader.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * The Class FileProcessor.
 */
public class FileProcessor implements Runnable {
	
	/** The barrier. */
	private Semaphore semaphoreThis;

	/** The messages. */
	private LinkedList<String> messages;

	/** The file path. */
	private String filePath;

	/** The wait time millis. */
	private long waitTimeMillis;

	private Semaphore semaphoreOtherFile;
	
	
	/**
	 * Instantiates a new file processor.
	 *
	 * @param semaphoreThis the semaphore
	 * @param messages the messages
	 * @param filePath the file path
	 * @param waitTimeInMillis the wait time in millis
	 */
	public FileProcessor(Semaphore semaphoreThis, Semaphore semaphoreOtherFile, LinkedList<String> messages, String filePath, long waitTimeInMillis) {
		this.semaphoreThis = semaphoreThis;
		this.semaphoreOtherFile = semaphoreOtherFile;
		this.messages = messages;
		this.filePath = filePath;
		this.waitTimeMillis = waitTimeInMillis;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public void run() {
		String line = null;
		try(BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))){
			do {
				try {
					semaphoreThis.acquire();
					line = reader.readLine();
					messages.offer(line);
					semaphoreOtherFile.release();
				} catch (InterruptedException | IOException e) {
					throw new RuntimeException("Too much time waiting for barrier. Should be retried");
				}
				if(line != null){
					try {
						Thread.sleep(this.waitTimeMillis);
					} catch (InterruptedException e) {
					}
				}
			}while(line != null);
		} catch (IOException e1) {
			throw new RuntimeException("Too much time waiting for barrier. Should be retried");
		}
	}
	

}
