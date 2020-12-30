package core;

public class DEBUG {
	/*
	 * Clase debug que contiene metodos estaticos para que cualquier clase pueda
	 * poner u nmensaje de debug en cualquier momento y solo tener que cambiar el
	 * booleano enabled para que los mensajes se impriman o no , asi es mas facil
	 * quitar las llamadas a debug en produccion y se pueden añadir otro tipo de
	 * mensajes segun queramos
	 */
	public static boolean enabled = false;
	public static boolean unsetStreams = false;
	public static boolean executing = false;

	public static void debugmessage(String mensaje) {

		if (enabled && !executing) {
			System.err.println("DEBUG : " + mensaje);
		}
	}

	public static void setExecuting() {
		executing = true;
		// TODO Auto-generated method stub

	}

	public static void unsetExecuting() {
		executing = false;
	}
}