package Principal;

import Hilos.HiloServidor;

public class Servidor {

	static HiloServidor hs;
	
	public static void main(String[] args) {
	
		hs = new HiloServidor();
		hs.start();

	}

}
