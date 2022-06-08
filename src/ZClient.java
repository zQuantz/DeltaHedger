import java.io.IOException;

import javax.swing.SwingUtilities;

import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;

public class ZClient {

    private EJavaSignal signal = new EJavaSignal();
    private ZWrapper wrapper = new ZWrapper();
    private EClientSocket socket = new EClientSocket(wrapper, signal);
    
    private void run() {

        socket.eConnect("localhost", 8777, 0);

        final EReader reader = new EReader(socket, signal);
        reader.start();
        
        new Thread(() -> {
            while (socket.isConnected()) {
                signal.waitForSignal();
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        try {
                            reader.processMsgs();
                        } catch (IOException e) {
                            wrapper.error(e);
                        }
                    });
                } catch (Exception e) {
                    wrapper.error(e);
                }
            }
        }).start();

        while (wrapper.getNextValidId() < 0) {
            sleep(1000);
        }

        socket.eDisconnect();
        
    }

    private void sleep( int ms) {
		try {
			Thread.sleep( ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}

    public ZClient() {
        run();
    }

}
