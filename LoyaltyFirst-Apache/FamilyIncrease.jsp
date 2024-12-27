<%@page import="java.sql.*"%>

<%
int fid=Integer.parseInt(request.getParameter("fid"));
int cid=Integer.parseInt(request.getParameter("cid"));
int npoints=Integer.parseInt(request.getParameter("npoints"));
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn=DriverManager.getConnection(url,"knagpure","ptojilug");
Statement stmt=conn.createStatement();
try{
ResultSet rs=stmt.executeQuery("update point_accounts set num_of_points = '"+npoints+"' where family_id ='"+fid+"' and cid !='"+cid+"'");
out.print("Point accounts of the family members are updated successfully");
}
catch(Exception e){
    out.println(e);
}

conn.close();
%>