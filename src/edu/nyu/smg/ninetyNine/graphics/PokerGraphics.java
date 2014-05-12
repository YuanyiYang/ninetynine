package edu.nyu.smg.ninetyNine.graphics;

import java.util.Collections;
import java.util.List;

import edu.nyu.smg.ninetyNine.client.Card;
import edu.nyu.smg.ninetyNine.client.PokerPresenter;
import edu.nyu.smg.ninetyNine.client.PokerPresenter.PokerMessage;
import edu.nyu.smg.ninetyNine.client.DirectionsOfTurn;
import edu.nyu.smg.ninetyNine.client.Rank;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.media.client.Audio;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PokerGraphics extends Composite implements PokerPresenter.View {

	private static GameSounds gameSounds = GWT.create(GameSounds.class);
	
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
//	@UiField
//	AbsolutePanel dndArea;
//	@UiField
//	Label dragMe;
//	@UiField
//	Label dragOntoMe;

	
	private boolean enableClicks = false;
	private final CardImageSupplier cardImageSupplier;
	private PokerPresenter pokerPresenter;
	private CardFadeAnimation fadeAnimation;
	private Timer myTimer;
	private Audio cardSelected;
	private Audio cardDown; // haven't used it in any where
//	private PickupDragController dragController;
//	private SimpleDropController dropController;
	private GameConstants gameConstants;
	

	public PokerGraphics() {
		gameConstants = (GameConstants)GWT.create(GameConstants.class);
		CardImages cardImages = GWT.create(CardImages.class);
		this.cardImageSupplier = new CardImageSupplier(cardImages);
		initWidget(uiBinder.createAndBindUi(this));
		if(Audio.isSupported()){
			cardSelected = Audio.createIfSupported();
			cardSelected.addSource(gameSounds.cardCapturedMp3().getSafeUri().asString(), AudioElement.TYPE_MP3);
			cardSelected.addSource(gameSounds.cardCapturedWav().getSafeUri().asString(), AudioElement.TYPE_WAV);
			cardDown = Audio.createIfSupported();
			cardDown.addSource(gameSounds.cardDownMp3().getSafeUri().asString(), AudioElement.TYPE_MP3);
			cardDown.addSource(gameSounds.cardDownWav().getSafeUri().asString(), AudioElement.TYPE_WAV);
		}
		submitButton.setText(gameConstants.submitButton());
		subtractButton.setText(gameConstants.subMoveButton());
//		dndArea.setPixelSize(300, 300);
//		dndArea.setStyleName("dnd-started-blue");
//		dragOntoMe.setStyleName("dnd-default");
//		dragController = new PickupDragController(dndArea, true);
//		dragController.setBehaviorConstrainedToBoundaryPanel(true);
//		dragController.setBehaviorMultipleSelection(true);
//		dropController = new SimpleDropController(dragOntoMe){
//			@Override
//			public void onDrop(DragContext context){
//				super.onDrop(context);
//				dragOntoMe.setStyleName("dnd-after-drop");
//				dragOntoMe.getElement().getStyle().setFontSize(2, Unit.EM);
//			}
//			
//			@Override
//			public void onEnter(DragContext context){
//				super.onEnter(context);
//				dragOntoMe.setStyleName("dnd-enter");
//				dragOntoMe.getElement().getStyle().setFontSize(5, Unit.EM);
//			}
//			
//			@Override
//			public void onLeave(DragContext context){
//				super.onEnter(context);
//				dragOntoMe.setStyleName("dnd-default");	
//				dragOntoMe.getElement().getStyle().setFontSize(2, Unit.EM);
//			}
//		};
//		dragController.registerDropController(dropController);
//		dragController.makeDraggable(dragMe);
	}

	private List<Image> createBackCards(int numOfCards) {
		List<CardImage> images = Lists.newArrayList();
		for (int i = 0; i < numOfCards; i++) {
			images.add(CardImage.Factory.getBackOfCardImage());
		}
		return createImages(images, false);
	}

	/*
	 * Use a Timer to simulate double click event. No animation on double click. Fading animation on
	 * single click. Play the sound after the animation.
	 */
	private class myClickHandler implements ClickHandler {
		private Image image;
		private CardImage imgFinal;

		public myClickHandler(Image image, CardImage imgFinal) {
			this.image = image;
			this.imgFinal = imgFinal;
		}

		@Override
		public void onClick(ClickEvent event) {
			if (enableClicks) {
				if (myTimer == null) {
					myTimer = new Timer() {
						@Override
						public void run() {
							fadeAnimation = new CardFadeAnimation(image,cardSelected);
							fadeAnimation.fade(500, 1.0);
							myTimer.cancel();
							myTimer = null;
						}
					};
					myTimer.schedule(200);
				} else {
					myTimer.cancel();
					myTimer = null;
					pokerPresenter.cardSelected(imgFinal.card);
				}
			}
		}
	}
	
	private List<Image> createCardImagesWithSub(List<Card> cards){
		List<CardImage> images = Lists.newArrayList();
		for(Card card : cards){
			images.add(CardImage.Factory.getCardImage(card));
		}
		List<Image> result = Lists.newArrayList();
		for(CardImage img : images){
			final CardImage imgFinal = img;
			final Image image = new Image(cardImageSupplier.getResource(img));
			
			if(imgFinal.card.getCardRank()==Rank.TEN || imgFinal.card.getCardRank()==Rank.QUEEN){
					image.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {						
							pokerPresenter.subCardSelected(imgFinal.card);										
					}
				});
			}
			result.add(image);
		}
		return result;
	}
	
	private List<Image> createCardImages(List<Card> cards, boolean withClick) {
		List<CardImage> images = Lists.newArrayList();
		for (Card card : cards) {
			images.add(CardImage.Factory.getCardImage(card));
		}
		return createImages(images, withClick);
	}

	
	private List<Image> createImages(List<CardImage> images, boolean withClick) {
		List<Image> result = Lists.newArrayList();
		for (CardImage img : images) {
			final CardImage imgFinal = img;
			final Image image = new Image(cardImageSupplier.getResource(img));
			if (withClick) {
				
//				image.addClickHandler(new myClickHandler(image, imgFinal)
//				
//				);
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

	private void placeImages(HorizontalPanel hPanel, List<Image> images) {
		hPanel.clear();
		Image last = images.isEmpty() ? null : images.get(images.size() - 1);
		for (Image image : images) {
			FlowPanel imageContainer = new FlowPanel();
			imageContainer.setStyleName(image != last ? "imgShortContainer"
					: "imgContainer");
			imageContainer.add(image);
			hPanel.add(imageContainer);
		}
	}

	private String directionString(boolean isClockwise) {
		if (isClockwise) {
			return gameConstants.clockwise();
			//return DirectionsOfTurn.Clockwise.toString();
		} else {
			//return DirectionsOfTurn.AntiClockwise.toString();
			return gameConstants.antiClockwise();
		}
	}

	private void placeString(HorizontalPanel hPanel, String str) {
		hPanel.clear();
		Label label = new Label(str);
		hPanel.add(label);
	}

	private void disableSubmitClicks() {
		submitButton.setEnabled(false);
		enableClicks = false;
	}

	public void disableAllButton() {
		submitButton.setEnabled(false);
		enableClicks = false;
		presenterSetSub(false);
	}

	private void alertPokerMessage(PokerMessage pokerMessage) {
		mesg.clear();
		String message = "";
		switch (pokerMessage) {
		case NEXT_MOVE_SUB:
			message += gameConstants.nextMoveSub();
			break;
		case HAS_WINNER:
			message += gameConstants.hasWinner();
			break;
		case AI_WIN:
			message += "Oops! The dummy AI beats you...";
			break;
		case INVISIBLE:
			break;
		default:
			break;
		}
		if (message.isEmpty()) {
			return;
		}
		Label label = new Label(message);
		mesg.add(label);
	}
	
	@Override
	public void setAIWin(){
		alertPokerMessage(PokerMessage.HAS_WINNER);
	}

	@UiHandler("submitButton")
	void onClickSubmitButton(ClickEvent e) {
		subtractButton.setEnabled(false); // change here to fix bug
		disableSubmitClicks();
		pokerPresenter.finishedSelectingCards();
	}

	@UiHandler("subtractButton")
	void onClickSubtractButton(ClickEvent e) {
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
		placeImages(selectedArea, ImmutableList.<Image> of());
		placeImages(opponentArea, createBackCards(numberOfBlackCards));
		placeImages(usedArea, createBackCards(numberOfCardsInUsedPile));
		placeImages(unUsedArea, createBackCards(numberOfCardsInUnusedPile));
		placeString(points, point + "");
		placeString(direction, directionString(isClockwise));
		alertPokerMessage(pokerMessage);
	}

	@Override
	public void setPlayerState(int numberOfOpponentCards,
			List<Card> usedCards,
			int numberOfCardsInUnusedPile, // int numberOfCardsInUsedPile
			List<Card> myCards, int point, boolean isClockwise,
			PokerMessage pokerMessage) {
		Collections.sort(myCards);
		placeImages(playerArea, createCardImages(myCards, false));
		placeImages(selectedArea, ImmutableList.<Image> of());
		placeImages(opponentArea, createBackCards(numberOfOpponentCards));
		placeImages(usedArea, createCardImages(usedCards, false)); // createBackCards(numberOfCardsInUsedPile)
		placeImages(unUsedArea, createBackCards(numberOfCardsInUnusedPile));
		placeString(points, point + "");
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
	public void chooseNextSubCard(List<Card> selectedCards, List<Card> remainingCards){
		Collections.sort(selectedCards);
		Collections.sort(remainingCards);
		enableClicks = true;
		placeImages(selectedArea, createCardImagesWithSub(selectedCards));
		placeImages(playerArea, createCardImagesWithSub(remainingCards));
		submitButton.setEnabled(!selectedCards.isEmpty());
	}

	@Override
	public void chooseNextMoveSub(boolean isSub) {
		pokerPresenter.viewsetSubField(isSub);
	}

	@Override
	public void presenterSetSub(boolean fromPersenterSub) {
		subtractButton.setEnabled(fromPersenterSub);
	}
}
