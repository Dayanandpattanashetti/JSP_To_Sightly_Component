package com.nni.aem.brandb.common.core.ui.controllers;

import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.foundation.Image;
import com.nni.aem.brandb.common.core.ui.util.ImageUtils;
import com.nni.aem.brandb.common.core.ui.util.LinksUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by jedlp on 1/10/15.
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class VideoWithMarkers {
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoWithMarkers.class);

    @SlingObject
    SlingHttpServletRequest slingHttpServletRequest;

    @Inject
    Resource resource;

    @Inject
    Style currentStyle;

    // -- properties to handle text
    private String vdTitle;
    private String[] vdVideoLinks;
    private String vdDescription;
    private String vdButtonUrlLink;
    private String vdButtonText;
    private String vdHeight;
    private String vhWidth;

    // -- properties to handle images
    private Image imageDesktop;
    private String titleDesktop;
    private Long heightDesktop;
    private Long widthDesktop;

    private String dataChapters;

    @PostConstruct
    private void initialize() {

        ValueMap properties = resource.adaptTo(ValueMap.class);

        if (properties == null) {
            LOGGER.error("Video Controller properties is null");
            return;
        }
        // -- set properties from resource request
        vdTitle = properties.get("vdTitle", String.class);
        vdVideoLinks = properties.get("vdVideoLink", String[].class);
        vdDescription = properties.get("rtecontent", String.class);
        vdButtonUrlLink = properties.get("vdButtonUrlLink", String.class);
        vdButtonText = properties.get("vdButtonText", String.class);
        vdHeight = properties.get("vdHeight", String.class);
        vhWidth = properties.get("vhWidth", String.class);

        // -- Desktop image properties
        titleDesktop = properties.get("desktop/jcr:title", String.class);
        heightDesktop = properties.get("desktop/height", Long.class);
        widthDesktop = properties.get("desktop/width", Long.class);
        String selectorDesktop = ImageUtils.getImageSelector(widthDesktop, heightDesktop, "high");


        imageDesktop = ImageUtils.getImagePath("desktop",resource,slingHttpServletRequest,selectorDesktop);

        //build data chapters
        String[] vdDataChaptersTime = properties.get("vdDataChaptersTime", String[].class);
        String[] vdDataChaptersTitle = properties.get("vdDataChaptersTitle", String[].class);

        String[] vdDataChaptersStop = properties.get("vdDataChaptersStop", String[].class);
        if (vdDataChaptersStop == null && vdDataChaptersTime != null) {  vdDataChaptersStop = new String[vdDataChaptersTime.length];   } //to make it compatible with previous version, which had no data.
        String[] vdDataChaptersTime2 = properties.get("vdDataChaptersTime2", String[].class);
        if (vdDataChaptersTime2 == null && vdDataChaptersTime != null) {  vdDataChaptersTime2 = new String[vdDataChaptersTime.length];   }
        String[] vdDataChaptersHide = properties.get("vdDataChaptersHide", String[].class);
        if (vdDataChaptersHide == null && vdDataChaptersTime != null) {  vdDataChaptersHide = new String[vdDataChaptersTime.length];   }

        String[] vdDataMarkersHide = properties.get("vdDataMarkersHide", String[].class);
        if (vdDataMarkersHide == null && vdDataChaptersTime != null) {  vdDataMarkersHide = new String[vdDataChaptersTime.length];   }



        StringBuffer sbChapters = new StringBuffer(100);

        if(   (vdDataChaptersTime!=null) && (vdDataChaptersTitle!=null)  ){
            for (int ii=0; ii<vdDataChaptersTime.length; ii++) {
                if (StringUtils.isNotEmpty(vdDataChaptersTime[ii])) {
                    if (ii > 0) {
                        sbChapters.append("||");
                    }
                    sbChapters.append(vdDataChaptersTime[ii]);
                    sbChapters.append("~~");
                    sbChapters.append(vdDataChaptersTitle[ii]);
                    sbChapters.append("~~");
                    if ("yes".equals(vdDataChaptersHide[ii])) {
                        sbChapters.append("false");
                    }else {
                        sbChapters.append("true");
                    }
                    sbChapters.append("~~");

                    if ("yes".equals(vdDataMarkersHide[ii])) {
                        sbChapters.append("false");
                    }else {
                        sbChapters.append("true");
                    }
                    sbChapters.append("~~");


                    if ("yes".equals(vdDataChaptersStop[ii])){
                        sbChapters.append("stop");
                        sbChapters.append("~~");
                        sbChapters.append("0");
                    } else if ( StringUtils.isNotEmpty(vdDataChaptersTime2[ii])) {
                        sbChapters.append("skip");
                        sbChapters.append("~~");
                        sbChapters.append(vdDataChaptersTime2[ii]);
                    }else {
                        sbChapters.append("none");
                        sbChapters.append("~~");
                        sbChapters.append(0);
                    }
                }
            }

        }
        dataChapters =sbChapters.toString();
    }


    public String getImageDesktopUlr() {
        if (imageDesktop == null) {
            return null;
        }
        return imageDesktop.getSrc();
    }


    public String getTitleDesktop() {
        return titleDesktop;
    }

    public String getVdTitle() {
        return vdTitle;
    }

    public String[] getVdVideoLinks() {
        return vdVideoLinks;
    }

    public String getVdDescription() {
        return vdDescription;
    }

    public String getVdButtonUrlLink() {
        if (vdButtonUrlLink!=null) {
            return LinksUtils.addHtmlExtension(vdButtonUrlLink);
        }
        return "#";
    }

    public String getVdButtonText() {
        return vdButtonText;
    }


    public String getVdHeight() {
        return ( vdHeight== null ||vdHeight.length() == 0 ) ? "auto" : vdHeight + "%";
    }

    public String getVhWidth() {
        return ( vhWidth == null || vhWidth.length() == 0 ) ? "auto"  :vhWidth + "%";
    }

    public String getDataChapters() {
       return dataChapters;
    }

}
