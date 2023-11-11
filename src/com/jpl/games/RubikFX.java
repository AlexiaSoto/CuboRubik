package com.jpl.games;

import com.jpl.games.model.Move;
import com.jpl.games.model.Moves;
import com.jpl.games.model.ParticipantModel;
import com.jpl.games.model.Rubik;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.function.Predicate;
public class RubikFX extends Application {
    private final BorderPane pane=new BorderPane();
    private Rubik rubik;
    private LocalTime time=LocalTime.now();
    private Timeline timer;
    private final StringProperty clock = new SimpleStringProperty("00:00:00");
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());
    private Button btnHover;
    private Moves moves=new Moves();
    private ParticipantModel participantModel;
    private Label nameLabel;
    public RubikFX(ParticipantModel participantModel) {
        this.participantModel = participantModel;
    }
    @Override
    public void start(Stage stage) {

        rubik=new Rubik();
        nameLabel = new Label( participantModel.getName());
        pane.setTop(nameLabel);
        ToolBar tbTop=new ToolBar(new Button("U"),new Button("Ui"),new Button("F"),
                new Button("Fi"),new Separator(),new Button("Y"),
                new Button("Yi"),new Button("Z"),new Button("Zi"),new Button("B"),new Button("Bi"),new Button("D"),
                new Button("Di"),new Button("E"),new Button("Ei"),new Button("R"),new Button("Ri"),new Separator(),
                new Button("X"),new Button("Xi"),new Button("L"),new Button("Li"),new Button("M"),
                new Button("Mi"),new Button("S"),new Button("Si"));

        Button bSc=new Button("Revolver Cubo");
        bSc.setOnAction(e->{
            if(moves.getNumMoves()>0){
                Action response = Dialogs.create()
                        .owner(stage)
                        .showConfirm();
                if(response==Dialog.Actions.YES){
                    rubik.doReset();
                    doScramble();
                }
            } else {
                doScramble();
            }
        });
        ChangeListener<Number> clockLis=(ov,l,l1)->clock.set(LocalTime.ofNanoOfDay(l1.longValue()).format(fmt));
        Label lMov=new Label();
        rubik.getCount().addListener((ov,v,v1)->{

            lMov.setText("Movimientos Realizados: "+(v1.intValue()+1));
        });
        rubik.getLastRotation().addListener((ov,v,v1)->{
            if(!rubik.isOnReplaying().get() && !v1.isEmpty()){
                moves.addMove(new Move(v1, LocalTime.now().minusNanos(time.toNanoOfDay()).toNanoOfDay()));
            }
        });
        Label lSolved=new Label("Solved");
        lSolved.setVisible(false);
        Label lSimulated=new Label();
        Label lTime=new Label();
        lTime.textProperty().bind(clock);
        lSimulated.textProperty().bind(rubik.getPreviewFace());
        tbTop.getItems().addAll(new Separator(),bSc,lMov,new Separator(),lTime,
                new Separator(),lSolved,new Separator(),lSimulated);
        pane.setTop(tbTop);

        ToolBar tbRight=new ToolBar();
        tbRight.setOrientation(Orientation.VERTICAL);
        pane.setRight(tbRight);
        ToolBar tbLeft=new ToolBar();
        tbLeft.setOrientation(Orientation.VERTICAL);
        pane.setLeft(tbLeft);

        pane.setCenter(rubik.getSubScene());
        pane.getChildren().stream()
                .filter(withToolbars())
                .forEach(tb->{
                    ((ToolBar)tb).getItems().stream()
                            .filter(withMoveButtons())
                            .forEach(n->{
                                Button b=(Button)n;
                                b.setOnAction(e->rotateFace(b.getText()));
                                b.hoverProperty().addListener((ov,b0,b1)->updateArrow(b.getText(),b1));
                            });
                });

        rubik.isOnRotation().addListener((b0,b1,b2)->{
            if(b2){
                pane.getChildren().stream().filter(withToolbars())
                        .forEach(tb->{
                            ((ToolBar)tb).getItems().stream().filter(withMoveButtons().and(isButtonHovered()))
                                    .findFirst().ifPresent(n->btnHover=(Button)n);
                        });
            } else {
                if(rubik.getPreviewFace().get().isEmpty()){
                    btnHover=null;
                } else {
                    if(btnHover!=null && !btnHover.isHover()){
                        updateArrow(btnHover.getText(), false);
                    }
                }
            }
        });

        rubik.isOnPreview().addListener((b0, b1, b2) -> {
            final String face=rubik.getPreviewFace().get();
            pane.getChildren().stream().filter(withToolbars())
                    .forEach(tb->{
                        ((ToolBar)tb).getItems().stream().filter(withMoveButtons())
                                .forEach((b)->{
                                    b.setDisable(!b2 || face.isEmpty() || face.equals("V")?false:
                                            !face.equals(((Button)b).getText()));
                                });
                    });
        });


        timer=new Timeline(new KeyFrame(Duration.ZERO, e->{
            clock.set(LocalTime.now().minusNanos(time.toNanoOfDay()).format(fmt));
        }),new KeyFrame(Duration.seconds(1)));
        timer.setCycleCount(Animation.INDEFINITE);
        rubik.isSolved().addListener((ov,b,b1)->{
            if(b1){
                lSolved.setVisible(true);
                timer.stop();
                moves.setTimePlay(LocalTime.now().minusNanos(time.toNanoOfDay()).toNanoOfDay());
                System.out.println(moves);
            } else {
                lSolved.setVisible(false);
            }
        });

        final Scene scene = new Scene(pane, 1180, 580, true);
        scene.cursorProperty().bind(rubik.getCursor());
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.show();
    }
    private void rotateFace(final String btRot){
        pane.getChildren().stream()
                .filter(withToolbars())
                .forEach(tb->{
                    ((ToolBar)tb).getItems().stream()
                            .filter(withMoveButtons().and(withButtonTextName(btRot)))
                            .findFirst().ifPresent(n->rubik.isHoveredOnClick().set(((Button)n).isHover()));
                });
        rubik.rotateFace(btRot);
    }
    private void updateArrow(String face, boolean hover){
        rubik.updateArrow(face,hover);
    }
    private void doScramble(){
        pane.getChildren().stream().filter(withToolbars()).forEach(setDisable(true));
        rubik.doScramble();
        rubik.isOnScrambling().addListener((ov,v,v1)->{
            if(v && !v1){
                System.out.println("scrambled!");
                pane.getChildren().stream().filter(withToolbars()).forEach(setDisable(false));
                moves=new Moves();
                time=LocalTime.now();
                timer.playFromStart();
            }
        });
    }

    private static Predicate<Node> withToolbars(){
        return n -> (n instanceof ToolBar);
    }
    private static Predicate<Node> withMoveButtons(){
        return n -> (n instanceof Button) && ((Button)n).getText().length()<=2;
    }
    private static Predicate<Node> withButtonTextName(String text){
        return n -> ((Button)n).getText().equals(text);
    }
    private static Predicate<Node> isButtonHovered(){
        return n -> ((Button)n).isHover();
    }
    private static Consumer<Node> setDisable(boolean disable){
        return n -> n.setDisable(disable);
    }

    public static void main(String[] args) {
        launch(args);
    }
}