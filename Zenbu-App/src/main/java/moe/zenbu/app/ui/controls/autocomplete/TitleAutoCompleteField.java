package moe.zenbu.app.ui.controls.autocomplete;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import moe.zenbu.app.beans.Title;

import java.util.List;

public class TitleAutoCompleteField extends StackPane
{
    private ObservableList<Title> titleList;
    private FilteredList<Title> filteredList;

    private ComboBox<Title> searchBox;
    private Button clearButton;

    public TitleAutoCompleteField()
    {
        titleList = FXCollections.observableArrayList();
        filteredList = new FilteredList<>(titleList);

        searchBox = new ComboBox<>();
        searchBox.setCellFactory((c) ->
        {
            return new ListCell<Title>()
            {
                @Override
                protected void updateItem(Title item, boolean empty)
                {
                    super.updateItem(item, empty);

                    if(empty)
                    {
                        super.setText(null);
                    }
                    else
                    {
                        super.setText(item.getTitle());
                    }
                }
            };
        });
        searchBox.setConverter(new StringConverter<Title>()
        {
            @Override
            public String toString(Title object)
            {
                if(object != null)
                {
                    return object.getTitle();
                }
                else
                {
                    return null;
                }
            }

            @Override
            public Title fromString(String string)
            {
                return titleList.stream().filter(t -> t.getTitle().equals(string)).findFirst().orElse(null);
            }
        });

        searchBox.getEditor().setOnKeyTyped(evt ->
        {
            filter(searchBox.getEditor().getText());
            searchBox.show();
        });
        searchBox.setItems(filteredList);
        searchBox.setEditable(true);

        clearButton = new Button("c");
        clearButton.setOnAction(evt -> searchBox.getEditor().clear());
//        clearButton.translateXProperty().bind(searchBox.widthProperty().subtract(clearButton.widthProperty()));

        super.getChildren().add(searchBox);
        super.getChildren().add(clearButton);
    }

    public Title getSelectedTitle()
    {
        return searchBox.getValue();
    }

    public ComboBox<Title> getSearchBox()
    {
        return searchBox;
    }

    public void loadTitles(List<Title> titles)
    {
        titleList.setAll(titles);
    }

    private void filter(final String query)
    {
        filteredList.setPredicate(t ->
        {
            if(query.isEmpty())
            {
                return false;
            }
            else if(t.getTitle().toLowerCase().replaceAll("[^a-zA-Z0-9\\!\\?\\s]", "").contains(query.toLowerCase().replaceAll("[^a-zA-Z0-9\\!\\?\\s]", "")))
            {
                return true;
            }
            else
            {
                return false;
            }
        });
    }
}
