package io.openvidu.server.game;

public class AlarmRunnable implements Runnable{
	private boolean running = true;
	
	public void terminate() {
		running = false;
	}
	
	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		while(running) {
			try {
				for(int i=0; i<30; i++) {
					System.out.println(name+": "+i);
					Thread.sleep(5000);
				}
			}catch(InterruptedException e) {
				running = false;
			}
		}
		
	}
	
}
