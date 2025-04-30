package com.nni.aem.brandb.common.core.ui.controllers;

import com.nni.aem.brandb.common.core.ui.util.LinksUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class)
public class MainHeaderSearch {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainHeaderSearch.class);

    @Inject
    Resource resource;

    // -- properties to handle text
    private String srResultPage;
    private String srLanguage;

    private String searchType;

    @PostConstruct
    private void initialize() {

        if ((resource == null) || org.apache.sling.api.resource.ResourceUtil.isNonExistingResource(resource)) {
            return;
        }

        ValueMap properties = resource.adaptTo(ValueMap.class); 

        // -- set properties from resource request
        final String tempSearchResultPage = properties.get("srResultPage", String.class);
        srResultPage = tempSearchResultPage == null ? "#" : LinksUtils.addHtmlExtension(tempSearchResultPage);
        srLanguage = properties.get("srLanguage", String.class);
        searchType = properties.get("searchType", String.class);
    }

    public String getSrResultPage() {
        return srResultPage;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getSrLanguage() {
        return srLanguage;
    }
}
