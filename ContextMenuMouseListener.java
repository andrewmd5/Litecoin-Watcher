import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 * @author Andrew
 *
 */
public class ContextMenuMouseListener extends MouseAdapter {
	private enum Actions {
		COPY, CUT, PASTE, SELECT_ALL, UNDO
	}

	private final Action copyAction;
	private final Action cutAction;
	private Actions lastActionSelected;
	private final Action pasteAction;
	private final JPopupMenu popup = new JPopupMenu();

	private String savedString = "";
	private final Action selectAllAction;
	private JTextComponent textComponent;

	private final Action undoAction;;

	public ContextMenuMouseListener() {
		undoAction = new AbstractAction("Undo") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent ae) {
				textComponent.setText("");
				textComponent.replaceSelection(savedString);

				lastActionSelected = Actions.UNDO;
			}
		};


		popup.add(undoAction);
		popup.addSeparator();

		cutAction = new AbstractAction("Cut") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent ae) {
				lastActionSelected = Actions.CUT;
				savedString = textComponent.getText();
				textComponent.cut();
			}
		};

		popup.add(cutAction);

		copyAction = new AbstractAction("Copy") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent ae) {
				lastActionSelected = Actions.COPY;
				textComponent.copy();
			}
		};

		popup.add(copyAction);

		pasteAction = new AbstractAction("Paste") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent ae) {
				lastActionSelected = Actions.PASTE;
				savedString = textComponent.getText();
				textComponent.paste();
			}
		};

		popup.add(pasteAction);
		popup.addSeparator();

		selectAllAction = new AbstractAction("Select All") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent ae) {
				lastActionSelected = Actions.SELECT_ALL;
				textComponent.selectAll();
			}
		};

		popup.add(selectAllAction);
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
			if (!(e.getSource() instanceof JTextComponent)) {
				return;
			}

			textComponent = (JTextComponent) e.getSource();
			textComponent.requestFocus();

			final boolean enabled = textComponent.isEnabled();
			final boolean editable = textComponent.isEditable();
			final boolean nonempty = !(textComponent.getText() == null || textComponent
					.getText().equals(""));
			final boolean marked = textComponent.getSelectedText() != null;

			final boolean pasteAvailable = Toolkit.getDefaultToolkit()
					.getSystemClipboard().getContents(null)
					.isDataFlavorSupported(DataFlavor.stringFlavor);

			undoAction
					.setEnabled(enabled
							&& editable
							&& (lastActionSelected == Actions.CUT || lastActionSelected == Actions.PASTE));
			cutAction.setEnabled(enabled && editable && marked);
			copyAction.setEnabled(enabled && marked);
			pasteAction.setEnabled(enabled && editable && pasteAvailable);
			selectAllAction.setEnabled(enabled && nonempty);

			int nx = e.getX();

			if (nx > 500) {
				nx = nx - popup.getSize().width;
			}

			popup.show(e.getComponent(), nx, e.getY() - popup.getSize().height);
		}
	}
}
