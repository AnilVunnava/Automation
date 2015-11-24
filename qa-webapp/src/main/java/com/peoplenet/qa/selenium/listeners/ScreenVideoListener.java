package com.peoplenet.qa.selenium.listeners;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import com.peoplenet.qa.selenium.factory.PropertiesLoader;

/**
 * @author Anil.Vunnava
 *
 *         ScreenVideo Listener is to generate the video of all test cases
 *         executed in the local environment and stored at location
 *         PropertiesLoader .getProperty("screenshotDirectory")/video/
 * 
 *         This will be enabled on the property set enableVideo=true/false
 */
public class ScreenVideoListener {
	private ScreenRecorder screenRecorder;

	/**
	 * @param project
	 * @throws Exception
	 */
	public void startRecording(String project) throws Exception {
		String screenRecdir = PropertiesLoader.getProperty("testResultsDir") != null ? PropertiesLoader
				.getProperty("testResultsDir") + "/video/"
				: new File(".") + "/video/";
		File file = new File(screenRecdir);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;

		Rectangle captureSize = new Rectangle(0, 0, width, height);

		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		this.screenRecorder = new SpecializedScreenRecorder(
				gc,
				captureSize,
				new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey,
						ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
						CompressorNameKey,
						ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24,
						FrameRateKey, Rational.valueOf(15), QualityKey, 1.0f,
						KeyFrameIntervalKey, 15 * 60), new Format(MediaTypeKey,
						MediaType.VIDEO, EncodingKey, "black", FrameRateKey,
						Rational.valueOf(30)), null, file, project);
		this.screenRecorder.start();
	}

	/**
	 * @throws Exception
	 */
	public void stopRecording() throws Exception {
		this.screenRecorder.stop();
	}
}
