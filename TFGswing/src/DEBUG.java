public class DEBUG {
	public static boolean enabled = true;

	public static void debugmessage(String mensaje) {

		if (enabled) {
			System.err.println("DEBUG : " + mensaje);
		}
	}
}