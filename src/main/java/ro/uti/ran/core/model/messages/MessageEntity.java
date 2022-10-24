package ro.uti.ran.core.model.messages;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 
 * @author mihai.plavichianu
 *
 */
@MappedSuperclass
public abstract class MessageEntity implements Serializable {
    @Column(name = "CONTENT_PATH")
    private String contentPath;

    public abstract Long getIdMessage();

    public abstract void setIdMessage(Long idMessage);

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }
}
