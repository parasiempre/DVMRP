import java.io.BufferedWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;



public class Host {

	 static int Host_id;
	 static int Lan_no;
	 static String type=null;
	 static int start_time;
	 static int period;
	 static BufferedWriter bw=null;
	 static long ptr=0;
	
	
	public Host()
	{
		Host_id=10;
		Lan_no=10;
		type=null;
		start_time=-1;
		period=10;
		
	}
	 
	public static void create_host_info(String[] s_space)
	{
		Host_id=Integer.parseInt(s_space[0]);
		Lan_no=Integer.parseInt(s_space[1]);
		type=s_space[2];
		if(type.equals("sender"))
		{
			
			start_time=Integer.parseInt(s_space[3]);
			period=Integer.parseInt(s_space[4]);
		}
	
	}
		
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		Host h=new Host();
		create_host_info(args);
		System.out.println("Host_ id: "+Host_id+"Lan_no: "+Lan_no+"type: ="+type);
		if(type.equals("sender"))
		{	
			int i=start_time;
			Thread.sleep(start_time*1000);
			while(true)
			{
				i=i+period;
				
				send("data "+Lan_no+" "+Lan_no);
				Thread.sleep(period*1000);
				if(i>100) System.exit(0);
			}
		}
		else if(type.equals("receiver"))
		{
			int i=0;
			while(true)
			{
				receive();
				if(i%10 == 0)send("receiver "+Lan_no);
				Thread.sleep(1000);
				i++;
				if(i==100) System.exit(0);
			}
		}
	}

	private static void receive() throws IOException {
		// TODO Auto-generated method stub
		
		RandomAccessFile ra=new RandomAccessFile("lan"+Lan_no+".txt", "rw");
		ra.seek(ptr);
		String s=ra.readLine();
		while(s!=null)
		{
			
			if(s.startsWith("data"))
			{
				BufferedWriter b=new BufferedWriter(new FileWriter("hin"+Host_id+".txt",true));
				b.write(s);
				b.newLine();
				b.close();
			}
			ptr=ra.getFilePointer();
			s=ra.readLine();
		}
		
	}

	private static void send(String data) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		//String data="data "+Lan_no+" "+Lan_no;
		String File_Name="hout"+Host_id+".txt";
		System.out.println(File_Name);
		bw=new BufferedWriter(new FileWriter(File_Name,true));
		bw.write(data);
		bw.newLine();
		bw.close();
		
		//Thread.sleep(period*1000);
		
		//send(data);
		
	}
	

}
