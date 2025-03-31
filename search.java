import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.foundation.Search;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.foundation.FileTypes;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;
import java.util.Map;


@Model(adaptables = SlingHttpServletRequest.class)
public class SearchModel {

    private static final Logger log = LoggerFactory.getLogger(SearchModel.class);

    @SlingObject
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

    @ScriptVariable
    private Page currentPage;

    @Inject
    @Named("q")
    private String query;

    @Inject
    @Named("searchIn")
    private String searchInProperty;

    private Search search;
    private TagManager tagManager;
    private FileTypes fileTypes = new FileTypes();
    private SearchTrends trends;
    private SearchResult result;

    @PostConstruct
    protected void init() {
        ResourceResolver resourceResolver = resource.getResourceResolver();
        tagManager = resourceResolver.adaptTo(TagManager.class);

        search = new Search(request);

        String requestSearchPath = request.getParameter("path");
        if (searchInProperty != null) {
            if (requestSearchPath != null && requestSearchPath.startsWith(searchInProperty)) {
                search.setSearchIn(requestSearchPath);
            } else {
                search.setSearchIn(searchInProperty);
            }
        } else if (requestSearchPath != null) {
            search.setSearchIn(requestSearchPath);
        }

        if (query != null) {
            search.setQuery(query);
        }

        try {
            search.getResult(); // Execute the search
        } catch (Exception e) {
            log.error("Error executing search", e);
        }
        trends = search.getTrends();
        result = search.getResult();


    }

    public String getQuery() {
        return search.getQuery();
    }

    public SearchTrends getTrends() {
        return search.getTrends();
    }

    public SearchResult getResult() {
        return search.getResult();
    }

    public List<SearchHit> getHits() {
        if (result != null) {
            return result.getHits();
        }
        return null;
    }

    public boolean hasResults() {
        if(result != null) {
            return result.getHits() != null && !result.getHits().isEmpty();
        }
        return false;
    }

    public String getSpellcheck() {
        if(result != null) {
            return result.getSpellcheck();
        }
        return null;
    }

    public int getStartIndex() {
        if(result != null) {
           return result.getStartIndex();
        }
        return 0;
    }

    public int getEndIndex() {
        if(result != null) {
            return result.getStartIndex() + result.getHits().size();
        }
        return 0;
    }


    public int getTotalMatches() {
        if(result != null) {
            return result.getTotalMatches();
        }
        return 0;
    }

    public long getExecutionTime() {
        if(result != null) {
            return result.getExecutionTime();
        }
        return 0;
    }

     public List<String> getRelatedQueries() {
       if(search != null) {
          return search.getRelatedQueries();
       }
       return null;
    }

   public Map<String, String> getFileTypes() {
    return fileTypes;
   }

    public TagManager getTagManager() {
      return tagManager;
    }

    public Facets getFacets() {
        if(result != null) {
           return result.getFacets();
        }
        return null;
    }


    public String getSearchInProperty() {
        return searchInProperty;
    }

    public List<ResultPage> getResultPages() {
        if(result != null) {
           return result.getResultPages();
        }
        return null;
    }

     public ResultPage getPreviousPage() {
        if(result != null) {
           return result.getPreviousPage();
        }
        return null;
    }

    public ResultPage getNextPage() {
        if(result != null) {
           return result.getNextPage();
        }
        return null;
    }

}