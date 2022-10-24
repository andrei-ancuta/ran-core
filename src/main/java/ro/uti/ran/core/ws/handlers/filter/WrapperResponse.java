package ro.uti.ran.core.ws.handlers.filter;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import ro.uti.ran.core.ws.handlers.RanWsPayload;

/**
 * ResponseWrapper for logging the message
 * @author mihai,plavichianu
 *
 */
public class WrapperResponse extends HttpServletResponseWrapper implements RanWsPayload {

	private WrapperOutputStream os;
	private String msgId = null;

	public  WrapperResponse(HttpServletResponse response) throws IOException {
		super(response);
		this.os = new WrapperOutputStream(response.getOutputStream());
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return os;
	}

	public String getPayload() {
		return os.getPayload();
	}
	
	@Override
	public String toString() {
		return getPayload();
	}
	
	public void setContent(String content) {
		os.setContent(content);
	}
	
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	public String getMsgId() {
		return msgId;
	}
	
	private class WrapperOutputStream extends ServletOutputStream {

		private StringBuffer bfr = new StringBuffer();
		private ServletOutputStream os;
		private String forcedOutput = null;
		
		public WrapperOutputStream(ServletOutputStream os) {
			super();
			this.os = os;
		}

		public void setContent(String content) {
			forcedOutput = content;
		}
		
		
		public String getPayload() {
			if(forcedOutput != null) {
				return forcedOutput;
			}
			
			return bfr.toString();
		}


		@Override
		public boolean isReady() {
			return os.isReady();
		}
		
		@Override
		public String toString() {
			return getPayload();
		}


		@Override
		public void setWriteListener(WriteListener listener) {
			os.setWriteListener(listener);
		}


		@Override
		public void write(int b) throws IOException {
			os.write(b);
			if (b != -1 && b != 0) {
				bfr.append((char) b);
			}
		}
		
		@Override
		public void write(byte[] b) throws IOException {
			os.write(b);
			for (byte byteSingle : b) {
				if(byteSingle != 0) {
					bfr.append((char) byteSingle);
				}
			}
			
		}
		
		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			os.write(b, off, len);
			int i=0;
			for (byte byteSingle : b) {
				if(i == len) {
					return;
				}
				if(byteSingle != 0) {
					bfr.append((char) byteSingle);
				}
				i++;
			}
		}

	}
	
}




