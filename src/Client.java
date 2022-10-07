import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private Socket socket;
	private BufferedReader reader;
	public BufferedWriter writer;
	private String username;
	public Client(Socket socket, String username) {
		try { 
			this.socket= socket;
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;
		}catch(IOException e) {
			closeEverything(socket, reader, writer);
		}
	}
	public void sendMessage() {
		try {
			writer.write(username);
			writer.newLine();
			writer.flush();
			
			Scanner scanner = new Scanner(System.in);
			while(socket.isConnected()) {
				String messageToSend = scanner.nextLine();
				writer.write(username + ": " + messageToSend);
				writer.newLine();
				writer.flush();
			}
		}catch (IOException e) {
			closeEverything(socket, reader, writer);
		}
	}
	public void listenForMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String msgFromChat;
				
				while(socket.isConnected()) {
					try {
					msgFromChat = reader.readLine();
					System.out.println(msgFromChat);
					}catch (IOException e) {
						closeEverything(socket, reader, writer);
					}
				}
			}
		}).start();
	}
	public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
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

	public static void main(String[] args) throws IOException{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your username for the Chat");
		String username = scanner.nextLine();
		Socket socket = new Socket("localhost", 1234);
		Client client = new Client(socket, username);
		client.listenForMessage();
		client.sendMessage();
		}
}
