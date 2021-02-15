package infiniteLoop;

public class InfiniteLoopApp {

	public static void main(String[] args) {
		
		while (true) {
			System.out.println("This is an infinite loop");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
