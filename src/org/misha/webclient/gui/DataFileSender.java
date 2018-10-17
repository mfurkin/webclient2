package org.misha.webclient.gui;

public interface DataFileSender {
	void hideProgressBar();
	void setupProgressBar(int avail);
	void updateProgressBar(int bytes_written);
}
