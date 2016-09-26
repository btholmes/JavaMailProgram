package jMail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

public class SendMail implements Runnable {
	static Session session; 
	String m_to; 
	String m_subject; 
	String m_text; 
	String d_host; 
	String d_port; 
	String imapHost; 
	String imapPort; 
	static String d_email; 
	static String d_uname; 
	static String d_password; 
	static String attachment; 
	static boolean attached = true; 
	static boolean authenticated; 
	static boolean recipient; 
	static boolean fileExists; 

	
	public SendMail() throws FileNotFoundException{
		
		//String d_email = "email@domain.com";
				d_email = "btholmes@iastate.edu";
		//String d_uname = "email@domain.com";
				d_uname = "btholmes@iastate.edu";
		//String d_password = "****";
				d_password = "Cloudatlas743";	
				d_host = "smtp.gmail.com";
				d_port  = "465"; //465,587
				
		//This is the person who will receive the email
				m_to = "klkost21@iastate.edu"; 
				m_subject = "House Attendance";
				
				m_text = createText(); 
				attachment = "/Users/btholmes/Downloads/HSBexcel.xlsx"; 

				imapHost = "imap.gmail.com"; 
				imapPort = "993"; 
//				searchSubject(imapHost, imapPort, "email@domain.com", "****", "SearchKeyword"); 
		
	}
	private static String createText() throws FileNotFoundException {
		// TODO Auto-generated method stub
		String result = "Hey, \n \n" + "Here are the house attendance results:\n\n"; 
		File text = new File("/Users/btholmes/Documents/workspace/Honors/SendMe.txt"); 
		Scanner in = new Scanner(text); 
		while(in.hasNextLine()){
			result += in.nextLine() + "\n"; 
			
		}
		
		result += "\n Best!\n"; 
		return result;
	}
	
	public SendMail(String destination, String msg, String subject, String item, String user, String pass){

		//String d_email = "email@domain.com";
		d_email = user;
		//String d_uname = "email@domain.com";
		d_uname = user;
		//String d_password = "****";
		d_password = pass;	
		d_host = "smtp.gmail.com";
		d_port  = "465"; //465,587
		
		//This is the person who will receive the email
		m_to = destination; 
		m_subject = subject;
		
		m_text = msg; 
				
		attachment = item.trim(); 
		if(attachment.length() <= 0){
			attached = false; 
		}

		imapHost = "imap.gmail.com"; 
		imapPort = "993"; 
		recipient = checkDestination(msg, subject); 

		
//		searchSubject(imapHost, imapPort, "email@domain.com", "****", "SearchKeyword"); 

	}


	public boolean checkDestination(String message, String subject){
		connect(d_email, d_host, d_port); 		
		MimeMessage emailThis;
		try {
			emailThis = createMail(message, subject, d_email, m_to);
			addAttachment(emailThis);
			sendMail(d_host, d_uname, d_password, emailThis);
		} catch (MessagingException e) {
			return false; 
		} 		
		return true; 
	}
	
	public SendMail(String user, String pass) throws MessagingException{
		String host = "imap.gmail.com"; 
		connect(user, host, "465"); 
		authenticated = checkConnection(host, user, pass); 
	}
	
	
	public static boolean checkConnection(String host, String username, String password) throws MessagingException{
			Properties props = System.getProperties();
		    props.setProperty("mail.store.protocol", "imaps");
		try {
	         Session session = Session.getDefaultInstance(props, null);
	         Store store = session.getStore("imaps");
	         store.connect("imap.gmail.com", username, password);
	    } catch (AuthenticationFailedException e) {
	        return false; 
	    }
	    return true; 
	}
	
	public static void connect(String email, String host, String port){
		try{
			Properties props = new Properties();
			props.put("mail.smtp.user", email);
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.port", port);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			
			auth auth = new auth(d_email, d_password);
			session = Session.getInstance(props, auth);
			session.setDebug(true);
		}
		catch(Exception e){
			System.out.println("Exception caught in connect");
		}

	}
	
