<%@page import="java.sql.*"%>

<%
int cid=Integer.parseInt(request.getParameter("cid"));
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn=DriverManager.getConnection(url,"knagpure","ptojilug");
Statement stmt=conn.createStatement();
ResultSet rs=stmt.executeQuery("select tref,t_date,t_points,amount from transactions where cid='"+cid+"'");
String output="";
while(rs.next())
{
    output+=rs.getObject(1)+","+rs.getObject(2)+" "+rs.getObject(3)+" "+rs.getObject(4)+"#";
    break;
}
out.print(output);
conn.close();
%>

