package ro.uti.ran.core.service.sistem;

import java.util.List;

/**
 * Created by Sache on 12/15/2015.
 */
public interface MailService {

    void sendEmail(List<String> to, String subject, String content);
    void sendEmail(List<String> to, String subject, String content, boolean isMultipart, boolean isHtml);
    void sendEmail(List<String> to, String subject, String content, boolean isMultipart, boolean isHtml, byte[] atasament);
    void sendEmail(String from, List<String> to, String subject, String content, boolean isMultipart, boolean isHtml);
    void sendEmail(String from, List<String> to, String subject, String content, boolean isMultipart, boolean isHtml, byte[] atasament);
}
