package com.example.latestfeed.Parsers;

import android.util.Log;

import com.example.latestfeed.Entities.News;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsParser {
    private static ArrayList<News> newsList;

    public NewsParser() {
        newsList = new ArrayList<>();
    }

    public static ArrayList<News> getNews() {
        return newsList;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        News currentRecord = new News();
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("item".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new News();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if ("item".equalsIgnoreCase(tagName)) {
                                newsList.add(currentRecord);
                                inEntry = false;
                            } else if ("title".equalsIgnoreCase(tagName)) {
                                currentRecord.setTitle(textValue);
                            } else if ("photographer".equalsIgnoreCase(tagName)) {
                                currentRecord.setCreator(textValue);
                            } else if ("pubDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setPublishDate(textValue);
                            } else if ("link".equalsIgnoreCase(tagName)) {
                                currentRecord.setNewsUrl(textValue);
                            }  else if ("image624X383".equalsIgnoreCase(tagName)) {
                                currentRecord.setImgUrl(textValue);
                            } else if ("shortDescription".equalsIgnoreCase(tagName)) {
                                currentRecord.setBody(textValue);
                            }

//                            else if ("description".equalsIgnoreCase(tagName)) {
//                                currentRecord.setBody(textValue);
//                                int descIndex1 = textValue.indexOf("<p>") + 3;
//                                descIndex1 = textValue.indexOf("<p>", descIndex1) + 3;
//                                int descIndex2 = textValue.indexOf("</p>", descIndex1) - 1;
//                                currentRecord.setBody(textValue.substring(descIndex1, descIndex2));
//                                int urlIndex1 = textValue.indexOf("src") + 5;
//                                String url = textValue.substring(urlIndex1, textValue.length());
//                                int urlIndex2 = url.indexOf('"');
//                                url = url.substring(0,urlIndex2);
//                                currentRecord.setImgUrl(url);
//                            }
                        }
                        break;

                    default:
                        //Nothing
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
