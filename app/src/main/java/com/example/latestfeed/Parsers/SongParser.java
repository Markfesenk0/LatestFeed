package com.example.latestfeed.Parsers;

import com.example.latestfeed.Entities.Song;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.bind.SchemaOutputResolver;

public class SongParser {
    private static ArrayList<Song> songs;

    public SongParser() {
        songs = new ArrayList<>();
    }

    public static ArrayList<Song> getSongs() {
        return songs;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        Song currentRecord = new Song();
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
                            currentRecord = new Song();
                        } else if ("link".equalsIgnoreCase(tagName)) {
                            String link = xpp.getAttributeValue(null, "href");
                            String title = xpp.getAttributeValue(null, "title");
                            if (title != null && title.equalsIgnoreCase("preview")) {
                                currentRecord.setPreviewUrl(link);
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if ("entry".equalsIgnoreCase(tagName)) {
                                songs.add(currentRecord);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                currentRecord.setTitle(textValue);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(textValue);
                            } else if ("category".equalsIgnoreCase(tagName)) {
                                String songCategory = xpp.getAttributeValue(null, "term");
                                if (songCategory != null) {
                                    currentRecord.setCategory(songCategory);
                                }
                            } else if ("image".equalsIgnoreCase(tagName)) {
                                currentRecord.setImgUrl(textValue);
                            } else if ("link".equalsIgnoreCase(tagName)) {
                                currentRecord.setSongUrl(textValue);
                                String href = xpp.getAttributeValue(null, "href");
                                if (href != null) {
                                    currentRecord.setSongUrl(href);
                                }
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
