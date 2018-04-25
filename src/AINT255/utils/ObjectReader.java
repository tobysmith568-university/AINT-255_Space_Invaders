/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AINT255.utils;

import AINT255.utils.Serial;
import org.jdom.Element;

/**
 * Created by IntelliJ IDEA.
 * User: sml
 * Date: 06-Aug-2004
 * Time: 09:22:41
 * To change this template use Options | File Templates.
 */
public interface ObjectReader extends Serial {
    public Object read(Element xob);
}

