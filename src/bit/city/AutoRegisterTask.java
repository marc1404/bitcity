package bit.city;

public class AutoRegisterTask implements Runnable {

	@Override
	public void run(){
		while(Start.getAutoRegister()){
			Start.registerUser();
		}
	}

}
