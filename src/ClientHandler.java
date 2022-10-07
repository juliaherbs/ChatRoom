import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

	
	public static  ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String clientUsername;
	
	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername = reader.readLine();
			clientHandlers.add(this);
			broadcastMessage("SERVER: " + clientUsername + " entered");
		} catch(IOException e) {
			
			closeEverything(socket, reader, writer);
		}
	}
	
	@Override
	public void run() {

		String messageFromClient;
		
		while (socket.isConnected()) {
			try {
				messageFromClient = reader.readLine();
				broadcastMessage(messageFromClient);
			}catch(IOException e) {
				closeEverything(socket, reader, writer);
				break;
			}
		}

	}

		public void broadcastMessage(String messageToSend) {
			for (ClientHandler clientHandler:clientHandlers) {
				try {
					if(!clientHandler.clientUsername.equals(clientUsername)) {
						clientHandler.writer.write(messageToSend);
						clientHandler.writer.newLine();
						clientHandler.writer.flush();
					}
				}catch(IOException e){
					closeEverything(socket, reader, writer);
				}
			}
		}
	
	public void removeClientHandler() {
		clientHandlers.remove(this);
		broadcastMessage("SERVER " + clientUsername + " has left the chat!");
	}
	public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
		removeClientHandler();
		try {
			if(reader != null) {
				reader.close();
			}
			if(writer != null) {
				writer.close();
			}
			if(socket != null) {
				socket.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}









