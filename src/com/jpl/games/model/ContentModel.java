package com.jpl.games.model;

import com.javafx.experiments.jfx3dviewer.AutoScalingGroup;
import com.javafx.experiments.jfx3dviewer.Xform;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
public class ContentModel {
    public SubScene getSubScene() { return subScene; }
    public void setContent(Node content) { autoScalingGroup.getChildren().add(content);  }
    private SubScene subScene;
    private final Group root3D = new Group();
    private final PerspectiveCamera CameraIMAGEN = new PerspectiveCamera(true);
    private final Rotate yUpRotate = new Rotate(180,0,0,0,Rotate.X_AXIS); // y Up
    private final Rotate XRotate = new Rotate(0,0,0,0,Rotate.X_AXIS);
    private final Rotate ZRotate = new Rotate(0,0,0,0,Rotate.Z_AXIS);
    private final Translate cameraPosition = new Translate(0,0,0);
    private final Xform formcameraX1 = new Xform();
    private final Xform formcameraX2 = new Xform();
    private final AutoScalingGroup autoScalingGroup = new AutoScalingGroup(2);
    private final AmbientLight ambientLight = new AmbientLight(Color.WHITE);
    private final PointLight light1 = new PointLight(Color.WHITE);
    private final double paneW, paneH;
    private double dimesionModel=100d;

    public void resetCam(){
        formcameraX1.ry.setAngle(0.0);
        formcameraX1.rx.setAngle(0.0);
        cameraPosition.setZ(-2d*dimesionModel);
        formcameraX2.t.setX(0.0);
        formcameraX2.t.setY(0.0);
        formcameraX1.setRx(-30.0);
        formcameraX1.setRy(30);
    }
    public ContentModel(double paneW, double paneH, double dimesionModel) {
        this.paneW=paneW;
        this.paneH=paneH;
        this.dimesionModel=dimesionModel;
        buildCameraImagen();
        buildSubScene();
        buildAxes();
        addLights();
    }
    private void buildCameraImagen() {
        CameraIMAGEN.setNearClip(1.0);
        CameraIMAGEN.setFarClip(10000.0);
        CameraIMAGEN.setFieldOfView(2d*dimesionModel/3d);
        CameraIMAGEN.getTransforms().addAll(yUpRotate,cameraPosition,
                XRotate,ZRotate);
        formcameraX1.getChildren().add(formcameraX2);
        formcameraX2.getChildren().add(CameraIMAGEN);
        cameraPosition.setZ(-2d*dimesionModel);
        root3D.getChildren().add(formcameraX1);
        formcameraX1.setRx(-30.0);
        formcameraX1.setRy(30);
    }
    private void buildSubScene() {
        root3D.getChildren().add(autoScalingGroup);
        subScene = new SubScene(root3D,paneW,paneH,true,javafx.scene.SceneAntialiasing.BALANCED);
        subScene.setCamera(CameraIMAGEN);
        subScene.setFill(Color.BLACK);
        setListeners(true);
    }
    private void buildAxes() {
        double length = 2d*dimesionModel;
        double width = dimesionModel/100d;
        double radius = 2d*dimesionModel/100d;
        final PhongMaterial greenMaterial = new PhongMaterial();
        final PhongMaterial redMaterial = new PhongMaterial();
        final PhongMaterial blueMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
        Sphere xSphere = new Sphere(radius);
        Sphere ySphere = new Sphere(radius);
        Sphere zSphere = new Sphere(radius);
        xSphere.setMaterial(redMaterial);
        ySphere.setMaterial(greenMaterial);
        zSphere.setMaterial(blueMaterial);

        xSphere.setTranslateX(dimesionModel);
        ySphere.setTranslateY(dimesionModel);
        zSphere.setTranslateZ(dimesionModel);


    }
    private void addLights(){
        root3D.getChildren().add(ambientLight);
        root3D.getChildren().add(light1);
        light1.setTranslateX(dimesionModel*0.6);
        light1.setTranslateY(dimesionModel*0.6);
        light1.setTranslateZ(dimesionModel*0.6);
    }

    private final EventHandler<ScrollEvent> scrollEventHandler = event -> {
        if (event.getTouchCount() > 0) {
            formcameraX2.setTx(formcameraX2.t.getX() - (0.01*event.getDeltaX()));  // -
            formcameraX2.setTy(formcameraX2.t.getY() + (0.01*event.getDeltaY()));  // -
        } else { // mouse wheel
            double z = cameraPosition.getZ()-(event.getDeltaY()*0.2);
            z = Math.max(z,-10d*dimesionModel);
            z = Math.min(z,0);
            cameraPosition.setZ(z);
        }
    };
    private final EventHandler<ZoomEvent> zoomEventHandler = event -> {
        if (!Double.isNaN(event.getZoomFactor()) && event.getZoomFactor() > 0.8 && event.getZoomFactor() < 1.2) {
            double z = cameraPosition.getZ()/event.getZoomFactor();
            z = Math.max(z,-10d*dimesionModel);
            z = Math.min(z,0);
            cameraPosition.setZ(z);
        }
    };
    private void setListeners(boolean addListeners){
        if(addListeners){

            subScene.addEventHandler(ZoomEvent.ANY, zoomEventHandler);
            subScene.addEventHandler(ScrollEvent.ANY, scrollEventHandler);
        } else {

            subScene.removeEventHandler(ZoomEvent.ANY, zoomEventHandler);
            subScene.removeEventHandler(ScrollEvent.ANY, scrollEventHandler);
        }
    }
}

