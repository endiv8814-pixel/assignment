package TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class DiscountServer {
    private static final Random random = new Random();
    private static final int PORT = 228;

    public static void main(String[] args) throws IOException {

        
        try (ServerSocket socket = new ServerSocket(ConnectionConfig.D_POST)){

            System.out.println("[SERVER] Listening on port: " + ConnectionConfig.D_POST);
            while (true){

                try (Socket client = socket.accept()){

                    handleClient(client);
                }

                catch (IOException e){

                    System.err.println("[SERVER] Connection error: " + e.getMessage());
                }
               
            }
    

        }
    }

    private static void handleClient(Socket socket){

        BufferedReader input;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            String recieved = input.readLine();
            if (recieved == null){
        
                System.out.println("[SERVER] Empty input received - closing connection");
                return;
        
            }

            double titalEmission = Double.parseDouble(recieved.trim());

            System.out.println("[SERVER] Recieved total emission: " + titalEmission);

            int discount = random.nextInt(31);

            double discountedEmission = DiscountCalculator.calculateDiscount(titalEmission, discount);

            String response = String.format("DISCOUNT:%d:%.2f", discount, discountedEmission);

            output.println(response);

            System.out.printf("[SERVER] Client connected | Sent: %.2f | Discount: %d%% | Result: %.2f%n", titalEmission, discount, discountedEmission);
         
        } catch (IOException e) {

            e.printStackTrace();
        }

    }
}
