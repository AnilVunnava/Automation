package com.peoplenet.qa.selenium.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.factory.SeleniumDriverFactory;

public class MailerService {
	private static Properties javaMailProperties = new Properties();
	private static VelocityEngine velocityEngine;
	private static final Logger log = Logger.getLogger("MailerService");

	static {
		try {
			if (velocityEngine == null) {
				velocityEngine = new VelocityEngine();
				velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER,
						"classpath");
				velocityEngine.setProperty("classpath.resource.loader.class",
						ClasspathResourceLoader.class.getName());
				velocityEngine.init();
			}
			if (javaMailProperties.isEmpty()) {
				javaMailProperties.put("mail.smtp.auth", true);
				javaMailProperties.put("mail.smtp.starttls.enable", true);
				javaMailProperties.put("mail.smtp.host",
						PropertiesLoader.getProperty("mailHost"));
				javaMailProperties.put("mail.smtp.socketFactory.port",
						PropertiesLoader.getProperty("mailPort"));
				javaMailProperties.put("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
				javaMailProperties.put("mail.smtp.auth", "true");
				javaMailProperties.put("mail.smtp.port",
						PropertiesLoader.getProperty("mailPort"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean sendMail(final String from, final String frmPass,
			String to, String filename) {
		try {
			Session session = Session.getDefaultInstance(javaMailProperties,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(from, frmPass);
						}
					});
			MimeBodyPart mbp1 = new MimeBodyPart();
			MimeBodyPart mbp2 = new MimeBodyPart();
			MimeMultipart part = new MimeMultipart();
			Template t = velocityEngine.getTemplate("reports/mail-report.vm");
			VelocityContext context = new VelocityContext();
			context.put("count", SeleniumDriverFactory.getSuiteConfig().size());
			context.put("functionality",
					PropertiesLoader.getProperty("functionality"));
			context.put("scenarios", PropertiesLoader.getProperty("scenarios"));
			context.put("mailBody", getMailBodyHtml());
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			String text = writer.toString();
			mbp1.setText(text);
			mbp1.setContent(text, "text/html;charset=UTF-8");
			MimeMessage message = new MimeMessage(session);
			message.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(PropertiesLoader.getProperty("mailSubject")
					+ " ["
					+ new SimpleDateFormat("yyyy-MM-dd-HH-mm")
							.format(new Date()) + "]");
			FileDataSource fds = new FileDataSource(filename);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());
			part.addBodyPart(mbp1);
			part.addBodyPart(mbp2);
			message.setContent(part);
			Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static String getMailBodyHtml() {
		String finalContent = "";
		Map<String, Map<String, Integer>> mailBody = new HashMap<String, Map<String, Integer>>();
		Set<String> projects = new HashSet<String>();
		for (SuiteConfig config : SeleniumDriverFactory.getSuiteConfig()) {
			projects.add(config.getProject());
		}
		for (String entry : projects) {
			Map<String, Integer> mailCnt = new HashMap<String, Integer>();
			int passCount = 0;
			int failCount = 0;
			int skipCount = 0;
			for (SuiteConfig config : SeleniumDriverFactory.getSuiteConfig()) {
				if (entry.equals(config.getProject())) {
					if (config.getStatus().equals("Passed"))
						passCount = passCount + 1;
					else if (config.getStatus().equals("Failed"))
						failCount = failCount + 1;
					else if (config.getStatus().equals("No"))
						skipCount = skipCount + 1;
				}
			}
			mailCnt.put("passed", passCount);
			mailCnt.put("failed", failCount);
			mailCnt.put("skipped", skipCount);
			mailCnt.put("total", passCount + failCount + skipCount);
			if (entry.equals("FLOW"))
				mailBody.put("HMY", mailCnt);
			else
				mailBody.put(entry, mailCnt);
		}
		for (Entry<String, Map<String, Integer>> key : mailBody.entrySet()) {
			String content = "<tr>"
					+ "<td style=\"padding: 8px;text-align: center;border: 1px solid black;border-collapse: collapse;\">"
					+ "Peoplenet "
					+ key.getKey()
					+ "</td>"
					+ "<td style=\"padding: 8px;text-align: center;border: 1px solid black;border-collapse: collapse;\">"
					+ key.getValue().get("passed")
					+ "</td>"
					+ "<td style=\"padding: 8px;text-align: center;border: 1px solid black;border-collapse: collapse;\">"
					+ key.getValue().get("failed")
					+ "</td>"
					+ "<td style=\"padding: 8px;text-align: center;border: 1px solid black;border-collapse: collapse;\">"
					+ key.getValue().get("skipped")
					+ "</td>"
					+ "<td style=\"padding: 8px;text-align: center;border: 1px solid black;border-collapse: collapse;\">"
					+ key.getValue().get("total") + "</td>" + "</tr>";
			finalContent += content;
		}
		return finalContent;
	}

	private static List<String> fileList = new ArrayList<String>();

	public static void sendReportMail(String from, String frmPass, String to) {
		String reportZip = PropertiesLoader.getProperty("testResultsDir")
				+ "/../qa-automation-"
				+ new SimpleDateFormat("MM-dd-yyyy-HH-mm").format(new Date())
				+ ".zip";
		String reportDir = PropertiesLoader.getProperty("testResultsDir");
		try {
			File file = new File(reportDir);
			log.info("Files To be Zipped under : " + file.getAbsolutePath());
			fileList.clear();
			generateFileList(file, file.getAbsolutePath());
			zipIt(reportZip, file.getAbsolutePath());
			Thread.sleep(1000);
			log.info("Sucessfully Zipped Reports in file : " + reportZip);
			log.info("Mail Sent Status : "
					+ sendMail(from, frmPass, to, reportZip));
			new File(reportZip).delete();
			File report = new File(file.getAbsolutePath() + "/report/");
			File screenshots = new File(file.getAbsolutePath()
					+ "/screenshots/");
			log.info("Path to Reports : " + report.getAbsolutePath());
			log.info("Path to Screenshots : " + screenshots.getAbsolutePath());
			report.delete();
			screenshots.delete();
		} catch (Exception e) {
			e.printStackTrace();
			new File(reportZip).delete();
		}
	}

	private static void zipIt(String zipFile, String source) {
		byte[] buffer = new byte[1024];
		try {
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (String file : fileList) {
				log.info("Adding to Zip File : " + file);
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);
				FileInputStream in = new FileInputStream(source
						+ File.separator + file);
				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				in.close();
			}
			zos.closeEntry();
			zos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param node
	 *            file or directory
	 */
	private static void generateFileList(File node, String source) {
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString(),
					source));
		}
		if (node.isDirectory()) {
			String[] subNote = node.list();
			if (node.getName().equals("video")
					|| node.getName().equals("test-output")) {
				log.debug("Dont't Add : " + node.getName());
			} else {
				for (String filename : subNote) {
					generateFileList(new File(node, filename), source);
				}
			}
		}
	}

	/**
	 * Format the file path for zip
	 * 
	 * @param file
	 *            file path
	 * @return Formatted file path
	 */
	private static String generateZipEntry(String file, String source) {
		return file.substring(source.length() + 1, file.length());
	}
}