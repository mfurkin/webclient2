package org.misha.webclient.gui;

import java.util.function.Predicate;

public class StringFilterAsObject implements Predicate<Object> {
	private Predicate<String> filter;
	public StringFilterAsObject(Predicate<String> aFilter) {
		filter = aFilter;
	}

	@Override
	public boolean test(Object obj) {
		return (obj instanceof String) ? filter.test((String)obj) : false;
	}

}
