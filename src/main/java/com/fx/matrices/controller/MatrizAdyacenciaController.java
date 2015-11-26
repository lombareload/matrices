package com.fx.matrices.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrizAdyacenciaController {

    private static final int RADIO = 20;
    List<Character> characters = new ArrayList<>();
    Map<String, TextField> texts = new HashMap<>();
    private CharacterIterator iterator = new StringCharacterIterator("ABCDEFGHIJKLMNOPQRST");
    @FXML
    private GridPane adyacencia;
    @FXML
    private Button botonGrafo;
    @FXML
    private Pane panelGrafo;
    @FXML
    private GridPane incidencia;

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

        List<Node> paths = new ArrayList<>();
        List<Node> pathNames = new ArrayList<>();

        int id = 0;
        for (Map.Entry<String, TextField> stringTextFieldEntry : texts.entrySet()) {
            String key = stringTextFieldEntry.getKey();
            TextField value = stringTextFieldEntry.getValue();
            int i = Integer.parseInt(value.getText());
            if (i > 0) {
                String[] split = key.split(",");
                int fromIndex = Integer.parseInt(split[0]);
                int toIndex = Integer.parseInt(split[1]);

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

                StackPane elipse = createElipse2(Integer.toString(id++));
                elipse.setLayoutX(midX - RADIO);
                elipse.setLayoutY(midY - RADIO);
                pathNames.add(elipse);

                QuadCurve quadCurve = new QuadCurve(fromX, fromY, midX, midY, toX, toY);
                quadCurve.setStrokeWidth(3);
                quadCurve.setStroke(Color.BLACK);
                paths.add(quadCurve);
            }
        }

        panelGrafo.getChildren().addAll(paths);
        panelGrafo.getChildren().addAll(pathNames);
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

    /*public void pintarIncidencia() {
        int id = 0;
        Map<String, Integer> relations = new HashMap<>();
        for (Map.Entry<String, TextField> stringTextFieldEntry : texts.entrySet()) {
            String key = stringTextFieldEntry.getKey();
            TextField value = stringTextFieldEntry.getValue();
            int i = Integer.parseInt(value.getText());
            if (i > 0) {
                String[] split = key.split(",");
                int fromIndex = Integer.parseInt(split[0]);
                int toIndex = Integer.parseInt(split[1]);

                Node fromNode = elipses.get(fromIndex);
                double fromX = fromNode.getLayoutX() + RADIO;//2;
                double fromY = fromNode.getLayoutY() + RADIO;//2;
//                MoveTo moveToFrom = new MoveTo(fromX, fromY);

                Node toNode = elipses.get(toIndex);
                double toX = toNode.getLayoutX() + RADIO;//2;
                double toY = toNode.getLayoutY() + RADIO;//2;

//                double dx = fromX - toX;
//                double dy = fromY - toY;

                double midX = (fromX + toX)/2;
                double midY = (fromY + toY)/2;

                StackPane elipse = createElipse2(Character.toString(numbers.current()));
                elipse.setLayoutX(midX - RADIO);
                elipse.setLayoutY(midY - RADIO);
                pathNames.add(elipse);

                QuadCurve quadCurve = new QuadCurve(fromX, fromY, midX, midY, toX, toY);
                quadCurve.setStrokeWidth(3);
                quadCurve.setStroke(Color.BLACK);
                paths.add(quadCurve);
                numbers.next();
//                QuadCurveTo path = paintLine(moveToFrom, toX, toY);
//                paths.add(path);
            }
        }
    }*/





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

    public static StackPane createElipse2(String value) {
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

    private static Path paintLine(MoveTo moveTo, double x, double y){
        Path path = new Path();
        path.getElements().add(moveTo);
        path.getElements().add(new LineTo(x, y));
        path.setStrokeWidth(3);
        return path;
    }
}
