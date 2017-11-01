package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.ORB;

import common.BankServerInterfacePOA;
import common.BankServerInterfacePackage.invalid_bankOperation;
import common.BankServerInterfacePackage.invalid_client;
import domain.BranchID;
import domain.Client;
import domain.EditRecordFields;
import udp.UDPServer;

public class BankServerImpl extends BankServerInterfacePOA 
{
	private static final long serialVersionUID = 1L;
	
	//Variable for each separate bank server
	private Logger logger = null;
	private BranchID branchID;
	private int clientCount;
	private Map<String, ArrayList<Client>> clientList = new HashMap<String, ArrayList<Client>>();
	
	//CORBA Variables
	private ORB orb = null;
	private int UDPPort;
	private String UDPHost;
	
	//UDP Server for listening incoming requests
	private UDPServer UDPServer;
	
	//Holds other servers' addresses
	HashMap<String, String> serversList = new HashMap();
	
	private static final int    CLIENT_NAME_INI_POS = 3;

	//1. Each branch will have its separate server
	public BankServerImpl(BranchID branchID, ORB orb, String host, int port, HashMap<String, String> serversList)
	{
		this.branchID = branchID;
		this.UDPPort = UDPPort;
		this.clientCount = 0;
		
		this.orb = orb;
		this.UDPHost = host;
		this.UDPPort = port;
		this.serversList = serversList;
		
		this.UDPServer = new UDPServer(UDPHost, UDPPort, this);
		
		//1.1 Logging Initiation
		this.logger = this.initiateLogger();
		this.logger.info("Server Log: Initializing server ... ");
				
		System.out.println("Server: " + branchID + " initialization success.");
		System.out.println("Server: " + branchID + " port is : " + UDPPort);
	}	

	private Logger initiateLogger() 
	{
		Logger logger = Logger.getLogger("Server Logs/" + this.branchID + "- Server Log");
		FileHandler fh;
		
		try
		{
			//FileHandler Configuration and Format Configuration
			fh = new FileHandler("Server Logs/" + this.branchID + " - Server Log.log");
			
			//Disable console handling
			logger.setUseParentHandlers(false);
			logger.addHandler(fh);
			
			//Formatting configuration
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		}
		catch (SecurityException e)
		{
			System.err.println("Server Log: Error: Security Exception " + e);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.err.println("Server Log: Error: IO Exception " + e);
			e.printStackTrace();
		}
		
		System.out.println("Server Log: Initialization successed ... " + "Server ID: " + branchID + " | " + "Port : " + UDPPort);
		
		return logger;
	}

	@Override
	public String[] getAllCustomerAccount()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createAccount(String firstName, String lastName, String address, String phone, String customerID,
	        common.BranchID branchID) throws invalid_client
	{
		this.logger.info("Initiating user account creation for " + firstName + " " + lastName);
		
		BranchID branchIDNew = BranchID.valueOf(branchID.toString());
		
		//If the user IS at the right branch ... we start the operation.
		if (branchIDNew == this.branchID)
		{
			//Each character will be a key, each key will starts with 10 buckets.
			String key = Character.toString((char)customerID.charAt(CLIENT_NAME_INI_POS));
			ArrayList<Client> values = new ArrayList<Client>(10);
			
			//If a key doesn't exist ... for example no client with last name Z ... then ...
			if(!clientList.containsKey(key))
			{
				//Adds a key and 10 buckets
				clientList.put(key, values);
			}
			
			//This is to test if the account is already exist.
			//1. Extract the buckets of the last Name ... for example, we will get all the clients with last name start with Z
			values = clientList.get(key);
			
			//2. After extracted the buckets, we loops to check if the customerID is already exist.
			for (Client client: values)
			{
				if (client.getCustomerID().equals(customerID))
				{
					this.logger.severe("Server Log: | Account Creation Error: Account Already Exists | Customer ID: " + customerID);
					throw new invalid_client ("Server Error: | Account Creation Error: Account Already Exists | Customer ID: " + customerID);
				}
			}
			
			//3. If no existing account found, then we create a new account.
			Client newClient;
			
			try
			{
				newClient = new Client(firstName, lastName, address, phone, customerID, branchIDNew);
				values.add(newClient);
				clientList.put(key, values);
				
				this.logger.info("Server Log: | Account Creation Successful | Customer ID: " + customerID);
				this.logger.info(newClient.toString());
			}
			catch (Exception e)
			{
				this.logger.severe("Server Log: | Account Creation Error. | Customer ID: " + customerID + " | " + e.getMessage());
				throw new invalid_client(e.getMessage());
			}	
		}//end if clause ... if not the same branch
		else
		{
			this.logger.severe("Server Log: | Account Creation Error: BranchID Mismatch | Customer ID: " + customerID);
			throw new invalid_client("Server Error: | Account Creation Error: BranchID Mismatch | Customer ID: " + customerID);		
		}
		
		return true;
	}

	@Override
	public boolean editRecord(String customerID, common.EditRecordFields fieldName, String newValue)
	        throws invalid_client
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deposit(String customerID, float amount) throws invalid_client, invalid_bankOperation
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void withdraw(String customerID, float amount) throws invalid_client, invalid_bankOperation
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getBalance(String customerID) throws invalid_client
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean transferFund(String sourceID, float amount, String destID) throws invalid_client, invalid_bankOperation
	{
		return false;	
	}

	@Override
	public int getUDPPort()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getUDPHost()
	{
		// TODO Auto-generated method stub
		return null;
	}

	//Will destory ORB and stop UDPServer from listening requests.
	@Override
	public void shutdown()
	{
		this.orb.shutdown(false);
		this.UDPServer.stop();		
	}

	@Override
	public int getLocalAccountCount()
	{
		return getClientCount();
	}

	public BranchID getBranchID()
	{
		return branchID;
	}

	public int getClientCount()
	{
		return clientCount;
	}

	public void setOrb(ORB orb)
	{
		this.orb = orb;
	}
	
	
}