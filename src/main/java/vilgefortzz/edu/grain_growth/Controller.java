package vilgefortzz.edu.grain_growth;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.util.Duration;
import vilgefortzz.edu.grain_growth.algorithm.Algorithm;
import vilgefortzz.edu.grain_growth.algorithm.SimpleGrainGrowth;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;
import vilgefortzz.edu.grain_growth.neighbourhood.VonNeumann;
import vilgefortzz.edu.grain_growth.nucleating.Nucleating;
import vilgefortzz.edu.grain_growth.nucleating.RandomNucleating;
import vilgefortzz.edu.grain_growth.grid.ColorGenerator;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class Controller implements Initializable{

    /**
     * Graphics properties
     */
    private GraphicsContext graphicsContext;

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
     * Growth
     */
    @FXML private ComboBox<Algorithm> algorithmComboBox;

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
     * Growth
     */
    @FXML private Button startButton;
    @FXML private Button stopButton;

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
    }

    private void initializeSolver(Grid grid) throws Exception {

        setOptionsToSolver(grid);
        solver.initialize();

        stepController.initialize();
        stepController.setSolver(solver);
        stepController.setOnSucceeded(
                event -> nextStep( (Grid) event.getSource().getValue() )
        );
        stepController.setPeriod(Duration.millis(200));
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

    private void setOptionsToSolver(Grid grid) {

        Algorithm algorithm = algorithmComboBox.getSelectionModel().getSelectedItem();
        Neighbourhood neighbourhood = neighbourhoodComboBox.getSelectionModel().getSelectedItem();

        solver.setGrid(grid);
        solver.setAlgorithm(algorithm);
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
