package developer;
public class DEBUG {
	/*
	 * Clase debug que contiene metodos estaticos para que cualquier clase pueda
	 * poner u nmensaje de debug en cualquier momento y solo tener que cambiar el
	 * booleano enabled para que los mensajes se impriman o no , asi es mas facil
	 * quitar las llamadas a debug en produccion y se pueden añadir otro tipo de
	 * mensajes segun queramos
	 */
	public static boolean enabled = true;

	public static void debugmessage(String mensaje) {

		if (enabled) {
			System.err.println("DEBUG : " + mensaje);
		}
	}
}