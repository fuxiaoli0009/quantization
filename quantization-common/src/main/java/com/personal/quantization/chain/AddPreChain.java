package com.personal.quantization.chain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddPreChain {

	@Autowired
	public List<AddPreHandler> addPreHandlerList;
	
	public String addPre(String source) {
		String result = null;
		for(AddPreHandler addPreHandler : addPreHandlerList) {
			result = addPreHandler.addPre(source);
			if(result != null) {
				return result;
			}
		}
		return result;
	}
}
