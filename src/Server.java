import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class Server {
	
	private ServerSocket serverSocket;
	

	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void startServer() {
		
		try {
			
			while(!serverSocket.isClosed()) {
				
				Socket socket = serverSocket.accept();
				System.out.println("new client");
				ClientHandler clientHandler = new ClientHandler(socket);
				
				
				Thread thread = new Thread(clientHandler);
				thread.start();
				
			}
		} catch(IOException e) {
			
		}
	}
	
	public void closeServerSocket() {
		try {
			if(serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket serverSocket = new ServerSocket(1234);
		Server server = new Server(serverSocket);
		server.startServer();
		
	}
	
}
	

