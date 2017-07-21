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
package edu.jproyo.dojos.syncreader.formatter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.jproyo.dojos.syncreader.model.Label;
import edu.jproyo.dojos.syncreader.model.PrefixedLabel;

/**
 * The Class Formatter.
 */
public class Formatter {
	
	/**
	 * Format raw list.
	 *
	 * @param messages the messages
	 * @return the list
	 */
	public List<Label> formatRawList(LinkedList<PrefixedLabel> messages){
		List<Label> list = new ArrayList<>();
		for (int i = 0; i < messages.size(); i+=2) {
			PrefixedLabel first = messages.get(i);
			PrefixedLabel second = new PrefixedLabel(PrefixedLabel.FILE_2_LABEL, Label.NA_LABEL);
			if(i+1 < messages.size()){
				second = messages.get(i+1);
			}
			if(second.isFirstLabel()){
				list.add(new Label(first.getValue(), Label.NA_LABEL));
				list.add(new Label(second.getValue(), Label.NA_LABEL));
			}else if(first.isSecondLabel()){
				list.add(new Label(Label.NA_LABEL, first.getValue()));
				list.add(new Label(Label.NA_LABEL, second.getValue()));
			}else{
				list.add(new Label(first.getValue(), second.getValue()));
			}
		}
		return list;
	}

}
