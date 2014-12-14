package de.kwirz.yapne.scene;

import de.kwirz.yapne.utils.BuilderValue;

/**
 * Created by konstantin on 18/11/14.
 */
public class PlaceBuilder extends AbstractNodeBuilder<PlaceBuilder> {
    private BuilderValue<Integer> marking_ = new BuilderValue<Integer>();

    public static PlaceBuilder create() {
        return new PlaceBuilder();
    }

    public PlaceBuilder marking(int value) {
        marking_.setValue(value);
        return this;
    }

    public Place build() {
        Place place = new Place();
        prepare(place);

        if (marking_.isSet())
            place.setMarking(marking_.getValue());

        return place;
    }
}
