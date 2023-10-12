import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;


public class Communication {

    final static String HEADER = "#B112#";
    //final static String PORT = "COM3";
    private SerialPort[] ports;
    private SerialPort port;
    String serialNumber = "7573530363135131A122";

    public Communication(){
    }

    /**
     * <h2>Message protocol</h2>
     * This method builds the message following specific rules and tries to send it to the Arduino board. Otherwise it shows an error message in the console.
     * @example #B112#2;6;8;10;G <li>Room B112</li> <li>tables 2,6,8,10</li> <li>in "go down" mode </li>
     * @param args boolean array of selected tables
     * @param mode char of the mode
     */
    public void sendMessage(boolean[] args, char mode){
        StringBuilder message = new StringBuilder(HEADER);
        for(int i = 0; i< args.length; i++){
            if(args[i]){
                message. append((i + 2)).append(";");
            }
        }
        message.append(mode).append("\n");

        try{

            port.getOutputStream().write(message.toString().getBytes());
            port.getOutputStream().flush();
        }catch (IOException e){
            System.out.println("Errore nel metodo sendMessage");
        }

    }
    /**
     * this is a simple method that sends a message to arduino and lights up the built-in led
     */
    public void test() throws IOException {

        port.getOutputStream().write("#test".getBytes());
        port.getOutputStream().flush();
    }

    /**
     * <h2>Listen to arduino</h2>
     * This method creates a <b>thread</b> that constantly listens to the serial COM for responses from Arduino and displays them on terminal.
     */
    /*public void listenToArduino(){
        Thread readerThread = new Thread(() -> {
            while (true) {

                byte[] buffer = new byte[1];
                int bytesRead = port.readBytes(buffer, buffer.length);

                if (bytesRead > 0) {
                    char receivedChar = (char) buffer[0];

                    System.out.print(receivedChar);
                }
            }
        });

        readerThread.start();
    }*/

    /**
     * <h2>Communication</h2>
     * This method finds all the serial ports available and checks if the desired one is connected via the Arduino serial number. It also opens the port and initializes the communication.
     * @return Boolean false if connection fails, otherwise true
     */
    public boolean connection(){
        ports = SerialPort.getCommPorts();

        System.out.println("[COMM] ricerica di porte...");
        for(int i = 0; i<ports.length; i++){
            System.out.println("[COMM] serial number " + (i+1) + ": " + ports[i].getSerialNumber());
            if(ports[i].getSerialNumber().equals(serialNumber)){
                System.out.println("[COMM] collegamento effetuato con la porta: " + ports[i].getSerialNumber());
                port = ports[i];
            }
        }

        try{
            port.openPort();
            port.setBaudRate(9600); // Imposta il baud rate
            //listenToArduino();
        }catch (NullPointerException e){
            return false;
        }
        return true;
    }
}
