<%@page import="java.sql.*"%>

<%
int cid=Integer.parseInt(request.getParameter("cid"));
String tref=request.getParameter("tref");
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn=DriverManager.getConnection(url,"knagpure","ptojilug");
Statement stmt=conn.createStatement();
ResultSet rs=stmt.executeQuery("select c.family_id,pa.percent_added,t.t_points from customers c join Point_Accounts pa on c.cid=pa.cid join Transactions t on c.cid=t.cid where c.cid="+cid+" and t.tref='"+tref+"'");
String output="";
while(rs.next())
{
    output+=rs.getObject(1)+","+rs.getObject(2)+","+rs.getObject(3)+"#";
    break;
}
out.print(output);
conn.close();
%>
