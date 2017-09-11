/** Copyright © 2017-2020, GSPANN Technologies and/or its affiliates. All rights reserved. * */
package beat;

import net.sf.jsqlparser.JSQLParserException;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;

/**
 *
 * @author Ravindra
 */
public class QueryGenerator {

    private SqlParser sqlParser;
    private String qry;
    private Formatter formatter;

    public QueryGenerator() {
        sqlParser = new SqlParser();
        formatter = new BasicFormatterImpl();
    }

    public String getTotalCntQueries(String schema, String tblname, String data, String qrGen, String hostType, String trgRule) {
        System.out.println("qrGen: " + trgRule);
        if (qrGen.equalsIgnoreCase("db")) {
            System.out.println("DB Done");
            if (hostType.equalsIgnoreCase("trg")) {
                System.out.println("Type Done");
                if (!trgRule.isEmpty()) {
                    if (!trgRule.toLowerCase().contains("where")) {
                        qry = "select count(1) from " + schema + "." + tblname + " where " + trgRule;
                    } else {
                        qry = "select count(1) from " + schema + "." + tblname + " " + trgRule;
                    }

                } else {
//                    qry = "select count(1) from " + schema + "." + tblname;
                    qry = "select count(1) from (" + data + " ) " + tblname;
                }
            } else {
                qry = "select count(1) from (" + data + " ) " + tblname;

            }
        } else {

//            if (!trgRule.isEmpty()) {
            if (!trgRule.toLowerCase().contains("where")) {
                qry = "select count(1) from " + tblname + " where " + trgRule;
            } else {
                qry = "select count(1) from " + tblname + " " + trgRule;
            }
//            } else {
//                qry = "select count(1) from " + tblname + " ";
//            }
        }
        return formatter.format(qry);

    }

