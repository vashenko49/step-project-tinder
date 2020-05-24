package com.tinder;


import com.tinder.start.ServerUtil;

import javax.swing.*;

public class JettyServer extends JFrame {
    public static final String APPLICATION_NAME = "step-project-tinder";


    public static void main(String[] args) throws Exception {
        System.out.println("Starting Server ..");
        ServerUtil.getInstance();
        System.out.println("Server is started");
    }

}
