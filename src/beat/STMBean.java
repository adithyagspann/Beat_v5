/** Copyright © 2017-2020, GSPANN Technologies and/or its affiliates. All rights reserved. **/

package beat;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Adithya
 */

/*Stores the Data of the STM specification */
public class STMBean {

    public SimpleStringProperty targetSchema = new SimpleStringProperty();
    public SimpleStringProperty targetTableName = new SimpleStringProperty();
    public SimpleStringProperty targetColumnName = new SimpleStringProperty();
    public SimpleStringProperty targetColumnSpec = new SimpleStringProperty();
    public SimpleStringProperty targetColumnKey = new SimpleStringProperty();
    public SimpleStringProperty sourceColumnSpec = new SimpleStringProperty();
    public SimpleStringProperty columnTransRule = new SimpleStringProperty();
   
    public String getTargetSchema() {
        return targetSchema.get();
    }

    public String getTargetTableName() {
        return targetTableName.get();
    }

    public String getTargetColumnName() {
        return targetColumnName.get();
    }

    public String getTargetColumnSpec() {
        return targetColumnSpec.get();
    }

    public String getTargetColumnKey() {
        return targetColumnKey.get();
    }

    public String getSourceColumnSpec() {
        return sourceColumnSpec.get();
    }

    public String getColumnTransRule() {
        return columnTransRule.get();
    }
//
    @Override
    public String toString() {
        return "targetSchema = " + getTargetSchema() + ", targetTableName = " + getTargetTableName() + ", targetColumnName = " + getTargetColumnName() + ", targetColumnSpec = " + getTargetColumnSpec() + ", targetColumnKey = " + getTargetColumnKey() + ", sourceColumnSpec = " + getTargetColumnSpec() + ", columnTransRule = " + getColumnTransRule() + '}';
    }

}
