<%@page import="java.sql.*"%>

<%
String tref=request.getParameter("tref");
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection conn=DriverManager.getConnection(url,"knagpure","ptojilug");
Statement stmt=conn.createStatement();
ResultSet rs=stmt.executeQuery("select t_date,t_points,prod_name,prod_points,quantity from transactions t join transactions_products tp on t.tref=tp.tref join products p on tp.prod_id=p.prod_id where t.tref='"+tref+"'");
String output="";
while(rs.next())
{
    output+=rs.getObject(1)+","+rs.getObject(2)+","+rs.getObject(3)+","+rs.getObject(4)+","+rs.getObject(5)+"#";
}
out.print(output);
conn.close();
%>

