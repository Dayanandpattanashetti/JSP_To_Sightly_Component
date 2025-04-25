package com.nni.aem.brandb.common.core.ui.controllers;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Model(adaptables = SlingHttpServletRequest.class)
public class AddToCalendar {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddToCalendar.class);

    private String calLinkType;
    private String calLinkLabel;
    private String calReminderTitle;
    private String calReminderDescription;
    private String calReminderLocation;
    private String calWindowTitle;
    private String calBodyText;
    private String calStartDate;
    private int calOccurrences;
    private String calFrequency;
    private String calImagePath;
    private String calImageAltText;
    private String decorativeImage;
    private String calEndAfter;

    private String[] typeCalendarItems;
    private String[] labelCalendarItems;
    private String[] urlLinkCalendarItems;

    private String startDateLabel;
    private String startTimeLabel;
    private String invalidDateMessage;

    @Inject
    private Resource resource;

    @PostConstruct
    private void initialize() {
        ValueMap properties = resource.adaptTo(ValueMap.class);

        calLinkType = properties.get("calLinkType", String.class);
        calLinkLabel = properties.get("calLinkLabel", String.class);
        calReminderTitle = properties.get("calReminderTitle", String.class);
        calReminderDescription = properties.get("calReminderDescription", String.class)!=null?
                properties.get("calReminderDescription", String.class) : "";
        calReminderLocation = properties.get("calReminderLocation", StringUtils.EMPTY);
        calWindowTitle = properties.get("calWindowTitle", String.class);
        calBodyText = properties.get("calBodyText", String.class);
        calFrequency = properties.get("calFrequency", String.class);
        Integer eventOccurrences = properties.get("calOccurrences", Integer.class);
        calImagePath = properties.get("calImagePath", String.class);
        calImageAltText = properties.get("calImageAltText", String.class);
        decorativeImage = properties.get("decorativeImage", String.class);
        calEndAfter = properties.get("calEndAfter", String.class);
        startDateLabel = properties.get("startDateLabel", StringUtils.EMPTY);
        startTimeLabel = properties.get("startTimeLabel", StringUtils.EMPTY);
        invalidDateMessage = properties.get("invalidDateMessage", StringUtils.EMPTY);
        calStartDate = null;

        if(eventOccurrences!=null){
            calOccurrences = eventOccurrences.intValue();
        }else{
            calOccurrences = 1; //default value
        }
        boolean withEndDate = (calEndAfter!=null && "YES".compareTo(calEndAfter)==0);

        Date startDate = setupStartDate();
        Date endDateVCalendar = setupEndDateVCalendar(startDate);
        Date endDate = setupEndDate(startDate);

        String startDateFormatted = getFormattedDate(startDate);
        String endDateFormatted = getFormattedDate(endDate);
        String endDateVCalFormatted = getFormattedDate(endDateVCalendar);

        typeCalendarItems = properties.get("typeCalendarItems", String[].class);
        labelCalendarItems = properties.get("labelCalendarItems", String[].class);

        if(typeCalendarItems!=null && typeCalendarItems.length>0){
            //create new array of url items
            urlLinkCalendarItems = new String[typeCalendarItems.length];

            for (int index = 0; index < typeCalendarItems.length; index++) {
                StringBuilder bufferCalendar = new StringBuilder();
                String linkCalendar = "#";
                String type = typeCalendarItems[index];
                try {
                    if ("google".compareTo(type) == 0) {
                        String recurEncoded = "RRULE:FREQ="+calFrequency;
                        if(withEndDate) {
                            recurEncoded += ";COUNT=" + calOccurrences;
                        }

                        bufferCalendar.append("https://www.google.com/calendar/render?action=TEMPLATE&text=%s");
                        bufferCalendar.append("&dates=").append(startDateFormatted).append("/").append(endDateVCalFormatted);
                        bufferCalendar.append("&details=%s");
                        bufferCalendar.append("&location=%s");
                        bufferCalendar.append("&sprop=&sprop=name");

                        linkCalendar = String.format(bufferCalendar.toString(), URLEncoder.encode(calReminderTitle, "UTF-8"),
                                URLEncoder.encode(calReminderDescription, "UTF-8"),
                                URLEncoder.encode(calReminderLocation, "UTF-8"))+
                                "&recur=" + URLEncoder.encode(recurEncoded, "UTF-8");
                    } else if ("yahoo".compareTo(type) == 0) {
                        bufferCalendar.append("http://calendar.yahoo.com/?v=60&view=d&type=20&title=%s");
                        bufferCalendar.append("&st=").append(startDateFormatted);
                        bufferCalendar.append("&dur=0030"); //duration in HHMM
                        bufferCalendar.append("&desc=%s");
                        bufferCalendar.append("&in_loc=%s");

                        linkCalendar = String.format(bufferCalendar.toString(), URLEncoder.encode(calReminderTitle, "UTF-8"),
                                URLEncoder.encode(calReminderDescription, "UTF-8"),
                                URLEncoder.encode(calReminderLocation, "UTF-8"));

                    } else if ("ical".compareTo(type) == 0) {
                        linkCalendar =  "data:text/calendar;charset=utf8," + getVCardCalendar(startDateFormatted, endDateVCalFormatted, withEndDate);
                    } else if ("outlook".compareTo(type) == 0) {
                        linkCalendar = "data:text/calendar;charset=utf8," + getVCardCalendar(startDateFormatted, endDateVCalFormatted, withEndDate);
                    }
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("EncodingException Calendar: " + e.getMessage());
                }
                urlLinkCalendarItems[index] = linkCalendar;
            }
        }
    }

    protected String getVCardCalendar(String st, String et, boolean endDate){
        StringBuilder vCardBuffer = new StringBuilder();
        try {
            String newLine = URLEncoder.encode("\n", "UTF-8");
            vCardBuffer.append("BEGIN:VCALENDAR").append(newLine);
            vCardBuffer.append("VERSION:2.0").append(newLine);
            vCardBuffer.append("BEGIN:VEVENT").append(newLine);
            vCardBuffer.append("DTSTART;TZID=").append(URLEncoder.encode("\"", "UTF-8")).append("(UTC-08:00) Pacific Time (US & Canada)");
            vCardBuffer.append(URLEncoder.encode("\"", "UTF-8")).append(":").append(st).append(newLine);
            vCardBuffer.append("DTEND;TZID=").append(URLEncoder.encode("\"", "UTF-8")).append("(UTC-08:00) Pacific Time (US & Canada)");
            vCardBuffer.append(URLEncoder.encode("\"", "UTF-8")).append(":").append(et).append(newLine);
            vCardBuffer.append("RRULE:FREQ=").append(calFrequency);
            if(endDate) {
                vCardBuffer.append(";COUNT=").append(calOccurrences).append(newLine);
            }else{
                vCardBuffer.append(newLine);
            }
            vCardBuffer.append("SUMMARY:").append(calReminderTitle).append(newLine);
            vCardBuffer.append("DESCRIPTION:").append( calReminderDescription ).append(newLine);
            vCardBuffer.append("LOCATION:").append( calReminderLocation ).append(newLine);
            vCardBuffer.append("BEGIN:VALARM").append(newLine);
            vCardBuffer.append("TRIGGER:-PT10M").append(newLine);
            vCardBuffer.append("ACTION:DISPLAY").append(newLine);
            vCardBuffer.append("DESCRIPTION:Reminder").append(newLine);
            vCardBuffer.append("END:VALARM").append(newLine);
            vCardBuffer.append("END:VEVENT").append(newLine);
            vCardBuffer.append("END:VCALENDAR");
        }catch(UnsupportedEncodingException e) {
            LOGGER.error("EncodingException Calendar: " + e.getMessage());
        }
        return vCardBuffer.toString();
    }

    protected Date setupStartDate(){
        Date d = new java.util.Date(); //current date
        if(calStartDate!=null){
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            try {
                d = dateFormat.parse(calStartDate);
            }catch (ParseException e){
                LOGGER.error("Date format error: " + e.getMessage());
            }
        }
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(d);
        c.set(java.util.Calendar.HOUR_OF_DAY, 7);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND,0);
        return c.getTime();
    }

    protected Date setupEndDate(Date aStartDate){
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(aStartDate);
        if(calFrequency!=null){
            if("DAILY".compareTo(calFrequency)==0){
                c.add(java.util.Calendar.DATE, calOccurrences);//add in days
            }else if("WEEKLY".compareTo(calFrequency)==0){
                c.add(java.util.Calendar.WEEK_OF_YEAR, calOccurrences);//add in weeks
            }else if("MONTHLY".compareTo(calFrequency)==0){
                c.add(java.util.Calendar.MONTH, calOccurrences);//add in months
            }
        }
        return c.getTime();
    }

    protected Date setupEndDateVCalendar(Date aStartDate){
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(aStartDate);
        c.add(Calendar.MINUTE, 30);
        return c.getTime();
    }

    protected String getFormattedDate(Date d){
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyyMMdd'T'HHmmss", Locale.US);
        /*format.setTimeZone(TimeZone.getTimeZone("UTC"));*/
        return format.format(d);
    }

    public String getCalLinkType() {
        return calLinkType;
    }

    public String getCalLinkLabel() {
        return calLinkLabel;
    }

    public String getCalReminderTitle() {
        return calReminderTitle;
    }

    public String getCalReminderLocation() {
        return calReminderLocation;
    }

    public String getCalWindowTitle() {
        return calWindowTitle;
    }

    public String[] getTypeCalendarItems() {
        return typeCalendarItems;
    }

    public String[] getLabelCalendarItems() {
        return labelCalendarItems;
    }

    public String[] getUrlLinkCalendarItems() {
        return urlLinkCalendarItems;
    }

    public String getCalBodyText() {
        return calBodyText;
    }

    public String getCalImagePath() {
        return calImagePath;
    }

    public String getCalImageAltText() {
        return calImageAltText;
    }

    public String getDecorativeImage() {
        return decorativeImage;
    }

    public String getCalReminderDescription() {
        return calReminderDescription;
    }

    public String getStartDateLabel() {
        return startDateLabel;
    }

    public String getStartTimeLabel() {
        return startTimeLabel;
    }

    public String getInvalidDateMessage() {
        return invalidDateMessage;
    }
}
