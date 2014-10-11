package moe.zenbu.app.ui.components.popups.embedded.seriesoptions.general;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import moe.zenbu.app.resources.I18n;
import moe.zenbu.app.util.ZenbuUtils;

import org.jrebirth.af.core.ui.DefaultView;

public class InlineSeriesOptionsPopupGeneralTabView extends DefaultView<InlineSeriesOptionsPopupGeneralTabModel, BorderPane, InlineSeriesOptionsPopupGeneralTabController>
{
    private VBox centerBox;
    private ComboBox<String[]> statusBox;
    private Button deleteButton;

    public InlineSeriesOptionsPopupGeneralTabView(final InlineSeriesOptionsPopupGeneralTabModel model)
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        statusBox = new ComboBox();
        statusBox.setItems(ZenbuUtils.getStatusList());
        statusBox.setCellFactory(box ->
        {
            return new ListCell<String[]>()
            {
                @Override
                protected void updateItem(final String[] item, final boolean empty)
                {
                    super.updateItem(item, empty);

                    if(empty)
                    {
                        super.setText("");
                    }
                    else
                    {
                        super.setText(item[0]);
                    }
                }
            };
        });
        statusBox.setConverter(new StringConverter<String[]>()
        {

            @Override
            public String toString(final String[] object)
            {
                return object[0];
            }

            @Override
            public String[] fromString(final String string)
            {
                return statusBox.getItems().parallelStream().filter(a -> a[1].equals(string)).findFirst().get();
            }
        });
        
        deleteButton = new Button(I18n.getLocalisedString("popup.series.option.delete"));

        centerBox = new VBox();
        centerBox.getChildren().addAll(statusBox, deleteButton);
        centerBox.getStyleClass().add("center-box");

        getRootNode().setCenter(centerBox);
        getRootNode().getStyleClass().add("series-options-general-tab");
    }

    public ComboBox<String[]> getStatusBox()
    {
        return statusBox;
    }

    public Button getDeleteButton()
    {
        return deleteButton;
    }
}
