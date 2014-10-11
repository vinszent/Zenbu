package moe.zenbu.app.ui.components.pages.download;

import javafx.scene.layout.StackPane;

import org.jrebirth.af.core.key.UniqueKey;
import org.jrebirth.af.core.ui.Model;
import org.jrebirth.af.core.ui.simple.DefaultSimpleModel;

public class DownloadPageModel extends DefaultSimpleModel<StackPane>
{
    private UniqueKey<? extends Model> currentModel;

    @Override
    protected void initModel()
    {
        listen(DownloadPageWaves.CHANGE_DOWNLOAD_PAGE);
    }

    @Override
    protected void initSimpleView()
    {
        super.initSimpleView();
    }

    public void doChangeDownloadPage()
    {
        
    }        
}
