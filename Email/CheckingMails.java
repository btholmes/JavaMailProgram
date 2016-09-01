package Email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;

class CheckingMails {
	static Session session; 
	
	public static void main(String[] args) throws MessagingException{
			
		//String d_email = "email@domain.com";
		String d_email = "email@domain.com";
		//String d_uname = "email@domain.com";
		String d_uname = "email@domain.com";
		//String d_password = "****";
		String d_password = "*****";	
		String d_host = "smtp.gmail.com";
		String d_port  = "465"; //465,587
		String m_to = "email@domain.com";
		String m_subject = "Testing2";
		String m_text = "Hey, this is the testing email."; 
		
		
		
		
		connect(d_email, d_host, d_port); 
		MimeMessage msg = createMail(m_text, m_subject, d_email, m_to); 
		sendMail(d_host, d_uname, d_password, msg); 
		
//		connect(d_email, d_host, d_port); 

		String imapHost = "imap.gmail.com"; 
		String imapPort = "993"; 
		searchSubject(imapHost, imapPort, "email@domain.com", "****", "SearchKeyword"); 

	}
	
	public static void connect(String email, String host, String port){
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
		
		auth auth = new auth();
		session = Session.getInstance(props, auth);
		session.setDebug(true);
		
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
		Transport transport = session.getTransport("smtps");
		transport.connect(host, 465, username, password);
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();
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
}
	
class auth extends Authenticator
{
    public PasswordAuthentication getPasswordAuthentication()
    {
//        return new PasswordAuthentication("email@domain.com", "*****");
        return new PasswordAuthentication("email@domain.com", "******");

    }
}