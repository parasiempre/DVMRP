import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Controller {

		static int[] hosts;
		static int[] routers;
		static int[] lans;
		
		static int host_count=0;
		static int routers_count=0;
		static int lans_count=0;
		
		static long[] router_ptr=new long[10];
		static long[] host_ptr=new long[10];
		static RandomAccessFile ra=null;
		static BufferedWriter bw=null;
		static int count=0;
		static int r_size[]=null;
		static int sec;
		
		
		public Controller()
		{
			hosts=new int[10];
			routers=new int[10];
			lans=new int[10];
			router_ptr=new long[10];
			host_ptr=new long[10];
			r_size=new int[10];
			sec=0;
			
			
		}
		
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		
		Controller c=new Controller();

		
		get_nodes_info(args);
		//System.out.println(routers[0]);
		update_files();
		
		
	}
	
	private static void update_files() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//String[] file = null;
		String s=null;
		sec++;
		for(int i=0;i<host_count;i++)
		{
			//System.out.println("r="+routers[i]);
			ra=new RandomAccessFile("hout"+hosts[i]+".txt", "rw");
			ra.seek(host_ptr[i]);
			s=ra.readLine();
			while(s!=null)
			{
				String[] ss=s.split(" ");
				System.out.println("lan"+ss[1]+".txt");
				bw=new BufferedWriter(new FileWriter("lan"+ss[1]+".txt",true));
				bw.write(s+"\r\n");
				//bw.newLine();
				bw.close();
				host_ptr[i]=ra.getFilePointer();
				s=ra.readLine();
				
			}
		
			ra.close();
		}
		
		for(int i=0;i<routers_count;i++)
		{
			//System.out.println("r="+routers[i]);
			ra=new RandomAccessFile("rout"+routers[i]+".txt", "rw");
		//	if(s==null)count++;
		//	else count=0;
			ra.seek(router_ptr[i]);
			s=ra.readLine();
			while(s!=null)
			{
				String[] ss=s.split(" ");
				System.out.println("lan"+ss[1]+".txt");
				bw=new BufferedWriter(new FileWriter("lan"+ss[1]+".txt",true));
				bw.write(s+"\r\n");
				//bw.newLine();
				bw.close();
				s=ra.readLine();
				
			}
		
			router_ptr[i]=ra.getFilePointer();
			ra.close();
		}
		if(sec==100) System.exit(0);
		Thread.sleep(1000);
		update_files();
		
	}

	private static void get_nodes_info(String[] args) {
		// TODO Auto-generated method stub
		int flag = 0,i;
		if(args[0].equals("host")) i=1;
		else i=0;
		//System.out.println("shivani");
		while(flag==0)
		{
			//System.out.println(args[i]);
			if(!args[i].equals("router"))
			{
				hosts[host_count++]=Integer.parseInt(args[i]);
				i++;
			}
			else flag=1;
			
		}
		
		i++;
		while(flag==1)
		{
			if(!args[i].equals("lan"))
			{
				routers[routers_count++]=Integer.parseInt(args[i]);
				i++;
			}
			else flag=2;
		}
		i++;
		
		while(flag==2)
		{
			//System.out.println(args[i]+" i= "+i+" length= "+args.length);
			if(!args[i].equals("&"))
			{
				lans[lans_count++]=Integer.parseInt(args[i]);
				i++;
			}
			else flag=2;
			if(i==args.length) break;
		}
	}

}
