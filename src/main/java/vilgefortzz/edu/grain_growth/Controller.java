package vilgefortzz.edu.grain_growth;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import vilgefortzz.edu.grain_growth.growth.Growth;
import vilgefortzz.edu.grain_growth.growth.SimpleGrainGrowth;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;
import vilgefortzz.edu.grain_growth.neighbourhood.VonNeumann;
import vilgefortzz.edu.grain_growth.nucleating.Nucleating;
import vilgefortzz.edu.grain_growth.nucleating.RandomNucleating;
import vilgefortzz.edu.grain_growth.grid.ColorGenerator;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class Controller implements Initializable{

    /**
     * Paths
     */
    private final String MICROSTRUCTURES_IMAGES_PATH = "file:microstructures/images/";

    /**
     * Graphics properties
     */
    private GraphicsContext graphicsContext;
    private final int DELAY = 60;

    private double width;
    private double height;

    private double cellSize;

    /**
     * Canvas
     */
    @FXML private Canvas canvas;

    /**
     * Growing step controller - iteration
     */
    private final StepController stepController = new StepController();

    /**
     * Solver
     */
    private Solver solver = new Solver();

    /**
     * Grid
     */
    @FXML private TextField columnsText;
    @FXML private TextField rowsText;
    @FXML private Button generateGridButton;

    /**
     * Algorithm
     */
    @FXML private ComboBox<Growth> algorithmComboBox;

    /**
     * Neighbourhood
     */
    @FXML private ComboBox<Neighbourhood> neighbourhoodComboBox;

    /**
     * Nucleating
     */
    @FXML private ComboBox<Nucleating> nucleatingComboBox;
    @FXML private TextField numberOfGrainsText;
    @FXML private Button nucleatingButton;

    /**
     * Algorithm
     */
    @FXML private Button startButton;
    @FXML private Button stopButton;

    /**
     * Export/Import
     */
    @FXML private Button importFromFileButton;
    @FXML private Button exportToFileButton;
    @FXML private Button importFromPngButton;
    @FXML private Button exportToPngButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        graphicsContext = canvas.getGraphicsContext2D();

        width = canvas.getWidth();
        height = canvas.getHeight();

        initializeOptions();
    }

    private void initializeOptions() {

        algorithmComboBox.getItems().add(
                new SimpleGrainGrowth()
        );

        neighbourhoodComboBox.getItems().add(
                new VonNeumann()
        );

        nucleatingComboBox.getItems().add(
                new RandomNucleating()
        );

        algorithmComboBox.getSelectionModel().selectFirst();
        neighbourhoodComboBox.getSelectionModel().selectFirst();
        nucleatingComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void generateGrid() throws Exception {

        Grid grid = createGrid();
        initializeSolver(grid);

        draw(solver.getGrid());

        nucleatingButton.setDisable(false);

        importFromFileButton.setDisable(false);
        importFromPngButton.setDisable(false);

        exportToFileButton.setDisable(false);
        exportToPngButton.setDisable(false);
    }

    private void initializeSolver(Grid grid) throws Exception {

        setOptionsToSolver(grid);
        solver.initialize();

        stepController.initialize();
        stepController.setSolver(solver);
        stepController.setOnSucceeded(
                event -> nextStep((Grid) event.getSource().getValue())
        );
        stepController.setPeriod(Duration.millis(DELAY));
    }

    private Grid createGrid() {

        int columns = Integer.parseInt(columnsText.getText());
        int rows = Integer.parseInt(rowsText.getText());

        Grid grid = new Grid(columns, rows);

        int gridWidth = grid.getWidth();
        int gridHeight = grid.getHeight();

        double cellWidth = width / gridWidth;
        double cellHeight = height / gridHeight;

        if (cellWidth < cellHeight) {
            cellSize = cellWidth;
        } else {
            cellSize = cellHeight;
        }

        return grid;
    }

    @FXML
    public void start() throws Exception{

        if (stepController.getState() != Worker.State.READY) {
            stepController.restart();
        } else {
            stepController.start();
        }

        startButton.setDisable(true);
        stopButton.setDisable(false);
        nucleatingButton.setDisable(true);
    }

    @FXML
    private void stop() {

        stepController.cancel();

        startButton.setDisable(false);
        stopButton.setDisable(true);
        generateGridButton.setDisable(false);
    }

    @FXML
    public void nucleating() throws Exception {

        Nucleating nucleating = nucleatingComboBox.getSelectionModel().getSelectedItem();
        int n = Integer.parseInt(numberOfGrainsText.getText());

        solver.setNucleating(nucleating);
        solver.nucleating(n);

        draw(solver.getGrid());

        startButton.setDisable(false);
    }

    @FXML
    public void importFromFile() throws Exception {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import microstructure from file");
        File file = chooser.showOpenDialog(new Stage());
    }

    @FXML
    public void exportToFile() throws Exception {
        throw new NotImplementedException();
    }

    @FXML
    public void importFromPng() throws Exception {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import microstructure from png");
        File file = chooser.showOpenDialog(new Stage());

        if (file != null) {
            Image image = new Image(MICROSTRUCTURES_IMAGES_PATH + file.getName());
            graphicsContext.drawImage(image, 0, 0, width, height);
        }
    }

    @FXML
    public void exportToPng() throws Exception {

        WritableImage exportedImage = new WritableImage((int) width, (int) height);
        File file = new File("microstructures/images/microstructure.png");

        canvas.snapshot(null, exportedImage);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(exportedImage, null), "png", file);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void setOptionsToSolver(Grid grid) {

        Growth growth = algorithmComboBox.getSelectionModel().getSelectedItem();
        Neighbourhood neighbourhood = neighbourhoodComboBox.getSelectionModel().getSelectedItem();

        solver.setGrid(grid);
        solver.setGrowth(growth);
        solver.setNeighbourhood(neighbourhood);
    }

    private void nextStep(Grid grid) {

        Platform.runLater(() -> {
            if (stepController.isFinished()) {
                startButton.setDisable(true);
                stopButton.setDisable(true);
                generateGridButton.setDisable(false);
            }

            draw(grid);
        });
    }

    private void draw(Grid grid) {

        graphicsContext.clearRect(0, 0, width, height);
        drawCells(grid);
    }

    private void drawCells(Grid grid){
        grid.forEach(c -> {
            graphicsContext.setFill(ColorGenerator.getColor(c.getState()));
            graphicsContext.fillRect(
                    c.getX() * cellSize,
                    c.getY() * cellSize,
                    cellSize,
                    cellSize
            );
        });
    }
}
