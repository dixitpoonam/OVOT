
package com.poonam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;


@Path("/changepassword")
public class ChangePassword 
{
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response changePassword(@QueryParam("email") String email, @QueryParam("oldPassword") String OldPassword, @QueryParam("newPassword") String NewPassword, @QueryParam("format") String cat)                               
	{
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
		    System.out.println("email :  " + email);
		    System.out.println("OldPassword : " + OldPassword);
		    System.out.println("NewPassword : " + NewPassword);
		    System.out.println("cat : " + cat);
		    
		    email = email.replaceAll(" ", "");
			OldPassword = OldPassword.replaceAll(" ", ""); 
			NewPassword = NewPassword.replaceAll(" ", "");
			
			
		    
		    
		    JSONObject jsonMain = new JSONObject();
		    
		    JSONObject jsonsub = new JSONObject();
		    
		   
		    String dev = "<User>";
		    String dev3 = "[";
		    String dev4 = "]";
		    
		  
		    
		    try 
		    {
		    		Class.forName(driver);
					
				
					conn=DriverManager.getConnection(url,user,passwd);
					stmt = conn.createStatement();
					
					String sql="select * from login where email='"+email+"'";
					
					ResultSet rs = stmt.executeQuery(sql);
			   
					System.out.println("Sql=====>" + sql);
			    
					if (rs.next())
					{
						a=true;
						
						
						String sqlUpdate = "Update login set password='"+NewPassword+"'  where email='"+email+"' ";
						System.out.println("sqlUpdate=====>" + sqlUpdate);
						
						int i = stmt.executeUpdate(sqlUpdate);
						
						if(i==1)
						{
							Status="Password changed successfully..........";
						}
						
						System.out.println("Password changed Successfully");
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
			  
			  dev = dev + "<User><OldPassword>" + OldPassword + "</OldPassword><NewPassword>" + NewPassword + "</NewPassword></User>";
			  output = dev + ""; 
		  } 
		  else
		  {
			  System.out.println("In Json Format..");
              JSONArray jasonMainArray = new JSONArray();
              
              jsonsub.put("OldPassword", OldPassword);
              jsonsub.put("NewPassword", NewPassword);
              
              
              jsonMain.put("[", jsonsub);
              
              jasonMainArray.put(jsonMain);
              
              dev = dev + dev3 + jsonsub;        
		  }
		              
		            
		              output = dev + dev4 + "</User>";
		            
		           
		           
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




