package moe.zenbu.app.ui.controls;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;
import javafx.util.Duration;

import org.apache.commons.lang3.StringUtils;

public class AutoCompleteField extends StackPane
{
    private ComboBox searchBox;
    private SVGPath clearSvg;
    private Button clearButton;

    private FadeTransition showClearButtonTransition;
    private FadeTransition hideClearButtonTransition;

    private List<String> items;
    private Executor filterExecutor;

    public AutoCompleteField()
    {
        this(false);
    }

    public AutoCompleteField(final boolean useTextProperty)
    {
        items = Collections.emptyList();
        filterExecutor = Executors.newSingleThreadExecutor();

        searchBox = new ComboBox();
        searchBox.setEditable(true);
        searchBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>()
        {
            public ListCell<String> call(ListView view)
            {
                return new ListCell<String>()
                {
                    {
                        super.maxWidthProperty().bind(this.widthProperty().subtract(40));
                        super.prefWidthProperty().bind(this.widthProperty().subtract(40));
                        super.minWidthProperty().bind(this.widthProperty().subtract(40));
                    }

                    @Override
                    public void updateItem(final String item, final boolean empty)
                    {
                        super.updateItem(item, empty);

                        if(empty)
                        {
                            super.setText(null);
                        }
                        else
                        {
                            super.setText(item);
                        }
                    }
                };
            }
        });

        if(useTextProperty)
        {
            searchBox.getEditor().textProperty().addListener((ov, oldVal, newVal) ->
            {
                if(newVal.isEmpty())
                {
                    hideClearButtonTransition.playFromStart();
                }
                else
                {
                    if(clearButton.getOpacity() == 0.0)
                    {
                        showClearButtonTransition.playFromStart();
                    }
                }

                this.filter(newVal);
            });
        }
        else
        {
            searchBox.getEditor().setOnKeyTyped(evt ->
            {
                String newVal = searchBox.getEditor().getText();

                if(newVal.isEmpty())
                {
                    hideClearButtonTransition.playFromStart();
                }
                else
                {
                    if(clearButton.getOpacity() == 0.0)
                    {
                        showClearButtonTransition.playFromStart();
                    }
                }

                this.filter(newVal);
            });
        }

        clearSvg = new SVGPath();
        clearSvg.setContent("m -33.932531,-7.68225 -3.447,3.93925 3.447,3.93949985 c 0.58575,0.58574999 0.58575,1.53549995 0,2.12149995 -0.58625,0.58575 -1.536,0.5855 -2.1215,-2.5e-4 L -39.368281,-1.47 l -3.31425,3.7877498 c -0.58575,0.58575 -1.5355,0.586 -2.1215,2.5e-4 -0.585751,-0.586 -0.585751,-1.53574996 0,-2.12149995 l 3.447,-3.93949985 -3.447,-3.93925 c -0.585751,-0.58575 -0.585751,-1.5355 -2.5e-4,-2.12125 0.58575,-0.586 1.5355,-0.586 2.12175,0 l 3.31425,3.788 3.31425,-3.788 c 0.586,-0.586 1.53575,-0.586 2.12175,0 0.5855,0.58575 0.5855,1.5355 -2.5e-4,2.12125");
        clearSvg.getStyleClass().add("svg");

        clearButton = new Button();
        clearButton.getStyleClass().add("clear-button");
        clearButton.setOpacity(0.0);
        clearButton.setOnAction(evt -> searchBox.getEditor().clear());
        clearButton.setGraphic(clearSvg);
        clearButton.hoverProperty().addListener((ov, oldVal, newVal) ->
        {
            if(newVal)
            {
                clearSvg.getStyleClass().add("hover");
            }
            else
            {
                clearSvg.getStyleClass().remove("hover");
            }
        });

        this.getChildren().addAll(searchBox, clearButton);
        this.getStyleClass().add("auto-complete-field");

        searchBox.prefWidthProperty().bind(this.widthProperty());
        searchBox.prefHeightProperty().bind(this.heightProperty());

        clearButton.translateXProperty().bind(searchBox.widthProperty().divide(2.0).subtract(clearButton.widthProperty()));

        showClearButtonTransition = new FadeTransition(Duration.millis(350), clearButton);
        showClearButtonTransition.setFromValue(0.0);
        showClearButtonTransition.setToValue(1.0);

        hideClearButtonTransition = new FadeTransition(Duration.millis(350), clearButton);
        hideClearButtonTransition.setFromValue(1.0);
        hideClearButtonTransition.setToValue(0.0);
    }

    private void filter(final String arg)
    {
        String query = arg.replaceAll("[^a-zA-Z0-9\\!\\?\\s]", "");

        filterExecutor.execute(() ->
        {
            List<String> filtered = items.stream().filter(s -> StringUtils.containsIgnoreCase(s.replaceAll("[^a-zA-Z0-9\\!\\?\\s]", ""), query)).collect(Collectors.toList());

            Platform.runLater(() ->
            {
                searchBox.getItems().setAll(filtered);
                if(!query.isEmpty())
                {
                    searchBox.show();
                }
            });
        });
    }

    public ComboBox getSearchBox()
    {
        return searchBox;
    }

    public Button getClearButton()
    {
        return clearButton;
    }

    public void setItems(List<String> items)
    {
        this.items = items;
    }

    public void addKeyTypedHandler(final EventHandler<KeyEvent> handler)
    {
        searchBox.getEditor().addEventHandler(KeyEvent.KEY_TYPED, handler);
    }

    public String getText()
    {
        return searchBox.getEditor().getText();
    }

    public void clear()
    {
        searchBox.getEditor().clear();
        hideClearButtonTransition.playFromStart();
    }

    public TextField getEditor()
    {
        return searchBox.getEditor();
    }

    @Deprecated
    public StackPane getRoot()
    {
        // TODO: Remove this
        return this;
    }
}
