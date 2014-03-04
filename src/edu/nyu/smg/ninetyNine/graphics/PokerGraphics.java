package edu.nyu.smg.ninetyNine.graphics;

import java.util.Collections;
import java.util.List;

import edu.nyu.smg.ninetyNine.client.Card;
import edu.nyu.smg.ninetyNine.client.PokerPresenter;
import edu.nyu.smg.ninetyNine.client.PokerPresenter.PokerMessage;
import edu.nyu.smg.ninetyNine.client.DirectionsOfTurn;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PokerGraphics extends Composite implements PokerPresenter.View {
	
	private static PokerGraphicsUiBinder uiBinder = GWT
			.create(PokerGraphicsUiBinder.class);

	interface PokerGraphicsUiBinder extends UiBinder<Widget, PokerGraphics> {
	}

	@UiField
	HorizontalPanel opponentArea;
	@UiField
	HorizontalPanel playerArea;
	@UiField
	HorizontalPanel selectedArea;
	@UiField
	HorizontalPanel usedArea;
	@UiField
	HorizontalPanel unUsedArea;
	@UiField
	HorizontalPanel points;
	@UiField
	HorizontalPanel direction;
	@UiField
	Button submitButton;
	@UiField
	Button subtractButton;
	@UiField
	HorizontalPanel mesg;
	
	private boolean enableClicks = false;
	private final CardImageSupplier cardImageSupplier;
	private PokerPresenter pokerPresenter;
	
	public PokerGraphics() {
		CardImages cardImages = GWT.create(CardImages.class);
		this.cardImageSupplier = new CardImageSupplier(cardImages);
		initWidget(uiBinder.createAndBindUi(this));
	}

	private List<Image> createBackCards(int numOfCards){
		List<CardImage> images = Lists.newArrayList();
		for(int i=0;i<numOfCards;i++){
			images.add(CardImage.Factory.getBackOfCardImage());
		}
		return createImages(images, false);
	}
	
	private List<Image> createCardImages(List<Card> cards, boolean withClick){
		List<CardImage> images = Lists.newArrayList();
		for(Card card : cards){
			images.add(CardImage.Factory.getCardImage(card));
		}
		return createImages(images, withClick);
	}
	
	private List<Image> createImages(List<CardImage> images, boolean withClick){
		List<Image> result = Lists.newArrayList();
		for(CardImage img : images){
			final CardImage imgFinal = img;
			Image image = new Image(cardImageSupplier.getResource(img));
			if(withClick){
				image.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						if(enableClicks){
							pokerPresenter.cardSelected(imgFinal.card);
						}
					}
				});
			}
			result.add(image);
		}
		return result;
	}
	
	private void placeImages(HorizontalPanel hPanel, List<Image> images){
		hPanel.clear();
		Image last = images.isEmpty() ? null : images.get(images.size()-1);
		for(Image image : images){
			FlowPanel imageContainer = new FlowPanel();
			imageContainer.setStyleName(image!=last ? "imgShortContainer" : "imgContainer");
			imageContainer.add(image);
			hPanel.add(imageContainer);
		}
	}
	
	private String directionString(boolean isClockwise){
		if(isClockwise){
			return DirectionsOfTurn.Clockwise.toString();
		}else{
			return DirectionsOfTurn.AntiClockwise.toString();
		}
	}
	
	private void placeString(HorizontalPanel hPanel, String str){
		hPanel.clear();
		Label label = new Label(str);
		hPanel.add(label);
	}
	
	private void disableSubmitClicks(){
		submitButton.setEnabled(false);
		enableClicks = false;
	}
	
	private void alertPokerMessage(PokerMessage pokerMessage){
		mesg.clear();
		String message = "";
		switch (pokerMessage) {
		case NEXT_MOVE_SUB:
			message += "The player's next move is to substract points!";
			break;
		case HAS_WINNER:
			message += "We have a winner!";
		case INVISIBLE:
			break;
		default:
			break;
		}
		if(message.isEmpty()){
			return;
		}
		Label label = new Label(message);
		mesg.add(label);
	}
	
	
	@UiHandler("submitButton")
	void onClickSubmitButton(ClickEvent e){
		subtractButton.setEnabled(true);
		disableSubmitClicks();
		pokerPresenter.finishedSelectingCards();
	}
	
	@UiHandler("subtractButton")
	void onClickSubtractButton(ClickEvent e){
		subtractButton.setEnabled(false);
		chooseNextMoveSub(true);
	}
	
	@Override
	public void setPresenter(PokerPresenter pokerPresenter) {
		this.pokerPresenter = pokerPresenter;
	}

	@Override
	public void setViewerState(int numberOfWhiteCards, int numberOfBlackCards,
			int numberOfCardsInUsedPile, int numberOfCardsInUnusedPile,
			int point, boolean isClockwise, PokerMessage pokerMessage) {
		placeImages(playerArea, createBackCards(numberOfWhiteCards));
		placeImages(selectedArea, ImmutableList.<Image>of());
		placeImages(opponentArea, createBackCards(numberOfBlackCards));
		placeImages(usedArea, createBackCards(numberOfCardsInUsedPile));
		placeImages(unUsedArea, createBackCards(numberOfCardsInUnusedPile));
		placeString(points, point+"");
		placeString(direction, directionString(isClockwise));
		alertPokerMessage(pokerMessage);
	}

	@Override
	public void setPlayerState(int numberOfOpponentCards,
			List<Card> usedCards, int numberOfCardsInUnusedPile,     // int numberOfCardsInUsedPile
			List<Card> myCards, int point, boolean isClockwise,
			PokerMessage pokerMessage) {
		Collections.sort(myCards);
		placeImages(playerArea, createCardImages(myCards, false));
		placeImages(selectedArea, ImmutableList.<Image>of());
		placeImages(opponentArea, createBackCards(numberOfOpponentCards));
		placeImages(usedArea, createCardImages(usedCards, false));             //createBackCards(numberOfCardsInUsedPile)
		placeImages(unUsedArea, createBackCards(numberOfCardsInUnusedPile));
		placeString(points, point+"");
		placeString(direction, directionString(isClockwise));
		alertPokerMessage(pokerMessage);
	}

	@Override
	public void chooseNextCard(List<Card> selectedCards,
			List<Card> remainingCards) {	
		Collections.sort(selectedCards);
		Collections.sort(remainingCards);
		enableClicks = true;
		placeImages(playerArea, createCardImages(remainingCards, true));
		placeImages(selectedArea, createCardImages(selectedCards, true));
		submitButton.setEnabled(!selectedCards.isEmpty());
	}

	
	@Override
	public void chooseNextMoveSub(boolean isSub) {
		pokerPresenter.viewsetSubField(isSub);	
	}

}
