package ro.uti.ran.core.repository.messages;

import org.springframework.data.repository.CrudRepository;

import ro.uti.ran.core.model.messages.MessageRequestEntity;

/**
 * 
 * @author mihai.plavichianu
 *
 */
public interface MessageRequestRepository extends CrudRepository<MessageRequestEntity, Long> {

}
