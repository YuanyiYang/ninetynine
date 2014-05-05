package edu.nyu.smg.ninetyNine.graphics;
import org.game_api.GameApi;
import org.game_api.GameApi.ContainerConnector;
import org.game_api.GameApi.Game;
import org.game_api.GameApi.UpdateUI;
import org.game_api.GameApi.VerifyMove;
import org.game_api.GameApi.IteratingPlayerContainer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

import edu.nyu.smg.ninetyNine.client.GameLogic;
import edu.nyu.smg.ninetyNine.client.PokerPresenter;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PokerEntryPoint implements EntryPoint{
	
	ContainerConnector container;
//	IteratingPlayerContainer container;
	PokerPresenter pokerPresenter;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {	
		
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {		
			@Override
			public void onUncaughtException(Throwable e) {
				String text = "Uncaught Exception";
				while(e!=null){
					StackTraceElement[] stackTraceElements = e.getStackTrace();
					text += e.toString() + "\n";
					for(int i=0; i<stackTraceElements.length;i++){
						text += " at " + stackTraceElements[i] + "\n";
					}
					e = e.getCause();
					if(e!=null){
						text += " Caused by: ";
					}
				}
				DialogBox dialogBox = new DialogBox(true, false);
		        DOM.setStyleAttribute(dialogBox.getElement(), "backgroundColor", "#ABCDEF");
		        System.err.print(text);
		        text = text.replaceAll(" ", "&nbsp;");
		        dialogBox.setHTML("<pre>" + text + "</pre>");
		        dialogBox.center();
			}
		});
		
		DeferredCommand.addCommand(new Command() {		
			@Override
			public void execute() {
				onModuleLoad2();
			}
		});
	}
	
	private void onModuleLoad2(){
		
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
		
		container = new ContainerConnector(game);
//		container = new IteratingPlayerContainer(game, 2);
		PokerGraphics pokerGraphics = new PokerGraphics();
		pokerPresenter = new PokerPresenter(container, pokerGraphics);
			
//		final ListBox playerSelect = new ListBox();
//		playerSelect.addItem("WhitePlayer");
//		playerSelect.addItem("BlackPlayer");
//		playerSelect.addItem("Viewer");
//		playerSelect.addChangeHandler(new ChangeHandler() {
//			
//			@Override
//			public void onChange(ChangeEvent event) {
//				int selectedIndex = playerSelect.getSelectedIndex();
//				String playerId = selectedIndex == 2 ? GameApi.VIEWER_ID
//			            : container.getPlayerIds().get(selectedIndex);
//			        container.updateUi(playerId);
//			}
//		});
	 
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(pokerGraphics);
//		flowPanel.add(playerSelect);
		RootPanel.get().setPixelSize(800, 800);
		RootPanel.get("mainDiv").add(flowPanel);
		container.sendGameReady();	
//		container.updateUi(container.getPlayerIds().get(0));
	}

}
