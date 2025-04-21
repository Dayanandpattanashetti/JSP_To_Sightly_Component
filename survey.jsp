<%@include file="/apps/diabetes-patient/common/includes/baseglobal.jsp" %>
<dpcommon:adaptTo adaptable="${slingRequest}" adaptTo="com.nni.aem.dp.common.core.ui.controllers.Survey" var="model"/>

<c:set var='uui' value="<%=java.util.UUID.randomUUID().toString() %>" scope="request"/>

<c:if test="${fn:length(model.surveyQuestions) gt 0}">
    <c:set var="surveyConfig" value="survey_options=${fn:length(model.surveyQuestions)};"/>
</c:if>
<c:if test="${not empty model.redirectPath}">
    <c:set var="surveyConfig" value="${surveyConfig}redirect_path='${model.redirectPath}';"/>
</c:if>

<div id="surveyAppId-${uui}" ng-controller="surveyController" ng-cloak class="ng-cloak">
    <div data-ng-init="${surveyConfig}"></div>

    <!-- start the survey -->
    <div ng-init="initSurveyApp()"></div>
    <div class="row">
        <div class="twelve columns"><c:out escapeXml="false" value="${model.headerRteContent}"/></div>
    </div>

    <form name="${formName}" autocomplete="off"  class="form-data" autocomplete="off" novalidate>
    <div class="cc-survey" ng-hide="ctrl.isFinished">
        <div>
            <div class="cc-survey__dot">........................................................................</div>
            <ul class="cc-survey__steps">
                <li class="cc-survey__step cc-survey__step--first">${model.startLabel}</li>
                <li ng-repeat="i in range() track by $index"
                    ng-class="{'cc-survey__step--selected': ( ($index+1) === current_step),
                       'cc-survey__step--completed': ( ($index+1) < current_step)}"
                    class="cc-survey__step">
                    <i class="dot"></i>
                </li>
                <li class="cc-survey__step cc-survey__step--last">${model.finishLabel}</li>
            </ul>
        </div>
        <div>
            <div class="cc-survey__q-and-a">
                <div ng-switch on="question_step_name">
                    <c:forEach var="question" varStatus="vs" items="${model.surveyQuestions}">
                    <div ng-switch-when="question_step${vs.count}">
                        <div class="cc-survey__question">
                                ${question.question}
                        </div>
                        <div class="cc-survey__a-and-h row">
                            <div class="five columns">
                                <div class="cc-survey__answers">
                                    <c:forEach var="surveyAnswer" varStatus="vsa" items="${question.answers}">
                                        <div class="cc-survey__answer"
                                             ng-click="selectAnswer($event, 'answer_hint_${vs.index}-${vsa.index}', ${vsa.index+1})"
                                             ng-class="{'cc-survey__answer--selected': currentAnswer(${vsa.index+1})}">
                                            <div class="cc-survey__answer__icon"><i class="fa"
                                                                                    ng-class="{ 'fa-check-circle': currentAnswer(${vsa.index+1}), 'blank-circle' : !currentAnswer(${vsa.index+1}) }"></i></div>
                                            <div class="cc-survey__answer__text"
                                                 name="qnt${vs.count}-ans${vsa.index}">${surveyAnswer}</div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="seven columns">
                                <c:if test="${fn:length(question.tips) gt 0}">
                                    <div ng-switch on="survery_answer_hint" class="cc-survey__hint__background">
                                        <c:forEach var="surveyAnswerHint" varStatus="vsah" items="${question.tips}">
                                            <div class="cc-survey__hint" ng-switch-when="answer_hint_${vs.index}-${vsah.index}">
                                                <div class="cc-survey__hint__icon hidden-sm hidden-xs">
                                                    <img src="${model.imageDesktopSrc}"/>
                                                </div>
                                                <div class="cc-survey__hint__content">
                                                    <div class="cc-survey__hint__title">
                                                        Did you know?
                                                    </div>
                                                    <div class="cc-survey__hint__text">
                                                        <c:out escapeXml="false" value="${surveyAnswerHint}"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                </div>
                <div class="row cc-survey__footer">
                    <div class="twelve columns text-right" ng-show="current_step < survey_options">
                        <button class="btn cc-survey__btn cc-survey__btn-primary"
                                ng-click="nextStep()"
                                ng-disabled="is_valid_next_button">${model.nextButtonLabel}</button>
                    </div>
                    <div class="twelve columns text-right" ng-show="current_step === survey_options">
                        <button class="btn cc-survey__btn cc-survey__btn-primary"
                                ng-click="submitSurvey()"
                                ng-disabled="is_valid_next_button">${model.finishButtonLabel}</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </form>
    <!-- end survey -->
</div>
