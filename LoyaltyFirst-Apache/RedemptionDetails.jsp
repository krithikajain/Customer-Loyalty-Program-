<%@page import="java.sql.*"%>

<%
int prizeid=Integer.parseInt(request.getParameter("prizeid"));
int cid=Integer.parseInt(request.getParameter("cid"));
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn=DriverManager.getConnection(url,"knagpure","ptojilug");
Statement stmt=conn.createStatement();
ResultSet rs=stmt.executeQuery("select p.p_description,p.points_needed,r.r_date,e.center_name from redemption_history r join prizes p on (r.prize_id=p.prize_id) join exchgcenters e on (r.center_id=e.center_id) where r.prize_id="+prizeid+" and r.cid="+cid);
String output="";
while(rs.next())
{
    output+=rs.getObject(1)+","+rs.getObject(2)+"#"+rs.getObject(3)+"@"+rs.getObject(4)+"#";
    break;
}
//out.print(output);
out.print(output);
conn.close();
%>

