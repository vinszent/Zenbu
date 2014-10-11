package moe.zenbu.app.ui.components.pages.download;

import moe.zenbu.app.enums.DownloadPage;

import org.jrebirth.af.core.wave.WaveItem;
import org.jrebirth.af.core.wave.WaveType;
import org.jrebirth.af.core.wave.WaveTypeBase;

public interface DownloadPageWaves
{
    WaveItem<DownloadPage> DOWNLOAD_PAGE = new WaveItem<DownloadPage>(){};

    WaveType CHANGE_DOWNLOAD_PAGE = WaveTypeBase.build("CHANGE_DOWNLOAD_PAGE", DOWNLOAD_PAGE);
}
