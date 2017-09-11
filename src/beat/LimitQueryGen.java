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
 **/

package beat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Ravindra
 */
public class LimitQueryGen {
    
    FileReader fr = null;
    
    public LimitQueryGen(String conname){
        try {
            File file = new File("conn/" + conname + ".con");
            fr = new FileReader(file.getAbsoluteFile());
        } catch (FileNotFoundException ex) {
            new ExceptionUI(ex);
        }
       }
    
    public String getLimitQuery(String qry, String limit) throws IOException{
           
        String limitq="";

            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String url_line = br.readLine();
            
            if(url_line.contains("db2")||url_line.contains("derby")){
                
                limitq = qry + " FETCH FIRST "+limit+" ROWS ONLY with UR";
                
                
            }
            else if(url_line.contains("oracle")){
                
                if(qry.toLowerCase().contains("where")){
                    
                    limitq = qry + " AND ROWNUM <= "+limit;
                    
                }
                else{
                    limitq = qry + " WHERE ROWNUM <= "+limit;
                }
            }
            else if(url_line.contains("sqlserver")){
             
                limitq= "select TOP "+limit + " ";
                limitq = qry.toLowerCase().replaceFirst("select ",  limitq);
                
            }
            else
            {
                limitq = qry + " LIMIT "+limit;
            }
            
        
      return limitq;
    }
    
}
