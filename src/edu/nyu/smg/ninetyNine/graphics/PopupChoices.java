package edu.nyu.smg.ninetyNine.graphics;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * A popup widget showing the user a couple of options to select from. It is a
 * modal popup, so the user must select one of the options. The first choice can
 * be selected by pressing ENTER. You can call center() to show the popup.
 */
public class PopupChoices extends DialogBox {

	public interface OptionChosen {
		void optionChosen(String option);
	}

	private Button firstChoice;

	public PopupChoices(String mainText, List<String> options,
			final OptionChosen optionChosen) {
		super(false, true);
		setText(mainText);
		setAnimationEnabled(true);
		HorizontalPanel buttons = new HorizontalPanel();
		for (String option : options) {
			final String optionF = option;
			Button btn = new Button(option);
			if (firstChoice == null) {
				firstChoice = btn;
			}
			btn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					hide();
					optionChosen.optionChosen(optionF);
				}
			});
			buttons.add(btn);
			if (option != options.get(options.size() - 1)) {
				Label label = new Label();
				label.setStyleName("withMargin");
				buttons.add(label);
			}
		}
		setWidget(buttons);
	}

	@Override
	public void center() {
		super.center();
		firstChoice.setFocus(true);
	}

}
