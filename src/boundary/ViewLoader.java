package boundary;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.reflect.Method;

public class ViewLoader {
    private static ControllerGraficoFactory controllerGraficoFactory = new ControllerGraficoFactory();


    public static void caricaView(String percorsoFXML, Stage stage) {
        try {
            // carico la nuova view
            FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(percorsoFXML));

            // ottengo il controller grafico associato alla view dalla factory
            Object controller = controllerGraficoFactory.getController(percorsoFXML);
            loader.setController(controller);
  
            Parent root = loader.load();

            // tramite reflection chiamo il metodo "setPrimaryStage" (se esiste nel controller)
            if (controller != null) {
                try {
                    // ottengo il metodo
                    Method setPrimaryStageMethod = controller.getClass().getMethod("setPrimaryStage", Stage.class);
                    
                    // passo lo stage al metodo
                    setPrimaryStageMethod.invoke(controller, stage);
                } catch (NoSuchMethodException e) {

                    System.out.println("Il controller non ha il metodo setPrimaryStage: " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            

            // imposto la nuova scena nello stage
            stage.setScene(new Scene(root));
              
            stage.setTitle(controllerGraficoFactory.getTitolo(percorsoFXML));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}