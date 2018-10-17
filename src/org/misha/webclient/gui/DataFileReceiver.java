package org.misha.webclient.gui;

public interface DataFileReceiver {
	void setHeader(String aHeader);
	void setData(Sample[] array);
	void setTimeLimits();
}
