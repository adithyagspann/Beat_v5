/** Copyright © 2017-2020, GSPANN Technologies and/or its affiliates. All rights reserved. * */
package beat;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

/**
 *
 * @author Adithya
 */

/*Checking the SQL Queries */
public class SqlParser {

    private CCJSqlParserManager parserManager;
    private PlainSelect plainSelect;

    public SqlParser() {
        parserManager = new CCJSqlParserManager();
    }

    //Checking the SQLQuery
    public boolean checkSqlQuery(String sql) throws JSQLParserException {

        Statement statement = parserManager.parse(new StringReader(sql));
        return true;

    }

    public String getColumnNamefromQuery(String sql) throws JSQLParserException {
        
        Select select = (Select) parserManager.parse(new StringReader(sql));
        plainSelect = (PlainSelect) select.getSelectBody();

        StringBuilder sb = new StringBuilder(plainSelect.getSelectItems().toString());
        sb.reverse().replace(0, sb.length(), sb.toString().split(" ")[0]).reverse();

        return sb.toString().replace("]", "").replace("[", "");
    }

    public String getTableNameFromQuery(String sql) throws JSQLParserException {
        Select select = (Select) parserManager.parse(new StringReader(sql));
        plainSelect = (PlainSelect) select.getSelectBody();

        System.out.println("Table: " + plainSelect.getFromItem());
        String table = plainSelect.getFromItem().toString();

        return table.substring(table.indexOf(".") + 1, table.length());
    }

}
