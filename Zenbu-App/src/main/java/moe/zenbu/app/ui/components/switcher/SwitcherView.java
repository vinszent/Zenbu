package moe.zenbu.app.ui.components.switcher;

import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import moe.zenbu.app.resources.I18n;

import org.jrebirth.af.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitcherView extends DefaultView<SwitcherModel, VBox, SwitcherController>
{
    private static final Logger log = LoggerFactory.getLogger(SwitcherView.class);

    private ToggleGroup buttonGroup;
    private ToggleButton collectionButton;
    private ToggleButton browseButton;
    private ToggleButton downloadsButton;
    private ToggleButton chatButton;
    private ToggleButton settingsButton;
    
    private SVGPath collectionSvg;
    private SVGPath browseSvg;
    private SVGPath downloadSvg;
    private SVGPath chatSvg;
    private SVGPath settingsSvg;

    public SwitcherView(final SwitcherModel model)
    {
        super(model);
    }

    @Override
    protected void initView()
    {
        collectionSvg = new SVGPath();
        collectionSvg.setContent("m -24.971428,-10.857139 0,13.7142803 L -16.4,-4 -24.971428,-10.857139 z M -4.4,7.4285699 -4.4,10.17143 C -4.4,11.18126 -5.0144,12 -5.771428,12 l -30.857144,0 C -37.385944,12 -38,11.18126 -38,10.17143 l 0,-2.7428601 3.428572,0 0,-4.5714286 -3.428572,0 0,-4.571432 3.428572,0 0,-4.5714186 -3.428572,0 0,-4.5714297 3.428572,0 0,-4.571432 -3.428572,0 0,-2.742858 C -38,-19.181259 -37.385944,-20 -36.628572,-20 l 30.857144,0 C -5.0144,-20 -4.4,-19.181299 -4.4,-18.171429 l 0,2.742858 -3.428572,0 0,4.57143 3.428572,0 0,4.5714317 -3.428572,0 0,4.5714186 3.428572,0 0,4.5714294 -3.428572,0 0,4.5714312 3.428572,0 z");
        collectionSvg.getStyleClass().add("switcher-svg");

        Region testRegion = new Region();
        testRegion.getStyleClass().add("test-region");

        collectionButton = new ToggleButton();
        collectionButton.setText(I18n.getLocalisedString("switcher.collection"));
        collectionButton.getStyleClass().add("switcher-button");
        collectionButton.setId("collection");
        collectionButton.setMaxWidth(Double.MAX_VALUE);
        collectionButton.setAlignment(Pos.CENTER_LEFT);
        collectionButton.setGraphicTextGap(10);
        collectionButton.setGraphic(collectionSvg);
        collectionButton.setOnAction(evt -> System.out.println(collectionButton.getWidth() + " : " + collectionButton.getHeight()));
        
        browseSvg = new SVGPath();
        browseSvg.setContent("m -14.833038,19.798088 c -0.819345,-0.432339 -0.90268,-0.991014 -1.646023,-1.022014 -0.850346,-0.03867 -1.541689,-0.332339 -2.500037,-0.541675 -0.850345,-0.186336 -2.371367,-1.049015 -3.71172,-1.16035 -1.12835,-0.09167 -3.357716,0.05933 -3.958391,1.149683 2.159698,1.433688 4.746735,2.271033 7.524443,2.271033 1.500022,0 2.941043,-0.24667 4.291728,-0.696677 m -2.416701,-26.4603853 c 0.326338,0.635343 1.187351,0.894013 1.712024,1.371687 1.138683,1.031348 1.628357,0.888679 2.242699,1.87836 0.618343,0.989681 2.611372,2.4167023 2.611372,3.1323793 0,0.714677 -1.007015,1.560356 -1.510356,1.395687 -0.503674,-0.165335 -1.830026,-0.156335 -2.611371,0.118002 -0.781345,0.276004 -6.524094,0.552008 -4.694401,5.406412 0.579675,1.543022 3.121379,1.282685 3.798721,3.840056 0.100668,0.373672 0.45134,1.976029 0.475674,2.500036 0.04167,0.809346 -0.573008,3.86339 0.208336,3.86339 0.784678,0 2.896042,-2.731373 2.896042,-3.226047 0,-0.494674 0.548342,-2.231033 0.548342,-3.716721 0,-1.486355 2.5277033,-1.462354 2.5277033,-3.437717 0,-1.783359 -1.3713533,-2.668705 -1.0623483,-3.522717 0.3020043,-0.849346 2.7120393,-0.88068 3.7153873,-0.983015 -1.753359,-4.5520663 -5.8820863,-7.9321153 -10.8578243,-8.6197923 M -32.770966,6.848566 c 0,3.121379 1.059016,6.000088 2.826708,8.302455 0.500007,-0.392673 1.031349,-1.446688 0.531341,-2.540704 -0.503674,-1.100016 -0.635676,-3.64572 -0.521007,-4.636734 0.111001,-0.989348 0.625009,-3.37705 2.024363,-3.399717 1.395687,-0.02067 2.354034,-0.48234 3.184046,-2.138697 1.722025,-3.446384 -3.232713,-4.107727 -1.510355,-6.0174213 0.48234,-0.535008 2.972043,2.2063653 3.336715,-1.447688 0.02433,-0.259337 -0.22567,-0.650009 -0.559008,-1.056015 -5.406412,1.81636 -9.312803,6.9257673 -9.312803,12.9345213 m 13.6462,-16.0005663 c 8.823127,0 16.0002323,7.1791053 16.0002323,16.0005663 0,8.823129 -7.1771053,16.000234 -16.0002323,16.000234 -8.823129,0 -16.000234,-7.177105 -16.000234,-16.000234 0,-8.821461 7.177105,-16.0005663 16.000234,-16.0005663");
        browseSvg.getStyleClass().add("switcher-svg");

        browseButton = new ToggleButton();
        browseButton.setText(I18n.getLocalisedString("switcher.browse"));
        browseButton.getStyleClass().add("switcher-button");
        browseButton.setId("browse");
        browseButton.setMaxWidth(Double.MAX_VALUE);
        browseButton.setAlignment(Pos.CENTER_LEFT);
        browseButton.setGraphicTextGap(10);
        browseButton.setGraphic(browseSvg);

        downloadSvg = new SVGPath();
        downloadSvg.setContent("m -66.966511,-49.7596 6.211765,0 0,-9.6384 7.152941,0 0,9.6384 6.211764,0 -9.788235,9.185129 -9.788235,-9.185129 z m -7.791812,12.088471 5.992659,-4.032753 3.695435,0 -6.400753,4.910682 6.672188,0 c 0.191248,0 0.3648,0.09675 0.450636,0.2496 l 1.538259,4.207435 11.264753,0 1.538258,-4.207435 c 0.08508,-0.152847 0.259389,-0.2496 0.450636,-0.2496 l 6.672188,0 -6.401506,-4.910682 3.696188,0 5.991906,4.032753 c 0.891859,0.532329 1.424941,1.76 1.186259,2.727529 l -1.056753,5.786353 c -0.239812,0.967529 -1.299953,1.759247 -2.355953,1.759247 l -30.707953,0 c -1.056,0 -2.116141,-0.791718 -2.355953,-1.759247 l -1.056,-5.786353 c -0.240188,-0.967529 0.293647,-2.1952 1.185506,-2.727529");
        downloadSvg.getStyleClass().add("switcher-svg");

        downloadsButton = new ToggleButton();
        downloadsButton.setText(I18n.getLocalisedString("switcher.downloads"));
        downloadsButton.setId("download");
        downloadsButton.getStyleClass().add("switcher-button");
        downloadsButton.setMaxWidth(Double.MAX_VALUE);
        downloadsButton.setAlignment(Pos.CENTER_LEFT);
        downloadsButton.setGraphicTextGap(10);
        downloadsButton.setGraphic(downloadSvg);
        
        chatSvg = new SVGPath();
        chatSvg.setContent("m -86.444444,-90 16,0 c 1.955556,0 3.555556,1.6 3.555556,3.555556 l 0,14.222222 -12.444445,0 -5.333333,5.333333 0,-5.333333 -1.777778,0 c -1.955556,0 -3.555556,-1.6 -3.555556,-3.555556 l 0,-10.666666 C -90,-88.4 -88.4,-90 -86.444444,-90 m 21.688889,19.911111 0,-11.022222 6.755555,0 c 1.955556,0 3.555556,1.6 3.555556,3.555555 l 0,10.666667 c 0,1.955556 -1.6,3.555556 -3.555556,3.555556 l -1.777777,0 0,5.333333 -5.333334,-5.333333 -8.888889,0 c -1.955555,0 -3.555555,-1.6 -3.555555,-3.555556 l 0,-3.236267 c 0.114489,0.02382 0.234311,0.03627 0.355555,0.03627 l 12.444445,0 z");
        chatSvg.getStyleClass().add("switcher-svg");

        chatButton = new ToggleButton();
        chatButton.setText(I18n.getLocalisedString("switcher.chat"));
        chatButton.getStyleClass().add("switcher-button");
        chatButton.setId("chat");
        chatButton.setMaxWidth(Double.MAX_VALUE);
        chatButton.setAlignment(Pos.CENTER_LEFT);
        chatButton.setGraphicTextGap(10);
        chatButton.setGraphic(chatSvg);

        settingsSvg = new SVGPath();
        settingsSvg.setContent("m 17.608165,19.693452 c 0.375669,0.378559 0.375669,0.992633 3.61e-4,1.371192 l -1.717964,1.678229 c -0.37603,0.377837 -0.972043,0.219622 -1.347712,-0.158937 l -8.8636222,-8.7148 2.7167377,-3.090961 9.2121995,8.915277 z M -13.389951,-4.9818651 c 0.134373,-0.892214 0.597097,-0.703295 0.835863,-0.325459 0.238768,0.378559 1.3022,1.988517 1.738915,2.717821 0.434548,0.724969 1.501954,2.15106698 3.492639,0.742669 2.073766,-1.466193 1.35277,-2.488808 0.990466,-3.178016 -0.361943,-0.691014 -1.47703,-2.628238 -1.637773,-2.87134 -0.160381,-0.243101 0.02673,-0.952176 0.670064,-0.654892 0.644417,0.297284 4.556432,1.852699 5.099707,4.080344 0.55339,2.270631 -0.464529,4.29707598 1.53518649,6.3116019 L 1.7592656,4.3701275 -0.67572011,7.2013719 -3.664456,4.3647092 c -0.712688,-0.7162994 -2.231619,-1.4109264 -3.60823,-1.0991934 -2.949363,0.6678965 -4.557877,-0.440327 -5.527393,-2.27062992 -0.86729,-1.638134 -0.723885,-5.08417498 -0.589872,-5.97675098 M 6.5848069,2.2739668 c 0.2160097,-0.249604 0.4836739,-0.255744 0.7181059,-0.05057 L 9.8932235,4.4831895 C 10.09659,4.6623547 10.12332,4.995761 9.9412635,5.20563 L -5.038538,22.250891 c -0.349661,0.403844 -0.957595,0.446468 -1.357466,0.09536 l -1.753363,-1.467277 c -0.399148,-0.351106 -0.44105,-0.964096 -0.09211,-1.367941 L 6.5848069,2.2739668 z M 16.895116,1.3409349 C 15.209301,0.03223388 13.81174,0.93420088 11.944954,3.0935748 11.735085,3.3355928 11.452972,3.0527548 11.293312,2.9122428 11.133653,2.7720888 8.6715765,0.55816988 8.5516515,0.45341688 c -0.120287,-0.105477 -0.265497,-0.303425 -0.07333,-0.525215 0.191085,-0.222511 0.892214,-1.12845198 1.34085,-1.71543498 3.2654305,-4.27179 -8.93369971,-7.168054 -7.0586054,-7.215013 0.9525378,-0.0242 4.7785824,-0.06863 5.3493104,-0.0083 2.3190335,0.245991 5.2272185,2.410784 6.6923275,3.417866 1.914106,1.315564 2.630406,2.082435 2.74997,2.187189 0.541108,0.473921 0.08705,1.567334 1.068489,2.42667698 1.038869,0.912443 1.686176,0.224679 2.286524,0.750616 0.299451,0.263691 1.132425,0.887518 1.371914,1.097748 0.240212,0.21095302 0.282836,0.56711602 0.03865,0.84995192 0,0 -2.28219,2.5224006 -2.474359,2.7434673 C 19.651584,4.6862033 19.137568,4.8740378 18.81825,4.5930084 18.497847,4.3123403 17.677877,3.5931508 17.538446,3.4692518 17.39757,3.3475208 17.630196,1.9113088 16.895113,1.3409429");
        settingsSvg.getStyleClass().add("switcher-svg");

        settingsButton = new ToggleButton();
        settingsButton.setText(I18n.getLocalisedString("switcher.settings"));
        settingsButton.setId("settings");
        settingsButton.getStyleClass().add("switcher-button");
        settingsButton.setMaxWidth(Double.MAX_VALUE);
        settingsButton.setAlignment(Pos.CENTER_LEFT);
        settingsButton.setGraphicTextGap(10);
        settingsButton.setGraphic(settingsSvg);

        buttonGroup = new ToggleGroup();
        buttonGroup.getToggles().addAll(collectionButton, browseButton, downloadsButton, chatButton, settingsButton);
        buttonGroup.selectedToggleProperty().addListener((ov, oldVal, newVal) ->
        {
            if(newVal == null)
            {
                buttonGroup.selectToggle(oldVal);
            }
        });
        buttonGroup.selectToggle(collectionButton);

        getRootNode().getStyleClass().add("switcher");
        getRootNode().getChildren().addAll(collectionButton, browseButton, downloadsButton, chatButton, settingsButton);
        
        getModel().doSetCompact(null);

        log.info("Initialised switcher view");
    }

    public ToggleGroup getButtonGroup()
    {
        return buttonGroup;
    }

    public ToggleButton getCollectionButton()
    {
        return collectionButton;
    }

    public ToggleButton getBrowseButton()
    {
        return browseButton;
    }

    public ToggleButton getDownloadButton()
    {
        return downloadsButton;
    }

    public ToggleButton getChatButton()
    {
        return chatButton;
    }

    public ToggleButton getSettingsButton()
    {
        return settingsButton;
    }
}