    public String getNullCntQueries(String schema, String tblname, String data, String type, String hostType, String trgRule) throws JSQLParserException {
        System.out.println("Trg Rule: " + trgRule);
        if (type.equalsIgnoreCase("db")) {
            if (hostType.equalsIgnoreCase("trg")) {
                if (!trgRule.isEmpty()) {
                    if (!trgRule.toLowerCase().contains("where")) {
                        qry = "select count(1) as " + data.trim() + " from " + schema + "." + tblname + " where " + data.trim() + " is null  or " + data.trim() + "='' and " + trgRule;
                    } else {
                        qry = "select count(1) as " + data.trim() + " from " + schema + "." + tblname + " " + trgRule + "  " + data.trim() + " is null  or " + data.trim() + "=''";
                    }
                } else {
                    qry = "select count(1) as " + data.trim() + " from " + schema + "." + tblname + " where " + data.trim() + " is null or " + data.trim() + "=''";
                }
            } //Check the Data
            else {
                qry = "select count(1) as " + sqlParser.getColumnNamefromQuery(data) + " from ( " + data + ") " + tblname + " where " + sqlParser.getColumnNamefromQuery(data) + " is null or " + sqlParser.getColumnNamefromQuery(data) + "=''";

            }
        } else {

            if (hostType.equalsIgnoreCase("trg")) {
                if (trgRule.isEmpty()) {
                    qry = "select count(1) as " + data.trim() + " from " + tblname + " where lower(" + data.trim() + ") = 'null'  or lower(" + data.trim() + ") =''";;
                } else {
                    if (trgRule.toLowerCase().startsWith("where")) {
                        qry = "select count(1) as " + data.trim() + " from " + tblname + " " + trgRule + " and (lower(" + data.trim() + ") = 'null'  or lower(" + data.trim() + ") ='')";
                    } else {
                        qry = "select count(1) as " + data.trim() + " from " + tblname + " where lower(" + data.trim() + ") = 'null'  or lower(" + data.trim() + ") ='' and " + trgRule;
                    }
                }
            } else {
                String tmp = data.trim().replaceAll("/", ".");
                System.out.println("Replace: " + tmp);

                if (trgRule.isEmpty()) {
                    qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from " + tblname + " where " + sqlParser.getColumnNamefromQuery(tmp) + " = 'null'  or lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") =''";
                } else {
                    if (trgRule.toLowerCase().startsWith("where")) {
                        qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from " + tblname + "  " + trgRule + " and (" + sqlParser.getColumnNamefromQuery(tmp) + " = 'null'  or lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") ='')";
                    } else {
                        qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from " + tblname + "  where (" + sqlParser.getColumnNamefromQuery(tmp) + " = 'null'  or lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") ='') and " + trgRule;

                    }
                }

            }
        }

        return qry;

    }

    public String getNotNullCntQueries(String schema, String tblname, String data, String type, String hostType, String trgRule) throws JSQLParserException {

        if (type.equalsIgnoreCase("db")) {
            if (hostType.equalsIgnoreCase("trg")) {
                if (!trgRule.isEmpty()) {
                    if (!trgRule.toLowerCase().contains("where")) {
                        qry = "select count(1) as " + data.trim() + " from " + schema + "." + tblname + " where " + data.trim() + " is not null  and " + data.trim() + "!='' or " + trgRule;
                    } else {
                        qry = "select count(1) as " + data.trim() + " from " + schema + "." + tblname + " " + trgRule + "  " + data.trim() + " is not null and " + data.trim() + "!='' ";
                    }
                } else {
                    qry = "select count(1) as " + data.trim() + " from " + schema + "." + tblname + " where " + data.trim() + " is not null and " + data.trim() + "!=''";
                }
            } else {
                qry = "select count(1) as " + sqlParser.getColumnNamefromQuery(data) + " from (" + data + ")" + tblname + " where " + sqlParser.getColumnNamefromQuery(data) + " is  not null and " + sqlParser.getColumnNamefromQuery(data) + "!=''";
            }
        } else {
            if (hostType.equalsIgnoreCase("trg")) {
                qry = "select count(" + data.trim() + ") as " + data.trim() + " from " + tblname + " where lower(" + data.trim() + ") != 'null' and lower(" + data.trim() + ") !='' ";

                if (trgRule.isEmpty()) {
                    qry = "select count(" + data.trim() + ") as " + data.trim() + " from " + tblname + " where lower(" + data.trim() + ") != 'null' and lower(" + data.trim() + ") !='' ";
                } else {

                    if (trgRule.toLowerCase().startsWith("where")) {
                        qry = "select count(" + data.trim() + ") as " + data.trim() + " from " + tblname + " " + trgRule + " and ( lower(" + data.trim() + ") != 'null' and lower(" + data.trim() + ") !='' )";
                    } else {
                        qry = "select count(" + data.trim() + ") as " + data.trim() + " from " + tblname + " where (lower(" + data.trim() + ") != 'null' and lower(" + data.trim() + ") !='' ) and " + trgRule;
                    }
                }

            } else {
                String tmp = data.trim().replaceAll("/", ".");
                System.out.println("Replace: " + tmp);
//                qry = "select count("+sqlParser.getColumnNamefromQuery(tmp)+") as " + sqlParser.getColumnNamefromQuery(tmp) + " from ( " + data + ") " + tblname + " where " + sqlParser.getColumnNamefromQuery(tmp) + " is null";

//                qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + " where lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") != 'null' and lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") != ''";
                if (trgRule.isEmpty()) {
                    qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + " where lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") != 'null' and lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") != ''";
                } else {

                    if (trgRule.toLowerCase().startsWith("where")) {

                        qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + " " + trgRule + " and lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") != 'null' and lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") != '')";
                    } else {
                        qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + " where (lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") != 'null' and lower(" + sqlParser.getColumnNamefromQuery(tmp) + ") != '') and " + trgRule;
                    }
                }
            }

        }
        return qry;
