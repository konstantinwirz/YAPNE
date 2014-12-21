package de.kwirz.yapne.presentation;

import java.util.HashMap;
import de.kwirz.yapne.model.*;
import de.kwirz.yapne.utils.Settings;
import javafx.scene.Node;
import javafx.scene.layout.Pane;



/**
 * Created by konstantin on 20/12/14.
 */
public class PetriNetPresentation extends Pane {

    private PetriNet model;

    public void setModel(PetriNet model) {
        this.model = model;
        initializeFromModel();
    }

    private void initializeFromModel() {
        if (model == null)
            return;

        // Hilfsabbildung
        HashMap<String, Node> nodes = new HashMap<>();

        Settings settings = Settings.getInstance();
        double strokeWidth =
                Double.valueOf(settings.getValue("stroke_width", PetriNetNodePresentation.getDefaultStrokeWidth()));
        double nodeSize =
                Double.valueOf(settings.getValue("node_size", PetriNetNodePresentation.getDefaultSize()));


        // at first create places and transitions then create arcs
        for (final String id : model.getIds()) {
            PetriNetElement element = model.getElementById(id);
            assert element != null;
            assert !nodes.containsKey(element.getId());

            PetriNetElementPresentation presentation = null;
            if (element instanceof PetriNetPlace) {
                presentation = PetriNetPlacePresentationBuilder.create()
                        .model((PetriNetPlace) element)
                        .size(nodeSize)
                        .strokeWidth(strokeWidth)
                        .build();
            } else if (element instanceof PetriNetTransition) {
                presentation = PetriNetTransitionPresentationBuilder.create()
                        .model((PetriNetTransition) element)
                        .size(nodeSize)
                        .strokeWidth(strokeWidth)
                        .build();
            }
            if (presentation != null)
                nodes.put(element.getId(), (Node) presentation);
        }

        // create arcs
        for (final String id : model.getIds()) {
            PetriNetElement element = model.getElementById(id);
            assert element != null;
            assert !nodes.containsKey(element.getId());

            if (element instanceof PetriNetArc) {
                PetriNetNodePresentation source =
                        (PetriNetNodePresentation) nodes.get(((PetriNetArc) element).getSource().getId());
                PetriNetNodePresentation target =
                        (PetriNetNodePresentation) nodes.get(((PetriNetArc) element).getTarget().getId());

                assert source != null && target != null;
                PetriNetArcPresentation presentation = PetriNetArcPresentationBuilder.create()
                                                        .source(source)
                                                        .target(target)
                                                        .model((PetriNetArc) element)
                                                        .build();
                nodes.put(element.getId(), (Node) presentation);
            }
        }

        getChildren().addAll(nodes.values());
    }

    public PetriNet getModel() {
        return model;
    }

    public void syncToModel() {

    }



    public void syncFromModel() {



    }
}
