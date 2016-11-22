/*
 * Copyright 2015 Andre.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package maps.main.java.com.lynden.gmapsfx.service.geocoding;

/**
 *
 * @author Andre
 */
import maps.main.java.com.lynden.gmapsfx.javascript.*;
import maps.main.java.com.lynden.gmapsfx.javascript.object.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import netscape.javascript.JSObject;

/**
 *
 * @author jlstephens89
 */
public class GeocoderGeometry extends JavascriptObject {

    public GeocoderGeometry() {
        super(GMapObjectType.GEOCODER_GEOMETRY);
    }

    public GeocoderGeometry(JSObject jsObject) {
        super(GMapObjectType.GEOCODER_GEOMETRY, jsObject);
    }

    private LatLong getLocation() {
        try {
            JSObject location = (JSObject) getJSObject().getMember("location");
            return new LatLong((JSObject) location);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "", e);
        }
        return null;
    }

    private GeocoderLocationType getLocationType() {
        try {
            String locationType = getJSObject().getMember("location_type").toString();
            return GeocoderLocationType.valueOf(locationType.toUpperCase());

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "", e);
        }
        return null;
    }

    private LatLongBounds getViewPort() {
        try {
            JSObject viewPort = (JSObject) getJSObject().getMember("viewport");
            return new LatLongBounds((JSObject) viewPort);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "", e);
        }
        return null;
    }

    private LatLongBounds getBounds() {
        try {
            JSObject bounds = (JSObject) getJSObject().getMember("bounds");
            return new LatLongBounds((JSObject) bounds);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "", e);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\t Location: ").append(getLocation()).append("\n");
        builder.append("\t Location Type: ").append(getLocationType()).append("\n");
        builder.append("\t View Port: ").append(getViewPort()).append("\n");
        builder.append("\t Bounds: ").append(getBounds()).append("\n");
        return builder.toString();
    }

}
