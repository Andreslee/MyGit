package banco;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
//Modificado 
public class Banco {

	private static int operaciones[];

	public static void readText(String args) {
		int count = 0;
		int numLines = 0;

		final String nomFich = args;

		Scanner in = null;

		try {

			in = new Scanner(new FileReader(nomFich));

			while (in.hasNextLine()) { // para saber
				String currLine = in.nextLine(); // el numero de lineas
				numLines++; // del texto
			}

			operaciones = new int[numLines];

			in = new Scanner(new FileReader(nomFich));

			while (in.hasNext()) { //
				while (in.hasNextInt()) { //
					int d = in.nextInt(); // lee y guarda cada linea en
					operaciones[count] = d; // una posicion del arreglo
					count++;
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("Error abriendo el fichero " + nomFich);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static void main(String[] args) {

		ActorSystem system = ActorSystem.create("Transacciones");

		ActorRef Cuenta1 = system
				.actorOf(Props.create(Cuenta.class), "Cuenta1");

		ActorRef Cliente1 = system.actorOf(Props.create(Cliente.class),
				"Cliente1");

		ActorRef Cliente2 = system.actorOf(Props.create(Cliente.class),
				"Cliente2");

		String fichero = args[0];
		readText(fichero);

		for (int i = 2; i < operaciones.length; i++) {
			if (i >= 2 && i < (2 + operaciones[0]))
				Cuenta1.tell(operaciones[i], Cliente1);
			if (i >= (2 + operaciones[0]) && i < operaciones.length)
				Cuenta1.tell(operaciones[i], Cliente2);
		}

		system.shutdown();

		system.awaitTermination();

		System.out.println("Saldo en la cuenta: " + Cuenta.cuenta);

	}

}

class Cuenta extends UntypedActor {

	static Integer cuenta = 0;
	Integer exito = 0;

	@Override
	public void onReceive(Object operacion) throws Exception {

		if (operacion instanceof Integer) {

			cuenta += (Integer) operacion;

			exito++;

			getSender().tell(exito, getSelf());

			exito--;

		}

		else {

			unhandled(operacion);

		}

	}
}

class Cliente extends UntypedActor {

	@Override
	public void onReceive(Object mensaje) throws Exception {

		if (mensaje instanceof Integer) {

			Integer i = (Integer) mensaje;

			switch (i) {

			case 0: {

				System.out.println("Transacción fallida");

				break;

			}

			default:

				System.out.println("Transacción exitosa");

			}

		}

		else {

			unhandled(mensaje);

		}
	}

}
}
