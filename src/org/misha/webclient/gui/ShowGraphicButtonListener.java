package org.misha.webclient.gui;

public class ShowGraphicButtonListener extends ButtonListener {
	private GraphicShower shower;
	public ShowGraphicButtonListener(String aText, GraphicShower aShower) {
		super(aText);
		shower = aShower;
	}

	@Override
	protected void makeCallback() {
		shower.showGraphic();
	}

	

}
