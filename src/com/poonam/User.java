package com.poonam;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

//import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
//import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Statement;

@Path("/user")
public class User
{
	
	@GET
	@Produces({"application/xml"})   //MediaType.APPLICATION_JSON
	public Response UserLogin(@QueryParam("email") String email,  @QueryParam("format") String cat)              
	{
	//	String url = "jdbc:mysql://164.52.208.36/PoonamDB";

		String url = "jdbc:mysql://localhost:3306/poonam";
		String user = "punam";
		String passwd = "Punam@123";
		String driver = "com.mysql.jdbc.Driver";
		
		String Status = "";
		 
		Connection conn=null;
		Statement stmt =null;
		
		String output = null;
		    boolean a = false;
		    //boolean b = false;
		    
		    //int i=0;
			
			email = email.replaceAll(" ", "");
		/* password = password.replaceAll(" ", ""); */
			
			
		    System.out.println("email : " + email);
		/* System.out.println("password" + password); */
		    System.out.println("cat : " + cat);
		    
		    
		    JSONObject jsonMain = new JSONObject();
		    
		    JSONObject jsonsub = new JSONObject();
		    
		   
		    String dev = "<User>";
		    String dev3 = "[";
		    String dev4 = "]";
		    
		  
		    String sql="select *  from login where email='"+email+"'";
		    try 
		    {
		    		Class.forName(driver);
					
				
					conn=DriverManager.getConnection(url,user,passwd);
					System.out.println("Developing connection...");
					stmt = conn.createStatement();
					
					ResultSet rs = stmt.executeQuery(sql);
			   
					System.out.println("Sql=====>" + sql);
			    
					if (rs.next())
					{
						a=true;
						Status = "successful";
						System.out.println("Successful");	
					}
					else
					{
						Status="failure";
						System.out.println("failure");
					}
			}
			catch (Exception e) 
			{
				
				e.printStackTrace();
			}
		            
		      
		
		  if (cat.equals("xml")) 
		  { 
			  System.out.println("In xml Format..");
			  
			  dev = dev + "<User><Status>" + Status + "</Status></User>";
			  output = dev + "</User>"; 
		  } 
		  else
		  {
		              System.out.println("In Json Format..");
		              JSONArray jasonMainArray = new JSONArray();
		                  
		              jsonsub.put("Status", Status);
		                  
		              jsonMain.put("[", jsonsub);
		                  
		              jasonMainArray.put(jsonMain);
		                  
		              dev = dev + dev3 + jsonsub;
		  }
		              
		            
		              output = dev + dev4 + "</User>";
		            
		           
		   
		/*
		 * if (!b) { output = "<error>UserName or Password invalid</error>"; return
		 * Response.ok("<?xml version=\"1.0\"?>" + output, "application/xml").build(); }
		 */
		              
		    if (a)
		    {
			      if (cat.equals("json"))
			      {
			        output = output.replaceAll("\\<", "");
			        output = output.replaceAll("User", "");
			        output = output.replaceAll("\\>", "");
			        output = output.replaceAll("\\}\\[\\{", "},{");
			        output = output.replaceAll("\\/", "");
			        output = output.replaceAll("User", "");
			        output = output.replaceAll("\\>", "");
			        return Response.ok(output, "application/json").build();
			      }
			      return Response.ok("<?xml version=\"1.0\"?>" + output, "application/xml").build();
		    }
		    
		    output = "<error>UserName or Password invalid</error>";
		    return Response.ok("<?xml version=\"1.0\"?>" + output, "application/xml").build();
		  }
	}





