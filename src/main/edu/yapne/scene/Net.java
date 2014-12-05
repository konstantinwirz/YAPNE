package edu.yapne.scene;

import java.util.HashMap;
import javafx.scene.Group;
import javafx.scene.Node;

import edu.yapne.model.*;


/**
 * Created by konstantin on 22/11/14.
 */
public class Net extends Group {

    private HashMap<String, Node> nodes = new HashMap<String, Node>();


    public static Net createFromModel(PetriNet model) {
        Net net = new Net();

        // at first create places and transitions then create arcs
        for (final String id : model.getIds()) {
            PetriNetElement element = model.getElementById(id);
            assert element != null;
            assert !net.nodes.containsKey(element.getId());
            AbstractNode node = null;
            if (element instanceof PetriNetPlace) {
                PetriNetPlace place = (PetriNetPlace) element;
                node = PlaceBuilder.create()
                        .centerX(place.getPosition().getX())
                        .centerY(place.getPosition().getY())
                        .label(place.getName())
                        .marking(((PetriNetPlace) element).getMarking())
                        .build();
            } else if (element instanceof PetriNetTransition) {
                PetriNetTransition transition = (PetriNetTransition) element;
                node = TransitionBuilder.create()
                        .centerX(transition.getPosition().getX())
                        .centerY(transition.getPosition().getY())
                        .label(transition.getName())
                        .build();
            }
            if (node != null)
                net.nodes.put(element.getId(), node);
        }

        // create arcs
        for (final String id : model.getIds()) {
            PetriNetElement element = model.getElementById(id);
            assert element != null;
            assert !net.nodes.containsKey(element.getId());
            if (element instanceof PetriNetArc) {
                AbstractNode source = (AbstractNode) net.nodes.get(((PetriNetArc) element).getSource().getId());
                AbstractNode target = (AbstractNode) net.nodes.get(((PetriNetArc) element).getTarget().getId());
                assert source != null && target != null;
                Arc arc = ArcBuilder.create()
                        .source(source)
                        .target(target)
                        .build();
                net.nodes.put(element.getId(), arc);
            }
        }

        net.getChildren().addAll(net.nodes.values());

        return net;
    }

}
