package maps.main.java.com.lynden.gmapsfx;

/**
 * Created by Владислав on 01.11.2016.
 */

import javafx.application.Application;

import javafx.scene.control.Label;
import maps.main.java.com.lynden.gmapsfx.javascript.event.UIEventType;
import maps.main.java.com.lynden.gmapsfx.javascript.object.*;

        import javafx.scene.Scene;
        import javafx.stage.Stage;
import netscape.javascript.JSObject;


public class MainAppVlad extends Application implements MapComponentInitializedListener {

    private GoogleMapView mapView;
    private GoogleMap map;

    @Override
    public void start(Stage stage) throws Exception {

        //Create the JavaFX component and set this as a listener so we know when
        //the map has been initialized, at which point we can then begin manipulating it.
        mapView = new GoogleMapView();
        mapView.addMapInializedListener(this);

        Scene scene = new Scene(mapView);

        stage.setTitle("JavaFX and Google Maps");
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(55.753474, 48.743395))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        map = mapView.createMap(mapOptions);

        //Add a marker to the map
//        MarkerOptions markerOptions = new MarkerOptions();
//
//        LatLong markerLatLong2 = new LatLong(55.753474, 48.743395);
//        markerOptions.position(markerLatLong2)
//                .title("My new Marker")
//                .visible(true);
//
//        Marker myMarker = new Marker( markerOptions );
//        map.addMarker(myMarker);
////////////////////////////////////////
        //ToolBar tb = new ToolBar();
        Label lblClick = new Label();

        //tb.getItems().addAll(new Label("Click: "), lblClick);

        map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
            //System.out.println("LatLong: lat: " + ll.getLatitude() + " lng: " + ll.getLongitude());
            lblClick.setText(ll.toString());
            System.out.println(ll.getLatitude() + " " + ll.getLongitude());
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}