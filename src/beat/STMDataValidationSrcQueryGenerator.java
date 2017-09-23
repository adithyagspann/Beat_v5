/** Copyright © 2017-2020, GSPANN Technologies and/or its affiliates. All rights reserved. * */
package beat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import net.sf.jsqlparser.JSQLParserException;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ravindra
 */
public class STMDataValidationSrcQueryGenerator {

    private final static Logger LOGGER = LoggerFactory.getLogger(STMDataValidationSrcQueryGenerator.class);
    private int i;

    public String getSrcDataValidationQuery(String[] srcColtranqry, String cmnjoin) throws JSQLParserException {
        LOGGER.info("Validating the Source Data Validataion Query.");
        String datavalidqry = "";
        int tbl_incr = 0;
        List tmp_tbl_list = new ArrayList();
        List joincols = getJoinColumns(cmnjoin);

        for (String q : srcColtranqry) {

            if (!q.equalsIgnoreCase("#INPROGRESS")) {

                q = mergeJoinColsWithQuery(q, joincols);

                if (tbl_incr == 0) {

                    datavalidqry = "With " + "tmp_tbl_" + tbl_incr + " as (\n" + q.trim() + "), ";
                    tmp_tbl_list.add("tmp_tbl_" + tbl_incr);
                    tbl_incr++;
                } else {

                    datavalidqry = datavalidqry + "\n" + "tmp_tbl_" + tbl_incr + " as (\n" + q + "), ";
                    tmp_tbl_list.add("tmp_tbl_" + tbl_incr);
                    tbl_incr++;
                }
            }
        }

        datavalidqry = datavalidqry.trim().substring(0, datavalidqry.length() - 2);
        String final_data_join = finalDataJoinCreator(joincols, tmp_tbl_list);
        String final_select_qry = getFinalDataSelectQuery(srcColtranqry);
        datavalidqry = datavalidqry + final_select_qry + final_data_join;

        Formatter f = new BasicFormatterImpl();
        String formatted_sql_code = f.format(datavalidqry);

//        System.out.println("datavalidqry : " + formatted_sql_code);
        LOGGER.info("Validated the Source Data Validataion Query.");
        return formatted_sql_code;
    }

    public List getJoinColumns(String cmnjoin) {
        LOGGER.info("Validated the Source Data Validataion Query.");
        StringTokenizer token = new StringTokenizer(cmnjoin, " ");
        List tmpcolist = new ArrayList();
        List joincolist = new ArrayList();

        while (token.hasMoreTokens()) {

            String col = token.nextToken().trim();

            if (!col.equalsIgnoreCase("\n") && !col.equalsIgnoreCase("\n") && !col.equalsIgnoreCase("") && !col.isEmpty()) {

                if (col.matches("[a-z,A-Z,0-9,.,_]*")) {
                    tmpcolist.add(col);
                }
            }
        }

        //System.out.println(" Tmp Join Cols List : " + tmpcolist);
        String col;

        for (int i = 0; i < tmpcolist.size(); i++) {

            col = tmpcolist.get(i).toString().trim();

            if (col.equalsIgnoreCase("on")) {

                joincolist.add(tmpcolist.get(++i).toString().trim());
                i++;
                String key = tmpcolist.get(++i).toString().trim();

                while (key.equalsIgnoreCase("and") || key.equalsIgnoreCase("or")) {

                    joincolist.add(tmpcolist.get(++i).toString().trim());
                    i++;
                    key = tmpcolist.get(++i).toString().trim();

                }

            }
        }

        //removing duplicate cols in join col list
        Set<String> final_list = new HashSet<>();
        final_list.addAll(joincolist);
        joincolist.clear();
        joincolist.addAll(final_list);

        //System.out.println("Join Cols List : " + joincolist);
        return joincolist;
    }

    public String mergeJoinColsWithQuery(String qry, List joincols) {

        String s = "";

        if (joincols.size() > 1) {
            for (Object l : joincols) {
                s = l.toString() + ",\n" + s;
            }
            s = s.substring(0, s.length() - 1);
        } else {
            s = joincols.get(0).toString();
        }

        qry = qry.replaceFirst("from", ",\n " + s + "\nfrom ").trim();

        Formatter f = new BasicFormatterImpl();
        String formatted_sql_code = f.format(qry);

//        System.out.println("Merge Join Cols : " + formatted_sql_code);
        return formatted_sql_code;
    }

