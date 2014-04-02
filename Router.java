import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Router {

	static int lan_count=0;
	static int router_count;	
	static int router_id;
	static int[] Lan;
	static int[] Dest;
	static int[] Cost;
	static int[] Next_Hop_Lan;
	static int[] Next_Hop_Router;
	static String[] Child_Map;
	
	static int flag=0;
	static String data="";
	static String recievers;
	static boolean receiver_flag;
	static String[][] neighbors;
	static String[] child_temp;
	static String[][] neighbors_temp;

	
		
	static RandomAccessFile ra = null;
	static BufferedWriter bw = null;
	static Router r;
	static long ptr[] = null;
	public Router()
	{
		Lan=new int[10];
		Dest=new int[10];
		Cost=new int[10];
		Next_Hop_Lan=new int[10];
		Next_Hop_Router=new int[10];
		Child_Map=new String[10];
		ptr=new long[10];
		child_temp=new String[10];
		neighbors_temp=new String[10][10];
		neighbors=new String[10][10];
		for(int i=0;i<10;i++)
		{
			Dest[i]=i;
			Cost[i]=10;
			Next_Hop_Lan[i]=10;
			Next_Hop_Router[i]=10;
			ptr[i]=0;
			Child_Map[i]="";
			child_temp[i]="";
			for(int j=0;j<10;j++)
			{
				neighbors_temp[i][j]="";
				neighbors[i][j]="";
			}
		}
		
		
	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		r=new Router();
		//System.out.println(args.length);
		router_id=Integer.parseInt(args[0]);
		
		for(int i=1;i<args.length;i++)
		{
			Lan[lan_count]=Integer.parseInt(args[i]);
			Dest[Integer.parseInt(args[i])]=Integer.parseInt(args[i]);
			Cost[Integer.parseInt(args[i])]=0;
			Next_Hop_Lan[Integer.parseInt(args[i])]=Integer.parseInt(args[i]);
			Next_Hop_Router[Integer.parseInt(args[i])]=router_id;
			lan_count++;
		}
		receiver_flag=false;
		recievers="";//new String[10];
		
		
	//	String Child_Map="";//String.valueOf(Lan[0]);
			//for(int i=1;i<lan_count;i++)
			//	 Child_Map=Child_Map+","+Lan[i]; 
			//System.out.println(Child_Map);
	
	//	for(int i=0;i<10;i++)
			//System.out.println("Dest= "+Dest[i]+" Cost= "+Cost[i]+" NH_lan= "+Next_Hop_Lan[i]+" NH_R= "+Next_Hop_Router[i]);
		//	System.out.println("Child Map= "+Child_Map[i]);
			
		build_RT();
		
		//for(int i=0;i<lan_count;i++)
		//br[i]=new BufferedReader(new FileReader(new File("lan"+i+".txt")));
		
		//check_lans();
	
	}


	private static void build_RT() throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		int i=0;
		while(true)
		{
			check_lans();
			//for(int j=0;j<10;j++)
		//	System.out.println("DEST= "+Dest[j]+" COst= "+Cost[j]+" NH_lan= "+Next_Hop_Lan[j]+" NH_Router= "+Next_Hop_Router[j]);
			
			if(i%5==0)
				send_dv();	
			
			//display_RT();
			
			if(i%10==0)if(flag!=0)
				check_NMR();
			
			if(i%20==0)check_receivers();
			
			if(i==100) System.exit(0);
			Thread.sleep(1000);
			i++;
		
		}
		
	}


	private static void check_receivers() {
		// TODO Auto-generated method stub

		for(int i=0;i<lan_count;i++)
		{
			if(receiver_flag==false)
				recievers="";

			receiver_flag=false;
		}
		
	}


	private static void check_NMR() throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		int f=0;
		System.out.println("Router: "+router_id);
			String[] split_data=data.split(" ");
			for(int i=0;i<lan_count;i++)	
				if(!recievers.equals("")) f=1;
			
			if(Child_Map[Integer.parseInt(split_data[2])].equals("") && f==0 )
			{
				System.out.println("SENDING NMR, MY CHILD ="+Child_Map[Integer.parseInt(split_data[2])]);
				BufferedWriter bw = new BufferedWriter( new FileWriter("rout"+router_id+".txt",true));
				bw.write("NMR "+Next_Hop_Lan[Integer.parseInt(split_data[2])]+" "+router_id+" "+split_data[2]);
				bw.newLine();
				bw.close();
			}
		
		
	}


	private static void display_RT() {
		// TODO Auto-generated method stub
		System.out.println("ROUTER "+router_id);
		System.out.println("Dest\tCost\tNH_Lan\tNH_Rout\tChildMAP");
		for(int i=0;i<10;i++)
		{
			System.out.println(Dest[i]+"\t"+Cost[i]+"\t"+Next_Hop_Lan[i]+"\t"+Next_Hop_Router[i]+"\t"+Child_Map[i]);
		}
	}


	private static void send_dv() throws IOException {
		String File_Name="rout"+router_id+".txt";
		//System.out.println(File_Name);
		bw=new BufferedWriter(new FileWriter(File_Name,true));
		
		String dv="DV ";
		String r=" ";
		for(int i=0;i<10;i++)
		{
			//if(Cost[i]!=10)
			r=r+Cost[i]+" "+Next_Hop_Router[i]+" ";
		}

		for(int i=0;i<lan_count;i++)
		{
			bw.write("DV "+Lan[i]+" "+router_id+r);
			bw.newLine();
		}
		

		//Thread.sleep(5000);
//	bw.flush();
		bw.close();
//	send_dv();
		
		
	}


	private static void check_lans() throws IOException {
		// TODO Auto-generated method stub
		String s;	
		for(int i=0;i<lan_count;i++)
		{
			
			ra=new RandomAccessFile("lan"+Lan[i]+".txt", "rw");
			ra.seek(ptr[i]);
			s=ra.readLine();
			while(s!=null)
			{
				//System.out.println(s);
				
				
				if(s.startsWith("DV"))
				{
					String[] ss=s.split(" ");
				//	System.out.println(ss.length)
					//	
					if(router_id==4) System.out.println("Child temp="+child_temp[0]+" child map="+Child_Map[0]);
					int k=0;
						for(int j=3;j<ss.length;j=j+2)
						{
						//Dest[i]=j-3;
					//	System.out.println("cost[k] "+Cost[k]+" cost[next] ="+Integer.parseInt(ss[j])+1);
							if(Integer.parseInt(ss[2])!=router_id)
							{
								if(Integer.parseInt(ss[j+1])==router_id)
								{
									if(Child_Map[k].equals(""))
									{	Child_Map[k]=ss[1]; 
										child_temp[k]=ss[1];
									}
									
									else if(!child_temp[k].contains(ss[1]))
									{
										child_temp[k]=child_temp[k]+ss[1];
										Child_Map[k]=Child_Map[k]+ss[1];
									}
								}
								if(!neighbors_temp[k][Integer.parseInt(ss[1])].contains(ss[2]) && Integer.parseInt(ss[j+1])==router_id)
								{
								if(neighbors[Integer.parseInt(ss[1])].equals("") && Integer.parseInt(ss[2])!=router_id) neighbors[k][Integer.parseInt(ss[1])]=ss[2];
								else if(!neighbors[k][Integer.parseInt(ss[1])].contains(ss[2]))
										neighbors[k][Integer.parseInt(ss[1])]=neighbors[k][Integer.parseInt(ss[1])]+ss[2];
									neighbors_temp[k][Integer.parseInt(ss[1])]=neighbors_temp[k][Integer.parseInt(ss[1])]+ss[2];
								}
								
							}
							
							if(Cost[k]!=0 && Cost[k]>=Integer.parseInt(ss[j])+1 )
							{
								if( Cost[k]>Integer.parseInt(ss[j])+1)
								{
									Cost[k]= Integer.parseInt(ss[j])+1;
									Next_Hop_Router[k]=Integer.parseInt(ss[2]);
									Next_Hop_Lan[k]=Integer.parseInt(ss[1]);
								}
								else if(Cost[k]==Integer.parseInt(ss[j])+1 && Next_Hop_Router[k]>Integer.parseInt(ss[2]))
								{
									Next_Hop_Router[k]=Integer.parseInt(ss[2]);
									Next_Hop_Lan[k]=Integer.parseInt(ss[1]);
								}
							
							}
							k++;
						}
						//ptr[i]=ra.getFilePointer();
						
				}
				else if(s.startsWith("data"))
				{
					
					
					flag=1;
					data=s;
					String[] ss=s.split(" ");
					
					if(Lan[i]==Next_Hop_Lan[Integer.parseInt(ss[2])])
					{
						for(int p=0;p<lan_count;p++)  
						{
							if(Lan[i]!=Lan[p] )
							{
								
								if(recievers.contains(String.valueOf(Lan[p])) || Child_Map[Integer.parseInt(ss[2])].contains(String.valueOf(Lan[p])))
								{
									
									BufferedWriter bw = new BufferedWriter( new FileWriter("rout"+router_id+".txt",true));
									bw.write("data "+Lan[p]+" "+ss[2]);
									bw.newLine();
									bw.close();
								}
							}
						}
					//	System.out.println(Child_Map+" of router "+router_id);
	
					}
					
				}	
				else if(s.startsWith("receiver"))
				{
					String ss[]=s.split(" ");
					if(recievers.equals(""))recievers=ss[1];
					else if(!recievers.contains(ss[1])) recievers=recievers+" "+ss[1];
					
					receiver_flag=true;
					
				//	if(Child_Map[Integer.parseInt(ss[1])].equals(""))Child_Map[Integer.parseInt(ss[1])]=ss[1];
				//	if(!Child_Map[Integer.parseInt(ss[1])].contains(ss[1])) Child_Map[Integer.parseInt(ss[1])]=Child_Map[Integer.parseInt(ss[1])]+ss[1];
				//	System.out.println("Child = "+Child_Map+" of router "+router_id);
				}
				else if(s.startsWith("NMR"))
				{
					String[] ss=s.split(" ");
					if(!ss[1].equals("") && Next_Hop_Lan[Integer.parseInt(ss[3])]!=Lan[i] && Next_Hop_Lan[Integer.parseInt(ss[3])]!=Integer.parseInt(ss[3]))
					{	
						
						String temp="";
						for(int f=0;f<neighbors[Integer.parseInt(ss[3])][Integer.parseInt(ss[1])].length();f++)
						{
							if(neighbors[Integer.parseInt(ss[3])][Integer.parseInt(ss[1])].charAt(f)!=ss[2].charAt(0))
							{
								if(!temp.equals(""))temp=temp+String.valueOf(neighbors[Integer.parseInt(ss[3])][Integer.parseInt(ss[1])].charAt(f));//.replace(ss[2]+",", "");	
								else temp=String.valueOf(neighbors[Integer.parseInt(ss[3])][Integer.parseInt(ss[1])].charAt(f));
							}
						}
						neighbors[Integer.parseInt(ss[3])][Integer.parseInt(ss[1])]=temp;
						
						if(neighbors[Integer.parseInt(ss[3])][Integer.parseInt(ss[1])].equals(""))
						{
							//Child_Map[Integer.parseInt(ss[3])].replace(ss[1], "");
							
							temp="";
							for(int f=0;f<Child_Map[Integer.parseInt(ss[3])].length();f++)
							{
								if(Child_Map[Integer.parseInt(ss[3])].charAt(f)!=ss[1].charAt(0))
								{
									if(!temp.equals(""))temp=temp+String.valueOf(Child_Map[Integer.parseInt(ss[3])].charAt(f));	
									else temp=String.valueOf(Child_Map[Integer.parseInt(ss[3])].charAt(f));
								}
							}
							Child_Map[Integer.parseInt(ss[3])]=temp;
							
							if(recievers.equals("") && Child_Map[Integer.parseInt(ss[3])].equals(""))
							{
								BufferedWriter bw = new BufferedWriter( new FileWriter("rout"+router_id+".txt",true));
								bw.write("NMR "+Next_Hop_Lan[Integer.parseInt(ss[3])]+" "+router_id+" "+ss[3]);
								bw.newLine();
								bw.close();
							}		
						}
					}
				}
				ptr[i]=ra.getFilePointer();
				s=ra.readLine();
				
			}
			//ptr[i]=ra.getFilePointer();
			ra.close();
		}
		
	

		
		
	}


	

}
