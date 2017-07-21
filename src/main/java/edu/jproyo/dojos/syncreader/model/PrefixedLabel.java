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
package edu.jproyo.dojos.syncreader.model;

/**
 * The Class PrefixedLabel.
 */
public class PrefixedLabel {
	
	/** The Constant FILE_1_LABEL. */
	public static final String FILE_1_LABEL = "file1";
	
	/** The Constant FILE_2_LABEL. */
	public static final String FILE_2_LABEL = "file2";
	
	/** The prefix. */
	private String prefix = FILE_1_LABEL;
	
	/** The value. */
	private String value = Label.NA_LABEL;
	
	/**
	 * Instantiates a new prefixed label.
	 *
	 * @param prefix the prefix
	 * @param value the value
	 */
	public PrefixedLabel(String prefix, String value) {
		this.prefix = prefix;
		this.value = value;
	}
	
	/**
	 * Gets the prefix.
	 *
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrefixedLabel other = (PrefixedLabel) obj;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/**
	 * Checks if is first label.
	 *
	 * @return true, if is first label
	 */
	public boolean isFirstLabel() {
		return FILE_1_LABEL.equals(getPrefix());
	}

	/**
	 * Checks if is second label.
	 *
	 * @return true, if is second label
	 */
	public boolean isSecondLabel() {
		return FILE_2_LABEL.equals(getPrefix());
	}
	
	

}
