package ro.uti.ran.core.service.messages;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ro.uti.ran.core.common.BeanUtils;
import ro.uti.ran.core.exception.MessageStoreException;
import ro.uti.ran.core.exception.codes.MessageStoreCodes;
import ro.uti.ran.core.messages.MessageData;
import ro.uti.ran.core.messages.MessageRequestData;
import ro.uti.ran.core.messages.MessageResponseData;
import ro.uti.ran.core.model.messages.MessageRequestEntity;
import ro.uti.ran.core.model.messages.MessageResponseEntity;
import ro.uti.ran.core.model.messages.OperationEntity;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.repository.messages.MessageRequestRepository;
import ro.uti.ran.core.repository.messages.MessageResponseRepository;
import ro.uti.ran.core.repository.messages.OperationRepository;
import ro.uti.ran.core.service.parametru.ParametruService;

/**
 * 
 * @author mihai.plavichianu
 *
 */
@Service
public class MessageServiceImpl implements MessageService {

	private static final Log logger = LogFactory.getLog(MessageServiceImpl.class);

	@Autowired
	private MessageRequestRepository messageRequestRepository;

	@Autowired
	private OperationRepository operationRepository;

	@Autowired
	private MessageResponseRepository messageResponseRepository;

	@Autowired
	private MessageRepositoryServiceImplHelper messageRepositoryServiceImplHelper;
	
	@Autowired
	private BeanUtils beanUtils;
	
    @Autowired
    private ParametruService parametruService;
	
	private String getMessagePath() {
		return getParametru(RanConstants.MESSAGE_PATH, "ran_message_store");
	}
	
	private boolean getMessageStoreLocal() {
		return Boolean.valueOf(getParametru(RanConstants.MESSAGE_STORE_LOCAL, "false"));
	}
	
	private int getMessageFaultStackTraceNbLines() {
		return Integer.valueOf(getParametru(RanConstants.MESSAGE_FAULT_STACKTRACE_NBLINES, "100"));
	}
	
	private String getParametru(String name, String defaultValue) {
		try {
			return parametruService.getParametru(name).getValoare();
		} catch(Exception e) {
			logger.error("Parametrul \"" + name + "\" nu putut fi gasit!", e);
			return defaultValue;
		}
	}
	
	@Override
	public Long storeMessageRequest(MessageRequestData messageRequest) throws MessageStoreException {
		OperationEntity operation = new OperationEntity();
		operation.setOperationTime(new Date());
		operation.setIpAddress(messageRequest.getIpAddress());
		operation.setWsdlServiceName(messageRequest.getWsdlServiceName());
		operation.setWsdlOperationName(messageRequest.getWsdlOperationName());
		operation.setOperationType(messageRequest.getRanOperationType());

		beanUtils.setProperty(operation, "hostName", messageRequest.getHostName());

		operation = operationRepository.save(operation);

		MessageRequestEntity msgReq = new MessageRequestEntity();

		// salveaza continutul mesajului pe disk
		String contentPath = saveFileToDisk(new SoapData(messageRequest), operation, "req", null, messageRequest.getHttpHeaders());
		logger.debug("msg req saved to: " + contentPath);

		msgReq.setContentPath(contentPath);
		msgReq.setIdMessage(operation.getIdOperation());
		
		msgReq = messageRequestRepository.save(msgReq);
		return msgReq.getIdMessage();
	}

	@Override
	public Long storeMessageResponse(MessageResponseData messageResponse, Long idMessageRequest) throws MessageStoreException {
		
		if(messageResponse == null) {
			return 0L;
		}
		
		MessageResponseEntity msgRsp = new MessageResponseEntity();
		msgRsp.setResponseDate(new Date());
		msgRsp.setIdMessage(idMessageRequest);

		String contentPath = "";
		String msgSuffix;
		String soapData = null;

		if (null == messageResponse.getFault()) {
			msgSuffix = "rsp";
		} else {

			beanUtils.setProperty(msgRsp, "faultCode", messageResponse.getFault().getFaultCode());
			beanUtils.setProperty(msgRsp, "faultMsg", messageResponse.getFault().getFaultMessage());
			beanUtils.setProperty(msgRsp, "faultStackTrace",
					beanUtils.limitStackTrace(messageResponse.getFault().getFaultStackTrace(), getMessageFaultStackTraceNbLines()));

			msgSuffix = "fault";

			soapData = buildSoapFaultData(messageResponse);
		}

		// salveaza continutul mesajului pe disk)
		
		OperationEntity operation = operationRepository.findOne(idMessageRequest);
		contentPath = saveFileToDisk(new SoapData(messageResponse), operation, msgSuffix, null, null);
		logger.debug("msg " + msgSuffix + " saved to: " + contentPath);
		
		msgRsp.setContentPath(contentPath);
		if (soapData != null) {
			saveFileToDisk(new SoapData(soapData), operation, msgSuffix, ".trace", null);
		}

		msgRsp = messageResponseRepository.save(msgRsp);
		return msgRsp.getIdMessage();
	}

