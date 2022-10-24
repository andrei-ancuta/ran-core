package ro.uti.ran.core.ws.handlers.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import ro.uti.ran.core.ws.handlers.RanWsPayload;

/**
 * RequestWrapper for logging the message
 * @author mihai,plavichianu
 *
 */
public class WrapperRequest extends HttpServletRequestWrapper implements RanWsPayload {

	private WrapperInputStream is;

	public  WrapperRequest(HttpServletRequest request) throws IOException {
		super(request);
		this.is = new WrapperInputStream(request.getInputStream());
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return is;
	}

	public String getPayload() {
		return is.getPayload();
	}
	
	@Override
	public String toString() {
		return getPayload();
	}
	
	private class WrapperInputStream extends ServletInputStream {

		private StringBuffer bfr = new StringBuffer();
		private ServletInputStream is;
		
		public WrapperInputStream(ServletInputStream is) {
			super();
			this.is = is;
		}

		@Override
		public int read() throws IOException {
			int ch = is.read();
			if (ch != -1 && ch != 0) {
				bfr.append((char) ch);
			}
			return ch;
		}

		@Override
		public int read(byte[] b) throws IOException {
			int ch = is.read(b);
			if (ch != -1) {
				for (byte byteSingle : b)
					if(byteSingle != 0) {
						bfr.append((char) byteSingle);
					}
					
			}
			return ch;
		}

		@Override
		public int read(byte[] b, int o, int l) throws IOException {
			int ch = is.read(b, o, l);
			if (ch != -1) {
				for (byte byteSingle : b)
					if(byteSingle != 0) {
						bfr.append((char) byteSingle);
					}

			}
			return ch;
		}

		@Override
		public int readLine(byte[] b, int o, int l) throws IOException {
			int ch = is.readLine(b, o, l);
			if (ch != -1 && ch != 0) {
				bfr.append(b);
			}
			return ch;
		}

		public String getPayload() {
			return bfr.toString();
		}
		
		@Override
		public String toString() {
			return getPayload();
		}

		@Override
		public boolean isFinished() {
			return is.isFinished();
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener listener) {
			is.setReadListener(listener);
		}
	}
}


