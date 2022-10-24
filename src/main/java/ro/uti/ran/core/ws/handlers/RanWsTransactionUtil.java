package ro.uti.ran.core.ws.handlers;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public abstract class RanWsTransactionUtil {

	
	private static boolean isWebSphere = false;
	
	static {
		;
		try {
			InitialContext ic  = new InitialContext();
			String serverName = ic.lookup("servername").toString();
			
			System.out.println("**********%%%*************" + serverName);
		} catch (NamingException e) {
			
		}
		
	}
	
	public static UserTransaction getUserTransaction() throws Exception {
		
		if(isWebSphere) {
			try {
				InitialContext ctx = new InitialContext();
				return (UserTransaction) ctx.lookup("java:comp/UserTransaction");
			} catch (NamingException e) {
				e.printStackTrace();
				throw e;
			}
		}
		
		return new UserTransaction() {
			
			@Override
			public void setTransactionTimeout(int arg0) throws SystemException {}
			
			@Override
			public void setRollbackOnly() throws IllegalStateException, SystemException {}
			
			@Override
			public void rollback() throws IllegalStateException, SecurityException,
					SystemException {}
			
			@Override
			public int getStatus() throws SystemException {return 0;}
			
			@Override
			public void commit() throws HeuristicMixedException,
					HeuristicRollbackException, IllegalStateException,
					RollbackException, SecurityException, SystemException {}
			
			@Override
			public void begin() throws NotSupportedException, SystemException {}
		};
		
	}
	
	public static void rollback(UserTransaction ut) throws Exception {
		
		if(!isWebSphere) {
			return;
		}
		
		try {
			if (null != ut && Status.STATUS_NO_TRANSACTION != ut.getStatus()) {
				ut.rollback();
			}
		} catch (SystemException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
