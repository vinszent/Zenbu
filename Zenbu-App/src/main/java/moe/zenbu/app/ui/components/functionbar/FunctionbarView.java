package moe.zenbu.app.ui.components.functionbar;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import org.jrebirth.af.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionbarView extends DefaultView<FunctionbarModel, HBox, FunctionbarController>
{
    private static final Logger log = LoggerFactory.getLogger(FunctionbarView.class);

    private ImageView logoView;
    private HBox logoBox;
    private StackPane functionBarBox;

    public FunctionbarView(final FunctionbarModel model)
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        logoView = new ImageView();
        logoView.getStyleClass().add("logo-view");

        logoBox = new HBox();
        logoBox.getStyleClass().add("logo-box");
        logoBox.getChildren().addAll(logoView);

        functionBarBox = new StackPane();
        functionBarBox.getStyleClass().add("function-bar-box");
        functionBarBox.getChildren().add(getModel().getInnerModel(FunctionbarInnerModels.COLLECTION).getRootNode());
        HBox.setHgrow(functionBarBox, Priority.ALWAYS);

        getRootNode().getChildren().addAll(logoBox, functionBarBox);
        getRootNode().getStyleClass().add("function-bar");

        getModel().doSetCompact(null);
    }
}
