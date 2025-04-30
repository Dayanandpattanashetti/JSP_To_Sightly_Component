<%@include file="/apps/diabetes-patient/common/includes/baseglobal.jsp" %>
<dpcommon:adaptTo adaptable="${slingRequest}" adaptTo="com.nni.aem.dp.common.core.ui.controllers.SearchResults" var="model" />
<c:set var='uui' value="<%=java.util.UUID.randomUUID().toString() %>" scope="request" />
<c:set var="searchlabel" value="${model.rsLanguage == 'en' ? 'Search' : 'Buscar'}" />

<c:if test="${properties.embedClientlib}">
    <cq:includeClientLib js="apps.obesity-patient.saxendacom.search"/>
</c:if>

<div id="search-container">
    <c:if test="${not empty model.rsTitle}">
        <h1>${model.rsTitle}</h1>
    </c:if>

    <div id="search-form${uui}" class="search-form">
        <form class="form-inline">
            <input type="search" name="q" id="q${uui}" class="form-control" aria-label="${searchlabel}" placeholder="${searchlabel}" />
            <input type="submit" class="form-control search-button" value="" title="Search website"/>
        </form>
    </div>

    <div id="pagination-top-${uui}">
        <nav aria-label="Page navigation">
            <ul class="pagination">
            </ul>
        </nav>
    </div>

    <div id="cse_results${uui}"></div>

    <div id="pagination-bottom-${uui}">
        <nav aria-label="Page navigation">
            <ul class="pagination">
            </ul>
        </nav>
    </div>
</div>
