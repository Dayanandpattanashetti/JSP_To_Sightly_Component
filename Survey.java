package com.nni.aem.dp.common.core.ui.controllers;


import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.foundation.Image;
import com.nni.aem.dp.common.core.ui.util.ImageUtils;
import com.nni.aem.dp.common.core.ui.util.LinksUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.stream.IntStream;

@Model(adaptables = SlingHttpServletRequest.class)
public class Survey {

    private static final Logger LOGGER = LoggerFactory.getLogger(Survey.class);

    @SlingObject
    SlingHttpServletRequest slingHttpServletRequest;

    @Inject
    Resource resource;

    @Inject
    Style currentStyle;

    private String headerRteContent;
    private Integer questionsNumber;
    private String nextButtonLabel;
    private String finishButtonLabel;
    private String startLabel;
    private String finishLabel;
    private String redirectPath;
    private Image imageDesktop;

    private SurveyQuestionItem[] surveyQuestions;
    private String jsonItems;

    @PostConstruct
    private void initialize() {

        ResourceResolver resourceResolver = slingHttpServletRequest.getResourceResolver();
        ValueMap properties = resource.adaptTo(ValueMap.class);

        // -- set properties from resource request
        headerRteContent = properties.get("headerRteContent", String.class);
        questionsNumber = properties.get("questionsNumber", Integer.class);
        nextButtonLabel = properties.get("nextButtonLabel", String.class);
        finishButtonLabel = properties.get("finishButtonLabel", String.class);
        startLabel = properties.get("startLabel", String.class);
        finishLabel = properties.get("finishLabel", String.class);
        redirectPath = properties.get("redirectPath", String.class);

        // -- Desktop image properties
        String titleDesktop= properties.get("desktop/jcr:title",String.class);
        Long heightDesktop  = properties.get("desktop/height", Long.class);
        Long widthDesktop =  properties.get("desktop/width",Long.class);
        String selectorDesktop = ImageUtils.getImageSelector(widthDesktop,heightDesktop,"high");
        imageDesktop = ImageUtils.getImagePath("desktop",resource,slingHttpServletRequest,selectorDesktop);

        surveyQuestions = new SurveyQuestionItem[questionsNumber];
        if(questionsNumber!=null){
            IntStream.iterate(0, i -> i +1)
            .limit(questionsNumber)
            .forEach(i -> {
                int idx = i+1;
                String surveyQuestion = properties.get("question" + idx, String.class);
                String[] surveyAnswers = properties.get("qnaAnswer" + idx, String[].class);
                String[] surveyTips = properties.get("qnaAnswerTip" + idx, String[].class);
                SurveyQuestionItem item = new SurveyQuestionItem(surveyQuestion, surveyAnswers, surveyTips);
                surveyQuestions[i] = item;
            });
        }else{
            surveyQuestions = new SurveyQuestionItem[0];
        }
    }

    public int getQuestionsNumber() {
        return questionsNumber;
    }

    public String getNextButtonLabel() {
        return nextButtonLabel;
    }

    public String getHeaderRteContent() {
        return headerRteContent;
    }

    public SurveyQuestionItem[] getSurveyQuestions() {
        return surveyQuestions;

    }

    public String getJsonItems() {
        return jsonItems;
    }

    public String getStartLabel() {
        return startLabel;
    }

    public String getFinishLabel() {
        return finishLabel;
    }

    public String getFinishButtonLabel() {
        return finishButtonLabel;
    }

    public String getRedirectPath() {
        if(StringUtils.isNotEmpty(redirectPath)){
            return LinksUtils.addHtmlExtension(redirectPath);
        }
        return "#";
    }

    public String getImageDesktopSrc(){
        if (imageDesktop == null) {
            return null;
        }
        if (imageDesktop.getPath().toLowerCase().endsWith(".gif")) {
            return imageDesktop.getPath();
        }
        return imageDesktop.getSrc();
    }

}
