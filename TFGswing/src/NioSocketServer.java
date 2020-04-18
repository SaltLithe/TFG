import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class NioSocketServer {
	// No me importa que el bucle este en el constructor pero molaria que se
	// gestionaran los mensajes en algun tipo de
	// controller o handler

	private ConcurrentHashMap<Integer, AsynchronousSocketChannel> clientes = new ConcurrentHashMap<Integer, AsynchronousSocketChannel>();
	private AtomicInteger ids = new AtomicInteger();

	public NioSocketServer() {
		try {
			// Create an AsynchronousServerSocketChannel that will listen on port 8080
			final AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open()
					.bind(new InetSocketAddress(8080));

			// Listen for a new request
			listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {

				@Override
				public void completed(AsynchronousSocketChannel ch, Void att) {

					// Aqui recibe al cliente , le manda el mensaje de greeting con su id , y el
					// nombre del server
					clientes.put(ids.incrementAndGet(), ch);

					DEBUG.debugmessage("SE HA CONECTADO UN CLIENTE AL SERVIDOR CON ID " + ids.get());

					PersonalMessage greetings = new PersonalMessage("Hello from server", "0", ids.get());
					String serializedmessage = null;
					ByteArrayOutputStream bo = new ByteArrayOutputStream();
					try {
						ObjectOutputStream so = new ObjectOutputStream(bo);
						so.writeObject(greetings);
						so.flush();
						serializedmessage = bo.toString();
					} catch (Exception e) {
					}
					ch.write(ByteBuffer.wrap(serializedmessage.getBytes()));

					// Encontrar una forma de guardar al cliente y a su buffer que si no va a ser un
					// lio
					// sobre todo los broadcasts
					// Allocate a byte buffer (4K) to read from the client
					ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
					/*
					 * try {
					 * 
					 * int bytesRead = ch.read(byteBuffer).get(); boolean running = true; // Esto
					 * solo soporta un cliente y aunque es asincrono me parece un poco // porqueria
					 * el while // Posible solucion , hacer que esto sea un bucle que lea
					 * constantemente todos // los sockets // Y si tienen algun mensaje que lo ponga
					 * en una arrayblocking queue // Donde habra uno o varios hilos dedicados a
					 * sacar strings de ahi , // deserializar y procesar // Porque si no veo posible
					 * un server bien hecho de ninguna manera
					 * 
					 * while (bytesRead != -1 && running) { System.out.println("bytes read: " +
					 * bytesRead);
					 * 
					 * // Comprobar que el buffer tiene algo que leer if (byteBuffer.position() > 2)
					 * { // Hacer que el buffer sea legible byteBuffer.flip();
					 * 
					 * // Transformar el buffer en linea byte[] lineBytes = new byte[bytesRead];
					 * byteBuffer.get(lineBytes, 0, bytesRead); String line = new String(lineBytes);
					 * 
					 * // Deserializar
					 * 
					 * // Mandarlo al metodo que lo gestiona
					 * 
					 * // Debug System.out.println("Message: " + line);
					 * 
					 * // Make the buffer ready to write byteBuffer.clear();
					 * 
					 * // Lee la siguiente linea y espera mas segundos que la vida esperada de
					 * nuestro // sistema solar bytesRead =
					 * ch.read(byteBuffer).get(Integer.MAX_VALUE, TimeUnit.SECONDS); } } } catch
					 * (InterruptedException e) { e.printStackTrace(); } catch (ExecutionException
					 * e) { e.printStackTrace(); } catch (TimeoutException e) { // The user exceeded
					 * the 20 second timeout, so close the connection
					 * ch.write(ByteBuffer.wrap("Good Bye\n".getBytes()));
					 * System.out.println("Connection timed out, closing connection"); }
					 */
					System.out.println("End of conversation");
					try {
						// Close the connection if we need to
						if (ch.isOpen()) {
							ch.close();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				@Override
				public void failed(Throwable exc, Void att) {
					/// ...
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getaddress() {
		String remoteaddr;
		try {
			remoteaddr = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			remoteaddr = null;
			e.printStackTrace();
		}

		return remoteaddr;
	}
}