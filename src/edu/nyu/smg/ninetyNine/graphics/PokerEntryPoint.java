package edu.nyu.smg.ninetyNine.graphics;
import edu.nyu.smg.ninetyNine.client.GameApi;
import edu.nyu.smg.ninetyNine.client.GameApi.Game;
import edu.nyu.smg.ninetyNine.client.GameApi.IteratingPlayerContainer;
import edu.nyu.smg.ninetyNine.client.GameApi.UpdateUI;
import edu.nyu.smg.ninetyNine.client.GameApi.VerifyMove;
import edu.nyu.smg.ninetyNine.client.GameLogic;
import edu.nyu.smg.ninetyNine.client.PokerPresenter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PokerEntryPoint implements EntryPoint{
	
	IteratingPlayerContainer container;
	PokerPresenter pokerPresenter;
	

	@Override
	public void onModuleLoad() {
		Game game = new Game() {
			
			@Override
			public void sendVerifyMove(VerifyMove verifyMove) {
				container.sendVerifyMoveDone(new GameLogic().verify(verifyMove));			
			}
			
			@Override
			public void sendUpdateUI(UpdateUI updateUI) {
				pokerPresenter.updateUI(updateUI);
			}
		};		
		container = new IteratingPlayerContainer(game, 2);
		PokerGraphics pokerGraphics = new PokerGraphics();
		pokerPresenter = new PokerPresenter(container, pokerGraphics);
		final ListBox playerSelect = new ListBox();
		playerSelect.addItem("WhitePlayer");
		playerSelect.addItem("BlackPlayer");
		playerSelect.addItem("Viewer");
		playerSelect.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = playerSelect.getSelectedIndex();
				int playerId = selectedIndex == 2 ? GameApi.VIEWER_ID
			            : container.getPlayerIds().get(selectedIndex);
			        container.updateUi(playerId);
			}
		});
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(pokerGraphics);
		flowPanel.add(playerSelect);
		RootPanel.get("mainDiv").add(flowPanel);
		container.sendGameReady();
		container.updateUi(container.getPlayerIds().get(0));
	}

}
