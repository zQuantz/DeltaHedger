import com.ib.client.EClientSocket;
import com.ib.client.EReaderSignal;

public class TestConn {

    static EClientSocket client;
    static ZWrapper wrapper = new ZWrapper();
    static EReaderSignal signal;

    public static void main(String args[]){
        new ZClient();
    }

}
