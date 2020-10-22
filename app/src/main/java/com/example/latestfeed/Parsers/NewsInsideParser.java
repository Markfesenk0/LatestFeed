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

public class NewsInsideParser {
    private static String body;
    public NewsInsideParser() {
        body = "";
    }

    public static String getBody() {
        return body;
    }

    public String parse(String xmlData) {
        String textValue = "";
        int index1 = 0;
        int index2 = 0;
        while(xmlData.lastIndexOf("</p>") != index2) {
            index1 = xmlData.indexOf("<p>", index1) + "<p>".length();
            index2 = xmlData.indexOf("</p>", index1);
            textValue = xmlData.substring(index1, index2);
            body = body + textValue + '\n';
        }
        return body;
    }
}
