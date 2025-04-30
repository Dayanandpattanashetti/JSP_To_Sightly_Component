package com.nni.aem.dp.common.core.ui.controllers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.day.cq.wcm.api.designer.Style;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jedlp on 1/30/15.
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class SearchResults {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResults.class);

    @SlingObject
    SlingHttpServletRequest slingHttpServletRequest;

    @Inject
    Resource resource;

    @Inject
    Style currentStyle;

    // -- properties to handle text

    private String rsToken;
    private String rsCX;
    private String srchOptDomainSearch;
    private String rsTitle;
    private String rsLanguage;
    private String rsWithoutResults;
    private String rsError;
    private String rsInvalidPage;
    private String rsInvalidPageLink;

    @PostConstruct
    private void initialize() {
        ValueMap properties = resource.adaptTo(ValueMap.class);

        // -- set properties from resource request
        rsToken = properties.get("rsToken", String.class);
        rsCX = properties.get("rsCX", String.class);
        srchOptDomainSearch = properties.get("srchOptDomainSearch", String.class);
        rsTitle = properties.get("rsTitle", String.class);
        rsLanguage = properties.get("rsLanguage", String.class);
        rsWithoutResults = properties.get("rsWithoutResults", String.class);
        rsError = properties.get("rsError", String.class);
        rsInvalidPage = properties.get("rsInvalidPage", String.class);
        rsInvalidPageLink = properties.get("rsInvalidPageLink", String.class);
    }

    public String getRsToken() {
        return rsToken;
    }

    public String getRsCX() {
        return rsCX;
    }

    public String getSrchOptDomainSearch() {
        return srchOptDomainSearch;
    }

    public String getRsTitle() {
        return rsTitle;
    }

    public String getRsLanguage() {
        return rsLanguage;
    }

    public String getRsWithoutResults() {
        return rsWithoutResults;
    }

    public String getRsError() {
        return rsError;
    }

    public String getRsInvalidPage() {
        return rsInvalidPage;
    }

    public String getRsInvalidPageLink() {
        return rsInvalidPageLink;
    }
}
