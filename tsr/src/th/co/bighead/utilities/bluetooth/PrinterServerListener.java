package th.co.bighead.utilities.bluetooth;

import java.net.Socket;

public interface PrinterServerListener {
    public void onConnect(Socket socket);
}
