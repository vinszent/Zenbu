package moe.zenbu.app.commands.transitions;

import java.util.concurrent.CountDownLatch;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Duration;

import moe.zenbu.app.util.ConcurrentUtils;

import org.jrebirth.af.core.command.AbstractSingleCommand;
import org.jrebirth.af.core.command.basic.showmodel.DisplayModelWaveBean;
import org.jrebirth.af.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlideRightTransitionCommand extends AbstractSingleCommand<DisplayModelWaveBean>
{
    private static final Logger log = LoggerFactory.getLogger(SlideLeftTransitionCommand.class);

    @Override
    protected void initCommand()
    {
    }

    @Override
    protected void perform(Wave wave)
    {
        final CountDownLatch latch = new CountDownLatch(1);

        Node oldNode = getWaveBean(wave).getHideModel() == null ? null : getWaveBean(wave).getHideModel().getRootNode();

        if(oldNode == null)
        {
            final ObservableList<Node> parentContainer = getWaveBean(wave).getChidrenPlaceHolder();
            oldNode = parentContainer.size() > 1 ? parentContainer.get(getWaveBean(wave).getChidrenPlaceHolder().size() - 1) : null;
        }

        final Node newNode = getWaveBean(wave).getShowModel() == null ? null : getWaveBean(wave).getShowModel().getRootNode();

        if(oldNode != null || newNode != null)
        {
            final ParallelTransition animation = new ParallelTransition();

            if(oldNode != null)
            {
                final TranslateTransition slide = new TranslateTransition();
                slide.setInterpolator(Interpolator.EASE_OUT);
                slide.setFromX(0.0);
                slide.setToX(oldNode.getLayoutBounds().getWidth());
                slide.setNode(oldNode);
                slide.setDuration(Duration.millis(300));

                final FadeTransition fade = new FadeTransition();
                fade.setInterpolator(Interpolator.EASE_OUT);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setNode(oldNode);
                fade.setDuration(Duration.millis(300));

                animation.getChildren().addAll(slide, fade);

                log.debug("Added old node");
            }
            if(newNode != null)
            {
                final TranslateTransition slide = new TranslateTransition();
                slide.setInterpolator(Interpolator.EASE_IN);
                slide.setFromX(-newNode.getLayoutBounds().getWidth());
                slide.setToX(0.0);
                slide.setNode(newNode);
                slide.setDuration(Duration.millis(300));

                final FadeTransition fade = new FadeTransition();
                fade.setInterpolator(Interpolator.EASE_IN);
                fade.setFromValue(0.0);
                fade.setToValue(1.0);
                fade.setNode(newNode);
                fade.setDuration(Duration.millis(300));

                animation.getChildren().addAll(slide, fade);

                log.debug("Added new node");
            }

            final Node oldNodeLink = oldNode;

            animation.setOnFinished(evt ->
            {
                latch.countDown();

                //if(oldNodeLink != null)
                //{
                    //getWaveBean(wave).childrenPlaceHolder().remove(oldNodeLink);
                    //log.debug("Finisehd");
                //}

                getWaveBean(wave).getShowModel().doShowView(wave);
            });

            Platform.runLater(() -> animation.play());
            ConcurrentUtils.awaitSilenty(latch);
        }
    }
}
