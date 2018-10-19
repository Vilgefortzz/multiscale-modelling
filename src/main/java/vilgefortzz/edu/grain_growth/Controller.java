package vilgefortzz.edu.grain_growth;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;
import vilgefortzz.edu.grain_growth.growth.SimpleGrainGrowth;
import vilgefortzz.edu.grain_growth.image.ColorGenerator;
import vilgefortzz.edu.grain_growth.image.ImageModifier;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;
import vilgefortzz.edu.grain_growth.neighbourhood.VonNeumann;
import vilgefortzz.edu.grain_growth.nucleating.Nucleating;
import vilgefortzz.edu.grain_growth.nucleating.RandomNucleating;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class Controller implements Initializable {

    /**
     * Paths
     */
    private final String MICROSTRUCTURES_FILES_DIR_PATH = "microstructures/files/";
    private final String MICROSTRUCTURES_IMAGES_DIR_PATH = "microstructures/images/";
    private final String MICROSTRUCTURES_IMAGES_PATH = "file:microstructures/images/";

    /**
     * Graphics properties
     */
    private GraphicsContext graphicsContext;
    private final int DELAY = 60;

    private int width;
    private int height;

    private double cellSize;

    /**
     * Canvas
     */
    @FXML
    private Canvas canvas;

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
    @FXML
    private TextField columnsText;
    @FXML
    private TextField rowsText;
    @FXML
    private Button generateGridButton;

    /**
     * Algorithm
     */
    @FXML
    private ComboBox<Growth> algorithmComboBox;

    /**
     * Neighbourhood
     */
    @FXML
    private ComboBox<Neighbourhood> neighbourhoodComboBox;

    /**
     * Nucleating
     */
    @FXML
    private ComboBox<Nucleating> nucleatingComboBox;
    @FXML
    private TextField numberOfGrainsText;
    @FXML
    private Button nucleatingButton;

    /**
     * Inclusions
     */
    @FXML
    private TextField amountOfInclusionsText;
    @FXML
    public ComboBox<String> typeOfInclusionComboBox;
    @FXML
    private Button addInclusionsButton;

    /**
     * Algorithm
     */
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;

    /**
     * Export/Import
     */
    @FXML
    private Button importFromFileButton;
    @FXML
    private Button exportToFileButton;
    @FXML
    private Button importFromBitmapButton;
    @FXML
    private Button exportToBitmapButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        graphicsContext = canvas.getGraphicsContext2D();

        width = (int)canvas.getWidth();
        height = (int)canvas.getHeight();

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

        typeOfInclusionComboBox.getItems().addAll(
                "square",
                "circular"
        );

        algorithmComboBox.getSelectionModel().selectFirst();
        neighbourhoodComboBox.getSelectionModel().selectFirst();
        nucleatingComboBox.getSelectionModel().selectFirst();
        typeOfInclusionComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void generateGrid() throws Exception {

        int columns = Integer.parseInt(columnsText.getText());
        int rows = Integer.parseInt(rowsText.getText());

        generateGrid(columns, rows, null);
    }

    private void generateGrid(int columns, int rows, List<Cell> cells) throws Exception {

        Grid grid = createGrid(columns, rows, cells);
        initializeSolver(grid);

        draw(solver.getGrid());

        nucleatingButton.setDisable(false);
        addInclusionsButton.setDisable(false);

        importFromFileButton.setDisable(false);
        importFromBitmapButton.setDisable(false);

        exportToFileButton.setDisable(false);
        exportToBitmapButton.setDisable(false);
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

    private Grid createGrid(int columns, int rows, List<Cell> cells) {

        Grid grid;

        if (cells != null) {
            grid = new Grid(columns, rows, cells);
        } else {
            grid = new Grid(columns, rows);
        }

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
    public void start() throws Exception {

        if (stepController.getState() != Worker.State.READY) {
            stepController.restart();
        } else {
            stepController.start();
        }

        startButton.setDisable(true);
        stopButton.setDisable(false);
        nucleatingButton.setDisable(true);
        addInclusionsButton.setDisable(true);
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
        chooser.setInitialDirectory(new File(MICROSTRUCTURES_FILES_DIR_PATH));
        File file = chooser.showOpenDialog(new Stage());

        String cellLine, gridDimensions, simulation;
        int gridWidth = 0, gridHeight = 0, type = 0;
        boolean isFinished = false;
        List<Cell> cells = new ArrayList<>();

        if (file != null) {

            try (BufferedReader buffer = new BufferedReader(new FileReader(file.getPath()))) {

                simulation = buffer.readLine();
                isFinished = Boolean.parseBoolean(simulation.split(" ")[0]);
                type = Integer.parseInt(simulation.split(" ")[1]);

                gridDimensions = buffer.readLine();
                gridWidth = Integer.parseInt(gridDimensions.split(" ")[0]);
                gridHeight = Integer.parseInt(gridDimensions.split(" ")[1]);

                while ((cellLine = buffer.readLine()) != null) {

                    String[] cell = cellLine.split(" ");
                    cells.add(new Cell(
                            Integer.parseInt(cell[0]),
                            Integer.parseInt(cell[1]),
                            Integer.parseInt(cell[2]),
                            Integer.parseInt(cell[3])
                    ));
                }
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }

            generateGrid(gridWidth, gridHeight, cells);
            solver.getGrowth().setType(type);
            solver.getGrowth().setFinished(isFinished);
            if (!isFinished) {
                startButton.setDisable(false);
            }
        }
    }

    @FXML
    public void exportToFile() throws Exception {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export microstructure to file");
        chooser.setInitialDirectory(new File(MICROSTRUCTURES_FILES_DIR_PATH));
        chooser.setInitialFileName("microstructure.txt");

        File file = chooser.showSaveDialog(new Stage());

        if (file != null) {

            if (file.createNewFile()) {
                System.out.println("File is created");
            } else {
                System.out.println("File already exists");
            }

            Grid grid = solver.getGrid();

            String simulationData = stepController.prepareData();
            List<String> gridData = grid.prepareData();

            FileWriter writer = new FileWriter(file);

            writer.write(simulationData);
            writer.write(System.getProperty("line.separator"));

            for (String gridDataItem : gridData) {
                writer.write(gridDataItem);
                writer.write(System.getProperty("line.separator"));
            }

            writer.close();
        }
    }

    @FXML
    public void importFromBitmap() throws Exception {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import microstructure from bitmap");
        chooser.setInitialDirectory(new File(MICROSTRUCTURES_IMAGES_DIR_PATH));
        File file = chooser.showOpenDialog(new Stage());

        List<Cell> cells = new ArrayList<>();

        if (file != null) {

            BufferedImage image = ImageIO.read(file);
            ColorGenerator.setState(Color.WHITE, 0);

            for (int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
                for (int xPixel = 0; xPixel < image.getWidth(); xPixel++) {

                    int color = image.getRGB(xPixel, yPixel);
                    cells.add(new Cell(
                            xPixel,
                            yPixel,
                            0,
                            ColorGenerator.getState(new Color(color))
                    ));
                }
            }

            generateGrid(image.getWidth(), image.getHeight(), cells);
            solver.getGrowth().setType(ColorGenerator.type);
            if (cells.stream().noneMatch(cell -> cell.getState() == 0)) {
                solver.getGrowth().setFinished(true);
            } else {
                startButton.setDisable(false);
            }
            exportToBitmapButton.setDisable(false);
        }
    }

    @FXML
    public void exportToBitmap() throws Exception {

        WritableImage exportedImage = new WritableImage(width, height);
        canvas.snapshot(null, exportedImage);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export microstructure to bitmap");
        fileChooser.setInitialDirectory(new File(MICROSTRUCTURES_IMAGES_DIR_PATH));
        fileChooser.setInitialFileName("microstructure.bmp");

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {

            BufferedImage image = SwingFXUtils.fromFXImage(exportedImage, null);

            if (file.createNewFile()) {
                System.out.println("File is created");
            } else {
                System.out.println("File already exists");
            }

            // Export to bitmap
            BufferedImage bitmapImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bitmapImage.getGraphics().drawImage(image, 0, 0, null);

            // Resize bitmap
            BufferedImage resized = ImageModifier.resize(bitmapImage, solver.getGrid().getWidth(), solver.getGrid().getHeight());

            ImageIO.write(resized, "bmp", file);

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

    private void drawCells(Grid grid) {
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
