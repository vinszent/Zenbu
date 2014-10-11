package moe.zenbu.app.ui.components.popups.embedded.episodeinfo;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.Region;

import moe.zenbu.app.beans.Episode;

import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Wave;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Memory;
import com.sun.jna.Native;

public class EpisodeInfoPopupModel extends DefaultModel<EpisodeInfoPopupModel, EpisodeInfoPopupView>
{
    static
    {
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
    }

    private WritablePixelFormat<ByteBuffer> previewPixelFormat;
    private AtomicInteger frameNumber;
    private DirectMediaPlayer player;

    private DoubleProperty playerPositionProperty = new SimpleDoubleProperty();

    @Override
    public void initModel()
    {
        listen(EpisodeInfoPopupWaves.SHOW);
        listen(EpisodeInfoPopupWaves.HIDE);

        player = new MediaPlayerFactory("--quiet", "--quiet-synchro").newDirectMediaPlayer(new BufferFormatCallback()
        {
            public BufferFormat getBufferFormat(int width, int height)
            {
                Platform.runLater(() -> getView().getCanvas().resize(width, height));

                return new RV32BufferFormat(width, height);
            }
        }, new RenderCallback()
        {
            AtomicReference<ByteBuffer> currentByteBuffer = new AtomicReference<>();

            public void display(DirectMediaPlayer arg0, Memory[] arg1, BufferFormat arg2)
            {
                int renderFrameNumber = frameNumber.get();

                currentByteBuffer.set(arg1[0].getByteBuffer(0, arg1[0].size()));

                Platform.runLater(() ->
                {
                    ByteBuffer byteBuffer = currentByteBuffer.get();
                    int actualFrameNumber = frameNumber.get();

                    if(renderFrameNumber == actualFrameNumber)
                    {
                        getView().getCanvas().getPixelWriter().setPixels(0, 0, arg2.getWidth(), arg2.getHeight(), previewPixelFormat, byteBuffer, arg2.getPitches()[0]);
                    }
                });
            }
        });

        player.addMediaPlayerEventListener(new MediaPlayerEventAdapter()
        {
            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition)
            {
                playerPositionProperty.setValue(newPosition);
            }
        });
       
        frameNumber = new AtomicInteger(0);
        previewPixelFormat = PixelFormat.getByteBgraInstance();
    }

    @Override
    public void bind()
    {
    }
    
    public void doShow(final Region origin, final Episode episode, final Wave wave)
    {
        getView().getSubgroupLabel().setText(episode.getSubgroups().stream().map(s -> s.getSubgroup()).collect(Collectors.joining(", ")));
        getView().getVideoFlagLabel().setText(episode.getVideoFlags().stream().map(s -> s.getVideoFlag()).collect(Collectors.joining(", ")));
        getView().getAudioFlagLabel().setText(episode.getAudioFlags().stream().map(s -> s.getAudioFlag()).collect(Collectors.joining(", ")));

        getView().getRootNode().show();
        new Thread(() ->
        {
            player.startMedia(episode.getFilepath());
            player.pause();
        }).start(); // TODO: Temporary
    }

    public void doHide(final Wave wave)
    {
        player.stop();
        getView().getRootNode().hide();
    }

    public DirectMediaPlayer getPlayer()
    {
        return player;
    }

    public DoubleProperty getPlayerPositionProperty()
    {
        return playerPositionProperty;
    }
}
