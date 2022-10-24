package ro.uti.ran.core.model.messages;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author mihai.plavichianu
 *
 */
@Entity
@Table(name = "MSG_REQUEST")
public class MessageRequestEntity extends MessageEntity {

    @Id
    @Column(name = "ID_MSG", nullable = false)
    private Long idMessage;

    @Override
    public Long getIdMessage() {
        return this.idMessage;
    }

    @Override
    public void setIdMessage(Long messageId) {
        this.idMessage = messageId;
    }

}
