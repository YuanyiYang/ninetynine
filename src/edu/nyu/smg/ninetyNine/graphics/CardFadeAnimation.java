package edu.nyu.smg.ninetyNine.graphics;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.Image;

public class CardFadeAnimation extends Animation{

	private Image image;
	private double opacityIncrement;
	private double targetOpacity;
	private double baseOpacity;
	private Audio soundEfx;
	
	public CardFadeAnimation(Image image, Audio soundEfx){
		this.image = image;
		this.soundEfx = soundEfx;
	}
	
	@Override
	protected void onUpdate(double progress) {
		image.getElement().getStyle().setOpacity(baseOpacity + progress * opacityIncrement);
	}
	
	@Override
	protected void onComplete() {
		super.onComplete();
		if(soundEfx!=null){
			soundEfx.play();
		}
		image.getElement().getStyle().setOpacity(targetOpacity);
		
	}
	
	public void fade(int duration, double targetOpacity){
		if(targetOpacity>1.0) targetOpacity=1.0;
		if(targetOpacity<0.0) targetOpacity=0.0;
		this.targetOpacity = targetOpacity;
		try {
			baseOpacity = 0.0;
			opacityIncrement = targetOpacity-baseOpacity;
			run(duration);
		} catch (NumberFormatException e) {
			onComplete();
		}
	}


	
}
