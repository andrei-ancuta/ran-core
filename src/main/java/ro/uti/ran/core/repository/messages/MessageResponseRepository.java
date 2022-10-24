package ro.uti.ran.core.repository.messages;

import org.springframework.data.repository.CrudRepository;

import ro.uti.ran.core.model.messages.MessageResponseEntity;

/**
 * 
 * @author mihai.plavichianu
 *
 */
public interface MessageResponseRepository extends CrudRepository<MessageResponseEntity, Long> {

}