	private static void addAttachment(MimeMessage msg) throws MessagingException {
//		 TODO Auto-generated method stub
		if(attached){
		      MimeBodyPart messageBodyPart = new MimeBodyPart();
//		      String filename = "/Users/btholmes/Downloads/HSBexcel.xlsx";
//		      String filename = attachment;
		      fileExists = checkFile(attachment); 
		      if(fileExists){ 
			      DataSource source = new FileDataSource(attachment);
			      messageBodyPart.setDataHandler(new DataHandler(source));
			      messageBodyPart.setFileName(attachment);
			      Multipart multipart = new MimeMultipart();
			      multipart.addBodyPart(messageBodyPart);
			      msg.setContent(multipart);
		      }
		}

	}

	
	private static boolean checkFile(String path) {
		// TODO Auto-generated method stub
			File file = new File(path); 
			System.out.println("File is " + file.exists());
		return file.exists();
	}
	
	public static MimeMessage createMail(String text, String subject, String email, String destination) throws MessagingException{
		MimeMessage msg = new MimeMessage(session);
		msg.setText(text);
		msg.setSubject(subject);
		msg.setFrom(new InternetAddress(email));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
				
		return msg; 
	}
	
	public static void sendMail(String host, String username, String password, MimeMessage msg) throws MessagingException{
		try{
			Transport transport = session.getTransport("smtps");
			transport.connect(host, 465, username, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		}
		catch(AuthenticationFailedException e){
			System.out.println("Failed to Authenticate that user and pass");
		}
	}

	
	public static void searchSubject(String host, String port, String username, String  password, String keyword) {
		 // server setting
		
		Properties properties = new Properties();
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", port);
 
        // SSL setting
        properties.setProperty("mail.imap.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback", "false");
        properties.setProperty("mail.imap.socketFactory.port",
                String.valueOf(port));
 
        Session session = Session.getDefaultInstance(properties);
		   try {
	            // connects to the message store
	            Store store = session.getStore("imap");
	            store.connect(username, password);
	 
	            // opens the inbox folder
	            Folder folderInbox = store.getFolder("INBOX");
	            folderInbox.open(Folder.READ_ONLY);
	 
	            // creates a search criterion
	            SearchTerm searchCondition = new SearchTerm() {
	                @Override
	                public boolean match(Message message) {
	                    try {
	                        if (message.getSubject() != null && message.getSubject().contains(keyword)) {
	                        	return true;
	                        }
	                    } catch (MessagingException ex) {
	                        ex.printStackTrace();
	                    }
	                    return false;
	                }
	            };
	 
	            // performs search through the folder
	            Message[] foundMessages = folderInbox.search(searchCondition);
	 
	            for (int i = 0; i < foundMessages.length; i++) {
	                Message message = foundMessages[i];
	                String subject = message.getSubject();
	                System.out.println("Found message #" + i + ": " + subject);
	            }
	 
	            // disconnect
	            folderInbox.close(false);
	            store.close();
	        } 
//		   catch (NoSuchProviderException ex) {
//	            System.out.println("No provider.");
//	            ex.printStackTrace();
//	        }
		   catch (MessagingException ex) {
	            System.out.println("Could not connect to the message store.");
	            ex.printStackTrace();
	        }
	    }

	@Override
	public void run(){
		// TODO Auto-generated method stub	
		
		connect(d_email, d_host, d_port); 		
		MimeMessage emailThis;
		try {
			emailThis = createMail(m_text, m_subject, d_email, m_to);
			addAttachment(emailThis);
			sendMail(d_host, d_uname, d_password, emailThis);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}

class auth extends Authenticator
{
	String userName; 
	String passWord; 
	public auth(String user, String pass){
		userName = user; 
		passWord = pass; 
	}
    public PasswordAuthentication getPasswordAuthentication()
    {
//        return new PasswordAuthentication("email@domain.com", "*****");
        return new PasswordAuthentication(userName, passWord);

    }
}