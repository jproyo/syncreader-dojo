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
import java.util.concurrent.atomic.AtomicBoolean;

import edu.jproyo.dojos.syncreader.model.PrefixedLabel;

/**
 * The Class FileProcessor.
 */
public class FileProcessor implements Runnable {
	
	/** The barrier. */
	private Semaphore semaphoreThis;

	/** The messages. */
	private LinkedList<PrefixedLabel> messages;

	/** The file path. */
	private String filePath;

	/** The wait time millis. */
	private long waitTimeMillis = 0l;

	/** The semaphore other file. */
	private Semaphore semaphoreOther;

	/** The label. */
	private String label;

	/** The other finished. */
	private AtomicBoolean proceedUntilEnd;
	
	
	/**
	 * Instantiates a new file processor.
	 */
	private FileProcessor() {}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public void run() {
		String line = null;
		try(BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))){
			do {
				try {
					if(proceedUntilEnd.get()){
						line = processLine(reader);
					}else{
						semaphoreThis.acquire();
						line = processLine(reader);
						semaphoreOther.release();
					}
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
			throw new RuntimeException("Too much time waiting for semaphore. Should be retried");
		}
		proceedUntilEnd.set(true);
	}

	private String processLine(BufferedReader reader) throws IOException {
		String line;
		line = reader.readLine();
		if(line != null && !line.isEmpty()){
			messages.offer(new PrefixedLabel(label,line));
		}
		return line;
	}
	
	
	/**
	 * Validate.
	 */
	public void validate(){
		if(this.label == null) throw new IllegalArgumentException("Label task shouldbe provided");
		if(this.semaphoreThis == null) throw new IllegalArgumentException("semaphoreThis shouldbe provided");
		if(this.semaphoreOther == null) throw new IllegalArgumentException("semaphoreOtherFile shouldbe provided");
		if(this.messages == null) throw new IllegalArgumentException("Messages Container shouldbe provided");
		if(this.filePath == null) throw new IllegalArgumentException("FilePath shouldbe provided");
		if(this.proceedUntilEnd == null) throw new IllegalArgumentException("proceedUntilEnd shouldbe provided");
		
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
	public static class Builder {
		
		/** The target. */
		private FileProcessor target = new FileProcessor();
		
		/**
		 * Label.
		 *
		 * @param label the label
		 * @return the builder
		 */
		public Builder label(String label){
			this.target.label = label;
			return this;
		}
		
		/**
		 * Semaphore this.
		 *
		 * @param semaphoreThis the semaphore this
		 * @return the builder
		 */
		public Builder semaphoreThis(Semaphore semaphoreThis){
			this.target.semaphoreThis = semaphoreThis;
			return this;
		}
		
		/**
		 * Semaphore other.
		 *
		 * @param semaphoreOther the semaphore other
		 * @return the builder
		 */
		public Builder semaphoreOther(Semaphore semaphoreOther){
			this.target.semaphoreOther = semaphoreOther;
			return this;
		}
		
		/**
		 * Messages.
		 *
		 * @param messages the messages
		 * @return the builder
		 */
		public Builder messages(LinkedList<PrefixedLabel> messages){
			this.target.messages = messages;
			return this;
		}
		
		/**
		 * File path.
		 *
		 * @param filePath the file path
		 * @return the builder
		 */
		public Builder filePath(String filePath){
			this.target.filePath = filePath;
			return this;
		}
		
		/**
		 * Wait time in millis.
		 *
		 * @param waitTimeInMillis the wait time in millis
		 * @return the builder
		 */
		public Builder waitTimeInMillis(long waitTimeInMillis){
			this.target.waitTimeMillis = waitTimeInMillis;
			return this;
		}
		
		/**
		 * Other finished.
		 *
		 * @param otherFinished the other finished
		 * @return the builder
		 */
		public Builder proceedUntilEnd(AtomicBoolean proceedUntilEnd){
			this.target.proceedUntilEnd = proceedUntilEnd;
			return this;
		}
		
		/**
		 * Builds the.
		 *
		 * @return the file processor
		 */
		public FileProcessor build(){
			this.target.validate();
			return this.target;
		}
		
	}

}
