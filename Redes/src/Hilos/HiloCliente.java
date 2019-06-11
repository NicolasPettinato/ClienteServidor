package Hilos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class HiloCliente extends Thread {
	
	private Socket cliente;	// att socket
	private ObjectInputStream entrada;
	private ObjectOutputStream salida;
	
	Scanner teclado = new Scanner(System.in);
	
	public HiloCliente(String iPServidor, int Port){
		/*recibimos por parametro la ip del servidor y el puerto*/
		try {
			cliente = new Socket(InetAddress.getByName(iPServidor),Port);  //ip del servidor y puerto
			obtenerStreams();
			enviarDatos("Iniciar Conexion");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
	private void obtenerStreams() throws IOException {
		entrada = new ObjectInputStream(cliente.getInputStream());
		salida = new ObjectOutputStream(cliente.getOutputStream());
		salida.flush();
	}
	
	public void enviarDatos(String mensaje) throws IOException {
		salida.writeObject(mensaje);
	}
	
	public void run() {
		/*corroborar todo el tiempo si recibio un mensaje*/
		do {
			try {
				procesarConexion();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				try {
					cliente.close();
					entrada.close();
					salida.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				break; //salga del do while
			}
		}while(true);
			
	}
	
	private void procesarConexion() throws ClassNotFoundException, IOException {
		String mensaje;
		mensaje = (String) entrada.readObject();
		System.out.println(mensaje);
	}
	
	public void cerrarConexion() {
		try {
			enviarDatos("Cerrar conexion");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Conexion Terminada");
	}
}
