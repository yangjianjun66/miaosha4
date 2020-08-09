import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC
{
    @Test
    public void JDBCTest() throws ClassNotFoundException, SQLException {
        //加载注册驱动,就是说加载一个对应的驱动进来
        Class.forName("com.mysql.jdbc.Driver");
        //
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test01?serverTimezone=GMT%2B8",
                "root",
                "admin");
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE student SET Ssex = '女' WHERE SId = 01");
        st.close();
        conn.close();
    }
}
