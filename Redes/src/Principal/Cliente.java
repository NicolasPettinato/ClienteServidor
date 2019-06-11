package Principal;

import java.io.IOException;
import java.util.Scanner;
import Hilos.HiloCliente;

public class Cliente {

	static HiloCliente hc;
	static boolean fin = false;
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
       	hc = new HiloCliente("192.168.0.13",3000);
		hc.start();
       	
		do {
			
			System.out.println("Elija una Opcion: ");
			System.out.println("1) Enviar mensaje");
			System.out.println("2) Cerrar conexion");
			
			int opc = sc.nextInt();
			if (opc == 1) {
				try {
					hc.enviarDatos("mensaje enviado desde el cliente");
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			if (opc == 2) {
				fin = !fin;
			}
			
		}while(!fin);
		
		hc.cerrarConexion();
	}

}
