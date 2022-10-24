package ro.uti.ran.core.service.sistem;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sun.istack.ByteArrayDataSource;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Sache on 12/15/2015.
 */
@Component
@Transactional
public class MailServiceImpl implements MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private Environment environment;

    private static final String MESSAGE_TAG = "{{message}}";
    private static final String RAN_PORTAL_URL_TAG = "{{ranPortalUrl}}";
    private static String template = MESSAGE_TAG;
    private static ByteArrayDataSource logo = null;
    
    static {
    	try {
        	InputStream stream = MailServiceImpl.class.getClassLoader().getResourceAsStream("mail/template.html");
			byte[] content = new byte[stream.available()];
			stream.read(content);
			template = new String(content, "UTF-8");
			
			stream = MailServiceImpl.class.getClassLoader().getResourceAsStream("mail/logo.jpg");
			content = new byte[stream.available()];
			stream.read(content);
			logo = new ByteArrayDataSource(content, "image/jpg"); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    public void sendEmail(List<String> to, String subject, String content) {
        sendEmail(environment.getProperty("mail.from"), to, subject, content, false, false);
    }

    public void sendEmail(List<String> to, String subject, String content, boolean isMultipart, boolean isHtml) {
        sendEmail(environment.getProperty("mail.from"), to, subject, content, isMultipart, isHtml);
    }

    public void sendEmail(List<String> to, String subject, String content, boolean isMultipart, boolean isHtml, byte[] atasament) {
        sendEmail(environment.getProperty("mail.from"), to, subject, content, isMultipart, isHtml, atasament);
    }

    public void sendEmail(String from, List<String> to, String subject, String content, boolean isMultipart, boolean isHtml) {
        sendEmail(from, to, subject, content, isMultipart, isHtml, null);
    }

    @Override
    public void sendEmail(String from, List<String> to, final String subject, String content, boolean isMultipart, boolean isHtml, byte[] atasament) {
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            content = template.replace(MESSAGE_TAG, content);
            String ranPortlaUrl = environment.getProperty("ran.portal.url");
            if (ranPortlaUrl == null || ranPortlaUrl.isEmpty()) {
                ranPortlaUrl = "https://ran.ancpi.ro";
            }
            content = content.replace(RAN_PORTAL_URL_TAG, ranPortlaUrl);

            for (String recipient : to) {
                if (log.isDebugEnabled()) {
                    log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart,
                            isHtml, recipient, subject, content);
                }

                isMultipart = true;
                final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, "UTF-8");
                message.setFrom(new InternetAddress(from, environment.getProperty("mail.from.alias", "ran-sistem@ancpi.ro"), "UTF-8"));
                message.setTo(recipient);
                message.getMimeMessage().setSubject(subject, "UTF-8");

                message.setText(content, isHtml || containsHTMLCode(content));

                addRanLogo(message);
                
                if (atasament != null) {
                    final MagicMatch fileTypeMatch = Magic.getMagicMatch(atasament);


                    Resource resource = new ByteArrayResource(atasament) {
                        @Override
                        public String getFilename() {
                            if (fileTypeMatch == null) {
                                return subject;
                            }
                            return subject + "." + fileTypeMatch.getExtension();
                        }
                    };
                    message.addAttachment(resource.getFilename(), new ByteArrayResource(atasament));
                }


                javaMailSender.send(mimeMessage);
            }
        } catch (Throwable t) {
            log.error("Eroare la trimiterea mailului!", t);
        }
    }
    
    private void addRanLogo(MimeMessageHelper message) {
		
    	if(logo == null) {
    		return;
    	}
    	
    	try {
    		
    		MimeBodyPart logoPart = new MimeBodyPart();
    		logoPart.setDataHandler(new DataHandler(logo));
    		logoPart.setFileName("logo.jpg");
    		
    		logoPart.setContentID("<" + "logo" + ">");
    		logoPart.setDisposition(MimeBodyPart.INLINE);
    		
    		message.getMimeMultipart().addBodyPart(logoPart);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private boolean containsHTMLCode(String message) {
    	if(message.contains("&#")) {
    		return true;
    	}
    	
    	return false;
    }
    
}
