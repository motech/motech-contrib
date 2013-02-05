package org.motechproject.diagnostics.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtils {
    public static List<Map<String, Object>> query(Connection conn, String sql) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        ResultSet resultSet = null;
        try{
            resultSet = conn.createStatement().executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();

            List<String> columnNames = new ArrayList<>();
            for(int i=1; i <= rsmd.getColumnCount(); i++){
                columnNames.add(rsmd.getColumnLabel(i));
            }

            while(resultSet.next()){
                Map<String, Object> result = new HashMap<>();
                for(String column : columnNames){
                    result.put(column, resultSet.getObject(column));
                }
                results.add(result);
            }
        }
        finally{
            resultSet.close();
        }
        return results;
    }
}
