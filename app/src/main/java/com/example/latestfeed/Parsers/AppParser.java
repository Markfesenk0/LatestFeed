package com.example.latestfeed.Parsers;

import com.example.latestfeed.Entities.App;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class AppParser {
    private static ArrayList<App> apps;

    public AppParser() {
        apps = new ArrayList<>();
    }

    public static ArrayList<App> getApps() {
        return apps;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        App currentRecord = new App();
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
                        if ("entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new App();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if ("entry".equalsIgnoreCase(tagName)) {
                                apps.add(currentRecord);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                currentRecord.setTitle(textValue);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(textValue);
                            } else if ("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(textValue);
                            } else if ("price".equalsIgnoreCase(tagName)) {
                                try {
                                    Double appPrice = Double.parseDouble(textValue.substring(1, textValue.length()));
                                    currentRecord.setPrice(textValue);
                                } catch (NumberFormatException e) {
                                    currentRecord.setPrice("Free");
                                }
                            } else if ("image".equalsIgnoreCase(tagName)) {
                                currentRecord.setImgUrl(textValue);
                            }
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
