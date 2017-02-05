import java.util.Random;

public class HandyFunctions {
	public static int randomInt(int min, int max){
    	int randomNum = new Random().nextInt((max-min) + 1 )+min;
    	return randomNum;
    }
}
