package com.jpl.games.model;


import com.jpl.games.math.Rotations;
import com.jpl.games.model3d.Model3D;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class Rubik {

    private final Group cube=new Group();
    private Map<String,MeshView> mapMeshes=new HashMap<>();
    private final double dimCube;
    private final MeshView faceArrow;
    private final MeshView axisArrow;
    private final ContentModel content;
    private final Rotations rot;
    private final Map<String,Transform> mapTransformsScramble=new HashMap<>();
    private final Map<String,Transform> mapTransformsOriginal=new HashMap<>();
    private final List<Integer> orderOriginal;
    private List<Integer> order;
    private List<Integer> reorder, layer, orderScramble;
    private List<String> sequence=new ArrayList<>();
    private boolean secondRotation=false;
    private final DoubleProperty rotation=new SimpleDoubleProperty(0d);
    private final BooleanProperty onRotation=new SimpleBooleanProperty(false);
    private final BooleanProperty onPreview=new SimpleBooleanProperty(false);
    private final BooleanProperty onScrambling=new SimpleBooleanProperty(false);
    private final BooleanProperty onReplaying=new SimpleBooleanProperty(false);
    private final BooleanProperty hoveredOnClick=new SimpleBooleanProperty(false);
    private final BooleanProperty solved=new SimpleBooleanProperty(false);
    private final ObjectProperty<Cursor> cursor = new SimpleObjectProperty<>(Cursor.DEFAULT);
    private Point3D axis=new Point3D(0,0,0);
    private final StringProperty previewFace=new SimpleStringProperty("");
    private final StringProperty lastRotation=new SimpleStringProperty("");
    private final ChangeListener<Number> rotMap;
    private final IntegerProperty count = new SimpleIntegerProperty(-1);
    private final LongProperty timestamp = new SimpleLongProperty(0l);
    public SubScene getSubScene(){ return content.getSubScene(); }
    public BooleanProperty isSolved() { return solved; }
    public BooleanProperty isOnRotation() { return onRotation; }
    public BooleanProperty isOnPreview() { return onPreview; }
    public BooleanProperty isOnScrambling() { return onScrambling; }
    public BooleanProperty isOnReplaying() { return onReplaying; }
    public BooleanProperty isHoveredOnClick() { return hoveredOnClick; }
    public IntegerProperty getCount() { return count; }
    public LongProperty getTimestamp() { return timestamp; }
    public StringProperty getPreviewFace() { return previewFace; }
    public StringProperty getLastRotation() { return lastRotation; }
    public ObjectProperty<Cursor> getCursor() { return cursor; }
    private String last="V", get="V";
    private static final int MOUSE_OUT=0;
    private static final int MOUSE_RELEASED=3;
    private final IntegerProperty mouse=new SimpleIntegerProperty(MOUSE_OUT);

    public Rubik(){

        Model3D model=new Model3D();
        model.importObj();
        mapMeshes=model.getMapMeshes();
        faceArrow=model.getFaceArrow();
        axisArrow=model.getAxisArrow();
        cube.getChildren().setAll(mapMeshes.values());
        cube.getChildren().addAll(faceArrow);
        cube.getChildren().addAll(axisArrow);
        dimCube=cube.getBoundsInParent().getWidth();
        content = new ContentModel(800,600,dimCube);
        content.setContent(cube);
        rot=new Rotations();
        order=rot.getCube();
        mapMeshes.forEach((k,v)->mapTransformsOriginal.put(k, v.getTransforms().get(0)));
        orderOriginal=order.stream().collect(Collectors.toList());

        rotMap=(ov,angOld,angNew)->{
            mapMeshes.forEach((k,v)->{
                layer.stream().filter(l->k.contains(l.toString()))
                        .findFirst().ifPresent(l->{
                            Affine a=new Affine(v.getTransforms().get(0));
                            a.prepend(new Rotate(angNew.doubleValue()-angOld.doubleValue(),axis));
                            v.getTransforms().setAll(a);
                        });
            });
        };
    }
    public void rotateFace(final String btRot){
        lastRotation.set("");
        lastRotation.set(btRot);
        rotateFace(btRot,false,false);
    }
    private void rotateFace(final String btRot, boolean bPreview, boolean bCancel){
        if(onRotation.get()){
            return;
        }
        onRotation.set(true);
        System.out.println((bPreview?(bCancel?"Cancelling: ":"Simulating: "):"Rotating: ")+btRot);
        boolean bFaceArrow= !(btRot.startsWith("X")||btRot.startsWith("Y")||btRot.startsWith("Z"));

        if(bPreview || onScrambling.get() || onReplaying.get() || secondRotation){
            rot.turn(btRot);
            reorder=rot.getCube();
            if(!bFaceArrow){
                layer=reorder.stream().collect(Collectors.toList());
            }else {
                AtomicInteger index = new AtomicInteger();
                layer=order.stream()
                        .filter(o->!Objects.equals(o, reorder.get(index.getAndIncrement())))
                        .collect(Collectors.toList());
                layer.add(0,reorder.get(Utils.getCenter(btRot)));
            }
            axis=Utils.getAxis(btRot);
        }
        double angIni=(bPreview || onScrambling.get() || onReplaying.get() || secondRotation?0d:5d)*(btRot.endsWith("i")?1d:-1d);
        double angEnd=(bPreview?5d:90d)*(btRot.endsWith("i")?1d:-1d);

        rotation.set(angIni);
        rotation.addListener(rotMap);

        Timeline timeline=new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(onScrambling.get() || onReplaying.get()?200:(bPreview?100:600)), e->{
                    rotation.removeListener(rotMap);
                    secondRotation=false;
                    if(bPreview){
                        if(bCancel){
                            previewFace.set("");
                            onPreview.set(false);
                        } else if(mouse.get()==MOUSE_RELEASED){
                            mouse.set(MOUSE_OUT);
                            onRotation.set(false);
                            updateArrow(btRot,false);
                        } else {
                            previewFace.set(btRot);
                        }
                    } else if(!(onScrambling.get() || onReplaying.get())){
                        mouse.set(MOUSE_OUT);
                        previewFace.set("V");
                        if(!hoveredOnClick.get()){
                            updateArrow(btRot,false);
                        } else {
                            secondRotation=true;
                        }
                        hoveredOnClick.set(false);
                    }
                    onRotation.set(false);
                },  new KeyValue(rotation,angEnd)));
        timeline.playFromStart();

        if(bPreview || onScrambling.get() || onReplaying.get() || secondRotation){
            order=reorder.stream().collect(Collectors.toList());
        }if(!bPreview && !onScrambling.get() && bFaceArrow){
            count.set(count.get()+1);
            solved.set(Utils.checkSolution(order));
        }
    }
    public void updateArrow(String face, boolean hover){
        boolean bFaceArrow=!(face.startsWith("X")||face.startsWith("Y")||face.startsWith("Z"));
        MeshView arrow=bFaceArrow?faceArrow:axisArrow;

        if(hover && onRotation.get()){
            return;
        }
        arrow.getTransforms().clear();
        if(hover){
            double d0=arrow.getBoundsInParent().getHeight()/2d;
            Affine aff=Utils.getAffine(dimCube, d0, bFaceArrow, face);
            arrow.getTransforms().setAll(aff);
            arrow.setMaterial(Utils.getMaterial(face));
            if(previewFace.get().isEmpty()) {
                previewFace.set(face);
                onPreview.set(true);
                rotateFace(face,true,false);
            }
        } else if(previewFace.get().equals(face)){
            rotateFace(Utils.reverseRotation(face),true,true);
        } else if(previewFace.get().equals("V")){
            previewFace.set("");
            onPreview.set(false);
        }
    }
    public void doScramble(){
        StringBuilder sb=new StringBuilder();
        final List<String> movements = Utils.getMovements();
        IntStream.range(0, 25).boxed().forEach(i->{
            while(last.substring(0, 1).equals(get.substring(0, 1))){
                get=movements.get((int)(Math.floor(Math.random()*movements.size())));
            }
            last=get;
            if(get.contains("2")){
                get=get.substring(0,1);
                sb.append(get).append(" ");
            }
            sb.append(get).append(" ");
        });
        System.out.println("sb: "+sb.toString());
        doSequence(sb.toString().trim());
    }

    public void doSequence(String list){
        onScrambling.set(true);
        sequence=Utils.unifyNotation(list);
        IntegerProperty index=new SimpleIntegerProperty(1);
        ChangeListener<Boolean> lis=(ov,b,b1)->{
            if(!b1){
                if(index.get()<sequence.size()){
                    rotateFace(sequence.get(index.get()));
                } else {
                    mapMeshes.forEach((k,v)->mapTransformsScramble.put(k, v.getTransforms().get(0)));
                    orderScramble=reorder.stream().collect(Collectors.toList());
                }
                index.set(index.get()+1);
            }
        };
        index.addListener((ov,v,v1)->{
            if(v1.intValue()==sequence.size()+1){
                onScrambling.set(false);
                onRotation.removeListener(lis);
                count.set(-1);
            }
        });
        onRotation.addListener(lis);
        rotateFace(sequence.get(0));
    }
    public void doReset(){
        System.out.println("Reset!");
        content.resetCam();
        mapMeshes.forEach((k,v)->v.getTransforms().setAll(mapTransformsOriginal.get(k)));
        order=orderOriginal.stream().collect(Collectors.toList());
        rot.setCube(order);
        count.set(-1);
    }
}