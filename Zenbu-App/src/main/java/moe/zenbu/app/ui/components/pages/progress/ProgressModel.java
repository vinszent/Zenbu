package moe.zenbu.app.ui.components.pages.progress;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressModel extends DefaultModel<ProgressModel, ProgressView>
{
    private static final Logger log = LoggerFactory.getLogger(ProgressModel.class);

    private DoubleProperty total;
    private DoubleProperty current;
    private StringProperty text;

    @Override
    protected void initModel()
    {
        listen(ProgressPageWaves.SET_TOTAL);
        listen(ProgressPageWaves.INCREMENT);
        listen(ProgressPageWaves.SET_TEXT);
        listen(ProgressPageWaves.SET_INDEFINITE);

        total = new SimpleDoubleProperty();
        current = new SimpleDoubleProperty();
        text = new SimpleStringProperty();
    }

    @Override
    protected void bind()
    {
        getView().getProgressBar().progressProperty().bind(current.divide(total));
        getView().getStatusLabel().textProperty().bind(text);
    }

    public void doSetTotal(final Integer value, final Wave wave)
    {
        log.trace("Setting total to {}", value);

        total.set(value);
        current.set(0);
    }

    public void doIncrement(final Wave wave)
    {
        log.trace("Incrementing");

        current.set(current.get() + 1);
    }

    public void doSetText(final String text, final Wave wave)
    {
        log.trace("Setting text to {}", text);

        this.text.set(text);
    }

    public void doSetIndefinite(final Wave wave)
    {
        log.trace("Setting to indefinite");

        System.out.println("Setting indefinite");
        current.set(1);
        total.set(-1);
    }
}
