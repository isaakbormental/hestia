/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package maps.main.java.com.lynden.gmapsfx.service.elevation;

import maps.main.java.com.lynden.gmapsfx.javascript.*;
import maps.main.java.com.lynden.gmapsfx.javascript.object.*;


/** Creates a request that can be passed in to the {@link ElevationService} to 
 * get the elevations for a number of locations.
 *
 * @author Geoff Capper
 */
public class LocationElevationRequest extends JavascriptObject {
    
    public LocationElevationRequest() {
        super(GMapObjectType.OBJECT);
    }
    
    public LocationElevationRequest(LatLong[] locations) {
        super(GMapObjectType.OBJECT);
        getJSObject().setMember("locations", getJSObject().eval("[]"));
        for (int i = 0; i < locations.length; i++) {
            getJSObject().eval(getVariableName()+".locations.push("+locations[i].getVariableName()+")");
        }
    }
    
}