    public String finalDataJoinCreator(List joincols, List tmp_tbls) {

        String final_Data_Join = tmp_tbls.get(0).toString() + " inner join ";
        boolean flag = false;

        for (Object tbl : tmp_tbls) {

            if (tmp_tbls.indexOf(tbl) == 0) {
                continue;
            }

            if (joincols.size() > 1) {

                String jcol = joincols.get(0).toString();
                jcol = jcol.substring(jcol.indexOf(".") + 1, jcol.length());

                final_Data_Join = final_Data_Join + tbl.toString() + " on " + tmp_tbls.get(0).toString() + "." + jcol + " = " + tbl.toString() + "." + jcol;

                for (Object l : joincols) {
                    jcol = l.toString();
                    jcol = jcol.substring(jcol.indexOf(".") + 1, jcol.length());
                    final_Data_Join = final_Data_Join + " and " + tmp_tbls.get(0).toString() + "." + jcol + " = " + tbl.toString() + "." + jcol;
                }

                final_Data_Join = final_Data_Join + " inner join ";
            } else {
                String jcol = joincols.get(0).toString();
                jcol = jcol.substring(jcol.indexOf(".") + 1, jcol.length());
                final_Data_Join = final_Data_Join + tbl.toString() + " on " + tmp_tbls.get(0).toString() + "." + jcol + " = " + tbl.toString() + "." + jcol + " inner join ";
            }
        }

        final_Data_Join = final_Data_Join.trim().substring(0, final_Data_Join.length() - 11);
        // System.out.println("final join qry: "+final_Data_Join);

        return final_Data_Join;
    }

    public String getFinalDataSelectQuery(String[] srcColtranqry) throws JSQLParserException {

        SqlParser sp = new SqlParser();

        String finaldataselectqry = "select ";

        for (String s : srcColtranqry) {
            if (!s.equalsIgnoreCase("#INPROGRESS")) {

                finaldataselectqry = finaldataselectqry + sp.getColumnNamefromQuery(s) + ", ";

            }
        }

        finaldataselectqry = finaldataselectqry.substring(0, finaldataselectqry.trim().length() - 1);

        finaldataselectqry = finaldataselectqry + " from ";

        //System.out.println(finaldataselectqry);
        return finaldataselectqry;
    }

    public String getSrcDataValidationQuery(String[] srcColtranqry, String tblname, String schname, String cmnjoin, String hostType) throws JSQLParserException {

        SqlParser sp = new SqlParser();
        String datavalidqry = "Select ";
        String formatted_sql_code = null;
        if (hostType.equalsIgnoreCase("db")) {
            for (String q : srcColtranqry) {

                if (!q.equalsIgnoreCase("#INPROGRESS")) {

                    String col = sp.getColumnRulefromQuery(q);
                    datavalidqry = datavalidqry + col + ",";

                }
            }

            datavalidqry = datavalidqry.trim().substring(0, datavalidqry.length() - 1);

            datavalidqry = datavalidqry + " from " + schname + "." + tblname + " " + cmnjoin;

            Formatter f = new BasicFormatterImpl();
            formatted_sql_code = f.format(datavalidqry);

        } else {

            for (String q : srcColtranqry) {

                if (!q.equalsIgnoreCase("#INPROGRESS")) {
                    String col = sp.getColumnRulefromQuery(q);
//                    String col = sp.getColumnRulefromQuery(q.replaceAll("/", "."));
                    datavalidqry = datavalidqry + col + ",";

                }
            }

            datavalidqry = datavalidqry.trim().substring(0, datavalidqry.length() - 1);

            datavalidqry = datavalidqry + " from " + tblname + " " + cmnjoin;
            formatted_sql_code = datavalidqry;

        }
//        System.out.println("datavalidqry : " + formatted_sql_code);
        return formatted_sql_code;

    }

}
