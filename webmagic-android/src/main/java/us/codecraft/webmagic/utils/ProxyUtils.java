package us.codecraft.webmagic.utils;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import us.codecraft.webmagic.proxy.Proxy;

/**
 * Pooled Proxy Object
 * 
 * @author yxssfxwzy@sina.com <br>
 * @since 0.5.1
 */

public class ProxyUtils {

	private static final String TAG = "ProxyUtils";

	public static boolean validateProxy(Proxy p) {
		Socket socket = null;
		try {
			socket = new Socket();
			InetSocketAddress endpointSocketAddr = new InetSocketAddress(p.getHost(), p.getPort());
			socket.connect(endpointSocketAddr, 3000);
			return true;
		} catch (IOException e) {
			LogPrinter.w(TAG, "FAILRE - CAN not connect!  remote: " + p);
			return false;
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					LogPrinter.e(TAG, "Error occurred while closing socket of validating proxy" + e);
				}
			}
		}

	}

}
