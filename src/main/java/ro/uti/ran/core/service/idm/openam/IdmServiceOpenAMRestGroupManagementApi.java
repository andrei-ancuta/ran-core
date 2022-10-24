package ro.uti.ran.core.service.idm.openam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ro.uti.ran.core.ws.internal.parametru.ParametruService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Managementul utilizatorilor in grup
 * @author mihai.plavichianu
 *
 */
@Component
class IdmServiceOpenAMRestGroupManagementApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdmServiceOpenAMRestGroupManagementApi.class);
	
    private static final String _OPEN_AM_GROUPS_PROP = "openam.ran.groups";
    private static final ObjectMapper _JSON_MAPPER = new ObjectMapper();
	private String _URL_ADD_USER_ON_GROUP = null;
	private String _URL_GET_USER = null;
	
	private static final Map<String, Lock> _GROUP_LOCKS = new HashMap<String, Lock>();
	
    @Autowired
    private ParametruService parametruService;
    
    @Autowired
    protected RestTemplate restTemplate;
    
    @Autowired
    protected Environment env;
	
	private boolean containsUser(ArrayNode users, String user) {
		
		if(users == null) {
			return false;
		}
		
		JsonNode node = null;
		for(int i=0; i<users.size(); i++) {
			node = users.get(i);
			if(node.textValue().contains(user)) {
				return true;
			}
		}
		
		return false;
	}
	
 	protected void addUserToParametruGroups(String user, HttpHeaders headers, boolean newUser) {
    	try {
    		addUserToGroups(user, headers, newUser, getGroups());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	    
    private void addUserToGroups(final String user, final HttpHeaders headers, final boolean newUser, String... groups) {
    	
    	List<Thread> requests = new ArrayList<Thread>();
    	
    	for(final String group : groups) {
    		
    		requests.add(
    			new Thread(new Runnable() {
				
					@Override
					public void run() {
						
						if(group.trim().isEmpty()) {
							return;
						}
						
						LOGGER.debug("Adaugare user " + user + " la grup " + group);
			            acquireLock(group);
			    		addUserToGroup(user, headers, group.trim(), newUser);
			    		releaseLock(group);
					}
				}) {{
					setName(group);
					start();
				}}
    		);
    	}
    	
    	for(Thread t : requests) { 
    		try {
    			t.join();
    		} catch (InterruptedException e) {
    			LOGGER.debug("Adaugare userului " + user + " la grupul " + t.getName() + " a esuat");
    			e.printStackTrace();
    		} 
    	}
    }
	    
    private void addUserToGroup(String user, HttpHeaders headers, String group, boolean newUser) {
    	
    	ResponseEntity<String> response = null;
        String responseBody = null;
        
    	HttpEntity<String> entity = new HttpEntity<String>(null, headers);
    	
    	try {
    		
    		user = getUserDN(user, headers); 
    		
            response = restTemplate.exchange(_URL_ADD_USER_ON_GROUP + group, HttpMethod.GET, entity, String.class);
            responseBody = response.getBody();
            JsonNode groupJson = _JSON_MAPPER.readTree(responseBody);
            //openAM-11 fix
            ArrayNode usersInGroup = groupJson.get("uniquemember") != null ? (ArrayNode) groupJson.get("uniquemember") : (ArrayNode) groupJson.get("uniqueMember");
            
            if(!newUser && containsUser(usersInGroup, user)) {
            	return;
			}
            
            if(usersInGroup == null) {
            	usersInGroup = new ArrayNode(_JSON_MAPPER.getNodeFactory());
            }
            
            usersInGroup.add(user);
            
 			entity = new HttpEntity<String>("{\"uniquemember\": " + usersInGroup + "}", headers);
 			LOGGER.debug("Request adaugare user la grup {}", entity.toString());
 			
 			response = restTemplate.exchange(_URL_ADD_USER_ON_GROUP + group, HttpMethod.PUT, entity, String.class);
 			
 			LOGGER.debug("Rezultat adaugare user la grup {}", response.getBody());
 			
        } catch(Exception e){
        	e.printStackTrace();
        	return;
        }
    	 
    }
    
    private String[] getGroups() {
    	try {
    		if(parametruService.getParametru(_OPEN_AM_GROUPS_PROP) != null && parametruService.getParametru(_OPEN_AM_GROUPS_PROP).getValoare() != null) {
    			return parametruService.getParametru(_OPEN_AM_GROUPS_PROP).getValoare().split(",");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return new String[0];
    }
    
    private String getUserDN(String user, HttpHeaders headers) {
    	ResponseEntity<String> response = null;
        String responseBody = null;
        
    	HttpEntity<String> entity = new HttpEntity<String>(null, headers);
    	
    	try {
            response = restTemplate.exchange(_URL_GET_USER + user, HttpMethod.GET, entity, String.class);
            responseBody = response.getBody();
            JsonNode dnJson = _JSON_MAPPER.readTree(responseBody);
            return ((ArrayNode) (dnJson.get("dn"))).get(0).textValue();
    	} catch(Exception e) {}
    	
    	return null;
    }
	    
    @PostConstruct
    private void init() {
    	_URL_ADD_USER_ON_GROUP = env.getProperty("openam.add-user-on-group");
    	_URL_GET_USER = env.getProperty("openam.get-user");
    	
    	try {
        	for(String group : getGroups()) {
        		_GROUP_LOCKS.put(group, new ReentrantLock());
        	}
    	} catch(Exception e) {
    		LOGGER.debug("Nu a fost configurat nici un grup");
    	}
    	
    }
    
    private void acquireLock(String group) {
    	if(!_GROUP_LOCKS.containsKey(group)) {
    		return;
    	}
    	_GROUP_LOCKS.get(group).lock();
    }
    
    private void releaseLock(String group) {
    	if(!_GROUP_LOCKS.containsKey(group)) {
    		return;
    	}
    	_GROUP_LOCKS.get(group).unlock();
    }
	
}
