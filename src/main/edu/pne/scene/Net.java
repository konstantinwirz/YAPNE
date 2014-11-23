package edu.pne.scene;


import java.util.HashMap;
import javafx.scene.Group;
import javafx.scene.Node;


/**
 * Created by konstantin on 22/11/14.
 */
public class Net extends Group {

    private HashMap<String, Node> nodes = new HashMap<String, Node>();


    public static Net createFromModel(edu.pne.pnml.model.Net model) {
        Net net = new Net();

        // at first create places and transitions then create arcs
        for (edu.pne.pnml.model.Element element : model.getElements()) {
            assert !net.nodes.containsKey(element.getId());
            AbstractNode node = null;
            if (element instanceof edu.pne.pnml.model.Place) {
                node = PlaceBuilder.create()
                        .centerX(element.getPosition().getX())
                        .centerY(element.getPosition().getY())
                        .label(element.getName())
                        .marking(((edu.pne.pnml.model.Place) element).getMarking())
                        .build();
            } else if (element instanceof edu.pne.pnml.model.Transition) {
                node = TransitionBuilder.create()
                        .centerX(element.getPosition().getX())
                        .centerY(element.getPosition().getY())
                        .label(element.getName())
                        .build();
            }
            if (node != null)
                net.nodes.put(element.getId(), node);
        }

        // create arcs
        for (edu.pne.pnml.model.Element element : model.getElements()) {
            assert !net.nodes.containsKey(element.getId());
            if (element instanceof edu.pne.pnml.model.Arc) {
                AbstractNode source = (AbstractNode) net.nodes.get(((edu.pne.pnml.model.Arc) element).getSource().getId());
                AbstractNode target = (AbstractNode) net.nodes.get(((edu.pne.pnml.model.Arc) element).getTarget().getId());
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
