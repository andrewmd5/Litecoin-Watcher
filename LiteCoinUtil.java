package crypto.currencies;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**
 * @author Andrew
 *
 */

public class LiteCoinUtils extends LiteCoinGUI {
	static final String[] browsers = { "google-chrome", "firefox", "opera",
			"epiphany", "konqueror", "conkeror", "midori", "kazehakase",
			"mozilla" };
	static double CurrentHigh;
	static double CurrentLow;
	static double difference;
	static final String errMsg = "Error attempting to launch web browser";
	static Color highPrevColor = Color.WHITE;
	static double lastHigh;
	static double lastLow;
	static double lastPrice;
	static Color lowPrevColor = Color.WHITE;
	static Color mainPrevColor = Color.WHITE;
//god this naming
	static double mainPrice;

	static String pageSource;

	private static String getUrlSource(final String url) throws IOException {
		final URL exchangeURL = new URL(url);
		final URLConnection yc = exchangeURL.openConnection();
		final BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream(), "UTF-8"));
		String inputLine;
		final StringBuilder a = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			a.append(inputLine);
		}
		in.close();

		return a.toString();
	}

	public static void loadInterface() {
		new Thread(new Runnable() { // oh god why am i doing this
					@Override
					public void run() {
						while (true) {
							try {
								pageSource = getUrlSource("http://currentlitecoinprice.com/");
								setMainPrice();
								setLowPrice();
								setHighPrice();
								Thread.sleep(3000);
							} catch (final IOException e) {
								e.printStackTrace();
							} catch (final InterruptedException e) {
								e.printStackTrace();
							}

						}

					}

				}).start();

	}

	public static void openURL(final String url) { //the best java cheap shit ever
		try { // attempt to use Desktop library from JDK 1.6+
			final Class<?> d = Class.forName("java.awt.Desktop");
			d.getDeclaredMethod("browse", new Class[] { java.net.URI.class })
					.invoke(d.getDeclaredMethod("getDesktop").invoke(null),
							new Object[] { java.net.URI.create(url) });
			// above code mimicks: java.awt.Desktop.getDesktop().browse()
		} catch (final Exception ignore) { // library not available or failed
			final String osName = System.getProperty("os.name");
			try {
				if (osName.startsWith("Mac OS")) {
					Class.forName("com.apple.eio.FileManager")
							.getDeclaredMethod("openURL",
									new Class[] { String.class })
							.invoke(null, new Object[] { url });
				} else if (osName.startsWith("Windows")) {
					Runtime.getRuntime().exec(
							"rundll32 url.dll,FileProtocolHandler " + url);
				} else { // assume Unix or Linux
					String browser = null;
					for (final String b : browsers) {
						if (browser == null
								&& Runtime.getRuntime()
										.exec(new String[] { "which", b })
										.getInputStream().read() != -1) {
							Runtime.getRuntime().exec(
									new String[] { browser = b, url });
						}
					}
					if (browser == null) {
						throw new Exception(Arrays.toString(browsers));
					}
				}
			} catch (final Exception e) {
				JOptionPane.showMessageDialog(null,
						errMsg + "\n" + e.toString());
			}
		}
	}

	private static void setHighPrice() {
		final Pattern highPattern = Pattern
				.compile("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div id='high' style='display:inline; '>(.*?)</div></p>");
		final Matcher highMatch = highPattern.matcher(pageSource);
		while (highMatch.find()) {
			String highPrice = highMatch.group(1);
			highPrice = highPrice.replace("High: $", "");
			if (lastHigh == 0.0) {
				lastHigh = Double.parseDouble(highPrice);

			} else {
				lastHigh = CurrentHigh;
			}
			CurrentHigh = Double.parseDouble(highPrice);
			highPriceLabel.setText(highMatch.group(1));
		}
		highPriceLabel.setForeground(setLastHighColor(lastHigh, CurrentHigh));
	}

	private static Color setLastHighColor(final double lastHigh,
			final double newHigh) {
		Color cl = Color.WHITE;
		if (lastHigh > newHigh) {
			// i believe i can fly
			cl = Color.GREEN;
			highPrevColor = cl;
		} else if (lastHigh != 0.0 && lastHigh < newHigh) {
			// freefalling
			cl = Color.RED;
			highPrevColor = cl;
		} else if (lastHigh == newHigh) {
			//stay still
			cl = highPrevColor;
		}
		return cl;

	}

	private static Color setLastLowColor(final double lastLow,
			final double newLow) {
		Color cl = Color.WHITE;
		if (lastLow > newLow) {
			// lows up
			cl = Color.RED;
			lowPrevColor = cl;
		} else if (lastLow != 0.0 && lastLow < newLow) {
			// uh oh more lost
			cl = Color.GREEN;
			lowPrevColor = cl;
		} else if (lastLow == newLow) {
			// no change
			cl = lowPrevColor;
		}
		return cl;

	}

	private static void setLowPrice() {
		final Pattern lowPattern = Pattern
				.compile("<p id='subline'><div id='low' style='display:inline;'>(.*?)</div>");
		final Matcher lowMatch = lowPattern.matcher(pageSource);
		while (lowMatch.find()) {
			String lowPrice = lowMatch.group(1);
			lowPrice = lowPrice.replace("Low: $", "");
			if (lastLow == 0.0) {
				lastLow = Double.parseDouble(lowPrice);

			} else {
				lastLow = CurrentLow;
			}
			CurrentLow = Double.parseDouble(lowPrice);
			lowPriceLabel.setText(lowMatch.group(1));

		}

		lowPriceLabel.setForeground(setLastLowColor(lastLow, CurrentLow));

	}

	private static void setMainPrice() throws IOException {

		final Pattern rawMain = Pattern.compile("<div id='last'>(.*?)</div>");
		final Matcher rawMatch = rawMain.matcher(pageSource);
		while (rawMatch.find()) {
			String price = rawMatch.group(1);
			price = price.replace("$", "");
			lastPrice = mainPrice;
			mainPrice = Double.parseDouble(price);
		}

		mainPriceLabel.setText("$" + mainPrice);
		if (mainPrice != lastPrice) {
			lastPriceLabel.setText("$" + lastPrice);
		}
		mainPriceLabel.setForeground(setPriceColor(lastPrice, mainPrice));
		if (mainPrevColor == Color.GREEN) {

			difference = mainPrice - lastPrice;

			differenceLabel.setText("+$"
					+ (double) Math.round(difference * 100000) / 100000);
			differenceLabel.setForeground(mainPrevColor);
		} else if (mainPrevColor == Color.RED) {
			difference = lastPrice - mainPrice;

			differenceLabel.setText("-$"
					+ (double) Math.round(difference * 100000) / 100000);
			differenceLabel.setForeground(mainPrevColor);
		}
		// System.out.println(mainPrice);
	}

	private static Color setPriceColor(final double lastPrice,
			final double newPrice) {
		Color cl = Color.WHITE;
		if (lastPrice > newPrice) {
			//and we lost money
			cl = Color.RED;
			mainPrevColor = cl;
		} else if (lastPrice != 0.0 && lastPrice < newPrice) {
		//yay money
			cl = Color.GREEN;
			mainPrevColor = cl;
		} else if (lastPrice == newPrice) {
			// nothing to see
			cl = mainPrevColor;
		}
		return cl;

	}
}
//lets never do this again
