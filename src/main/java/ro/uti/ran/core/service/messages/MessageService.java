package ro.uti.ran.core.service.messages;

import ro.uti.ran.core.exception.MessageStoreException;
import ro.uti.ran.core.messages.MessageRequestData;
import ro.uti.ran.core.messages.MessageResponseData;

/**
 * 
 * @author mihai.plavichianu
 *
 */
public interface MessageService {

	/**
    *
    * @param messageRequest
    * @return Message request identifier
    */
   Long storeMessageRequest(MessageRequestData messageRequest) throws MessageStoreException;

   /**
    *
    * @param messageResponse
    * @param idMessageRequest
    * @return message response identifier
    */
   Long storeMessageResponse(MessageResponseData messageResponse, Long idMessageRequest) throws MessageStoreException;
   
}
