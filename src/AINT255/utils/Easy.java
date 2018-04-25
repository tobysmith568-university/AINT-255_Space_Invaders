/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AINT255.utils;


import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import org.jdom.Document;
import org.jdom.Element;

import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 08-Nov-2004
 * Time: 15:02:11
 * To change this template use Options | File Templates.
 */
public class Easy {
    public static void save(Object ob, String filename) {
        try {
            ObjectWriter writer = new SimpleWriter();
            Element el = writer.write(ob);
            XMLOutputter out = new XMLOutputter(); // ("  ", true);
            FileWriter file = new FileWriter(filename);
            out.output(el, file);
            file.close();
            System.out.println("Saved object to " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object load(String filename) {
        try {
            SAXBuilder builder = new SAXBuilder();
            InputStream is = new FileInputStream(filename);
            Document doc = builder.build(is);
            Element el = doc.getRootElement();
            ObjectReader reader = new SimpleReader();
            return reader.read(el);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}