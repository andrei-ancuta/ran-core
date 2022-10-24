package ro.uti.ran.core.repository.messages;

import org.springframework.data.repository.CrudRepository;

import ro.uti.ran.core.model.messages.OperationEntity;


/**
 * 
 * @author mihai.plavichianu
 *
 */
public interface OperationRepository extends CrudRepository<OperationEntity, Long> {
}
