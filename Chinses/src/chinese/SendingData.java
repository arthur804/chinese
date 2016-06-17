package chinese;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class SendingData extends ArrayList<Integer>{

	public SendingData(){
		super(5);
	}
	
	public void retryFirst(int first){
		clear();
		add(first);
	}
	
}
