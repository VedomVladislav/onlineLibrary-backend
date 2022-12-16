package by.vedom.library.business.util;

import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class Logger {

    public static void debugMethodName(String text) {
        System.out.println();
        System.out.println();
        log.log(Level.INFO, text);
    }

}
