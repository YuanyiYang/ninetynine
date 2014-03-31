package edu.nyu.smg.ninetyNine.graphics;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;;


public interface GameSounds extends ClientBundle{

	@Source("sounds/pieceCaptured.mp3")
	DataResource cardCapturedMp3();
	
	@Source("sounds/pieceCaptured.wav")
	DataResource cardCapturedWav();
	
	@Source("sounds/pieceDown.mp3")
	DataResource cardDownMp3();
	
	@Source("sounds/pieceDown.wav")
	DataResource cardDownWav();
}
