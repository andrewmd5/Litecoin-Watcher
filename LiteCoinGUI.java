package crypto.currencies;

import java.awt.Color;

/**
 * @author Andrew
 * @email andrew@codeusa523.org
 * #toostatic4me
 */
public class LiteCoinGUI {

	static JLabel currentPrice = new JLabel("Current Price:");
	static JLabel differenceLabel = new JLabel("");
	static JLabel highPriceLabel = new JLabel("...");
	static JLabel lastPriceLabel = new JLabel("...");
	static JLabel lblLastPrice = new JLabel("Last Price:");
	static JLabel lowPriceLabel = new JLabel("...");
	static JLabel mainPriceLabel = new JLabel("...");

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final LiteCoinGUI window = new LiteCoinGUI();
					window.frmCurrentLitecoinPrice.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	private final JTextArea donationsText = new JTextArea();

	private JFrame frmCurrentLitecoinPrice;

	/**
	 * Create the application.
	 */
	public LiteCoinGUI() {
		initialize();
		LiteCoinUtils.loadInterface();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCurrentLitecoinPrice = new JFrame();
		frmCurrentLitecoinPrice.setResizable(false);
		frmCurrentLitecoinPrice.setTitle("Litecoin Stats");
		frmCurrentLitecoinPrice.getContentPane().setBackground(Color.DARK_GRAY);
		frmCurrentLitecoinPrice.setBounds(100, 100, 450, 289);
		frmCurrentLitecoinPrice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCurrentLitecoinPrice.getContentPane().setLayout(null);

		mainPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPriceLabel.setFont(new Font("Calibri Light", Font.PLAIN, 24));

		mainPriceLabel.setBounds(222, 35, 201, 86);
		frmCurrentLitecoinPrice.getContentPane().add(mainPriceLabel);

		lowPriceLabel.setForeground(Color.WHITE);
		lowPriceLabel.setFont(new Font("Calibri Light", Font.PLAIN, 14));
		lowPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);

		lowPriceLabel.setBounds(65, 131, 125, 37);
		frmCurrentLitecoinPrice.getContentPane().add(lowPriceLabel);
		highPriceLabel.setForeground(Color.WHITE);
		highPriceLabel.setFont(new Font("Calibri Light", Font.PLAIN, 14));
		highPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);

		highPriceLabel.setBounds(252, 132, 143, 37);
		frmCurrentLitecoinPrice.getContentPane().add(highPriceLabel);
		currentPrice.setForeground(Color.WHITE);
		currentPrice.setHorizontalAlignment(SwingConstants.CENTER);
		currentPrice.setFont(new Font("Calibri Light", Font.PLAIN, 24));
		currentPrice.setBounds(43, 49, 178, 56);

		frmCurrentLitecoinPrice.getContentPane().add(currentPrice);
		lblLastPrice.setForeground(Color.WHITE);
		lblLastPrice.setFont(new Font("Calibri Light", Font.PLAIN, 24));
		lblLastPrice.setHorizontalAlignment(SwingConstants.CENTER);
		lblLastPrice.setBounds(65, 104, 125, 27);

		frmCurrentLitecoinPrice.getContentPane().add(lblLastPrice);
		lastPriceLabel.setForeground(Color.WHITE);
		lastPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lastPriceLabel.setFont(new Font("Calibri Light", Font.PLAIN, 24));
		lastPriceLabel.setBounds(222, 74, 201, 86);

		frmCurrentLitecoinPrice.getContentPane().add(lastPriceLabel);
		donationsText.setBackground(Color.DARK_GRAY);
		donationsText.setForeground(Color.WHITE);
		donationsText.setText("Donations: LYmNUo62vbxKM5mipM8ZSsnv5RbPBhHj6z");
		donationsText.setBounds(10, 209, 382, 22);
		donationsText.addMouseListener(new ContextMenuMouseListener());

		frmCurrentLitecoinPrice.getContentPane().add(donationsText);

		final JButton btnNewButton = new JButton("Play my game.");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				LiteCoinUtils.openURL("http://codeusa.net/");
			}
		});
		btnNewButton.setBounds(10, 227, 125, 23);
		frmCurrentLitecoinPrice.getContentPane().add(btnNewButton);
		differenceLabel.setBounds(359, 73, 85, 14);

		frmCurrentLitecoinPrice.getContentPane().add(differenceLabel);

	}
}
