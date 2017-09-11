/** Copyright © 2017-2020, GSPANN Technologies and/or its affiliates. All rights reserved.
 *
 * This software and related documentation are provided under a license agreement containing restrictions on use and
 * disclosure and are protected by intellectual property laws. Except as expressly permitted in your license agreement
 * or allowed by law, you may not use, copy, reproduce, translate, broadcast, modify, license, transmit, distribute,
 * exhibit, perform, publish, or display any part, in any form, or by any means. Reverse engineering, disassembly
 * or decompilation of this software, unless required by law for interoperability, is prohibited.
 *
 * The information contained herein is subject to change without notice and is not warranted to be error-free.
 * If you find any errors, please report them to us in writing.
 *
 * If this software or related documentation is delivered to the U.S. Government or anyone licensing it on behalf
 * of the U.S. Government, the following notice is applicable:
 *
 * U.S. GOVERNMENT RIGHTS Programs, software, databases, and related documentation and technical data delivered to U.S.
 * Government customers are "commercial computer software" or "commercial technical data" pursuant to the applicable Federal
 * Acquisition Regulation and agency-specific supplemental regulations. As such, the use, duplication, disclosure,
 * modification, and adaptation shall be subject to the restrictions and license terms set forth in the applicable Government
 * contract, and, to the extent applicable by the terms of the Government contract, the additional rights set forth in FAR 52.227-19,
 * Commercial Computer Software License (December 2007). GSPANN Technologies Inc., 362 Fairview Way, Milpitas, CA 95035, USA .
 *
 * This software is developed for general use in a variety of information management applications. It is not developed or
 * intended for use in any inherently dangerous applications, including applications which may create a risk of personal
 * injury. If you use this software in dangerous applications, then you shall be responsible to take all appropriate fail-safe,
 * backup, redundancy, and other measures to ensure the safe use of this software. Gspann and its affiliates disclaim any liability
 * for any damages caused by use of this software in dangerous applications.
 *
 * GSPANN is a registered trademark of GSPANN and/or its affiliates. Other names may be trademarks of their respective owners.
 *
 * This software and documentation may provide access to or information on content, products, and services from third parties.
 * Gspann and its affiliates are not responsible for and expressly disclaim all warranties of any kind with respect to third-party
 * content, products, and services. Gspann and its affiliates will not be responsible for any loss, costs, or damages incurred due
 * to your access to or use of third-party content, products, or services.
 * */
package beat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import net.sf.jsqlparser.JSQLParserException;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;

/**
 *
 * @author Ravindra
 */
public class STMDataValidationSrcQueryGenerator {

    private int i;

    public String getSrcDataValidationQuery(String[] srcColtranqry, String cmnjoin) throws JSQLParserException {

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
        return formatted_sql_code;
    }

    public List getJoinColumns(String cmnjoin) {

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

                    String col = sp.getColumnNamefromQuery(q);
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

                    String col = sp.getColumnNamefromQuery(q.replaceAll("/", "."));
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
