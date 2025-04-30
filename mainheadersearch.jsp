<%@include file="/apps/brandbase/common/includes/baseglobal.jsp" %>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="com.nni.aem.brandb.common.core.ui.controllers.MainHeaderSearch" var="model"/>

<c:set var="searchlabel" value="${model.srLanguage == 'es' ? 'Buscar' : 'Search'}"/>
<c:set var="currentPagePath" value="<%=  ((Page)(pageContext.getAttribute("currentPage"))).getPath() %>"/>

<c:choose>
    <c:when test="${properties.searchType == 'navItem'}">
        <div class="frm-search-link nav-item ${fn:startsWith(model.srResultPage, currentPagePath) ? 'option-selected-mobile' : ''}">
            <a class="nav-link-l1" href="${model.srResultPage}">
                <span>
                    ${searchlabel}
                    <em class="fa fa-search"></em>
                </span>
            </a>
        </div>
    </c:when>
    <c:when test="${model.searchType != 'navItem'}">
        <form class="form-inline frm-search" action="${model.srResultPage}">
            <em class="fa fa-search"></em>
            <input class="form-control" aria-label="${searchlabel}" type="text" id="q" name="q" type="search">
            <button class="btn btn-primary" type="submit">${searchlabel}</button>
        </form>
    </c:when>
</c:choose>
