package com.fx.matrices.controller;

import com.fx.matrices.model.Arista;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;

public class MatrizAdyacenciaController {

    private static final int RADIO = 20;
    List<Character> characters = new ArrayList<>();
    Map<String, TextField> texts = new HashMap<>();
    private CharacterIterator iterator = new StringCharacterIterator("ABCDEFGHIJKLMNOPQRST");
    List<Arista> aristas = Collections.emptyList();

    @FXML
    private GridPane adyacencia;
    @FXML
    private Button botonGrafo;
    @FXML
    private Pane panelGrafo;
    @FXML
    private TableView incidencia;

    public void agregarNodo() {
        characters.add(iterator.current());
        pintarMatriz();
        iterator.next();
        botonGrafo.setDisable(false);
    }

    private void pintarMatriz() {
        final int i = characters.size() - 1;
        for(int j = 0; j <= i; j++) {

            TextField text = new TextField("0");
            String id = i+","+j;
            texts.put(id, text);
            adyacencia.add(text, i+1, j+1);
        }
        for(int j = 0; j < i; j++) {

            TextField text = new TextField("0");
            String id = j + "," + i;
            texts.put(id, text);
            adyacencia.add(text, j+1, i+1);
        }

        Character character = characters.get(i);
        adyacencia.add(new Label(character.toString()), 0, i+1);
        adyacencia.add(new Label(character.toString()), i+1, 0);
    }

    public void pintarGrafo() {
        panelGrafo.getChildren().clear();
        List<Node> elipses = crearElipses();

        pintarAristas(elipses);

        pintarIncidencia();

        panelGrafo.getChildren().addAll(elipses);
    }

    private List<Node> crearElipses() {
        final double delta = 360 / characters.size();
        double angle = 0;
        ArrayList<Node> elipses = new ArrayList<Node>(characters.size());
        for(int i = 0; i < characters.size(); i++) {
            final double sin = Math.sin(Math.toRadians(angle));
            final double cos = Math.cos(Math.toRadians(angle));
            final double dy = 200 * sin;
            final double dx = 200 * cos;
            StackPane elipse = createElipse(characters.get(i).toString());
            elipse.setLayoutX(250 + dx);
            elipse.setLayoutY(250 + dy);
            elipses.add(elipse);
            angle += delta;
            angle = angle % 360;
        }
        return elipses;
    }

    private void pintarIncidencia() {
        incidencia.getColumns().clear();

        // create rows
        characters.stream().<Integer>reduce(0, (x, c) -> {
            TableColumn column = new TableColumn<String[], String>(Character.toString(c));
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                @Override
                public ObservableValue call(TableColumn.CellDataFeatures param) {
                    return new ReadOnlyStringWrapper(((String[]) param.getValue())[x]);
                }
            });
            incidencia.getColumns().add(column);
            return x + 1;
        }, (a, b) -> a);

        TableColumn column = new TableColumn<String[], String>("Arista");
        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String[] strings = (String[]) param.getValue();
                return new ReadOnlyStringWrapper(strings[strings.length-1]);
            }
        });
        incidencia.getColumns().add(column);

        // insert into rows
        ObservableList items = incidencia.getItems();
        items.clear();
        for (Arista arista : aristas) {
            String[] values = new String[characters.size()+1];
            int j = 0;
            for (char c: characters) {
                values[j++] = arista.hasConnectionWith(c) ? "1" : "0";
            }
            values[j] = String.valueOf(arista.getId());
            items.add(values);
        }
    }

    private void pintarAristas(List<Node> elipses) {
        List<Node> paths = new ArrayList<>();
        List<Node> pathNames = new ArrayList<>();
        aristas = new ArrayList<>();

        int id = 0;
        for (Map.Entry<String, TextField> stringTextFieldEntry : texts.entrySet()) {
            String key = stringTextFieldEntry.getKey();
            TextField value = stringTextFieldEntry.getValue();
            int i = Integer.parseInt(value.getText());
            if (i > 0) {
                String[] split = key.split(",");
                int fromIndex = Integer.parseInt(split[0]);
                int toIndex = Integer.parseInt(split[1]);

                aristas.add(new Arista(id, characters.get(fromIndex), characters.get(toIndex)));
                Node fromNode = elipses.get(fromIndex);
                double fromX = fromNode.getLayoutX() + RADIO;
                double fromY = fromNode.getLayoutY() + RADIO;

                Node toNode = elipses.get(toIndex);
                double toX = toNode.getLayoutX() + RADIO;
                double toY = toNode.getLayoutY() + RADIO;

//                double dx = fromX - toX;
//                double dy = fromY - toY;

                double midX = (fromX + toX)/2;
                double midY = (fromY + toY)/2;

                StackPane aristaNodo = createElipseArista(Integer.toString(id++));
                aristaNodo.setLayoutX(midX - RADIO);
                aristaNodo.setLayoutY(midY - RADIO);
                pathNames.add(aristaNodo);

                QuadCurve quadCurve = new QuadCurve(fromX, fromY, midX, midY, toX, toY);
                quadCurve.setStrokeWidth(3);
                quadCurve.setStroke(Color.BLACK);
                paths.add(quadCurve);
            }
        }

        panelGrafo.getChildren().addAll(paths);
        panelGrafo.getChildren().addAll(pathNames);
    }

    public static StackPane createElipse(String value) {
        Ellipse e = EllipseBuilder
                .create()
                .fill(Color.WHITESMOKE)
                .strokeWidth(3)
                .stroke(Color.BLACK)
                .radiusX(RADIO)
                .radiusY(RADIO)
                .build();

        Text text = new Text(value);
        StackPane stack = new StackPane();
        stack.getChildren().add(e);
        stack.getChildren().add(text);
        return stack;
    }

    public static StackPane createElipseArista(String value) {
        Ellipse e = EllipseBuilder
                .create()
                .fill(Color.AQUAMARINE)
                .strokeWidth(3)
                .stroke(Color.BLACK)
                .radiusX(RADIO)
                .radiusY(RADIO)
                .build();

        Text text = new Text(value);
        StackPane stack = new StackPane();
        stack.getChildren().add(e);
        stack.getChildren().add(text);
        return stack;
    }
}
