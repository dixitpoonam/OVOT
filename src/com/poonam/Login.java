package com.poonam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/login")
public class Login {
	public final String DB_DRIVER = "org.gjt.mm.mysql.Driver";

	public final String DB_URL1 = "jdbc:mysql://103.241.181.36"; // db_CustomerComplaints
	public final String DB_USER1 = "diary";
	public final String DB_PASS1 = "d@1ry";

	public final String DB_URL2 = "jdbc:mysql://173.234.153.82"; // db_gps
	public final String DB_USER2 = "diary";
	public final String DB_PASS2 = "d@1ry";

	Connection conn1 = null;
	Connection conn2 = null;
	Statement stmt1 = null;
	Statement stmt2 = null;
	ResultSet res1 = null;
	ResultSet res2 = null;

	@GET
	@Produces({ "application/xml" })
	public Response getTransporterList(@QueryParam("Username") String uname, @QueryParam("Password") String password,@QueryParam("format") String format) throws SQLException
	{

		System.out.println("UserName=" + uname);
		System.out.println("Password=" + password);
		System.out.println("Format=" + format);

		boolean auth = false;
		boolean data = false;

		String dev = "<Transporters>";
		String dev3 = "[";
		String dev4 = "]";
		String output = "";
		String username = "";
		String transporter = "";
		int sr = 1;

		JSONObject jsonMain = new JSONObject();
		JSONObject jsonsub = new JSONObject();

		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException cnfex) {
			cnfex.printStackTrace();
		}

			System.out.println("Developing connection......");
			conn1 = DriverManager.getConnection(DB_URL1, DB_USER1, DB_PASS1);
			conn2 = DriverManager.getConnection(DB_URL2, DB_USER2, DB_PASS2);

			stmt1 = conn1.createStatement();
			String sql1 = "select * from db_CustomerComplaints.t_admin where UName='" + uname + "' and pass='"
					+ password + "' and Active='Yes'";
			System.out.println("Authntication Query:" + sql1);
			res1 = stmt1.executeQuery(sql1);
			if (res1.next()) {
				username = res1.getString("Name");
				System.out.println("User:" + username);
				auth = true;
				System.out.println("Login Success....");

				stmt2 = conn2.createStatement();
				String sql2 = "select distinct(TypeValue),OperationalStatus from db_gps.t_transporter where OperationalStatus ='Active' order by typevalue";
				System.out.println("TransporterData fetch Query:" + sql2);
				res2 = stmt2.executeQuery(sql2);
				while (res2.next()) {
					transporter = res2.getString("TypeValue");
					data = true;
					System.out.println(transporter);
					
					if(format.equals("json")) {
					JSONArray jsonMainArray = new JSONArray(); 
					jsonsub.put("Transporter",transporter);
					jsonMain.put("[", jsonsub);
					jsonMainArray.put(jsonMain);
					dev = String.valueOf(dev) + dev3 + jsonsub;
					}
					else
					{	  if(sr==1) {
							dev = dev+dev3;
							}
		                  dev = dev +"<Transporter>" + transporter + "</Transporter>";
		                  output = dev + "</Transporters>";
					}
					sr++;
				}
				output = String.valueOf(dev) + dev4 + "</Transporters>";   
			}
			else
			{
				System.out.println("login failed.....");
			}
	

			if (auth) {
				if(format.equals("json")) {
				output = output.replaceAll("\\<", "");
				output = output.replaceAll("Transporters", "");
				output = output.replaceAll("\\>", "");
				output = output.replaceAll("\\}\\[\\{", "},{");
				output = output.replaceAll("\\/", "");
				output = output.replaceAll("Transporters  ", "");
				output = output.replaceAll("\\>", "");
				return Response.ok(output, "application/json").build();   //json response
			}
				return Response.ok("<?xml version=\"1.0\"?>" + output, "application/xml").build();  //xml response	
			}
			// authentication failure
			output = "<error>Wrong Username or Password</error>";
			return Response.ok("<?xml version=\"1.0\"?>" + output, "application/xml").build();

	}//end of method
}

