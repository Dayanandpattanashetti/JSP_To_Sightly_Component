<%@include file="/apps/brandbase/common/includes/baseglobal.jsp" %>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="com.nni.aem.brandb.common.core.ui.controllers.VideoWithMarkers" var="model"/>
<c:set var='uui' value="<%=java.util.UUID.randomUUID().toString() %>" scope="request"/>

<%-- Video Chapter List Title --%>
<c:set var="vdChapterTitle" value="Video Chapters"/>
<c:if test="${not empty properties.vdChapterTitle}">
    <c:set var="vdChapterTitle" value="${properties.vdChapterTitle}"/>
</c:if>

<div class="video-wrapper noselection">
    <c:if test="${not empty model.vdTitle && fn:trim(model.vdTitle) != ''}">
        <span class="video-modal-title">${model.vdTitle}</span>
    </c:if>
    <c:if test="${not empty properties.shareVideoUrlLink}">
        <%-- End get  embedded/modal content--%>
        <div class="video-chapters-share  ${properties.sharevideoAlignment}">
           <a data-toggle="modal" data-target="#sem${uui}" class="" href="#">${ not empty properties.shareVideoUrlLabel ? properties.shareVideoUrlLabel : 'Share this video'}</a>
        </div>

        <%--contento to be shown--%>
        <c:catch var ="catchException">
            <c:set var="embeddedResource" value="${properties.shareVideoUrlLink}/jcr:content/blankpagepar"/>
            <c:if test="${not empty embeddedResource}">
                <dpcommon:disableWCMMode>
                    <div style="float:left;">
                        <sling:include path="${embeddedResource}.emb.html"/>
                    </div>
                </dpcommon:disableWCMMode>
            </c:if>
        </c:catch>
        <c:if test = "${catchException != null}">
            <p>The exception is : ${catchException}   ${catchException.message}</p>
        </c:if>
        <%--contento to be shown--%>
    </c:if>
    <video
        class="video-js vjs-16-9 vjs-default-skin vjs-big-play-centered video_container with-markers"
        controls preload="none"
        poster="${model.imageDesktopUlr}"
        width="${model.vhWidth}"  height="${model.vdHeight}"
        data-chapters="${model.dataChapters}"
        data-tagid-resume="${properties.tagidResume}"
        data-tagid-pause="${properties.tagidPause}">
        <c:forEach items="${model.vdVideoLinks}" var="link" varStatus="status">
            <c:set var="kind" value='${fn:substring(fn:trim(link),fn:length(link)-4,fn:length(link))}'/>
            <c:if test='${fn:contains(kind,"." )}'>
                <c:if test="${fn:substring(kind,1,4) !='mov'}">
                    <source src="${link}" type="video/${fn:substring(kind,1,4)}">
                </c:if>
                <c:if test="${fn:substring(kind,1,4) =='mov'}">
                    <source src="${link}" type='video/mp4; codecs="avc1.42E01E, mp4a.40.2"'/>
                </c:if>
            </c:if>
            <c:if test='${!fn:contains(kind,"." )}'>
                <c:if test='${fn:contains(fn:toLowerCase(link),"mp4")}'>
                    <source src="${link}" type="video/mp4">
                </c:if>
                <c:if test='${fn:contains(fn:toLowerCase(link),"webm" )}'>
                    <source src="${link}" type="video/webm">
                </c:if>
                <c:if test='${fn:contains(fn:toLowerCase(link),"flv" )}'>
                    <source src="${link}" type="video/flv">
                </c:if>
                <c:if test='${fn:contains(fn:toLowerCase(link),"mov")}'>
                    <source src="${link}" type='video/mp4; codecs="avc1.42E01E, mp4a.40.2"'/>
                </c:if>
            </c:if>
        </c:forEach>
    </video>
    <c:if test="${not empty model.dataChapters && fn:trim(model.dataChapters) != ''}">
        <div class="chapter-list">
            <div class="header">
                <p><span class="text">${vdChapterTitle}</span><em class="menu-toggle-icon"></em></p>
            </div>
            <div class="body"></div>
        </div>
    </c:if>
    <c:if test="${not empty model.vdDescription && fn:trim(model.vdDescription) != ''}">
        ${model.vdDescription}
    </c:if>
    <c:if test="${not empty model.vdButtonText && fn:trim(model.vdButtonText) != ''}">
        <a href="${model.vdButtonUrlLink}" class="cta-link" >${model.vdButtonText}</a>
    </c:if>
</div>

