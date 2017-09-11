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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Ravindra
 */
public final class LoadConnectionsTreeView {
    
    private TreeItem <String> connections,connt,databases,tables,views,dbs;
    private TreeView conntree;
    private String connpath = "conn/";
    private List connnames;
    private Image childIcon;
    private Image dbIcon;
    private Image schIcon;
    private Image tabIcon;
    private Image tfoldIcon;
    private Image vfoldIcon;
    
    public LoadConnectionsTreeView(TreeView conntree){
        
        this.conntree = conntree;
        ImageView rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/conns.png")));
        connections = new TreeItem<>("Connections",rootIcon);
        connections.setExpanded(true);
        
        connnames = getConnectionNames(connpath);
        childIcon = new Image(getClass().getResourceAsStream("/icon/cont.png")); 
        dbIcon = new Image(getClass().getResourceAsStream("/icon/dbsicon.png"));
        schIcon = new Image(getClass().getResourceAsStream("/icon/schema.png"));
        tabIcon = new Image(getClass().getResourceAsStream("/icon/tabicon.png"));
        tfoldIcon = new Image(getClass().getResourceAsStream("/icon/tfold.png"));
        vfoldIcon = new Image(getClass().getResourceAsStream("/icon/vfold.png"));
        for (Iterator it = connnames.iterator(); it.hasNext();) {
        Object itemString = it.next();
        //System.out.println(itemString);
        connt = new TreeItem<>(itemString.toString(),new ImageView(childIcon));
        databases = new TreeItem<>("Databases",new ImageView(dbIcon));
        connt.getChildren().add(databases);
        connections.getChildren().add(connt);
    }
    conntree.setRoot(connections); 
    }
    
    public void appendConnectionTreeView(String connname){
        
        connt = new TreeItem<>(connname,new ImageView(childIcon));
        databases = new TreeItem<>("Databases",new ImageView(dbIcon));
        connt.getChildren().add(databases);
        connections.getChildren().add(connt);
        conntree.setRoot(connections);
    }
    
    public void loadDBTreeView(List dblist,TreeItem ti){
        
        if(ti.getChildren().isEmpty()){
        ti.getChildren().clear();
        for (Iterator it = dblist.iterator(); it.hasNext();) {
        Object itemString = it.next();
        //System.out.println(itemString);
        dbs = new TreeItem<>(itemString.toString(),new ImageView(dbIcon));
        ti.getChildren().add(dbs);
        }
        ti.setExpanded(true);
        conntree.setRoot(connections);
        }
        
        
    }
    
    
    public void loadSchemaTreeView(List schlist,TreeItem ti){
        if(ti.getChildren().isEmpty()){
        ti.getChildren().clear();
        for (Iterator it = schlist.iterator(); it.hasNext();) {
        Object itemString = it.next();
        //System.out.println(itemString);
        connt = new TreeItem<>(itemString.toString(),new ImageView(schIcon));
        tables = new TreeItem<>("Tables",new ImageView(tfoldIcon));
        views = new TreeItem<>("Views",new ImageView(vfoldIcon));
        connt.getChildren().add(tables);
        connt.getChildren().add(views);
        ti.getChildren().add(connt);
        }
        ti.setExpanded(true);
        conntree.setRoot(connections);
        }
    }
    
     public void loadTableTreeView(List tablist,TreeItem ti){
        if(ti.getChildren().isEmpty()){
        ti.getChildren().clear();
        for (Iterator it = tablist.iterator(); it.hasNext();) {
        Object itemString = it.next();
        //System.out.println(itemString);
        connt = new TreeItem<>(itemString.toString(),new ImageView(tabIcon));
        ti.getChildren().add(connt);
        }
        ti.setExpanded(true);
        conntree.setRoot(connections); 
        }
    }
    
    
    public List getConnectionNames(String path){
        
        List connlist = new ArrayList();
        
        try {
                File directory = new File(path);
                File[] fList = directory.listFiles();
                
                for (File file : fList){
                    if (file.isFile()) {
                       // System.out.println(file.getAbsolutePath());
                        connlist.add(file.getName().split(".con")[0]);    
                    } 
                }
            } catch (SecurityException | IllegalArgumentException ex) {
                new ExceptionUI(ex);
            }
        
        return connlist;
        
    }
}
    
  