//        return formatter.format(qry);

    }

    public String getDistinctCntQueries(String schema, String tblname, String data, String type, String hostType, String trgRule) throws JSQLParserException {

        if (type.equalsIgnoreCase("db")) {
            if (hostType.equalsIgnoreCase("src")) {
                qry = "select count(distinct(" + sqlParser.getColumnNamefromQuery(data) + ")) as " + sqlParser.getColumnNamefromQuery(data) + "  from (" + data + ")" + tblname;
            } else {
                if (!trgRule.isEmpty()) {
                    if (!trgRule.toLowerCase().contains("where")) {
                        qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + schema + "." + tblname + " where " + trgRule;
                    } else {
                        qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + schema + "." + tblname + "  " + trgRule;
                    }
                } else {
                    qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + schema + "." + tblname;
                }
            }
        } else {
//            qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname;

            if (hostType.equalsIgnoreCase("trg")) {
                qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname;

                if (trgRule.isEmpty()) {
                    qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname;
                } else {

                    if (trgRule.toLowerCase().startsWith("where")) {

                        qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname + " " + trgRule;
                    } else {
                        qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname + " where " + trgRule;
                    }
                }

            } else {
                String tmp = data.trim().replaceAll("/", ".");
//                qry = "select count("+sqlParser.getColumnNamefromQuery(tmp)+") as " + sqlParser.getColumnNamefromQuery(tmp) + " from ( " + data + ") " + tblname + " where " + sqlParser.getColumnNamefromQuery(tmp) + " is null";

//                qry = "select count(distinct(" + sqlParser.getColumnNamefromQuery(tmp) + ")) as " + sqlParser.getColumnNamefromQuery(tmp) + " from " + tblname;
                if (trgRule.isEmpty()) {
                    qry = "select count(distinct(" + sqlParser.getColumnNamefromQuery(tmp) + ")) as " + sqlParser.getColumnNamefromQuery(tmp) + " from " + tblname;
                } else {

                    if (trgRule.toLowerCase().startsWith("where")) {

                        qry = "select count(distinct(" + sqlParser.getColumnNamefromQuery(tmp) + ")) as " + sqlParser.getColumnNamefromQuery(tmp) + " from " + tblname + " " + trgRule;
                    } else {
                        qry = "select count(distinct(" + sqlParser.getColumnNamefromQuery(tmp) + ")) as " + sqlParser.getColumnNamefromQuery(tmp) + " from " + tblname + " where " + trgRule;
                    }
                }

            }
        }

        return qry;
//        return formatter.format(qry);

    }

    public String getDuplicateCntQueries(String schema, String tblname, String data, String type, String hostType, String trgRule) throws JSQLParserException {

        if (type.equalsIgnoreCase("db")) {
            if (hostType.equalsIgnoreCase("src")) {

                qry = "select count(" + sqlParser.getColumnNamefromQuery(data) + ")- count(distinct(" + sqlParser.getColumnNamefromQuery(data) + ")) as " + sqlParser.getColumnNamefromQuery(data) + "  from (" + data + ")" + tblname;
            } else {
                if (!trgRule.isEmpty()) {
                    if (!trgRule.toLowerCase().contains("where")) {
                        qry = "select count(" + data + ")- count(distinct(" + data + ")) as " + data.trim() + " from " + schema + "." + tblname + " where " + trgRule;
                    } else {
                        qry = "select count(" + data + ")- count(distinct(" + data + ")) as " + data.trim() + " from " + schema + "." + tblname + "  " + trgRule;
                    }
                } else {
                    qry = "select count(" + data + ")- count(distinct(" + data + ")) as " + data.trim() + " from " + schema + "." + tblname;
                }
            }
        } else {
//            qry = "select count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname;

            if (hostType.equalsIgnoreCase("trg")) {
                qry = "select count(" + data.trim() + ") - count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname;

                if (trgRule.isEmpty()) {
                    qry = "select count(" + data.trim() + ") - count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname;
                } else {

                    if (trgRule.toLowerCase().startsWith("where")) {

                        qry = "select count(" + data.trim() + ") - count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname + " " + trgRule;
                    } else {
                        qry = "select count(" + data.trim() + ") - count(distinct(" + data.trim() + ")) as " + data.trim() + " from " + tblname + " where " + trgRule;
                    }
                }

            } else {
                String tmp = data.trim().replaceAll("/", ".");
//                qry = "select count("+sqlParser.getColumnNamefromQuery(tmp)+") as " + sqlParser.getColumnNamefromQuery(tmp) + " from ( " + data + ") " + tblname + " where " + sqlParser.getColumnNamefromQuery(tmp) + " is null";

//                qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") - count(distinct(" + sqlParser.getColumnNamefromQuery(tmp) + ")) as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname;
                if (trgRule.isEmpty()) {
                    qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") - count(distinct(" + sqlParser.getColumnNamefromQuery(tmp) + ")) as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname;
                } else {

                    if (trgRule.toLowerCase().startsWith("where")) {

                        qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") - count(distinct(" + sqlParser.getColumnNamefromQuery(tmp) + ")) as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + " " + trgRule;
                    } else {
                        qry = "select count(" + sqlParser.getColumnNamefromQuery(tmp) + ") - count(distinct(" + sqlParser.getColumnNamefromQuery(tmp) + ")) as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + " where " + trgRule;
                    }
                }
            }

        }

        return qry;
//        return formatter.format(qry);

    }

    public String getMaxColQueries(String schema, String tblname, String data, String type, String hostType, String trgRule) throws JSQLParserException {

        if (type.equalsIgnoreCase("db")) {
            if (hostType.equalsIgnoreCase("src")) {
                qry = "select max(" + sqlParser.getColumnNamefromQuery(data) + ") as " + sqlParser.getColumnNamefromQuery(data) + " from (" + data + ")" + tblname;
            } else {
                if (!trgRule.isEmpty()) {
                    if (!trgRule.toLowerCase().contains("where")) {
                        qry = "select max(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname + "  where " + trgRule;
                    } else {
                        qry = "select max(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname + "  " + trgRule;
                    }
                } else {
                    qry = "select max(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname;
                }

            }
        } else {
//            qry = "select max(" + data.trim() + ") as " + data.trim() + " from " + tblname;

            if (hostType.equalsIgnoreCase("trg")) {
//                qry = "select max(" + data.trim() + ") as " + data.trim() + " from " + tblname;

                if (trgRule.isEmpty()) {
                    qry = "select max(" + data.trim() + ") as " + data.trim() + " from " + tblname;
                } else {

                    if (trgRule.toLowerCase().startsWith("where")) {

                        qry = "select max(" + data.trim() + ") as " + data.trim() + " from " + tblname + " " + trgRule;
                    } else {
                        qry = "select max(" + data.trim() + ") as " + data.trim() + " from " + tblname + " where " + trgRule;
                    }
                }

            } else {
                String tmp = data.trim().replaceAll("/", ".");
                System.out.println("Replace: " + tmp);
//                qry = "select count("+sqlParser.getColumnNamefromQuery(tmp)+") as " + sqlParser.getColumnNamefromQuery(tmp) + " from ( " + data + ") " + tblname + " where " + sqlParser.getColumnNamefromQuery(tmp) + " is null";

//                qry = "select max(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname;
                if (trgRule.isEmpty()) {
                    qry = "select max(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname;
                } else {

                    if (trgRule.toLowerCase().startsWith("where")) {

                        qry = "select max(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + " " + trgRule;
                    } else {
                        qry = "select max(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + " where " + trgRule;
                    }
                }
            }

        }

        return qry;
//        return formatter.format(qry);

    }

    public String getMinColQueries(String schema, String tblname, String data, String type, String hostType, String trgRule) throws JSQLParserException {

        if (type.equalsIgnoreCase("db")) {
            if (hostType.equalsIgnoreCase("src")) {
                qry = "select min(" + sqlParser.getColumnNamefromQuery(data) + ") as " + sqlParser.getColumnNamefromQuery(data) + " from (" + data + ")" + tblname;
            } else {
                if (!trgRule.isEmpty()) {
                    if (!trgRule.toLowerCase().contains("where")) {
                        qry = "select min(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname + " where " + trgRule;
                    } else {
                        qry = "select min(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname + "  " + trgRule;
                    }
                } else {
                    qry = "select min(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname;
                }
            }
        } else {
//            qry = "select min(" + data.trim() + ") as " + data.trim() + " from " + tblname;
            if (hostType.equalsIgnoreCase("trg")) {
//                qry = "select min(" + data.trim() + ") as " + data.trim() + " from " + tblname;

                if (trgRule.isEmpty()) {
                    qry = "select min(" + data.trim() + ") as " + data.trim() + " from " + tblname;
                } else {

                    if (trgRule.toLowerCase().startsWith("where")) {

                        qry = "select min(" + data.trim() + ") as " + data.trim() + " from " + tblname + " " + trgRule;
                    } else {
                        qry = "select min(" + data.trim() + ") as " + data.trim() + " from " + tblname + " where " + trgRule;
                    }
                }
            } else {
                String tmp = data.trim().replaceAll("/", ".");
                System.out.println("Replace: " + tmp);
//                qry = "select count("+sqlParser.getColumnNamefromQuery(tmp)+") as " + sqlParser.getColumnNamefromQuery(tmp) + " from ( " + data + ") " + tblname + " where " + sqlParser.getColumnNamefromQuery(tmp) + " is null";

//                qry = "select min(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname;
                if (trgRule.isEmpty()) {
                    qry = "select min(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname;
                } else {

                    if (trgRule.toLowerCase().startsWith("where")) {

                        qry = "select min(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + " " + trgRule;
                    } else {
                        qry = "select min(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + " where " + trgRule;
                    }
                }
            }

        }
        return qry;
//        return formatter.format(qry);

    }

    public String getSumColQueries(String schema, String tblname, String data, String type, String hostType, String dtype, String trgRule) throws JSQLParserException {

        if (type.equalsIgnoreCase("db")) {
            if (hostType.equalsIgnoreCase("src")) {
                qry = "select sum(" + sqlParser.getColumnNamefromQuery(data) + ") as " + sqlParser.getColumnNamefromQuery(data) + " from (" + data + ")" + tblname;
            } else {
                if (!trgRule.isEmpty()) {
                    if (!trgRule.toLowerCase().contains("where")) {
                        qry = "select sum(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname + " where " + trgRule;
                    } else {
                        qry = "select sum(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname + "  " + trgRule;
                    }
                } else {
                    qry = "select sum(" + data.trim() + ") as " + data.trim() + " from " + schema + "." + tblname;
                }
            }
        } else {

            if (hostType.equalsIgnoreCase("trg")) {
                if (trgRule.isEmpty()) {
                    qry = "select sum(" + data.trim() + ") as " + data.trim() + " from " + tblname;
                } else {
                    if (!trgRule.toLowerCase().contains("where")) {
                        qry = "select sum(" + data.trim() + ") as " + data.trim() + " from " + tblname + " where " + trgRule;
                    } else {
                        qry = "select sum(" + data.trim() + ") as " + data.trim() + " from " + tblname + "  " + trgRule;
                    }
                }
            } else {
                String tmp = data.trim().replaceAll("/", ".");
//                qry = "select sum(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname;
  if (trgRule.isEmpty()) {
                    qry = "select sum(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname;
                } else {
                    if (!trgRule.toLowerCase().contains("where")) {
                        qry = "select sum(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname+ " where " + trgRule;
                    } else {
                        qry = "select sum(" + sqlParser.getColumnNamefromQuery(tmp) + ") as " + sqlParser.getColumnNamefromQuery(tmp) + " from  " + tblname + "  " + trgRule;
                    }
                }
            }
        }

//        return formatter.format(qry);
        return qry;

    }

    public boolean validateSpecialConditon(String data) {

        boolean flag = false;

        String[] notallowed = {"count", "sum", "max", "min", "avg"};

        for (String item : notallowed) {

            if (data.toLowerCase().contains(item)) {
                flag = false;
                break;
            } else {
                flag = true;
            }

        }

        return flag;
    }

}
