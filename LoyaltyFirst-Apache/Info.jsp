<%@page import="java.sql.*"%>

<%
int cid=Integer.parseInt(request.getParameter("cid"));
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn=DriverManager.getConnection(url,"knagpure","ptojilug");
Statement stmt=conn.createStatement();
ResultSet rs=stmt.executeQuery("select c.cname,pa.num_of_points from Customers c join Point_Accounts pa on c.cid=pa.cid where c.cid='"+cid+"'");
String output="";
while(rs.next())
{
    output+=rs.getObject(1)+","+rs.getObject(2)+"#";
}
out.print(output);
conn.close();
%>

