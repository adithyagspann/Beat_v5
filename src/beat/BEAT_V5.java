/** Copyright © 2017-2020, GSPANN Technologies and/or its affiliates. All rights reserved.* */
package beat;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ravindra
 */
public class BEAT_V5 extends Application {

    private static final Logger logger = LoggerFactory.getLogger(BEAT_V5.class);
    Stage mainStage;
    Parent root;
    MainStageController controller;

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Preparing to Load UI");
        this.mainStage = stage;
        mainStage.setTitle("BEAT - BIG DATA & ETL Analytics Tester");
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/cont.png")));
        mainStage.resizableProperty().setValue(Boolean.TRUE);
        mainStage.setMaximized(true);

        showMainStage();
    }

    private void showMainStage() throws IOException {
        logger.info("UI loading");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/MainStage.fxml"));
        root = fxmlLoader.load();
        System.out.println("Root :" + root);
        controller = fxmlLoader.<MainStageController>getController();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger.info("Beat Application Starting");
        launch(args);
    }

}
