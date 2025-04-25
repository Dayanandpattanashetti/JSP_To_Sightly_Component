<%@include file="/apps/diabetes-patient/common/includes/baseglobal.jsp" %>
<dpcommon:adaptTo adaptable="${slingRequest}" adaptTo="com.nni.aem.brandb.common.core.ui.controllers.AddToCalendar" var="model"/>

<c:set var='uuidDay' value="<%=java.util.UUID.randomUUID().toString() %>" scope="request"/>
<c:set var='uuidMonth' value="<%=java.util.UUID.randomUUID().toString() %>" scope="request"/>
<c:set var='uuidYear' value="<%=java.util.UUID.randomUUID().toString() %>" scope="request"/>
<c:set var='uuidTime' value="<%=java.util.UUID.randomUUID().toString() %>" scope="request"/>
<c:set var='uuidApMeridiem' value="<%=java.util.UUID.randomUUID().toString() %>" scope="request"/>


<%
    pageContext.setAttribute("isEditMode", WCMMode.fromRequest(request) == WCMMode.EDIT, PageContext.REQUEST_SCOPE);
%>

<c:set var="linkColor" value="" />
<c:if test="${not empty model.calLinkLabel}">
    <c:set var="linkColor" value="color: ${properties.calendarTextFontColor} !important;" />
</c:if>

<c:set var="altText" value="" />
<c:if test="${!model.decorativeImage}">
    <c:set var="altText" value="${properties.calImageAltText}" />
</c:if>

<div class="addto-calendar component">
    <c:if test="${empty model.calLinkLabel && isEditMode}">
        <p><b>Please edit AddToCalendar Component</b></p>
    </c:if>
    <c:if test="${(empty altText && not empty model.calImagePath && !model.decorativeImage) && isEditMode}">
        <p><b>Please edit AddToCalendar Component - add missing alt text for image or set as decorative</b></p>
    </c:if>
    <c:if test="${not empty model.calLinkLabel && model.calLinkType eq 'BUTTON'}">
        <button class="btn btn-primary btn-lg calendar-btn">${fn:escapeXml(model.calLinkLabel)}</button>
    </c:if>
    <c:if test="${not empty model.calLinkLabel && model.calLinkType eq 'TEXT'}">
        <a class="calendar-btn" href="#">${fn:escapeXml(model.calLinkLabel)}</a>
    </c:if>

    <!-- Modal window stars -->
    <div class="stacked modal fade addto-calendar" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
        <div class="modal-dialog" ${modalStyle}>
            <div class="modal-content">

                <div class="modal-header">
                    <c:if test="${not empty model.calWindowTitle}">
                        <h5 class="modal-title">${model.calWindowTitle}</h2>
                    </c:if>

                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                </div>
                
                <div class="modal-body">
                    <%--contento to be shown--%>
                    <c:if test="${empty model.typeCalendarItems}">
                        <p><b>Please edit AddToCalendar Component</b></p>
                    </c:if>

                    <c:if test="${not empty model.typeCalendarItems}">
                        <div class="row">
                            <c:if test="${not empty model.calImagePath}">
                                <div class="col-2 d-none d-sm-block">
                                    <img class="mx-auto mw-100" src="${model.calImagePath}" alt="${altText}" />
                                </div>
                            </c:if>
                            <c:if test="${not empty model.calBodyText}">
                                <div class="col-10"><c:out escapeXml="false" value="${model.calBodyText}"/></div>
                            </c:if>
                        </div>

                        <div class="row">
                            <c:if test="${not empty model.calImagePath}">
                                <div class="col-2 d-none d-sm-block"></div>
                            </c:if>

                            <div class="col-12 col-sm-6 col-md-5 pr-sm-0">
                                <label>${model.startDateLabel}</label>
                                <label for="${uuidDay}" class="visually-hidden">${properties.dayFieldLabel}</label>
                                <input id="${uuidDay}" type="number" placeholder="DD" class="input-xsmall" step="1" min="1" max="31" name="reminder-input-dd" />
                                <label for="${uuidMonth}" class="visually-hidden">${properties.monthFieldLabel}</label>
                                <input id="${uuidMonth}" type="number" placeholder="MM" class="input-xsmall" step="1" min="1" max="12" name="reminder-input-mm" />
                                <label for="${uuidYear}" class="visually-hidden">${properties.yearFieldLabel}</label>
                                <input id="${uuidYear}" type="number" placeholder="YYYY" class="input-small" step="1" maxlength="4" name="reminder-input-yyyy" />
                                <label name="reminder-alert" class="alert-danger cal-reminder-alert">${model.invalidDateMessage}</label>
                            </div>

                            <div class="col-12 col-sm-4 col-md-5 pl-sm-0">
                                <label>${model.startTimeLabel}</label>
                                <label for="${uuidTime}" class="visually-hidden">${properties.timeFieldLabel}</label>
                                <select id="${uuidTime}" name="hourSelector" autocomplete="off">
                                </select>
                                <label for="${uuidApMeridiem}" class="visually-hidden">${properties.apMeridiemFieldLabel}</label>
                                <select id="${uuidApMeridiem}" name="ap-meridiem" autocomplete="off">
                                    <option value="am" selected>AM</option>
                                    <option value="pm">PM</option>
                                </select>
                            </div>
                        </div>

                        <div class="row cal-links-container">
                            <c:if test="${not empty model.calImagePath}">
                                <div class="col-2 d-none d-sm-block"></div>
                            </c:if>
                            
                            <div name="links-container" class="col-10">
                                <c:forEach items="${model.typeCalendarItems}" var="item" varStatus="itemStatus">
                                    <span><a style="${linkColor}" href="${model.urlLinkCalendarItems[itemStatus.index]}" target="_blank" class="cal-a-underline">${fn:escapeXml(model.labelCalendarItems[itemStatus.index])}</a></span>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                    <%--contento to be shown--%>

                </div>

            </div>
        </div>
    </div>
    <!-- Modal window ends -->

</div>
<%
    pageContext.removeAttribute("isEditMode",PageContext.REQUEST_SCOPE);
%>
