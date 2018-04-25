/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AINT255.utils;

import AINT255.utils.Serial;
import org.jdom.Element;

public interface ObjectWriter extends Serial {
    public Element write(Object o);
}