	private String saveFileToDisk(SoapData soapData, OperationEntity operation, String suffix, String extension,
			Map<String, List<String>> httpHeaders) throws MessageStoreException {

		// Calea catre user home este independenta de sistemul de operare si este folosita doar daca mesajele sunt salvate local
		// pe UNIX e /Users/username
		// pe WINDOWS e C:Users\\username
		String userDir = System.getProperty("user.home");

		Calendar cal = Calendar.getInstance();
		cal.setTime(operation.getOperationTime());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		if (extension == null) {
			extension = ".xml";
		}

		String fileName = new StringBuilder(getMessageStoreLocal() ? userDir : "").append(IOUtils.DIR_SEPARATOR).append(getMessagePath())
				.append(IOUtils.DIR_SEPARATOR).append(year).append(IOUtils.DIR_SEPARATOR).append(month).append(IOUtils.DIR_SEPARATOR)
				.append(day).append(IOUtils.DIR_SEPARATOR).append(operation.getWsdlServiceName()).append(IOUtils.DIR_SEPARATOR)
				.append(operation.getWsdlOperationName()).append(IOUtils.DIR_SEPARATOR).append(cal.getTimeInMillis()).append("-")
				.append(suffix).append(extension).toString();

		// Calea o sa fie de forma : calea_catre_user/an/luna/zi/nume_service_WSDL/nume_operatie_WSDL/timestamp.xml

		// Salveaza asincron mesajul pe disk
		messageRepositoryServiceImplHelper.saveFile(soapData, fileName, httpHeaders);

		return fileName;
	}

	private String buildSoapFaultData(MessageResponseData messageResponse) {
		StringBuilder sb = new StringBuilder();
		sb.append("FAULT RESPONSE\n");
		sb.append("-------------------------------\n");
		sb.append("FAULT CODE: ");
		sb.append(messageResponse.getFault().getFaultCode());
		sb.append("\n");

		sb.append("FAULT MESSAGE: ");
		sb.append(messageResponse.getFault().getFaultMessage());
		sb.append("\n");

		sb.append("STACK TRACE:\n\n");
		sb.append(ExceptionUtils.getFullStackTrace(messageResponse.getFault().getFaultStackTrace()));

		return sb.toString();
	}

	/**
	 * Trebuie creata o clasa separata pentru @Async
	 * 
	 * @author mihai.plavichianu
	 * 
	 */
	@Component
	public static class MessageRepositoryServiceImplHelper {

		@Async
		private Future<String> saveFile(SoapData soapData, String fileName) throws MessageStoreException {
			File file = new File(fileName);
			try {
				FileUtils.writeStringToFile(file, soapData.toString());
			} catch (IOException e) {
				logger.error("Error: ", e);
				throw new MessageStoreException(MessageStoreCodes.SAVE_MESSAGE, fileName);
			}
			return new AsyncResult<String>(fileName);
		}

		@Async
		private Future<String> saveFile(SoapData soapData, String fileName, Map<String, List<String>> httpHeaders)
				throws MessageStoreException {

			String soapMessage = soapData.toString();
			
			if (httpHeaders != null && httpHeaders.entrySet().size() > 0) {
				soapMessage = getHttpString(httpHeaders) + soapMessage;
			}

			return saveFile(new SoapData(soapMessage), fileName);
		}

		private String getHttpString(Map<String, List<String>> httpHeaders) {

			StringBuilder sb = new StringBuilder();
			sb.append("HTTP HEADERS:\n");

			for (String header : httpHeaders.keySet()) {
				sb.append(header + ":" + httpHeaders.get(header) + "\n");
			}

			sb.append("\n");
			return sb.toString();
		}
	
	}
	
	private static class SoapData {
		
		private MessageData messageData;
		private String message;
		
		public SoapData(MessageData messageData) {
			setMessageData(messageData);
		}
		
		public SoapData(String message) {
			setMessage(message);
		}
		
		private void setMessageData(MessageData messageData) {
			this.messageData = messageData;
		}
		
		private void setMessage(String message) {
			this.message = message;
		}
		
		private void build() {
			if(messageData == null) {
				message = "";
			} else {
				message = messageData.getSoapMessage();
				messageData = null;
			}
		}
		
		@Override
		public String toString() {
			if(message == null) {
				build();
			}
			
			return message;
		}
		
	}

}
