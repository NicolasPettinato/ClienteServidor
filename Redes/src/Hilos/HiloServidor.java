package Hilos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HiloServidor extends Thread {
	
	
	private ServerSocket server;   //socket encargado de esperar una conexion del cliente
	private Socket conexion;	   //socket comun
	
	//Streams para poder enviar informacion por red y recibirlos
	private ObjectInputStream entrada;	
	private ObjectOutputStream salida;
	
	private boolean conexionActiva = true;
	
	public HiloServidor() {
		/* cuando creo el objeto de HiloServidor se invoca al constructor del mismo, ser crea un serverSocket que va
		 a escuchar conexiones por el puerto 300, va a esperar una conexion de un cliente, y una vez que un cliente
		 se conecte al servidor va a obtener los dos objetos de entrada y salida para mas adelante 
		 mandar datos y recibir datos*/
		try {
			server = new ServerSocket(3000,10); //se crea socket del servidor (numero de puerto y maximo de conexiones)
			esperarConexion();                  
			obtenerStreams();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void obtenerStreams() throws IOException {
		entrada = new ObjectInputStream(conexion.getInputStream());  //obtiene streams de entrada de la conexion que se genero
		salida = new ObjectOutputStream(conexion.getOutputStream()); 
		salida.flush(); //limpia el buffer

	}

	private void esperarConexion() throws IOException {
		System.err.println("Esperando Conexion...");
		conexion = server.accept();
		/* escucha constantemente si hay algun cliente que se quiere conectar al servidor, 
		acepta la conexion y guarda el socket mediante el cual se va a comunicar */
	}
	
	public void run() {
		do {
			
			/* permite que este hilo reciba informacion mediante la red y que envie
			  informacion hacia algun cliente mediante la red*/
			try {
				procesarConexion();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}while(conexionActiva);
	}

	private void procesarConexion() throws ClassNotFoundException, IOException {
		/* lo que va a suceder cuando este hilo reciba un mensaje desde un cliente */
		String mensaje;
		mensaje = (String) entrada.readObject(); //lee lo que hay en el Stream de entrada y se guarda en el att mensaje
		System.out.println(mensaje);
		
		if(mensaje.compareTo("Iniciar Conexion")==0) {
			System.out.println("Conexion Establecida: " + conexion.getInetAddress().getHostName());
			enviarDatos("El servidor acepto su solicitud de conexion"); /* envia datos al cliente */
		}
		if(mensaje.compareTo("Cerrar conexion")==0){
			conexionActiva = false;
			server.close();
			conexion.close();
			entrada.close();
			salida.close();
		}
	}

	private void enviarDatos(String mensaje) {
		/* mensajes o datos que le van a llegar al cliente*/
		try {
			salida.writeObject(mensaje); //Stream de salida - mandar datos por red
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
	}

	
	

}
