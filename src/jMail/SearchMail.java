package jMail;

import java.io.File;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.MimeMultipart;


class SearchMail implements Runnable {

	static String d_email; 
	static String d_uname; 
	static String d_password; 
	String d_host; 
	String d_port; 
	static String imapHost; 
	static String imapPort; 
	static String keyword;
	
	public static void main(String[] args) throws Exception {
		d_uname = "name@iastate.edu"; // or @gmail.com
		d_email = d_uname; 
		d_password = "****"; 
		keyword = "Search Subject for this"; 
		Thread thread = new Thread(new SearchMail(d_uname, d_password, keyword)); 
		thread.start();
	}
	
	public SearchMail(String user, String pass, String key) throws Exception{
		d_email = user.trim();
		d_uname = user.trim();
		d_password = pass.trim();	
		d_host = "smtp.gmail.com";
		d_port  = "465"; //465,587
		imapHost = "imap.gmail.com"; 
		imapPort = "993"; 
		keyword = key; 
	}
	
	
	public static void searchSubject(String host, String port, String user, String  password, String keyword) throws Exception {
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
       Store store = null;
		   try {
	            store = session.getStore("imap");
	            store.connect(user, password);
		   }
     	   catch (MessagingException ex) {
	            System.out.println("Could not connect to the message store.");
	            ex.printStackTrace();
	        }
	            // opens the inbox folder
	            Folder folderInbox = store.getFolder("INBOX");
	            folderInbox.open(Folder.READ_ONLY);

	            FetchProfile profile = new FetchProfile();
	            profile.add(UIDFolder.FetchProfileItem.UID);
	            Message[] messages = folderInbox.getMessages();
//	            folderInbox.fetch(messages,profile);
	            
	            File output = new File("messages.txt"); 
	            PrintWriter writer = new PrintWriter(output); 
	            for(int i = 0;i < messages.length;i++)
	            {
	            	String body = ""; 
		            if(messages[i].getSubject() != null && messages[i].getSubject().contains(keyword))	{
		            	if(messages[i].isMimeType("text/plain")){
		            		body = messages[i].getContent().toString(); 
		            	}else if (messages[i].isMimeType("multipart/*")) {
		                    MimeMultipart mimeMultipart = (MimeMultipart) messages[i].getContent();
		                    body = getTextFromMimeMultipart(mimeMultipart);
		                }
		            	System.out.println("Found message #" + i + ": " + messages[i].getSubject());
		            	System.out.println("Body: " + body);
		            	writer.println("Found message #" + i + ": " + messages[i].getSubject());
		            	writer.println("Body: " + body); 
		            }	            		            
	            }	            
	            // disconnect
	            writer.flush();
	            writer.close();
	            folderInbox.close(false);
	            store.close();
	        } 
//		   catch (NoSuchProviderException ex) {
//	            System.out.println("No provider.");
//	            ex.printStackTrace();
//	        }
	
	    
	
	private static String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart) throws Exception{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; // without break same text appears twice in my tests
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub	
		try {
			searchSubject("imap.gmail.com", "993", d_uname, d_password, keyword ); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Failed in run");
		}
		
	}
	
}
